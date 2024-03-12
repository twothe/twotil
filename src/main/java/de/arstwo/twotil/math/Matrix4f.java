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
 * A 3D Matrix implentation for Quaternions using floats.
 * <p>
 * Calculations are done in place, so no additional memory is allocated by operations.
 * <p>
 * This is compatible with OpenGL and other imeplementations, which use a colum-major approach instead of the row-major that is intuitively used. This is only
 * relevant if accessing the data directly. It's underlying FloatBuffer can therefore be directly inserted into OpenGL. You can use the constants below to
 * access specific data points directly.
 */
public class Matrix4f implements Cloneable {

	/* Data length of this Matrix class */
	public static final int ROWS = 4;
	public static final int COLS = 4;
	public static final int LENGTH = ROWS * COLS;
	public static final int SIZE_BYTE = LENGTH * (Float.SIZE >>> 3);
	
	private static final float[] IDENTITY_DATA = new float[]{
		1.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 1.0f
	};
	protected static final Matrix4f IDENTITY = new Matrix4f().setIdentity();
	
	private static final float[] ZERO_DATA = new float[LENGTH];

	/**
	 * Returns a new identity matrix.
	 *
	 * @return a new identity matrix.
	 */
	public static Matrix4f getIdentityMatrix() {
		return IDENTITY.clone();
	}
	/**
	 * Matrix constants for easier access: | xx xy xz xw | | yx yy yz yw | | zx zy zz zw | | wx wy wz ww |
	 *
	 * Note that OpenGL expects the matrix in format xy, yx, ..., zw, ww
	 */
	public final static int XX = 0, YX = 1, ZX = 2, WX = 3;
	public final static int XY = 4, YY = 5, ZY = 6, WY = 7;
	public final static int XZ = 8, YZ = 9, ZZ = 10, WZ = 11;
	public final static int XW = 12, YW = 13, ZW = 14, WW = 15;
	protected final FloatBuffer buffer;
	protected final float[] data;

	/**
	 * Creats a new Matrix4f with all values setColumnMajor to 0.
	 */
	public Matrix4f() {
		this.buffer = FloatBuffer.allocate(LENGTH);
		this.data = this.buffer.array();
		if ((this.data == null) || (this.data.length != LENGTH)) {
			throw new UnsupportedOperationException("FloatBuffer did not provide an underlying array.");
		}
	}

	/**
	 * Copy constructor. Generates a new matrix with all values set to other.
	 *
	 * @param other any other Matrix4f
	 * @return a new Matrix4f with all values setColumnMajor to other.
	 */
	public Matrix4f set(final Matrix4f other) {
		assert this != other : "Trying to set Matrix to itself";
		return this.setUnchecked(other.data);
	}

	/**
	 * Sets the data of this matrix to the given external data represented in that array.
	 * <p>
	 * The expected format is colum-major (xx yx zx wx...)
	 *
	 * @param data any compatible float array that holds data flatted by column then by row.
	 * @return
	 */
	public Matrix4f setColumnMajor(final float[] data) {
		if (data == null) {
			throw new NullPointerException("Matrix4f data must not be null.");
		}
		if (data.length != this.data.length) {
			throw new IllegalArgumentException("Array length must be equal to the size of Matrix4f data.");
		}
		return this.setUnchecked(data);
	}

	/**
	 * Sets the data of this matrix to the given external data represented in that array, that is in a row-major format.
	 * <p>
	 * The internal format is colum-major (xx yx zx wx...), while the input data is row-major (xx, xy, xz, xw, ...).
	 *
	 * @param data any compatible float array that holds data flatted by row then by column.
	 * @return
	 */
	public Matrix4f setRowMajor(final float[] data) {
		if (data == null) {
			throw new NullPointerException("Matrix4f data must not be null.");
		}
		if (data.length != this.data.length) {
			throw new IllegalArgumentException("Array length must be equal to the size of Matrix4f data.");
		}
		int i = -1;
		this.data[XX] = data[++i];
		this.data[XY] = data[++i];
		this.data[XZ] = data[++i];
		this.data[XW] = data[++i];

		this.data[YX] = data[++i];
		this.data[YY] = data[++i];
		this.data[YZ] = data[++i];
		this.data[YW] = data[++i];

		this.data[ZX] = data[++i];
		this.data[ZY] = data[++i];
		this.data[ZZ] = data[++i];
		this.data[ZW] = data[++i];

		this.data[WX] = data[++i];
		this.data[WY] = data[++i];
		this.data[WZ] = data[++i];
		this.data[WW] = data[++i];

		return this;
	}
	
