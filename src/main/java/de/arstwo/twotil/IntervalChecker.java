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
 * <p>
 * Timer checks are performed atomically to ensure that if multiple threads attempt to reset the timer, only one will succeed and trigger due task execution.
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
		return every(Duration.of(amount, unit));
	}

	/**
	 * Creates a new IntervalChecker with the specified time interval. The returned timer is marked as "due".
	 *
	 * @param duration interval between actions.
	 * @return An IntervalChecker set to the specified time interval.
	 * @see ChronoUnit
	 */
	public static IntervalChecker every(final Duration duration) {
		if (duration.isPositive()) {
			return new IntervalChecker(duration);
		} else {
			throw new IllegalArgumentException("Amount of interval must be > 0");
		}
	}

	final Duration intervalDuration;
	final AtomicReference<Instant> lastCheck = new AtomicReference(null);
	Runnable taskOnDue = null;
	Runnable taskOnNotDue = null;

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
	 * Will not execute any set tasks.
	 *
	 * @return true if the time had expired, otherwise false.
	 */
	public boolean update() {
		Instant checkTime;
		boolean due;

		do {
			checkTime = lastCheck.get();
			due = isDue(checkTime);
		} while (due && !lastCheck.compareAndSet(checkTime, Instant.now()));

		return due;
	}

	/**
	 * If the timer is due it is reset and ifDue is executed, otherwise ifNotDue is executed without a timer reset.
	 *
	 * @param onDue task to execute if due, or null.
	 * @param onNotDue task to execute if not due, or null.
	 * @return true if the timer was due, otherwise false.
	 */
	public boolean executeTasks(final Runnable onDue, final Runnable onNotDue) {
		if (update()) {
			if (onDue != null) {
				onDue.run();
			}
			return true;
		} else {
			if (onNotDue != null) {
				onNotDue.run();
			}
			return false;
		}
	}

	/**
	 * Checks (and possibly resets) the timer, then performs the previously set tasks (if any) accordingly.
	 *
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean execute() {
		return IntervalChecker.this.executeTasks(this.taskOnDue, this.taskOnNotDue);
	}

	/**
	 * Specifies a fixed task to run on execute.. checks if the timer is due.
	 *
	 * @param task any runnable.
	 * @return this, for convenience.
	 */
	public IntervalChecker whenDue(final Runnable task) {
		this.taskOnDue = task;
		return this;
	}

	/**
	 * Specifies a fixed task to run on execute... checks if the timer is not due.
	 *
	 * @param task any runnable.
	 * @return this, for convenience.
	 */
	public IntervalChecker whenNotDue(final Runnable task) {
		this.taskOnNotDue = task;
		return this;
	}

	/**
	 * Executes the previously set task if the interval has expired. In that case, also resets the timer.
	 *
	 * Will not run any not-due task.
	 *
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean executeIfDue() {
		return executeTaskIfDue(this.taskOnDue);
	}

	/**
	 * Executes the specified operation if the interval has expired. In that case, also resets the timer.
	 *
	 * Will not run any not-due task.
	 *
	 * @param task The task to be performed.
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean executeTaskIfDue(final Runnable task) {
		return IntervalChecker.this.executeTasks(task, null);
	}

	/**
	 * Executes the previously set task if the interval has not expired.
	 *
	 * Will not run any due task.
	 *
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean executeIfNotDue() {
		return executeTaskIfNotDue(this.taskOnNotDue);
	}

	/**
	 * Checks the timer and executes the given task if not expired, otherwise execute the due task (if set) and resets the timer.
	 *
	 * Will not run any due task.
	 *
	 * @param task The task to be performed if not due.
	 * @return true if the interval had expired, otherwise false.
	 */
	public boolean executeTaskIfNotDue(final Runnable task) {
		return IntervalChecker.this.executeTasks(null, task);
	}

	/**
	 * Checks whether or not this timer is due right now without excecuting any tasks.
	 *
	 * @return true if it is due right now, otherwise false.
	 */
	public boolean isDue() {
		return isDue(this.lastCheck.get());
	}

	/**
	 * Forces a new time interval without calling any tasks.
	 *
	 * @return this, for convenience.
	 */
	public IntervalChecker restartTimer() {
		this.lastCheck.set(Instant.now());
		return this;
	}

	/**
	 * Forces the timer to indicate that the interval time has expired at the next check.
	 *
	 * @return this, for convenience.
	 */
	public IntervalChecker forceDue() {
		this.lastCheck.set(null);
		return this;
	}
}
