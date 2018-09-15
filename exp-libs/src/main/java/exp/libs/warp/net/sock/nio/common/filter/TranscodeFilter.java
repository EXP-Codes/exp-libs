package exp.libs.warp.net.sock.nio.common.filter;

import exp.libs.envm.Charset;
import exp.libs.warp.net.sock.nio.common.filterchain.INextFilter;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.BaseFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.IConfig;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <pre>
 * 消息转码过滤器，仅适用于字符串的消息转码
 * 
 * 接收消息时：把接收到的消息字符串的recvCharset编码，转码为业务处理时使用的编码
 * 发送消息时：把业务处理时使用的编码，转码为发送消息时的writeCharset编码
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TranscodeFilter extends BaseFilter {

	/**
	 * 内部业务处理时使用的字符集编码
	 */
	private String hdrCharset = Charset.DEFAULT;
	
	/**
	 * 构造函数
	 * @param hdrCharset 内部业务处理时使用的字符集编码
	 */
	public TranscodeFilter(String hdrCharset) {
		this.hdrCharset = hdrCharset;
	}
	
	@Override
	public void onMessageReceived(INextFilter nextFilter, ISession session,
			Object msg) throws Exception {
		
		String recvMsg = msg.toString();
		
		byte[] recvbytes = recvMsg.getBytes(hdrCharset);
		String hdrMsg = new String(recvbytes, hdrCharset);
		
		nextFilter.onMessageReceived(session, hdrMsg);
	}

	@Override
	public void onMessageSent(INextFilter preFilter, ISession session, Object msg)
			throws Exception {

		IConfig conf = session.getConfig();
		String writeCharset = conf.getWriteCharset();
		
		String hdrMsg = msg.toString();
		byte[] sendbytes = hdrMsg.getBytes(writeCharset);
		String sendMsg = new String(sendbytes, writeCharset);
		
		preFilter.onMessageSent(session, sendMsg);
	}
}
