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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class Guarded<T> {

	final T target;
	final Lock lock;

	public Guarded(final T target, final boolean fair) {
		this.target = target;
		this.lock = new ReentrantLock(fair);
	}

	public Guarded(final T target) {
		this(target, false);
	}

	public void access(final Consumer<T> action) {
		lock.lock();
		try {
			action.accept(target);
		} finally {
			lock.unlock();
		}
	}

	public <R> R process(final Function<T, R> action) {
		lock.lock();
		try {
			return action.apply(target);
		} finally {
			lock.unlock();
		}
	}

	public void tryAccess(final Consumer<T> action) {
		if (lock.tryLock()) {
			try {
				action.accept(target);
			} finally {
				lock.unlock();
			}
		}
	}

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
