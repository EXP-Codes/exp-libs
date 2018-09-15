package exp.libs.utils.format;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.BoolUtils;


/**
 * <PRE>
 * Json数据提取工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-19
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JsonUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(JsonUtils.class);
	
	/** 私有化构造函数 */
	protected JsonUtils() {}
	
	/**
	 * 是否为合法的json格式字符串
	 * @param json json格式字符串
	 * @return true:合法; false:非法
	 */
	public static boolean isVaild(String json) {
		boolean isVaild = true;
		try {
			JSONObject.fromObject(json);
		} catch(Throwable e) {
			isVaild = false;
		}
		return isVaild;
	}
	
	/**
	 * 是否为非法的json格式字符串
	 * @param json json格式字符串
	 * @return true:非法; false:合法
	 */
	public static boolean isInvaild(String json) {
		return !isVaild(json);
	}
	
	/**
	 * 从json对象中取指定键名的string类型值
	 * @param json json对象
	 * @param key 键名
	 * @return string类型值, 若无值则返回""
	 */
	public static String getStr(JSONObject json, String key) {
		String val = "";
		try {
			val = json.getString(key);
		} catch(Throwable e) {
			log.debug("从JSON中提取 string 类型值 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的string类型值.
	 * @param json json对象
	 * @param key 键名
	 * @param defavlt 默认值
	 * @return string类型值, 若无值则返回默认值
	 */
	public static String getStr(JSONObject json, String key, String defavlt) {
		String val = defavlt;
		try {
			val = json.getString(key);
		} catch(Throwable e) {
			log.debug("从JSON中提取 string 类型值 [{}] 失败.", key);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的bool类型值.
	 * @param json json对象
	 * @param key 键名
	 * @param defavlt 默认值
	 * @return bool类型值, 若无值则返回默认值
	 */
	public static boolean getBool(JSONObject json, String key, boolean defavlt) {
		boolean val = defavlt;
		try {
			val = BoolUtils.toBool(json.getString(key), defavlt);
		} catch(Throwable e) {
			log.debug("从JSON中提取 bool 类型值 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的int类型值.
	 * @param json json对象
	 * @param key 键名
	 * @param defavlt 默认值
	 * @return int类型值, 若无值则返回默认值
	 */
	public static int getInt(JSONObject json, String key, int defavlt) {
		int val = defavlt;
		try {
			val = NumUtils.toInt(json.getString(key), defavlt);
		} catch(Throwable e) {
			log.debug("从JSON中提取 int 类型值 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的long类型值.
	 * @param json json对象
	 * @param key 键名
	 * @param defavlt 默认值
	 * @return long类型值, 若无值则返回默认值
	 */
	public static long getLong(JSONObject json, String key, long defavlt) {
		long val = defavlt;
		try {
			val = NumUtils.toLong(json.getString(key), defavlt);
		} catch(Throwable e) {
			log.debug("从JSON中提取 long 类型值 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的JSONObject对象
	 * @param json json对象
	 * @param key 键名
	 * @return JSONObject对象, 若无值则返回null
	 */
	public static JSONObject getObject(JSONObject json, String key) {
		JSONObject val = null;
		try {
			val = json.getJSONObject(key);
		} catch(Throwable e) {
			val = new JSONObject();
			log.debug("从JSON中提取 object 对象 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的JSONArray对象
	 * @param json json对象
	 * @param key 键名
	 * @return JSONArray对象, 若无值则返回null
	 */
	public static JSONArray getArray(JSONObject json, String key) {
		JSONArray val = null;
		try {
			val = json.getJSONArray(key);
		} catch(Throwable e) {
			val = new JSONArray();
			log.debug("从JSON中提取 array 对象 [{}] 失败.", key, e);
		}
		return val;
	}
	
	/**
	 * 从json对象中取指定键名的JSONArray对象, 并将其转换成String[]数组
	 * @param json json对象
	 * @param key 键名
	 * @return 
	 * 	<PRE>
	 * 		String[]数组.
	 * 		由于第三方构件问题, 若JSONArray无元素, 会自动填充一个 "" 元素, 
	 * 		此时返回的不是 String[0], 而是 String[1] { "" }
	 * 	</PRE>
	 */
	public static String[] getStrArray(JSONObject json, String key) {
		return toStrArray(getArray(json, key));
	}
	
	/**
	 * 把json的array对象转换成String[]数组
	 * @param array json的array对象
	 * @return 
	 * 	<PRE>
	 * 		String[]数组.
	 * 		由于第三方构件问题, 若JSONArray无元素, 会自动填充一个 "" 元素, 
	 * 		此时返回的不是 String[0], 而是 String[1] { "" }
	 * 	</PRE>
	 */
	public static String[] toStrArray(JSONArray array) {
		if(array == null) {
			return new String[0];
		}
		
		String[] sArray = new String[array.size()];
		for(int i = 0; i < sArray.length; i++) {
			sArray[i] = array.getString(i);
		}
		return sArray;
	}
	
	/**
	 * 把json的array对象转换成List<String>队列
	 * @param array json的array对象
	 * @return 
	 * 	<PRE>
	 * 		String[]数组.
	 * 		由于第三方构件问题, 若JSONArray无元素, 会自动填充一个 "" 元素, 
	 * 		此时返回的不是 List<String>(0), 而是 List<String>(1) { "" }
	 * 	</PRE>
	 */
	public static List<String> toStrList(JSONArray array) {
		if(array == null) {
			return new LinkedList<String>();
		}
		
		List<String> sList = new LinkedList<String>();
		for(int i = 0; i < array.size(); i++) {
			sList.add(array.getString(i));
		}
		return sList;
	}
	
	/**
	 * 把List对象转换成JSONArray对象
	 * @param list List对象
	 * @return JSONArray对象
	 */
	public static JSONArray toJsonArray(Collection<String> list) {
		JSONArray array = new JSONArray();
		if(list != null) {
			for(String s : list) {
				array.add(s);
			}
		}
		return array;
	}
	
	/**
	 * 把 String[]数组转换成JSONArray对象
	 * @param array  String[]数组
	 * @return JSONArray对象
	 */
	public static JSONArray toJsonArray(String[] array) {
		JSONArray jsonArray = new JSONArray();
		if(array != null) {
			for(String s : array) {
				jsonArray.add(s);
			}
		}
		return jsonArray;
	}
	
}
