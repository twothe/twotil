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

import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * A 3D Vector implentation (Quaternion) using floats.
 * <p>
 * Describing this as a Quaternion allows for faster tranformation in 3D space using an appropriate Matrix4f. A vector can either be a direction or a position
 * (point in space). A direction has by definition no origin, while a position is relative to the origin at (0,0,0).
 * <p>
 * Calculations are done in place, so no additional memory is allocated by operations.
 * <p>
 * Uses a FloatBuffer which is compatible with OpenGL and similar imeplementations.
 */
public class Vector4f implements Cloneable {

	protected final FloatBuffer buffer;
	protected final float[] data;
	/* Data length of this Vector class */
	public static final int LENGTH = 4;
	public static final int SIZE_BYTE = LENGTH * (Float.SIZE >>> 3);
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	public static final int W = 3;

	/**
	 * Create a new empty vector with all values set to 0.
	 */
	public Vector4f() {
		this.buffer = FloatBuffer.allocate(LENGTH);
		this.data = this.buffer.array();
		if ((this.data == null) || (this.data.length != LENGTH)) {
			throw new UnsupportedOperationException("FloatBuffer did not provide an underlying array.");
		}
	}

	/**
	 * Creates a new vector with the given values.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @param isPosition determines if the vector is a position (point in space) or direction.
	 */
	public Vector4f(final float x, final float y, final float z, final boolean isPosition) {
		this();
		this.data[X] = x;
		this.data[Y] = y;
		this.data[Z] = z;
		this.data[W] = isPosition ? 1.0f : 0.0f;
	}

	/**
	 * Copy constructor. Creates a new Vector4f with all values set to other.
	 *
	 * @param other any other Vector4f
	 * @return a new Vector4f with the same values as other.
	 */
	public Vector4f set(final Vector4f other) {
		return this.setUnchecked(other.data);
	}

	/**
	 * Sets the vector to the given values without changing the W value.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f set(final float x, final float y, final float z) {
		this.data[X] = x;
		this.data[Y] = y;
		this.data[Z] = z;
		return this;
	}

	/**
	 * Sets the x value of this vector.
	 *
	 * @param x any float
	 * @return this vector.
	 */
	public Vector4f setX(final float x) {
		this.data[X] = x;
		return this;
	}

	/**
	 * Sets the y value of this vector.
	 *
	 * @param y any float
	 * @return this vector.
	 */
	public Vector4f setY(final float y) {
		this.data[Y] = y;
		return this;
	}

	/**
	 * Sets the z value of this vector.
	 *
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f setZ(final float z) {
		this.data[Z] = z;
		return this;
	}

	/**
	 * Sets all values of this vector to those of a given float array.
	 * <p>
	 * This can be used to copy external data into a Vector4f.
	 *
	 * @param data any array of exactly 4 floats.
	 * @return this vector, with all data set to the external values.
	 */
	public Vector4f set(final float[] data) {
		if (data == null) {
			throw new NullPointerException("Vector4f data must not be null.");
		}
		if (data.length != this.data.length) {
			throw new IllegalArgumentException("Array length must be equal to the size of Vector4f data.");
		}
		return this.setUnchecked(data);
	}

	/**
	 * Internal function to set the data of this vector without checking it.
	 *
	 * @param data any array of exactly 4 floats.
	 * @return this vector, with all data set to the new values.
	 */
	protected Vector4f setUnchecked(final float[] data) {
		for (int i = LENGTH - 1; i >= 0; --i) {
			this.data[i] = data[i];
		}
		return this;
	}

	/**
	 * Sets the x,y,z content of this vector to all zero.
	 *
	 * @return this vector.
	 */
	public Vector4f setZero() {
		this.data[X] = 0.0f;
		this.data[Y] = 0.0f;
		this.data[Z] = 0.0f;
		return this;
	}

	/**
	 * Sets the x,y,z content of this vector to all zero and turns it into a position (point in space).
	 *
	 * @return this vector.
	 */
	public Vector4f setZeroPosition() {
		this.data[X] = 0.0f;
		this.data[Y] = 0.0f;
		this.data[Z] = 0.0f;
		this.data[W] = 1.0f;
		return this;
	}

