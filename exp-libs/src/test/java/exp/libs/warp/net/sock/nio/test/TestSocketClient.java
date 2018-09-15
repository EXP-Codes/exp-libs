package exp.libs.warp.net.sock.nio.test;

import exp.libs.utils.os.ThreadUtils;
import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.nio.client.NioSocketClient;

public class TestSocketClient {

	public static void main(String[] args) {
		SocketBean sb = new SocketBean();
		sb.setIp("127.0.0.1");
		sb.setPort(9998);
		
		NioSocketClient client = new NioSocketClient(sb, new NioClientHandler());
		if(client.conn()) {
			client.write("hello server");
		}
		
		ThreadUtils.tSleep(5000);
		client.close();
	}
	
}