	/**
	 * Sets the content of this matrix to all 0.
	 * @return this matrix, with all content set to 0.
	 */
	public Matrix4f setZero() {
		return this.setUnchecked(ZERO_DATA);
	}

	/**
	 * Internal function to setColumnMajor the data of this matrix without checking it.
	 *
	 * @param data any array of exactly 16 floats.
	 * @return this matrix, with all data setColumnMajor to the new values.
	 */
	protected Matrix4f setUnchecked(final float[] data) {
		System.arraycopy(data, 0, this.data, 0, LENGTH);
		return this;
	}

	/**
	 * Sets this matrix to identity values.
	 *
	 * @return this matrix.
	 */
	public Matrix4f setIdentity() {
		return this.setUnchecked(IDENTITY_DATA);
	}

	/**
	 * Transposes this matrix (flips it diagonally).
	 *
	 * @return this matrix.
	 */
	public Matrix4f setTranspose() {
		float h;
		h = this.data[YX];
		this.data[YX] = this.data[XY];
		this.data[XY] = h;
		h = this.data[ZX];
		this.data[ZX] = this.data[XZ];
		this.data[XZ] = h;
		h = this.data[WX];
		this.data[WX] = this.data[XW];
		this.data[XW] = h;
		h = this.data[ZY];
		this.data[ZY] = this.data[YZ];
		this.data[YZ] = h;
		h = this.data[WY];
		this.data[WY] = this.data[YW];
		this.data[YW] = h;
		h = this.data[WZ];
		this.data[WZ] = this.data[ZW];
		this.data[ZW] = h;
		return this;
	}

	/**
	 * Sets this matrix to be an orthogonal (2D) projection.
	 *
	 * @return this matrix.
	 */
	public Matrix4f setOrthogonalProjection() {
		this.setIdentity();
		this.data[ZZ] = -1;
		return this;
	}

	/**
	 * Sets this matrix to be a projection matrix with the given values.
	 *
	 * @param fovY Field of view
	 * @param aspectRatio the aspect ratio of the plane
	 * @param zNear near clipping range
	 * @param zFar far clipping range
	 * @return this matrix.
	 */
	public Matrix4f setProjection(final float fovY, final float aspectRatio, final float zNear, final float zFar) {
		this.setIdentity();
		final float radians = fovY / 2 * (float) Math.PI / 180;
		float sine, cotangent, deltaZ;

		deltaZ = zFar - zNear;
		sine = (float) Math.sin(radians);

		if ((deltaZ == 0) || (sine == 0) || (aspectRatio == 0)) {
			return this;
		}

		cotangent = (float) Math.cos(radians) / sine;

		this.data[XX] = cotangent / aspectRatio;
		this.data[YY] = cotangent;
		this.data[ZZ] = -(zFar + zNear) / deltaZ;
		this.data[WZ] = -1;
		this.data[ZW] = -2 * zNear * zFar / deltaZ;
		this.data[WW] = 0;
		return this;
	}

	/**
	 * Sets this matrix to a look matrix (an imaginary camera in 3D space looking into a specific direction) with the given Vector4f values.
	 *
	 * @param position the position of the camera
	 * @param right what is considered the direction 'right' of the camera
	 * @param up what is considered the direction 'up' of the camera
	 * @param look what is considered the direction that the camera is 'looking' at
	 * @return this matrix.
	 */
	public Matrix4f setLook(final Vector4f position, final Vector4f right, final Vector4f up, final Vector4f look) {
		return this.setLook(position.data[Vector4f.X], position.data[Vector4f.Y], position.data[Vector4f.Z],
						right.data[Vector4f.X], right.data[Vector4f.Y], right.data[Vector4f.Z],
						up.data[Vector4f.X], up.data[Vector4f.Y], up.data[Vector4f.Z],
						look.data[Vector4f.X], look.data[Vector4f.Y], look.data[Vector4f.Z]);
	}

	/**
	 * Sets this matrix to a look matrix (an imaginary camera in 3D space looking into a specific direction) with the given float values.
	 *
	 * @return this matrix.
	 * @see Matrix4f#setLook(Vector4f, Vector4f, Vector4f, Vector4f)
	 */
	public Matrix4f setLook(final float positionX, final float positionY, final float positionZ,
					final float rightX, final float rightY, final float rightZ,
					final float upX, final float upY, final float upZ,
					final float lookX, final float lookY, final float lookZ) {
		this.data[XX] = rightX;
		this.data[XY] = rightY;
		this.data[XZ] = rightZ;
		this.data[XW] = -positionX;

		this.data[YX] = upX;
		this.data[YY] = upY;
		this.data[YZ] = upZ;
		this.data[YW] = -positionY;

		this.data[ZX] = lookX;
		this.data[ZY] = lookY;
		this.data[ZZ] = lookZ;
		this.data[ZW] = -positionZ;

		this.data[WX] = 0;
		this.data[WY] = 0;
		this.data[WZ] = 0;
		this.data[WW] = 1;

		return this;
	}

