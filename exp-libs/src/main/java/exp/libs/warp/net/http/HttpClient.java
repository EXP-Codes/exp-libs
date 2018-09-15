package exp.libs.warp.net.http;

/**
 * <PRE>
 * 封装了Apache-HttpClient.
 *  可以保持连接对象, 并介入获取连接过程中的请求/响应参数.
 * -----------------------------------------------
 *   在JDK1.6、JDK1.7、JDK1.8下使用均支持TLSv1.2
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-21
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HttpClient extends _HttpClient {

	public HttpClient() {
		super();
	}
	
	public HttpClient(String charset, int connTimeout, int callTimeout) {
		super(charset, connTimeout, callTimeout);
	}

}
