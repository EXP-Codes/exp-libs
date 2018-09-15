package exp.libs.warp.net.pf;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.warp.net.sock.io.client.SocketClient;
import exp.libs.warp.net.sock.io.common.IHandler;
import exp.libs.warp.net.sock.io.common.ISession;

class _PFHandler implements IHandler {

	private Logger log = LoggerFactory.getLogger(_PFHandler.class);
	
	/** 端口转发代理服务配置 */
	private PFConfig config;
	
	private String ip;
	
	private int port;
	
	protected _PFHandler(PFConfig config) {
		this.config = config;
		this.ip = config.getRemoteIP();
		this.port = config.getRemotePort();
	}
	
	@Override
	public void _handle(ISession localClient) {
		int overtime = config.getOvertime();
		SocketClient virtualClient = new SocketClient(ip, port, overtime);
		if(!virtualClient.conn()) {
			localClient.close();
			log.warn("会话 [{}] 连接到真实服务端口 [{}:{}] 失败", 
					localClient.ID(), ip, port);
			return;
		}
		
		// 转发本地真实客户端的IO流到虚拟客户端
		Socket localSocket = localClient.getSocket();
		Socket virtualSocket = virtualClient.getSocket();
		new _TranslateData(localClient.ID(), _TranslateData.TYPE_REQUEST, 
				overtime, localSocket, virtualSocket).start();	// 请求转发
		new _TranslateData(localClient.ID(), _TranslateData.TYPE_RESPONE, 
				overtime, virtualSocket, localSocket).start();	// 响应转发
	}

	@Override
	public IHandler _clone() {
		return new _PFHandler(config);
	}

	@Deprecated
	@Override
	public boolean _login(ISession session) {
		return true;
	}

}
