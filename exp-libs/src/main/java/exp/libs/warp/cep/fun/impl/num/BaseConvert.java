package exp.libs.warp.cep.fun.impl.num;

import java.util.List;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunctionN;

/**
 * <pre>
 * 自定函数：
 * 	进制互转:10进制正整数 <-> 任意进制数(>=2, <=36)
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BaseConvert extends BaseFunctionN {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 7613079111724651345L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "base";
	
	/**
	 * 进制数字符串正则
	 */
	private final static String BASE_NUM_REGEX = "[0-9a-zA-Z]+";
	
	/**
	 * 限定参数个数为2.
	 */
	@Override
	public boolean checkNumberOfParameters(int inParamsNum){
        return inParamsNum == 2;
    }
	
	/**
	 * 进制互转:10进制正整数 <-> 任意进制数(>=2, <=36)
	 * 共2个参数,但根据类型有2种应用：
	 * 
	 * @param1 long:10进制正整数(>=0, 不在此范围则置值为0)
	 * @param2 int:转换进制基数(>=2, <=36, 不在此范围则置值为10)
	 * @return String: x进制数
	 * 或
	 * @param1 String:任意进制数字符串(空串则置返回值为0)
	 * @param2 int:所给定字符串的进制数(>=2, <=36, 不在此范围则置返回值为0)
	 * @return long:10进制数
	 * 
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(List<Object> params) throws EvaluationException {
		Object param1 = params.remove(0);
		Object result = null;
		
		// 任意进制数 -> 10进制正整数
		if(param1 instanceof String) {
			String baseNum = asString(1, param1);
			int base = asInt(2, params.remove(0));
			result = base2Dec(baseNum, base);
			
		// 10进制正整数 -> 任意进制数
		} else {
			long dec = asLong(1, param1);
			int base = asInt(2, params.remove(0));
			result = dec2Base(dec, base);
		}
		return result;
	}
	
	/**
	 * 进制转换: 任意进制数(>=2, <=36) -> 10进制正整数
	 * @param1 String:任意进制数字符串(空串则置返回值为0)
	 * @param2 int:所给定字符串的进制数(>=2, <=36, 不在此范围则置返回值为0)
	 * @return long:10进制数
	 */
	private Long base2Dec(String baseNum, int base) {
		long dec = 0L;
		if(baseNum == null || !baseNum.trim().matches(BASE_NUM_REGEX)) {
			dec = 0L;
			
		} else if(base < 2 || base > 36) {
			dec = 0L;
			
		} else {
			StringBuilder sb = new StringBuilder(baseNum.trim().toUpperCase());
			char[] chs = sb.reverse().toString().toCharArray();
			int p = 0;	//指数
			for(char ch : chs) {
				int n = (int) (ch <= '9' ? ch - '0' : ch - '7');
				dec += n * (long) Math.pow(base, p++);
			}
		}
		return Long.valueOf(dec);
	}
	
	/**
	 * 进制转换:10进制正整数 -> 任意进制数(>=2, <=36)
	 * @param1 int:10进制正整数(>=0, 不在此范围则置值为0)
	 * @param2 int:转换进制基数(>=2, <=36, 不在此范围则置值为10)
	 * @return String: base进制数
	 */
	private String dec2Base(long dec, int base) {
		StringBuilder sb = new StringBuilder();
		base = (base >= 2 && base <= 36 ? base : 10);
		
		if(dec <= 0) {
			sb.append(0);
			
		} else {
			while(dec > 0) {
				int r = (int) (dec % base);	//进制数限制在36,不会溢出
				char ch = (char) (r < 10 ? r + '0' : r + '7');
				sb.append(ch);
				dec /= base;
			}
		}
		sb.reverse();	//反转
		return sb.toString();
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
