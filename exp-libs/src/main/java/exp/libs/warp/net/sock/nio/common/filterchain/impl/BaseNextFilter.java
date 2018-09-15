package exp.libs.warp.net.sock.nio.common.filterchain.impl;

import exp.libs.warp.net.sock.nio.common.filterchain.AbstractNextFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.IFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 基本关系过滤器，用于封装业务过滤器
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class BaseNextFilter extends AbstractNextFilter {

	/**
	 * 构造函数
	 * @param filter 业务过滤器
	 */
	public BaseNextFilter(IFilter filter) {
		super(filter);
	}

	/**
	 * 触发下一个业务过滤器的onSessionCreated事件
	 * @param session 会话
	 */
	@Override
	public void onSessionCreated(ISession session) {
		
		try {
			filter.onSessionCreated(this.nextFilter, session);
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}

	/**
	 * 触发下一个业务过滤器的onMessageReceived事件
	 * @param session 会话
	 * @param msg 接收消息
	 */
	@Override
	public void onMessageReceived(ISession session, Object msg) {

		try {
			filter.onMessageReceived(this.nextFilter, session, msg);
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}

	/**
	 * 触发上一个业务过滤器的onMessageSent事件
	 * @param session 会话
	 * @param msg 发送消息
	 */
	@Override
	public void onMessageSent(ISession session, Object msg) {

		try {
			filter.onMessageSent(this.preFilter, session, msg);
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}

	/**
	 * 触发下一个业务过滤器的onExceptionCaught事件
	 * @param session 会话
	 * @param exception 异常
	 */
	@Override
	public void onExceptionCaught(ISession session, Throwable exception) {

		filter.onExceptionCaught(this.nextFilter, session, exception);
	}

}
