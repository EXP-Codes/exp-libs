package exp.libs.warp.net.sock.nio.common.filterchain.impl;

import java.nio.ByteBuffer;

import exp.libs.warp.net.sock.nio.common.interfaze.IConfig;
import exp.libs.warp.net.sock.nio.common.interfaze.IFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 过滤链链头
 * 
 * 事件onSessionCreated、onMessageReceived的入口
 * 事件onMessageSent的出口
 * 数据在过滤链中遇到异常事件onExceptionCaught时，将通过过滤链直接把异常抛回到链头处理
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final class HeadFilter extends BaseNextFilter {

	/**
	 * 构造函数
	 * @param filter 业务过滤器
	 */
	public HeadFilter(IFilter filter) {
		super(filter);
	}

	/**
	 * <pre>
	 * 过滤链的开头，消息处理流程的入口，接收远端机的数据
	 * 
	 * 若接收到的消息为断开连接的命令，则会在此处进行关闭会话操作。
	 * 否则会把消息交付给下一个业务过滤器
	 * </pre>
	 * @param session 会话
	 * @param msg 消息
	 */
	@Override
	public void onMessageReceived(ISession session, Object msg) {
		try {
			
			//若对方发来断开连接的命令消息，则直接断开连接
			if(session.getConfig().getExitCmd().equals(msg.toString().trim())) {
				session.close();
				
			} else {
				filter.onMessageReceived(this.nextFilter, session, msg);
			}
			
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}
	
	/**
	 * <pre>
	 * 过滤链的末尾，消息处理流程的出口，向远端机的发送数据
	 * 
	 * 发送的消息编码为本地机的字符集编码，至于对方怎么处理则不理会，建议通过过滤器转换对方的编码
	 * </pre>
	 * @param session 会话
	 * @param msg 消息
	 */
	@Override
	public void onMessageSent(ISession session, Object msg) {

		try {
			synchronized (session) {
				if(session.isClosed()) {
					return;
				}
			
				IConfig conf = session.getConfig();
				String sendMsg = msg.toString();
				
				sendMsg = sendMsg.concat(conf.getWriteDelimiter());
				byte[] byteMsg = sendMsg.getBytes(conf.getWriteCharset());
				
				ByteBuffer sendBuffer = ByteBuffer.wrap(byteMsg);
				while (sendBuffer.hasRemaining()) {
					session.getLayerSession().write(sendBuffer);
				}
			}
		} catch (Exception e) {
			this.fireExceptionCaught(session, e);
		}
	}
	
}
