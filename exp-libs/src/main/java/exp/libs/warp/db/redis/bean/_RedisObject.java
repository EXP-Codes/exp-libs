package exp.libs.warp.db.redis.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.db.redis.RedisClient;

/**
 * <PRE>
 * Redis对象基类.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
abstract class _RedisObject {

	/** 日志器 */
	protected final static Logger log = LoggerFactory.getLogger(_RedisObject.class);
	
	/** 泛型类型枚举 */
	private final static int TYPE_NONE = 0, TYPE_STR = 1, TYPE_OBJ = 2;
	
	/** 当前的泛型类型 */
	private int type;
	
	/** Redis连接客户端对象 */
	protected RedisClient redis;
	
	/**
	 * 构造函数
	 * @param redis redis客户端连接对象（需确保可用）
	 */
	protected _RedisObject(RedisClient redis) {
		this.type = TYPE_NONE;
		this.redis = (redis == null ? new RedisClient() : redis);
	}
	
	protected boolean typeIsNone() {
		return type == TYPE_NONE;
	}
	
	protected boolean typeIsStr() {
		return type == TYPE_STR;
	}
	
	protected boolean typeIsObj() {
		return type == TYPE_OBJ;
	}
	
	protected void setTypeStr() {
		this.type = TYPE_STR;
	}
	
	protected void setTypeObj() {
		this.type = TYPE_OBJ;
	}
	
}
