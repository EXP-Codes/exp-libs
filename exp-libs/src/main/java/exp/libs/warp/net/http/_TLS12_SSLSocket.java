package exp.libs.warp.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.security.cert.X509Certificate;

import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.crypto.tls.TlsCredentials;

/**
 * <PRE>
 * HTTPS： SSLSocket
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a>
 * @version   1.0 # 2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _TLS12_SSLSocket extends SSLSocket {

	private java.security.cert.Certificate[] peertCerts;

	private final String host;

	private final TlsClientProtocol tlsClientProtocol;

	protected _TLS12_SSLSocket(final String host,
			final TlsClientProtocol tlsClientProtocol) {
		this.host = host;
		this.tlsClientProtocol = tlsClientProtocol;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return tlsClientProtocol.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return tlsClientProtocol.getOutputStream();
	}

	@Override
	public synchronized void close() throws IOException {
		tlsClientProtocol.close();
	}

	@Override
	public void addHandshakeCompletedListener(HandshakeCompletedListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean getEnableSessionCreation() {
		return false;
	}

	@Override
	public String[] getEnabledCipherSuites() {
		return null;
	}

	@Override
	public String[] getEnabledProtocols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getNeedClientAuth() {
		return false;
	}

	@Override
	public SSLSession getSession() {
		return new SSLSession() {

			@Override
			public int getApplicationBufferSize() {
				return 0;
			}

			@Override
			public String getCipherSuite() {
				throw new UnsupportedOperationException();
			}

			@Override
			public long getCreationTime() {
				throw new UnsupportedOperationException();
			}

			@Override
			public byte[] getId() {
				throw new UnsupportedOperationException();
			}

			@Override
			public long getLastAccessedTime() {
				throw new UnsupportedOperationException();
			}

			@Override
			public java.security.cert.Certificate[] getLocalCertificates() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Principal getLocalPrincipal() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int getPacketBufferSize() {
				throw new UnsupportedOperationException();
			}

			@Override
			public X509Certificate[] getPeerCertificateChain()
					throws SSLPeerUnverifiedException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public java.security.cert.Certificate[] getPeerCertificates()
					throws SSLPeerUnverifiedException {
				return peertCerts;
			}

			@Override
			public String getPeerHost() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int getPeerPort() {
				return 0;
			}

			@Override
			public Principal getPeerPrincipal()
					throws SSLPeerUnverifiedException {
				return null;
				// throw new UnsupportedOperationException();

			}

			@Override
			public String getProtocol() {
				throw new UnsupportedOperationException();
			}

			@Override
			public SSLSessionContext getSessionContext() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object getValue(String arg0) {
				throw new UnsupportedOperationException();
			}

			@Override
			public String[] getValueNames() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void invalidate() {
				throw new UnsupportedOperationException();

			}

			@Override
			public boolean isValid() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void putValue(String arg0, Object arg1) {
				throw new UnsupportedOperationException();

			}

			@Override
			public void removeValue(String arg0) {
				throw new UnsupportedOperationException();

			}

		};
	}

	@Override
	public String[] getSupportedProtocols() {
		return null;
	}

	@Override
	public boolean getUseClientMode() {
		return false;
	}

	@Override
	public boolean getWantClientAuth() {
		return false;
	}

	@Override
	public void removeHandshakeCompletedListener(HandshakeCompletedListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnableSessionCreation(boolean arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnabledCipherSuites(String[] arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnabledProtocols(String[] arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setNeedClientAuth(boolean arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setUseClientMode(boolean arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setWantClientAuth(boolean arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return null;
	}

	@Override
	public void startHandshake() throws IOException {
		tlsClientProtocol.connect(new DefaultTlsClient() {
			@SuppressWarnings("unchecked")
			@Override
			public Hashtable<Integer, byte[]> getClientExtensions()
					throws IOException {
				Hashtable<Integer, byte[]> clientExtensions = super.getClientExtensions();
				if (clientExtensions == null) {
					clientExtensions = new Hashtable<Integer, byte[]>();
				}

				// Add host_name
				byte[] host_name = host.getBytes();

				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final DataOutputStream dos = new DataOutputStream(baos);
				dos.writeShort(host_name.length + 3); // entry size
				dos.writeByte(0); // name type = hostname
				dos.writeShort(host_name.length);
				dos.write(host_name);
				dos.close();
				clientExtensions.put(ExtensionType.server_name, baos.toByteArray());
				return clientExtensions;
			}

			@Override
			public TlsAuthentication getAuthentication() throws IOException {
				return new TlsAuthentication() {

					@Override
					public void notifyServerCertificate(
							Certificate serverCertificate) throws IOException {

						try {
							CertificateFactory cf = CertificateFactory.getInstance("X.509");
							List<java.security.cert.Certificate> certs = new LinkedList<java.security.cert.Certificate>();
							for (org.bouncycastle.asn1.x509.Certificate c : serverCertificate
									.getCertificateList()) {
								certs.add(cf.generateCertificate(new ByteArrayInputStream(c.getEncoded())));
							}
							peertCerts = certs.toArray(new java.security.cert.Certificate[0]);

						} catch (CertificateException e) {
							System.out.println("Failed to cache server certs" + e);
							throw new IOException(e);
						}

					}

					@Override
					public TlsCredentials getClientCredentials(
							CertificateRequest arg0) throws IOException {
						return null;
					}

				};

			}

		});

	}

}
