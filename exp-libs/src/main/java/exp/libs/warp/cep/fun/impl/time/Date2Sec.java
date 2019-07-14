package exp.libs.warp.cep.fun.impl.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.CEPParseException;
import exp.libs.warp.cep.fun.BaseFunction1;

/**
 * <pre>
 * 自定函数：
 * 	yyyy-MM-dd HH:mm:ss -> 纪元秒 转换
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Date2Sec extends BaseFunction1 {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 8272123829108108343L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "date2sec";
	
	/**
	 * yyyy-MM-dd HH:mm:ss -> 纪元秒 转换.
	 * @param String/Date: yyyy-MM-dd HH:mm:ss格式的日期
	 * @return Long: 纪元秒
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(Object param) throws EvaluationException {
		long second = -1L;
		if(param instanceof String) {
			String dataFormat = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
			String dataStr = asString(1, param);
			
			try {
				second = sdf.parse(dataStr).getTime();
			} catch (ParseException e) {
				throw new CEPParseException(this, 1, dataStr, dataFormat, e);
			}
			
		} else {
			second = asDate(1, param).getTime();
		}
		return second;
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
