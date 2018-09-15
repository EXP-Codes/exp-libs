package exp.libs.warp.net.mq.jms.sup;

import java.io.IOException;

/**
 * <PRE>
 * Consumer类的辅助类
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ConsumerHelper extends TransportListenerImpl implements Runnable {

	/**
	 * 锁变量
	 */
	private Object connectedLock = new Object();

	/**
	 * 是否连接上
	 */
	private Boolean connected = false;

	/**
	 * 是否需要重连
	 */
	private Boolean needRetry = true;

	/**
	 * 重连次数，小于等于0则一直重连
	 */
	private int retryCount = 10;

	/**
	 * 重连间隔，单位：毫秒
	 */
	private long time2Retry = 5 * 1000;

	/**
	 * 消费者
	 */
	private Consumer consumer = null;

	/**
	 * 是否是第一次检测
	 */
	private boolean firstCheck = true;

	/**
	 * 消费者的clientId
	 */
	private String clientId;

	/**
	 * 实时监控consumer的连接，断开则尝试重连
	 * 
	 * @param consumer
	 *            消费者
	 */
	protected void monitorTransport(Consumer consumer) {
		this.consumer = consumer;
		if (isValid(consumer.getsClientID())) {
			clientId = consumer.getsClientID();
		}
		Thread monitor = new Thread(this);// 开一条线程去监控连接
		monitor.start();
	}

	/**
	 * 线程方法
	 */
	public void run() {
		while (true) {
			if (!connected) {
				doConnect(consumer);
				if (!needRetry) {// 不重连，返回失败信息
					if (consumer.getCallBack() != null) {
						Exception e = new Exception("连接失败！超出重连次数【" + retryCount
								+ "】！");
						consumer.getCallBack().onReConnectionException(e);
					}
					break;
				}
			} else {
				try {
					synchronized (connectedLock) {// 已经连接上，等待
						connectedLock.wait();
					}
				} catch (InterruptedException e) {
					if (consumer.getCallBack() != null) {
						consumer.getCallBack().onReConnectionException(e);
					}
					break;
				}
			}
		}
	}

	/**
	 * 尝试连接
	 * 
	 * @param consumer
	 *            消费者
	 */
	private void doConnect(Consumer consumer) {
		int tryCount = 0;// 已经尝试连接次数
		while (!connected) {
			try {
				consumer.getJms().checkConnection();
				if (consumer.isConnectionClosed()) {
					throw new Exception("Transport Failed!");
				}
				// 第一次检测不需要，由用户自己调用创建消费者方法
				if (!firstCheck) {
					int createType = consumer.getCreateType();

					// 断开连接时，取消ActiveMQConnectionFactory的ClientID,重连时恢复
					consumer.setsClientID(clientId);
					if (createType == Consumer.TYPE_BYQUEUE) {
						consumer.createConsumerByQueue();
					} else if (createType == Consumer.TYPE_BYROPIC) {
						consumer.createConsumerByTopic();
					} else if (createType == Consumer.TYPE_DURABLE_SUBSCRIBER) {
						consumer.createDurableSubscriber();
					}
					// 重新建立连接,需要重新设置监听器
					if (consumer.getMsgListener() != null) {
						consumer.setMessageListener(consumer.getMsgListener());
					}
				}
				// consumer.setTransportListener(this);
				connected = true;
				firstCheck = false;
			} catch (Exception e) {
				connected = false;
				tryCount++;
				if (retryCount > 0 && tryCount >= retryCount) {
					needRetry = false;
					break;
				}
				try {
					Thread.sleep(time2Retry);
				} catch (InterruptedException e1) {
					if (consumer.getCallBack() != null) {
						consumer.getCallBack().onReConnectionException(e);
					}
					break;
				}
			}
		}
	}

	@Override
	public void onException(IOException error) {
		if (isValid(clientId)) {
			// 断开连接时，取消ActiveMQConnectionFactory的ClientID
			consumer.getJms().setsClientID(null);
		}
		connected = false;
		needRetry = true;
		synchronized (connectedLock) {
			connectedLock.notifyAll();
		}
	}

	private boolean isValid(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 设置重连次数
	 * 
	 * @param retryCount
	 *            重连次数，小于等于0则一直重连
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * 设置重连间隔
	 * 
	 * @param time2Retry
	 *            重连间隔，单位：毫秒
	 */
	public void setTime2Retry(long time2Retry) {
		this.time2Retry = time2Retry;
	}

	/**
	 * 该类实现了一个连接监听和重连的机制。线程确保建立连接并休眠 知道连接冲断被唤醒后重新连接。属性包括重连次数和重连间隔。
	 */
}
