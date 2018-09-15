package exp.libs.utils.test;

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.envm.Charset;
import exp.libs.utils.encode.CryptoUtils;

public class TestCryptoUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToMD5String() {
		String md5 = CryptoUtils.toMD5("测试MD5");
		System.out.println(md5);
	}
	
	@Test
	public void testToMD5StringString() {
		String md5 = CryptoUtils.toMD5("测试MD5", Charset.UTF8);
		System.out.println(md5);
		
		md5 = CryptoUtils.toMD5("测试MD5", Charset.GBK);
		System.out.println(md5);
	}

	@Test
	public void testToMD5StringArray() {
		String md5 = CryptoUtils.toMD5(new String[] { "测试", "MD5" });
		System.out.println(md5);
	}
	
	@Test
	public void testToMD5StringArrayString() {
		String md5 = CryptoUtils.toMD5(new String[] { "测试", "MD5" }, Charset.UTF8);
		System.out.println(md5);
		
		md5 = CryptoUtils.toMD5(new String[] { "测试", "MD5" }, Charset.GBK);
		System.out.println(md5);
	}

	@Test
	public void testTo16MD5() {
		String _32MD5 = CryptoUtils.toMD5("测试MD5");
		System.out.println(_32MD5);
		
		String _16MD5 = CryptoUtils.to16MD5(_32MD5);
		System.out.println(_16MD5);
	}

	@Test
	public void testToDESString() {
		String des = CryptoUtils.toDES("测试DES-123");
		System.out.println(des);
	}

	@Test
	public void testToDESStringString() {
		String des = CryptoUtils.toDES("测试DES-123", "test-key");
		System.out.println(des);
		
		des = CryptoUtils.toDES("测试DES-123", "<8bit");
		System.out.println(des);
	}

	@Test
	public void testToDESStringStringString() {
		String des = CryptoUtils.toDES("测试DES-123", "test-key", Charset.UTF8);
		System.out.println(des);
	}

	@Test
	public void testDeDESString() {
		String des = CryptoUtils.toDES("测试DES-123");
		System.out.println(des);
		
		String undes = CryptoUtils.deDES(des);
		System.out.println(undes);
	}

	@Test
	public void testDeDESStringString() {
		String des = CryptoUtils.toDES("测试DES-123", "test-key");
		System.out.println(des);
		
		String undes = CryptoUtils.deDES(des, "test-key");
		System.out.println(undes);
		
		undes = CryptoUtils.deDES(des, "error-key");
		System.out.println(undes);
		
		des = CryptoUtils.toDES("测试DES-456", "<8bit");
		System.out.println(des);
		undes = CryptoUtils.deDES(des, "<8bit");
		System.out.println(undes);
	}

	@Test
	public void testDeDESStringStringString() {
		String des = CryptoUtils.toDES("测试DES-123", "test-key", Charset.UTF8);
		System.out.println(des);
		
		String undes = CryptoUtils.deDES(des, "test-key", Charset.UTF8);
		System.out.println(undes);
		
		undes = CryptoUtils.deDES(des, "test-key", Charset.GBK);
		System.out.println(undes);
	}
	
	@Test
	public void testRSA() {
		String data = "abc\td中文&68.?s  a";
		System.out.println("测试字符串：" + data);

		
//		String keys = CryptoUtils.getRSAKeyStrPair();
		Key[] keys = CryptoUtils.getRSAKeyPair();
		String publicKey = CryptoUtils.toRSAPublicKey((RSAPublicKey) keys[0], true);
		String privateKey = CryptoUtils.toRSAPrivateKey((RSAPrivateKey) keys[1], true);
		System.out.println("RSA公钥:\r\n" + publicKey);
		System.out.println("RSA私钥:\r\n" + privateKey);
		
		
		String toRSA = CryptoUtils.toRSAByPubKey(data, publicKey);
		System.out.println("RSA公钥加密：" + toRSA);
		String deRSA = CryptoUtils.deRSAByPriKey(toRSA, privateKey);
		System.out.println("RSA私钥解密：" + deRSA);
		
		
		toRSA = CryptoUtils.toRSAByPriKey(data, privateKey);
		System.out.println("RSA私钥加密：" + toRSA);
		deRSA = CryptoUtils.deRSAByPubKey(toRSA, publicKey);
		System.out.println("RSA公钥解密：" + deRSA);
		
		
		String sign = CryptoUtils.doRSASign(data, privateKey);
		System.out.println("RSA私钥签名串：" + sign);
		boolean isOk = CryptoUtils.checkRSASign(data, sign, publicKey);
		System.out.println("RSA公钥签名校验：" + isOk);
	}

}
