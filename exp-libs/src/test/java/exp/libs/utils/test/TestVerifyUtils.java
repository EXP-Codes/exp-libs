package exp.libs.utils.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.utils.verify.VerifyUtils;

public class TestVerifyUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsAscii() {
		Assert.assertTrue(VerifyUtils.isASCII('0'));
		Assert.assertTrue(VerifyUtils.isASCII('a'));
		Assert.assertTrue(VerifyUtils.isASCII('A'));
		Assert.assertTrue(VerifyUtils.isASCII('+'));
		Assert.assertTrue(VerifyUtils.isASCII('\n'));
		Assert.assertFalse(VerifyUtils.isASCII('天'));
	}

	@Test
	public void testIsAsciiCtrl() {
		Assert.assertTrue(VerifyUtils.isASCIICtrl('\0'));
		Assert.assertTrue(VerifyUtils.isASCIICtrl('\t'));
		Assert.assertFalse(VerifyUtils.isASCIICtrl('A'));
		Assert.assertFalse(VerifyUtils.isASCIICtrl('+'));
		Assert.assertTrue(VerifyUtils.isASCIICtrl('\n'));
		Assert.assertFalse(VerifyUtils.isASCIICtrl('天'));
	}

	@Test
	public void testIsRealNumber() {
		Assert.assertFalse(VerifyUtils.isRealNumber("01234567"));
		Assert.assertTrue(VerifyUtils.isRealNumber("0"));
		Assert.assertTrue(VerifyUtils.isRealNumber("+0"));
		Assert.assertTrue(VerifyUtils.isRealNumber("-0"));
		Assert.assertTrue(VerifyUtils.isRealNumber("123.09"));
		Assert.assertTrue(VerifyUtils.isRealNumber("+123.09"));
		Assert.assertTrue(VerifyUtils.isRealNumber("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isRealNumber("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isRealNumber("-"));
		Assert.assertFalse(VerifyUtils.isRealNumber("+"));
		Assert.assertFalse(VerifyUtils.isRealNumber("ab123"));
		Assert.assertFalse(VerifyUtils.isRealNumber("xzy"));
	}

	@Test
	public void testIsPositiveReal() {
		Assert.assertFalse(VerifyUtils.isPositiveReal("01234567"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("0"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("+0"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("-0"));
		Assert.assertTrue(VerifyUtils.isPositiveReal("123.09"));
		Assert.assertTrue(VerifyUtils.isPositiveReal("+123.09"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("-"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("+"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("ab123"));
		Assert.assertFalse(VerifyUtils.isPositiveReal("xzy"));
	}

	@Test
	public void testIsNegativeReal() {
		Assert.assertFalse(VerifyUtils.isNegativeReal("01234567"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("-04088"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("0"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("+0"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("-0"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("123.09"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("+123.09"));
		Assert.assertTrue(VerifyUtils.isNegativeReal("-4088"));
		Assert.assertTrue(VerifyUtils.isNegativeReal("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("-"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("+"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("ab123"));
		Assert.assertFalse(VerifyUtils.isNegativeReal("xzy"));
	}

	@Test
	public void testIsNotNegativeReal() {
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("01234567"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-04088"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("+04088"));
		Assert.assertTrue(VerifyUtils.isNotNegativeReal("0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeReal("+0"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeReal("123.09"));
		Assert.assertTrue(VerifyUtils.isNotNegativeReal("+123.09"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-4088"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("-"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("+"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("ab123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeReal("xzy"));
	}

	@Test
	public void testIsIntegerNumber() {
		Assert.assertFalse(VerifyUtils.isIntegerNumber("01234567"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("-04088"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("+04088"));
		Assert.assertTrue(VerifyUtils.isIntegerNumber("0"));
		Assert.assertTrue(VerifyUtils.isIntegerNumber("+0"));
		Assert.assertTrue(VerifyUtils.isIntegerNumber("-0"));
		Assert.assertTrue(VerifyUtils.isIntegerNumber("123"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("123.09"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("+123.09"));
		Assert.assertTrue(VerifyUtils.isIntegerNumber("-4088"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("-"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("+"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("ab123"));
		Assert.assertFalse(VerifyUtils.isIntegerNumber("xzy"));
	}

	@Test
	public void testIsPositiveInteger() {
		Assert.assertFalse(VerifyUtils.isPositiveInteger("01234567"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-04088"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("+04088"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("0"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("+0"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-0"));
		Assert.assertTrue(VerifyUtils.isPositiveInteger("123"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-123"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("123.09"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("+123.09"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-4088"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("-"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("+"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("ab123"));
		Assert.assertFalse(VerifyUtils.isPositiveInteger("xzy"));
	}

	@Test
	public void testIsNegativeInteger() {
		Assert.assertFalse(VerifyUtils.isNegativeInteger("01234567"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("-04088"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("+04088"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("0"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("+0"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("-0"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("123"));
		Assert.assertTrue(VerifyUtils.isNegativeInteger("-123"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("123.09"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("+123.09"));
		Assert.assertTrue(VerifyUtils.isNegativeInteger("-4088"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("-"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("+"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("ab123"));
		Assert.assertFalse(VerifyUtils.isNegativeInteger("xzy"));
	}

	@Test
	public void testIsNotNegativeInteger() {
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("01234567"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-04088"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("+04088"));
		Assert.assertTrue(VerifyUtils.isNotNegativeInteger("0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeInteger("+0"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeInteger("123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("123.09"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("+123.09"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("-"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("+"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("ab123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeInteger("xzy"));
	}

	@Test
	public void testIsFloatNumber() {
		Assert.assertFalse(VerifyUtils.isFloatNumber("01234567"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("-04088"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("+04088"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("0"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("+0"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("-0"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("123"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("-123"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("123.09"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("+123.09"));
		Assert.assertTrue(VerifyUtils.isFloatNumber("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("-"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("+"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("ab123"));
		Assert.assertFalse(VerifyUtils.isFloatNumber("xzy"));
	}

	@Test
	public void testIsPositiveFloat() {
		Assert.assertFalse(VerifyUtils.isPositiveFloat("01234567"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-04088"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("+04088"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("0"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("+0"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-0"));
		Assert.assertTrue(VerifyUtils.isPositiveFloat("123"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-123"));
		Assert.assertTrue(VerifyUtils.isPositiveFloat("123.09"));
		Assert.assertTrue(VerifyUtils.isPositiveFloat("+123.09"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("-"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("+"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("ab123"));
		Assert.assertFalse(VerifyUtils.isPositiveFloat("xzy"));
	}

	@Test
	public void testIsNegativeFloat() {
		Assert.assertFalse(VerifyUtils.isNegativeFloat("01234567"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("-04088"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("+04088"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("0"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("+0"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("-0"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("123"));
		Assert.assertTrue(VerifyUtils.isNegativeFloat("-123"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("123.09"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("+123.09"));
		Assert.assertTrue(VerifyUtils.isNegativeFloat("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("-"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("+"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("ab123"));
		Assert.assertFalse(VerifyUtils.isNegativeFloat("xzy"));
	}

	@Test
	public void testIsNotNegativeFloat() {
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("01234567"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-04088"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("+04088"));
		Assert.assertTrue(VerifyUtils.isNotNegativeFloat("0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeFloat("+0"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-0"));
		Assert.assertTrue(VerifyUtils.isNotNegativeFloat("123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-123"));
		Assert.assertTrue(VerifyUtils.isNotNegativeFloat("123.09"));
		Assert.assertTrue(VerifyUtils.isNotNegativeFloat("+123.09"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-401.0026723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-401.0026.723424311"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("-"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("+"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("ab123"));
		Assert.assertFalse(VerifyUtils.isNotNegativeFloat("xzy"));
	}

	@Test
	public void testIsDigits() {
		Assert.assertTrue(VerifyUtils.isDigits("01234567"));
		Assert.assertTrue(VerifyUtils.isDigits("0"));
		Assert.assertFalse(VerifyUtils.isDigits("+0"));
		Assert.assertFalse(VerifyUtils.isDigits("-0"));
		Assert.assertFalse(VerifyUtils.isDigits("+123"));
		Assert.assertFalse(VerifyUtils.isDigits("-401"));
		Assert.assertFalse(VerifyUtils.isDigits("-"));
		Assert.assertFalse(VerifyUtils.isDigits("+"));
		Assert.assertFalse(VerifyUtils.isDigits("ab123"));
		Assert.assertFalse(VerifyUtils.isDigits("xzy"));
	}
	
	@Test
	public void testIsLetterString() {
		Assert.assertFalse(VerifyUtils.isLetter("0adg"));
		Assert.assertFalse(VerifyUtils.isLetter("a%$%%"));
		Assert.assertTrue(VerifyUtils.isLetter("Azxw"));
		Assert.assertTrue(VerifyUtils.isLetter("g"));
		Assert.assertFalse(VerifyUtils.isLetter("-+"));
		Assert.assertFalse(VerifyUtils.isLetter("\n"));
		Assert.assertFalse(VerifyUtils.isLetter("天"));
		Assert.assertFalse(VerifyUtils.isLetter(""));
	}
	
	@Test
	public void testIsLetterChar() {
		Assert.assertFalse(VerifyUtils.isLetter('0'));
		Assert.assertTrue(VerifyUtils.isLetter('a'));
		Assert.assertTrue(VerifyUtils.isLetter('A'));
		Assert.assertFalse(VerifyUtils.isLetter('+'));
		Assert.assertFalse(VerifyUtils.isLetter('\n'));
		Assert.assertFalse(VerifyUtils.isLetter('天'));
	}

	@Test
	public void testIsUpperLetterString() {
		Assert.assertFalse(VerifyUtils.isUpperLetter("0adg"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("a%$%%"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("Azxw"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("azxw"));
		Assert.assertTrue(VerifyUtils.isUpperLetter("AZXW"));
		Assert.assertTrue(VerifyUtils.isUpperLetter("B"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("-+"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("\n"));
		Assert.assertFalse(VerifyUtils.isUpperLetter("天"));
		Assert.assertFalse(VerifyUtils.isUpperLetter(""));
	}
	
	@Test
	public void testIsUpperLetterChar() {
		Assert.assertFalse(VerifyUtils.isUpperLetter('0'));
		Assert.assertFalse(VerifyUtils.isUpperLetter('a'));
		Assert.assertTrue(VerifyUtils.isUpperLetter('A'));
		Assert.assertFalse(VerifyUtils.isUpperLetter('+'));
		Assert.assertFalse(VerifyUtils.isUpperLetter('\n'));
		Assert.assertFalse(VerifyUtils.isUpperLetter('天'));
	}

	@Test
	public void testIsLowerLetterString() {
		Assert.assertFalse(VerifyUtils.isLowerLetter("0adg"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("a%$%%"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("Azxw"));
		Assert.assertTrue(VerifyUtils.isLowerLetter("azxw"));
		Assert.assertTrue(VerifyUtils.isLowerLetter("b"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("AZXW"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("-+"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("\n"));
		Assert.assertFalse(VerifyUtils.isLowerLetter("天"));
		Assert.assertFalse(VerifyUtils.isLowerLetter(""));
	}
	
	@Test
	public void testIsLowerLetterChar() {
		Assert.assertFalse(VerifyUtils.isLowerLetter('0'));
		Assert.assertTrue(VerifyUtils.isLowerLetter('a'));
		Assert.assertFalse(VerifyUtils.isLowerLetter('A'));
		Assert.assertFalse(VerifyUtils.isLowerLetter('+'));
		Assert.assertFalse(VerifyUtils.isLowerLetter('\n'));
		Assert.assertFalse(VerifyUtils.isLowerLetter('天'));
	}

	@Test
	public void testIsDigitsOrLetterString() {
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("0adg"));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter("a%$%%"));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("Azxw"));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("azxw"));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("b"));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("1"));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter("AZXW"));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter("-+"));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter("\n"));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter("天"));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter(""));
	}

	@Test
	public void testIsDigitsOrLetterChar() {
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter('0'));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter('a'));
		Assert.assertTrue(VerifyUtils.isDigitsOrLetter('A'));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter('+'));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter('\n'));
		Assert.assertFalse(VerifyUtils.isDigitsOrLetter('天'));
	}

	@Test
	public void testIsUsername() {
		Assert.assertFalse(VerifyUtils.isUsername("12345"));
		Assert.assertFalse(VerifyUtils.isUsername("123456"));
		Assert.assertFalse(VerifyUtils.isUsername("_123456"));
		Assert.assertTrue(VerifyUtils.isUsername("sky123456"));
		Assert.assertFalse(VerifyUtils.isUsername("sky231%$#"));
		Assert.assertFalse(VerifyUtils.isUsername("123ddhs_dud"));
		Assert.assertFalse(VerifyUtils.isUsername("12345_"));
		Assert.assertFalse(VerifyUtils.isUsername("_12345"));
	}

	@Test
	public void testIsPassword() {
		Assert.assertFalse(VerifyUtils.isPassword("12345"));
		Assert.assertTrue(VerifyUtils.isPassword("123456"));
		Assert.assertTrue(VerifyUtils.isPassword("_123456"));
		Assert.assertTrue(VerifyUtils.isPassword("sky123456"));
		Assert.assertTrue(VerifyUtils.isPassword("sky231%$#/!@~^&*(){}"));
		Assert.assertTrue(VerifyUtils.isPassword("123ddhs_dud"));
		Assert.assertTrue(VerifyUtils.isPassword("12345_"));
		Assert.assertTrue(VerifyUtils.isPassword("_12345"));
	}

	@Test
	public void testIsFullwidthChar() {
		Assert.assertTrue(VerifyUtils.isFullwidth("１２３４５"));
		Assert.assertTrue(VerifyUtils.isFullwidth("ａｂｃｄｅｆｇ"));
		Assert.assertFalse(VerifyUtils.isFullwidth("12345"));
		Assert.assertFalse(VerifyUtils.isFullwidth("abcdefg"));
	}

	@Test
	public void testIsEmail() {
		Assert.assertTrue(VerifyUtils.isEmail("abc@126.com"));
		Assert.assertTrue(VerifyUtils.isEmail("123@hotmail.com"));
		Assert.assertTrue(VerifyUtils.isEmail("_abc.123@qq.com"));
		Assert.assertFalse(VerifyUtils.isEmail("_abc.123yahoo.com"));
		Assert.assertFalse(VerifyUtils.isEmail("_abc@123@yahoo@com"));
		Assert.assertFalse(VerifyUtils.isEmail("_abc#123@yahoo#org"));
	}

	@Test
	public void testIsHttp() {
		Assert.assertTrue(VerifyUtils.isHttp("https://www.4008-517-517.cn/cn/?utm_source=baidu&utm_medium=cpc&utm_term=kfc%2525e7%2525bd%252591%2525e4%2525b8%25258a%2525e8%2525ae%2525a2%2525e9%2525a4%252590&utm_campaign=%2525e7%2525ab%25259e%2525e5%252593%252581%2525e8%2525af%25258d-%2525e5%2525b9%2525bf%2525e4%2525b8%25259c"));
		Assert.assertTrue(VerifyUtils.isHttp("http://www.xp510.com/"));
		Assert.assertTrue(VerifyUtils.isHttp("http://115.com/?goto=http%3A%2F%2F115.com%2F%3Fmode%3Dwangpan"));
		Assert.assertFalse(VerifyUtils.isHttp("www.youbiyao.net/?y"));
		Assert.assertFalse(VerifyUtils.isHttp("127.0.0.1:8081/nexus"));
		Assert.assertFalse(VerifyUtils.isHttp("about:blank"));
	}

	@Test
	public void testIsTelephone() {
		Assert.assertTrue(VerifyUtils.isTelephone("020-12345678"));
		Assert.assertTrue(VerifyUtils.isTelephone("020-1234567"));
		Assert.assertTrue(VerifyUtils.isTelephone("020-123456"));
		Assert.assertTrue(VerifyUtils.isTelephone("0755-12345678"));
		Assert.assertTrue(VerifyUtils.isTelephone("0755-1234567"));
		Assert.assertTrue(VerifyUtils.isTelephone("0755-123456"));
		Assert.assertFalse(VerifyUtils.isTelephone("020-123"));
		Assert.assertTrue(VerifyUtils.isTelephone("10000"));
		Assert.assertTrue(VerifyUtils.isTelephone("95508"));
		Assert.assertTrue(VerifyUtils.isTelephone("88888888"));
		Assert.assertFalse(VerifyUtils.isTelephone("13912345678"));
		Assert.assertFalse(VerifyUtils.isTelephone("123"));
	}

	@Test
	public void testIsMobilePhone() {
		Assert.assertFalse(VerifyUtils.isMobilePhone("020-12345678"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("020-1234567"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("020-123456"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("0755-12345678"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("0755-1234567"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("0755-123456"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("020-123"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("10000"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("95508"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("88888888"));
		Assert.assertTrue(VerifyUtils.isMobilePhone("13912345678"));
		Assert.assertFalse(VerifyUtils.isMobilePhone("123"));
	}

	@Test
	public void testIsIdentity() {
		Assert.assertFalse(VerifyUtils.isIdentity("13912345678"));
		Assert.assertFalse(VerifyUtils.isIdentity("020-12345678"));
		Assert.assertFalse(VerifyUtils.isIdentity("123"));
		Assert.assertTrue(VerifyUtils.isIdentity("440684199001010001"));
		Assert.assertTrue(VerifyUtils.isIdentity("44068419900101000X"));
		Assert.assertTrue(VerifyUtils.isIdentity("44068419900101000x"));
		Assert.assertTrue(VerifyUtils.isIdentity("440684199001012"));
		Assert.assertFalse(VerifyUtils.isIdentity("44068419900101X"));
		Assert.assertFalse(VerifyUtils.isIdentity("44068419900101x"));
	}

	@Test
	public void testIsYear() {
		Assert.assertFalse(VerifyUtils.isYear("0000"));
		Assert.assertFalse(VerifyUtils.isYear("02015"));
		Assert.assertTrue(VerifyUtils.isYear("0"));
		Assert.assertTrue(VerifyUtils.isYear("2016"));
		Assert.assertTrue(VerifyUtils.isYear("1234"));
		Assert.assertTrue(VerifyUtils.isYear("456"));
		Assert.assertTrue(VerifyUtils.isYear("98"));
		Assert.assertTrue(VerifyUtils.isYear("5"));
		Assert.assertTrue(VerifyUtils.isYear("9999"));
		Assert.assertTrue(VerifyUtils.isYear("10001"));
	}

	@Test
	public void testIsMonth() {
		Assert.assertFalse(VerifyUtils.isMonth("00"));
		Assert.assertFalse(VerifyUtils.isMonth("02015"));
		Assert.assertTrue(VerifyUtils.isMonth("01"));
		Assert.assertTrue(VerifyUtils.isMonth("1"));
		Assert.assertTrue(VerifyUtils.isMonth("02"));
		Assert.assertTrue(VerifyUtils.isMonth("2"));
		Assert.assertTrue(VerifyUtils.isMonth("10"));
		Assert.assertTrue(VerifyUtils.isMonth("11"));
		Assert.assertTrue(VerifyUtils.isMonth("12"));
		Assert.assertFalse(VerifyUtils.isMonth("012"));
		Assert.assertFalse(VerifyUtils.isMonth("13"));
	}

	@Test
	public void testIsDay() {
		Assert.assertFalse(VerifyUtils.isDay("00"));
		Assert.assertFalse(VerifyUtils.isDay("02015"));
		Assert.assertTrue(VerifyUtils.isDay("01"));
		Assert.assertTrue(VerifyUtils.isDay("1"));
		Assert.assertTrue(VerifyUtils.isDay("02"));
		Assert.assertTrue(VerifyUtils.isDay("2"));
		Assert.assertTrue(VerifyUtils.isDay("10"));
		Assert.assertTrue(VerifyUtils.isDay("21"));
		Assert.assertTrue(VerifyUtils.isDay("29"));
		Assert.assertTrue(VerifyUtils.isDay("30"));
		Assert.assertTrue(VerifyUtils.isDay("31"));
		Assert.assertFalse(VerifyUtils.isDay("32"));
		Assert.assertFalse(VerifyUtils.isDay("031"));
	}

	@Test
	public void testIsHour() {
		Assert.assertTrue(VerifyUtils.isHour("00"));
		Assert.assertFalse(VerifyUtils.isHour("02015"));
		Assert.assertTrue(VerifyUtils.isHour("01"));
		Assert.assertTrue(VerifyUtils.isHour("1"));
		Assert.assertTrue(VerifyUtils.isHour("02"));
		Assert.assertTrue(VerifyUtils.isHour("2"));
		Assert.assertTrue(VerifyUtils.isHour("10"));
		Assert.assertTrue(VerifyUtils.isHour("21"));
		Assert.assertFalse(VerifyUtils.isHour("29"));
		Assert.assertTrue(VerifyUtils.isHour("23"));
		Assert.assertFalse(VerifyUtils.isHour("24"));
		Assert.assertFalse(VerifyUtils.isHour("32"));
		Assert.assertFalse(VerifyUtils.isHour("024"));
	}

	@Test
	public void testIsMinute() {
		Assert.assertTrue(VerifyUtils.isMinute("00"));
		Assert.assertFalse(VerifyUtils.isMinute("02015"));
		Assert.assertTrue(VerifyUtils.isMinute("01"));
		Assert.assertTrue(VerifyUtils.isMinute("1"));
		Assert.assertTrue(VerifyUtils.isMinute("02"));
		Assert.assertTrue(VerifyUtils.isMinute("2"));
		Assert.assertTrue(VerifyUtils.isMinute("0"));
		Assert.assertTrue(VerifyUtils.isMinute("21"));
		Assert.assertFalse(VerifyUtils.isMinute("029"));
		Assert.assertTrue(VerifyUtils.isMinute("59"));
		Assert.assertFalse(VerifyUtils.isMinute("60"));
		Assert.assertFalse(VerifyUtils.isMinute("61"));
		Assert.assertFalse(VerifyUtils.isMinute("059"));
	}

	@Test
	public void testIsSecond() {
		Assert.assertTrue(VerifyUtils.isSecond("00"));
		Assert.assertFalse(VerifyUtils.isSecond("02015"));
		Assert.assertTrue(VerifyUtils.isSecond("01"));
		Assert.assertTrue(VerifyUtils.isSecond("1"));
		Assert.assertTrue(VerifyUtils.isSecond("02"));
		Assert.assertTrue(VerifyUtils.isSecond("2"));
		Assert.assertTrue(VerifyUtils.isSecond("0"));
		Assert.assertTrue(VerifyUtils.isSecond("21"));
		Assert.assertFalse(VerifyUtils.isSecond("029"));
		Assert.assertTrue(VerifyUtils.isSecond("59"));
		Assert.assertFalse(VerifyUtils.isSecond("60"));
		Assert.assertFalse(VerifyUtils.isSecond("61"));
		Assert.assertFalse(VerifyUtils.isSecond("059"));
	}

	@Test
	public void testIsMillis() {
		Assert.assertTrue(VerifyUtils.isMillis("0"));
		Assert.assertTrue(VerifyUtils.isMillis("00"));
		Assert.assertTrue(VerifyUtils.isMillis("000"));
		Assert.assertTrue(VerifyUtils.isMillis("010"));
		Assert.assertTrue(VerifyUtils.isMillis("011"));
		Assert.assertTrue(VerifyUtils.isMillis("1"));
		Assert.assertTrue(VerifyUtils.isMillis("12"));
		Assert.assertTrue(VerifyUtils.isMillis("999"));
		Assert.assertFalse(VerifyUtils.isMillis("9999"));
	}

	@Test
	public void testIsDate() {
		Assert.assertTrue(VerifyUtils.isDate("2016-01-01"));
		Assert.assertFalse(VerifyUtils.isDate("02016-01-01"));
		Assert.assertTrue(VerifyUtils.isDate("2016-1-1"));
		Assert.assertFalse(VerifyUtils.isDate("2016-13-01"));
		Assert.assertFalse(VerifyUtils.isDate("2016-01-32"));
	}

	@Test
	public void testIsTime() {
		Assert.assertTrue(VerifyUtils.isTime("00:00:00"));
		Assert.assertTrue(VerifyUtils.isTime("23:59:59"));
		Assert.assertFalse(VerifyUtils.isTime("24:00:00"));
		Assert.assertTrue(VerifyUtils.isTime("0:0:0"));
		Assert.assertFalse(VerifyUtils.isTime("0:60:60"));
		Assert.assertFalse(VerifyUtils.isTime("16-01-32"));
		Assert.assertTrue(VerifyUtils.isTime("0:0:0.0"));
		Assert.assertTrue(VerifyUtils.isTime("23:59:59.999"));
		Assert.assertTrue(VerifyUtils.isTime("23:1:01.012"));
		Assert.assertTrue(VerifyUtils.isTime("6:01:1.9"));
	}

	@Test
	public void testIsDateTime() {
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 00:00:00"));
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 23:59:59"));
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 23:59:59.123"));
		Assert.assertFalse(VerifyUtils.isDateTime("2016-01-01 24:00:00"));
		Assert.assertFalse(VerifyUtils.isDateTime("02016-01-01 0:0:0"));
		Assert.assertFalse(VerifyUtils.isDateTime("2016-01-01 0:60:60"));
		Assert.assertFalse(VerifyUtils.isDateTime("2016-13-01 16:01:32"));
		Assert.assertFalse(VerifyUtils.isDateTime("2016-01-01-0:0:0.0"));
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 23:59:59.999"));
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 23:1:01.012"));
		Assert.assertTrue(VerifyUtils.isDateTime("2016-01-01 6:01:1.9"));
	}

	@Test
	public void testIsMac() {
		Assert.assertTrue(VerifyUtils.isMac("68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isMac("00-01-00-01-1D-30-A6-F8-68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isMac("127.0.0.1"));
		Assert.assertFalse(VerifyUtils.isMac("fe80::5168:bd2c:886e:5550%13"));
		Assert.assertFalse(VerifyUtils.isMac("00-00-00-00-00-00-00-E0"));
	}

	@Test
	public void testIsIp() {
		Assert.assertFalse(VerifyUtils.isIP("68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isIP("00-01-00-01-1D-30-A6-F8-68-F7-28-F5-2C-D3"));
		Assert.assertTrue(VerifyUtils.isIP("127.0.0.1"));
		Assert.assertTrue(VerifyUtils.isIP("0.0.0.0"));
		Assert.assertTrue(VerifyUtils.isIP("255.255.255.255"));
		Assert.assertFalse(VerifyUtils.isIP("256.0.0.0"));
		Assert.assertFalse(VerifyUtils.isIP("127.0.0.1:9998"));
		Assert.assertFalse(VerifyUtils.isIP("fe80::5168:bd2c:886e:5550%13"));
		Assert.assertFalse(VerifyUtils.isIP("00-00-00-00-00-00-00-E0"));
	}

	@Test
	public void testIsIpv4() {
		Assert.assertFalse(VerifyUtils.isIPv4("68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isIPv4("00-01-00-01-1D-30-A6-F8-68-F7-28-F5-2C-D3"));
		Assert.assertTrue(VerifyUtils.isIPv4("127.0.0.1"));
		Assert.assertTrue(VerifyUtils.isIPv4("0.0.0.0"));
		Assert.assertTrue(VerifyUtils.isIPv4("255.255.255.255"));
		Assert.assertFalse(VerifyUtils.isIPv4("256.0.0.0"));
		Assert.assertFalse(VerifyUtils.isIPv4("127.0.0.1:9998"));
		Assert.assertFalse(VerifyUtils.isIPv4("fe80::5168:bd2c:886e:5550%13"));
		Assert.assertFalse(VerifyUtils.isIPv4("00-00-00-00-00-00-00-E0"));
	}

	@Test
	public void testIsIpv6() {
		Assert.assertFalse(VerifyUtils.isIPv6("68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isIPv6("00-01-00-01-1D-30-A6-F8-68-F7-28-F5-2C-D3"));
		Assert.assertFalse(VerifyUtils.isIPv6("127.0.0.1"));
		Assert.assertFalse(VerifyUtils.isIPv6("0.0.0.0"));
		Assert.assertFalse(VerifyUtils.isIPv6("255.255.255.255"));
		Assert.assertFalse(VerifyUtils.isIPv6("256.0.0.0"));
		Assert.assertFalse(VerifyUtils.isIPv6("127.0.0.1:9998"));
		Assert.assertFalse(VerifyUtils.isIPv6("00-00-00-00-00-00-00-E0"));
		Assert.assertTrue(VerifyUtils.isIPv6("fe80::5168:bd2c:886e:5550%13"));
		Assert.assertTrue(VerifyUtils.isIPv6("fe80:0000:0000:0000:0204:61ff:fe9d:f156"));	// 完整地址
		Assert.assertTrue(VerifyUtils.isIPv6("fe80:0:0:0:204:61ff:fe9d:f156"));	// 压缩全0字段 
		Assert.assertTrue(VerifyUtils.isIPv6("fe80::204:61ff:fe9d:f156"));	// 去掉全0字段 
		Assert.assertTrue(VerifyUtils.isIPv6("fe80:0000:0000:0000:0204:61ff:254.157.241.86"));	// 后2位为ipv4
		Assert.assertTrue(VerifyUtils.isIPv6("fe80:0:0:0:204:61ff:254.157.241.86"));	// 压缩全0字段，后2位为ipv4
		Assert.assertTrue(VerifyUtils.isIPv6("fe80::204:61ff:254.157.241.86"));	// 去掉全0字段，后2位为ipv4
	}

	@Test
	public void testIsPortString() {
		Assert.assertFalse(VerifyUtils.isPort("99999"));
		Assert.assertFalse(VerifyUtils.isPort("00000"));
		Assert.assertTrue(VerifyUtils.isPort("0"));
		Assert.assertTrue(VerifyUtils.isPort("1"));
		Assert.assertTrue(VerifyUtils.isPort("12"));
		Assert.assertTrue(VerifyUtils.isPort("123"));
		Assert.assertTrue(VerifyUtils.isPort("1024"));
		Assert.assertTrue(VerifyUtils.isPort("55555"));
		Assert.assertFalse(VerifyUtils.isPort("01024"));
		Assert.assertTrue(VerifyUtils.isPort("65535"));
		Assert.assertFalse(VerifyUtils.isPort("65536"));
	}

	@Test
	public void testIsPortInt() {
		Assert.assertFalse(VerifyUtils.isPort(99999));
		Assert.assertTrue(VerifyUtils.isPort(00000));
		Assert.assertTrue(VerifyUtils.isPort(0));
		Assert.assertTrue(VerifyUtils.isPort(1));
		Assert.assertTrue(VerifyUtils.isPort(12));
		Assert.assertTrue(VerifyUtils.isPort(123));
		Assert.assertTrue(VerifyUtils.isPort(1024));
		Assert.assertTrue(VerifyUtils.isPort(55555));
		Assert.assertTrue(VerifyUtils.isPort(01024));
		Assert.assertTrue(VerifyUtils.isPort(65535));
		Assert.assertFalse(VerifyUtils.isPort(65536));
	}

	@Test
	public void testIsSocket() {
		Assert.assertFalse(VerifyUtils.isSocket("0.0.0.0:99999"));
		Assert.assertFalse(VerifyUtils.isSocket("0.0.0.0:00000"));
		Assert.assertTrue(VerifyUtils.isSocket("0.0.0.0:0"));
		Assert.assertTrue(VerifyUtils.isSocket("1.1.1.1:1"));
		Assert.assertFalse(VerifyUtils.isSocket("255.255.255.256:12"));
		Assert.assertTrue(VerifyUtils.isSocket("127.0.0.1:123"));
		Assert.assertTrue(VerifyUtils.isSocket("127.0.0.1:1024"));
		Assert.assertTrue(VerifyUtils.isSocket("192.168.0.1:55555"));
		Assert.assertFalse(VerifyUtils.isSocket("255.255.255.255:01024"));
		Assert.assertTrue(VerifyUtils.isSocket("255.255.255.255:65535"));
		Assert.assertFalse(VerifyUtils.isSocket("255.255.255.255:65536"));
	}

	@Test
	public void testIsChineseString() {
		Assert.assertFalse(VerifyUtils.isChinese("部分中文.!,,"));
		Assert.assertFalse(VerifyUtils.isChinese("部分中文abc"));
		Assert.assertFalse(VerifyUtils.isChinese("部分中文123"));
		Assert.assertFalse(VerifyUtils.isChinese("部 分 中 文"));
		Assert.assertTrue(VerifyUtils.isChinese("全是中文。！，，"));
		Assert.assertTrue(VerifyUtils.isChinese("全是中文"));
		Assert.assertTrue(VerifyUtils.isChinese("全角不属中文ａ"));
		Assert.assertFalse(VerifyUtils.isChinese("all english"));
	}

	@Test
	public void testIsChineseChar() {
		Assert.assertTrue(VerifyUtils.isChinese('中'));
		Assert.assertFalse(VerifyUtils.isChinese('a'));
		Assert.assertFalse(VerifyUtils.isChinese('1'));
		Assert.assertTrue(VerifyUtils.isChinese('。'));
		Assert.assertFalse(VerifyUtils.isChinese('&'));
		Assert.assertTrue(VerifyUtils.isChinese('ａ'));	// 全角属中文
		Assert.assertFalse(VerifyUtils.isChinese(' '));
	}
	
	@Test
	public void testExistChinese() {
		Assert.assertTrue(VerifyUtils.existChinese("部分中文.!,,"));
		Assert.assertTrue(VerifyUtils.existChinese("部分中文abc"));
		Assert.assertTrue(VerifyUtils.existChinese("部分中文123"));
		Assert.assertTrue(VerifyUtils.existChinese("部 分 中 文"));
		Assert.assertTrue(VerifyUtils.existChinese("全是中文。！，，"));
		Assert.assertTrue(VerifyUtils.existChinese("全是中文"));
		Assert.assertTrue(VerifyUtils.existChinese("全角不属中文ａ"));
		Assert.assertFalse(VerifyUtils.existChinese("all english"));
	}

	@Test
	public void testExistMessyCode() {
		Assert.assertFalse(VerifyUtils.existMessyChinese("部分中文.!,,"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("部分中文abc"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("部分中文123"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("部 分 中 文"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("全是中文。！，，"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("全是中文"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("全角不属中文ａ"));
		Assert.assertFalse(VerifyUtils.existMessyChinese("all english"));
		
		try {
			byte[] bytes = "乱码测试ａtest123abc".getBytes("GBK");
			String messy = new String(bytes, "ISO-8859-1");
			System.out.println(messy);
			Assert.assertTrue(VerifyUtils.existMessyChinese(messy));
			
		} catch (UnsupportedEncodingException e) {
			fail("编码异常");
		}
		
		try {
			byte[] bytes = "乱码测试ａtest123abc".getBytes("ISO-8859-1");
			String messy = new String(bytes, "UTF-8");
			System.out.println(messy);
			Assert.assertFalse(VerifyUtils.existMessyChinese(messy));
			
		} catch (UnsupportedEncodingException e) {
			fail("编码异常");
		}
	}

}
