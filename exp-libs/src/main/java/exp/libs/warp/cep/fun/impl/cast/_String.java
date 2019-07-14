package exp.libs.warp.cep.fun.impl.cast;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	强制类型转换: 调用入参的toString方法得到其 String 类型
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _String extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 3205146116981255386L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "str";
	
	/**
	 * 强制类型转换: 强制把任何对象转换为String
	 * @param Object:各种类型值
	 * @return String:字符串
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		return param.toString();
	}

	/**
	 * 获取函数名称
	 * @return 函数名称
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
}
