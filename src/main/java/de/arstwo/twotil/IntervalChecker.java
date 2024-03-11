/*
 * Copyright 2024 Stefan Feldbinder <sfeldbin@googlemail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.arstwo.twotil;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A utility class designed for managing and checking time intervals in a thread-safe manner.
 * <p>
 * This class simplifies the boilerplate code necessary to handle interval checking.
 * <p>
 * Usage example:
 * <pre>{@code
 *   IntervalChecker everyMinute = IntervalChecker.every(1, ChronoUnit.MINUTES);
 *   if (everyMinute.updateIfDue()) {
 *     // Do the task...
 *   }
 *   // alternatively:
 *   everyMinute.executeIfDue(myTask::run);
 * }
 * </pre>
 */
public class IntervalChecker {

	/**
	 * Creates a new IntervalChecker with the specified time interval. The returned timer is marked as "due".
	 *
	 * @param amount The amount of the specified unit.
	 * @param unit The time unit, usually {@link ChronoUnit ChronoUnit}.
	 * @return An IntervalChecker set to the specified time interval.
	 * @see ChronoUnit
	 */
	public static IntervalChecker every(final long amount, final TemporalUnit unit) {
		if (amount > 0) {
			return new IntervalChecker(Duration.of(amount, unit));
		} else {
			throw new IllegalArgumentException("Amount of interval must be > 0");
		}
	}

	final Duration intervalDuration;
	final AtomicReference<Instant> lastCheck = new AtomicReference(null);

	private IntervalChecker(final Duration intervalDuration) {
		this.intervalDuration = intervalDuration;
	}

	boolean isDue(final Instant checkTime) {
		return (checkTime == null)
						|| checkTime.plus(intervalDuration).isBefore(Instant.now());
	}

	/**
	 * Checks the interval and atomically resets the timer if the time has expired.
	 *
	 * @return true if the time had expired, otherwise false.
	 */
	public boolean updateIfDue() {
		Instant checkTime;

		do {
			checkTime = lastCheck.get();
		} while (isDue(checkTime) && !lastCheck.compareAndSet(checkTime, Instant.now()));

		return isDue(checkTime);
	}

	/**
	 * Executes the specified operation if the interval has expired. In that case, also resets the timer.
	 *
	 * @param task The task to be performed.
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean executeIfDue(final Runnable task) {
		if (updateIfDue()) {
			task.run();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Forces a new time interval. Typically when the task has been completed externally.
	 */
	public void forceChecked() {
		this.lastCheck.set(Instant.now());
	}

	/**
	 * Forces the timer to indicate that the interval time has expired at the next check.
	 */
	public void forceDue() {
		this.lastCheck.set(null);
	}
}
