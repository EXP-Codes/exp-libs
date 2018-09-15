package exp.libs.warp.cep.fun.impl.cast.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import exp.libs.warp.cep.CEPUtils;
import exp.libs.warp.cep.fun.impl.cast._NullStr;

/**
 * <pre>
 * 自定义函数测试：
 * 	强制类型转换: 强制把任何值转换为空串
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Test_NullStr {

	/**
	 * 空串强制类型转换测试
	 * @throws Exception
	 */
	@Test
	public void testCastNullStr() throws Exception {
		
		//正确性测试
		Object rst = CEPUtils.call(
				_NullStr.NAME, new Object[] {"-123456789"});
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"987654321012345678\")");
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"abc\")");
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"\")");
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"null\")");
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"abc123!@#\")");
		Assert.assertEquals("", rst);
		
		rst = CEPUtils.call("nullstr(\"    \")");
		Assert.assertEquals("", rst);

		Date now = new Date();
		CEPUtils.declare("now", now);
		rst = CEPUtils.call("nullstr($now$)");
		Assert.assertEquals("", rst);
		
		//错误测试
		try {
			CEPUtils.call("nullstr()");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
		try {
			CEPUtils.call("nullstr(\" \r\n \t \0   \")");	//参数不能有换行
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
	}
}
