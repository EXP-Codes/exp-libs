package exp.libs.utils.num;

import java.util.UUID;

import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.time.TimeUtils;

/**
 * <PRE>
 * 唯一性ID生成器工具.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-02
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class IDUtils {

	private final static byte[] LOCK_SECOND_ID = new byte[1];
	private static volatile int LAST_SECOND_ID = -1;
	
	private final static byte[] LOCK_MILLIS_ID = new byte[1];
	private static volatile long LAST_MILLIS_ID = -1L;
	
	private final static byte[] LOCK_TIME_ID = new byte[1];
	private static volatile long LAST_TIME_ID = -1L;
	
	protected IDUtils() {}
	
	/**
	 * 获取时空唯一性ID
	 * @return UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * <PRE>
	 * 获取时间序唯一性ID（秒级）
	 *  当频繁获取ID时，此方法会强制使得每次请求最多延迟1s以保证唯一性
	 * </PRE>
	 * @return 时间序唯一性ID（秒级）
	 */
	public static int getSecondID() {
		int id = -1;
		synchronized (LOCK_SECOND_ID) {
			do {
				id = (int) (System.currentTimeMillis() / 1000);
				if(LAST_SECOND_ID != id) {
					break;
				}
				ThreadUtils.tSleep(500);
			} while(true);
			LAST_SECOND_ID = id;
		}
		return id;
	}
	
	/**
	 * 获取时间序唯一性ID（毫秒级）
	 * @return 时间序唯一性ID（毫秒级）
	 */
	public static long getMillisID() {
		long id = -1;
		synchronized (LOCK_MILLIS_ID) {
			do {
				id = System.currentTimeMillis();
			} while(LAST_MILLIS_ID == id);
			LAST_MILLIS_ID = id;
		}
		return id;
	}
	
	/**
	 * 获取时间序唯一性ID（yyyyMMddHHmmssSSS）
	 * @return 时间序唯一性ID（毫秒级）
	 */
	public static long getTimeID() {
		long id = -1;
		synchronized (LOCK_TIME_ID) {
			do {
				id = NumUtils.toLong(TimeUtils.getSysDate("yyyyMMddHHmmssSSS"));
			} while(LAST_TIME_ID == id);
			LAST_TIME_ID = id;
		}
		return id;
	}
	
}
