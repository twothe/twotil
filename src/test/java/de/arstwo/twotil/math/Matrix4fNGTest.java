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
import java.security.SecureRandom;
import java.util.Random;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Two
 */
public class Matrix4fNGTest {

	private static final float DELTA = 1e-3f; // delta for float comparison

	public Matrix4fNGTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUpMethod() throws Exception {
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
	}

	private void assertIsIdentity(final Matrix4f instance) {
		assertNotNull(instance);
		assertNotNull(instance.data);
		assertEquals(Matrix4f.LENGTH, instance.data.length);
		for (int i = 0; i < Matrix4f.LENGTH; ++i) {
			switch (i) {
				case Matrix4f.XX:
				case Matrix4f.YY:
				case Matrix4f.ZZ:
				case Matrix4f.WW:
					assertEquals(1.0f, instance.data[i]);
					break;
				default:
					assertEquals(0.0f, instance.data[i]);
					break;
			}
		}
	}

	private void assertIsNotIdentity(final Matrix4f instance) {
		assertNotNull(instance);
		assertNotNull(instance.data);
		assertEquals(Matrix4f.LENGTH, instance.data.length);
		for (int i = 0; i < Matrix4f.LENGTH; ++i) {
			switch (i) {
				case Matrix4f.XX:
				case Matrix4f.YY:
				case Matrix4f.ZZ:
				case Matrix4f.WW:
					if (instance.data[i] != 1.0f) {
						return;
					}
					break;
				default:
					if (instance.data[i] != 0.0f) {
						return;
					}
					break;
			}
		}
		fail("Matrix is identity but should not");
	}

	private void assertAllEquals(final float[] actual, final float expected) {
		for (int i = 0; i < actual.length; ++i) {
			assertEquals(actual[i], expected);
		}
	}

	@Test
	public void testGetIdentityMatrix() {
		System.out.println("getIdentityMatrix");
		Matrix4f expResult = new Matrix4f().setIdentity();
		Matrix4f result = Matrix4f.getIdentityMatrix();
		assertEquals(result, expResult);
	}

	@Test
	public void testSet_Matrix4f() {
		System.out.println("set");
		Matrix4f other = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.set(other);
			fail("Matrix4f set to null");
		} catch (NullPointerException e) {
		}
		other = new Matrix4f();
		assertSame(instance, instance.set(other));
		assertNotSame(instance, other);
		assertEquals(instance.data, other.data);

		for (int i = 0; i < other.data.length; ++i) {
			other.data[i] = i;
		}
		assertSame(instance, instance.set(other));
		assertNotSame(instance, other);
		assertEquals(instance.data, other.data);

