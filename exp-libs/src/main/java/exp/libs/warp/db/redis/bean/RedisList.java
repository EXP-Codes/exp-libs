package exp.libs.warp.db.redis.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import exp.libs.utils.other.ListUtils;
import exp.libs.warp.db.redis.RedisClient;

/**
 * <PRE>
 * Redis-List对象.
 * 模仿LinkedList的使用习惯进行封装.
 * ------------------------------
 * 
 * 使用样例:
 * 
 * final String LIST_IN_REDIS_KEY = "list在Redis中的键名（自定义且需唯一）";
 * {@link #RedisClient} redis = new RedisClient("127.0.0.1", 6379);	// redis连接客户端（支持单机/集群）
 * 
 * RedisList&lt;自定义对象&gt; list = new RedisList&lt;自定义对象&gt;(LIST_IN_REDIS_KEY, redis);
 * list.add(element);
 * list.addAll(elements);
 * list.isEmpty();
 * list.size();
 * list.getAll();
 * list.remove(element);
 * list.clear();
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
public class RedisList<E extends Serializable> extends _RedisObject {

	/** 此列表的默认键名 */
	private final static String DEFAULT_LIST_NAME = "REDIS_LIST";
	
	/** 此列表在redis中的键名（需确保不为空） */
	private final String LIST_NAME;
	
	/**
	 * 构造函数
	 * @param listName 此列表在redis中的键名（需确保不为空）
	 * @param redis redis客户端连接对象（需确保可用）
	 */
	public RedisList(String listName, RedisClient redis) {
		super(redis);
		this.LIST_NAME = (listName == null ? DEFAULT_LIST_NAME : listName);
	}
	
	/**
	 * 实时在redis缓存中检查此列表是否为空
	 * @return true:为空; false:非空
	 */
	public boolean isEmpty() {
		return size() <= 0;
	}
	
	/**
	 * 实时在redis缓存中查询此列表的大小
	 * @return 列表的大小
	 */
	public long size() {
		long size = 0L;
		try {
			size = redis.getListSize(LIST_NAME);
			
		} catch(Exception e) {
			log.error("查询redis缓存失败", e);
		}
		return size;
	}
	
	/**
	 * 实时在redis缓存中向此列表添加一个元素（添加到尾部）
	 * @param e 新元素
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(E e) {
		return addToTail(e);
	}
	
	/**
	 * 实时在redis缓存中向此列表添加一个元素（添加到头部）
	 * @param e 新元素
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addToHead(E e) {
		boolean isOk = false;
		if(e == null) {
			return isOk;
		}
		
		try {
			if(typeIsStr() || e instanceof String) {
				setTypeStr();
				isOk = redis.addStrValsToListHead(LIST_NAME, (String) e) > 0;
				
			} else {
				setTypeObj();
				isOk = redis.addSerialObjsToListHead(LIST_NAME, e) > 0;
			}
		} catch(Exception ex) {
			log.error("写入redis缓存失败", ex);
		}
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中向此列表添加一个元素（添加到尾部）
	 * @param e 新元素
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addToTail(E e) {
		boolean isOk = false;
		if(e == null) {
			return isOk;
		}
		
		try {
			if(typeIsStr() || e instanceof String) {
				setTypeStr();
				isOk = redis.addStrValsToListTail(LIST_NAME, (String) e) > 0;
				
			} else {
				setTypeObj();
				isOk = redis.addSerialObjsToListTail(LIST_NAME, e) > 0;
			}
		} catch(Exception ex) {
			log.error("写入redis缓存失败", ex);
		}
		return isOk;
	}
	
	/**
	 * 实时在redis缓存中向此列表添加若干个元素（添加到尾部）
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
	 * 实时在redis缓存中向此列表添加若干个元素（添加到尾部）
	 * @param es 新元素集
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addAll(List<E> es) {
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
	 * 实时在redis缓存中查询此列表中的一个元素
	 * @param index 被查询的元素索引（第一个元素为0, 最后一个元素为-1, 正数索引为从前向后数, 负数索引为从后向前数）
	 * @return 对应的值（若不存在返回null）
	 */
	@SuppressWarnings("unchecked")
	public E get(long index) {
		E e = null;
		if(isEmpty()) {
			return e;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				String str = redis.getStrValInList(LIST_NAME, index);
				if(str != null) {
					e = (E) str;
				}
				
			} else {
				Object obj = redis.getSerialObjInList(LIST_NAME, index);
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
	 * 实时在redis缓存中查询此列表的所有元素
	 * @return 所有元素集（即使为空也只会返回空表，不会返回null）
	 */
	@SuppressWarnings("unchecked")
	public List<E> getAll() {
		List<E> list = new LinkedList<E>();
		if(isEmpty()) {
			return list;
		}
		alignType();
		
		try {
			if(typeIsStr()) {
				List<String> sList = redis.getStrList(LIST_NAME);
				if(ListUtils.isNotEmpty(sList)) {
					for(String s : sList) {
						if(s == null) {
							continue;
						}
						list.add((E) s);
					}
				}
				
			} else {
				List<Object> oList = redis.getSerialList(LIST_NAME);
				if(ListUtils.isNotEmpty(oList)) {
					for(Object o : oList) {
						if(o == null) {
							continue;
						}
						list.add((E) o);
					}
				}
			}
		} catch(Exception e) {
			log.error("读取redis缓存失败", e);
		}
		return list;
	}
	
	/**
	 * 实时在redis缓存中删除此列表的若干个元素
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
					num += redis.delStrValsInList(LIST_NAME, (String) e);
				}
				
			} else {
				for(E e : es) {
					if(e == null) {
						continue;
					}
					num += redis.delSerialObjsInList(LIST_NAME, e);
				}
			}
		} catch(Exception ex) {
			log.error("删除redis缓存失败", ex);
		}
		return num;
	}
	
	/**
	 * 实时在redis缓存中删除此列表
	 * @return true:删除成功; false:删除失败
	 */
	public boolean clear() {
		boolean isOk = false;
		try {
			isOk = redis.delKeys(LIST_NAME) >= 0;
			
		} catch(Exception e) {
			log.error("删除redis缓存失败", e);
		}
		return isOk;
	}
	
	/**
	 * 校准类型（此方法通过取队列第一个元素进行判断，因此只能在列表非空时执行）
	 */
	private void alignType() {
		if(!typeIsNone()) {
			return;
		}
		
		try {
			Object obj = redis.getSerialObjInList(LIST_NAME, 0);
			if(obj != null) {
				if(obj instanceof String) {
					setTypeStr();
					
				} else {
					setTypeObj();
				}
			} else {
				setTypeStr();
			}
		} catch(Exception ex) {
			log.error("读取redis缓存失败", ex);
		}
	}
	
}
