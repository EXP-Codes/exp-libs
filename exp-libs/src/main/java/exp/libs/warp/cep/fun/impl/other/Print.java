package exp.libs.warp.cep.fun.impl.other;

import java.io.File;
import java.util.List;

import com.singularsys.jep.EvaluationException;

import exp.libs.warp.cep.fun.BaseFunctionN;
import exp.libs.utils.io.FileUtils;

/**
 * <pre>
 * 自定函数：
 * 	打印消息到 标准流、异常流、或文件.
 * 	调试配置时可用.
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Print extends BaseFunctionN {

	/**
	 * 序列化唯一ID
	 */
	private static final long serialVersionUID = 2264898922532103999L;

	/**
	 * 建议函数名,方便调用.
	 * 可不使用.
	 */
	public final static String NAME = "wait";
	
	/**
	 * 打印消息到 标准流、异常流、或文件.
	 * 共2个入参：
	 * @param1 String:需要打印的消息
	 * @param2 String:
	 * 	输出位置:
	 * 	"out":标准流
	 * 	"err":异常流(默认)
	 * 	两者都不是:认为是文件路径(若路径异常自动切回默认值,并打印异常原因)
	 * @return Boolean: true:打印成功; false:打印失败
	 * @throws EvaluationException 若执行失败则抛出异常
	 */
	@Override
	protected Object eval(List<Object> params) throws EvaluationException {
		String msg = asString(1, params.remove(0));
		String pTo = asString(2, params.remove(0));
		
		Boolean isPrint = true;
		if("out".equalsIgnoreCase(pTo)) {
			System.out.println(msg);
			
		} else if("err".equalsIgnoreCase(pTo)) {
			System.err.println(msg);
			
		} else {
			File file = FileUtils.createFile(pTo);
			if(file != null && file.exists() && !file.isDirectory()) {
				isPrint = FileUtils.write(file, msg, true);
			}
		}
		return isPrint;
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
