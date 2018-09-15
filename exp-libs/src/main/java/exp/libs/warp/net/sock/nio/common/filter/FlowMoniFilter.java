package exp.libs.warp.net.sock.nio.common.filter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.net.sock.nio.common.filterchain.INextFilter;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.BaseFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 流量监控过滤器 （监控单位：字节）
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class FlowMoniFilter extends BaseFilter {

	/**
	 * 日志器
	 */
	private final static Logger log = LoggerFactory.getLogger(FlowMoniFilter.class);
	
	/**
	 * 接收数据流 属性标签
	 */
	private static final String RECV_FLOW_KEY = "recvFlow";

	/**
	 * 发送数据流 属性标签
	 */
	private static final String SEND_FLOW_KEY = "sendFlow";

	/**
	 * <pre>
	 * 会话验证事件。
	 * 
	 * 对流量监控过滤器而言，为之后处理方便起见，在触发会话验证事件的过程中，可添加流量监控的属性标签
	 * </pre>
	 * 
	 * @param nextFilter 关系过滤器
	 * @param session 会话
	 * @throws Exception 异常
	 */
	@Override
	public void onSessionCreated(INextFilter nextFilter, ISession session)
			throws Exception {
		
		// 添加session的属性键值
		session.getProperties().put(RECV_FLOW_KEY, new Statistic());
		session.getProperties().put(SEND_FLOW_KEY, new Statistic());

		// 重写时注意补充过滤链连接
		nextFilter.onSessionCreated(session);
	}

	/**
	 * <pre>
	 * 消息接收事件。
	 * 
	 * 在接收到消息时，累加字节数
	 * </pre>
	 * 
	 * @param nextFilter 关系过滤器
	 * @param session 会话
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	@Override
	public void onMessageReceived(INextFilter nextFilter, ISession session,
			Object msg) throws Exception {

		int recvMsgSize = msg.toString().getBytes().length;
		Map<String, Object> property = session.getProperties();
		Statistic recvFlow = (Statistic) property.get(RECV_FLOW_KEY);

		recvFlow.add(recvMsgSize);
		log.info("会话 [" + session + "] 累计接收流量 [" + 
				recvFlow.getFlow() + " bytes].");

		// 重写时注意补充过滤链连接
		nextFilter.onMessageReceived(session, msg);
	}

	/**
	 * <pre>
	 * 消息发送事件。
	 * 
	 * 在发送消息时，累加字节数
	 * </pre>
	 * 
	 * @param preFilter 关系过滤器
	 * @param session 会话
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	@Override
	public void onMessageSent(INextFilter preFilter, ISession session, Object msg)
			throws Exception {

		int sendMsgSize = msg.toString().getBytes().length;
		Map<String, Object> property = session.getProperties();
		Statistic sendFlow = (Statistic) property.get(SEND_FLOW_KEY);

		sendFlow.add(sendMsgSize);
		log.info("会话 [" + session + "] 累计发送流量 [" + 
				sendFlow.getFlow() + " bytes].");
		
		// 重写时注意补充过滤链连接
		preFilter.onMessageSent(session, msg);
	}

	
	/**
	 * <PRE>
	 * 内部统计类，用于计算流量
	 * </PRE>
	 * <br/><B>PROJECT : </B> exp-libs
	 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
	 * @version   2015-12-27
	 * @author    EXP: ***REMOVED***@qq.com
	 * @since     jdk版本：jdk1.6
	 */
	private static class Statistic {

		/**
		 * 流量
		 */
		private int flow = 0;

		/**
		 * 递增流量
		 * @param newFlow 新流量
		 */
		public synchronized void add(int newFlow) {
			this.flow += newFlow;
		}

		/**
		 * 返回流量
		 * @return 流量
		 */
		public int getFlow() {
			return this.flow;
		}
	}

}
