package exp.libs.warp.db.redis.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import exp.libs.utils.other.ListUtils;
import exp.libs.warp.db.redis.RedisClient;

/**
 * <PRE>
 * Redis-Set对象.
 * 模仿HashSet的使用习惯进行封装.
 * ------------------------------
 * 
 * 使用样例:
 * 
 * final String SET_IN_REDIS_KEY = "set在Redis中的键名（自定义且需唯一）";
 * {@link #RedisClient} redis = new RedisClient("127.0.0.1", 6379);	// redis连接客户端（支持单机/集群）
 * 
 * RedisSet&lt;自定义对象&gt; set = new RedisSet&lt;自定义对象&gt;(SET_IN_REDIS_KEY, redis);
 * set.add(element);
 * set.addAll(elements);
 * set.contains(element);
 * set.isEmpty();
 * set.size();
 * set.getAll();
 * set.remove(element);
 * set.clear();
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
public class RedisSet<E extends Serializable> extends _RedisObject {

	/** 此集合的默认键名 */
	private final static String DEFAULT_SET_NAME = "REDIS_SET";
	
	/** 此集合在redis中的键名（需确保不为空） */
	private final String SET_NAME;
	
	/**
	 * 构造函数
	 * @param setName 此集合在redis中的键名（需确保不为空）
	 * @param redis redis客户端连接对象（需确保可用）
	 */
	public RedisSet(String setName, RedisClient redis) {
		super(redis);
		this.SET_NAME = (setName == null ? DEFAULT_SET_NAME : setName);
	}
	
	/**
	 * 实时在redis缓存中检查此集合是否包含某个元素
	 * @param e 被检查的元素
	 * @return true:包含; false:不包含
	 */
	public boolean contains(E e) {
		boolean isExist = false;
		if(isEmpty() || e == null) {
			return isExist;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				isExist = redis.existInSet(SET_NAME, (String) e);
				
			} else {
				isExist = redis.existInSet(SET_NAME, e);
			}
		} catch(Exception ex) {
			log.error("查询redis缓存失败", ex);
		}
		return isExist;
	}
	
	/**
	 * 实时在redis缓存中检查此集合是否为空
	 * @return true:为空; false:非空
	 */
	public boolean isEmpty() {
		return size() <= 0;
	}
	
	/**
	 * 实时在redis缓存中查询此集合的大小
	 * @return 集合的大小
	 */
	public long size() {
		long size = 0L;
		try {
			size = redis.getSetSize(SET_NAME);
			
		} catch(Exception e) {
			log.error("查询redis缓存失败", e);
		}
		return size;
	}
	
	/**
	 * 实时在redis缓存中向此集合添加一个元素
	 * @param e 新元素
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(E e) {
		boolean isOk = false;
		if(e == null) {
			return isOk;
		}
		
		try {
			if(typeIsStr() || e instanceof String) {
				setTypeStr();
				isOk = redis.addStrValsToSet(SET_NAME, (String) e) > 0;
				
			} else {
				setTypeObj();
				isOk = redis.addSerialObjsToSet(SET_NAME, e) > 0;
			}
		} catch(Exception ex) {
			log.error("写入redis缓存失败", ex);
		}
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中向此集合添加若干个元素
	 * @param es 新元素集
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addAll(E... es) {
		boolean isOk = false;
		if(es == null) {
			return isOk;
		}
		
		redis.closeAutoCommit();
		isOk = true;
		for(E e : es) {
			isOk &= add(e);
		}
		redis.commit();
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中向此集合添加若干个元素
	 * @param es 新元素集
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addAll(Set<E> es) {
		boolean isOk = false;
		if(es == null) {
			return isOk;
		}
		
		redis.closeAutoCommit();
		isOk = true;
		for(E e : es) {
			isOk &= add(e);
		}
		redis.commit();
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中随机取出集合中的一个元素
	 * @return 若集合为空则返回null
	 */
	@SuppressWarnings("unchecked")
	public E getRandom() {
		E e = null;
		if(isEmpty()) {
			return e;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				String str = redis.getRandomStrValInSet(SET_NAME);
				if(str != null) {
					e = (E) str;
				}
				
			} else {
				Object obj = redis.getRandomSerialObjInSet(SET_NAME);
				if(obj != null) {
					e = (E) obj;
				}
			}
		} catch(Exception ex) {
			log.error("读取redis缓存失败", ex);
		}
		return e;
	}

	/**
	 * 实时在redis缓存中查询此集合的所有元素
	 * @return 所有元素集（即使为空也只会返回空表，不会返回null）
	 */
	@SuppressWarnings("unchecked")
	public Set<E> getAll() {
		Set<E> set = new HashSet<E>();
		if(isEmpty()) {
			return set;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				Set<String> sSet = redis.getStrSet(SET_NAME);
				if(ListUtils.isNotEmpty(sSet)) {
					for(String s : sSet) {
						if(s == null) {
							continue;
						}
						set.add((E) s);
					}
				}
				
			} else {
				Set<Object> oSet = redis.getSerialSet(SET_NAME);
				if(ListUtils.isNotEmpty(oSet)) {
					for(Object o : oSet) {
						if(o == null) {
							continue;
						}
						set.add((E) o);
					}
				}
			}
		} catch(Exception e) {
			log.error("读取redis缓存失败", e);
		}
		return set;
	}
	
	/**
	 * 实时在redis缓存中删除此集合的若干个元素
	 * @param es 被删除的元素集
	 * @return 已成功删除的数量
	 */
	public long remove(E... es) {
		long num = 0L;
		if(isEmpty() || ListUtils.isEmpty(es)) {
			return num;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				for(E e : es) {
					if(e == null) {
						continue;
					}
					num += redis.delStrValsInSet(SET_NAME, (String) e);
				}
				
			} else {
				for(E e : es) {
					if(e == null) {
						continue;
					}
					num += redis.delSerialObjsInSet(SET_NAME, e);
				}
			}
		} catch(Exception ex) {
			log.error("删除redis缓存失败", ex);
		}
		return num;
	}
	
	/**
	 * 实时在redis缓存中删除此集合
	 * @return true:删除成功; false:删除失败
	 */
	public boolean clear() {
		boolean isOk = false;
		try {
			isOk = redis.delKeys(SET_NAME) >= 0;
			
		} catch(Exception e) {
			log.error("删除redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * 校准类型（此方法通过随机取集合中的一个元素进行判断，因此只能在集合非空时执行）
	 */
	private void alignType() {
		if(!typeIsNone()) {
			return;
		}
		
		try {
			Object obj = redis.getRandomSerialObjInSet(SET_NAME);
			if(obj != null) {
				if(obj instanceof String) {
					setTypeStr();
					
				} else {
					setTypeObj();
				}
			} else {
				String str = redis.getRandomStrValInSet(SET_NAME);
				if(str != null) {
					setTypeStr();
				}
			}
		} catch(Exception ex) {
			log.error("读取redis缓存失败", ex);
		}
	}
	
}
