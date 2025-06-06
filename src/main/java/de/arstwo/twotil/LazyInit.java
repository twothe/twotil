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

import java.util.function.Supplier;

/**
 * Initializes something on the first use.
 * <p>
 * Confirmation of initialization is done by replacing the accessor function. This way the JIT can easily determine that the resulting accessor is empty
 * boilerplate that can be inlined.
 */
public class LazyInit<T> implements Supplier<T> {

	private volatile boolean initialized = false;
	private Supplier<T> getter;

	public LazyInit(final Supplier<T> initializer) {
		this.getter = () -> {
			synchronized (this) {
				if (!initialized) {
					final T v = initializer.get();
					getter = () -> v;
					initialized = true;
				}
				return getter.get();
			}
		};
	}

	@Override
	public T get() {
		return getter.get();
	}
}
