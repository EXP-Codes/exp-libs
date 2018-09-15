package exp.libs.utils.test;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.utils.other.RandomUtils;

public class TestRandomUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRandomInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomIntIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomGaussian() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomSpellName() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomKanjiName() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomChineseName() {
		for(int i = 0; i < 100; i++) {
			String[] name = RandomUtils.genChineseName();
			System.out.println(name[0]);
			System.out.println(name[1]);
		}
	}

}
