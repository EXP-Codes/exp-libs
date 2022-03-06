package exp.libs.warp.net.sock.nio.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.net.sock.nio.common.filterchain.INextFilter;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.BaseFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 日志打印过滤器（TODO:未完成）
 * 
 * 可用于打印消息在各个触发事件点的日志状态
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LogFilter extends BaseFilter {

	/**
	 * 日志器
	 */
	private final static Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	public void onSessionCreated(INextFilter nextFilter, ISession session)
			throws Exception {
				
		/**
		 * 打印会话创建日志
		 */
		log.info("Log onSessionCreated");
		
		nextFilter.onSessionCreated(session);
	}

	@Override
	public void onMessageReceived(INextFilter nextFilter, ISession session,
			Object msg) throws Exception {

		/**
		 * 打印消息接收日志
		 */
		log.info("Log onMessageReceived");
		
		nextFilter.onMessageReceived(session, msg);
	}

	@Override
	public void onMessageSent(INextFilter preFilter, ISession session, Object msg)
			throws Exception {

		/**
		 * 打印消息发送日志
		 */
		log.info("Log onMessageSent");
		
		preFilter.onMessageSent(session, msg);
	}

	@Override
	public void onExceptionCaught(INextFilter nextFilter, ISession session, 
			Throwable exception) {

		/**
		 * 打印异常日志
		 */
		log.info("Log onExceptionCaught");
		
		nextFilter.onExceptionCaught(session, exception);
	}

	@Override
	public void clean() {
		/**
		 * 打印资源清理日志
		 */
		log.info("Log clean");
	}

}
