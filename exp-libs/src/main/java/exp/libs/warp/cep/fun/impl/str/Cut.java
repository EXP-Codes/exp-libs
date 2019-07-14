package exp.libs.warp.cep.fun.impl.str;

import java.util.List;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunctionN;

/**
 * <pre>
 * 自定函数：
 * 	字符串操作：定点切割.
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Cut extends BaseFunctionN {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 4655975901512831863L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "cut";
	
	/**
	 * 限定参数个数为3.
	 */
	@Override
	public boolean checkNumberOfParameters(int inParamsNum){
        return inParamsNum == 3;
    }
	
	/**
	 * 字符串切割,任何非法的起止标识都只会返回原字符串.
	 * @param 共3个入参：
	 * 		param[1] String:原字符串
	 * 		param[2] int:切割起点,从0开始（包括）
	 * 		param[3] int： 切割止点（不包括）
	 * @return String
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(List<Object> params) throws EvaluationException {
		String cutStr = asString(1, params.remove(0));
		int iBgnIdx = asInt(2, params.remove(0));
		int iEndIdx = asInt(3, params.remove(0));
		
		if(iBgnIdx >= 0 && iBgnIdx < iEndIdx) {
			cutStr = cutStr.substring(iBgnIdx, iEndIdx);
		}
		return cutStr;
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
