package exp.libs.warp.db.redis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.conf.xml.XConfig;
import exp.libs.warp.db.redis.bean.RedisBean;

/**
 * <PRE>
 * Redis连接客户端.
 * 内部自带连接池, 适用于Redis单机/主从/哨兵/集群模式 .
 * 
 * 根据实际Redis的配置，使用不同的构造函数即可，已屏蔽到集群/非集群的连接/操作方式差异性.
 * Redis配置推荐使用{@link RedisBean}方式（{@link XConfig}}已提供对其的xml配置文件支持）
 * --------------------------------------------------
 * <br/>
 * 科普：
 * 	对于 单机/主从/哨兵 模式，连接方式都是一样的，使用{@link #Jedis}实例连接（已封装到{@link #_Jedis}）
 * 	对于 集群 模式，则需要使用{@link #JedisCluster}实例连接（已封装到{@link #_JedisCluster}）
 * 
 * 	一般情况下，对于 主从/哨兵 模式，只需要连接到主机即可（或者连接从机亦可，但一般不建议）
 * 	特别地，对于 哨兵模式，一定不能连接到 哨兵机（哨兵机是用于监控主从机器，当主机挂掉的时候重新选举主机的，不做数据业务）
 * 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RedisClient implements _IJedis {

	// FIXME 哨兵模式需要连接到哨兵机，又哨兵机分配当前可用的主机，使用 JedisSentinelPool 连接池
	// 详见 https://blog.csdn.net/shmilychan/article/details/73433804
	
	/** 默认redis IP */
	public final static String DEFAULT_IP = "127.0.0.1";
	
	/** 默认redis 端口 */
	public final static int DEFAULT_PORT = 6379;
	
	/**
	 * Redis连接接口, 其实现类有两个：
	 * 	_Jedis : 适用于Redis单机/主从/哨兵模式
	 *  _JedisCluster : 适用于Redis集群模式
	 */
	private _IJedis iJedis;
	
	/**
	 * 构造函数
	 * @param rb redis配置对象（通过{@link RedisBean#isCluster()}方法自动切换集群/非集群模式）
	 */
	public RedisClient(RedisBean rb) {
		
		// 默认模式
		if(rb == null) {
			this.iJedis = new _Jedis(DEFAULT_IP, DEFAULT_PORT);
			
		// 非集群模式
		} else if(!rb.isCluster()) {
			HostAndPort hp = new HostAndPort(DEFAULT_IP, DEFAULT_PORT);
			Iterator<String> sockets = rb.getSockets().iterator();
			if(sockets.hasNext()) {
				hp = toHostAndPort(sockets.next());
			}
			this.iJedis = new _Jedis(rb.toPoolConfig(), rb.getTimeout(), 
					rb.getPassword(), hp.getHost(), hp.getPort());
			
		// 集群模式
		} else {
			List<HostAndPort> clusterNodes = new LinkedList<HostAndPort>();
			Set<String> sockets = rb.getSockets();
			for(String socket : sockets) {
				HostAndPort node = toHostAndPort(socket);
				if(node != null) {
					clusterNodes.add(node);
				}
			}
			this.iJedis = new _JedisCluster(rb.toPoolConfig(), rb.getTimeout(), 
					rb.getPassword(), toArray(clusterNodes));
		}
	}
	
	/**
	 * 构造函数（单机模式）
	 * 使用默认的IP端口： 127.0.0.1:6379
	 */
	public RedisClient() {
		this(DEFAULT_IP, DEFAULT_PORT);
	}
	
	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 */
	public RedisClient(String ip, int port) {
		this.iJedis = new _Jedis(ip, port);
	}

	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param timeout 超时时间(ms)
	 */
	public RedisClient(String ip, int port, int timeout) {
		this.iJedis = new _Jedis(timeout, ip, port);
	}

	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param password redis密码
	 */
	public RedisClient(String ip, int port, String password) {
		this.iJedis = new _Jedis(password, ip, port);
	}

	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 */
	public RedisClient(String ip, int port, int timeout, String password) {
		this.iJedis = new _Jedis(timeout, password, ip, port);
	}
	
	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param timeout 超时时间(ms)
	 * @param poolConfig 连接池配置
	 */
	public RedisClient(String ip, int port, int timeout, 
			GenericObjectPoolConfig poolConfig) {
		this.iJedis = new _Jedis(poolConfig, timeout, ip, port);
	}
	
	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param password redis密码
	 * @param poolConfig 连接池配置
	 */
	public RedisClient(String ip, int port, String password, 
			GenericObjectPoolConfig poolConfig) {
		this.iJedis = new _Jedis(poolConfig, password, ip, port);
	}
	
	/**
	 * 构造函数（适用单机/主从/哨兵模式） 
	 * @param ip redis IP
	 * @param port redis端口
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 * @param poolConfig 连接池配置
	 */
	public RedisClient(String ip, int port, int timeout, String password, 
			GenericObjectPoolConfig poolConfig) {
		this.iJedis = new _Jedis(poolConfig, timeout, password, ip, port);
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(removeDuplicate(clusterNodes));
	}

	/**
	 * 构造函数（适用集群模式） 
	 * @param timeout 超时时间(ms)
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(int timeout, HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(timeout, 
				removeDuplicate(clusterNodes));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param password redis密码
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(String password, HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(password, 
				removeDuplicate(clusterNodes));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(int timeout, String password, 
			HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(timeout, password, 
				removeDuplicate(clusterNodes));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param timeout 超时时间(ms)
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			int timeout, HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(poolConfig, timeout, 
				removeDuplicate(clusterNodes));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param password redis密码
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			String password, HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(poolConfig, password, 
				removeDuplicate(clusterNodes));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 * @param clusterNodes 集群节点
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			int timeout, String password, HostAndPort... clusterNodes) {
		this.iJedis = new _JedisCluster(
				poolConfig, timeout, password, clusterNodes);
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(String... clusterSockets) {
		this(toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param timeout 超时时间(ms)
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(int timeout, String... clusterSockets) {
		this(timeout, toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param password redis密码
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(String password, String[] clusterSockets) {
		this(password, toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(int timeout, String password, String... clusterSockets) {
		this(timeout, password, toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param timeout 超时时间(ms)
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			int timeout, String... clusterSockets) {
		this(poolConfig, timeout, toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param password redis密码
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			String password, String... clusterSockets) {
		this(poolConfig, password, toHostAndPorts(clusterSockets));
	}
	
	/**
	 * 构造函数（适用集群模式） 
	 * @param poolConfig 连接池配置
	 * @param timeout 超时时间(ms)
	 * @param password redis密码
	 * @param clusterSockets 集群连接Socket串（格式为 ip:port，  如 127.0.0.1:6739）
	 */
	public RedisClient(GenericObjectPoolConfig poolConfig, 
			int timeout, String password, String... clusterSockets) {
		this(poolConfig, timeout, password, toHostAndPorts(clusterSockets));
	}

	/**
	 * 集群节点去重
	 * @param clusterNodes 集群节点
	 * @return 去重后的集群节点
	 */
	private static HostAndPort[] removeDuplicate(HostAndPort[] clusterNodes) {
		List<HostAndPort> list = new LinkedList<HostAndPort>();
		if(clusterNodes != null) {
			Set<String> sockets = new HashSet<String>();
			for(HostAndPort node : clusterNodes) {
				if(sockets.add(node.toString())) {
					list.add(node);
				}
			}
		}
		return toArray(list);
	}
	
	/**
	 * 把socket字符串格式的集群节点转换成HostAndPort格式
	 * @param clusterSockets socket字符串格式的集群节点
	 * @return HostAndPort格式的集群节点
	 */
	private static HostAndPort[] toHostAndPorts(String[] clusterSockets) {
		List<HostAndPort> list = new LinkedList<HostAndPort>();
		if(clusterSockets != null) {
			for(String socket : clusterSockets) {
				HostAndPort hp = toHostAndPort(socket);
				if(hp != null) {
					list.add(hp);
				}
			}
		}
		return toArray(list);
	}
	
	/**
	 * 把socket字符串格式的字符串转换成HostAndPort格式
	 * @param socket socket字符串格式
	 * @return HostAndPort格式
	 */
	private static HostAndPort toHostAndPort(String socket) {
		HostAndPort hp = null;
		if(StrUtils.isNotTrimEmpty(socket)) {
			String[] rst = HostAndPort.extractParts(socket);
			if(rst.length == 2) {
				String host = rst[0];
				int port = NumUtils.toInt(rst[1], DEFAULT_PORT);
				hp = new HostAndPort(host, port);
			}
		}
		return hp;
	}
	
	/**
	 * 把HostAndPort链表转换成数组
	 * @param clusterNodes HostAndPort链表
	 * @return HostAndPort数组
	 */
	private static HostAndPort[] toArray(List<HostAndPort> clusterNodes) {
		HostAndPort[] array = new HostAndPort[clusterNodes.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = clusterNodes.get(i);
		}
		return array;
	}

	@Override
	public boolean isVaild() {
		return iJedis.isVaild();
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		iJedis.setAutoCommit(autoCommit);
	}

	@Override
	public void closeAutoCommit() {
		iJedis.closeAutoCommit();
	}

	@Override
	public void commit() {
		iJedis.commit();
	}

	@Override
	public void destory() {
		iJedis.destory();
	}
	
	/**
	 * 断开redis连接
	 */
	public void close() {
		destory();
	}

	@Override
	public boolean clearAll() {
		return iJedis.clearAll();
	}

	@Override
	public boolean existKey(String redisKey) {
		return iJedis.existKey(redisKey);
	}

	@Override
	public long delKeys(String... redisKeys) {
		return iJedis.delKeys(redisKeys);
	}

	@Override
	public boolean addStrVal(String redisKey, String value) {
		return iJedis.addStrVal(redisKey, value);
	}

	@Override
	public long appendStrVal(String redisKey, String value) {
		return iJedis.appendStrVal(redisKey, value);
	}

	@Override
	public String getStrVal(String redisKey) {
		return iJedis.getStrVal(redisKey);
	}

	@Override
	public boolean addSerialObj(String redisKey, Serializable object) {
		return iJedis.addSerialObj(redisKey, object);
	}

	@Override
	public Object getSerialObj(String redisKey) {
		return iJedis.getSerialObj(redisKey);
	}

	@Override
	public boolean addStrMap(String redisKey, Map<String, String> map) {
		return iJedis.addStrMap(redisKey, map);
	}

	@Override
	public boolean addStrValToMap(String redisKey, String key, String value) {
		return iJedis.addStrValToMap(redisKey, key, value);
	}

	@Override
	public Map<String, String> getStrMap(String redisKey) {
		return iJedis.getStrMap(redisKey);
	}

	@Override
	public String getStrValInMap(String redisKey, String key) {
		return iJedis.getStrValInMap(redisKey, key);
	}

	@Override
	public List<String> getStrValsInMap(String redisKey, String... keys) {
		return iJedis.getStrValsInMap(redisKey, keys);
	}

	@Override
	public List<String> getAllStrValsInMap(String redisKey) {
		return iJedis.getAllStrValsInMap(redisKey);
	}

	@Override
	public boolean addSerialMap(String redisKey, Map<String, Serializable> map) {
		return iJedis.addSerialMap(redisKey, map);
	}

	@Override
	public boolean addSerialObjToMap(String redisKey, String key, Serializable object) {
		return iJedis.addSerialObjToMap(redisKey, key, object);
	}

	@Override
	public Map<String, Object> getSerialMap(String redisKey) {
		return iJedis.getSerialMap(redisKey);
	}

	@Override
	public Object getSerialObjInMap(String redisKey, String key) {
		return iJedis.getSerialObjInMap(redisKey, key);
	}

	@Override
	public List<Object> getSerialObjsInMap(String redisKey, String... keys) {
		return iJedis.getSerialObjsInMap(redisKey, keys);
	}

	@Override
	public List<Object> getAllSerialObjsInMap(String redisKey) {
		return iJedis.getAllSerialObjsInMap(redisKey);
	}

	@Override
	public boolean existKeyInMap(String redisKey, String key) {
		return iJedis.existKeyInMap(redisKey, key);
	}

	@Override
	public Set<String> getAllKeysInMap(String redisKey) {
		return iJedis.getAllKeysInMap(redisKey);
	}

	@Override
	public long delKeysInMap(String redisKey, String... keys) {
		return iJedis.delKeysInMap(redisKey, keys);
	}

	@Override
	public long getMapSize(String redisKey) {
		return iJedis.getMapSize(redisKey);
	}

	@Override
	public long addStrList(String redisKey, List<String> list) {
		return iJedis.addStrList(redisKey, list);
	}

	@Override
	public long addStrValsToList(String redisKey, String... values) {
		return iJedis.addStrValsToList(redisKey, values);
	}

	@Override
	public long addStrValsToListHead(String redisKey, String... values) {
		return iJedis.addStrValsToListHead(redisKey, values);
	}

	@Override
	public long addStrValsToListTail(String redisKey, String... values) {
		return iJedis.addStrValsToListTail(redisKey, values);
	}

	@Override
	public List<String> getStrList(String redisKey) {
		return iJedis.getStrList(redisKey);
	}

	@Override
	public String getStrValInList(String redisKey, long index) {
		return iJedis.getStrValInList(redisKey, index);
	}

	@Override
	public List<String> getAllStrValsInList(String redisKey) {
		return iJedis.getAllStrValsInList(redisKey);
	}

	@Override
	public long delStrValsInList(String redisKey, String value) {
		return iJedis.delStrValsInList(redisKey, value);
	}

	@Override
	public long delStrValsInList(String redisKey, String value, long count) {
		return iJedis.delStrValsInList(redisKey, value, count);
	}

	@Override
	public long addSerialList(String redisKey, List<Serializable> list) {
		return iJedis.addSerialList(redisKey, list);
	}

	@Override
	public long addSerialObjsToList(String redisKey, Serializable... objects) {
		return iJedis.addSerialObjsToList(redisKey, objects);
	}

	@Override
	public long addSerialObjsToListHead(String redisKey, Serializable... objects) {
		return iJedis.addSerialObjsToListHead(redisKey, objects);
	}

	@Override
	public long addSerialObjsToListTail(String redisKey, Serializable... objects) {
		return iJedis.addSerialObjsToListTail(redisKey, objects);
	}

	@Override
	public long delSerialObjsInList(String redisKey, Serializable object) {
		return iJedis.delSerialObjsInList(redisKey, object);
	}

	@Override
	public long delSerialObjsInList(String redisKey, Serializable object, long count) {
		return iJedis.delSerialObjsInList(redisKey, object, count);
	}

	@Override
	public List<Object> getSerialList(String redisKey) {
		return iJedis.getSerialList(redisKey);
	}

	@Override
	public Object getSerialObjInList(String redisKey, long index) {
		return iJedis.getSerialObjInList(redisKey, index);
	}

	@Override
	public List<Object> getAllSerialObjsInList(String redisKey) {
		return iJedis.getAllSerialObjsInList(redisKey);
	}

	@Override
	public long getListSize(String redisKey) {
		return iJedis.getListSize(redisKey);
	}

	@Override
	public long addStrSet(String redisKey, Set<String> set) {
		return iJedis.addStrSet(redisKey, set);
	}

	@Override
	public long addStrValsToSet(String redisKey, String... values) {
		return iJedis.addStrValsToSet(redisKey, values);
	}

	@Override
	public Set<String> getStrSet(String redisKey) {
		return iJedis.getStrSet(redisKey);
	}

	@Override
	public String getRandomStrValInSet(String redisKey) {
		return iJedis.getRandomStrValInSet(redisKey);
	}
	
	@Override
	public Set<String> getAllStrValsInSet(String redisKey) {
		return iJedis.getAllStrValsInSet(redisKey);
	}

	@Override
	public long delStrValsInSet(String redisKey, String... values) {
		return iJedis.delStrValsInSet(redisKey, values);
	}

	@Override
	public boolean existInSet(String redisKey, String value) {
		return iJedis.existInSet(redisKey, value);
	}

	@Override
	public long addSerialSet(String redisKey, Set<Serializable> set) {
		return iJedis.addSerialSet(redisKey, set);
	}

	@Override
	public long addSerialObjsToSet(String redisKey, Serializable... objects) {
		return iJedis.addSerialObjsToSet(redisKey, objects);
	}

	@Override
	public Set<Object> getSerialSet(String redisKey) {
		return iJedis.getSerialSet(redisKey);
	}

	@Override
	public Object getRandomSerialObjInSet(String redisKey) {
		return iJedis.getRandomSerialObjInSet(redisKey);
	}
	
	@Override
	public Set<Object> getAllSerialObjsInSet(String redisKey) {
		return iJedis.getAllSerialObjsInSet(redisKey);
	}

	@Override
	public long delSerialObjsInSet(String redisKey, Serializable... objects) {
		return iJedis.delSerialObjsInSet(redisKey, objects);
	}

	@Override
	public boolean existInSet(String redisKey, Serializable object) {
		return iJedis.existInSet(redisKey, object);
	}

	@Override
	public long getSetSize(String redisKey) {
		return iJedis.getSetSize(redisKey);
	}

}
