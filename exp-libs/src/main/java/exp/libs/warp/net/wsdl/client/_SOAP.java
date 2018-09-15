package exp.libs.warp.net.wsdl.client;

/**
 * <PRE>
 * SOAP协议版本枚举
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-20
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class _SOAP {

	/** soap协议版本:1.1 */
	protected final static _SOAP _1_1 = new _SOAP("SOAP 1.1", 
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"");
	
	/** soap协议版本:1.2 (默认soap协议, 但JDK1.6不支持) */
	protected final static _SOAP _1_2 = new _SOAP("SOAP 1.2", 
			"xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"");
	
	/** 版本 */
	private String version;
	
	/** 声明 */
	private String statement;
	
	private _SOAP(String version, String statement) {
		this.version = version;
		this.statement = statement;
	}
	
	protected String VER() {
		return version;
	}
	
	protected String STATEMENT() {
		return statement;
	}
	
}
