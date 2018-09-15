package exp.libs.warp.net.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <PRE>
 * 此协议工厂适用于 javax.net.ssl.HttpsURLConnection.
 * 
 * 引入BouncyCastle重写通信安全密级协议, 使得JDK1.6和1.7支持TLSv1.2.
 * 注： JDK1.8本已支持TLSv1.2， 无需再使用此方法
 * ---------------------------------------------------------------------------
 * 样例:
 * 
 * URL url = new URL( "https:// ...URL that only Works in TLSv1.2" );
 * HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
 * conn.setSSLSocketFactory(new _TLS12_HttpURLSocketFactory());  
 * 
 * <PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a>
 * @version   1.0 # 2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _TLS12_HttpURLSocketFactory extends SSLSocketFactory {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Adding Custom BouncyCastleProvider
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
			Security.addProvider(new BouncyCastleProvider());
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// HANDSHAKE LISTENER
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class TLSHandshakeListener implements HandshakeCompletedListener {
		@Override
		public void handshakeCompleted(HandshakeCompletedEvent event) {

		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECURE RANDOM
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private SecureRandom _secureRandom = new SecureRandom();

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Adding Custom BouncyCastleProvider
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Socket createSocket(Socket socket, final String host, int port,
			boolean arg3) throws IOException {
		if (socket == null) {
			socket = new Socket();
		}
		if (!socket.isConnected()) {
			socket.connect(new InetSocketAddress(host, port));
		}

		final TlsClientProtocol tlsClientProtocol = new TlsClientProtocol(
				socket.getInputStream(), socket.getOutputStream(),
				_secureRandom);
		return _createSSLSocket(host, tlsClientProtocol);

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOCKET FACTORY METHODS
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String[] getDefaultCipherSuites() {
		return null;
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return null;
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return null;
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return null;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost,
			int localPort) throws IOException, UnknownHostException {
		return null;
	}

	@Override
	public Socket createSocket(InetAddress address, int port,
			InetAddress localAddress, int localPort) throws IOException {
		return null;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOCKET CREATION
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private SSLSocket _createSSLSocket(final String host,
			final TlsClientProtocol tlsClientProtocol) {
		return new _TLS12_SSLSocket(host, tlsClientProtocol);
	}
	
}
