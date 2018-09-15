package exp.libs.warp.db.redis.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import exp.libs.warp.db.redis.RedisClient;

/**
 * <PRE>
 * Redis-Map对象（其键类型固定为String，值类型通过泛型动态声明）.
 * 模仿HashMap的使用习惯进行封装.
 * ------------------------------
 * 
 * 使用样例:
 * 
 * final String MAP_IN_REDIS_KEY = "map在Redis中的键名（自定义且需唯一）";
 * {@link #RedisClient} redis = new RedisClient("127.0.0.1", 6379);	// redis连接客户端（支持单机/集群）
 * 
 * RedisMap&lt;自定义对象&gt; map = new RedisMap&lt;自定义对象&gt;(MAP_IN_REDIS_KEY, redis);
 * map.put(key, val);
 * map.putAll(otherMap);
 * map.get(key);
 * map.containsKey(key);
 * map.isEmpty();
 * map.size();
 * map.keySet();
 * map.values();
 * map.getAll();
 * map.remove(key);
 * map.clear();
 * 
 * redis.close();	// 断开redis连接
 * 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RedisMap<VAL extends Serializable> extends _RedisObject {

	/** 此哈希表的默认键名 */
	private final static String DEFAULT_MAP_NAME = "REDIS_MAP";
	
	/** 此哈希表在redis中的键名（需确保不为空） */
	private final String MAP_NAME;
	
	/**
	 * 构造函数
	 * @param mapName 此哈希表在redis中的键名（需确保不为空）
	 * @param redis redis客户端连接对象（需确保可用）
	 */
	public RedisMap(String mapName, RedisClient redis) {
		super(redis);
		this.MAP_NAME = (mapName == null ? DEFAULT_MAP_NAME : mapName);
	}
	
	/**
	 * 实时在redis缓存中检查此哈希表是否包含某个键
	 * @param key 被检查的键
	 * @return true:包含; false:不包含
	 */
	public boolean containsKey(String key) {
		boolean isExist = false;
		try {
			isExist = redis.existKeyInMap(MAP_NAME, key);
			
		} catch(Exception e) {
			log.error("查询redis缓存失败", e);
		}
		return isExist;
	}
	
	/**
	 * 实时在redis缓存中检查此哈希表是否为空
	 * @return true:为空; false:非空
	 */
	public boolean isEmpty() {
		return size() <= 0;
	}
	
	/**
	 * 实时在redis缓存中查询此哈希表的大小
	 * @return 哈希表的大小
	 */
	public long size() {
		long size = 0L;
		try {
			size = redis.getMapSize(MAP_NAME);
			
		} catch(Exception e) {
			log.error("查询redis缓存失败", e);
		}
		return size;
	}
	
	/**
	 * 实时在redis缓存中向此哈希表添加一个键值对
	 * @param key 键
	 * @param value 值
	 * @return true:添加成功; false:添加失败
	 */
	public boolean put(String key, VAL value) {
		boolean isOk = false;
		if(key == null || value == null) {
			return isOk;
		}
		
		try {
			if(typeIsStr() || value instanceof String) {
				setTypeStr();
				isOk = redis.addStrValToMap(MAP_NAME, key, (String) value);
				
			} else {
				setTypeObj();
				isOk = redis.addSerialObjToMap(MAP_NAME, key, value);
			}
		} catch(Exception e) {
			log.error("写入redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中向此哈希表添加一个哈希表
	 * @param map 哈希表
	 * @return true:添加成功; false:添加失败
	 */
	public boolean putAll(Map<String, VAL> map) {
		boolean isOk = false;
		if(map == null || map.isEmpty()) {
			return isOk;
		}
		
		isOk = true;
		redis.closeAutoCommit();
		Iterator<String> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			VAL obj = map.get(key);
			isOk &= put(key, obj);
		}
		redis.commit();
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中查询此哈希表中的一个键值
	 * @param key 被查询的键
	 * @return 对应的值（若不存在返回null）
	 */
	@SuppressWarnings("unchecked")
	public VAL get(String key) {
		VAL value = null;
		if(isEmpty() || key == null) {
			return value;
		}
		alignType(key);	// 尝试校准类型
		
		try {
			if(typeIsStr()) {
				String str = redis.getStrValInMap(MAP_NAME, key);
				if(str != null) {
					value = (VAL) str;
				}
				
			} else if(typeIsObj()) {
				Object obj = redis.getSerialObjInMap(MAP_NAME, key);
				if(obj != null) {
					value = (VAL) obj;
				}
				
			} else {
				// Undo: 若无法校准类型，说明该键值不存在
			}
		} catch(Exception e) {
			log.error("读取redis缓存失败", e);
		}
		return value;
	}
	
	/**
	 * 实时在redis缓存中查询此哈希表的所有键值对
	 * @return 所有键值对（即使为空也只会返回空表，不会返回null）
	 */
	public Map<String, VAL> getAll() {
		Map<String, VAL> map = new HashMap<String, VAL>();
		
		redis.closeAutoCommit();
		Set<String> keys = keySet();
		for(String key : keys) {
			VAL val = get(key);
			if(val != null) {
				map.put(key, val);
			}
		}
		redis.commit();
		return map;
	}
	
	/**
	 * 实时在redis缓存中查询此哈希表的所有键集
	 * @return 所有键集（即使为空也只会返回空表，不会返回null）
	 */
	public Set<String> keySet() {
		Set<String> keys = null;
		try {
			keys = redis.getAllKeysInMap(MAP_NAME);
			
		} catch(Exception e) {
			log.error("读取redis缓存失败", e);
		}
		return (keys == null ? new HashSet<String>() : keys);
	}
	
	/**
	 * 实时在redis缓存中查询此哈希表的所有值集
	 * @return 所有值集（即使为空也只会返回空表，不会返回null）
	 */
	public Collection<VAL> values() {
		return getAll().values();
	}
	
	/**
	 * 实时在redis缓存中删除此哈希表的若干个键值对
	 * @param keys 被删除的键集
	 * @return 已成功删除的数量
	 */
	public long remove(String... keys) {
		long num = 0L;
		try {
			num = redis.delKeysInMap(MAP_NAME, keys);
			
		} catch(Exception e) {
			log.error("删除redis缓存失败", e);
		}
		return num;
	}
	
	/**
	 * 实时在redis缓存中删除此哈希表
	 * @return true:删除成功; false:删除失败
	 */
	public boolean clear() {
		boolean isOk = false;
		try {
			isOk = redis.delKeys(MAP_NAME) >= 0;
			
		} catch(Exception e) {
			log.error("删除redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * 校准类型（此方法通过随机取哈希表中的一个元素进行判断，因此只能在哈希表非空时执行）
	 * @param key 当前准备取出的一个元素的键（由于该元素未必存在，因此此方法未必能一次校准）
	 */
	private void alignType(String key) {
		if(!typeIsNone()) {
			return;
		}
		
		try {
			Object obj = redis.getSerialObjInMap(MAP_NAME, key);
			if(obj != null) {
				if(obj instanceof String) {
					setTypeStr();
					
				} else {
					setTypeObj();
				}
			} else {
				String str = redis.getStrValInMap(MAP_NAME, key);
				if(str != null) {
					setTypeStr();
				}
			}
		} catch(Exception ex) {
			log.error("读取redis缓存失败", ex);
		}
	}
	
}
