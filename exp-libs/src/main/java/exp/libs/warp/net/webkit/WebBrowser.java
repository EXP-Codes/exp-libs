package exp.libs.warp.net.webkit;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import exp.libs.envm.HttpHead;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.cmd.CmdUtils;

/**
 * <PRE>
 * 仿真浏览器接口
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WebBrowser {

	/** 等待动态元素的加载时间(单位:s) */
	private final static long WAIT_ELEMENT_SECOND = 5;
	
	/** WEB驱动类型 */
	private WebDriverType type;
	
	/** WEB驱动 */
	private WebDriver driver;
	
	/**
	 * 构造函数
	 * @param type WEB驱动类型
	 */
	public WebBrowser(WebDriverType type) {
		this(type, false, WAIT_ELEMENT_SECOND);
	}
	
	/**
	 * 构造函数
	 * @param type WEB驱动类型
	 * @param loadImages 是否加载图片(默认不加载)
	 */
	public WebBrowser(WebDriverType type, boolean loadImages) {
		this(type, loadImages, WAIT_ELEMENT_SECOND);
	}

	/**
	 * 构造函数
	 * @param type WEB驱动类型
	 * @param loadImages 是否加载图片(默认不加载)
	 * @param waitElementSecond 等待动态元素的加载时间(单位:s)
	 */
	public WebBrowser(WebDriverType type, boolean loadImages, long waitElementSecond) {
		this.type = type;
		waitElementSecond = (waitElementSecond <= 0 ? 
				WAIT_ELEMENT_SECOND : waitElementSecond);
		
		setWebPropertyToSysEnv();
		initWebDriver(loadImages);
		setWaitElementTime(waitElementSecond);
	}
	
	/**
	 * 设置WEB属性到系统环境
	 */
	private void setWebPropertyToSysEnv() {
		String property = "";
		if(WebDriverType.PHANTOMJS == type) {
			property = PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY;
			
		} else if(WebDriverType.CHROME == type) {
			property = ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
			
		} else {
			// Undo
		}
		
		if(StrUtils.isNotEmpty(property)) {
			System.setProperty(property, type.DRIVER_PATH());
		}
	}
	
	/**
	 * 初始化WEB驱动参数
	 * @param loadImages 是否加载图片(默认不加载)
	 */
	public void initWebDriver(boolean loadImages) {
		if(WebDriverType.PHANTOMJS == type) {
			this.driver = new PhantomJSDriver(getCapabilities(loadImages));
			
		} else if(WebDriverType.CHROME == type) {
			this.driver = new ChromeDriver(getCapabilities(loadImages));
			
		} else {
			this.driver = new HtmlUnitDriver(getCapabilities(loadImages));
		}
	}
	
	/**
	 * 获取WEB驱动的能力参数
	 * @param loadImages
	 * @return
	 */
	private DesiredCapabilities getCapabilities(boolean loadImages) {
		DesiredCapabilities capabilities = null;
		
		if(WebDriverType.PHANTOMJS == type) {
			capabilities = DesiredCapabilities.phantomjs();
			capabilities.setJavascriptEnabled(true);	// 执行页面js脚本
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);	// SSL证书支持
			
			final String PAGE_SETTINGS = PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX;
			capabilities.setCapability(PAGE_SETTINGS.concat("loadImages"), loadImages);		// 加载图片
			capabilities.setCapability(PAGE_SETTINGS.concat("XSSAuditingEnabled"), false);	// 跨域请求监控
			capabilities.setCapability(PAGE_SETTINGS.concat("localToRemoteUrlAccessEnabled"), false);	// 本地资源是否可以访问远程URL
			capabilities.setCapability(PAGE_SETTINGS.concat("userAgent"), HttpHead.VAL.USER_AGENT);	// 伪装浏览器
			
//			final String HERDER = PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX;
//			capabilities.setCapability(HERDER.concat("Accept"), "application/json, text/javascript, */*; q=0.01");
//			capabilities.setCapability(HERDER.concat("Content-Type"), "application/x-www-form-urlencoded; charset=UTF-8");
//			capabilities.setCapability(HERDER.concat("Accept-Encoding"), "gzip, deflate, br");
//			capabilities.setCapability(HERDER.concat("Accept-Language"), "zh-CN,zh;q=0.8");
			
		} else if(WebDriverType.CHROME == type) {
			capabilities = DesiredCapabilities.chrome();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("loadImages", loadImages);
			
		} else {
			capabilities = DesiredCapabilities.htmlUnit();
			capabilities.setJavascriptEnabled(true);
		}
		return capabilities;
	}
	
	/**
	 * 隐式等待期望的元素出现
	 * @param second 最长的等待秒数
	 */
	public void setWaitElementTime(long second) {
		driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);	
	}
	
	/**
	 * 获取WEB驱动
	 * @return WEB驱动
	 */
	public WebDriver DRIVER() {
		return driver;
	}
	
	/**
	 * 关闭浏览器（退出浏览器进程）
	 */
	public void quit() {
		try {
			driver.quit();
		} catch(Throwable e) {}
		
		// 以防万一, 使用系统命令杀掉驱动进程 （Chrome只能通过此方法）
		if(WebDriverType.PHANTOMJS != type && WebDriverType.HTMLUTIL != type) {
			CmdUtils.kill(type.DRIVER_NAME());
		}
	}
	
	/**
	 * 关闭当前页面（若所有页面都被关闭，则自动退出浏览器进程）
	 */
	public void close() {
		try {
			driver.close();
		} catch(Throwable e) {}
	}
	
	/**
	 * 打开页面
	 * @param url 页面URL
	 */
	public void open(String url) {
		driver.navigate().to(url);
	}
	
	/**
	 * 刷新当前页面
	 */
	public void refresh() {
		driver.navigate().refresh();
	}
	
	/**
	 * 获取当前页面路径
	 * @return 当前页面路径
	 */
	public String getCurURL() {
		String url = "";
		try {
			url = driver.getCurrentUrl();
		} catch(Throwable e) {}
		return url;
	}
	
	/**
	 * 获取当前页面源码
	 * @return 当前页面源码
	 */
	public String getPageSource() {
		String ps = "";
		try {
			ps = driver.getPageSource();
		} catch(Throwable e) {}
		return ps;
	}
	
	/**
	 * 添加cookie
	 * @param cookie cookie对象
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addCookie(Cookie cookie) {
		boolean isOk = true;
		try {
			driver.manage().addCookie(cookie);
		} catch(Exception e) {
			isOk = false;
		}
		return isOk;
	}
	
	/**
	 * 添加cookie集
	 * @param cookies cookie集
	 * @return true:添加成功; false:添加失败
	 */
	public boolean addCookies(Set<Cookie> cookies) {
		boolean isOk = true;
		if(cookies == null) {
			isOk = false;
			
		} else {
			for(Cookie cookie : cookies) {
				isOk &= addCookie(cookie);
			}
		}
		return isOk;
	}
	
	/**
	 * 获取cookie集
	 * @return cookie集
	 */
	public Set<Cookie> getCookies() {
		return driver.manage().getCookies();
	}
	
	/**
	 * 清空cookies
	 */
	public void clearCookies() {
		try {
			driver.manage().deleteAllCookies();
		} catch(Throwable e) {}
	}
	
}
