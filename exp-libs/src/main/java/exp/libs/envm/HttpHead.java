package exp.libs.envm;

/**
 * <PRE>
 * HTTP请求头参数枚举, 请求头样例:
 * 
 * Accept:application/json, text/javascript;
 * Accept-Encoding:gzip, deflate, br
 * Accept-Language:zh-CN,zh;q=0.8,en;q=0.6
 * Connection:keep-alive
 * Content-Length:68
 * Content-Type:application/x-www-form-urlencoded; charset=UTF-8
 * Cookie:l=v; sid=891ab0o9; fts=1497517922; buvid3=0EE9E160-55FF-4EA6-9B8A-D37EAB81B76927368infoc; UM_distinctid=15d88e8e6da42-01bb282a267083-414a0229-100200-15d88e8e6db5a; pgv_pvi=958242816; rpdid=olwiqlmmxldoswoipwsxw; LIVE_BUVID=ebf2cce7237945227c579bec3e986459; LIVE_BUVID__ckMd5=08a5ee5cfdbdd99f; biliMzIsnew=1; biliMzTs=0; im_seqno_1650868=9548; DedeUserID=1650868; DedeUserID__ckMd5=686caa22740f2663; SESSDATA=e6e4328c%2C1515920104%2C162b21cd; bili_jct=9db6a9c26d414e848430dac8f7c2ea9b; finger=81df3ec0; Hm_lvt_8a6d461cf92ec46bd14513876885e489=1513755792; _dfcaptcha=f63bb4f803bb08dc6b7427b0afee793e; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1513749373,1513755780,1513756902,1513817560; Hm_lpvt_8a6e55dbd2870f0f5bc9194cddf32a02=1513820331
 * Host:api.live.bilibili.com
 * Origin:http://live.bilibili.com
 * Referer:http://live.bilibili.com/269706
 * User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HttpHead {

	/** 键枚举 */
	public class KEY {
		
		public final static String ACCEPT = "Accept";
		
		public final static String ACCEPT_ENCODING = "Accept-Encoding";
		
		public final static String ACCEPT_LANGUAGE = "Accept-Language";
		
		public final static String CONNECTION = "Connection";
		
		public final static String CONTENT_TYPE = "Content-Type";
		
		public final static String COOKIE = "Cookie";
		
		public final static String HOST = "Host";
		
		public final static String ORIGIN = "Origin";
		
		public final static String REFERER = "Referer";
		
		public final static String USER_AGENT = "User-Agent";
		
		public final static String CONTENT_ENCODING = "Content-Encoding";
		
		public final static String SET_COOKIE = "Set-Cookie";
	}
	
	/** 值枚举 */
	public class VAL {
		
		/** 浏览器代理头标识: 假装是Mac，可避免被反爬 */
		public final static String USER_AGENT = 
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
//				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
		
		/** 请求GET的数据是text (需补充字符集) */
		public final static String GET_TXT = 
				"text/xml; charset=";
		
		/** 请求POST的数据是xml (需补充字符集) */
		public final static String POST_XML = 
				"application/x-javascript text/xml; charset=";
		
		/** 请求POST的数据是json (需补充字符集) */
		public final static String POST_JSON = 
				"application/x-javascript; charset=";
		
		/** 请求POST的数据是表单 (需补充字符集) */
		public final static String POST_FORM = 
				"application/x-www-form-urlencoded; charset=";
		
		/** 响应数据编码: gizp */
		public final static String GZIP = "gzip";
		
	}
	
}
