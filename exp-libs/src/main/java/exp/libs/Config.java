package exp.libs;

import exp.libs.envm.Charset;
import exp.libs.warp.conf.ini.INIConfig;

/**
 * <PRE>
 * 经验库内部配置
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public final class Config {
	
	/** 全局配置文件路径（包路径） */
	private final static String GLOBAL_CONF_PATH = "/global.properties";
	
	/** 全局配置 */
	private INIConfig globalCfg;
	
	/** 单例 */
	private static volatile Config instance;
	
	/**
	 * 私有化构造函数
	 */
	private Config() {
		this.globalCfg = new INIConfig(GLOBAL_CONF_PATH, Charset.ISO);
	}
	
	/**
	 * 获取单例
	 * @return 单例
	 */
	public static Config getInstn() {
		if(instance == null) {
			synchronized (Config.class) {
				if(instance == null) {
					instance = new Config();
				}
			}
		}
		return instance;
	}
	
	public String TEMPLATE_DB_BEAN() {
		return globalCfg.getVal("TEMPLATE_DB_BEAN");
	}
	
	public String TEMPLATE_PROXOOL() {
		return globalCfg.getVal("TEMPLATE_PROXOOL");
	}
	
	public String ICON_RES() {
		return globalCfg.getVal("ICON_RES");
	}
	
	public String VER_DB_SCRIPT() {
		return globalCfg.getVal("VER_DB_SCRIPT");
	}
	
}
