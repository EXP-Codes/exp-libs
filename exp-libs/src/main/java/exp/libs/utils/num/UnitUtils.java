package exp.libs.utils.num;

import java.text.DecimalFormat;

import exp.libs.envm.StorageUnit;
import exp.libs.envm.TimeUnit;

/**
 * <PRE>
 * 单位转换工具.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class UnitUtils {

	/** 获取 [1byte] 的数值表示 (单位:byte) */
	public final static int _1_BYTE = 1;
	
	/** 获取 [1KB] 的数值表示 (单位:byte) */
	public final static int _1_KB = 1024 * _1_BYTE;
	
	/** 获取 [1MB] 的数值表示 (单位:byte) */
	public final static int _1_MB = 1024 * _1_KB;
	
	/** 获取 [1GB] 的数值表示 (单位:byte) */
	public final static int _1_GB = 1024 * _1_MB;
	
	/** 获取 [1TB] 的数值表示 (单位:byte) */
	public final static long _1_TB = 1024L * _1_GB;
	
	/** 私有化构造函数 */
	protected UnitUtils() {}
	
	/**
	 * 字节单位转换
	 * @param bytes 字节大小
	 * @return 根据字节大小自动调整为byte、KB、MB等单位字符串
	 */
	public String convertBytes(long bytes) {
		double size = (double) bytes;
		String unit = StorageUnit.BYTE.VAL;
		
		if(size >= 1024 && StorageUnit.BYTE.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.KB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.KB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.MB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.MB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.GB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.GB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.TB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.TB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.PB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.PB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.EB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.EB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.ZB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.ZB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.YB.VAL;
		}
		
		if(size >= 1024 && StorageUnit.YB.VAL.equals(unit)) { 
			size = size / 1024.0;
			unit = StorageUnit.BB.VAL;
		}
		return new DecimalFormat("0.00 " + unit).format(size);
	}
	
	/**
	 * byte -> KB
	 * @param bytes 字节大小
	 * @return KB大小
	 */
	public static double toKB(long bytes) {
		return bytes / 1024.0;
	}
	
	/**
	 * byte -> MB
	 * @param bytes 字节大小
	 * @return MB大小
	 */
	public static double toMB(long bytes) {
		return toKB(bytes) / 1024.0;
	}
	
	/**
	 * byte -> GB
	 * @param bytes 字节大小
	 * @return GB大小
	 */
	public static double toGB(long bytes) {
		return toMB(bytes) / 1024.0;
	}
	
	/**
	 * byte -> TB
	 * @param bytes 字节大小
	 * @return TB大小
	 */
	public static double toTB(long bytes) {
		return toGB(bytes) / 1024.0;
	}
	
	/**
	 * 毫秒单位转换
	 * @param millis 毫秒值
	 * @return 根据毫秒值大小自动调整为ms、s、minute、hour、day等单位字符串
	 */
	public static String convertMills(long millis) {
		double time = millis;
		String unit = TimeUnit.MS.VAL;
		
		if(time >= 1000 && TimeUnit.MS.VAL.equals(unit)) { 
			time = time / 1000.0;
			unit = TimeUnit.SECOND.VAL;
		}
		
		if(time >= 60 && TimeUnit.SECOND.VAL.equals(unit)) { 
			time = time / 60.0;
			unit = TimeUnit.MINUTE.VAL;
		}
		
		if(time >= 60 && TimeUnit.MINUTE.VAL.equals(unit)) { 
			time = time / 60.0;
			unit = TimeUnit.HOUR.VAL;
		}
		
		if(time >= 24 && TimeUnit.HOUR.VAL.equals(unit)) { 
			time = time / 24.0;
			unit = TimeUnit.DAY.VAL;
		}
		return new DecimalFormat("0.00 " + unit).format(time);
	}
	
	/**
	 * millis -> second
	 * @param millis 毫秒值
	 * @return 秒值
	 */
	public static double toSecond(long millis) {
		return millis / 1000.0;
	}
	
	/**
	 * millis -> minute
	 * @param millis 毫秒值
	 * @return 分钟
	 */
	public static double toMinute(long millis) {
		return millis / 60000.0;
	}
	
	/**
	 * millis -> hour
	 * @param millis 毫秒值
	 * @return 小时
	 */
	public static double toHour(long millis) {
		return millis / 3600000.0;
	}
	
	/**
	 * millis -> day
	 * @param millis 毫秒值
	 * @return 天
	 */
	public static double toDay(long millis) {
		return millis / 86400000.0;
	}
	
}
