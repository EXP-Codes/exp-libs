package exp.libs.warp.net.wsdl.server;

import javax.jws.WebService;  

/**
 * <PRE>
 * Websevices服务提供的API接口定义.
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-20
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
@WebService  	// JAX-WS注解, 必须有
public interface _IWsdlService {  
    
	/**
	 * 自定义接口服务
	 * @param param 样例入参
	 * @return 样例出参
	 */
	public String foo(String param);  
	
	/**
	 * 自定义接口服务
	 * @param param 样例入参
	 */
    public void bar(int param);  
    
}  