	/**
	 * Performs a matrix addition of this and other.
	 *
	 * @param other any other Matrix4f
	 * @return this matrix.
	 */
	public Matrix4f add(final Matrix4f other) {
		@SuppressWarnings("MismatchedReadAndWriteOfArray")
		final float[] thisData = this.data;
		final float[] otherData = other.data;
		for (int i = LENGTH - 1; i >= 0; --i) {
			thisData[i] += otherData[i];
		}
		return this;
	}

	/**
	 * Performs a matrix subtraction of this and other.
	 *
	 * @param other any other Matrix4f
	 * @return this matrix.
	 */
	public Matrix4f sub(final Matrix4f other) {
		@SuppressWarnings("MismatchedReadAndWriteOfArray")
		final float[] thisData = this.data;
		final float[] otherData = other.data;
		for (int i = LENGTH - 1; i >= 0; --i) {
			thisData[i] -= otherData[i];
		}
		return this;
	}

	/**
	 * Performs a matrix multiplication of this and other.
	 *
	 * @param other any other Matrix4f
	 * @return this matrix.
	 */
	public Matrix4f multiply(final Matrix4f other) {
		final float[] result = new float[LENGTH];
		final float[] thisData = this.data;
		final float[] otherData = other.data;
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				result[col * ROWS + row]
								= thisData[XX + row] * otherData[col * ROWS]
								+ thisData[XY + row] * otherData[col * ROWS + 1]
								+ thisData[XZ + row] * otherData[col * ROWS + 2]
								+ thisData[XW + row] * otherData[col * ROWS + 3];
			}
		}
		System.arraycopy(result, 0, this.data, 0, result.length);
		return this;
	}

	/**
	 * Sets the translation components of this matrix with the given values.
	 * <p>
	 * Note that all other values are kept as they were. If you want a pure translation matrix you need to setIdentity first.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this matrix.
	 */
	public Matrix4f setTranslate(float x, float y, float z) {
		this.data[XW] = x;
		this.data[YW] = y;
		this.data[ZW] = z;
		return this;
	}

	/**
	 * Translates this matrix by the given other matrix.
	 *
	 * @param other any other matrix
	 * @return this matrix.
	 */
	public Matrix4f translate(final Matrix4f other) {
		this.data[XW] += other.data[Matrix4f.XW];
		this.data[YW] += other.data[Matrix4f.YW];
		this.data[ZW] += other.data[Matrix4f.ZW];
		return this;
	}

	/**
	 * Translates this matrix by the given values.
	 *
	 * @param x any float
	 * @param y any float
	 * @param z any float
	 * @return this matrix.
	 */
	public Matrix4f translate(final float x, final float y, final float z) {
		this.data[XW] += x;
		this.data[YW] += y;
		this.data[ZW] += z;
		return this;
	}

	/**
	 * Translates this matrix by the given Vector4f.
	 *
	 * @param direction any Vector4f interpreted as a direction
	 * @return this matrix.
	 */
	public Matrix4f translate(final Vector4f direction) {
		this.data[XW] += direction.data[Vector4f.X];
		this.data[YW] += direction.data[Vector4f.Y];
		this.data[ZW] += direction.data[Vector4f.Z];
		return this;
	}

	/**
	 * Translates this matrix by the given x values.
	 *
	 * @param x any float
	 * @return this matrix.
	 */
	public Matrix4f translateX(final float x) {
		this.data[XW] += x;
		return this;
	}

	/**
	 * Translates this matrix by the given y values.
	 *
	 * @param y any float
	 * @return this matrix.
	 */
	public Matrix4f translateY(final float y) {
		this.data[YW] += y;
		return this;
	}

	/**
	 * Translates this matrix by the given z values.
	 *
	 * @param z any float
	 * @return this matrix.
	 */
	public Matrix4f translateZ(final float z) {
		this.data[ZW] += z;
		return this;
	}

	/**
	 * Rotates this matrix around a given axis.
	 *
	 * @param radians radians to rotate around the axis
	 * @param axis the axis to rotate around
	 * @return this matrix.
	 */
	public Matrix4f rotate(final float radians, final Vector4f axis) {
		return this.rotate(radians, axis.data[Vector4f.X], axis.data[Vector4f.Y], axis.data[Vector4f.Z]);
	}

	/**
	 * Rotates this matrix around a given axis.
	 *
	 * @param radians radians to rotate around the axis
	 * @param axisX the x part of the axis to rotate around
	 * @param axisY the y part of the axis to rotate around
	 * @param axisZ the z part of the axis to rotate around
	 * @return this matrix.
	 */
	public Matrix4f rotate(final float radians, final float axisX, final float axisY, final float axisZ) {
		final Matrix4f other = new Matrix4f().setRotation(radians, axisX, axisY, axisZ);
		return this.multiply(other);
	}

	/**
	 * Sets this matrix to be a fixed rotation around a given axis.
	 *
	 * @param radians radians to rotate around the axis
	 * @param axis the axis to rotate around
	 * @return this matrix
	 */
	public Matrix4f setRotation(final float radians, final Vector4f axis) {
		return this.setRotation(radians, axis.data[Vector4f.X], axis.data[Vector4f.Y], axis.data[Vector4f.Z]);
	}

	/**
	 * Sets this matrix to be a fixed rotation around a given axis.
	 *
	 * @param radians radians to rotate around the axis
	 * @param axisX the x part of the axis to rotate around
	 * @param axisY the y part of the axis to rotate around
	 * @param axisZ the z part of the axis to rotate around
	 * @return this matrix
	 */
	public Matrix4f setRotation(final float radians, final float axisX, final float axisY, final float axisZ) {
		final double radiansHalf = radians / 2.0;
		final float q0 = (float) Math.cos(radiansHalf);
		final float sinRadiansHalf = (float) Math.sin(radiansHalf);
		final float q1 = sinRadiansHalf * axisX;
		final float q2 = sinRadiansHalf * axisY;
		final float q3 = sinRadiansHalf * axisZ;
		final float q0Squared = q0 * q0;
		final float q1Squared = q1 * q1;
		final float q2Squared = q2 * q2;
		final float q3Squared = q3 * q3;

		this.data[XX] = q0Squared + q1Squared - q2Squared - q3Squared;
		this.data[XY] = 2.0f * (q1 * q2 - q0 * q3);
		this.data[XZ] = 2.0f * (q1 * q3 + q0 * q2);
		this.data[XW] = 0.0f;

		this.data[YX] = 2.0f * (q2 * q1 + q0 * q3);
		this.data[YY] = q0Squared - q1Squared + q2Squared - q3Squared;
		this.data[YZ] = 2.0f * (q2 * q3 - q0 * q1);
		this.data[YW] = 0.0f;

		this.data[ZX] = 2.0f * (q3 * q1 - q0 * q2);
		this.data[ZY] = 2.0f * (q3 * q2 + q0 * q1);
		this.data[ZZ] = q0Squared - q1Squared - q2Squared + q3Squared;
		this.data[ZW] = 0.0f;

		this.data[WX] = 0.0f;
		this.data[WY] = 0.0f;
		this.data[WZ] = 0.0f;
		this.data[WW] = 1.0f;

		return this;
	}

	/**
	 * Returns the internal float buffer for direct access.
	 *
	 * @return the internal float buffer for direct access.
	 */
	public FloatBuffer buffer() {
		return buffer;
	}

	@Override
	public int hashCode() {
		return 71 + Arrays.hashCode(this.data);
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		return this.equals((Matrix4f) obj);
	}

	public boolean equals(final Matrix4f other) {
		if (other == null) {
			return false;
		}
		for (int i = LENGTH - 1; i >= 0; --i) {
			if (Float.floatToIntBits(this.data[i]) != Float.floatToIntBits(other.data[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if this is equal to other within the given delta for precision
	 *
	 * @param other any other Matrix4f
	 * @param delta acceptable distance between two floats to count as equal
	 * @return true if those are equal within the given delta, false otherwise
	 */
	public boolean equals(Matrix4f other, float delta) {
		for (int i = this.data.length - 1; i >= 0; --i) {
			if (MathUtil.aboutEqual(this.data[i], other.data[i], delta)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Matrix4f clone() {
		// no super.clone on purpose
		return new Matrix4f().setUnchecked(this.data);
	}

	@Override
	public String toString() {
		return String.format("%n|%7.3f %7.3f %7.3f %7.3f|%n|%7.3f %7.3f %7.3f %7.3f|%n|%7.3f %7.3f %7.3f %7.3f|%n|%7.3f %7.3f %7.3f %7.3f|%n", new Object[]{this.data[XX], this.data[XY], this.data[XZ], this.data[XW], this.data[YX], this.data[YY], this.data[YZ], this.data[YW], this.data[ZX], this.data[ZY], this.data[ZZ], this.data[ZW], this.data[WX], this.data[WY], this.data[WZ], this.data[WW]});
	}
}
