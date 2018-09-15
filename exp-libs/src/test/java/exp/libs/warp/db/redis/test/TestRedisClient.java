package exp.libs.warp.db.redis.test;

import java.io.Serializable;

import exp.libs.warp.db.redis.RedisClient;
import exp.libs.warp.db.redis.bean.RedisList;
import exp.libs.warp.db.redis.bean.RedisMap;
import exp.libs.warp.db.redis.bean.RedisObj;
import exp.libs.warp.db.redis.bean.RedisSet;

/**
 * <PRE>
 * RedisClient测试/场景演示.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestRedisClient {

	public static void main(String[] args) {
		
		/*********************** Redis连接样例 ***********************/
		
		// 场景1  -  Redis 单机模式
		// 127.0.0.1:6379
		RedisClient redis = new RedisClient("127.0.0.1", 6379);
		
		// 场景2  -  Redis 主从模式 
		// （若用于[读写]只能连接主机，若仅[读]则可连接主/从，但无论如何只能连接其中一台）
		// 主机： 127.0.0.1:6:6379
		// 从机： 127.0.0.1:6380, 127.0.0.1:6381, 127.0.0.1:6382
		redis = new RedisClient("127.0.0.1", 6379);
		
		// 场景3  -  Redis 哨兵模式 
		// （若用于[读写]只能连接主机，若仅[读]则可连接主/从，但无论如何只能连接其中一台，哨兵不允许连接）
		// 主机： 127.0.0.1:6379
		// 从机： 127.0.0.1:6380, 127.0.0.1:6381, 127.0.0.1:6382
		// 哨兵： 127.0.0.1:26380, 127.0.0.1:26381, 127.0.0.1:26382
		redis = new RedisClient("127.0.0.1", 6379);
				
		// 场景4  -  Redis 集群模式 
		// 集群节点（需同时连接所有节点）：
		//   127.0.0.1:6390, 127.0.0.1:6391, 127.0.0.1:6392
		//   127.0.0.1:6393, 127.0.0.1:6394, 127.0.0.1:6395
		redis = new RedisClient(
				"127.0.0.1:6390", "127.0.0.1:6391", 
				"127.0.0.1:6392", "127.0.0.1:6393", 
				"127.0.0.1:6394", "127.0.0.1:6395"
		);
		
		/*********************** Redis操作样例 ***********************/
		
		// RedisMap示例
		final String REDIS_MAP_KEY = "REDIS_MAP_KEY";	// 这个Map对象在Redis中的键值
		RedisMap<SerialObject> map = new RedisMap<SerialObject>(REDIS_MAP_KEY, redis);
		map.put("site", new SerialObject(1, "http://exp-blog.com"));
		map.put("mail", new SerialObject(2, "289065406@qq.com"));
		System.out.println(map.size());
		System.out.println(map.get("site"));
		System.out.println(map.get("mail"));
		map.clear();
		
		// RedisList示例
		final String REDIS_LIST_KEY = "REDIS_LIST_KEY";	// 这个List对象在Redis中的键值
		RedisList<SerialObject> list = new RedisList<SerialObject>(REDIS_LIST_KEY, redis);
		list.add(new SerialObject(3, "EXP-LIST"));
		System.out.println(list.size());
		System.out.println(list.get(0));
		list.clear();	
		
		// RedisSet示例
		final String REDIS_SET_KEY = "REDIS_SET_KEY";	// 这个Set对象在Redis中的键值
		RedisSet<SerialObject> set = new RedisSet<SerialObject>(REDIS_SET_KEY, redis);
		set.add(new SerialObject(4, "http://exp-blog.com"));
		set.add(new SerialObject(5, "289065406@qq.com"));
		System.out.println(set.size());
		System.out.println(set.getRandom());
		set.clear();
		
		// RedisObj示例
		final String REDIS_OBJ_KEY = "REDIS_OBJ_KEY";	// 这个Obj对象在Redis中的键值
		RedisObj<SerialObject> obj = new RedisObj<SerialObject>(REDIS_OBJ_KEY, redis);
		obj.set(new SerialObject(6, "EXP-OBJ"));
		System.out.println(obj.exists());
		System.out.println(obj.get());
		obj.clear();
		
		
		// 断开redis连接
		redis.close();
	}
	
	
	/**
	 * <pre>
	 * 测试用的序列化对象.
	 * 必须实现java.io.Serializable接口
	 * </pre>
	 */
	private static class SerialObject implements Serializable {
		
		private static final long serialVersionUID = -6911765769239092862L;

		private int id;
		
		private String value;
		
		private SerialObject(int id, String value) {
			this.id = id;
			this.value = value;
		}
		
		public String toString() {
			return id + ":" + value;
		}
		
	}
	
}
