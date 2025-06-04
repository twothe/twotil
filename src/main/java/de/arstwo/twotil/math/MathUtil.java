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

/**
 * Various math-related content that fits nowhere else.
 */
public class MathUtil {

	public static final double TO_DEGREE_D = 180.0 / Math.PI;
	public static final float TO_DEGREE = (float) TO_DEGREE_D;
	public static final double TO_RADIANS_D = Math.PI / 180.0;
	public static final float TO_RADIANS = (float) TO_RADIANS_D;

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static byte abs(final byte x) {
		return x < 0 ? (byte) -x : x;
	}

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static short abs(final short x) {
		return x < 0 ? (short) -x : x;
	}

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static int abs(final int x) {
		return x < 0 ? -x : x;
	}

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static long abs(final long x) {
		return x < 0 ? -x : x;
	}

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static float abs(final float x) {
		return x < 0 ? -x : x;
	}

	/**
	 * Much faster but less generic abs implementation.
	 */
	public static double abs(final double x) {
		return x < 0 ? -x : x;
	}

	/**
	 * Checks if two floats are equal within the given tolerance.
	 *
	 * @param a any float
	 * @param b any float
	 * @param delta the maximum difference that is accepted as equal
	 * @return true if both values are equal within the given tolerance, false otherwise.
	 */
	public static boolean aboutEqual(final float a, final float b, final float delta) {
		return abs(b - a) <= delta;
	}

	/**
	 * Checks if two doubles are equal within the given tolerance.
	 *
	 * @param a any double
	 * @param b any double
	 * @param delta the maximum difference that is accepted as equal
	 * @return true if both values are equal within the given tolerance, false otherwise.
	 */
	public static boolean aboutEqual(final double a, final double b, final double delta) {
		return abs(b - a) <= delta;
	}

	/**
	 * Returns the given value clamped to the min/max valued specified.
	 *
	 * @param <T> any comparable
	 * @param value the value to check and return
	 * @param min minimum value to clamp to
	 * @param max maximum value to clamp to
	 * @return the value given, clamped to the specified boundaries.
	 */
	public static <T extends Comparable<T>> T withinRange(final T value, final T min, final T max) {
		return value.compareTo(min) < 0 ? min
						: value.compareTo(max) > 0 ? max
						: value;
	}

	/**
	 * Do not instantiate.
	 */
	private MathUtil() {
	}
}
