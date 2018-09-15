package exp.libs.warp.cep.fun.impl.other;

import java.util.List;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.CEPUtils;
import exp.libs.warp.cep.fun.BaseFunctionN;

/**
 * <pre>
 * 自定函数：
 * 	宏定义函数:给定多个无直接关系的函数，连续执行.期间只要有一个执行失败则中断.
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Macro extends BaseFunctionN {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = -2686858276208958413L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "macro";
	
	/**
	 * 给定多个无直接关系的函数，连续执行.期间只要有一个执行失败则中断.
	 * 不定个数入参：
	 * @param1 String:完整的函数式/表达式
	 * @param2 String:完整的函数式/表达式
	 * @param... String:完整的函数式/表达式
	 * @paramN String:完整的函数式/表达式
	 * @return Boolean: true:全部执行成功; false:存在执行失败
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(List<Object> params) throws EvaluationException {
		Boolean allExec = true;
		for(int i = 0; i < curNumberOfParameters; i++) {
			try {
				String funExp = asString(i + 1, params.get(i));
				CEPUtils.call(funExp);
				
			} catch (Exception e) {
				allExec = false;
				break;
			}
		}
		return allExec;
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
