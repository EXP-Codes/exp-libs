package exp.libs.utils.verify;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 正则表达式工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RegexUtils {

	/** 私有化构造方法 */
	protected RegexUtils() {}
	
	/**
	 * 完全匹配
	 * @param str 被匹配字符串
	 * @param regex 正则表达式
	 * @return true:完全匹配; false:非完全匹配
	 */
	public static boolean matches(String str, String regex) {
		boolean isMatch = false;
		if(StrUtils.isNotEmpty(str, regex)) {
			isMatch = Pattern.compile(regex).matcher(str).matches();
		}
		return isMatch;
	}
	
	/**
	 * 部分匹配
	 * @param str 被匹配字符串
	 * @param regex 正则表达式
	 * @return true:存在子串匹配; false:完全不匹配
	 */
	public static boolean contains(String str, String regex) {
		boolean isMatch = false;
		if(StrUtils.isNotEmpty(str) && StrUtils.isNotEmpty(regex)) {
			Pattern ptn = Pattern.compile(regex);
			Matcher mth = ptn.matcher(str);
			isMatch = mth.find();
		}
		return isMatch;
	}
	
	/**
	 * <PRE>
	 * 取首次匹配的group(1)
	 * </PRE>
	 * @param str 被匹配字符串
	 * @param regex 正则表达式(必须至少含有1个括号)
	 * @return 匹配值(若无匹配返回"")
	 */
	public static String findFirst(String str, String regex) {
		return findGroup(str, regex, 1);
	}
	
	/**
	 * <PRE>
	 * 取首次匹配的group(i)
	 * </PRE>
	 * @param str 被匹配字符串
	 * @param regex 正则表达式(必须含有若干个括号)
	 * @param groupId 第i个组号(即括号)
	 * @return 匹配值(若无匹配返回"")
	 */
	public static String findGroup(String str, String regex, int groupId) {
		String value = "";
		if(StrUtils.isNotEmpty(str) && StrUtils.isNotEmpty(regex)) {
			groupId = (groupId < 0 ? 0 : groupId);
			Pattern ptn = Pattern.compile(regex);
			Matcher mth = ptn.matcher(str);
			if(mth.find()) {
				value = (mth.groupCount() >= groupId ? mth.group(groupId) : "");
			}
		}
		return value;
	}
	
	/**
	 * <PRE>
	 * 取首次匹配的group(0...n)
	 * </PRE>
	 * @param str 被匹配字符串
	 * @param regex 正则表达式
	 * @return 匹配值集(集合索引值对应正则式括号次序)
	 */
	public static List<String> findGroups(String str, String regex) {
		List<String> list = new ArrayList<String>();
		if(StrUtils.isNotEmpty(str) && StrUtils.isNotEmpty(regex)) {
			Pattern ptn = Pattern.compile(regex);
			Matcher mth = ptn.matcher(str);
			
			int groupCount = mth.groupCount();
			if(mth.find()) {
				for (int i = 0; i <= groupCount; i++) {
					list.add(mth.group(i));
				}
			}
		}
		return list;
	}
	
	/**
	 * <PRE>
	 * 取每次匹配的group(1)
	 * </PRE>
	 * @param str 被匹配字符串
	 * @param regex 正则表达式(必须至少含有1个括号)
	 * @return 匹配值集（集合大小即为匹配次数）
	 */
	public static List<String> findBrackets(String str, String regex) {
		List<String> list = new LinkedList<String>();
		if(StrUtils.isNotEmpty(str) && StrUtils.isNotEmpty(regex)) {
			Pattern ptn = Pattern.compile(regex);
			Matcher mth = ptn.matcher(str);
			
			while(mth.find()) {
				String value = (mth.groupCount() >= 1 ? mth.group(1) : "");
				list.add(value);
			}
		}
		return list;
	}
	
	/**
	 * <PRE>
	 * 取每次匹配的group(0...n)
	 * </PRE>
	 * @param str 被匹配字符串
	 * @param regex 正则表达式
	 * @return 匹配值集(行索引对应匹配次数，列索引值对应正则式括号次序)
	 */
	public static List<List<String>> findAll(String str, String regex) {
		List<List<String>> list = new LinkedList<List<String>>();
		if(StrUtils.isNotEmpty(str) && StrUtils.isNotEmpty(regex)) {
			Pattern ptn = Pattern.compile(regex);
			Matcher mth = ptn.matcher(str);
			
			int groupCount = mth.groupCount();
			while(mth.find()) {
				List<String> groups = new ArrayList<String>(groupCount);
				for (int i = 0; i <= groupCount; i++) {
					groups.add(mth.group(i));
				}
				list.add(groups);
			}
		}
		return list;
	}
	
}
