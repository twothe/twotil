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
package de.arstwo.twotil.math;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class MathUtilNGTest {

	@Test
	public void testAboutEqual() {
		assertTrue(MathUtil.aboutEqual(1.0f, 1.001f, 0.01f));
		assertFalse(MathUtil.aboutEqual(1.0f, 1.02f, 0.01f));
		assertTrue(MathUtil.aboutEqual(1.0, 1.0001, 0.01));
		assertFalse(MathUtil.aboutEqual(1.0, 1.1, 0.01));
	}

	@Test
	public void testWithinRange() {
		assertEquals(MathUtil.withinRange(5, 0, 10), Integer.valueOf(5));
		assertEquals(MathUtil.withinRange(-1, 0, 10), Integer.valueOf(0));
		assertEquals(MathUtil.withinRange(11, 0, 10), Integer.valueOf(10));
	}
}
