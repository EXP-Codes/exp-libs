package exp.libs.warp.net.mq.jms.sup;

/**
 * <pre>
 * 消息消费者回调抽象类
 * </pre>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class AbstractConsumerCallBack {

	/**
	 * 消息消费者自动重连异常
	 * 
	 * @param e
	 *            异常
	 */
	public void onReConnectionException(Throwable e) {

	}

	/**
	 *  MQ 自动重连工具ConsumerHelper提供的异常回调
	 */
}
