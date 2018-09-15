package exp.libs.utils.test;

import static org.junit.Assert.*;

import org.junit.Test;

import exp.libs.utils.num.NumUtils;

public class TestNumUtils {

	@Test
	public void testCompare() {
		fail("Not yet implemented");
	}

	@Test
	public void testNumToPrecent() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrecentToNum() {
		fail("Not yet implemented");
	}

	@Test
	public void testToInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testTolong() {
		fail("Not yet implemented");
	}

	@Test
	public void testToFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testToDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncrement() {
		fail("Not yet implemented");
	}

	@Test
	public void testToNegativeInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testToNegativelong() {
		fail("Not yet implemented");
	}

	@Test
	public void testToPositiveInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testToPositivelong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaxlonglong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaxIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinlonglong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompress() {
		System.out.println(NumUtils.compress(null));
		System.out.println(NumUtils.compress(new int[] {}));
		System.out.println(NumUtils.compress(new int[] { 56 }));
		System.out.println(NumUtils.compress(new int[] { 1, 3, 5, 7, 9 }));
		System.out.println(NumUtils.compress(new int[] { 0, 2, 4, 6, 8 }));
		System.out.println(NumUtils.compress(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
		System.out.println(NumUtils.compress(new int[] { 1, 2, 3, 5, 6, 8 }));
		System.out.println(NumUtils.compress(new int[] { 1, 2, 3, 3, 5, 6, 8 }));
		System.out.println(NumUtils.compress(new int[] { 1, 2, 3, 5, 6, 9 }));
		System.out.println(NumUtils.compress(new int[] { 1, 2, 3, 5, 6, 8, 10 }));
		System.out.println(NumUtils.compress(new int[] { 1, 2, 3, 5, 6, 8, 9 }));
	}

}
