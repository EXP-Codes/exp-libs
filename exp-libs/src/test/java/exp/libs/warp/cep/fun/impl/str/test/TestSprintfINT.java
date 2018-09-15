package exp.libs.warp.cep.fun.impl.str.test;

import org.junit.Assert;
import org.junit.Test;

import exp.libs.warp.cep.CEPUtils;

/**
 * <pre>
 * 自定义函数测试：
 * 	获取C语言 sprintf 风格的格式字符串（仅针对int入参）
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestSprintfINT {

	@Test
	public void testEval() throws Exception {
		Object re = null;
		re = CEPUtils.call("sprintf(\"%2d\", 2)");
		Assert.assertEquals(" 2", re.toString());
		System.out.println(re);
		
		// 不允许非整型入参
		try {
			re = CEPUtils.call("sprintf(\"%2f\", 0.22)");
		} catch(Exception e) {
			Assert.assertFalse(false);
		}
	}

}
