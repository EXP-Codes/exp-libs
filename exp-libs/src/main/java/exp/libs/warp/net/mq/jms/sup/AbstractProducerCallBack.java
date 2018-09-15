package exp.libs.warp.net.mq.jms.sup;

import javax.jms.Message;

/**
 * <pre>
 * 消息生产者回调抽象类
 * </pre>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class AbstractProducerCallBack {

	/**
	 * 发送的消息被消费者消费后回调
	 * 
	 * @param message
	 *            异常
	 */
	public void onMessageConsumed(Message message) {

	}

	/**
	 * 消息发送者提供的接收到消息的回调类 这个机制是在ACTIVE MQ基础上建立的： 1. 在接收端建立一个临时的消息回复者 2.
	 * 在发送端建立一个临时的消息接收者，并将自身实现为一个IListener 3. 发送端在发送消息时指定消息的回复地址为其临时的消息接受者。 4.
	 * 接收端在接收到消息，提供了respons接口供应用发送回复信息，该回复信息将 发送到发送端的临时接收者 5.
	 * 临时接受者通过AbstractProducerCallBack来处理回复业务。
	 */
}
