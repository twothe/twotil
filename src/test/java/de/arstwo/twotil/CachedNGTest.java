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

import java.util.concurrent.atomic.AtomicInteger;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class CachedNGTest {

	@Test
	public void testGetCachesResults() {
		AtomicInteger counter = new AtomicInteger();
		Cached<String, Integer> cache = new Cached<>(k -> counter.incrementAndGet());
		assertEquals(cache.get("a"), Integer.valueOf(1));
		assertEquals(cache.get("a"), Integer.valueOf(1));
		assertEquals(counter.get(), 1);
	}

	@Test
	public void testClearAndReplace() {
		AtomicInteger counter = new AtomicInteger();
		Cached<String, Integer> cache = new Cached<>(k -> counter.incrementAndGet());
		assertEquals(cache.get("a"), Integer.valueOf(1));
		cache.replace("a", 42);
		assertEquals(cache.get("a"), Integer.valueOf(42));
		cache.clear();
		assertEquals(cache.get("b"), Integer.valueOf(2));
	}

	@Test
	public void testRemoveIfAndReplaceIf() {
		AtomicInteger counter = new AtomicInteger();
		Cached<String, Integer> cache = new Cached<>(k -> counter.incrementAndGet());
		assertEquals(cache.get("a"), Integer.valueOf(1));
		cache.removeIf(e -> e.getKey().equals("a"));
		assertEquals(cache.get("a"), Integer.valueOf(2));
		assertTrue(cache.replaceIf("a", 2, 3));
		assertEquals(cache.get("a"), Integer.valueOf(3));
		assertFalse(cache.replaceIf("a", 2, 4));
	}
}
