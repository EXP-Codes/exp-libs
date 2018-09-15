package exp.libs.warp.db.redis;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import exp.libs.envm.Charset;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.ObjUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * Redis集群连接（仅适用于Redis集群模式）
 * ----------------------------------
 * 注意：
 * 	经测试，对于利用redis-trib.rb构建的Redis Cluster，
 *  若节点的配置文件 redis.conf 中同时绑定了[内网IP]和[外网IP]， 
 *  即使redis-trib.rb构建集群时使用的是[外网IP]，也无法通过JedisCluster[从外网]访问集群（报错为无法获取连接）
 *  但是[从内网]访问是可以的，具体原因不明。
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class _JedisCluster extends JedisCluster implements _IJedis {

	/** 默认字符集编码 */
	private final static String CHARSET = Charset.UTF8;
	
	/** Redis部分接口的返回值 */
	private final static String OK = "OK";

	protected _JedisCluster(HostAndPort... clusterNodes) {
		this(null, DEFAULT_TIMEOUT, null, clusterNodes);
	}
	
	protected _JedisCluster(int timeout, HostAndPort... clusterNodes) {
		this(null, timeout, null, clusterNodes);
	}
	
	protected _JedisCluster(String password, HostAndPort... clusterNodes) {
		this(null, DEFAULT_TIMEOUT, password, clusterNodes);
	}
	
	protected _JedisCluster(int timeout, String password, 
			HostAndPort... clusterNodes) {
		this(null, timeout, password, clusterNodes);
	}
	
	protected _JedisCluster(GenericObjectPoolConfig poolConfig, 
			HostAndPort... clusterNodes) {
		this(poolConfig, DEFAULT_TIMEOUT, null, clusterNodes);
	}
	
	protected _JedisCluster(GenericObjectPoolConfig poolConfig, 
			int timeout, HostAndPort... clusterNodes) {
		this(poolConfig, timeout, null, clusterNodes);
	}
	
	protected _JedisCluster(GenericObjectPoolConfig poolConfig, 
			String password, HostAndPort... clusterNodes) {
		this(poolConfig, DEFAULT_TIMEOUT, password, clusterNodes);
	}
	
	@SuppressWarnings("unchecked")
	protected _JedisCluster(GenericObjectPoolConfig poolConfig, 
			int timeout, String password, HostAndPort... clusterNodes) {
		super(new HashSet<HostAndPort>(ListUtils.asList(clusterNodes)), timeout, 
				timeout, DEFAULT_MAX_REDIRECTIONS, 
				(StrUtils.isEmpty(password) ? null : password), 
				(poolConfig == null ? new GenericObjectPoolConfig() : poolConfig));
	}

	/**
	 * 对Redis键统一转码，使得Jedis的 String接口 和 byte[]接口 所产生的键值最终一致。
	 * (若不转码, 在redis编码与程序编码不一致的情况下, 即使键值相同, 
	 * 	但使用String接口与byte[]接口存储到Redis的是两个不同的哈希表)
	 * @param redisKey redis键
	 * @return 统一转码后的redis键
	 */
	private String _transcode(String redisKey) {
		return CharsetUtils.transcode(redisKey, CHARSET);
	}
	
	/**
	 * 对Redis键统一转码，使得Jedis的 String接口 和 byte[]接口 所产生的键值最终一致。
	 * (若不转码, 在redis编码与程序编码不一致的情况下, 即使键值相同, 
	 * 	但使用String接口与byte[]接口存储到Redis的是两个不同的哈希表)
	 * @param redisKey redis键
	 * @return 统一转码后的redis键(字节数组)
	 */
	private byte[] _transbyte(String redisKey) {
		return CharsetUtils.toBytes(redisKey, CHARSET);
	}
	
	/**
	 * 对Redis键统一转码，使得Jedis的 String接口 和 byte[]接口 所产生的键值最终一致。
	 * (若不转码, 在redis编码与程序编码不一致的情况下, 即使键值相同, 
	 * 	但使用String接口与byte[]接口存储到Redis的是两个不同的哈希表)
	 * @param redisKey redis键(字节数组)
	 * @return 统一转码后的redis键
	 */
	private String _transstr(byte[] redisKey) {
		return CharsetUtils.toStr(redisKey, CHARSET);
	}
	
	@Deprecated
	@Override
	public boolean isVaild() {
		return false;	// 集群模式不支持此操作
	}
	
	@Deprecated
	@Override
	public void setAutoCommit(boolean autoCommit) {
		// Undo 集群模式不支持此操作
	}

	@Override
	public void closeAutoCommit() {
		// Undo 集群模式不支持此操作
	}

	@Deprecated
	@Override
	public void commit() {
		// Undo 集群模式不支持此操作
	}
	
	@Override
	public void destory() {
		try {
			super.close();
		} catch (IOException e) {}
	}
	
	@Override
	public boolean clearAll() {
		return false;	// 集群模式不支持此操作
	}

	@Override
	public boolean existKey(String redisKey) {
		boolean isExist = false;
		if(redisKey != null) {
			isExist = super.exists(_transcode(redisKey));
		}
		return isExist;
	}

	@Override
	public long delKeys(String... redisKeys) {
		long num = 0;
		if(redisKeys != null) {
			for(String redisKey : redisKeys) {
				if(redisKey == null) {
					continue;
				}
				num += super.del(_transcode(redisKey));
			}
		}
		return num;
	}

	@Override
	public boolean addStrVal(String redisKey, String value) {
		boolean isOk = false;
		if(redisKey != null && value != null) {
			isOk = OK.equalsIgnoreCase(super.set(_transcode(redisKey), value));
		}
		return isOk;
	}

	@Override
	public long appendStrVal(String redisKey, String value) {
		long len = -1;
		if(redisKey != null && value != null) {
			len = super.append(_transcode(redisKey), value);
		}
		return len;
	}

	@Override
	public String getStrVal(String redisKey) {
		String value = "";
		if(redisKey != null) {
			value = super.get(_transcode(redisKey));
		}
		return value;
	}

	@Override
	public boolean addSerialObj(String redisKey, Serializable object) {
		boolean isOk = false;
		if(redisKey != null && object != null) {
			isOk = OK.equalsIgnoreCase(super.set(
					_transbyte(redisKey), 
					ObjUtils.toSerializable(object))
			);
		}
		return isOk;
	}

	@Override
	public Object getSerialObj(String redisKey) {
		Object object = null;
		if(redisKey != null) {
			object = ObjUtils.unSerializable(super.get(_transbyte(redisKey)));
		}
		return object;
	}

	@Override
	public boolean addStrMap(String redisKey, Map<String, String> map) {
		boolean isOk = false;
		if(redisKey != null && map != null) {
			isOk = OK.equalsIgnoreCase(super.hmset(_transcode(redisKey), map));
		}
		return isOk;
	}

	@Override
	public boolean addStrValToMap(String redisKey, String key, String value) {
		boolean isOk = false;
		if(redisKey != null && key != null && value != null) {
			isOk = super.hset(_transcode(redisKey), key, value) >= 0;
		}
		return isOk;
	}

	@Override
	public Map<String, String> getStrMap(String redisKey) {
		Map<String, String> map = null;
		if(redisKey != null) {
			map = super.hgetAll(_transcode(redisKey));
		}
		return (map == null ? new HashMap<String, String>() : map);
	}

	@Override
	public String getStrValInMap(String redisKey, String key) {
		String value = null;
		if(redisKey != null && key != null) {
			List<String> values = super.hmget(_transcode(redisKey), key);
			if(ListUtils.isNotEmpty(values)) {
				value = values.get(0);
			}
		}
		return value;
	}

	@Override
	public List<String> getStrValsInMap(String redisKey, String... keys) {
		List<String> values = null;
		if(redisKey != null && keys != null) {
			values = super.hmget(_transcode(redisKey), keys);
		}
		return (values == null ? new LinkedList<String>() : values);
	}

	@Override
	public List<String> getAllStrValsInMap(String redisKey) {
		List<String> values = null;
		if(redisKey != null) {
			values = super.hvals(_transcode(redisKey));
		}
		return (values == null ? new LinkedList<String>() : values);
	}

	@Override
	public boolean addSerialMap(String redisKey, Map<String, Serializable> map) {
		boolean isOk = false;
		if(redisKey != null && map != null) {
			isOk = true;
			byte[] byteKey = _transbyte(redisKey);
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				Serializable object = map.get(key);
				isOk &= super.hset(byteKey, _transbyte(key), 
						ObjUtils.toSerializable(object)) >= 0;
			}
		}
		return isOk;
	}

	@Override
	public boolean addSerialObjToMap(String redisKey, String key,
			Serializable object) {
		boolean isOk = false;
		if(redisKey != null && key != null && object != null) {
			isOk = super.hset(_transbyte(redisKey), _transbyte(key), 
					ObjUtils.toSerializable(object)) >= 0;
		}
		return isOk;
	}

	@Override
	public Map<String, Object> getSerialMap(String redisKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(redisKey != null) {
			Map<byte[], byte[]> byteMap = super.hgetAll(_transbyte(redisKey));
			Iterator<byte[]> keys = byteMap.keySet().iterator();
			while(keys.hasNext()) {
				byte[] key = keys.next();
				byte[] val = byteMap.get(key);
				map.put(_transstr(key), ObjUtils.unSerializable(val));
			}
		}
		return map;
	}

	@Override
	public Object getSerialObjInMap(String redisKey, String key) {
		Object value = null;
		if(redisKey != null && key != null) {
			List<byte[]> values = super.hmget(
					_transbyte(redisKey), _transbyte(key));
			if(ListUtils.isNotEmpty(values)) {
				value = ObjUtils.unSerializable(values.get(0));
			}
		}
		return value;
	}

	@Override
	public List<Object> getSerialObjsInMap(String redisKey, String... keys) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null && keys != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(String key : keys) {
				List<byte[]> byteVals = super.hmget(byteKey, _transbyte(key));
				if(ListUtils.isEmpty(byteVals)) {
					values.add(null);
					
				} else {
					values.add(ObjUtils.unSerializable(byteVals.get(0)));
				}
			}
		}
		return (values == null ? new LinkedList<Object>() : values);
	}

	@Override
	public List<Object> getAllSerialObjsInMap(String redisKey) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null) {
			byte[] byteKey = _transbyte(redisKey);
			Collection<byte[]> byteVals = super.hvals(byteKey);
			for(byte[] byteVal : byteVals) {
				values.add(ObjUtils.unSerializable(byteVal));
			}
		}
		return (values == null ? new LinkedList<Object>() : values);
	}

	@Override
	public boolean existKeyInMap(String redisKey, String key) {
		boolean isExist = false;
		if(redisKey != null && key != null) {
			isExist = super.hexists(_transcode(redisKey), key);
		}
		return isExist;
	}

	@Override
	public Set<String> getAllKeysInMap(String redisKey) {
		Set<String> keys = new HashSet<String>();
		if(redisKey != null) {
			keys = super.hkeys(_transcode(redisKey));
		}
		return (keys == null ? new HashSet<String>() : keys);
	}

	@Override
	public long delKeysInMap(String redisKey, String... keys) {
		long num = 0;
		if(redisKey != null && keys != null) {
			num = super.hdel(_transcode(redisKey), keys);
		}
		return num;
	}

	@Override
	public long getMapSize(String redisKey) {
		long size = 0L;
		if(redisKey != null) {
			size = super.hlen(_transcode(redisKey)); 
		}
		return size;
	}

	@Override
	public long addStrList(String redisKey, List<String> list) {
		long num = 0;
		if(redisKey != null && list != null) {
			redisKey = _transcode(redisKey);
			for(String value : list) {
				if(value == null) {
					continue;
				}
				num = super.rpush(redisKey, value);
			}
		}
		return num;
	}

	@Override
	public long addStrValsToList(String redisKey, String... values) {
		return addStrValsToListTail(redisKey, values);
	}

	@Override
	public long addStrValsToListHead(String redisKey, String... values) {
		long num = 0;
		if(redisKey != null && values != null) {
			redisKey = _transcode(redisKey);
			for(String value : values) {
				if(value == null) {
					continue;
				}
				num = super.lpush(redisKey, value);
			}
		}
		return num;
	}

	@Override
	public long addStrValsToListTail(String redisKey, String... values) {
		long num = 0;
		if(redisKey != null && values != null) {
			num = addStrList(redisKey, Arrays.asList(values));
		}
		return num;
	}

	@Override
	public List<String> getStrList(String redisKey) {
		return getAllStrValsInList(redisKey);
	}

	@Override
	public String getStrValInList(String redisKey, long index) {
		String value = null;
		if(redisKey != null) {
			value = super.lindex(_transcode(redisKey), index);
		}
		return value;
	}

	@Override
	public List<String> getAllStrValsInList(String redisKey) {
		List<String> values = null;
		if(redisKey != null) {
			values = super.lrange(_transcode(redisKey), 0, -1);
		}
		return (values == null ? new LinkedList<String>() : values);
	}

	@Override
	public long delStrValsInList(String redisKey, String value) {
		return delStrValsInList(redisKey, value, 0);
	}

	@Override
	public long delStrValsInList(String redisKey, String value, long count) {
		long num = 0L;
		if(redisKey != null && value != null) {
			num = super.lrem(_transcode(redisKey), count, value);
		}
		return num;
	}

	@Override
	public long addSerialList(String redisKey, List<Serializable> list) {
		long num = 0;
		if(redisKey != null && list != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : list) {
				if(object == null) {
					continue;
				}
				num = super.rpush(byteKey, ObjUtils.toSerializable(object));
			}
		}
		return num;
	}

	@Override
	public long addSerialObjsToList(String redisKey, Serializable... objects) {
		return addSerialObjsToListTail(redisKey, objects);
	}

	@Override
	public long addSerialObjsToListHead(String redisKey,
			Serializable... objects) {
		long num = 0;
		if(redisKey != null && objects != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num = super.lpush(byteKey, ObjUtils.toSerializable(object));
			}
		}
		return num;
	}

	@Override
	public long addSerialObjsToListTail(String redisKey,
			Serializable... objects) {
		long num = 0;
		if(redisKey != null && objects != null) {
			num = addSerialList(redisKey, Arrays.asList(objects));
		}
		return num;
	}

	@Override
	public long delSerialObjsInList(String redisKey, Serializable object) {
		return delSerialObjsInList(redisKey, object, 0);
	}

	@Override
	public long delSerialObjsInList(String redisKey, Serializable object, long count) {
		long num = 0L;
		if(redisKey != null && object != null) {
			num = super.lrem(_transbyte(redisKey), count, 
					ObjUtils.toSerializable(object));
		}
		return num;
	}

	@Override
	public List<Object> getSerialList(String redisKey) {
		return getAllSerialObjsInList(redisKey);
	}

	@Override
	public Object getSerialObjInList(String redisKey, long index) {
		Object object = null;
		if(redisKey != null) {
			object = ObjUtils.unSerializable(
					super.lindex(_transbyte(redisKey), index));
		}
		return object;
	}

	@Override
	public List<Object> getAllSerialObjsInList(String redisKey) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null) {
			List<byte[]> byteVals = super.lrange(_transbyte(redisKey), 0, -1);
			if(ListUtils.isNotEmpty(byteVals)) {
				for(byte[] byteVal : byteVals) {
					values.add(ObjUtils.unSerializable(byteVal));
				}
			}
		}
		return values;
	}

	@Override
	public long getListSize(String redisKey) {
		long size = 0L;
		if(redisKey != null) {
			size = super.llen(_transcode(redisKey)); 
		}
		return size;
	}

	@Override
	public long addStrSet(String redisKey, Set<String> set) {
		long num = 0;
		if(redisKey != null && set != null) {
			redisKey = _transcode(redisKey);
			for(String s : set) {
				num += super.sadd(redisKey, s);
			}
		}
		return num;
	}

	@Override
	public long addStrValsToSet(String redisKey, String... values) {
		long addNum = 0;
		if(redisKey != null && values != null) {
			addNum = super.sadd(_transcode(redisKey), values);
		}
		return addNum;
	}

	@Override
	public Set<String> getStrSet(String redisKey) {
		return getAllStrValsInSet(redisKey);
	}

	@Override
	public String getRandomStrValInSet(String redisKey) {
		String value = null;
		if(redisKey != null) {
			value = super.srandmember(_transcode(redisKey));
		}
		return value;
	}
	
	@Override
	public Set<String> getAllStrValsInSet(String redisKey) {
		Set<String> values = new HashSet<String>();
		if(redisKey != null) {
			values = super.smembers(_transcode(redisKey));
		}
		return values;
	}

	@Override
	public long delStrValsInSet(String redisKey, String... values) {
		long num = 0;
		if(redisKey != null && values != null) {
			num = super.srem(_transcode(redisKey), values);
		}
		return num;
	}

	@Override
	public boolean existInSet(String redisKey, String value) {
		boolean isExist = false;
		if(redisKey != null && value != null) {
			isExist = super.sismember(_transcode(redisKey), value);
		}
		return isExist;
	}

	@Override
	public long addSerialSet(String redisKey, Set<Serializable> set) {
		long num = 0L;
		if(redisKey != null && set != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : set) {
				if(object == null) {
					continue;
				}
				num += super.sadd(byteKey, ObjUtils.toSerializable(object));
			}
		}
		return num;
	}

	@Override
	public long addSerialObjsToSet(String redisKey, Serializable... objects) {
		long num = 0L;
		if(redisKey != null && objects != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num += super.sadd(byteKey, ObjUtils.toSerializable(object));
			}
		}
		return num;
	}

	@Override
	public Set<Object> getSerialSet(String redisKey) {
		return getAllSerialObjsInSet(redisKey);
	}

	@Override
	public Object getRandomSerialObjInSet(String redisKey) {
		Object value = null;
		if(redisKey != null) {
			value = ObjUtils.unSerializable(
					super.srandmember(_transbyte(redisKey)));
		}
		return value;
	}
	
	@Override
	public Set<Object> getAllSerialObjsInSet(String redisKey) {
		Set<Object> values = new HashSet<Object>();
		if(redisKey != null) {
			Set<byte[]> byteVals = super.smembers(_transbyte(redisKey));
			if(ListUtils.isNotEmpty(byteVals)) {
				for(byte[] byteVal : byteVals) {
					values.add(ObjUtils.unSerializable(byteVal));
				}
			}
		}
		return values;
	}

	@Override
	public long delSerialObjsInSet(String redisKey, Serializable... objects) {
		long num = 0;
		if(redisKey != null && objects != null) {
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num += super.srem(byteKey, ObjUtils.toSerializable(object));
			}
		}
		return num;
	}

	@Override
	public boolean existInSet(String redisKey, Serializable object) {
		boolean isExist = false;
		if(redisKey != null && object != null) {
			isExist = super.sismember(_transbyte(redisKey), 
					ObjUtils.toSerializable(object));
		}
		return isExist;
	}

	@Override
	public long getSetSize(String redisKey) {
		long size = 0;
		if(redisKey != null) {
			size = super.scard(_transcode(redisKey));
		}
		return size;
	}

}
