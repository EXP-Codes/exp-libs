package exp.libs.warp.net.websock.interfaze;

import exp.libs.warp.net.websock.bean.Frame;

/**
 * <pre>
 * WebSocket客户端会话接口
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-21
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public interface ISession {

	/**
	 * 设置本地连接保活超时（0不生效，默认60，即60秒后自动断开）
	 * @param keepTimeout 保活超时时间, 单位:秒
	 */
	public void setKeepTimeout(int keepTimeout);
	
	/**
	 * 切换调试模式
	 * @param debug 调试模式
	 */
	public void debug(boolean debug);
	
	/**
	 * 检查WebSocket连接是否连接中
	 * @return true:连接存活; fase:连接已断开
	 */
	public boolean isConnecting();
	
	/**
	 * 向服务器发送数据帧
	 * @param frame 数据帧
	 */
	public void send(Frame frame);
	
}
