package exp.libs.warp.net.sock.nio.common.filterchain.impl;

import exp.libs.warp.net.sock.nio.common.handler.BaseHandler;
import exp.libs.warp.net.sock.nio.common.interfaze.IFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.IHandler;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 过滤链链尾
 * 
 * 事件onSessionCreated、onMessageReceived的出口
 * 事件onSessionCreated 与 onMessageReceived会交付处理器handler处理
 * onMessageSent事件则把消息返回到客户端
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
final class TailFilter extends BaseNextFilter {

	/**
	 * 业务处理器
	 */
	private IHandler handler;
	
	/**
	 * 基本业务处理器
	 */
	private BaseHandler bHandler;

	/**
	 * 构造函数
	 * @param filter 业务过滤器
	 */
	public TailFilter(IFilter filter) {
		super(filter);
	}

	/**
	 * 触发业务处理器的onSessionCreated接口
	 * @param session 会话
	 */
	@Override
	public void onSessionCreated(ISession session) {

		try {
			this.bHandler.onSessionCreated(session);
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}

	/**
	 * 触发业务处理器的onMessageReceived接口
	 * @param session 会话
	 * @param msg 接收消息
	 */
	@Override
	public void onMessageReceived(ISession session, Object msg) {

		try {
			this.bHandler.onMessageReceived(session, msg);
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}

	/**
	 * 触发业务处理器的onExceptionCaught接口
	 * @param session 会话
	 * @param exception 异常
	 */
	@Override
	public void onExceptionCaught(ISession session, Throwable exception) {
		this.bHandler.onExceptionCaught(session, exception);
	}

	/**
	 * 设置业务处理器，并将其封装到基本业务处理器中，以在客户的操作前后附加服务器的默认操作
	 * @param handler 业务处理器
	 */
	public void setHandler(IHandler handler) {
		this.handler = handler;
		this.bHandler = new BaseHandler(this.handler);
	}

}
