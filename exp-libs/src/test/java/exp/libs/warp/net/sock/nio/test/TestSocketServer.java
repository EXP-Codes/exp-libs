package exp.libs.warp.net.sock.nio.test;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.nio.server.NioSocketServer;

public class TestSocketServer {

	public static void main(String[] args) {
		SocketBean sb = new SocketBean();
		sb.setIp("127.0.0.1");
		sb.setPort(9998);
		
		NioSocketServer server = new NioSocketServer(sb, new NioServerHandler());
		server._start();
	}
	
}
