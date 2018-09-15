package exp.libs.utils.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.utils.other.ListUtils;

public class TestListUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRemoveDuplicate() {
		fail("Not yet implemented");
	}

	@Test
	public void testCutbackNull() {
		String[] array = new String[] { null, "aaa", "bbb", null, "ccc", null, "ddd" };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { null, "aaa", "bbb", null, null, null, "ccc", null, "ddd", null };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { "aaa", "bbb", null, null, null, "ccc", null, "ddd", null };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { "aaa", null, null, null, null };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { null, null, null, null };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { "aaa", "bbb", "ccc", "ddd" };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = new String[] { };
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
		
		array = null;
		System.out.println(ListUtils.toString(array));
		System.out.println(ListUtils.cutbackNull(array));
		System.out.println(ListUtils.toString(array));
	}

	@Test
	public void testCheckSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersection() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtraction() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnion() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareCollectionOfECollectionOfE() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareListOfEListOfE() {
		fail("Not yet implemented");
	}

}
