package exp.libs.warp.net.sock.nio.common.filter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.net.sock.nio.common.filterchain.INextFilter;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.BaseFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 会话最近操作时间过滤器
 * 
 * 记录会话最后一次接收/发送命令的时间
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LatelyFilter extends BaseFilter {

	/**
	 * 日志器
	 */
	private final static Logger log = LoggerFactory.getLogger(LatelyFilter.class);
	
	/**
	 * 最后一次接收命令时间的属性 键值
	 */
	private String lastRecvKey = "lastRecv";
	
	/**
	 * 最后一次发送命令时间的属性 键值
	 */
	private String lastSendKey = "lastSend";
	
	@Override
	public void onSessionCreated(INextFilter nextFilter, ISession session)
			throws Exception {
				
		// 添加session的属性键值
		session.getProperties().put(lastRecvKey, new OpTime());
		session.getProperties().put(lastSendKey, new OpTime());
				
		nextFilter.onSessionCreated(session);
	}
	
	@Override
	public void onMessageReceived(INextFilter nextFilter, ISession session,
			Object msg) throws Exception {

		Map<String, Object> property = session.getProperties();
		OpTime lastRecvTime = (OpTime) property.get(lastRecvKey);

		lastRecvTime.reFlash();
		log.info("会话 [" + session + "] 最近一次接收消息时间 [" + 
				lastRecvTime.getTime() + "].");

		nextFilter.onMessageReceived(session, msg);
	}

	@Override
	public void onMessageSent(INextFilter preFilter, ISession session, Object msg)
			throws Exception {

		Map<String, Object> property = session.getProperties();
		OpTime lastSendTime = (OpTime) property.get(lastSendKey);

		lastSendTime.reFlash();
		log.info("会话 [" + session + "] 最近一次发送消息时间 [" + 
				lastSendTime.getTime() + "].");
		
		preFilter.onMessageSent(session, msg);
	}

	/**
	 * <PRE>
	 * 内部类，用于刷新时间点
	 * </PRE>
	 * <br/><B>PROJECT : </B> exp-libs
	 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
	 * @version   2015-12-27
	 * @author    EXP: ***REMOVED***@qq.com
	 * @since     jdk版本：jdk1.6
	 */
	private static class OpTime {

		/**
		 * 最近一次的操作时间
		 */
		private long time = -1;

		/**
		 * 刷新操作时间点
		 */
		public synchronized void reFlash() {
			this.time = System.currentTimeMillis();
		}

		/**
		 * 返回最近一次的操作时间
		 * @return 最近一次的操作时间
		 */
		public long getTime() {
			return this.time;
		}
	}

}
