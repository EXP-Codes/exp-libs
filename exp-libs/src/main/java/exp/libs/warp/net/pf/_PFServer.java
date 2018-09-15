package exp.libs.warp.net.pf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.io.server.SocketServer;

class _PFServer {
	
	private Logger log = LoggerFactory.getLogger(_PFServer.class);

	private final static String LOCAL_IP = "0.0.0.0";
	
	/** 端口转发代理服务配置 */
	private PFConfig config;
	
	/** 本地端口侦听服务 */
	private SocketServer listenServer;
	
	protected _PFServer(PFConfig config) {
		this.config = config;
		
		SocketBean localSockBean = new SocketBean(); {
			localSockBean.setId(config.getServerName());
			localSockBean.setIp(LOCAL_IP);
			localSockBean.setPort(config.getLocalListenPort());
			localSockBean.setOvertime(config.getOvertime());
			localSockBean.setMaxConnectionCount(config.getMaxConn());
		}
		
		SocketBean remoteSockBean = new SocketBean(); {
			remoteSockBean.setIp(config.getRemoteIP());
			remoteSockBean.setPort(config.getRemotePort());
		}
		
		_PFHandler pfHandler = new _PFHandler(config);
		this.listenServer = new SocketServer(localSockBean, pfHandler);
	}
	
	protected boolean _start() {
		boolean isOk = listenServer._start();
		if(isOk == true) {
			log.info("端口转发服务 [{}] 启动成功: 本地侦听端口 [{}], 转发端口: [{}:{}]",
					config.getServerName(), config.getLocalListenPort(), 
					config.getRemoteIP(), config.getRemotePort());
			
		} else {
			log.warn("端口转发服务 [{}] 启动失败", config.getServerName());
		}
		return isOk;
	}
	
	protected void _stop() {
		listenServer._stop();
		log.info("端口转发服务 [{}] 已停止", config.getServerName());
	}
	
}
