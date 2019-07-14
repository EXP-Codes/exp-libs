package exp.libs.warp.net.mq.jms.sup;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQDestination;

/**
 * 消息消费者<br/>
 * 使用场景:订阅主题，或监听队列消息时使用<br/>
 * 1 实例化Consumer,2设置相关参数,3 创建持久订阅者或队列监听者,4 设置监听类<br/>
 * 用途：提供消息消费者注册及相关操作和属性设置<br/>
 * 例：Consumer consumer= new Consumer(pro.getValue("tcp",
 * "tcp://192.168.6.2:61616"));<br/>
 * //持久订阅必须输入<br/>
 * consumer.setsClientID("gd_eoms");<br/>
 * //必须选择监听的方式，含三种方式，详见consumer.create....相关说明<br/>
 * consumer.createDurableSubscriber();<br/>
 * //Listener 为实现IListener的实体类，有信息则将调用onMessage方法<br/>
 * consumer.setMessageListener(new Listener());<br/>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Consumer {
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
	 * 接收主题
	 */
	private String sThemeName;

	/**
	 * 客户端ID
	 */
	private String sClientID;

	/**
	 * 消息确认机制
	 */
	private int iAcknowledgementMode = Session.AUTO_ACKNOWLEDGE;

	/**
	 * 用户名
	 */
	private String sUserName;

	/**
	 * 密码
	 */
	private String sPassWord;

	/**
	 * 过滤语句
	 */
	private String sSelectors;

	/**
	 * 消息ID
	 */
	private String jmsMessageID;

	/**
	 * JMS操作基类
	 */
	private JMSCommon jms;

	/**
	 * 接收方式，通过Queue
	 */
	public static final int TYPE_BYQUEUE = 1;

	/**
	 * 接收方式，通过Topic
	 */
	public static final int TYPE_BYROPIC = 2;

	/**
	 * 接收方式，通过创建持久消费者
	 */
	public static final int TYPE_DURABLE_SUBSCRIBER = 3;

	/**
	 * 创建对象的方式
	 */
	protected int createType = TYPE_BYQUEUE;

	/**
	 * 消息监听器
	 */
	protected IListener msgListener;

	/**
	 * 提供的重连实现中的异常处理钩子
	 */
	protected AbstractConsumerCallBack callBack;

	/**
	 * 自动回复的消息产生器
	 */
	private MessageProducer replyProducer;

	// private final static Logger log = Logger.getLogger(Consumer.class);
	/**
	 * 指定JMS的URL地址创建实例
	 * 
	 * @param sURI
	 *            格式如: tcp://IP:prot?parameter
	 *            或failover：//(tcp://IP:prot1,tcp://IP:prot2...)
	 *            其中parameter为可选部分,关于parameter属性，可以参考Apache的官方文档。
	 * 
	 */
	public Consumer(String sURI) {
		this.sURI = sURI;
	}

	/**
	 * 创建一般消费者(Queue 方式)
	 * 
	 * @throws Exception
	 */
	public void createConsumerByQueue() throws Exception {
		setCreateType(TYPE_BYQUEUE);
		setParameter();
		jms.createConsumerByQueue();
	}

	/**
	 * 设置监听类<br>
	 * 当有消息到达时,将Message通过onMessage方法告知给消费者
	 * 
	 * @param iListener
	 *            监听类
	 * @throws Exception
	 */
	public void setMessageListener(IListener iListener) throws Exception {
		if(jms == null){
			setParameter();
		}
		jms.setMessageListener(iListener);
	}

	/**
	 * 获取消息监听类
	 * 
	 * @return 消息监听类
	 * @throws JMSException
	 */
	public MessageListener getMessageListener() throws JMSException {
		return jms.getConsumer().getMessageListener();
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
	 * 添加实时连接监控，应在创建消费者后调用
	 * 
	 * @throws Exception
	 *             达到重连次数限制还是没有连接上时抛出
	 */
	public void addTransportListener() throws Exception {
		initMsgListenerClazz();
		ConsumerHelper ch = new ConsumerHelper();
		ch.monitorTransport(this);
	}

	/**
	 * 添加实时连接监控
	 * 
	 * @param retryCount
	 *            重连次数，小于等于0则一直重连，默认：10
	 * @param time2Retry
	 *            重连间隔，单位：毫秒，默认：5000
	 * @throws Exception
	 *             获取消息监听器失败时抛出
	 */
	public void addTransportListener(int retryCount, long time2Retry)
			throws Exception {
		initMsgListenerClazz();
		ConsumerHelper ch = new ConsumerHelper();
		ch.setRetryCount(retryCount);
		ch.setTime2Retry(time2Retry);
		ch.monitorTransport(this);
	}

	/**
	 * 创建一般消费者(Topic 方式) iListener 实现IListener的实体类即可
	 * 
	 * @throws Exception
	 */
	public void createConsumerByTopic() throws Exception {
		setCreateType(TYPE_BYROPIC);
		setParameter();
		jms.createConsumerByTopic();
	}

	/**
	 * 创建持久消费者 （必须设置sClientID与ConsumerName,且需确保唯一性）
	 * 
	 * @throws Exception
	 */
	public void createDurableSubscriber() throws Exception {
		setCreateType(TYPE_DURABLE_SUBSCRIBER);
		setParameter();
		jms.createDurableSubscriber();
	}

	/**
	 * 取消持久订阅
	 * 
	 * @throws Exception
	 */
	public void unsubscribe() throws Exception {
		setParameter();
		jms.unsubscribe();
	}

	/**
	 * 主动收取信息
	 * 
	 * @return Message 对象
	 * @throws Exception
	 */
	public Message receive() throws Exception {
		return jms.getConsumer().receive(1);
	}

	/**
	 * 清理资源(含会话)
	 * 
	 * @throws Exception
	 */
	public void closeAll() throws Exception {
		if (jms != null) {
			jms.closeConsumer();
			jms.closeSession();

		}
	}

	/**
	 * 初始化参数
	 * 
	 * @param jms
	 *            JMS操作对象
	 */
	private void setParameter() {
		if (jms == null) {
			jms = new JMSCommon(sURI);
		}
		jms.setContextFactory(contextFactory);
		jms.setConnectionFactory(connectionFactory);
		jms.setiAcknowledgementMode(iAcknowledgementMode);
		jms.setsPassWord(sPassWord);
		jms.setsUserName(sUserName);
		jms.setsThemeName(sThemeName == null || sThemeName.equals("") ? "test"
				: sThemeName);
		jms.setsClientID(sClientID);
		jms.setsConsumerName(sThemeName);
		jms.setsSelectors(sSelectors);
		jms.setJMSMessageID(jmsMessageID);
	}

	/**
	 * 连接是否关闭
	 * 
	 * @return true为关闭
	 */
	protected boolean isConnectionClosed() {
		return jms.isConnectionClosed();
	}

	/**
	 * 获取主题名称
	 * 
	 * @return 主题(String)
	 */
	public String getsThemeName() {
		return sThemeName;
	}

	/**
	 * 设置主题名称
	 * 
	 * @param themeName
	 *            主题名
	 */
	public void setsThemeName(String themeName) {
		sThemeName = themeName;
	}

	/**
	 * 获取客户端ID<br/>
	 * 持久订阅者必须输入，且需要保持唯一。
	 * 
	 * @return 客户端ID
	 */
	public String getsClientID() {
		return sClientID;
	}

	/**
	 * 设置客户端ID<br/>
	 * 持久订阅者必须输入，且需要保持唯一。
	 * 
	 * @param clientID
	 *            客户端ID
	 */
	public void setsClientID(String clientID) {
		sClientID = clientID;
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
	 * 设置消息确认机制,缺省为自动确认即AUTO_ACKNOWLEDGE
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
	 * 获取过滤条件
	 * 
	 * @return 过滤语句
	 */
	public String getsSelectors() {
		return sSelectors;
	}

	/**
	 * 设置过滤条件<br/>
	 * 需遵照SQL92语法,如 name like '%张%' and sex=1 ...
	 * 
	 * @param selectors
	 *            过滤条件
	 */
	public void setsSelectors(String selectors) {
		sSelectors = selectors;
	}

	/**
	 * 获取消息ID
	 * 
	 * @return 消息ID
	 */
	public String getJMSMessageID() {
		return jmsMessageID;
	}

	/**
	 * 设置消息ID 请注意设置的消息ID会被系统覆盖
	 * 
	 * @param messageID
	 *            消息ID
	 */
	public void setJMSMessageID(String messageID) {
		jmsMessageID = messageID;
	}

	/**
	 * 获取JMSCommon对象
	 * 
	 * @return JMSCommon对象
	 */
	public JMSCommon getJms() {
		return jms;
	}

	/**
	 * 获取创建对象的方式
	 * 
	 * @return 创建对象的方式
	 */
	public int getCreateType() {
		return createType;
	}

	/**
	 * 获取创建对象的方式
	 * 
	 * @param createType
	 *            创建对象的方式
	 */
	public void setCreateType(int createType) {
		this.createType = createType;
	}

	/**
	 * 获取上下文工厂
	 * 
	 * @return 上下文工厂
	 */
	public String getContextFactory() {
		return contextFactory;
	}

	/**
	 * 设置上下文工厂
	 * 
	 * @param contextFactory
	 *            上下文工厂
	 */
	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * 获取连接工厂
	 * 
	 * @return 连接工厂
	 */
	public String getConnectionFactory() {
		return connectionFactory;
	}

	/**
	 * 设置连接工厂
	 * 
	 * @param connectionFactory
	 *            连接工厂
	 */
	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 保存消息监听器信息
	 * 
	 * @throws JMSException
	 */
	private void initMsgListenerClazz() throws JMSException {
		// 断开连接后，MessageConsumer对象关闭，取不到值，这里要保存监听器信息，重连时使用
		IListener listener = (IListener) getMessageListener();
		if (listener != null) {
			msgListener = listener;
		}
	}

	/**
	 * 获取消息监听器
	 * 
	 * @return 消息监听器
	 */
	protected IListener getMsgListener() {
		return msgListener;
	}

	/**
	 * 获取消费者回调
	 * 
	 * @return 消费者回调
	 */
	protected AbstractConsumerCallBack getCallBack() {
		return callBack;
	}

	/**
	 * 设置消费者回调
	 * 
	 * @param callBack
	 *            消费者回调
	 */
	public void setCallBack(AbstractConsumerCallBack callBack) {
		this.callBack = callBack;
	}

	/**
	 * 收到消息后进行响应，以便消息生产者进行onMessageConsumed回调
	 * 
	 * @param message 响应的消息，该消息的<code>JMSReplyTo</code>必须和接收到的消息一致
	 * @see AbstractProducerCallBack#onMessageConsumed(Message)
	 * @see Message#setJMSReplyTo(String)
	 * @throws JMSException 响应消息失败
	 */
	public void response(Message message) throws JMSException {
		if (replyProducer == null) {
			replyProducer = getJms().getsession().createProducer(null);
			replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}
		ActiveMQDestination destination = ActiveMQDestination.transform(message
				.getJMSReplyTo());
		ActiveMQConnection conn = transform(getJms().getConnection());
		if (destination == null || conn == null) {
			return;
		}
		if (destination.isTemporary()) {
			if (!conn.isDeleted(destination)) {
				replyProducer.send(destination, message);
			} else {
				throw new JMSException("该消息的生产者跟JMS服务器连接中断过，响应通道丢失！");
			}
		} else {
			throw new JMSException("JMSReplyTo设置有误！");
		}
	}

	/**
	 * 转换连接类型
	 * 
	 * @param conn
	 *            连接
	 * @return ActiveMQConnection
	 */
	private ActiveMQConnection transform(Connection conn) {
		if (conn == null) {
			return null;
		}
		if (conn instanceof ActiveMQConnection) {
			return (ActiveMQConnection) conn;
		}
		return null;
	}
	/**
	 * 从接口来分析该类的功能实现。  Consumer 提供了如下接口： 1. JSM常用功能：
	 * Create/设置MessageListener|TransportListener/ receive/ 2. response
	 * 往接收到的消息指定的回复地址发送消息。主要 目的是为了实现Procesers的消息接收回调功能。详细见
	 * AbstractProducerCallBack类的说明 3.
	 * 自动重连的帮助方法。ConsumerHelper类是建立在TransportListener
	 * 机制上的自动重连帮助类，该类自身线程建立连接，并在发生连接中断时 进行重连。 AbstractConsumerCallBack
	 * 接口用于定义重连的异常回调
	 * 
	 */
}
