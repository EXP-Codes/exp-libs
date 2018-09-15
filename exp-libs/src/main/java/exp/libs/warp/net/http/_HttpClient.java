package exp.libs.warp.net.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.envm.HttpHead;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.io.IOUtils;

/**
 * <PRE>
 * 封装了Apache-HttpClient.
 *  可以保持连接对象, 并介入获取连接过程中的请求/响应参数
 * -----------------------------------------------
 *   在JDK1.6、JDK1.7、JDK1.8下使用均支持TLSv1.2
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _HttpClient {

	/** 日志器 */
	protected final static Logger log = LoggerFactory.getLogger(_HttpClient.class);
	
	/** GET请求方法名 */
	public final static String METHOD_GET = "GET";
	
	/** POST请求方法名 */
	public final static String METHOD_POST = "POST";
	
	/** 默认编码 */
	public final static String DEFAULT_CHARSET = Charset.UTF8;
	
	/** 连接超时, 默认1分钟 */
	public final static int CONN_TIMEOUT = 60000;

	/** 响应/读取超时 , 默认1分钟 */
	public final static int CALL_TIMEOUT = 60000;
	
	protected String charset;
	
	protected int connTimeout;
	
	protected int callTimeout;
	
	protected HttpClient client;
	
	/** 最近一次请求的Method */
	private HttpMethod method;
	
	protected _HttpClient() {
		this(DEFAULT_CHARSET, CONN_TIMEOUT, CALL_TIMEOUT);
	}
	
	/**
	 * 
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 */
	protected _HttpClient(String charset, int connTimeout, int callTimeout) {
		this.charset = (CharsetUtils.isVaild(charset) ? charset : DEFAULT_CHARSET);
		this.connTimeout = (connTimeout < 0 ? CONN_TIMEOUT : connTimeout);
		this.callTimeout = (callTimeout < 0 ? CALL_TIMEOUT : callTimeout);
		this.client = HttpUtils.createHttpClient(this.connTimeout, this.callTimeout);
	}
	
	/**
	 * 获取最近一次请求的Method: 
	 * 	可用来获取服务端返回的ResponseHeader等参数
	 * @return 最近一次请求的Method(可能返回null)
	 */
	public HttpMethod getHttpMethod() {
		return method;
	}
	
	private void reset(HttpMethod method) {
		release();
		this.method = method;
	}
	
	private void release() {
		if(method != null) {
			method.releaseConnection();
			method = null;
		}
	}
	
	/**
	 * 释放HTTP资源
	 */
	public void close() {
		release();
		HttpUtils.close(client);
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public String doPost(String url) {
		return doPost(url, null, null);
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public String doPost(String url, 
			Map<String, String> header, Map<String, String> request) {
		String response = "";
		try {
			response = _doPost(url, header, request);
			
		} catch(Exception e) {
			log.error("提交{}请求失败: [{}]", METHOD_POST, url, e);
		}
		return response;
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 * @throws Exception
	 */
	private String _doPost(String url, Map<String, String> header, 
			Map<String, String> request) throws Exception {
		PostMethod post = new PostMethod(url);
		addParamsToHeader(post, header);
		addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的
		reset(post);
		
		int status = client.executeMethod(post);
		String response = (!HttpUtils.isResponseOK(status) ? 
				"" : responseAsString(post));
		return response;
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public String doGet(String url) {
		return doGet(url, null, null);
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public String doGet(String url, 
			Map<String, String> header, Map<String, String> request) {
		String response = "";
		try {
			response = _doGet(url, header, request);
			
		} catch(Exception e) {
			log.error("提交{}请求失败: [{}]", METHOD_GET, url, e);
		}
		return response;
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 * @throws Exception
	 */
	private String _doGet(String url, Map<String, String> header, 
			Map<String, String> request) throws Exception {
		String kvs = HttpUtils.encodeRequests(request, charset);	
		url = HttpUtils.concatGET(url, kvs);	// GET的参数是拼在url后面的
		
		GetMethod get = new GetMethod(url);
		addParamsToHeader(get, header);
		reset(get);
		
		int status = client.executeMethod(get);
		String response = (!HttpUtils.isResponseOK(status) ? 
				"" : responseAsString(get));
		return response;
	}
	
	/**
	 *  提取HTTP连接的响应结果
	 * @param method 请求方法
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	private String responseAsString(HttpMethod method) {
		
		// 检测返回的内容是否使用gzip压缩过
		Header header = method.getResponseHeader(HttpHead.KEY.CONTENT_ENCODING);
		boolean isGzip = (header == null ? false : 
			HttpHead.VAL.GZIP.equalsIgnoreCase(header.getValue()));

		String response = "";
		try {
			InputStreamReader is = isGzip ? 
					new InputStreamReader(new GZIPInputStream(method.getResponseBodyAsStream()), charset) : 
					new InputStreamReader(method.getResponseBodyAsStream(), charset);
			response = IOUtils.toStr(is);
			is.close();
			
		} catch (Exception e) {
			log.error("获取HTTP响应结果失败", e);
		}
		return response;
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public boolean downloadByPost(String savePath, String url) {
		return downloadByPost(savePath, url, null, null);
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public boolean downloadByPost(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) {
		boolean isOk = false;
		try {
			isOk = _downloadByPost(savePath, url, header, request);
			
		} catch (Exception e) {
			log.error("下载资源失败: [{}]", url, e);
		}
		return isOk;
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return 是否下载成功（下载成功会保存到savePath）
	 * @throws Exception
	 */
	private boolean _downloadByPost(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) 
					throws Exception {
		PostMethod post = new PostMethod(url);
		addParamsToHeader(post, header);
		addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的
		reset(post);
		
		int status = client.executeMethod(post);
		boolean isOk = (!HttpUtils.isResponseOK(status) ? false : 
			responseAsRes(post, savePath));
		return isOk;
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public boolean downloadByGet(String savePath, String url) {
		return downloadByGet(savePath, url, null, null);
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public boolean downloadByGet(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) {
		boolean isOk = false;
		try {
			isOk = _downloadByGet(savePath, url, header, request);
			
		} catch (Exception e) {
			log.error("下载资源失败: [{}]", url, e);
		}
		return isOk;
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return 是否下载成功（下载成功会保存到savePath）
	 * @throws Exception
	 */
	private boolean _downloadByGet(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) 
					throws Exception {
		String kvs = HttpUtils.encodeRequests(request, charset);	
		url = HttpUtils.concatGET(url, kvs);	// GET的参数是拼在url后面的
		
		GetMethod get = new GetMethod(url);
		addParamsToHeader(get, header);
		reset(get);
		
		int status = client.executeMethod(get);
		boolean isOk = (!HttpUtils.isResponseOK(status) ? false : 
			responseAsRes(get, savePath));
		return isOk;
	}
	

	/**
	 * 保存HTTP资源
	 * @param method 请求方法
	 * @param savePath 包括文件名的保存路径
	 * @return
	 */
	private boolean responseAsRes(HttpMethod method, String savePath) {
		boolean isOk = false;
		try {
			InputStream is = method.getResponseBodyAsStream();
			isOk = IOUtils.toFile(is, savePath);
			is.close();

		} catch (Exception e) {
			log.error("保存资源 [{}] 失败", savePath, e);
		}
		return isOk;
	}
	
	/**
	 * 添加请求头参数参数
	 * @param method
	 * @param params
	 */
	private void addParamsToHeader(HttpMethod method, Map<String, String> params) {
		if(params != null) {
			Iterator<String> keyIts = params.keySet().iterator();
			while(keyIts.hasNext()) {
				String key = keyIts.next();
				String val = params.get(key);
				method.addRequestHeader(key, val);
			}
		}
	}
	
	/**
	 * 添加post方法的请求参数
	 * @param post
	 * @param params
	 */
	private void addParamsToBody(PostMethod post, Map<String, String> params) {
		if(params != null) {
			Iterator<String> keyIts = params.keySet().iterator();
			while(keyIts.hasNext()) {
				String key = keyIts.next();
				String val = params.get(key);
				post.addParameter(key, val);
			}
		}
	}
	
}
