package exp.libs.warp.net.sock.nio.common.envm;

/**
 * <pre>
 * NioSocket 客户端和服务端内部默认的通信协议报文
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Protocol {

	/** 连接受限 */
	public final static String CONN_LIMIT = "Connected limited number";
	
	/** 未处理消息数受限 */
	public final static String MSG_LIMIT = "Undo Msg limited number";
	
	/** 周期心跳 */
	public final static String HEARTBEAT = "Regular Heartbeat";
	
}
