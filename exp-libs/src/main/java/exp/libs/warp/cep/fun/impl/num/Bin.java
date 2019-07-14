package exp.libs.warp.cep.fun.impl.num;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.CEPParseException;
import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	进制转换： 10 -> 2
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Bin extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 8438227776608140579L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "bin";
	
	/**
	 * 进制转换： 10 -> 2
	 * @param param Integer/String:10进制数值(字符串)
	 * @return String: 2进制数值字符串
	 * @throws EvaluationException 若解析失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		int iNum = 0;
		
		if(param instanceof String) {
			String sNum = asString(1, param);
			try {
				iNum = Integer.parseInt(sNum);
			} catch (NumberFormatException e) {
				throw new CEPParseException(this, 1, sNum, Integer.class, e);
			}
			
		} else {
			iNum = asInt(1, param);
		}
		return Integer.toBinaryString(iNum);
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
