package exp.libs.warp.cep.fun.impl.cast.test;

import junit.framework.Assert;

import org.junit.Test;

import exp.libs.warp.cep.CEPUtils;
import exp.libs.warp.cep.fun.impl.cast._Long;

/**
 * <pre>
 * 自定义函数测试：
 * 	强制类型转换: String -> long
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Test_Long {

	/**
	 * long强制类型转换测试
	 * @throws Exception
	 */
	@Test
	public void testCastLong() throws Exception {
		
		//正确性测试
		Object lnum1 = CEPUtils.call(
				_Long.NAME, new Object[] {"-123456789"});
		Assert.assertTrue(lnum1 instanceof Long);
		System.out.println(lnum1);
		
		Object lnum2 = CEPUtils.call("long(\"987654321012345678\")");
		Assert.assertTrue(lnum2 instanceof Long);
		System.out.println(lnum2);
		
		//错误测试
		try {
			CEPUtils.call("long(\"9876543210123456789\")");	//数值溢出
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
		try {
			CEPUtils.call("long(\"1L\")");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
		try {
			CEPUtils.call("long()");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
	}
}
