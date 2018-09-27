package exp.libs.warp.db.redis.bean;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPoolConfig;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.db.sql.bean.DataSourceBean;

/**
 * <PRE>
 * Redis配置对象.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RedisBean {
	
	/** 配置对象的默认ID */
	protected final static String DEFAULT_ID = "DEFAULT_REDIS";
	
	private String id;
	
	/**
     * <pre>
     * Redis具有单机/主从/哨兵/集群四种模式。
     * 其中 单机/主从/哨兵 与 集群  是使用不同的连接方式的，
     * 	前三者只需要连接到作为主机的机器（从机仅用于备份/切换，哨兵机仅用于监控，均无需连接）；
     *  后者则需要连接到集群中所有节点。
     * </pre>
     */
    protected final static boolean DEFAULT_CLUSTER = false;
    
	private boolean cluster;
	
	/** 默认的Redis socket */
    protected final static String DEFAULT_SOCKET = "127.0.0.1:6379";
    
	private Set<String> sockets;
	
	 /** 默认的访问密码 */
    protected final static String DEFAULT_AUTH = "";
    
	private String password;
	
	/** 默认连接超时时间(ms) */
    protected final static int DEFAULT_TIMEOUT = 2000;
    
	private int timeout;
	
	/**
     * <pre>
     * 可用连接Jedis实例的最大数目（即可同时存在的最大连接数）。
     * JedisPool的默认值为8； 若为-1，则表示不限制。
     * 
     * 若连接池已分配了最大的jedis实例，则此时连接池的状态为exhausted(耗尽)
     * </pre>
     */
    protected final static int DEFAULT_MAX_TOTAL = 100;
    
	private int maxTotal;
	
	/**
     * <pre>
     * 连接池中可同时存在最大的Jedis空闲实例（即空闲连接数）。
     * JedisPool的默认值为8。
     * </pre>
     */
    protected final static int DEFAULT_MAX_IDLE = 8;
    
	private int maxIdle;
	
	/**
     * <pre>
     * 当连接池已满时，等待可用连接的最大时间（单位:ms）。
     * JedisPool的默认值为-1，表示永不超时。
     * 如果超过等待时间，则直接抛出JedisConnectionException
     * </pre>
     */
    protected final static long DEFAULT_MAX_WAIT = -1;
    
	private long maxWaitMillis;
	
	/**
     * <pre>
     * 在borrow一个jedis实例时，是否提前进行validate操作。
     * 如果为true，则得到的jedis实例均是可用的。
     * </pre>
     */
    protected final static boolean DEFAULT_TEST_ON_BORROW = true;
    
	private boolean testOnBorrow;
	
	public RedisBean() {
		setId(DEFAULT_ID);
		setCluster(DEFAULT_CLUSTER);
		setPassword(DEFAULT_AUTH);
		setTimeout(DEFAULT_TIMEOUT);
		setMaxTotal(DEFAULT_MAX_TOTAL);
		setMaxIdle(DEFAULT_MAX_IDLE);
		setMaxWaitMillis(DEFAULT_MAX_WAIT);
		setTestOnBorrow(DEFAULT_TEST_ON_BORROW);
		
		this.sockets = new HashSet<String>();
	}

	public RedisBean(DataSourceBean ds) {
		this();
		if(ds != null) {
			setId(ds.getId());
			setCluster(false);
			addSockets(StrUtils.concat(ds.getIp(), ":", ds.getPort()));
			setPassword(ds.getPassword());
			setTimeout((int) ds.getMaximumActiveTime());
			setMaxTotal(ds.getMaximumConnectionCount());
			setMaxIdle(ds.getPrototypeCount());
			setMaxWaitMillis(ds.getMaximumConnectionLifetime());
			setTestOnBorrow(ds.isTestBeforeUse());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = (StrUtils.isNotEmpty(id) ? id :
			(StrUtils.isNotEmpty(this.id) ? this.id : DEFAULT_ID));
	}

	public boolean isCluster() {
		return cluster;
	}

	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}

	public Set<String> getSockets() {
		return sockets;
	}

	public void addSockets(String... sockets) {
		if(sockets != null) {
			for(String socket : sockets) {
				if(StrUtils.isTrimEmpty(socket)) {
					continue;
				}
				
				socket = StrUtils.trimAll(socket);
				String[] socks = socket.split(",|，");
				for(String sock : socks) {
					this.sockets.add(sock);
				}
			}
		}
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = (StrUtils.isNotEmpty(password) ? password :
			(StrUtils.isNotEmpty(this.password) ? this.password : DEFAULT_AUTH));
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = (timeout >= 0 ? timeout :
			(this.timeout >= 0 ? this.timeout : DEFAULT_TIMEOUT));
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = (maxTotal >= 0 ? maxTotal :
			(this.maxTotal >= 0 ? this.maxTotal : DEFAULT_MAX_TOTAL));
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
		this.maxIdle = (maxIdle >= 0 ? maxIdle :
			(this.maxIdle >= 0 ? this.maxIdle : DEFAULT_MAX_IDLE));
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
		if(this.maxWaitMillis < 0) {
			this.maxWaitMillis = DEFAULT_MAX_WAIT;
		}
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n++++++++++++++++ Redis ++++++++++++++++\r\n");
		sb.append("id : ").append(getId()).append("\r\n");
		sb.append("cluster : ").append(isCluster()).append("\r\n");
		sb.append("sockets : ").append(getSockets()).append("\r\n");
		sb.append("password : ").append(getPassword()).append("\r\n");
		sb.append("timeout : ").append(getTimeout()).append("\r\n");
		sb.append("maxTotal : ").append(getMaxTotal()).append("\r\n");
		sb.append("maxIdle : ").append(getMaxIdle()).append("\r\n");
		sb.append("maxWaitMillis : ").append(getMaxWaitMillis()).append("\r\n");
		sb.append("testOnBorrow : ").append(isTestOnBorrow()).append("\r\n");
		sb.append("-------------------------------------------\r\n");
		return sb.toString();
	}
	
	/**
	 * 生成连接池配置
	 * @return Redis连接池配置
	 */
	public GenericObjectPoolConfig toPoolConfig() {
		GenericObjectPoolConfig poolConfig = isCluster() ? 
				new GenericObjectPoolConfig() : new JedisPoolConfig();
		poolConfig.setMaxTotal(getMaxTotal());
		poolConfig.setMaxIdle(getMaxIdle());
		poolConfig.setMaxWaitMillis(getMaxWaitMillis());
		poolConfig.setTestOnBorrow(isTestOnBorrow());
		return poolConfig;
	}
	
	/**
	 * 克隆Redis配置
	 */
	public RedisBean clone() {
		
		// 部分get/set方法含有校验逻辑，若通过setXX(getXX)可能会因为重复校验导致数据异常
		// 因此此处采用直接赋值方式进行克隆
		
		RedisBean _clone = new RedisBean();
		_clone.id = this.id;
		_clone.cluster = this.cluster;
		_clone.sockets = new HashSet<String>(this.sockets);
		_clone.password = this.password;
		_clone.timeout = this.timeout;
		_clone.maxTotal = this.maxTotal;
		_clone.maxIdle = this.maxIdle;
		_clone.maxWaitMillis = this.maxWaitMillis;
		_clone.testOnBorrow = this.testOnBorrow;
		return _clone;
	}
	
}
