package exp.libs.warp.net.sock.io.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.thread.ThreadPool;

/**
 * <pre>
 * 预备线程.
 * 	用于异步处理Socket客户端的登陆操作
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _ReserveThread implements Runnable {

	private Logger log = LoggerFactory.getLogger(_ReserveThread.class);
	
	private final String serverName;
	
	private final ThreadPool execPool;
	
	private final List<_SocketClientProxy> clientProxys;
	
	private final _SocketClientProxy clientProxy;
	
	protected _ReserveThread(final String serverName, final ThreadPool execPool, 
			final List<_SocketClientProxy> clientProxys, 
			final _SocketClientProxy clientProxy) {
		this.serverName = serverName;
		this.execPool = execPool;
		this.clientProxys = clientProxys;
		this.clientProxy = clientProxy;
	}
	
	@Override
	public void run() {
		boolean isOk = clientProxy.login();	// 客户端注册（阻塞操作）
		if(isOk == true) {
			clientProxys.add(clientProxy);	// 放入客户端会话队列
			execPool.execute(clientProxy);	// 放入客户端业务逻辑执行线程池
		} else {
//			clientProxy.write("[ERROR] REFUSE TO LOGIN");
			clientProxy.close();
		}
		
		log.debug("Socket服务 [{}] 注册新会话 [{}] {}, 当前活动会话数: [{}]", serverName, 
				clientProxy.ID(), (isOk ? "成功" : "失败"), clientProxys.size());
	}
	
}
