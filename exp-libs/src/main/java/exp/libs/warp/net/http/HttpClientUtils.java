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

import exp.libs.envm.HttpHead;
import exp.libs.utils.io.IOUtils;

/**
 * <PRE>
 * HTTP-Client工具(apache工具)
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
public class HttpClientUtils extends HttpUtils {

	/** 私有化构造函数 */
	protected HttpClientUtils() {}
	
	/**
	 * 添加请求头参数参数
	 * @param method
	 * @param params
	 */
	private static void addParamsToHeader(HttpMethod method, Map<String, String> params) {
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
	private static void addParamsToBody(PostMethod post, Map<String, String> params) {
		if(params != null) {
			Iterator<String> keyIts = params.keySet().iterator();
			while(keyIts.hasNext()) {
				String key = keyIts.next();
				String val = params.get(key);
				post.addParameter(key, val);
			}
		}
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doPost(String url) {
		return doPost(url, null, null);
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doPost(String url, Map<String, String> header, 
			Map<String, String> request) {
		return doPost(url, header, request, DEFAULT_CHARSET);
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doPost(String url, Map<String, String> header, 
			Map<String, String> request, String charset) {
		return doPost(url, header, request, 
				CONN_TIMEOUT, CALL_TIMEOUT, charset);
	}
	
	/**
	 * 提交POST请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doPost(String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) {
		String response = "";
		try {
			response = _doPost(url, header, request, 
					connTimeout, readTimeout, charset);
			
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
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 * @throws Exception
	 */
	private static String _doPost(String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) throws Exception {
		PostMethod post = new PostMethod(url);
		addParamsToHeader(post, header);
		addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的
		
		HttpClient client = createHttpClient(connTimeout, readTimeout);
		int status = client.executeMethod(post);
		String response = (!isResponseOK(status) ? "" : 
			responseAsString(post, charset));
		post.releaseConnection();
		close(client);
		return response;
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doGet(String url) {
		return doGet(url, null, null);
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doGet(String url, Map<String, String> header, 
			Map<String, String> request) {
		return doGet(url, header, request, DEFAULT_CHARSET);
	}
	
	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doGet(String url, Map<String, String> header, 
			Map<String, String> request, String charset) {
		return doGet(url, header, request, 
				CONN_TIMEOUT, CALL_TIMEOUT, charset);
	}

	/**
	 * 提交GET请求
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	public static String doGet(String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) {
		String response = "";
		try {
			response = _doGet(url, header, request, 
					connTimeout, readTimeout, charset);
			
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
	 * @param connTimeout 连接超时（ms）
	 * @param readTimeout 读取超时（ms）
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 * @throws Exception
	 */
	private static String _doGet(String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) throws Exception {
		String kvs = encodeRequests(request, charset);	
		url = HttpUtils.concatGET(url, kvs);	// GET的参数是拼在url后面的
		
		GetMethod get = new GetMethod(url);
		addParamsToHeader(get, header);
		
		HttpClient client = createHttpClient(connTimeout, readTimeout);
		int status = client.executeMethod(get);
		String response = (!isResponseOK(status) ? "" : 
			responseAsString(get, charset));
		get.releaseConnection();
		close(client);
		return response;
	}
	
	/**
	 *  提取HTTP连接的响应结果
	 * @param method 请求方法
	 * @param charset 字符集编码
	 * @return HTTP返回的字符串（包括文本、json、xml等内容）
	 */
	private static String responseAsString(HttpMethod method, String charset) {
		
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
	public static boolean downloadByPost(String savePath, String url) {
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
	public static boolean downloadByPost(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) {
		return downloadByPost(savePath, url, header, request, 
				CONN_TIMEOUT, CALL_TIMEOUT, DEFAULT_CHARSET);
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
	public static boolean downloadByPost(String savePath, String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) {
		boolean isOk = false;
		try {
			isOk = _downloadByPost(savePath, url, header, request, 
					connTimeout, readTimeout, charset);
			
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
	private static boolean _downloadByPost(String savePath, String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) throws Exception {
		PostMethod post = new PostMethod(url);
		addParamsToHeader(post, header);
		addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的
		
		HttpClient client = createHttpClient(connTimeout, readTimeout);
		int status = client.executeMethod(post);
		boolean isOk = (!isResponseOK(status) ? false : 
			responseAsRes(post, savePath));
		post.releaseConnection();
		close(client);
		return isOk;
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public static boolean downloadByGet(String savePath, String url) {
		return downloadByGet(savePath, url, null, null);
	}
	
	/**
	 * 下载资源，适用于返回类型是非文本的响应
	 * @param savePath 包括文件名的保存路径
	 * @param url 资源路径
	 * @param header 请求头参数表
	 * @param request 请求参数表
	 * @return 是否下载成功（下载成功会保存到savePath）
	 */
	public static boolean downloadByGet(String savePath, String url, 
			Map<String, String> header, Map<String, String> request) {
		return downloadByGet(savePath, url, header, request, 
				CONN_TIMEOUT, CALL_TIMEOUT, DEFAULT_CHARSET);
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
	public static boolean downloadByGet(String savePath, String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) {
		boolean isOk = false;
		try {
			isOk = _downloadByGet(savePath, url, header, request, 
					connTimeout, readTimeout, charset);
			
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
	private static boolean _downloadByGet(String savePath, String url, 
			Map<String, String> header, Map<String, String> request, 
			int connTimeout, int readTimeout, String charset) throws Exception {
		String kvs = encodeRequests(request, charset);	
		url = HttpUtils.concatGET(url, kvs);	// GET的参数是拼在url后面的
		
		GetMethod get = new GetMethod(url);
		addParamsToHeader(get, header);
		
		HttpClient client = createHttpClient(connTimeout, readTimeout);
		int status = client.executeMethod(get);
		boolean isOk = (!isResponseOK(status) ? false : 
			responseAsRes(get, savePath));
		get.releaseConnection();
		close(client);
		return isOk;
	}
	
	/**
	 * 保存HTTP资源
	 * @param method 请求方法
	 * @param savePath 包括文件名的保存路径
	 * @return
	 */
	private static boolean responseAsRes(HttpMethod method, String savePath) {
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
	
}