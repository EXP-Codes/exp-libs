package exp.libs.warp.cep;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.PostfixMathCommandI;

/**
 * <pre>
 * 函数解析异常类。
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public final class CEPParseException extends EvaluationException {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = -7153724066612560181L;

	/**
	 * 函数名称
	 */
	private String funName;
	
	/**
	 * 参数编号,从1开始
	 */
	private int pidx;
	
	/**
	 * 被解析的数据
	 */
	private Object data;
	
	/**
	 * 解析格式
	 */
	private String format;
	
	/**
	 * 强制类型转换的类
	 */
	private Class<?> clazz;
	
	/**
	 * 引起异常的原因
	 */
	private Throwable cause;
	
	/**
	 * 构造函数
	 * @param pmci 函数接口
	 * @param pidx 参数编号，从1开始
	 * @param data 被解析的数据
	 * @param format 解析格式
	 * @param cause 引起异常的原因
	 */
	public CEPParseException(PostfixMathCommandI pmci, int pidx, 
			Object data, String format, Throwable cause) {
		this.funName = pmci.getName();
		this.pidx = pidx;
		this.data = data;
		this.format = format;
		this.clazz = null;
		this.cause = cause;
	}
	
	/**
	 * 构造函数
	 * @param pmci 函数接口
	 * @param pidx 参数编号，从1开始
	 * @param data 被解析的数据
	 * @param clazz 强制类型转换的类
	 * @param cause 引起异常的原因
	 */
	public CEPParseException(PostfixMathCommandI pmci, int pidx,  
			Object data, Class<?> clazz, Throwable cause) {
		this.funName = pmci.getName();
		this.pidx = pidx;
		this.data = data;
		this.format = null;
		this.clazz = clazz;
		this.cause = cause;
	}
	
	/**
	 * 返回异常信息
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Function [").append(funName).append("] parse param ");
		sb.append(pidx).append(" exception : \r\n");
		
		//格式转换异常
		if(format != null) {
			sb.append("Can't parse [").append(data);
			sb.append("] with [").append(format).append("].");
			
		//强制类型转换异常
		} else {
			String dataType = (data == null ? "Null" : 
				data.getClass().getSimpleName());
			
			sb.append("Can't parse data [").append(data);
			sb.append("] cast type from [").append(dataType).append("] to [");
			sb.append(clazz.getSimpleName()).append("].");
		}
		sb.append("\r\nCause By: ").append(cause.getMessage());
		return sb.toString();
	}
}
