package exp.libs.warp.net.mq.jms.sup;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * 消息生产者<br/>
 * 使用场景：需要发送消息至JMS时，1 实例化Producers, 2 设置相关参数 ,3 调用Send方法发送消息。<br/>
 * 用途：提供生产消息相关操作及属性设置<br/>
 * 例:<br/>
 * //tcp://192.168.6.2:61616 为连接参数<br/>
 * Producers producers = new Producers("tcp://192.168.6.2:61616");<br/>
 * //队列名为110<br/>
 * producers.setsThemeName("110");<br/>
 * //message为消息内容。<br/>
 * producers.sendByQueue("message");<br/>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Producers implements IListener {

	/**
	 * 连接字符串
	 */
	private String sURI;

	/**
	 * 上下文工厂
	 */
	private String contextFactory;

	/**
	 * 连接工厂
	 */
	private String connectionFactory;

	/**
	 * 发送主题
	 */
	private String sThemeName;

	/**
	 * 回复主题
	 */
	private String sRThemeName;

	/**
	 * 客户端ID
	 */
	private String sClientID;

	/**
	 * 是否采用事务，true为是
	 */
	private boolean isTransaction = false;

	/**
	 * 是否长连接，true为是
	 */
	private boolean isLongConnection = false;

	/**
	 * 消息发送模式
	 */
	private int iDeliveryMode = DeliveryMode.PERSISTENT;

	/**
	 * 消息确认机制
	 */
	private int iAcknowledgementMode = Session.AUTO_ACKNOWLEDGE;

	/**
	 * 消息优先级
	 */
	private int iPriority = 0;

	/**
	 * 消息存活时间 单位毫秒
	 */
	private Long iTimeToLive = 0L;

	/**
	 * 用户名
	 */
	private String sUserName;

	/**
	 * 密码
	 */
	private String sPassWord;

	/**
	 * JMS操作基类
	 */
	private JMSCommon jms;

	/**
	 * 消息ID
	 */
	private String jmsCorrelationID;

	/**
	 * 提供的重连实现中的异常处理钩子
	 */
	private AbstractProducerCallBack callBack;

	/**
	 * 回调目的地
	 */
	private Destination tempDest = null;

	/**
	 * 消费者
	 */
	private MessageConsumer responseConsumer = null;

	/**
	 * 获取JMSCommon对象
	 * 
	 * @return JMSCommon对象
	 */
	public JMSCommon getJms() {
		return jms;
	}

	/**
	 * 指定JMS的URL地址创建实例
	 * 
	 * @param sURI
	 *            格式如: tcp://IP:prot?parameter
	 *            或failover：//(tcp://IP:prot1,tcp://IP:prot2...) 其中
	 *            ?parameter为可选部分
	 */
	public Producers(String sURI) {
		this.sURI = sURI;
	}

	/**
	 * 获取会话(注意该方法在未创建连接的情况下会创建连接)
	 * 
	 * @return Session 会话
	 * @throws Exception
	 */
	public synchronized Session getSession() throws Exception {
		setParameter();
		return jms.getsession();
	}

	/**
	 * 发送消息Topic 方式
	 * 
	 * @param message
	 *            消息内容(String)
	 * @return Message对象，可获取相关发送状态。 请注意 采用长连接在退出时请手工释放资源。
	 *         备注：如有需要请设置好相关属性后才调用发送方法。
	 */
	public synchronized Message sendByTopic(String message) throws Exception {
		Message msg = null;
		createProducerByTopic();
		msg = jms.getsession().createTextMessage(message);// 转换信息
		sendMsg(msg);
		return msg;

	}

	/**
	 * 发送消息Topic 方式
	 * 
	 * @param message
	 *            Message对象
	 * @return Message对象，可获取相关发送状态。 请注意 采用长连接在退出时请手工释放资源。
	 *         备注：如有需要请设置好相关属性后才调用发送方法。
	 */
	public Message sendByTopic(Message message) throws Exception {
		createProducerByTopic();
		sendMsg(message);
		return message;
	}

	/**
	 * 发送消息Topic 方式
	 * 
	 * @param message
	 *            消息内容(String)
	 * @return Message对象，可获取相关发送状态。 请注意 采用长连接在退出时请手工释放资源。
	 *         备注：如有需要请设置好相关属性后才调用发送方法。
	 */
	public synchronized Message sendByQueue(String message) throws Exception {
		Message msg = null;
		createProducerByQueue();
		msg = jms.getsession().createTextMessage(message);// 转换信息
		sendMsg(msg);
		return msg;

	}

	/**
	 * 发送消息Topic 方式
	 * 
	 * @param message
	 *            Message对象
	 * @return Message对象，可获取相关发送状态。 请注意 采用长连接在退出时请手工释放资源。
	 *         备注：如有需要请设置好相关属性后才调用发送方法。
	 */
	public Message sendByQueue(Message message) throws Exception {
		createProducerByQueue();
		sendMsg(message);
		return message;
	}

	/**
	 * 提交事务
	 * 
	 * @throws Exception
	 *             备注:采用事务方式提交信息时，在提交时候才会释放连接资源。
	 */
	public void commit() throws Exception {
		if (jms != null) {
			jms.getsession().commit();
			closeAll();
		}
	}

	/**
	 * 回滚事务
	 * 
	 * @throws Exception
	 *             备注：该操作在回滚事务的同时会释放回话和连接资源。
	 */
	public void rollback() throws Exception {
		if (jms != null) {
			jms.getsession().rollback();
			closeAll();
		}
	}

	/**
	 * 释放回话和连接资源 长连接时需要调用该方法,主动释放资源。
	 * 
	 * @throws Exception
	 */
	public void closeAll() throws Exception {
		if (jms != null) {
			jms.closeSession();
			jms.closeConnection();
		}
	}

	/**
	 * 初始化参数
	 */
	private void setParameter() throws Exception {
		if (jms == null) {
			jms = new JMSCommon(sURI);
		}
		jms.setContextFactory(contextFactory);
		jms.setConnectionFactory(connectionFactory);
		jms.setiAcknowledgementMode(iAcknowledgementMode);
		jms.setisTransaction(isTransaction);
		jms.setsPassWord(sPassWord);
		jms.setsUserName(sUserName);
		jms.setsThemeName(sThemeName == null || sThemeName.equals("") ? "test"
				: sThemeName);
		jms.setsClientID(sClientID);
		jms.setiTimeToLive(iTimeToLive);
		jms.setJMSCorrelationID(jmsCorrelationID);
		jms.setSRThemeName(sRThemeName);
		jms.setiDeliveryMode(iDeliveryMode);
		jms.createConnection(sClientID);
		jms.createSession();
		jms.setiPriority(iPriority);
	}

	/**
	 * 发送消息后清理资源
	 * 
	 * @param message
	 *            Message消息
	 * @return Message消息
	 * @throws Exception
	 */
	private Message sendMsg(Message message) throws Exception {
		checkCallBack(message);
		jms.send(message);
		if (!isTransaction && !isLongConnection) {
			closeAll(); // 未采用事务和长连接的情况下释放资源
		} 
		return message;
	}

	/**
	 * 创建生产者Topic方式(广播方式)
	 */
	private void createProducerByTopic() throws Exception {

		setParameter();
		jms.createProducerByTopic();
	}

	/**
	 * 创建生产者Queue方式(队列方式)
	 */
	private void createProducerByQueue() throws Exception {
		setParameter();
		jms.createProducerByQueue();
	}

	/**
	 * 回复消息到制定的队列当中
	 * 
	 * @param destination
	 *            目的地
	 * @param message
	 *            Message消息
	 * @throws Exception
	 */
	public void createProducerReplyto(Destination destination, String message)
			throws Exception {
		setParameter();
		jms.createProducerReplyto(destination, message);
	}

	/**
	 * 回复消息到制定的队列当中
	 * 
	 * @param destination
	 *            目的地
	 * @param message
	 *            Message消息
	 * @throws Exception
	 */
	public void createProducerReplyto(Destination destination, Message message)
			throws Exception {
		setParameter();
		jms.createProducerReplyto(destination, message);
	}

	/**
	 * 连接是否关闭
	 * 
	 * @return 是否关闭，true为是
	 */
	public boolean isConnectionClosed() {
		return jms.isConnectionClosed();
	}

	/**
	 * 会话是否关闭
	 * 
	 * @return 是否关闭，true为是
	 */
	public boolean isSessionClosed() {
		return jms.isSessionClosed();
	}

	/**
	 * 消息生产者是否关闭
	 * 
	 * @return 是否关闭，true为是
	 */
	public boolean isProducerClosed() {
		return jms.isProducerClosed();
	}

	/**
	 * 回复消息的消息生产者是否关闭
	 * 
	 * @return 是否关闭，true为是
	 */
	public boolean isReplyProducerClosed() {
		return jms.isReplyProducerClosed();
	}

	/**
	 * 设置连接监听<br/>
	 * 当与JMS服务器连接有异常时，可以通过onException(IOException error)方法获得回调
	 * 
	 * @param iListener
	 *            实现接口 ITransportListener 或者继承 TransportListenerImpl 的实例
	 */
	// public void setTransportListener(ITransportListener iListener) {
	// jms.setTransportListener(iListener);
	// }

	/**
	 * 获取主题名称(广播名称)
	 * 
	 * @return 主题(String)
	 */
	public String getsThemeName() {
		return sThemeName;
	}

	/**
	 * 设置主题名称(广播名称)
	 * 
	 * @param themeName
	 *            主题名
	 */
	public void setsThemeName(String themeName) {
		sThemeName = themeName;
	}

	/**
	 * 获取客户端ID
	 * 
	 * @return 客户端ID
	 */
	public String getsClientID() {
		return sClientID;
	}

	/**
	 * 设置客户端ID
	 * 
	 * @param clientID
	 *            客户端ID
	 */
	public void setsClientID(String clientID) {
		sClientID = clientID;
	}

	/**
	 * 获取是否采用事务
	 * 
	 * @return 是/true;否/false
	 */
	public boolean getisTransaction() {
		return isTransaction;
	}

	/**
	 * 设置是否采用事务，缺省为否
	 * 
	 * @param transaction
	 *            是否采用事务 建议：对于大批量数据需要提交时，建议采用事务的方式，达到多少条后再提交一次。用以提升性能。
	 */
	public void setisTransaction(boolean transaction) {
		isTransaction = transaction;
	}

	/**
	 * 返回消息存储模式
	 * 
	 * @return 1: NON_PERSISTENT (非持久); 2:PERSISTENT(持久)
	 */
	public int getiDeliveryMode() {
		return iDeliveryMode;
	}

	/**
	 * 设置消息存储模式 缺省为持久化模式
	 * 
	 * @param deliveryMode
	 *            1: NON_PERSISTENT ; 2:PERSISTENT
	 */
	public void setiDeliveryMode(int deliveryMode) {
		iDeliveryMode = deliveryMode;
	}

	/**
	 * 获取消息确认机制
	 * 
	 * @return 消息确认机制(int) 自动确认<br/>
	 *         AUTO_ACKNOWLEDGE = 1; <br/>
	 *         消费者手动确认,请注意该确认机会会话层的，即确认一个代表该会话下的所有信息均被确认<br/>
	 *         CLIENT_ACKNOWLEDGE = 2;<br/>
	 *         迟钝确认消息提交,如果JMS provider失败，那么可能会导致一些重复的消息。<br/>
	 *         DUPS_OK_ACKNOWLEDGE = 3;<br/>
	 *         事务确认<br/>
	 *         SESSION_TRANSACTED = 0;
	 */
	public int getiAcknowledgementMode() {
		return iAcknowledgementMode;
	}

	/**
	 * 设置消息确认机制,缺省为自动确认
	 * 
	 * @param acknowledgementMode
	 *            自动确认<br/>
	 *            AUTO_ACKNOWLEDGE = 1; <br/>
	 *            消费者手动确认,请注意该确认机会会话层的，即确认一个代表该会话下的所有信息均被确认<br/>
	 *            CLIENT_ACKNOWLEDGE = 2;<br/>
	 *            迟钝确认消息提交,如果JMS provider失败，那么可能会导致一些重复的消息。<br/>
	 *            DUPS_OK_ACKNOWLEDGE = 3;<br/>
	 *            事务确认<br/>
	 *            SESSION_TRANSACTED = 0;
	 */
	public void setiAcknowledgementMode(int acknowledgementMode) {
		iAcknowledgementMode = acknowledgementMode;
	}

	/**
	 * 获取过期时间 <br/>
	 * 默认为0,无过期时间
	 * 
	 * @return 过期时间 单位毫秒
	 */
	public Long getiTimeToLive() {
		return iTimeToLive;
	}

	/**
	 * 设置过期时间，
	 * 
	 * @param timeToLive
	 *            过期时间，单位毫秒，默认为0，无过期时间
	 */
	public void setiTimeToLive(Long timeToLive) {
		iTimeToLive = timeToLive;
	}

	/**
	 * 获取用户名<br/>
	 * 连接JMS服务器时使用,若服务器未设,可忽略
	 * 
	 * @return 用户名
	 */
	public String getsUserName() {
		return sUserName;
	}

	/**
	 * 设置用户名<br/>
	 * 连接JMS服务器时使用,若服务器未设,可忽略
	 * 
	 * @param userName
	 *            用户名
	 */
	public void setsUserName(String userName) {
		sUserName = userName;
	}

	/**
	 * 获取密码<br/>
	 * 连接JMS服务器时使用,若服务器未设,可忽略
	 * 
	 * @return 密码
	 */
	public String getsPassWord() {
		return sPassWord;
	}

	/**
	 * 获取密码<br/>
	 * 连接JMS服务器时使用,若服务器未设,可忽略
	 * 
	 * @param passWord
	 *            密码
	 */
	public void setsPassWord(String passWord) {
		sPassWord = passWord;
	}

	/**
	 * 获取是否采用长连接<br/>
	 * 对于大量消息同时发送时，建议采用长连接或事务方式提交，提升性能。<br/>
	 * 请注意 采用长连接在退出时请手工释放资源。
	 * 
	 * @return 是/否
	 */
	public boolean isLongConnection() {
		return isLongConnection;
	}

	/**
	 * 设置是否采用长连接<br/>
	 * 对于大量消息同时发送时，建议采用长连接或事务方式提交，提升性能。<br/>
	 * 请注意 采用长连接在退出时请手工释放资源。
	 * 
	 * @param isLongConnection
	 *            长连接，true为是
	 */
	public void setisLongConnection(boolean isLongConnection) {
		this.isLongConnection = isLongConnection;
	}

	/**
	 * 获取相关ID
	 * 
	 * @return 返回相关ID
	 */
	public String getJMSCorrelationID() {
		return jmsCorrelationID;
	}

	/**
	 * 设置相关ID 可视为属性,可根据实际需要使用
	 * 
	 * @param correlationID
	 *            相关ID
	 */
	public void setJMSCorrelationID(String correlationID) {
		jmsCorrelationID = correlationID;
	}

	/**
	 * 获取回复主题名
	 * 
	 * @return 返回回复主题名
	 */
	public String getsRThemeName() {
		return sRThemeName;
	}

	/**
	 * 设置返回主题名
	 * 
	 * @param themeName
	 *            这是返回主题名
	 */
	public void setsRThemeName(String themeName) {
		sRThemeName = themeName;
	}

	/**
	 * 获取消息优先级
	 * 
	 * @return 消息优先级
	 */
	public int getIPriority() {
		return iPriority;
	}

	/**
	 * 设置消息优先级
	 * 
	 * @param priority
	 *            消息优先级
	 */
	public void setIPriority(int priority) {
		iPriority = priority;
	}

	public String getContextFactory() {
		return contextFactory;
	}

	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}

	public String getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 设置生产者回调
	 * 
	 * @param callBack
	 *            生产者回调
	 */
	public void setCallBack(AbstractProducerCallBack callBack) {
		this.callBack = callBack;
	}

	/**
	 * 发送消息之前检查回调方法，如果设置回调，初始化回调需要的值
	 * 
	 * @param message
	 *            Message消息
	 */
	private void checkCallBack(Message message) {
		if (callBack == null) {
			return;
		}
		try {
			if (tempDest == null) {
				tempDest = getSession().createTemporaryQueue();
			}
			if (responseConsumer == null) {
				responseConsumer = getSession().createConsumer(tempDest);
				responseConsumer.setMessageListener(this);
			}
			message.setJMSReplyTo(tempDest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听临时通道的消息
	 * 
	 * @param message
	 *            Message消息
	 */
	public void onMessage(Message message) {
		if (callBack != null) {// 设置了消息消费回调，调用
			callBack.onMessageConsumed(message);
		}
	}

	/**
	 * Producers类： 1. 对producer常用功能进行封装，如：sendByTopic..../commit/
	 * rollback/配置参数设置 2. 提供了创建指定目的的回复producers； createProducerReplyto 3.
	 * 提供了长连接的属性，长连接时producers将保持会话，而短连接时 每一次发送完消息都是重新建立会话，发送消息，关闭会话。 4.
	 * 发送消息或接收到回复的回调机制。详细见AbstractProducerCallBack
	 */
}
