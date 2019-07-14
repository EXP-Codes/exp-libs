package exp.libs.warp.db.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <PRE>
 * Redis操作接口.
 * 
 *  此接口是基于Jedis提供的 String接口 与 byte[]接口 进行二次封装的.
 *  其中String类型原样调用, byte[]则封装为序列化对象接口.
 *  
 *  特别地, Jedis的byte[]是跟编码相关的.
 *   即对于同一个键值, 在没有声明编码的时候, 使用 String接口 与 byte[]接口 得到的字节码可能不一致.
 *   为了确保屏蔽两个接口的差异, 此接口将所有字节码统一编码成utf-8后再调用Jedis接口.
 *   
 *  需注意的是，对于同一个键，要么只能写入 String类型 对象，要么只能写入 byte[]类型对象.
 *  	否则，虽然同时写入两种类型不会报错（实际上都是作为byte[]存储到Redis），
 *  	但是，读取的时候因为无法识别 byte[]数据是由String编码得到的，还是序列化得到的，导致解析异常。
 *  
 *  而此接口之所以不把String也进行序列化，而是区分对待，主要是考虑到跨平台多程序使用redis时序列化对象的还原问题。
 *  另一个就是序列化带来的效率下降问题。
 *  
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
interface _IJedis {
	
////////////////////////////////////////////////////////////////////////////////
// ↓↓↓↓↓  公共接口  ↓↓↓↓↓
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 测试Redis连接是否有效(集群模式不支持此操作)
	 * @return true:连接成功; false:连接失败
	 */
	public boolean isVaild();
	
	/**
	 * <pre>
	 * 设置是否自动提交.
	 * 在使用连接池的情况下, redis的操作默认均为短连接.
	 * 此方法可临时切换操作模式为长连接/短连接.
	 * 在调用{@link #commit}方法后恢复为短连接模式.
	 * -----------------
	 * 此方法非多线程安全，集群模式不支持此操作.
	 * </pre>
	 * @param autoCommit true:自动提交; false:手动提交(需调用{@link #commit}方法)
	 */
	public void setAutoCommit(boolean autoCommit);
	
	/**
	 * <pre>
	 * 在使用连接池的情况下, redis的操作默认均为短连接.
	 * 此方法可临时切换操作模式为长连接, 在调用{@link #commit}方法后恢复为短连接模式.
	 * -----------------
	 * 此方法非多线程安全，集群模式不支持此操作.
	 * </pre>
	 */
	public void closeAutoCommit();
	
	/**
	 * <pre>
	 * 把redis操作模式切换为默认的短连接模式.
	 * -----------------
	 * 此方法非多线程安全，集群模式不支持此操作.
	 * </pre>
	 */
	public void commit();
	
	/**
	 * 断开Redis连接
	 */
	public void destory();
	
	/**
	 * 清空Redis库中所有数据(集群模式不支持此操作)
	 * @return true:清空成功; false:清空失败
	 */
	public boolean clearAll();
	
	/**
	 * 检查Redis库里面是否存在某个键值
	 * @param redisKey 所操作对象在redis中的键
	 * @return true:存在; false:不存在
	 */
	public boolean existKey(String redisKey);
	
	/**
	 * 删除Redis中的若干个键（及其对应的内容）
	 * @param redisKeys 所操作对象在redis中的键集
	 * @return 删除成功的个数
	 */
	public long delKeys(String... redisKeys);
	
////////////////////////////////////////////////////////////////////////////////
// ↓↓↓↓↓  单值/对象相关接口  ↓↓↓↓↓
////////////////////////////////////////////////////////////////////////////////

	/**
	 * 新增一个键值对（若已存在则覆盖）
	 * @param redisKey 所操作对象在redis中的键
	 * @param value 新的值
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addStrVal(String redisKey, String value);
	
	/**
	 * <pre>
	 * 在已有的键的原值的末尾附加值（若键不存在则新增）.
	 * 配套{@link #addStrVal}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param value 附加的值
	 * @return 附加值后，该键上最新的值的总长度(若附加失败返回-1)
	 */
	public long appendStrVal(String redisKey, String value);
	
	/**
	 * <pre>
	 * 获取指定键的值.
	 * 配套{@link #addStrVal}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 对应的值（若不存在键则返回null）
	 */
	public String getStrVal(String redisKey);
	
