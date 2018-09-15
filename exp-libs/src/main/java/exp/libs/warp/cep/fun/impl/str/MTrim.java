package exp.libs.warp.cep.fun.impl.str;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	去换行，去空格
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class MTrim extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 8381083020732955709L;
	
	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "mtrim";
	
	/**
	 * 去除字符串头尾空字符.
	 * 共1个入参：
	 * @param1 String:原字符串
	 * @return String
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		String str = asString(1, param);
		return str.trim().replaceAll("\r\n|\n|\r|\\s", "");
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
