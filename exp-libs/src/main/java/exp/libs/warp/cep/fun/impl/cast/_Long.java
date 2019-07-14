package exp.libs.warp.cep.fun.impl.cast;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.CEPParseException;
import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	强制类型转换: String -> Long
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _Long extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 3376514202967586085L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "long";
	
	/**
	 * 强制类型转换: String -> Long
	 * @param param String:数字字符串
	 * @return Long:长整型
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		Long lNum = new Long(-1L);
		String sNum = asString(1, param);
		
		try {
			lNum = Long.parseLong(sNum);
		} catch (NumberFormatException e) {
			throw new CEPParseException(this, 1, sNum, Long.class, e);
		}
		return lNum;
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
