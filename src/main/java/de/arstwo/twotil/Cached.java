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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;

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
 * @param <K> any
 * @param <V> any
 */
public class Cached<K, V> implements Function<K, V> {

        /**
         * Returns a function that caches the results of the supplied function.
         *
         * @param <I> input type
         * @param <O> output type
         * @param source source function for cache misses
         * @return caching wrapper for {@code source}
         */
        public static <I, O> Function<I, O> cached(final Function<I, O> source) {
                return new Cached<>(source);
        }

	final Function<K, V> source;
	final ConcurrentMap<K, Optional<V>> cache = new ConcurrentHashMap<>();

	/**
	 * Creates a new cache with source as the supplier.
	 *
	 * @param source accessor to the data to cache.
	 */
	public Cached(final Function<K, V> source) {
		this.source = source;
	}

	/**
	 * Gets a value from the cache or the source.
	 *
	 * @param key the key to look for.
	 * @return the value retrieved either from cache, or from the source.
	 */
	public V get(final K key) {
		return cache.computeIfAbsent(key, source.andThen(Optional::ofNullable)).orElse(null);
	}

	/**
	 * Necessary for functional usage.
	 *
	 * @param key the key to look for.
	 * @return the value retrieved either from cache, or from the source.
	 */
	@Override
	public V apply(final K key) {
		return get(key);
	}

	/**
	 * Clears the cache.
	 */
	public void clear() {
		this.cache.clear();
	}

        /**
         * Removes entries that match the given predicate.
         *
         * @param filter predicate tested against each key/value pair
         */
        public void removeIf(final Predicate<? super Map.Entry<K, V>> filter) {
                this.cache.entrySet().removeIf(entry -> filter.test(Map.entry(entry.getKey(), entry.getValue().orElse(null))));
        }

	/**
	 * Replaces a cache entry.
	 *
	 * @param key entry key
	 * @param newValue new value to set
	 */
	public void replace(final K key, final V newValue) {
		this.cache.replace(key, Optional.ofNullable(newValue));
	}

	/**
	 * Replaces a cache entry if it matches the old value.
	 *
	 * @param key entry key
	 * @param oldValue expected old value
	 * @param newValue new value to set
	 * @return true if the entry was replaced, false otherwise, which usually indicates that the values did not match.
	 */
	public boolean replaceIf(final K key, final V oldValue, final V newValue) {
		return this.cache.replace(key, Optional.ofNullable(oldValue), Optional.ofNullable(newValue));
	}
}
