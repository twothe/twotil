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
 * A helper class that stretches a given SimplexNoise on 2 or 3 axis.
 */
public class NoiseStretch {

	protected final double stretchX;
	protected final double stretchY;
	protected final double stretchZ;
	protected final SimplexNoise noise;

	/**
	 * Creates a new 2D NoiseStretch with the given values.
	 *
	 * @param noise the noise to use
	 * @param stretchX how much the noise will be stretched on the x axis
	 * @param stretchZ how much the noise will be stretched on the z axis
	 */
	public NoiseStretch(final SimplexNoise noise, final double stretchX, final double stretchZ) {
		this(noise, stretchX, 1.0, stretchZ);
	}

	/**
	 * Creates a new 3D NoiseStretch with the given values.
	 *
	 * @param noise the noise to use
	 * @param stretchX how much the noise will be stretched on the x axis
	 * @param stretchY how much the noise will be stretched on the y axis
	 * @param stretchZ how much the noise will be stretched on the z axis
	 */
	public NoiseStretch(final SimplexNoise noise, final double stretchX, final double stretchY, final double stretchZ) {
		this.noise = noise;
		this.stretchX = stretchX;
		this.stretchY = stretchY;
		this.stretchZ = stretchZ;
	}

	/**
	 * Gets the specific stretched noise value at a given 2D position
	 *
	 * @param x x value of the position
	 * @param z z value of the position
	 * @return the stretched noise value at the given 2D position
	 */
	public double getNoise(final double x, final double z) {
		return this.noise.noise(x / this.stretchX, z / this.stretchZ);
	}

	/**
	 * Gets the specific stretched noise value at a given 3D position
	 *
	 * @param x x value of the position
	 * @param y y value of the position
	 * @param z z value of the position
	 * @return the stretched noise value at the given 3D position
	 */
	public double getNoise(final double x, final double y, final double z) {
		return this.noise.noise(x / this.stretchX, y / this.stretchY, z / this.stretchZ);
	}
}
