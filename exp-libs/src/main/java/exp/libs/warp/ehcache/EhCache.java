package exp.libs.warp.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

final class EhCache {

	private final static String CONF_PATH = "./conf/ehcache.xml";
	
	private final static String CACHE_NAME = "data-cache";
	
	private final static int MEMORY_SIZE = 5000;
	
	private CacheManager cm;
	
	private Cache cache;
	
	private int cnt;
	
	private static volatile EhCache instance;
	
	private EhCache() {
		init();
	};
	
	protected static EhCache getInstn() {
		if(instance == null) {
			synchronized (EhCache.class) {
				if(instance == null) {
					instance = new EhCache();
				}
			}
		}
		return instance;
	}
	
	protected void init() {
		this.cm = CacheManager.create(CONF_PATH);
		this.cache = cm.getCache(CACHE_NAME);
		this.cnt = 0;
	}
	
	/**
	 * 强制刷出到外存
	 */
	protected void flush() {
		cache.flush();
		cnt = 0;
	}
	
	protected void clear() {
		cm.clearAll();
		cnt = 0;
	}
	
	/**
	 * 会自动清理外存文件
	 */
	protected void stop() {
		cm.shutdown();
		cm = null;
		cache = null;
		cnt = 0;
	}
	
	protected void add(Object o) {
		add(o, "");
	}
	
	protected void add(Object key, Object val) {
		// FIXME : 仅2.8.3版本支持
//		cache.putIfAbsent(new Element(key, val));
//		if(++cnt >= MEMORY_SIZE) {
//			flush();
//		}
	}
	
	protected boolean contains(Object o) {
		return cache.isElementInMemory(o) || cache.isElementOnDisk(o);
	}
	
	protected Object get(Object key) {
		return cache.get(key);
	}
	
}
