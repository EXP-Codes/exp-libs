package exp.libs.warp.cep.fun.impl.cast.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import exp.libs.warp.cep.CEPUtils;
import exp.libs.warp.cep.fun.impl.cast._Date;

/**
 * <pre>
 * 自定义函数测试：
 * 	强制类型转换: String -> Date
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Test_Date {

	/**
	 * Date强制类型转换测试
	 * @throws Exception
	 */
	@Test
	public void testCastDate() throws Exception {
		
		//正确性测试
		Object date1 = CEPUtils.call(
				_Date.NAME, new Object[] {"2014-09-30 14:05:16"});
		Assert.assertTrue(date1 instanceof Date);
		System.out.println(date1);
		
		Object date2 = CEPUtils.call(
				"date(\"2014-09-30 14:05:16\", \"yyyy-MM-dd HH:mm:ss\")");
		Assert.assertTrue(date2 instanceof Date);
		System.out.println(date2);
		
		//错误测试
		try {
			CEPUtils.call("date(\"2014,09,30 14,05,16\")");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
		
		try {
			CEPUtils.call("date()");
			Assert.assertTrue(false);
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			Assert.assertTrue(true);
		}
	}
}
