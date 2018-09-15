package exp.libs.warp.net.wsdl.server;

import javax.xml.ws.Endpoint;

/**
 * <PRE>
 * 此类并不是一个发布Websevices的API. 
 * 此类的作用是【演示】如何通过 JAX-WS 发布一个超轻量级的Websevices服务.
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-20
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WsdlServerDemo {  
	
	/** 自定义发布Websevices的地址 */
	public final static String WSDL_URL = "http://127.0.0.1:8080/wsdemo?wsdl";
	
	/**
	 * <pre>
	 * 发布服务.
	 *   发布服务后，建议切到soapUI捕获此服务, 然后停掉此处的服务, 就可以在soapUI调试.
	 * </pre>
	 * @param args
	 */
    public static void main(String[] args) {
    	Endpoint.publish(WSDL_URL, new _WsdlServiceImpl());
    }
    
}  
