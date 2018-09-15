package exp.libs.warp.cep.fun.impl.str;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	字符串操作：反转
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Reverse extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = -2796230393987172389L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "reverse";

	/**
	 * 字符串反转.
	 * 共1个入参：
	 * @param1 String:原字符串
	 * @return String
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		String str = asString(1, param);
		return (new StringBuilder(str)).reverse().toString();
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
