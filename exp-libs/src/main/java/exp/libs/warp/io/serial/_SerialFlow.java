package exp.libs.warp.io.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.PathUtils;

/**
 * <PRE>
 * 序列化基类
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-07-01
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _SerialFlow {

	/** 日志器 */
	protected final static Logger log = LoggerFactory.getLogger(SerialFlowReader.class);
	
	/** 默认序列化文件位置 */
	protected final static String DEFAULT_FILEPATH = OSUtils.isRunByTomcat() ? 
			PathUtils.getProjectCompilePath().concat("serializable.dat") : 
			"./serializable.dat";
			
}
