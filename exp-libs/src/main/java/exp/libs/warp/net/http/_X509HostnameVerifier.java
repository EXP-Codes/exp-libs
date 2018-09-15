package exp.libs.warp.net.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * <PRE>
 * HTTPS： 用于绕过SSL域名校验
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _X509HostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String arg0, SSLSession arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
