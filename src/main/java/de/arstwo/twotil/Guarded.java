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

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functional approach of thread-safe lock access to a given target.
 */
public class Guarded<T> {

	final T target;
	final Lock lock;

        /**
         * Creates a guarded wrapper around the given target.
         *
         * @param target object to protect
         * @param fair   if {@code true} use a fair lock
         */
        public Guarded(final T target, final boolean fair) {
                this.target = target;
                this.lock = new ReentrantLock(fair);
        }

        /**
         * Creates a guarded wrapper using a non-fair lock.
         *
         * @param target object to protect
         */
        public Guarded(final T target) {
                this(target, false);
        }

	/**
	 * Accesses the target and supplies it to the given consumer when ready.
	 *
	 * @param action any consumer for the target
	 */
	public void access(final Consumer<T> action) {
		lock.lock();
		try {
			action.accept(target);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Accesses the target, supplies it to the given function when ready and returns the computed result.
	 *
	 * @param <R> any result type of the function given
	 * @param action the function to apply to the target once ready
	 * @return the result of the function
	 */
	public <R> R process(final Function<T, R> action) {
		lock.lock();
		try {
			return action.apply(target);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Try immediate access to the target and forward it to the given consumer.
	 *
	 * @param action any consumer for the target
	 * @return true if the target could be accessed immediately, false otherwise.
	 */
	public boolean tryAccess(final Consumer<T> action) {
		if (lock.tryLock()) {
			try {
				action.accept(target);
			} finally {
				lock.unlock();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Try to access the target immediately, supplies it to the given function when ready and possibly returns the computed result.
	 *
	 * @param <R> any result type of the function given
	 * @param action the function to apply to the target if ready
	 * @return an optional result of the function applied if the target was ready, an empty optional if it was not ready.
	 */
	public <R> Optional<R> tryProcess(final Function<T, R> action) {
		if (lock.tryLock()) {
			try {
				return Optional.ofNullable(action.apply(target));
			} finally {
				lock.unlock();
			}
		} else {
			return Optional.empty();
		}
	}
}
