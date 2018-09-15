package exp.libs.warp.conf.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <pre>
 * XML文件配置器工厂.
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final public class XConfigFactory {

	private final static XConfig DEFAULT_CONFIG = new XConfig("DEFAULT_CONFIG");
	
	private Map<String, XConfig> configs;
	
	private static volatile XConfigFactory instance;
	
	private XConfigFactory() {
		this.configs = new HashMap<String, XConfig>(2);
	}
	
	private static XConfigFactory getInstn() {
		if(instance == null) {
			synchronized (XConfigFactory.class) {
				if(instance == null) {
					instance = new XConfigFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 创建一个xml配置加载器
	 * @param name 配置加载器名称（随意即可，必须唯一）
	 * @return xml配置加载器
	 */
	public static XConfig createConfig(final String name) {
		return getInstn()._createConfig(name);
	}
	
	private XConfig _createConfig(final String name) {
		if(name == null) {
			return DEFAULT_CONFIG;
		}
		
		XConfig conf = configs.get(name);
		if(conf == null) {
			conf = new XConfig(name);
			
		} else {
			conf.destroy();
			conf = new XConfig(name);
		}
		configs.put(name, conf);
		return conf;
	}
	
	/**
	 * 获取默认的xml配置加载器
	 * @return 默认的xml配置加载器
	 */
	public static XConfig getDefaultConfig() {
		return getInstn()._getConfig(null);
	}
	
	/**
	 * 获取xml配置加载器
	 * @return xml配置加载器(若不存在则返回默认的xml配置加载器)
	 */
	public static XConfig getConfig(final String name) {
		return getInstn()._getConfig(name);
	}
	
	private XConfig _getConfig(final String name) {
		XConfig conf = DEFAULT_CONFIG;
		if(name != null) {
			conf = configs.get(name);
			if(conf == null) {
				conf = DEFAULT_CONFIG;
			}
		}
		return conf;
	}
	
	/**
	 * 删除xml配置加载器
	 * @param name  xml配置加载器名称
	 */
	public static void removeConfig(final String name) {
		getInstn()._removeConfig(name);
	}
	
	private void _removeConfig(final String name) {
		XConfig conf = configs.remove(name);
		if(conf != null) {
			conf.destroy();
		}
	}
	
	/**
	 * 删除所有xml配置器
	 */
	public static void clear() {
		getInstn()._clear();
	}
	
	private void _clear() {
		Iterator<XConfig> its = configs.values().iterator();
		while(its.hasNext()) {
			XConfig conf = its.next();
			conf.destroy();
		}
		configs.clear();
	}
	
}