	/**
	 * Sets the x,y,z content of this vector to all zero and turns it into a directional vector.
	 *
	 * @return this vector.
	 */
	public Vector4f setZeroDirection() {
		this.data[X] = 0.0f;
		this.data[Y] = 0.0f;
		this.data[Z] = 0.0f;
		this.data[W] = 0.0f;
		return this;
	}

	/**
	 * Checks if this vector is a directional vector.
	 *
	 * @return true if it is a directional vector, false othewise.
	 */
	public boolean isDirection() {
		return (this.data[W] == 0);
	}

	/**
	 * Checks if this vector is a position (point in space) vector.
	 *
	 * @return true if it is a position vector, false othewise.
	 */
	public boolean isPosition() {
		return (this.data[W] == 1);
	}

	/**
	 * Sets this vector to be a direactional vector.
	 *
	 * @return this vector.
	 */
	public Vector4f setIsDirection() {
		this.data[W] = 0.0f;
		return this;
	}

	/**
	 * Sets this vector to be a position (point in space) vector.
	 *
	 * @return this vector.
	 */
	public Vector4f setIsPosition() {
		this.data[W] = 1.0f;
		return this;
	}

	/**
	 * Returns the squared length of this vector.
	 *
	 * @return the squared length of this vector.
	 */
	public float lengthSquared() {
		final float x = this.data[X];
		final float y = this.data[Y];
		final float z = this.data[Z];
		return x * x + y * y + z * z; // direction vector is not part of the length
	}

	/**
	 * Returns the length of this vector.
	 *
	 * @return the length of this vector.
	 */
	public float length() {
		final double x = this.data[X];
		final double y = this.data[Y];
		final double z = this.data[Z];
		return (float) Math.sqrt(x * x + y * y + z * z); // direction vector is not part of the length
	}

	/**
	 * Normalizes this vector to a length of 1.
	 *
	 * @return this vector.
	 */
	public Vector4f normalize() {
		final float length = this.length();
		if (length == 0.0f) {
			throw new NullPointerException();
		}
		this.data[X] /= length;
		this.data[Y] /= length;
		this.data[Z] /= length;
		return this;
	}

	/**
	 * Adds the given values to this vector.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f add(final float x, final float y, final float z) {
		this.data[X] += x;
		this.data[Y] += y;
		this.data[Z] += z;
		return this;
	}

	/**
	 * Adds the given vector to this vector.
	 *
	 * @param other any other Vector4f.
	 * @return this vector.
	 */
	public Vector4f add(final Vector4f other) {
		this.data[X] += other.data[X];
		this.data[Y] += other.data[Y];
		this.data[Z] += other.data[Z];
		return this;
	}

	/**
	 * Adds a specific value to the x component of this vector.
	 *
	 * @param x any float
	 * @return this vector.
	 */
	public Vector4f addX(final float x) {
		this.data[X] += x;
		return this;
	}

	/**
	 * Adds a specific value to the y component of this vector.
	 *
	 * @param y any float
	 * @return this vector.
	 */
	public Vector4f addY(final float y) {
		this.data[Y] += y;
		return this;
	}

	/**
	 * Adds a specific value to the z component of this vector.
	 *
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f addZ(final float z) {
		this.data[Z] += z;
		return this;
	}

	/**
	 * Subtracts the given values to this vector.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f sub(final float x, final float y, final float z) {
		this.data[X] -= x;
		this.data[Y] -= y;
		this.data[Z] -= z;
		return this;
	}

	/**
	 * Subtracts the given vector to this vector.
	 *
	 * @param other any other Vector4f.
	 * @return this vector.
	 */
	public Vector4f sub(final Vector4f other) {
		this.data[X] -= other.data[X];
		this.data[Y] -= other.data[Y];
		this.data[Z] -= other.data[Z];
		return this;
	}

	/**
	 * Subtracts a specific value to the x component of this vector.
	 *
	 * @param x any float
	 * @return this vector.
	 */
	public Vector4f subX(final float x) {
		this.data[X] -= x;
		return this;
	}

	/**
	 * Subtracts a specific value to the y component of this vector.
	 *
	 * @param y any float
	 * @return this vector.
	 */
	public Vector4f subY(final float y) {
		this.data[Y] -= y;
		return this;
	}

	/**
	 * Subtracts a specific value to the z component of this vector.
	 *
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f subZ(final float z) {
		this.data[Z] -= z;
		return this;
	}

	/**
	 * Shears this vector with the given vector components.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this vector.
	 */
	public Vector4f shear(final float x, final float y, final float z) {
		this.data[X] *= x;
		this.data[Y] *= y;
		this.data[Z] *= z;
		return this;
	}

