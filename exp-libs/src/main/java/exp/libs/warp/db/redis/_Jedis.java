package exp.libs.warp.db.redis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import exp.libs.envm.Charset;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.ObjUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * Redis连接池（仅适用于Redis单机/主从/哨兵模式）
 * ----------------------------------
 *  若Redis是以集群模式部署, 使用此连接池虽然可以连接, 但只是连接到集群的其中一台节点机器.
 *  换而言之，此时只能在这台特定的机器上面进行数据读写.
 *  若在机器A上面写, 再在机器B上面读, 就会因为不是使用集群连接而报错: JedisMovedDataException: MOVED 866
 *  解决方式是改用 {@link #_JedisCluster} 集群连接模式. 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class _Jedis implements _IJedis {

	/** 默认字符集编码 */
	private final static String CHARSET = Charset.UTF8;
	
	/** Redis部分接口的返回值 */
	private final static String OK = "OK";

	/** 测试Redis连接有效性的返回值 */
	private final static String PONG = "PONG";

	/** Jedis连接池 */
	private JedisPool pool;
	
	/** 是否自动提交(默认true, 即所有操作均为短连接) */
	private boolean autoCommit;
	
	/** 当前持有的长连接(仅autoCommit=false时有效) */
	private Jedis longJedis;
	
	protected _Jedis(String ip, int port) {
		this(null, Protocol.DEFAULT_TIMEOUT, null, ip, port);
	}
	
	protected _Jedis(int timeout, String ip, int port) {
		this(null, timeout, null, ip, port);
	}
	
	protected _Jedis(String password, String ip, int port) {
		this(null, Protocol.DEFAULT_TIMEOUT, password, ip, port);
	}
	
	protected _Jedis(int timeout, String password, String ip, int port) {
		this(null, timeout, password, ip, port);
	}
	
	protected _Jedis(GenericObjectPoolConfig poolConfig, String ip, int port) {
		this(poolConfig, Protocol.DEFAULT_TIMEOUT, null, ip, port);
	}
	
	protected _Jedis(GenericObjectPoolConfig poolConfig, 
			int timeout, String ip, int port) {
		this(poolConfig, timeout, null, ip, port);
	}
	
	protected _Jedis(GenericObjectPoolConfig poolConfig, 
			String password, String ip, int port) {
		this(poolConfig, Protocol.DEFAULT_TIMEOUT, password, ip, port);
	}
	
	protected _Jedis(GenericObjectPoolConfig poolConfig, 
			int timeout, String password, String ip, int port) {
		if(poolConfig == null) {
			poolConfig = new JedisPoolConfig();
		}
		
		this.pool = StrUtils.isTrimEmpty(password) ? 
				new JedisPool(poolConfig, ip, port, timeout) : 
				new JedisPool(poolConfig, ip, port, timeout, password);
		this.autoCommit = true;
		this.longJedis = null;
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
	
	/**
	 * 从连接池获取Redis连接
	 * @return
	 */
	private Jedis _getJedis() {
		Jedis conn = null;
		if(autoCommit) {
			conn = pool.getResource();
			
		} else if(longJedis == null || !longJedis.isConnected()) {
			conn = pool.getResource();
			longJedis = conn;
			
		} else {
			conn = longJedis;
		}
		return conn;
	}
	
	/**
	 * 把Redis连接返回连接池
	 * @param jedis
	 */
	private void _close(Jedis jedis) {
		if(!autoCommit && jedis == longJedis) {
			return;
		}
		
		if(jedis != null) {
			try {
//				pool.returnResource(jedis);
				jedis.close();
			} catch(Exception e) {
				// UNDO 重复关闭会抛出运行时异常
			}
		}
	}

	@Override
	public boolean isVaild() {
		Jedis jedis = _getJedis();
		boolean isOk = _isVaild(jedis);
		_close(jedis);
		return isOk;
	}
	
	private boolean _isVaild(Jedis jedis) {
		boolean isOk = false;
		if(jedis != null) {
			isOk = PONG.equalsIgnoreCase(jedis.ping());
		}
		return isOk;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
		if(autoCommit == true) {
			_close(longJedis);
			longJedis = null;
		}
	}

	@Override
	public void closeAutoCommit() {
		setAutoCommit(false);
	}

	@Override
	public void commit() {
		setAutoCommit(true);
	}
	
	@Override
	public void destory() {
		commit();
		pool.close();
	}

	@Override
	public boolean clearAll() {
		Jedis jedis = _getJedis();
		boolean isOk = OK.equalsIgnoreCase(jedis.flushAll());
		_close(jedis);
		return isOk;
	}

	@Override
	public boolean existKey(String redisKey) {
		boolean isExist = false;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			isExist = jedis.exists(_transcode(redisKey));
			_close(jedis);
		}
		return isExist;
	}

	@Override
	public long delKeys(String... redisKeys) {
		long num = 0;
		if(redisKeys != null) {
			Jedis jedis = _getJedis();
			for(String redisKey : redisKeys) {
				if(redisKey == null) {
					continue;
				}
				num += jedis.del(_transcode(redisKey));
			}
			_close(jedis);
		}
		return num;
	}

	@Override
	public boolean addStrVal(String redisKey, String value) {
		boolean isOk = false;
		if(redisKey != null && value != null) {
			Jedis jedis = _getJedis();
			isOk = OK.equalsIgnoreCase(jedis.set(_transcode(redisKey), value));
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public long appendStrVal(String redisKey, String value) {
		long len = -1;
		if(redisKey != null && value != null) {
			Jedis jedis = _getJedis();
			len = jedis.append(_transcode(redisKey), value);
			_close(jedis);
		}
		return len;
	}

	@Override
	public String getStrVal(String redisKey) {
		String value = "";
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			value = jedis.get(_transcode(redisKey));
			_close(jedis);
		}
		return value;
	}

	@Override
	public boolean addSerialObj(String redisKey, Serializable object) {
		boolean isOk = false;
		if(redisKey != null && object != null) {
			Jedis jedis = _getJedis();
			isOk = OK.equalsIgnoreCase(jedis.set(
					_transbyte(redisKey), 
					ObjUtils.toSerializable(object))
			);
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public Object getSerialObj(String redisKey) {
		Object object = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			object = ObjUtils.unSerializable(jedis.get(_transbyte(redisKey)));
			_close(jedis);
		}
		return object;
	}

	@Override
	public boolean addStrMap(String redisKey, Map<String, String> map) {
		boolean isOk = false;
		if(redisKey != null && map != null) {
			Jedis jedis = _getJedis();
			isOk = OK.equalsIgnoreCase(jedis.hmset(_transcode(redisKey), map));
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public boolean addStrValToMap(String redisKey, String key, String value) {
		boolean isOk = false;
		if(redisKey != null && key != null && value != null) {
			Jedis jedis = _getJedis();
			isOk = jedis.hset(_transcode(redisKey), key, value) >= 0;
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public Map<String, String> getStrMap(String redisKey) {
		Map<String, String> map = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			map = jedis.hgetAll(_transcode(redisKey));
			_close(jedis);
		}
		return (map == null ? new HashMap<String, String>() : map);
	}

	@Override
	public String getStrValInMap(String redisKey, String key) {
		String value = null;
		if(redisKey != null && key != null) {
			Jedis jedis = _getJedis();
			List<String> values = jedis.hmget(_transcode(redisKey), key);
			if(ListUtils.isNotEmpty(values)) {
				value = values.get(0);
			}
			_close(jedis);
		}
		return value;
	}

	@Override
	public List<String> getStrValsInMap(String redisKey, String... keys) {
		List<String> values = null;
		if(redisKey != null && keys != null) {
			Jedis jedis = _getJedis();
			values = jedis.hmget(_transcode(redisKey), keys);
			_close(jedis);
		}
		return (values == null ? new LinkedList<String>() : values);
	}

	@Override
	public List<String> getAllStrValsInMap(String redisKey) {
		List<String> values = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			values = jedis.hvals(_transcode(redisKey));
			_close(jedis);
		}
		return (values == null ? new LinkedList<String>() : values);
	}

	@Override
	public boolean addSerialMap(String redisKey, Map<String, Serializable> map) {
		boolean isOk = false;
		if(redisKey != null && map != null) {
			isOk = true;
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				Serializable object = map.get(key);
				isOk &= jedis.hset(byteKey, _transbyte(key), 
						ObjUtils.toSerializable(object)) >= 0;
			}
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public boolean addSerialObjToMap(String redisKey, String key,
			Serializable object) {
		boolean isOk = false;
		if(redisKey != null && key != null && object != null) {
			Jedis jedis = _getJedis();
			isOk = jedis.hset(_transbyte(redisKey), _transbyte(key), 
					ObjUtils.toSerializable(object)) >= 0;
			_close(jedis);
		}
		return isOk;
	}

	@Override
	public Map<String, Object> getSerialMap(String redisKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			Map<byte[], byte[]> byteMap = jedis.hgetAll(_transbyte(redisKey));
			Iterator<byte[]> keys = byteMap.keySet().iterator();
			while(keys.hasNext()) {
				byte[] key = keys.next();
				byte[] val = byteMap.get(key);
				map.put(_transstr(key), ObjUtils.unSerializable(val));
			}
			_close(jedis);
		}
		return map;
	}

	@Override
	public Object getSerialObjInMap(String redisKey, String key) {
		Object value = null;
		if(redisKey != null && key != null) {
			Jedis jedis = _getJedis();
			List<byte[]> values = jedis.hmget(
					_transbyte(redisKey), _transbyte(key));
			if(ListUtils.isNotEmpty(values)) {
				value = ObjUtils.unSerializable(values.get(0));
			}
			_close(jedis);
		}
		return value;
	}

	@Override
	public List<Object> getSerialObjsInMap(String redisKey, String... keys) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null && keys != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(String key : keys) {
				List<byte[]> byteVals = jedis.hmget(byteKey, _transbyte(key));
				if(ListUtils.isEmpty(byteVals)) {
					values.add(null);
					
				} else {
					values.add(ObjUtils.unSerializable(byteVals.get(0)));
				}
			}
			_close(jedis);
		}
		return (values == null ? new LinkedList<Object>() : values);
	}

	@Override
	public List<Object> getAllSerialObjsInMap(String redisKey) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			List<byte[]> byteVals = jedis.hvals(byteKey);
			for(byte[] byteVal : byteVals) {
				values.add(ObjUtils.unSerializable(byteVal));
			}
			_close(jedis);
		}
		return (values == null ? new LinkedList<Object>() : values);
	}

	@Override
	public boolean existKeyInMap(String redisKey, String key) {
		boolean isExist = false;
		if(redisKey != null && key != null) {
			Jedis jedis = _getJedis();
			isExist = jedis.hexists(_transcode(redisKey), key);
			_close(jedis);
		}
		return isExist;
	}

	@Override
	public Set<String> getAllKeysInMap(String redisKey) {
		Set<String> keys = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			keys = jedis.hkeys(_transcode(redisKey));
			_close(jedis);
		}
		return (keys == null ? new HashSet<String>() : keys);
	}

	@Override
	public long delKeysInMap(String redisKey, String... keys) {
		long num = 0;
		if(redisKey != null && keys != null) {
			Jedis jedis = _getJedis();
			num = jedis.hdel(_transcode(redisKey), keys);
			_close(jedis);
		}
		return num;
	}

	@Override
	public long getMapSize(String redisKey) {
		long size = 0L;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			size = jedis.hlen(_transcode(redisKey)); 
			_close(jedis);
		}
		return size;
	}

	@Override
	public long addStrList(String redisKey, List<String> list) {
		long num = 0;
		if(redisKey != null && list != null) {
			Jedis jedis = _getJedis();
			redisKey = _transcode(redisKey);
			for(String value : list) {
				if(value == null) {
					continue;
				}
				num = jedis.rpush(redisKey, value);
			}
			_close(jedis);
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
			Jedis jedis = _getJedis();
			redisKey = _transcode(redisKey);
			for(String value : values) {
				if(value == null) {
					continue;
				}
				num = jedis.lpush(redisKey, value);
			}
			_close(jedis);
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
			Jedis jedis = _getJedis();
			value = jedis.lindex(_transcode(redisKey), index);
			_close(jedis);
		}
		return value;
	}

	@Override
	public List<String> getAllStrValsInList(String redisKey) {
		List<String> values = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			values = jedis.lrange(_transcode(redisKey), 0, -1);
			_close(jedis);
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
			Jedis jedis = _getJedis();
			num = jedis.lrem(_transcode(redisKey), count, value);
			_close(jedis);
		}
		return num;
	}

	@Override
	public long addSerialList(String redisKey, List<Serializable> list) {
		long num = 0;
		if(redisKey != null && list != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : list) {
				if(object == null) {
					continue;
				}
				num = jedis.rpush(byteKey, ObjUtils.toSerializable(object));
			}
			_close(jedis);
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
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num = jedis.lpush(byteKey, ObjUtils.toSerializable(object));
			}
			_close(jedis);
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
			Jedis jedis = _getJedis();
			num = jedis.lrem(_transbyte(redisKey), count, 
					ObjUtils.toSerializable(object));
			_close(jedis);
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
			Jedis jedis = _getJedis();
			object = ObjUtils.unSerializable(
					jedis.lindex(_transbyte(redisKey), index));
			_close(jedis);
		}
		return object;
	}

	@Override
	public List<Object> getAllSerialObjsInList(String redisKey) {
		List<Object> values = new LinkedList<Object>();
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			List<byte[]> byteVals = jedis.lrange(_transbyte(redisKey), 0, -1);
			if(ListUtils.isNotEmpty(byteVals)) {
				for(byte[] byteVal : byteVals) {
					values.add(ObjUtils.unSerializable(byteVal));
				}
			}
			_close(jedis);
		}
		return values;
	}

	@Override
	public long getListSize(String redisKey) {
		long size = 0L;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			size = jedis.llen(_transcode(redisKey)); 
			_close(jedis);
		}
		return size;
	}

	@Override
	public long addStrSet(String redisKey, Set<String> set) {
		long num = 0;
		if(redisKey != null && set != null) {
			Jedis jedis = _getJedis();
			redisKey = _transcode(redisKey);
			for(String s : set) {
				num += jedis.sadd(redisKey, s);
			}
			_close(jedis);
		}
		return num;
	}

	@Override
	public long addStrValsToSet(String redisKey, String... values) {
		long num = 0;
		if(redisKey != null && values != null) {
			Jedis jedis = _getJedis();
			num = jedis.sadd(_transcode(redisKey), values);
			_close(jedis);
		}
		return num;
	}

	@Override
	public Set<String> getStrSet(String redisKey) {
		return getAllStrValsInSet(redisKey);
	}

	@Override
	public String getRandomStrValInSet(String redisKey) {
		String value = null;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			value = jedis.srandmember(_transcode(redisKey));
			_close(jedis);
		}
		return value;
	}
	
	@Override
	public Set<String> getAllStrValsInSet(String redisKey) {
		Set<String> values = new HashSet<String>();
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			values = jedis.smembers(_transcode(redisKey));
			_close(jedis);
		}
		return values;
	}

	@Override
	public long delStrValsInSet(String redisKey, String... values) {
		long num = 0;
		if(redisKey != null && values != null) {
			Jedis jedis = _getJedis();
			num = jedis.srem(_transcode(redisKey), values);
			_close(jedis);
		}
		return num;
	}

	@Override
	public boolean existInSet(String redisKey, String value) {
		boolean isExist = false;
		if(redisKey != null && value != null) {
			Jedis jedis = _getJedis();
			isExist = jedis.sismember(_transcode(redisKey), value);
			_close(jedis);
		}
		return isExist;
	}

	@Override
	public long addSerialSet(String redisKey, Set<Serializable> set) {
		long num = 0L;
		if(redisKey != null && set != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : set) {
				if(object == null) {
					continue;
				}
				num += jedis.sadd(byteKey, ObjUtils.toSerializable(object));
			}
			_close(jedis);
		}
		return num;
	}

	@Override
	public long addSerialObjsToSet(String redisKey, Serializable... objects) {
		long num = 0L;
		if(redisKey != null && objects != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num += jedis.sadd(byteKey, ObjUtils.toSerializable(object));
			}
			_close(jedis);
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
			Jedis jedis = _getJedis();
			value = ObjUtils.unSerializable(
					jedis.srandmember(_transbyte(redisKey)));
			_close(jedis);
		}
		return value;
	}
	
	@Override
	public Set<Object> getAllSerialObjsInSet(String redisKey) {
		Set<Object> values = new HashSet<Object>();
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			Set<byte[]> byteVals = jedis.smembers(_transbyte(redisKey));
			if(ListUtils.isNotEmpty(byteVals)) {
				for(byte[] byteVal : byteVals) {
					values.add(ObjUtils.unSerializable(byteVal));
				}
			}
			_close(jedis);
		}
		return values;
	}

	@Override
	public long delSerialObjsInSet(String redisKey, Serializable... objects) {
		long num = 0;
		if(redisKey != null && objects != null) {
			Jedis jedis = _getJedis();
			byte[] byteKey = _transbyte(redisKey);
			for(Serializable object : objects) {
				if(object == null) {
					continue;
				}
				num += jedis.srem(byteKey, ObjUtils.toSerializable(object));
			}
			_close(jedis);
		}
		return num;
	}

	@Override
	public boolean existInSet(String redisKey, Serializable object) {
		boolean isExist = false;
		if(redisKey != null && object != null) {
			Jedis jedis = _getJedis();
			isExist = jedis.sismember(_transbyte(redisKey), 
					ObjUtils.toSerializable(object));
			_close(jedis);
		}
		return isExist;
	}

	@Override
	public long getSetSize(String redisKey) {
		long size = 0;
		if(redisKey != null) {
			Jedis jedis = _getJedis();
			size = jedis.scard(_transcode(redisKey));
			_close(jedis);
		}
		return size;
	}

}
