package exp.libs.warp.db.redis.bean;

import java.io.Serializable;

import exp.libs.warp.db.redis.RedisClient;

/**
 * <PRE>
 * Redis-Obj对象.
 * 封装了Redis最基本的键值对操作.
 * ------------------------------
 * 
 * 使用样例:
 * 
 * final String OBJ_IN_REDIS_KEY = "object在Redis中的键名（自定义且需唯一）";
 * {@link #RedisClient} redis = new RedisClient("127.0.0.1", 6379);	// redis连接客户端（支持单机/集群）
 * 
 * RedisObj&lt;自定义对象&gt; obj = new RedisObj&lt;自定义对象&gt;(OBJ_IN_REDIS_KEY, redis);
 * obj.set(value);
 * obj.appent(string);	// 当且仅当使用String作为泛型时此方法才有效
 * obj.get();
 * obj.exists();
 * list.clear();
 * 
 * redis.close();	// 断开redis连接
 * 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RedisObj<OBJ extends Serializable> extends _RedisObject {
	
	/** 此对象的默认键名 */
	private final static String DEFAULT_OBJ_NAME = "REDIS_OBJ";
	
	/** 此对象在redis中的键名（需确保不为空） */
	private final String OBJ_NAME;
	
	/**
	 * 构造函数
	 * @param objName 此对象在redis中的键名（需确保不为空）
	 * @param redis redis客户端连接对象（需确保可用）
	 */
	public RedisObj(String objName, RedisClient redis) {
		super(redis);
		this.OBJ_NAME = (objName == null ? DEFAULT_OBJ_NAME : objName);
	}
	
	/**
	 * 实时在redis缓存中检查此对象是否存在值
	 * @return true:存在; false:不存在
	 */
	public boolean exists() {
		boolean isExist = false;
		try {
			isExist = redis.existKey(OBJ_NAME);
			
		} catch(Exception e) {
			log.error("查询redis缓存失败", e);
		}
		return isExist;
	}
	
	/**
	 * 实时在redis缓存中设置此对象的值
	 * @param value 设置的值
	 * @return true:成功; false:失败
	 */
	public boolean set(OBJ value) {
		boolean isOk = false;
		if(value == null) {
			return isOk;
		}
		
		try {
			if(typeIsStr() || value instanceof String) {
				setTypeStr();
				isOk = redis.addStrVal(OBJ_NAME, (String) value);
				
			} else {
				setTypeObj();
				isOk = redis.addSerialObj(OBJ_NAME, value);
			}
		} catch(Exception e) {
			log.error("写入redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 实时在redis缓存中在此对象的值后附加新的字符串值。
	 * 此方法当且仅当使用String作为实例化泛型（即 RedisObj&lt;String&gt;）时有效。
	 * </pre>
	 * @param value 附加的字符串值
	 * @return true:成功; false:失败
	 */
	public boolean append(String value) {
		boolean isOk = false;
		if(value == null || !typeIsStr()) {
			return isOk;
		}
		
		try {
			isOk = redis.appendStrVal(OBJ_NAME, value) >= 0;
			
		} catch(Exception e) {
			log.error("写入redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中查询此对象的值
	 * @return 若无值则返回null
	 */
	@SuppressWarnings("unchecked")
	public OBJ get() {
		OBJ value = null;
		if(!exists()) {
			return value;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				String str = redis.getStrVal(OBJ_NAME);
				if(str != null) {
					value = (OBJ) str;
				}
				
			} else if(typeIsObj()) {
				Object obj = redis.getSerialObj(OBJ_NAME);
				if(obj != null) {
					value = (OBJ) obj;
				}
			}
		} catch(Exception e) {
			log.error("读取redis缓存失败", e);
		}
		return value;
	}
	
	/**
	 * 实时在redis缓存中删除此对象
	 * @return true:删除成功; false:删除失败
	 */
	public boolean clear() {
		boolean isOk = false;
		try {
			isOk = redis.delKeys(OBJ_NAME) >= 0;
			
		} catch(Exception e) {
			log.error("删除redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 校准类型（此方法通过直接取出值进行判断，因此只能有值时执行）.
	 * 由于未必存在值，因此此方法未必能一次校准.
	 * </pre>
	 */
	private void alignType() {
		if(!typeIsNone()) {
			return;
		}
		
		try {
			Object obj = redis.getSerialObj(OBJ_NAME);
			if(obj != null) {
				if(obj instanceof String) {
					setTypeStr();
					
				} else {
					setTypeObj();
				}
			} else {
				String str = redis.getStrVal(OBJ_NAME);
				if(str != null) {
					setTypeStr();
				}
			}
		} catch(Exception ex) {
			log.error("读取redis缓存失败", ex);
		}
	}

}
