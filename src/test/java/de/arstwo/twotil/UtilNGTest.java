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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class UtilNGTest {

	@Test
	public void testIsBlankAndEmpty() {
		assertTrue(Util.isBlank(""));
		assertTrue(Util.isBlank("  "));
		assertFalse(Util.isBlank("x"));
		assertTrue(Util.isEmpty(new int[]{}));
		assertFalse(Util.isEmpty(new int[]{1}));
		assertTrue(Util.isBlank(Arrays.asList(" ")));
	}

	@Test
	public void testJoinNonBlank() {
		String r = Util.joinNonBlank(",", "a", " ", null, "b");
		assertEquals(r, "a,b");
	}

	@Test
	public void testFirstOrThrow() {
		try {
			Util.firstOrThrow(Objects::nonNull, () -> new RuntimeException("fail"), (String) null);
			fail("no throw");
		} catch (RuntimeException e) {
		}
		assertEquals(Util.firstOrThrow(Objects::nonNull, RuntimeException::new, null, "x"), "x");
	}

	@Test
	public void testInverseAndGroup() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "a");
		map.put(2, "a");
		Map<String, List<Integer>> inv = Util.inverseMap(map);
		assertEquals(inv.get("a"), Arrays.asList(1, 2));

		List<String> list = Arrays.asList("x", "y", "x");
		Map<String, List<String>> grouped = Util.groupList(list, v -> v);
		assertEquals(grouped.get("x"), Arrays.asList("x", "x"));
	}

	@Test
	public void testGroupListSorted() {
		List<String> list = Arrays.asList("b", "a", "b");
		Map<String, NavigableSet<String>> grouped = Util.groupListSorted(list, v -> v, Comparator.naturalOrder());
		assertEquals(grouped.get("b").first(), "b");
		assertEquals(grouped.get("b").size(), 1);
	}

	@Test
	public void testCompareDates() {
		class D {

			Date d;

			D(Date d) {
				this.d = d;
			}
		}
		List<D> l = Arrays.asList(new D(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())),
						new D(Date.from(LocalDate.of(2019, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		l.sort(Util.compareDatesASC(x -> x.d));
		assertTrue(l.get(0).d.before(l.get(1).d));
		l.sort(Util.compareDatesDESC(x -> x.d));
		assertTrue(l.get(0).d.after(l.get(1).d));
	}
}
