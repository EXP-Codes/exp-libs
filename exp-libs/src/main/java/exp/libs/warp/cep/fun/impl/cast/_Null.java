package exp.libs.warp.cep.fun.impl.cast;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	强制类型转换: 强制把任何值转换为null
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _Null extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = -4867551570914177849L;
	
	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "null";
	
	/**
	 * 强制类型转换: 强制把任何值转换为null
	 * 仅1个入参：
	 * @param1 Object:各种类型值
	 * @return Object:null
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		return null;
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
