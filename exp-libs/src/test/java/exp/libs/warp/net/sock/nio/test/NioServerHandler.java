package exp.libs.warp.net.sock.nio.test;

import exp.libs.warp.net.sock.nio.common.interfaze.IHandler;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

public class NioServerHandler implements IHandler {

	@Override
	public void onSessionCreated(ISession session) throws Exception {
		session.write("conn success");
	}

	@Override
	public void onMessageReceived(ISession session, Object msg)
			throws Exception {
		System.out.println("S-GET:" + msg);
		session.write("hello client");
	}

	@Override
	public void onExceptionCaught(ISession session, Throwable exception) {
		exception.printStackTrace();
	}

}
