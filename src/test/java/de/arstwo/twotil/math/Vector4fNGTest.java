/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class Vector4fNGTest {

	private static final float DELTA = 1e-3f; // delta for float comparison

	public Vector4fNGTest() {
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

	@Test
	public void testSet_Vector4f() {
		System.out.println("set");
		Vector4f other = null;
		Vector4f instance = new Vector4f();
		try {
			instance.set(other);
			fail("Set vector to null");
		} catch (NullPointerException e) {
		}
		other = new Vector4f();
		assertSame(instance.set(other), instance);
		assertNotSame(instance, other);
		assertEquals(instance, other);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
			other.data[other.data.length - i - 1] = i;
		}
		assertNotEquals(instance, other);
		assertSame(instance.set(other), instance);
		assertNotSame(instance, other);
		assertEquals(instance, other);
	}

	@Test
	public void testSet_3args() {
		System.out.println("set");
		float x = 0.0F;
		float y = 0.0F;
		float z = 0.0F;
		Vector4f instance = new Vector4f();
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertSame(instance.set(x, y, z), instance);
		assertEquals(instance.data[Vector4f.X], x);
		assertEquals(instance.data[Vector4f.Y], y);
		assertEquals(instance.data[Vector4f.Z], z);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		x = 1.0f;
		y = 22.5544f;
		z = 1132.22f;
		assertNotEquals(instance.data[Vector4f.X], x);
		assertNotEquals(instance.data[Vector4f.Y], y);
		assertNotEquals(instance.data[Vector4f.Z], z);
		assertSame(instance.set(x, y, z), instance);
		assertEquals(instance.data[Vector4f.X], x);
		assertEquals(instance.data[Vector4f.Y], y);
		assertEquals(instance.data[Vector4f.Z], z);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testSetX() {
		System.out.println("setX");
		float x = 0.0F;
		Vector4f instance = new Vector4f();
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertSame(instance.setX(x), instance);
		assertEquals(instance.data[Vector4f.X], x);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		x = 1.0f;
		assertNotEquals(instance.data[Vector4f.X], x);
		assertSame(instance.setX(x), instance);
		assertEquals(instance.data[Vector4f.X], x);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testSetY() {
		System.out.println("setY");
		float y = 0.0F;
		Vector4f instance = new Vector4f();
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertSame(instance.setY(y), instance);
		assertEquals(instance.data[Vector4f.Y], y);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		y = 22.5544f;
		assertNotEquals(instance.data[Vector4f.Y], y);
		assertSame(instance.setY(y), instance);
		assertEquals(instance.data[Vector4f.Y], y);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testSetZ() {
		System.out.println("setZ");
		float z = 0.0F;
		Vector4f instance = new Vector4f();
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertSame(instance.setZ(z), instance);
		assertEquals(instance.data[Vector4f.Z], z);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		z = 1132.22f;
		assertNotEquals(instance.data[Vector4f.Z], z);
		assertSame(instance.setZ(z), instance);
		assertEquals(instance.data[Vector4f.Z], z);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testSet_floatArr() {
		System.out.println("set");
		float[] data = null;
		Vector4f instance = new Vector4f();
		try {
			instance.set(data);
			fail("Set vector to null data");
		} catch (NullPointerException e) {
		}
		data = new float[1];
		try {
			instance.set(data);
			fail("Set vector to illegal length data");
		} catch (IllegalArgumentException e) {
		}
		data = new float[instance.data.length + 1];
		try {
			instance.set(data);
			fail("Set vector to illegal length data");
		} catch (IllegalArgumentException e) {
		}

		data = new float[instance.data.length];
		assertSame(instance.set(data), instance);
		assertNotSame(instance.data, data);
		assertEquals(instance.data, data);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
			data[data.length - i - 1] = i;
		}
		assertNotEquals(instance.data, data);
		assertSame(instance.set(data), instance);
		assertNotSame(instance.data, data);
		assertEquals(instance.data, data);
	}

	@Test
	public void testSetUnchecked() {
		System.out.println("setUnchecked");
		float[] data = null;
		Vector4f instance = new Vector4f();
		try {
			instance.setUnchecked(data);
			fail("Set vector to null data");
		} catch (NullPointerException e) {
		}
		data = new float[1];
		try {
			instance.setUnchecked(data);
			fail("Set vector to illegal length data");
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		data = new float[instance.data.length + 1];
		instance.setUnchecked(data);

		data = new float[instance.data.length];
		assertSame(instance.setUnchecked(data), instance);
		assertNotSame(instance.data, data);
		assertEquals(instance.data, data);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
			data[data.length - i - 1] = i;
		}
		assertNotEquals(instance.data, data);
		assertSame(instance.setUnchecked(data), instance);
		assertNotSame(instance.data, data);
		assertEquals(instance.data, data);
	}

	@Test
	public void testSetZero() {
		System.out.println("setZero");
		Vector4f instance = new Vector4f();
		assertSame(instance.setZero(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.setZero(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], (float) (instance.data.length - 1));
	}

	@Test
	public void testSetZeroPosition() {
		System.out.println("setZeroPosition");
		Vector4f instance = new Vector4f();
		assertSame(instance.setZeroPosition(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.setZeroPosition(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
	}

	@Test
	public void testSetZeroDirection() {
		System.out.println("setZeroDirection");
		Vector4f instance = new Vector4f();
		assertSame(instance.setZeroDirection(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.setZeroDirection(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testIsDirection() {
		System.out.println("isDirection");
		Vector4f instance = new Vector4f();
		instance.data[Vector4f.W] = 0.0f;
		assertTrue(instance.isDirection());
		instance.data[Vector4f.W] = 1.0f;
		assertFalse(instance.isDirection());
		instance.data[Vector4f.W] = -1.0f;
		assertFalse(instance.isDirection());
		instance.data[Vector4f.W] = 0.3f;
		assertFalse(instance.isDirection());
		instance.data[Vector4f.W] = 0.0f;
		assertTrue(instance.isDirection());
	}

	@Test
	public void testIsPosition() {
		System.out.println("isPosition");
		Vector4f instance = new Vector4f();
		instance.data[Vector4f.W] = 0.0f;
		assertFalse(instance.isPosition());
		instance.data[Vector4f.W] = 1.0f;
		assertTrue(instance.isPosition());
		instance.data[Vector4f.W] = -1.0f;
		assertFalse(instance.isPosition());
		instance.data[Vector4f.W] = 0.3f;
		assertFalse(instance.isPosition());
		instance.data[Vector4f.W] = 1.0f;
		assertTrue(instance.isPosition());
	}

	@Test
	public void testSetIsDirection() {
		System.out.println("setIsDirection");
		Vector4f instance = new Vector4f();
		assertSame(instance.setIsDirection(), instance);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.setIsDirection(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 2.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
	}

	@Test
	public void testSetIsPosition() {
		System.out.println("setIsPosition");
		Vector4f instance = new Vector4f();
		assertSame(instance.setIsPosition(), instance);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		for (int i = 0; i < instance.data.length; ++i) {
			instance.data[i] = i;
		}
		assertSame(instance.setIsPosition(), instance);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 2.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
	}

	@Test
	public void testLengthSquared() {
		System.out.println("lengthSquared");
		Vector4f instance = new Vector4f();
		float result = instance.lengthSquared();
		assertEquals(result, 0.0f, 0.0f);

		instance.set(1.0f, 0.0f, 0.0f);
		result = instance.lengthSquared();
		assertEquals(result, 1.0f, 0.0f);
		instance.set(0.0f, 1.0f, 0.0f);
		result = instance.lengthSquared();
		assertEquals(result, 1.0f, 0.0f);
		instance.set(0.0f, 0.0f, 1.0f);
		result = instance.lengthSquared();
		assertEquals(result, 1.0f, 0.0f);

		float x, y, z, expected;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 100; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			expected = x * x + y * y + z * z;
			instance.set(x, y, z);
			result = instance.lengthSquared();

			assertEquals(result, expected, 0.0f);
		}
	}

	@Test
	public void testLength() {
		System.out.println("length");
		Vector4f instance = new Vector4f();
		float result = instance.lengthSquared();
		assertEquals(result, 0.0f, 0.0f);

		instance.set(1.0f, 0.0f, 0.0f);
		result = instance.length();
		assertEquals(result, 1.0f, 0.0f);
		instance.set(0.0f, 1.0f, 0.0f);
		result = instance.length();
		assertEquals(result, 1.0f, 0.0f);
		instance.set(0.0f, 0.0f, 1.0f);
		result = instance.length();
		assertEquals(result, 1.0f, 0.0f);

		double x, y, z;
		float expected;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 100; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			if ((x == 0.0) && (y == 0.0) && (z == 0.0)) {
				x = 1.0;
			}
			expected = (float) Math.sqrt(x * x + y * y + z * z);
			instance.set((float) x, (float) y, (float) z);
			result = instance.length();

			assertEquals(result, expected, 0.000001f);
		}
	}

	@Test
	public void testNormalize() {
		System.out.println("normalize");
		Vector4f instance = new Vector4f();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		try {
			instance.normalize();
			fail("Normalized null vector to " + instance.toString());
		} catch (ArithmeticException e) {
		}

		instance.setIsPosition();
		assertEquals(instance.data[Vector4f.W], 1.0f);
		try {
			instance.normalize();
			fail("Normalized null vector to " + instance.toString());
		} catch (ArithmeticException e) {
		}

		instance.setIsDirection();
		instance.set(1.0f, 0.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(1.0f, 0.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		instance.setIsDirection();
		instance.set(0.0f, 1.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(0.0f, 1.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		instance.setIsDirection();
		instance.set(0.0f, 0.0f, 1.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(0.0f, 0.0f, 1.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		instance.setIsDirection();
		instance.set(2.0f, 0.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(2.0f, 0.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		instance.setIsDirection();
		instance.set(0.0f, 2.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(0.0f, 2.0f, 0.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		instance.setIsDirection();
		instance.set(0.0f, 0.0f, 2.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		instance.setIsPosition();
		instance.set(0.0f, 0.0f, 2.0f);
		instance.normalize();
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		double x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			if ((x == 0.0) && (y == 0.0) && (z == 0.0)) {
				x = 1.0;
			}
			instance.set((float) x, (float) y, (float) z);
			if (instance.isPosition()) {
				instance.setIsDirection();
			} else {
				instance.setIsPosition();
			};
			instance.normalize();
			assertEquals(instance.lengthSquared(), 1.0f, 0.000001f);
		}
	}

	@Test
	public void testAdd_3args() {
		System.out.println("add");
		float xAdd = 0.0F;
		float yAdd = 0.0F;
		float zAdd = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.add(xAdd, yAdd, zAdd);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.add(xAdd, yAdd, zAdd);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xAdd = rand.nextFloat() * 100.0f;
			yAdd = rand.nextFloat() * 100.0f;
			zAdd = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.add(xAdd, yAdd, zAdd);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.add(xAdd, yAdd, zAdd);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testAdd_Vector4f() {
		System.out.println("add");
		float xAdd = 0.0F;
		float yAdd = 0.0F;
		float zAdd = 0.0F;
		Vector4f instance = new Vector4f();
		Vector4f other = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.set(xAdd, yAdd, zAdd);
		other.setIsDirection();
		instance.add(other);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], xAdd);
		assertEquals(other.data[Vector4f.Y], yAdd);
		assertEquals(other.data[Vector4f.Z], zAdd);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		other.set(xAdd, yAdd, zAdd);
		other.setIsDirection();
		instance.add(other);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], xAdd);
		assertEquals(other.data[Vector4f.Y], yAdd);
		assertEquals(other.data[Vector4f.Z], zAdd);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.set(xAdd, yAdd, zAdd);
		other.setIsPosition();
		instance.add(other);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], xAdd);
		assertEquals(other.data[Vector4f.Y], yAdd);
		assertEquals(other.data[Vector4f.Z], zAdd);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		other.set(xAdd, yAdd, zAdd);
		other.setIsPosition();
		instance.add(other);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], xAdd);
		assertEquals(other.data[Vector4f.Y], yAdd);
		assertEquals(other.data[Vector4f.Z], zAdd);
		assertEquals(other.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xAdd = rand.nextFloat() * 100.0f;
			yAdd = rand.nextFloat() * 100.0f;
			zAdd = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			other.set(xAdd, yAdd, zAdd);
			other.setIsDirection();
			instance.add(other);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 0.0f);
			assertEquals(other.data[Vector4f.X], xAdd);
			assertEquals(other.data[Vector4f.Y], yAdd);
			assertEquals(other.data[Vector4f.Z], zAdd);
			assertEquals(other.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			other.set(xAdd, yAdd, zAdd);
			other.setIsDirection();
			instance.add(other);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 1.0f);
			assertEquals(other.data[Vector4f.X], xAdd);
			assertEquals(other.data[Vector4f.Y], yAdd);
			assertEquals(other.data[Vector4f.Z], zAdd);
			assertEquals(other.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsDirection();
			other.set(xAdd, yAdd, zAdd);
			other.setIsPosition();
			instance.add(other);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 0.0f);
			assertEquals(other.data[Vector4f.X], xAdd);
			assertEquals(other.data[Vector4f.Y], yAdd);
			assertEquals(other.data[Vector4f.Z], zAdd);
			assertEquals(other.data[Vector4f.W], 1.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			other.set(xAdd, yAdd, zAdd);
			other.setIsPosition();
			instance.add(other);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 1.0f);
			assertEquals(other.data[Vector4f.X], xAdd);
			assertEquals(other.data[Vector4f.Y], yAdd);
			assertEquals(other.data[Vector4f.Z], zAdd);
			assertEquals(other.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testAddX() {
		System.out.println("addX");
		float xAdd = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.addX(xAdd);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.addX(xAdd);
		assertEquals(instance.data[Vector4f.X], xAdd);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xAdd = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.addX(xAdd);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.addX(xAdd);
			assertEquals(instance.data[Vector4f.X], x + xAdd);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testAddY() {
		System.out.println("addY");
		float yAdd = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.addY(yAdd);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.addY(yAdd);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], yAdd);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			yAdd = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.addY(yAdd);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.addY(yAdd);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y + yAdd);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testAddZ() {
		System.out.println("addZ");
		float zAdd = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.addZ(zAdd);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.addZ(zAdd);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], zAdd);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			zAdd = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.addZ(zAdd);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.addZ(zAdd);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z + zAdd);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testSub_3args() {
		System.out.println("sub");
		float xSub = 0.0F;
		float ySub = 0.0F;
		float zSub = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.sub(xSub, ySub, zSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.sub(xSub, ySub, zSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xSub = rand.nextFloat() * 100.0f;
			ySub = rand.nextFloat() * 100.0f;
			zSub = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.sub(xSub, ySub, zSub);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.sub(xSub, ySub, zSub);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testSub_Vector4f() {
		System.out.println("sub");
		float xSub = 0.0F;
		float ySub = 0.0F;
		float zSub = 0.0F;
		Vector4f instance = new Vector4f();
		Vector4f other = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.set(xSub, ySub, zSub);
		other.setIsDirection();
		instance.sub(other);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		other.set(xSub, ySub, zSub);
		other.setIsDirection();
		instance.sub(other);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.set(xSub, ySub, zSub);
		other.setIsPosition();
		instance.sub(other);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		other.set(xSub, ySub, zSub);
		other.setIsPosition();
		instance.sub(other);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xSub = rand.nextFloat() * 100.0f;
			ySub = rand.nextFloat() * 100.0f;
			zSub = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			other.set(xSub, ySub, zSub);
			other.setIsDirection();
			instance.sub(other);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 0.0f);
			assertEquals(other.data[Vector4f.X], xSub);
			assertEquals(other.data[Vector4f.Y], ySub);
			assertEquals(other.data[Vector4f.Z], zSub);
			assertEquals(other.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			other.set(xSub, ySub, zSub);
			other.setIsDirection();
			instance.sub(other);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 1.0f);
			assertEquals(other.data[Vector4f.X], xSub);
			assertEquals(other.data[Vector4f.Y], ySub);
			assertEquals(other.data[Vector4f.Z], zSub);
			assertEquals(other.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsDirection();
			other.set(xSub, ySub, zSub);
			other.setIsPosition();
			instance.sub(other);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 0.0f);
			assertEquals(other.data[Vector4f.X], xSub);
			assertEquals(other.data[Vector4f.Y], ySub);
			assertEquals(other.data[Vector4f.Z], zSub);
			assertEquals(other.data[Vector4f.W], 1.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			other.set(xSub, ySub, zSub);
			other.setIsPosition();
			instance.sub(other);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 1.0f);
			assertEquals(other.data[Vector4f.X], xSub);
			assertEquals(other.data[Vector4f.Y], ySub);
			assertEquals(other.data[Vector4f.Z], zSub);
			assertEquals(other.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testSubX() {
		System.out.println("subX");
		float xSub = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.subX(xSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.subX(xSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xSub = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.subX(xSub);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.subX(xSub);
			assertEquals(instance.data[Vector4f.X], x - xSub);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testSubY() {
		System.out.println("subY");
		float ySub = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.subY(ySub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.subY(ySub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			ySub = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.subY(ySub);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.subY(ySub);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y - ySub);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testSubZ() {
		System.out.println("subZ");
		float zSub = 0.0F;
		Vector4f instance = new Vector4f();
		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		instance.subZ(zSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);

		instance.set(0.0f, 0.0f, 0.0f);
		instance.setIsPosition();
		instance.subZ(zSub);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);

		float x, y, z;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 200; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			zSub = rand.nextFloat() * 100.0f;

			instance.set(x, y, z);
			instance.setIsDirection();
			instance.subZ(zSub);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 0.0f);

			instance.set(x, y, z);
			instance.setIsPosition();
			instance.subZ(zSub);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z - zSub);
			assertEquals(instance.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testShear() {
		final Vector4f instance = new Vector4f();
		Vector4f result = instance.shear(0f, 0f, 0f);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shear(0f, 0f, 0f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 0f);
		assertEquals(instance.getY(), 0f);
		assertEquals(instance.getZ(), 0f);
		assertTrue(instance.isPosition());

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shear(1f, 1f, 1f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsPosition();
		result = instance.shear(0.22f, -93836.3f, 15f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f * 0.22f);
		assertEquals(instance.getY(), 2f * -93836.3f);
		assertEquals(instance.getZ(), 1f * 15f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsDirection();
		result = instance.shear(0.22f, -93836.3f, 15f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f * 0.22f);
		assertEquals(instance.getY(), 2f * -93836.3f);
		assertEquals(instance.getZ(), 1f * 15f);
		assertTrue(instance.isDirection());
	}

	@Test
	public void testShearX() {
		System.out.println("shearX");
		final Vector4f instance = new Vector4f();
		Vector4f result = instance.shearX(0f);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearX(0f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 0f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearX(1f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsPosition();
		result = instance.shearX(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f * 0.22f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 1f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsDirection();
		result = instance.shearX(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f * 0.22f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 1f);
		assertTrue(instance.isDirection());
	}

	@Test
	public void testShearY() {
		System.out.println("shearY");
		final Vector4f instance = new Vector4f();
		Vector4f result = instance.shearY(0f);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearY(0f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 0f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearY(1f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsPosition();
		result = instance.shearY(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f);
		assertEquals(instance.getY(), 2f * 0.22f);
		assertEquals(instance.getZ(), 1f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsDirection();
		result = instance.shearY(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f);
		assertEquals(instance.getY(), 2f * 0.22f);
		assertEquals(instance.getZ(), 1f);
		assertTrue(instance.isDirection());
	}

	@Test
	public void testShearZ() {
		System.out.println("shearZ");
		final Vector4f instance = new Vector4f();
		Vector4f result = instance.shearZ(0f);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearZ(0f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 0f);
		assertTrue(instance.isPosition());

		instance.set(1f, 2f, 3f).setIsPosition();
		result = instance.shearZ(1f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsPosition();
		result = instance.shearZ(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 1f * 0.22f);
		assertTrue(instance.isPosition());

		instance.set(3f, 2f, 1f).setIsDirection();
		result = instance.shearZ(0.22f);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 3f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 1f * 0.22f);
		assertTrue(instance.isDirection());
	}

	@Test
	public void testScale() {
		final Vector4f instance = new Vector4f();
		float magnitude = 0.0f;
		Vector4f result = instance.scale(magnitude);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 3f, 2f).setIsPosition();
		result = instance.scale(magnitude);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 0f);
		assertEquals(instance.getY(), 0f);
		assertEquals(instance.getZ(), 0f);
		assertEquals(instance.isPosition(), true);

		magnitude = 1.0f;
		instance.set(1f, 3f, 2f).setIsPosition();
		result = instance.scale(magnitude);
		assertEquals(result, instance);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 3f);
		assertEquals(instance.getZ(), 2f);
		assertEquals(instance.isPosition(), true);

		magnitude = 37.56f;
		instance.set(-21f, 333333f, 0.0000022f).setIsPosition();
		result = instance.scale(magnitude);
		assertEquals(result, instance);
		// float precision means it has to be correct on about 6 digits
		assertEquals(instance.getX(), -788.76f, 0.01f);
		assertEquals(instance.getY(), 12519987.48f, 10f);
		assertEquals(instance.getZ(), 0.000082632f, 0.000001f);
		assertEquals(instance.isPosition(), true);
	}

	@Test
	public void testDot() {
		System.out.println("dot");
		Vector4f other = null;
		Vector4f instance = new Vector4f();
		try {
			instance.dot(other);
			fail("Allowed dot(null)");
		} catch (NullPointerException e) {
		}

		other = new Vector4f();
		float result;
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(1.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 1.0f, 0.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 0.0f, 1.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 0.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		// --- dot of same axis --------------------------------------------------
		instance.set(1.0f, 0.0f, 0.0f);
		other.set(1.0f, 0.0f, 0.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 1.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 1.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 1.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 1.0f, 0.0f);
		other.set(0.0f, 1.0f, 0.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 1.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 1.0f);
		assertEquals(instance.data[Vector4f.Z], 0.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 1.0f);
		assertEquals(other.data[Vector4f.Z], 0.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		instance.set(0.0f, 0.0f, 1.0f);
		other.set(0.0f, 0.0f, 1.0f);
		instance.setIsDirection();
		other.setIsDirection();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 0.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 1.0f);
		assertEquals(other.data[Vector4f.W], 0.0f);

		instance.setIsPosition();
		other.setIsPosition();
		result = instance.dot(other);
		assertEquals(result, 1.0f, 0.0f);
		assertEquals(instance.data[Vector4f.X], 0.0f);
		assertEquals(instance.data[Vector4f.Y], 0.0f);
		assertEquals(instance.data[Vector4f.Z], 1.0f);
		assertEquals(instance.data[Vector4f.W], 1.0f);
		assertEquals(other.data[Vector4f.X], 0.0f);
		assertEquals(other.data[Vector4f.Y], 0.0f);
		assertEquals(other.data[Vector4f.Z], 1.0f);
		assertEquals(other.data[Vector4f.W], 1.0f);

		float x, y, z, xO, yO, zO, expected;
		final Random rand = new SecureRandom();
		for (int i = 0; i < 100; ++i) {
			x = rand.nextFloat() * 100.0f;
			y = rand.nextFloat() * 100.0f;
			z = rand.nextFloat() * 100.0f;
			xO = rand.nextFloat() * 100.0f;
			yO = rand.nextFloat() * 100.0f;
			zO = rand.nextFloat() * 100.0f;
			expected = x * xO + y * yO + z * zO;

			instance.set(x, y, z);
			other.set(xO, yO, zO);
			instance.setIsDirection();
			other.setIsDirection();
			result = instance.dot(other);
			assertEquals(result, expected, 0.0f);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 0.0f);
			assertEquals(other.data[Vector4f.X], xO);
			assertEquals(other.data[Vector4f.Y], yO);
			assertEquals(other.data[Vector4f.Z], zO);
			assertEquals(other.data[Vector4f.W], 0.0f);

			instance.setIsPosition();
			other.setIsPosition();
			result = instance.dot(other);
			assertEquals(result, expected, 0.0f);
			assertEquals(instance.data[Vector4f.X], x);
			assertEquals(instance.data[Vector4f.Y], y);
			assertEquals(instance.data[Vector4f.Z], z);
			assertEquals(instance.data[Vector4f.W], 1.0f);
			assertEquals(other.data[Vector4f.X], xO);
			assertEquals(other.data[Vector4f.Y], yO);
			assertEquals(other.data[Vector4f.Z], zO);
			assertEquals(other.data[Vector4f.W], 1.0f);
		}
	}

	@Test
	public void testCross() {
		final Vector4f instance = new Vector4f();
		final Vector4f other = new Vector4f(0, 0, 0, false);
		Vector4f result = instance.cross(other);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		instance.cross(other);
		assertEquals(instance.getX(), 0f);
		assertEquals(instance.getY(), 0f);
		assertEquals(instance.getZ(), 0f);
		assertEquals(instance.isPosition(), true);

		instance.set(1f, 2f, 3f).setIsPosition();
		other.set(3f, 2f, 1f).setIsPosition();
		instance.cross(other);
		assertEquals(instance.getX(), -4f);
		assertEquals(instance.getY(), 8f);
		assertEquals(instance.getZ(), -4f);

		instance.set(3.2f, -5.9f, 1133.9982f).setIsPosition();
		other.set(-73f, 2.22222f, -312321f).setIsPosition();
		instance.cross(other);
		// float precision means it has to be correct on about 6 digits
		assertEquals(instance.getX(), 1.84017e6f, 10f);
		assertEquals(instance.getY(), 916645f, 1f);
		assertEquals(instance.getZ(), -423.589f, 0.0001f);
	}

	@Test
	public void testTransform() {
		final Vector4f instance = new Vector4f();
		final Matrix4f matrix = new Matrix4f();
		Vector4f result = instance.transform(matrix);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		matrix.setIdentity();
		instance.transform(matrix);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);

		instance.set(1f, 2f, 3f).setIsPosition();
		matrix.setIdentity().setTranslate(2f, 10f, 100f);
		instance.transform(matrix);
		assertEquals(instance.getX(), 3f, DELTA);
		assertEquals(instance.getY(), 12f, DELTA);
		assertEquals(instance.getZ(), 103f, DELTA);
	}

	@Test
	public void testTranslate() {
		System.out.println("translate");
		final Vector4f instance = new Vector4f();
		final Matrix4f matrix = new Matrix4f();
		Vector4f result = instance.translate(matrix);
		assertEquals(result, instance);
		for (float f : instance.data) {
			assertEquals(f, 0f);
		}

		instance.set(1f, 2f, 3f).setIsPosition();
		matrix.setIdentity();
		instance.translate(matrix);
		assertEquals(instance.getX(), 1f);
		assertEquals(instance.getY(), 2f);
		assertEquals(instance.getZ(), 3f);

		instance.set(1f, 2f, 3f).setIsPosition();
		matrix.setIdentity().setTranslate(2f, 10f, 100f);
		instance.translate(matrix);
		assertEquals(instance.getX(), 3f, DELTA);
		assertEquals(instance.getY(), 12f, DELTA);
		assertEquals(instance.getZ(), 103f, DELTA);

		instance.set(1f, 2f, 3f).setIsPosition();
		final float[] matrixData = new float[]{
			1f, 293f, -9f, 2f,
			0.033f, 1f, -9f, 10f,
			-93f, 293f, 1f, 100f,
			55.098f, -900012f, -9f, 1f,};
		matrix.setRowMajor(matrixData);
		instance.translate(matrix);
		assertEquals(instance.getX(), 3f, DELTA);
		assertEquals(instance.getY(), 12f, DELTA);
		assertEquals(instance.getZ(), 103f, DELTA);
	}

	@Test
	public void testBuffer() {
		System.out.println("buffer");
		Vector4f instance = new Vector4f();
		final FloatBuffer iBuffer = instance.buffer();
		assertNotNull(iBuffer);
		final float[] iBufferArray = iBuffer.array();
		assertNotNull(iBufferArray);
		assertEquals(iBufferArray.length, Vector4f.LENGTH);

		float[] expectedData = new float[4];
		instance.set(expectedData);
		assertSame(instance.buffer(), iBuffer);
		assertSame(instance.buffer().array(), iBufferArray);
		for (int i = 0; i < expectedData.length; ++i) {
			assertEquals(iBufferArray[i], expectedData[i]);
		}

		expectedData = new float[]{1f, 2f, 3f, 4f};
		instance.set(expectedData);
		assertSame(instance.buffer(), iBuffer);
		assertSame(instance.buffer().array(), iBufferArray);
		for (int i = 0; i < expectedData.length; ++i) {
			assertEquals(iBufferArray[i], expectedData[i]);
		}
	}

}