	/**
	 * Shears this vector on the x-axis only.
	 *
	 * @param x any float.
	 * @return this vector.
	 */
	public Vector4f shearX(final float x) {
		this.data[X] *= x;
		return this;
	}

	/**
	 * Shears this vector on the y-axis only.
	 *
	 * @param y any float.
	 * @return this vector.
	 */
	public Vector4f shearY(final float y) {
		this.data[Y] *= y;
		return this;
	}

	/**
	 * Shears this vector on the z-axis only.
	 *
	 * @param z any float.
	 * @return this vector.
	 */
	public Vector4f shearZ(final float z) {
		this.data[Z] *= z;
		return this;
	}

	/**
	 * Scales this vector by the given magnitude.
	 *
	 * @param magnitude any float.
	 * @return this vector.
	 */
	public Vector4f scale(final float magnitude) {
		this.data[X] *= magnitude;
		this.data[Y] *= magnitude;
		this.data[Z] *= magnitude;
		return this;
	}

	/**
	 * Calculates the dot product between this vector and another.
	 *
	 * @param other any other Vector4f
	 * @return this vector.
	 */
	public float dot(final Vector4f other) {
		float result = this.data[X] * other.data[X];
		result += this.data[Y] * other.data[Y];
		result += this.data[Z] * other.data[Z];
		return result;
	}

	/**
	 * Calculates the cross product between this vector and another.
	 *
	 * @param other any other Vector4f
	 * @return this vector.
	 */
	public Vector4f cross(final Vector4f other) {
		this.data[X] = this.data[Y] * other.data[Z] - this.data[Z] * other.data[Y];
		this.data[Y] = this.data[Z] * other.data[X] - this.data[X] * other.data[Z];
		this.data[Z] = this.data[X] * other.data[Y] - this.data[Y] * other.data[X];
		return this;
	}

	/**
	 * Transforms this vector using the given matrix.
	 *
	 * @param matrix any Matrix4f
	 * @return this vector.
	 */
	public Vector4f transform(final Matrix4f matrix) {
		final float x = this.data[X], y = this.data[Y], z = this.data[Z], w = this.data[W];
		this.data[X] = matrix.data[Matrix4f.XX] * x + matrix.data[Matrix4f.XY] * y + matrix.data[Matrix4f.XZ] * z + matrix.data[Matrix4f.XW] * w;
		this.data[Y] = matrix.data[Matrix4f.YX] * x + matrix.data[Matrix4f.YY] * y + matrix.data[Matrix4f.YZ] * z + matrix.data[Matrix4f.YW] * w;
		this.data[Z] = matrix.data[Matrix4f.ZX] * x + matrix.data[Matrix4f.ZY] * y + matrix.data[Matrix4f.ZZ] * z + matrix.data[Matrix4f.ZW] * w;
		return this;
	}

	/**
	 * Translates this vector using the given matrix.
	 * <p>
	 * Note that this has no effect if this vector is a directional vector.
	 *
	 * @param matrix any Matrix4f
	 * @return this vector.
	 */
	public Vector4f translate(final Matrix4f matrix) {
		if (this.data[W] != 0) {
			this.data[X] += matrix.data[Matrix4f.XW];
			this.data[Y] += matrix.data[Matrix4f.YW];
			this.data[Z] += matrix.data[Matrix4f.ZW];
		}
		return this;
	}

	/**
	 * Returns the underlying FloatBuffer for direct access.
	 *
	 * @return the underlying FloatBuffer.
	 */
	public FloatBuffer buffer() {
		return this.buffer;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + Arrays.hashCode(this.data);
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		return this.equals((Vector4f) obj);
	}

	public boolean equals(final Vector4f other) {
		for (int i = LENGTH - 1; i >= 0; --i) {
			if (Float.floatToIntBits(this.data[i]) != Float.floatToIntBits(other.data[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Vector4f clone() {
		// no super.clone on purpose
		return new Vector4f().setUnchecked(this.data);
	}

	@Override
	public String toString() {
		return "(" + this.data[X] + ", " + this.data[Y] + ", " + this.data[Z] + ", " + this.data[W] + ")";
	}

}
