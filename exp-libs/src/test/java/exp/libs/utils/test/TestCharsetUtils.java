package exp.libs.utils.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.envm.Charset;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.num.BODHUtils;

public class TestCharsetUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsInvaild() {
		System.out.println(CharsetUtils.isVaild(Charset.UTF8));
		System.out.println(CharsetUtils.isVaild(Charset.ASCII));
		System.out.println(CharsetUtils.isVaild("unknow"));
	}

	@Test
	public void testToStr() {
		byte[] bytes = CharsetUtils.toBytes("字符集测试abc123", Charset.UTF8);
		String hex = BODHUtils.toHex(bytes);
		System.out.println(hex);
		
		String str = CharsetUtils.toStr(bytes, Charset.UTF8);
		System.out.println(str);
	}

	@Test
	public void testToBytes() {
		byte[] bytes = CharsetUtils.toBytes("字符集测试abc123", Charset.UTF8);
		String hex = BODHUtils.toHex(bytes);
		System.out.println(hex);
	}

	@Test
	public void testTranscode() {
		byte[] utf8Bytes = CharsetUtils.toBytes("字符集测试abc123", Charset.UTF8);
		String utf8Hex = BODHUtils.toHex(utf8Bytes);
		System.out.println(utf8Hex);
		
		byte[] gbkBytes = CharsetUtils.transcode(utf8Bytes, Charset.UTF8, Charset.GBK);
		String gbkHex = BODHUtils.toHex(gbkBytes);
		System.out.println(gbkHex);
		
		String str = CharsetUtils.toStr(gbkBytes, Charset.GBK);
		System.out.println(str);
	}

	@Test
	public void testToHex() {
		byte[] bytes = CharsetUtils.toBytes("字符集测试abc123", Charset.UTF8);
		String hex = BODHUtils.toHex(bytes);
		System.out.println(hex);
	}

}