		int i = 0;
		assertEquals(other.data[i++], instance.data[Matrix4f.XX]);
		assertEquals(other.data[i++], instance.data[Matrix4f.YX]);
		assertEquals(other.data[i++], instance.data[Matrix4f.ZX]);
		assertEquals(other.data[i++], instance.data[Matrix4f.WX]);
		assertEquals(other.data[i++], instance.data[Matrix4f.XY]);
		assertEquals(other.data[i++], instance.data[Matrix4f.YY]);
		assertEquals(other.data[i++], instance.data[Matrix4f.ZY]);
		assertEquals(other.data[i++], instance.data[Matrix4f.WY]);
		assertEquals(other.data[i++], instance.data[Matrix4f.XZ]);
		assertEquals(other.data[i++], instance.data[Matrix4f.YZ]);
		assertEquals(other.data[i++], instance.data[Matrix4f.ZZ]);
		assertEquals(other.data[i++], instance.data[Matrix4f.WZ]);
		assertEquals(other.data[i++], instance.data[Matrix4f.XW]);
		assertEquals(other.data[i++], instance.data[Matrix4f.YW]);
		assertEquals(other.data[i++], instance.data[Matrix4f.ZW]);
		assertEquals(other.data[i++], instance.data[Matrix4f.WW]);
	}

	@Test
	public void testSet_floatArr() {
		System.out.println("set");
		float[] data = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.setColumnMajor(data);
			fail("Matrix4f set to null data");
		} catch (NullPointerException e) {
		}
		data = new float[1];
		try {
			instance.setColumnMajor(data);
			fail("Matrix4f set to illegal length data");
		} catch (IllegalArgumentException e) {
		}
		data = new float[Matrix4f.LENGTH];
		assertSame(instance, instance.setColumnMajor(data));
		assertNotSame(data, instance.data);
		assertEquals(data, instance.data);
		for (int i = 0; i < data.length; ++i) {
			data[i] = i;
		}
		assertSame(instance, instance.setColumnMajor(data));
		assertNotSame(data, instance.data);
		assertEquals(data, instance.data);

		int i = 0;
		assertEquals(data[i++], instance.data[Matrix4f.XX]);
		assertEquals(data[i++], instance.data[Matrix4f.YX]);
		assertEquals(data[i++], instance.data[Matrix4f.ZX]);
		assertEquals(data[i++], instance.data[Matrix4f.WX]);
		assertEquals(data[i++], instance.data[Matrix4f.XY]);
		assertEquals(data[i++], instance.data[Matrix4f.YY]);
		assertEquals(data[i++], instance.data[Matrix4f.ZY]);
		assertEquals(data[i++], instance.data[Matrix4f.WY]);
		assertEquals(data[i++], instance.data[Matrix4f.XZ]);
		assertEquals(data[i++], instance.data[Matrix4f.YZ]);
		assertEquals(data[i++], instance.data[Matrix4f.ZZ]);
		assertEquals(data[i++], instance.data[Matrix4f.WZ]);
		assertEquals(data[i++], instance.data[Matrix4f.XW]);
		assertEquals(data[i++], instance.data[Matrix4f.YW]);
		assertEquals(data[i++], instance.data[Matrix4f.ZW]);
		assertEquals(data[i++], instance.data[Matrix4f.WW]);
	}

	@Test
	public void testSetUnchecked() {
		System.out.println("setUnchecked");
		float[] data = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.setUnchecked(data);
			fail("Matrix4f set to null data");
		} catch (NullPointerException e) {
		}
		data = new float[1];
		try {
			instance.setUnchecked(data);
			fail("Matrix4f set to illegal length data");
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		data = new float[40];
		instance.setUnchecked(data);
		data = new float[Matrix4f.LENGTH];
		assertSame(instance, instance.setUnchecked(data));
		assertNotSame(data, instance.data);
		assertEquals(data, instance.data);
		for (int i = 0; i < data.length; ++i) {
			data[i] = i;
		}
		assertSame(instance, instance.setUnchecked(data));
		assertNotSame(data, instance.data);
		assertEquals(data, instance.data);
	}

	@Test
	public void testSetIdentity() {
		System.out.println("setIdentity");
		Matrix4f instance = new Matrix4f();
		assertSame(instance, instance.setIdentity());
		assertIsIdentity(instance);
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertIsNotIdentity(instance);
		assertSame(instance, instance.setIdentity());
		assertIsIdentity(instance);
	}

	@Test
	public void testSetTranspose() {
		System.out.println("setTranspose");
		float f;
		Matrix4f instance = new Matrix4f();
		assertSame(instance, instance.setTranspose());
		assertIsIdentity(instance.setIdentity().setTranspose());
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		f = 0.0f;
		assertEquals(instance.data[Matrix4f.XX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WW], f);

		instance.setTranspose();

		f = 0.0f;
		assertEquals(instance.data[Matrix4f.XX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.XW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.YW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.ZW], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WX], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WY], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WZ], f);
		f += 1.0f;
		assertEquals(instance.data[Matrix4f.WW], f);
	}

	@Test
	public void testSetOrthogonalProjectionSimple() {
		Matrix4f instance = new Matrix4f().setOrthogonalProjection();

		// Expected matrix values after setting orthogonal projection
		float[] expectedData = {
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, -1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);

		assertEquals(instance.data, expected.data, DELTA, "setOrthogonalProjection failed");
	}

	@Test
	public void testSetProjectionSimple() {
		float fovY = 90.0f; // Field of view in degrees
		float aspectRatio = 4.0f / 3.0f; // Common aspect ratio
		float zNear = 0.1f; // Near clipping plane
		float zFar = 100.0f; // Far clipping plane

		Matrix4f instance = new Matrix4f();
		instance.setProjection(fovY, aspectRatio, zNear, zFar);

		float radians = fovY / 2 * (float) Math.PI / 180;
		float sine = (float) Math.sin(radians);
		float cotangent = (float) Math.cos(radians) / sine;
		float deltaZ = zFar - zNear;

		// Adjusted expected matrix values after setting projection, in column-major order
		float[] expectedData = {
			cotangent / aspectRatio, 0.0f, 0.0f, 0.0f,
			0.0f, cotangent, 0.0f, 0.0f,
			0.0f, 0.0f, -(zFar + zNear) / deltaZ, -1.0f,
			0.0f, 0.0f, -2 * zNear * zFar / deltaZ, 0.0f
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);
		assertEquals(instance.data, expected.data, DELTA, "setProjection failed");
	}

	@Test
	public void testAdd() {
		System.out.println("add");
		Matrix4f other = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.add(other);
			fail("Added null to Matrix");
		} catch (NullPointerException e) {
		}
		other = new Matrix4f();
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);
		assertSame(instance.add(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.add(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertAllEquals(other.data, 0.0f);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], (float) i);
		}

		for (int i = 0; i < other.data.length; ++i) {
			other.data[i] = i;
		}
		assertSame(instance.add(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], (float) 2 * i);
			assertEquals(other.data[i], (float) i);
		}
	}

	@Test
	public void testSub() {
		System.out.println("sub");
		Matrix4f other = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.sub(other);
			fail("Added null to Matrix");
		} catch (NullPointerException e) {
		}
		other = new Matrix4f();
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);
		assertSame(instance.sub(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.sub(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertAllEquals(other.data, 0.0f);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], (float) i);
		}

		for (int i = 0; i < other.data.length; ++i) {
			other.data[i] = i;
		}
		assertSame(instance.sub(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
			assertEquals(other.data[i], (float) i);
		}
	}

	@Test
	public void testMultiply() {
		System.out.println("multiply");
		Matrix4f other = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.multiply(other);
			fail("Multiplied Matrix with null");
		} catch (NullPointerException e) {
		}
		instance.setIdentity();
		other = new Matrix4f().setIdentity();
		assertSame(instance.multiply(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertEquals(instance.data, other.data);
		assertIsIdentity(instance);
		assertIsIdentity(other);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
			other.data[instance.data.length - i - 1] = i;
		}
		assertNotEquals(instance.data, other.data);
		instance.setColumnMajor(new float[]{
			1, 2, 3, 4,
			5, 6, 7, 8,
			9, 10, 11, 12,
			13, 14, 15, 16
		});
		final float[] otherData = new float[]{
			16, 15, 14, 13,
			12, 11, 10, 9,
			8, 7, 6, 5,
			4, 3, 2, 1
		};
		other.setColumnMajor(otherData);
		float[] expectedData = {
			386, 444, 502, 560,
			274, 316, 358, 400,
			162, 188, 214, 240,
			50, 60, 70, 80
		};

		assertSame(instance.multiply(other), instance);
		assertNotSame(instance, other);
		assertNotSame(instance.data, other.data);
		assertNotEquals(instance.data, other.data);
		assertEquals(other.data, otherData);

		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);
		assertEquals(instance.data, expected.data, DELTA, "Multiplication failed");
	}

	@Test
	public void testSetTranslate() {
		System.out.println("setTranslate");
		float x = 0.0F;
		float y = 0.0F;
		float z = 0.0F;
		Matrix4f instance = new Matrix4f();
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		assertSame(instance.setTranslate(x, y, z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], x);
					break;
				case Matrix4f.YW:
					assertEquals(instance.data[i], y);
					break;
				case Matrix4f.ZW:
					assertEquals(instance.data[i], z);
					break;
				default:
					assertEquals(instance.data[i], 0.0f);
					break;
			}
		}
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		x = 1.0f;
		y = 22.7f;
		z = 92827.1113f;
		assertSame(instance.setTranslate(x, y, z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], x);
					break;
				case Matrix4f.YW:
					assertEquals(instance.data[i], y);
					break;
				case Matrix4f.ZW:
					assertEquals(instance.data[i], z);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testTranslate_Matrix4f() {
		System.out.println("translate");
		float x, y, z;
		Matrix4f instance = new Matrix4f();
		Matrix4f other = null;
		try {
			instance.translate(other);
			fail("Translated Matrix with null Matrix");
		} catch (NullPointerException e) {
		}

		other = new Matrix4f();
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);
		assertSame(instance.translate(other), instance);
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(other.data, 0.0f);
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.translate(other), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], (float) i);
		}
		assertAllEquals(other.data, 0.0f);

		x = 1.0f;
		y = 22.7f;
		z = 92827.1113f;
		other.data[Matrix4f.XW] = x;
		other.data[Matrix4f.YW] = y;
		other.data[Matrix4f.ZW] = z;
		assertSame(instance.translate(x, y, z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], x + i);
					assertEquals(other.data[i], x);
					break;
				case Matrix4f.YW:
					assertEquals(instance.data[i], y + i);
					assertEquals(other.data[i], y);
					break;
				case Matrix4f.ZW:
					assertEquals(instance.data[i], z + i);
					assertEquals(other.data[i], z);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					assertEquals(other.data[i], 0.0f);
					break;
			}
		}
	}

	@Test
	public void testTranslate_3args() {
		System.out.println("translate");
		float x = 0.0F;
		float y = 0.0F;
		float z = 0.0F;
		Matrix4f instance = new Matrix4f();
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		assertSame(instance.translate(x, y, z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		x = 1.0f;
		y = 22.7f;
		z = 92827.1113f;
		assertSame(instance.translate(x, y, z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], x + i);
					break;
				case Matrix4f.YW:
					assertEquals(instance.data[i], y + i);
					break;
				case Matrix4f.ZW:
					assertEquals(instance.data[i], z + i);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testTranslate_Vector4f() {
		System.out.println("translate");
		Vector4f vector = null;
		Matrix4f instance = new Matrix4f();
		try {
			instance.translate(vector);
			fail("Translated Matrix by null vector");
		} catch (NullPointerException e) {
		}
		vector = new Vector4f();
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(vector.data, 0.0f);
		assertSame(instance.translate(vector), instance);
		assertAllEquals(instance.data, 0.0f);
		assertAllEquals(vector.data, 0.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		vector.set(1.0f, 22.7f, 92827.1113f);
		assertSame(instance.translate(vector), instance);
		assertEquals(vector.data[Vector4f.X], 1.0f);
		assertEquals(vector.data[Vector4f.Y], 22.7f);
		assertEquals(vector.data[Vector4f.Z], 92827.1113f);
		for (int i = 0; i < instance.data.length; ++i) {

			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], vector.data[Vector4f.X] + i);
					break;
				case Matrix4f.YW:
					assertEquals(instance.data[i], vector.data[Vector4f.Y] + i);
					break;
				case Matrix4f.ZW:
					assertEquals(instance.data[i], vector.data[Vector4f.Z] + i);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testTranslateX() {
		System.out.println("translateX");
		float x = 0.0F;
		Matrix4f instance = new Matrix4f();
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		assertSame(instance.translateX(x), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		x = 92827.1113f;
		assertSame(instance.translateX(x), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.XW:
					assertEquals(instance.data[i], x + i);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testTranslateY() {
		System.out.println("translateY");
		float y = 0.0F;
		Matrix4f instance = new Matrix4f();
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		assertSame(instance.translateY(y), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		y = 92827.1113f;
		assertSame(instance.translateY(y), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.YW:
					assertEquals(instance.data[i], y + i);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testTranslateZ() {
		System.out.println("translateZ");
		float z = 0.0F;
		Matrix4f instance = new Matrix4f();
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		assertSame(instance.translateZ(z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			assertEquals(instance.data[i], 0.0f);
		}
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		z = 92827.1113f;
		assertSame(instance.translateZ(z), instance);
		for (int i = 0; i < instance.data.length; ++i) {
			switch (i) {
				case Matrix4f.ZW:
					assertEquals(instance.data[i], z + i);
					break;
				default:
					assertEquals(instance.data[i], (float) i);
					break;
			}
		}
	}

	@Test
	public void testRotationWithZeroRadians() {
		Matrix4f matrix = new Matrix4f().setIdentity();
		matrix.rotate(0, new Vector4f(1, 1, 1, false)); // Axis doesn't matter if radians is 0

		Matrix4f expected = new Matrix4f().setIdentity();

		assertEquals(matrix.data, expected.data, DELTA, "Matrix should remain unchanged with 0 radians rotation.");
	}

	@Test
	public void testRotateAroundXAxis() {
		Matrix4f matrix = new Matrix4f().setIdentity();
		float radians = (float) Math.toRadians(90);
		matrix.rotate(radians, new Vector4f(1, 0, 0, false));

		float[] expectedData = {
			1f, 0f, 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, -1f, 0f, 0f,
			0f, 0f, 0f, 1f
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);

		assertEquals(matrix.data, expected.data, DELTA, "Rotation around X-axis failed.");
	}

	@Test
	public void testRotateAroundYAxis() {
		Matrix4f matrix = new Matrix4f().setIdentity();
		float radians = (float) Math.toRadians(90);
		matrix.rotate(radians, new Vector4f(0, 1, 0, false));

		float[] expectedData = {
			0f, 0f, -1f, 0f,
			0f, 1f, 0f, 0f,
			1f, 0f, 0f, 0f,
			0f, 0f, 0f, 1f
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);

		assertEquals(matrix.data, expected.data, DELTA, "Rotation around Y-axis failed.");
	}

	@Test
	public void testRotateAroundZAxis() {
		Matrix4f matrix = new Matrix4f().setIdentity();
		float radians = (float) Math.toRadians(90);
		matrix.rotate(radians, new Vector4f(0, 0, 1, false));

		float[] expectedData = {
			0f, 1f, 0f, 0f,
			-1f, 0f, 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);

		assertEquals(matrix.data, expected.data, DELTA, "Rotation around Z-axis failed.");
	}

	@Test
	public void testRotateAroundArbitraryAxis() {
		Matrix4f matrix = new Matrix4f().setIdentity();
		float radians = (float) Math.toRadians(45);
		matrix.rotate(radians, new Vector4f(1, 1, 1, false).normalize());

		float[] expectedData = {
			0.8047f, 0.5050f, -0.3106f, 0f, // First column
			-0.3106f, 0.8047f, 0.5050f, 0f, // Second column
			0.5050f, -0.3106f, 0.8047f, 0f, // Third column
			0f, 0f, 0f, 1f // Fourth column (homogeneous coordinates)
		};
		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);

		assertEquals(matrix.data, expected.data, DELTA, "Rotation around an arbitrary axis failed.");
	}

	@Test
	public void testSetLookSimple() {
		Vector4f position = new Vector4f(0f, 0f, 0f, true);
		Vector4f right = new Vector4f(1f, 0f, 0f, false);
		Vector4f up = new Vector4f(0f, 1f, 0f, false);
		Vector4f look = new Vector4f(0f, 0f, -1f, false);

		Matrix4f matrix = new Matrix4f();
		matrix.setLook(position, right, up, look);

		// Expected matrix for a camera looking along the negative Z-axis, with up as positive Y
		float[] expectedData = {
			1f, 0f, 0f, 0f, // Right
			0f, 1f, 0f, 0f, // Up
			0f, 0f, -1f, 0f, // Look (forward direction is negative Z)
			0f, 0f, 0f, 1f // Homogeneous coordinate
		};

		Matrix4f expected = new Matrix4f().setColumnMajor(expectedData);
		assertEquals(matrix.data, expected.data, DELTA, "setLook failed");
	}

	@Test
	public void testSetLookComplex() {
		Vector4f position = new Vector4f(1f, 2f, 3f, true); // Position vector
		Vector4f target = new Vector4f(4f, 5f, 6f, true); // Target position
		Vector4f up = new Vector4f(0f, 1f, 0f, false); // Up direction

		Vector4f lookDirection = target.clone().sub(position).normalize();

		Vector4f right = up.cross(lookDirection).normalize();

		Vector4f actualUp = lookDirection.cross(right).normalize();

		Matrix4f matrix = new Matrix4f();
		matrix.setLook(position, right, actualUp, lookDirection);

		Matrix4f inverseLookAt = matrix.clone().setTranspose();

		Vector4f transformedTarget = target.clone().transform(inverseLookAt);

		assertTrue(transformedTarget.data[Vector4f.Z] < position.data[Vector4f.Z], "The target is not in front of the camera after transformation.");
	}

	@Test
	public void testBuffer() {
		System.out.println("buffer");
		Matrix4f instance = new Matrix4f();
		FloatBuffer result = instance.buffer();
		assertNotNull(result);
		assertSame(result, instance.buffer);
		for (int i = 0; i < instance.buffer.capacity(); ++i) {
			assertEquals(result.get(i), instance.buffer.get(i));
		}

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		result = instance.buffer();
		assertNotNull(result);
		assertSame(result, instance.buffer);
		for (int i = 0; i < instance.buffer.capacity(); ++i) {
			assertEquals(result.get(i), instance.buffer.get(i));
			assertEquals(result.get(i), instance.data[i]);
		}
	}

	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		Matrix4f instance = new Matrix4f();
		final int result = instance.hashCode();
		assertNotEquals(result, 0);
		assertEquals(result, instance.hashCode());
		Matrix4f other = new Matrix4f();
		assertEquals(result, instance.hashCode());
		assertEquals(instance.hashCode(), other.hashCode());
		final Random random = new SecureRandom();
		for (int i = 0; i < 100; ++i) {
			for (int j = 0; j < instance.data.length; ++j) {
				instance.data[j] = random.nextFloat();
				other.data[j] = instance.data[j];
			}
			assertNotEquals(instance.hashCode(), result);
			assertEquals(instance.hashCode(), other.hashCode());
		}
	}

	@Test
	public void testEquals_Object() {
		System.out.println("equals");
		Object obj = null;
		Matrix4f instance = new Matrix4f();
		assertFalse(instance.equals(obj));
		obj = new Object();
		assertFalse(instance.equals(obj));
	}

	@Test
	public void testEquals_Matrix4f() {
		System.out.println("equals");
		Matrix4f other = null;
		Matrix4f instance = new Matrix4f();
		assertFalse(instance.equals(other));

		other = new Matrix4f();
		assertTrue(instance.equals(other));
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertFalse(instance.equals(other));
		for (int i = 0; i < other.data.length; ++i) {
			other.data[i] = i;
		}
		assertTrue(instance.equals(other));
	}

	@Test
	public void testClone() {
		System.out.println("clone");
		Matrix4f instance = new Matrix4f();
		final Matrix4f result = instance.clone();
		assertNotSame(result, instance);
		assertNotSame(result.data, instance.data);
		assertNotSame(result.buffer, instance.buffer);
		assertEquals(result.data, instance.data);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		final Matrix4f result2 = instance.clone();
		assertNotSame(result2, instance);
		assertNotSame(result2, result);
		assertNotSame(result2.data, instance.data);
		assertNotSame(result2.data, result.data);
		assertNotSame(result2.buffer, instance.buffer);
		assertNotSame(result2.buffer, result.buffer);
		assertEquals(result2.data, instance.data);
		assertNotEquals(result2.data, result.data);
	}

	@Test
	public void testToString() {
		System.out.println("toString");
		Matrix4f instance = new Matrix4f();
		String result = instance.toString();
		assertNotNull(result);
		assertNotEquals(result.length(), 0);
		assertEquals(instance.toString(), result);
		assertTrue(result.length() >= instance.data.length * 2); // minimum would be 0 and space per data values
	}
}
