package exp.libs.utils.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.num.NumUtils;

/**
 * <PRE>
 * 布尔数据处理工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-19
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BoolUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(BoolUtils.class);
	
	/** 私有化构造函数 */
	protected BoolUtils() {}
	
	/**
	 * 把字符串转换成bool对象
	 * @param tof "true"或"false"字符串（忽略大小写）
	 * @return true或false
	 */
	public static boolean toBool(String tof) {
		return toBool(tof, false);
	}
	
	/**
	 * 把字符串转换成bool对象
	 * @param tof "true"或"false"字符串（忽略大小写）
	 * @param defavlt 默认值
	 * @return true或false, 转换失败则返回默认值
	 */
	public static boolean toBool(String tof, boolean defavlt) {
		boolean bool = defavlt;
		try {
			bool = Boolean.parseBoolean(tof.toLowerCase());
		} catch (Exception e) {
			log.debug("转换 [{}] 为bool类型失败.", tof, e);
		}
		return bool;
	}
	
	/**
	 * 使得 %percent 几率命中
	 * @param percent 命中百分比（取值范围0-100）
	 * @return 此方法会随机产生一个数值，若在百分比范围内则认为命中，此时返回true
	 */
	public static boolean hit(int percent) {
		percent = NumUtils.limitRange(percent, 0, 100);
		int random = RandomUtils.genInt(1, 100);
		return random <= percent;
	}
	
}
