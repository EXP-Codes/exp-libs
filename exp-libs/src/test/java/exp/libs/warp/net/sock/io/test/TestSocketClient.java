package exp.libs.warp.net.sock.io.test;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.io.client.SocketClient;

public class TestSocketClient {

	public static void main(String[] args) {
		SocketBean sb = new SocketBean();
		sb.setIp("127.0.0.1");
		sb.setPort(9998);
		
		SocketClient client = new SocketClient(sb);
		client.conn();
		System.out.println(client.read());
		client.close();
	}
	
}
