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
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */

// 注解中必须指定接口类位置, 否则会报错 "Could not load Webservice SEI" 
@WebService(endpointInterface="exp.libs.warp.net.wsdl.server._IWsdlService")  
public class _WsdlServiceImpl implements _IWsdlService {

	@Override
	public String foo(String param) {
		return "webservice-demo : foo-".concat(param);
	}

	@Override
	public void bar(int param) {
		System.out.println("webservice-demo : bar-" + param);
	}

}  