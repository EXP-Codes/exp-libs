package exp.libs.warp.cep.fun.impl.cast.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import exp.libs.warp.cep.CEPUtils;
import exp.libs.warp.cep.fun.impl.cast._Null;

/**
 * <pre>
 * 自定义函数测试：
 * 	强制类型转换: 强制把任何值转换为null
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Test_Null {

	/**
	 * null强制类型转换测试
	 * @throws Exception
	 */
	@Test
	public void testCastNull() throws Exception {
		
		//正确性测试
		Object rst = CEPUtils.call(
				_Null.NAME, new Object[] {"-123456789"});
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"987654321012345678\")");
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"abc\")");
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"\")");
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"null\")");
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"abc123!@#\")");
		Assert.assertNull(rst);
		
		rst = CEPUtils.call("null(\"    \")");
		Assert.assertNull(rst);

		Date now = new Date();
		CEPUtils.declare("now", now);
		rst = CEPUtils.call("null($now$)");
		Assert.assertNull(rst);
		
		//错误测试
		try {
			CEPUtils.call("null()");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
		try {
			CEPUtils.call("null(\" \r\n \t \0   \")");	//参数不能有换行
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
	}
}
