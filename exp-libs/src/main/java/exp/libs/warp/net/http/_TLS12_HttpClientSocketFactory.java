package exp.libs.warp.net.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.ReflectionSocketFactory;
import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <PRE>
 * 此协议工厂适用于 org.apache.commons.httpclient.HttpClient.
 * 
 * 引入BouncyCastle重写通信安全密级协议, 使得JDK1.6和1.7支持TLSv1.2.
 * 注： JDK1.8本已支持TLSv1.2， 无需再使用此方法
 * ---------------------------------------------------------------------------
 * 样例 （只需在连接HTTP前注册_TLS12_HttpClientSocketFactory到https即可）:
 * 
 * String scheme = "https";
 * Protocol sslProtocol = Protocol.getProtocol(scheme);
 * int sslPort = sslProtocol.getDefaultPort();
 * _TLS12_ProtocolSocketFactory sslSocketFactory = new _TLS12_ProtocolSocketFactory();
 * Protocol https = new Protocol(scheme, sslSocketFactory, sslPort);
 * Protocol.registerProtocol(scheme, https);
 * 
 * 
 * String url = "http:// ...URL that only Works in TLSv1.2";
 * HttpClient httpClient = new HttpClient();
 * GetMethod method = new GetMethod(url);
 * client.executeMethod(method);
 * 
 * <PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a>
 * @version   1.0 # 2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _TLS12_HttpClientSocketFactory implements ProtocolSocketFactory {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Adding Custom BouncyCastleProvider
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
			Security.addProvider(new BouncyCastleProvider());
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECURE RANDOM
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private SecureRandom _secureRandom = new SecureRandom();
		
	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort) throws IOException, UnknownHostException {
		Socket socket = new Socket(host, port, localAddress, localPort);
		return _createSSLSocket(socket, host);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		
		if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        
        Socket socket = null;
        if (timeout == 0) {
        	socket = new Socket(host, port, localAddress, localPort);
            
        } else {
            // To be eventually deprecated when migrated to Java 1.4 or above
            socket = ReflectionSocketFactory.createSocket(
                "javax.net.SocketFactory", host, port, localAddress, localPort, timeout);
            if (socket == null) {
                socket = ControllerThreadSocketFactory.createSocket(
                    this, host, port, localAddress, localPort, timeout);
            }
        }
        return _createSSLSocket(socket, host);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		Socket socket = new Socket(host, port);
		return _createSSLSocket(socket, host);
	}
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOCKET CREATION
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private SSLSocket _createSSLSocket(final Socket socket, final String host) throws IOException {
		final TlsClientProtocol tlsClientProtocol = new TlsClientProtocol(
				socket.getInputStream(), socket.getOutputStream(), _secureRandom);
		_TLS12_SSLSocket sslSocket = new _TLS12_SSLSocket(host, tlsClientProtocol);
		sslSocket.startHandshake();	// 在这里必须手动调用 (这是与_TLS12_HttpURLSocketFactory唯一的不同, _TLS12_HttpURLSocketFactory是自动调用的)
		return sslSocket;
	}

}
