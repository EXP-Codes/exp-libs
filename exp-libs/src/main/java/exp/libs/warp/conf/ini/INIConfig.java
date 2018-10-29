package exp.libs.warp.conf.ini;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.io.FileUtils;
import exp.libs.utils.io.JarUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * INI配置器.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-10-29
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class INIConfig {

	/** 日志器 */
	private Logger log = LoggerFactory.getLogger(INIConfig.class);
	
	/** 默认分组名称 */
	public final static String DEFAULT_SECTION = "DEFAULT";
	
	/** ini配置文件路径 */
	private String iniPath;
	
	/** ini配置文件 */
	private IniFile iniFile;
	
	/** ini配置文件读取器 */
	private _IniFileReader reader;
	
	/** ini配置文件保存器 */
	private _IniFileWriter writer;
	
	/**
	 * 构造函数
	 * @param iniPath ini配置文件路径
	 */
	public INIConfig(String iniPath) {
		this(iniPath, null);
	}
	
	/**
	 * 构造函数
	 * {@link #save()}
	 * @param iniPath ini配置文件路径
	 * @param encoding ini配置文件编码
	 */
	public INIConfig(String iniPath, String encoding) {
		
		// iniPath可能是包路径，即ini文件可能在jar包内, 尝试读取
		// （但此时ini文件只能读，不能写, 即 {@link #save()} 方法失效）
		if(!FileUtils.exists(iniPath)) {
			String tmpPath = FileUtils.createTmpFile();
			JarUtils.copyFile(iniPath, tmpPath);
			iniPath = tmpPath;
		}
		File file = new File(iniPath);
		
		this.iniFile = new BasicIniFile();
		this.reader = new _IniFileReader(iniFile, file, encoding);
		this.writer = new _IniFileWriter(iniFile, file, encoding);
		read();
	}
	
	/**
	 * 读取ini配置文件
	 * @return true:读取成功; false:读取失败
	 */
	private boolean read() {
		boolean isOk = false;
		try {
			reader.read();
			isOk = true;
			
		} catch (Exception e) {
			log.error("读取 [{}] 文件失败.", iniPath, e);
		}
		return isOk;
	}
	
	/**
	 * 保存更改到ini配置文件
	 * 只有在调用此方法时，针对ini配置项的修改才会写入文件.
	 * @return true:保存成功; false:保存失败
	 */
	private boolean write() {
		boolean isOk = false;
		try {
			writer.write();
			isOk = true;
			
		} catch (Exception e) {
			log.error("保存 [{}] 文件失败.", iniPath, e);
		}
		return isOk;
	}
	
	/**
	 * 保存更改到ini配置文件.
	 * 只有在调用此方法时，针对ini配置项的修改才会写入文件.
	 * @return true:保存成功; false:保存失败
	 */
	public boolean save() {
		return write();
	}
	
	/**
	 * 在默认分组下，新增/修改一个键值对
	 * @param key 键名
	 * @param value 值
	 * @return true:新增/修改成功; false:新增/修改成功
	 */
	public boolean setKV(String key, String value) {
		return setKV(DEFAULT_SECTION, key, value);
	}
	
	/**
	 * 在指定分组下，新增/修改一个键值对
	 * @param section 分组名
	 * @param key 键名
	 * @param value 值
	 * @return true:新增/修改成功; false:新增/修改成功
	 */
	public boolean setKV(String section, String key, String value) {
		boolean isOk = false;
		if(StrUtils.isNotTrimEmpty(section, key)) {
			IniSection iniSection = iniFile.getSection(section);
			if(iniSection == null) {
				iniSection = new BasicIniSection(section);
				iniFile.addSection(section);
			}
			IniItem iniItem = new IniItem(key);
			iniItem.setValue(value);
			iniSection.addItem(iniItem);
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 删除默认分组下的一个键值对
	 * @param key 键名
	 */
	public void delKV(String key) {
		delKV(DEFAULT_SECTION, key);
	}
	
	/**
	 * 删除指定分组下的一个键值对
	 * @param section 分组名
	 * @param key 键名
	 */
	public void delKV(String section, String key) {
		IniSection iniSection = iniFile.getSection(section);
		if(iniSection != null) {
			iniSection.removeItem(key);
		}
	}
	
	/**
	 * 删除指定若干个分组、及其下的所有键值对
	 * @param sections 分组名
	 */
	public void delSections(String... sections) {
		if(ListUtils.isNotEmpty(sections)) {
			iniFile.removeSections(sections);
		}
	}
	
	/**
	 * 清空ini中所有分组和键值对配置项
	 */
	public void delAll() {
		iniFile.removeAll();
	}
	
	/**
	 * 获取指定分组下的所有键值对
	 * @param section 分组名
	 * @return 键值对表
	 */
	public Map<String, String> getAllKVs(String section) {
		Map<String, String> kvs = new LinkedHashMap<String, String>();
		if(StrUtils.isTrimEmpty(section)) {
			section = DEFAULT_SECTION;
		}
		
		Iterator<IniSection> iniSections = iniFile.getSections().iterator();
		while(iniSections.hasNext()) {
			IniSection iniSection = iniSections.next();
			if(!iniSection.getName().equals(section)) {
				continue;
			}
			
			Iterator<IniItem> iniItems = iniSection.getItems().iterator();
			while(iniItems.hasNext()) {
				IniItem iniItem = iniItems.next();
				kvs.put(iniItem.getName(), iniItem.getValue());
			}
		}
		return kvs;
	}
	
	/**
	 * 获取所有键值对
	 * @return 键值对表
	 */
	public Map<String, String> getAllKVs() {
		Map<String, String> kvs = new LinkedHashMap<String, String>();
		Iterator<IniSection> iniSections = iniFile.getSections().iterator();
		while(iniSections.hasNext()) {
			IniSection iniSection = iniSections.next();
			Iterator<IniItem> iniItems = iniSection.getItems().iterator();
			while(iniItems.hasNext()) {
				IniItem iniItem = iniItems.next();
				kvs.put(iniItem.getName(), iniItem.getValue());
			}
		}
		return kvs;
	}
	
}
