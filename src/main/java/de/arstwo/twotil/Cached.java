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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Simple key->value caching functionality, for both as a class and functional usage.
 * <p>
 * Null and thread safe, results are cached indefinitely as long as the function is in scope.
 * <p>
 * Usage example:
 * <pre>{@code
 *   Cached<Long, Person> cache = new Cached(myDB::getByID);
 *   // ...
 *   Person person = cache.get(personID);
 * }
 * </pre>
 *
 * @author Stefan Feldbinder <sfeldbin@googlemail.com>
 * @param <KEY> any
 * @param <VALUE> any
 */
public class Cached<KEY, VALUE> implements Function<KEY, VALUE> {

	/**
	 * Convenience accessor for functional feeling.
	 *
	 * @param <IN> any
	 * @param <OUT> any
	 * @param source supplier for cache misses.
	 * @return A function that caches the result of the given source function.
	 */
	public static <IN, OUT> Function<IN, OUT> cached(final Function<IN, OUT> source) {
		return new Cached(source);
	}

	final Function<KEY, VALUE> source;
	final ConcurrentMap<KEY, Optional<VALUE>> cache = new ConcurrentHashMap<>();

	/**
	 * Creates a new cache with source as the supplier.
	 *
	 * @param source accessor to the data to cache.
	 */
	public Cached(final Function<KEY, VALUE> source) {
		this.source = source;
	}

	/**
	 * Gets a value from the cache or the source.
	 *
	 * @param key the key to look for.
	 * @return the value retrieved either from cache, or from the source.
	 */
	public VALUE get(final KEY key) {
		return cache.computeIfAbsent(key, source.andThen(Optional::ofNullable)).orElse(null);
	}

	/**
	 * Necessary for functional usage.
	 *
	 * @param key the key to look for.
	 * @return the value retrieved either from cache, or from the source.
	 */
	@Override
	public VALUE apply(final KEY key) {
		return get(key);
	}

}
