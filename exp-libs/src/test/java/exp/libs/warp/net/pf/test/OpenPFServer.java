package exp.libs.warp.net.pf.test;

import exp.libs.warp.net.pf.PFAgent;


public class OpenPFServer extends Thread {
	
	public static void main(String[] args) {
		// 获取本地监听端口、远程IP和远程端口
		int localPort = 9999;
		String remoteIP = "172.168.10.63";
		int remotePort = 1521;
//		int overtime = 10000;
//		int maxConn = 100;
		
		PFAgent agent = new PFAgent(localPort, remoteIP, remotePort);
		agent._start();
	}
	
}
