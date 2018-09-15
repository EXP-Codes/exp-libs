package exp.libs.warp.net.sock.io.test;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.io.server.SocketServer;

public class TestSocketServer {

	public static void main(String[] args) {
		SocketBean sb = new SocketBean();
		sb.setIp("127.0.0.1");
		sb.setPort(9998);
		
		SocketServer server = new SocketServer(sb, null);
		server._start();
	}
	
}