	/**
	 * <pre>
	 * 新增一个序列化对象（该对象须实现Serializable接口）.
	 * 该对象会以序列化形式存储到Redis.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param object 新增的对象（须实现Serializable接口）
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addSerialObj(String redisKey, Serializable object);
	
	/**
	 * <pre>
	 * 获取指定键的序列化对象.
	 * 该对象会从Redis反序列化.
	 * 配套{@link #addSerialObj}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 反序列化的对象（若失败则返回null）
	 */
	public Object getSerialObj(String redisKey);
	
////////////////////////////////////////////////////////////////////////////////
//↓↓↓↓↓  Map相关接口  ↓↓↓↓↓
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <pre>
	 * 新增一个 Map&lt;String, String&gt;哈希表.
	 * 	若已存在同键的哈希表，则两个哈希表合并, 表内同键的值会被后者覆盖.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param map Map&lt;String, String&gt;哈希表
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addStrMap(String redisKey, Map<String, String> map);
	
	/**
	 * <pre>
	 * 新增一个 键值对 到 Map&lt;String, String&gt;哈希表.
	 * 若该哈希表不存在则创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param key 新增到哈希表内的键
	 * @param value 新增到哈希表内的值
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addStrValToMap(String redisKey, String key, String value);
	
	/**
	 * <pre>
	 * 取出一个  Map&lt;String, String&gt;哈希表.
	 * 配套{@link #addStrMap}或{@link #addStrValToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Map&lt;String, String&gt;哈希表（若不存在则返回空表，不会返回null）
	 */
	public Map<String, String> getStrMap(String redisKey);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, String&gt;哈希表中的某个键的值
	 * 配套{@link #addStrMap}或{@link #addStrValToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param key 哈希表中的某个键
	 * @return 哈希表中对应的值（若不存在返回null）
	 */
	public String getStrValInMap(String redisKey, String key);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, String&gt;哈希表中的若干个键的值
	 * 配套{@link #addStrMap}或{@link #addStrValToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param keys 哈希表中的一些键
	 * @return 哈希表中对应的一些值列表（列表不会为null， 内部值顺序对应入参的键顺序, 若该键对应值不存在, 则该位置为null）
	 */
	public List<String> getStrValsInMap(String redisKey, String... keys);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, String&gt;哈希表中的所有值.
	 * 配套{@link #addStrMap}或{@link #addStrValToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 若不存在该哈希表或哈希表为空，则返回空列表（不会返回null）
	 */
	public List<String> getAllStrValsInMap(String redisKey);
	
	/**
	 * <pre>
	 * 新增一个 Map&lt;String, Serializable&gt;哈希表.
	 * 	若已存在同键的哈希表，则两个哈希表合并, 表内同键的值会被后者覆盖.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param map 哈希表（其值须实现Serializable接口）
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addSerialMap(String redisKey, Map<String, Serializable> map);
	
	/**
	 * <pre>
	 * 新增一个 键值对像 到  Map&lt;String, Serializable&gt;哈希表.
	 * 若该哈希表不存在则创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param key 新增到哈希表内的键
	 * @param object 新增到哈希表内的值对象（须实现Serializable接口）
	 * @return true:新增成功; false:新增失败
	 */
	public boolean addSerialObjToMap(String redisKey, String key, Serializable object);
	
	/**
	 * <pre>
	 * 取出一个  Map&lt;String, Serializable&gt;哈希表（其值为反序列化对象）.
	 * 配套{@link #addSerialMap}或{@link #addSerialObjToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Map&lt;String, Object&gt;哈希表（若不存在则返回空表，不会返回null）
	 */
	public Map<String, Object> getSerialMap(String redisKey);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, Serializable&gt;哈希表中的某个键的值对象（反序列化对象）
	 * 配套{@link #addSerialMap}或{@link #addSerialObjToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param keys 哈希表中的某个键
	 * @return 哈希表中对应的值对象（反序列化对象，若不存在返回null）
	 */
	public Object getSerialObjInMap(String redisKey, String key);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, Serializable&gt;哈希表中的若干个键的值对象（反序列化对象）
	 * 配套{@link #addSerialMap}或{@link #addSerialObjToMap}使用.
	 * </pre>
	 * @param redisKey 哈希表的键
	 * @param keys 哈希表中的一些键
	 * @return 哈希表中对应的一些值对象列表（列表不会为null， 内部值顺序对应入参的键顺序, 若该键对应值对象不存在, 则该位置为null）
	 */
	public List<Object> getSerialObjsInMap(String redisKey, String... keys);
	
	/**
	 * <pre>
	 * 获取某个Map&lt;String, Serializable&gt;哈希表中的所有值.
	 * 配套{@link #addSerialMap}或{@link #addSerialObjToMap}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 若不存在该哈希表或哈希表为空，则返回空列表（不会返回null）
	 */
	public List<Object> getAllSerialObjsInMap(String redisKey);
	
	/**
	 * 检查某个哈希表中是否存在某个键
	 * @param redisKey 所操作对象在redis中的键
	 * @param key 哈希表中的某个键
	 * @return true:存在; false:不存在
	 */
	public boolean existKeyInMap(String redisKey, String key);
	
	/**
	 * 获取哈希表中所有键
	 * @param redisKey 所操作对象在redis中的键
	 * @return 若不存在该哈希表或哈希表为空，则返回空集（不会返回null）
	 */
	public Set<String> getAllKeysInMap(String redisKey);
	
	/**
	 * 删除某个哈希表中的若干个键（及其对应的值）
	 * @param redisKey 所操作对象在redis中的键
	 * @param keys 哈希表中的一些键
	 * @return 删除成功的个数
	 */
	public long delKeysInMap(String redisKey, String... keys);
	
	/**
	 * 获取哈希表的大小
	 * @param redisKey 所操作对象在redis中的键
	 * @return 哈希表的大小
	 */
	public long getMapSize(String redisKey);
	
////////////////////////////////////////////////////////////////////////////////
//↓↓↓↓↓  List相关接口  ↓↓↓↓↓
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <pre>
	 * 新增一个 List&lt;String&gt;列表.
	 * 	若已存在同键列表，则两个列表合并.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param list List&lt;String&gt;列表
	 * @return 该列表的总长度
	 */
	public long addStrList(String redisKey, List<String> list);
	
	/**
	 * <pre>
	 * 添加一些值到 List&lt;String&gt;列表.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param values 添加的值
	 * @return 添加后，该列表的总长度
	 */
	public long addStrValsToList(String redisKey, String... values);
	
	/**
	 * <pre>
	 * 添加一些值到 List&lt;String&gt;列表 头部.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param values 添加的值
	 * @return 添加后，该列表的总长度
	 */
	public long addStrValsToListHead(String redisKey, String... values);
	
	/**
	 * <pre>
	 * 添加一些值到 List&lt;String&gt;列表 尾部.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param values 添加的值列表
	 * @return 添加后，该列表的总长度
	 */
	public long addStrValsToListTail(String redisKey, String... values);
	
	/**
	 * <pre>
	 * 获取整个 List&lt;String&gt;列表（即返回其中的所有值）.
	 * 配套{@link #addStrList}或{@link #addStrValsToList}或{@link #addStrValsToListHead}或{@link #addStrValsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return List&lt;String&gt;列表（若不存在则返回空表，不会返回null）
	 */
	public List<String> getStrList(String redisKey);
	
	/**
	 * <pre>
	 * 获取 List&lt;String&gt;列表 中指定位置的值 .
	 * 配套{@link #addStrList}或{@link #addStrValsToList}或{@link #addStrValsToListHead}或{@link #addStrValsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param index 列表中的索引位置（第一个元素为0, 最后一个元素为-1, 正数索引为从前向后数, 负数索引为从后向前数）
	 * @return 索引对应值（若列表为空则返回null）
	 */
	public String getStrValInList(String redisKey, long index);
	
	/**
	 * <pre>
	 * 获取整个 List&lt;String&gt;列表中的所有值.
	 * 配套{@link #addStrList}或{@link #addStrValsToList}或{@link #addStrValsToListHead}或{@link #addStrValsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return List&lt;String&gt;列表（若不存在则返回空表，不会返回null）
	 */
	public List<String> getAllStrValsInList(String redisKey);
	
	/**
	 * <pre>
	 * 删除 List&lt;String&gt;列表中, 值与期望值一致的所有元素.
	 * 配套{@link #addStrList}或{@link #addStrValsToList}或{@link #addStrValsToListHead}或{@link #addStrValsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param value 期望被删除的值
	 * @return 成功从改集合中删除的值个数
	 */
	public long delStrValsInList(String redisKey, String value);
	
	/**
	 * <pre>
	 * 删除 List&lt;String&gt;列表中，从前往后数的前N个值与期望值一致的所有元素.
	 * 配套{@link #addStrList}或{@link #addStrValsToList}或{@link #addStrValsToListHead}或{@link #addStrValsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param value 期望被删除的值
	 * @param count 期望被删除的个数（正数表示从前往后数，负数表示从后往前数）
	 * @return 成功从改集合中删除的值个数
	 */
	public long delStrValsInList(String redisKey, String value, long count);
	
	/**
	 * <pre>
	 * 新增一个 List&lt;Serializable&gt;列表.
	 * 	若已存在同键列表，则两个列表合并.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param list List&lt;Serializable&gt;列表
	 * @return 该列表的总长度
	 */
	public long addSerialList(String redisKey, List<Serializable> list);
	
	/**
	 * <pre>
	 * 添加一些序列化对象到 List&lt;Serializable&gt;列表.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param objects 添加的序列化对象（需实现Serializable接口）
	 * @return 添加后，该列表的总长度
	 */
	public long addSerialObjsToList(String redisKey, Serializable... objects);
	
	/**
	 * <pre>
	 * 添加一些序列化对象到 List&lt;Serializable&gt;列表 头部.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param objects 添加的序列化对象（需实现Serializable接口）
	 * @return 添加后，该列表的总长度
	 */
	public long addSerialObjsToListHead(String redisKey, Serializable... objects);
	
	/**
	 * <pre>
	 * 添加一些序列化对象到 List&lt;Serializable&gt;列表 尾部.
	 * 若列表不存在则自动创建.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param objects 添加的序列化对象（需实现Serializable接口）
	 * @return 添加后，该列表的总长度
	 */
	public long addSerialObjsToListTail(String redisKey, Serializable... objects);
	
	/**
	 * <pre>
	 * 删除 List&lt;Serializable&gt;列表中, 值与期望值一致的所有元素.
	 * 配套{@link #addSerialList}或{@link #addSerialObjsToList}或{@link #addSerialObjsToListHead}或{@link #addSerialObjsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param object 期望被删除的值（需实现Serializable接口）
	 * @return 成功从改集合中删除的值个数
	 */
	public long delSerialObjsInList(String redisKey, Serializable object);
	
	/**
	 * <pre>
	 * 删除 List&lt;Serializable&gt;列表中，从前往后数的前N个值与期望值一致的所有元素.
	 * 配套{@link #addSerialList}或{@link #addSerialObjsToList}或{@link #addSerialObjsToListHead}或{@link #addSerialObjsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param object 期望被删除的值（需实现Serializable接口）
	 * @param count 期望被删除的个数（正数表示从前往后数，负数表示从后往前数）
	 * @return 成功从改集合中删除的值个数
	 */
	public long delSerialObjsInList(String redisKey, Serializable object, long count);
	
	/**
	 * <pre>
	 * 获取整个 List&lt;Serializable&gt;列表（即返回其中的所有值）.
	 * 配套{@link #addSerialList}或{@link #addSerialObjsToList}或{@link #addSerialObjsToListHead}或{@link #addSerialObjsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return List&lt;Object&gt;列表（若不存在则返回空表，不会返回null）
	 */
	public List<Object> getSerialList(String redisKey);
	
	/**
	 * <pre>
	 * 获取 List&lt;Serializable&gt;列表 中指定位置的值 .
	 * 配套{@link #addSerialList}或{@link #addSerialObjsToList}或{@link #addSerialObjsToListHead}或{@link #addSerialObjsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param index 列表中的索引位置（第一个元素为0, 最后一个元素为-1, 正数索引为从前向后数, 负数索引为从后向前数）
	 * @return 索引对应值（若列表为空则返回null）
	 */
	public Object getSerialObjInList(String redisKey, long index);
	
	/**
	 * <pre>
	 * 获取整个 List&lt;Serializable&gt;列表中的所有值.
	 * 配套{@link #addSerialList}或{@link #addSerialObjsToList}或{@link #addSerialObjsToListHead}或{@link #addSerialObjsToListTail}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return List&lt;Object&gt;列表（若不存在则返回空表，不会返回null）
	 */
	public List<Object> getAllSerialObjsInList(String redisKey);
	
	/**
	 * 获取列表的大小
	 * @param redisKey 所操作对象在redis中的键
	 * @return 列表的大小
	 */
	public long getListSize(String redisKey);
	
////////////////////////////////////////////////////////////////////////////////
//↓↓↓↓↓  Set相关接口  ↓↓↓↓↓
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <pre>
	 * 新增一个 Set&lt;String&gt;集合.
	 * 	若已存在同键列表，则两个集合合并, 表内同键的值会被后者覆盖.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param set Set&lt;String&gt;列表
	 * @return 成功添加到该集合的值个数
	 */
	public long addStrSet(String redisKey, Set<String> set);
	
	/**
	 * 添加一些值到 Set&lt;String&gt;集合.
	 * @param redisKey 所操作对象在redis中的键
	 * @param values 添加的值
	 * @return 成功添加到该集合的值个数
	 */
	public long addStrValsToSet(String redisKey, String... values);
	
	/**
	 * <pre>
	 * 获取整个 Set&lt;String&gt;集合（即返回其中的所有值）.
	 * 配套{@link #addStrSet}或{@link #addStrValsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Set&lt;String&gt;集合（若不存在则返回空集，不会返回null）
	 */
	public Set<String> getStrSet(String redisKey);
	
	/**
	 * <pre>
	 * 获取 Set&lt;String&gt;集合中一个随机元素.
	 * 配套{@link #addStrSet}或{@link #addStrValsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 若集合为空则返回null
	 */
	public String getRandomStrValInSet(String redisKey);
	
	/**
	 * <pre>
	 * 获取 Set&lt;String&gt;集合中的所有值.
	 * 配套{@link #addStrSet}或{@link #addStrValsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Set&lt;String&gt;集合（若不存在则返回空集，不会返回null）
	 */
	public Set<String> getAllStrValsInSet(String redisKey);
	
	/**
	 * <pre>
	 * 删除 Set&lt;String&gt;集合中指定的值.
	 * 配套{@link #addStrSet}或{@link #addStrValsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param values 期望被删除的值
	 * @return 成功从该集合中删除的值个数
	 */
	public long delStrValsInSet(String redisKey, String... values);
	
	/**
	 * <pre>
	 * 检测某个值是否在指定的 Set&lt;String&gt;集合中.
	 * 配套{@link #addStrSet}或{@link #addStrValsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param value 被检测的值
	 * @return true:在集合中; false:不在集合中
	 */
	public boolean existInSet(String redisKey, String value);
	
	/**
	 * <pre>
	 * 新增一个 Set&lt;Serializable&gt;集合.
	 * 	若已存在同键列表，则两个集合合并, 表内同键的值会被后者覆盖.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param set Set&lt;Serializable&gt;列表
	 * @return 成功添加到该集合的值个数
	 */
	public long addSerialSet(String redisKey, Set<Serializable> set);
	
	/**
	 * 添加一些值到 Set&lt;Serializable&gt;集合.
	 * @param redisKey 所操作对象在redis中的键
	 * @param objects 添加的值对象（需实现Serializable接口）
	 * @return 成功添加到该集合的值个数
	 */
	public long addSerialObjsToSet(String redisKey, Serializable... objects);
	
	/**
	 * <pre>
	 * 获取整个 Set&lt;Serializable&gt;集合（即返回其中的所有值对象）.
	 * 配套{@link #addSerialSet}或{@link #addSerialObjsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Set&lt;Object&gt;集合（若不存在则返回空集，不会返回null）
	 */
	public Set<Object> getSerialSet(String redisKey);
	
	/**
	 * <pre>
	 * 获取 Set&lt;Serializable&gt;集合中一个随机对象（反序列化对象）.
	 * 配套{@link #addSerialSet}或{@link #addSerialObjsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return 若集合为空则返回null
	 */
	public Object getRandomSerialObjInSet(String redisKey);
	
	/**
	 * <pre>
	 * 获取 Set&lt;Serializable&gt;集合中的所对象（反序列化对象）.
	 * 配套{@link #addSerialSet}或{@link #addSerialObjsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @return Set&lt;Object&gt;集合（若不存在则返回空集，不会返回null）
	 */
	public Set<Object> getAllSerialObjsInSet(String redisKey);
	
	/**
	 * <pre>
	 * 删除 Set&lt;Serializable&gt;集合中指定的值对象.
	 * 配套{@link #addSerialSet}或{@link #addSerialObjsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param objects 期望被删除的值对象（需实现Serializable接口）
	 * @return 成功从该集合中删除的值个数
	 */
	public long delSerialObjsInSet(String redisKey, Serializable... objects);
	
	/**
	 * <pre>
	 * 检测某个值对象是否在指定的 Set&lt;Serializable&gt;集合中.
	 * 配套{@link #addSerialSet}或{@link #addSerialObjsToSet}使用.
	 * </pre>
	 * @param redisKey 所操作对象在redis中的键
	 * @param object 被检测的值对象（需实现Serializable接口）
	 * @return true:在集合中; false:不在集合中
	 */
	public boolean existInSet(String redisKey, Serializable object);
	
	/**
	 * 获取集合的大小
	 * @param redisKey 所操作对象在redis中的键
	 * @return 集合的大小
	 */
	public long getSetSize(String redisKey);
	
}
