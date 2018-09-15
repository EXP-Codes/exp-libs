package exp.libs.utils.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.envm.Charset;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.num.BODHUtils;

public class TestBODHUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToHex() {
		byte[] bytes = CharsetUtils.toBytes("测试用字符串", Charset.UTF8);
		String hex = BODHUtils.toHex(bytes);
		System.out.println(hex);
	}

	@Test
	public void testToBytes() {
		byte[] bytes = BODHUtils.toBytes("E6B58BE8AF95E794A8E5AD97E7ACA6E4B8B2");
		System.out.println(bytes);
	}

	@Test
	public void testDecToHex() {
		System.out.println(BODHUtils.decToHex(1234));
		System.out.println(BODHUtils.decToHex("15"));
	}

	@Test
	public void testDecToOct() {
		System.out.println(BODHUtils.decToOct(1234));
		System.out.println(BODHUtils.decToOct("15"));
	}

	@Test
	public void testDecToBin() {
		System.out.println(BODHUtils.decToBin(1234));
		System.out.println(BODHUtils.decToBin("15"));
	}

	@Test
	public void testBinToOct() {
		System.out.println(BODHUtils.binToOct("1111"));
	}

	@Test
	public void testBinToDec() {
		System.out.println(BODHUtils.binToOct("1111"));
	}

	@Test
	public void testBinToHex() {
		System.out.println(BODHUtils.binToHex("1111"));
	}

	@Test
	public void testOctToBin() {
		System.out.println(BODHUtils.octToBin("17"));
	}

	@Test
	public void testOctToDec() {
		System.out.println(BODHUtils.octToDec("17"));
	}

	@Test
	public void testOctToHex() {
		System.out.println(BODHUtils.octToHex("17"));
	}

	@Test
	public void testHexToBin() {
		System.out.println(BODHUtils.hexToBin("F"));
	}

	@Test
	public void testHexToOct() {
		System.out.println(BODHUtils.hexToOct("F"));
	}

	@Test
	public void testHexToDec() {
		System.out.println(BODHUtils.hexToDec("F"));
	}

}
