package exp.libs.warp.ehcache;



public class EhCacheUtils {

	public static void init() {
		EhCache.getInstn().init();
	}
	
	public static void flush() {
		EhCache.getInstn().flush();
	}
	
	public static void clear() {
		EhCache.getInstn().clear();
	}
	
	public static void stop() {
		EhCache.getInstn().stop();
	}
	
	public static void add(Object o) {
		EhCache.getInstn().add(o);
	}
	
	public static void add(Object key, Object val) {
		EhCache.getInstn().add(key, val);
	}
	
	public static boolean contains(Object o) {
		return EhCache.getInstn().contains(o);
	}
	
	public static Object get(Object key) {
		return EhCache.getInstn().get(key);
	}
}
