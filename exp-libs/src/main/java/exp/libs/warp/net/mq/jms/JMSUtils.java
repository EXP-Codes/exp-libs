package exp.libs.warp.net.mq.jms;

import exp.libs.warp.conf.xml.XConfig;
import exp.libs.warp.conf.xml.XConfigFactory;
import exp.libs.warp.net.mq.jms.bean.JmsBean;
import exp.libs.warp.net.mq.jms.sup.Consumer;
import exp.libs.warp.net.mq.jms.sup.Producers;

/**
 * <pre>
 * JMS工具类
 * </pre>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JMSUtils {
	/**
	 * QUEUE
	 */
	public static final int TYPE_BYQUEUE = 1;

	/**
	 * TOPIC
	 */
	public static final int TYPE_BYTOPIC = 2;

	/**
	 * DURABLE_TOPIC
	 */
	public static final int TYPE_DURABLE_SUBSCRIBER = 3;

	/**
	 * 通过配置id获取producers对象
	 * 
	 * @param producersId
	 *            配置id
	 * @return Producers producers对象
	 */
	public static Producers getProducers(String producersId) {
		// FIXME
		XConfig conf = XConfigFactory.getDefaultConfig();
		JmsBean jmsBean = conf.getJmsBean(producersId);
		return getProducers(jmsBean);
	}

	/**
	 * Bean做为入参 getProducers
	 * 
	 * @param jmsBean
	 *            JmsBean
	 * @return Producers producers对象
	 */
	public static Producers getProducers(JmsBean jmsBean) {

		// 创建生产者，并将服务器地址端口用参数传递过去，此时还没连接
		Producers producer = new Producers(jmsBean.getUrl());

		producer.setContextFactory(jmsBean.getContextFactory());
		producer.setConnectionFactory(jmsBean.getConnectionFactory());
		producer.setsThemeName(jmsBean.getSendTheme());
		if (jmsBean.getUser() != null && !"".equals(jmsBean.getUser())) {
			producer.setsUserName(jmsBean.getUser());
		}
		if (jmsBean.getPassword() != null && !"".equals(jmsBean.getPassword())) {
			producer.setsPassWord(jmsBean.getPassword());
		}

		producer.setiDeliveryMode(jmsBean.getDeliveryMode());
		producer.setiTimeToLive(jmsBean.getTimeToLive());

		return producer;
	}
	
	/**
	 * 通过配置id获取consumer对象<br>
	 * getConsumer
	 * 
	 * @param consumerId
	 *            配置id
	 * @return Consumer consumer对象
	 * @throws Exception
	 */
	public static Consumer getConsumer(String consumerId) throws Exception {
		// FIXME
		XConfig conf = XConfigFactory.getDefaultConfig();
		JmsBean jmsBean = conf.getJmsBean(consumerId);
		return getConsumer(jmsBean);
	}

	/**
	 * bean 作为入参 getConsumer
	 * 
	 * @param jmsBean
	 *            JmsBean
	 * @return Consumer consumer对象
	 * @throws Exception
	 */
	public static Consumer getConsumer(JmsBean jmsBean) throws Exception {

		if (jmsBean == null) {
			throw (new Exception("没有找到相应的JmsBean。"));
		}

		Consumer consumer = new Consumer(jmsBean.getUrl());

		consumer.setContextFactory(jmsBean.getContextFactory());
		consumer.setConnectionFactory(jmsBean.getConnectionFactory());
		consumer.setsThemeName(jmsBean.getGetTheme());
		if (jmsBean.getUser() != null && !"".equals(jmsBean.getUser())) {
			consumer.setsUserName(jmsBean.getUser());
		}
		if (jmsBean.getPassword() != null && !"".equals(jmsBean.getPassword())) {
			consumer.setsPassWord(jmsBean.getPassword());
		}

		if (jmsBean.getSelector() != null && !"".equals(jmsBean.getSelector())) {
			consumer.setsSelectors(jmsBean.getSelector());
		}
		int type = jmsBean.getType();
		if (type == TYPE_BYQUEUE) {
			consumer.createConsumerByQueue();
		} else if (type == TYPE_BYTOPIC) {
			consumer.createConsumerByTopic();
		} else {
			if (jmsBean.getClientID() != null) {
				consumer.setsClientID(jmsBean.getClientID());
			} else {
				consumer.setsClientID(jmsBean.getGetTheme() + "_"
						+ jmsBean.getId());
			}
			consumer.createDurableSubscriber();
		}

		return consumer;
	}
}
