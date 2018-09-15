package exp.libs.warp.net.mq.jms.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * JMSBean
 * </pre>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JmsBean {

	/** 配置文件读取记录 */
	public Set<String> record = new HashSet<String>();

	/**
	 * id
	 */
	protected String id;

	/**
	 * mqurl
	 */
	protected String url;

	/**
	 * 用户
	 */
	protected String user;

	/**
	 * 认证密码
	 */
	protected String password;

	/**
	 * 上下文工厂
	 */
	protected String contextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

	/**
	 * 连接工厂
	 */
	protected String connectionFactory = "ConnectionFactory";

	/**
	 * 是否会话事务，会话事务需要手动提交，保证多个消息的事务性
	 */
	protected boolean transcation = false;

	/**
	 * 是否是长连接，短连接则每次发送完后关闭连接，下次重新连接
	 */
	protected boolean longConnection = false;

	/**
	 * 1:NON_PERSISTENT:不要求将消息记录到稳定存储器中(非持久)
	 * 2:PERSISTENT：保证不能要求JMS提供商失败而造成的消息丢失（持久化）
	 */
	protected int deliveryMode = 1;

	/**
	 * 确认模式： 3:DUPS_OK_ACKNOWLEDGE：懒惰确认消息的传递，可能会造成消息的重复传递
	 * 1:AUTO_ACKNOWLEDGE:客户端自动确认
	 * 2:CLIENT_ACKNOWLEDGE:客户端通过调用消息的acknowledge方法来确认消息
	 */
	@Deprecated
	protected int iAcknowledgementMode = 1;

	/**
	 * 确认模式： 3:DUPS_OK_ACKNOWLEDGE：懒惰确认消息的传递，可能会造成消息的重复传递
	 * 1:AUTO_ACKNOWLEDGE:客户端自动确认
	 * 2:CLIENT_ACKNOWLEDGE:客户端通过调用消息的acknowledge方法来确认消息
	 */
	protected int acknowledgementMode = 1;

	/**
	 * 消息的生存时间,单位毫秒
	 */
	protected long timeToLive = 1800000;

	// /**
	// * 主题
	// */
	// protected String theme;

	/**
	 * 主题
	 */
	protected String getTheme;

	/**
	 * 主题
	 */
	protected String sendTheme;

	/**
	 * producer设置的相应主题
	 */
	protected String replyTheme;

	/**
	 * JMS类型，队列、主题、持久主题 1:QUEUE,2:TOPIC,3:DURABLETOPIC
	 */
	protected int type = 1;

	/**
	 * FIXME 该属性是否有用？ 区别配置是consumer还是producer
	 */
	protected boolean consumer;

	/**
	 * clientID
	 */
	protected String clientID;

	/**
	 * 用来创建永久订阅的consumerName
	 */
	protected String consumerName;

	/**
	 * jms selector
	 */
	protected String selector;

	/**
	 * priority 优先级
	 */
	protected int priority = 1;

	/**
	 * getUrl
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * setUrl
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * getUser
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * setUser
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * getPassword
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * setPassword
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * getContextFactory
	 * 
	 * @return the contextFactory
	 */
	public String getContextFactory() {
		return contextFactory;
	}

	/**
	 * setContextFactory
	 * 
	 * @param contextFactory
	 *            the contextFactory to set
	 */
	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * getConnectionFactory
	 * 
	 * @return the connectionFactory
	 */
	public String getConnectionFactory() {
		return connectionFactory;
	}

	/**
	 * setConnectionFactory
	 * 
	 * @param connectionFactory
	 *            the connectionFactory to set
	 */
	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * getDeliveryMode
	 * 
	 * @return the deliveryMode
	 */
	public int getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * setDeliveryMode
	 * 
	 * @param deliveryMode
	 *            the deliveryMode to set
	 */
	public void setDeliveryMode(int deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	/**
	 * getiAcknowledgementMode
	 * 
	 * @return the iAcknowledgementMode
	 */
	public int getiAcknowledgementMode() {
		return iAcknowledgementMode;
	}

	/**
	 * setiAcknowledgementMode
	 * 
	 * @param iAcknowledgementMode
	 *            the iAcknowledgementMode to set
	 */
	public void setiAcknowledgementMode(int iAcknowledgementMode) {
		this.iAcknowledgementMode = iAcknowledgementMode;
	}

	/**
	 * getTimeToLive
	 * 
	 * @return the timeToLive
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * setTimeToLive
	 * 
	 * @param timeToLive
	 *            the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	/**
	 * getComsumerTheme
	 * 
	 * @return the comsumerTheme
	 */
	public String getGetTheme() {
		return getTheme;
	}

	/**
	 * setComsumerTheme
	 * 
	 * @param comsumerTheme
	 *            the comsumerTheme to set
	 */
	public void setGetTheme(String comsumerTheme) {
		this.getTheme = comsumerTheme;
	}

	/**
	 * getProducerTheme
	 * 
	 * @return the producerTheme
	 */
	public String getSendTheme() {
		return sendTheme;
	}

	/**
	 * setProducerTheme
	 * 
	 * @param producerTheme
	 *            the producerTheme to set
	 */
	public void setSendTheme(String producerTheme) {
		this.sendTheme = producerTheme;
	}

	/**
	 * getReplyTheme
	 * 
	 * @return the replyTheme
	 */
	public String getReplyTheme() {
		return replyTheme;
	}

	/**
	 * setReplyTheme
	 * 
	 * @param replyTheme
	 *            the replyTheme to set
	 */
	public void setReplyTheme(String replyTheme) {
		this.replyTheme = replyTheme;
	}

	/**
	 * getType
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * setType
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * consumer
	 * 
	 * @return the consumer
	 */
	public boolean isConsumer() {
		return consumer;
	}

	/**
	 * setConsumer
	 * 
	 * @param consumer
	 *            the consumer to set
	 */
	public void setConsumer(boolean consumer) {
		this.consumer = consumer;
	}

	/**
	 * getClientID
	 * 
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * setClientID
	 * 
	 * @param clientID
	 *            the clientID to set
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	/**
	 * getConsumerName
	 * 
	 * @return the consumerName
	 */
	public String getConsumerName() {
		return consumerName;
	}

	/**
	 * setConsumerName
	 * 
	 * @param consumerName
	 *            the consumerName to set
	 */
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	/**
	 * getSelector
	 * 
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * setSelector
	 * 
	 * @param selector
	 *            the selector to set
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * getId
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * setId
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getPriority
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * setPriority
	 * 
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "JmsBean [id=" + id + ", url=" + url + ", user=" + user
				+ ", password=" + password + ", transcation="
				+ this.isTranscation() + ", longConnection=" + longConnection
				+ ", deliveryMode=" + deliveryMode + ", acknowledgementMode="
				+ acknowledgementMode + ", timeToLive=" + timeToLive
				+ ", getTheme=" + getTheme + ", sendTheme=" + sendTheme
				+ ", replyTheme=" + replyTheme + ", type=" + type
				+ ", consumer=" + consumer + ", clientID=" + clientID
				+ ", consumerName=" + consumerName + ", selector=" + selector
				+ ", priority=" + priority + "]";
	}

	/**
	 * acknowledgementMode
	 * 
	 * @return the acknowledgementMode
	 */
	public int getAcknowledgementMode() {
		return acknowledgementMode;
	}

	/**
	 * acknowledgementMode
	 * 
	 * @param acknowledgementMode
	 *            the acknowledgementMode to set
	 */
	public void setAcknowledgementMode(int acknowledgementMode) {
		this.acknowledgementMode = acknowledgementMode;
	}

	/**
	 * transcation
	 * 
	 * @return the transcation
	 */
	public boolean isTranscation() {
		return transcation;
	}

	/**
	 * transcation
	 * 
	 * @param transcation
	 *            the transcation to set
	 */
	public void setTranscation(boolean transcation) {
		this.transcation = transcation;
	}

	/**
	 * longConnection
	 * 
	 * @return the longConnection
	 */
	public boolean isLongConnection() {
		return longConnection;
	}

	/**
	 * longConnection
	 * 
	 * @param longConnection
	 *            the longConnection to set
	 */
	public void setLongConnection(boolean longConnection) {
		this.longConnection = longConnection;
	}
	
	public JmsBean clone() {
		// TODO
		return this;
	}

}
