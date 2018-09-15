package exp.libs.utils.other;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exp.libs.envm.Delimiter;
import exp.libs.utils.format.ESCUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.utils.verify.VerifyUtils;

/**
 * <PRE>
 * 字符串处理工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-19
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class StrUtils {

	/** 全角字符集 */
	private final static char[] FULL_WIDTH_CH = {
			'０', '１', '２', '３' , '４', '５' , '６', '７', '８', '９', 
			'Ａ', 'Ｂ', 'Ｃ', 'Ｄ', 'Ｅ' , 'Ｆ', 'Ｇ' , 'Ｈ', 'Ｉ', 'Ｊ', 'Ｋ', 
			'Ｌ', 'Ｍ', 'Ｎ', 'Ｏ', 'Ｐ' , 'Ｑ', 'Ｒ' , 'Ｓ', 'Ｔ', 'Ｕ', 'Ｖ', 
			'Ｗ', 'Ｘ', 'Ｙ', 'Ｚ', 'ａ' , 'ｂ', 'ｃ' , 'ｄ', 'ｅ', 'ｆ', 'ｇ', 
			'ｈ', 'ｉ', 'ｊ', 'ｋ', 'ｌ' , 'ｍ', 'ｎ' , 'ｏ', 'ｐ', 'ｑ', 'ｒ', 
			'ｓ', 'ｔ', 'ｕ', 'ｖ', 'ｗ' , 'ｘ', 'ｙ' , 'ｚ', '：', '；', '，', 
			'（', '）', '【', '】', '｛', '｝', '‘', '’', '“', '”', 
			'＜', '＞', '《', '》', '？', '。'
	};
	
	/** 半角字符集 */
	private final static char[] HALF_WIDTH_CH = {
			'0', '1', '2', '3' , '4', '5' , '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E' , 'F', 'G' , 'H', 'I', 'J', 'K', 
			'L', 'M', 'N', 'O', 'P' , 'Q', 'R' , 'S', 'T', 'U', 'V', 
			'W', 'X', 'Y', 'Z', 'a' , 'b', 'c' , 'd', 'e', 'f', 'g', 
			'h', 'i', 'j', 'k', 'l' , 'm', 'n' , 'o', 'p', 'q', 'r', 
			's', 't', 'u', 'v', 'w' , 'x', 'y' , 'z', ':', ';', ',', 
			'(', ')', '[', ']', '{', '}', '\'', '\'', '\"', '\"', 
			'<', '>', '<', '>', '?', '.'
	};
	
	/** 私有化构造函数 */
	protected StrUtils() {}
	
	/**
	 * 判断字符串是否为空
	 * @param str 待判断字符串
	 * @return true:字符串为null或""; false:字符串非空
	 */
	public static boolean isEmpty(String str) {
		boolean isEmpty = false;
		if(str == null || "".equals(str)) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * 判断所有字符串是否均为空
	 * @param strs 字符串集
	 * @return true:所有字符串为null或""; false:存在字符串非空
	 */
	public static boolean isEmpty(String... strs) {
		boolean isEmpty = true;
		if(strs != null) {
			for(String s : strs) {
				isEmpty &= isEmpty(s);
				if(isEmpty == false) {
					break;
				}
			}
		}
		return isEmpty;
	}
	
	/**
	 * 判断字符串是否非空
	 * @param str 待判断字符串
	 * @return true:字符串非空; false:字符串为null或""
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 判断所有字符串是否均为非空
	 * @param strs 字符串集
	 * @return true:所有字符串非空; false:存在字符串为null或""
	 */
	public static boolean isNotEmpty(String... strs) {
		boolean isNotEmpty = true;
		if(strs != null && strs.length > 0) {
			for(String str : strs) {
				isNotEmpty &= isNotEmpty(str);
				if(isNotEmpty == false) {
					break;
				}
			}
		} else {
			isNotEmpty = false;
		}
		return isNotEmpty;
	}
	
	/**
	 * 判断trim(字符串)是否为空
	 * @param str 待判断字符串
	 * @return true:trim(字符串)为null或""; false:trim(字符串)非空
	 */
	public static boolean isTrimEmpty(String str) {
		boolean isTrimEmpty = false;
		if(str == null || "".equals(str.trim())) {
			isTrimEmpty = true;
		}
		return isTrimEmpty;
	}
	
	/**
	 * 判断所有trim(字符串)是否均为空
	 * @param strs 字符串集
	 * @return true:所有trim(字符串)为null或""; false:存在trim(字符串)非空
	 */
	public static boolean isTrimEmpty(String... strs) {
		boolean isTrimEmpty = true;
		if(strs != null) {
			for(String str : strs) {
				isTrimEmpty &= isTrimEmpty(str);
				if(isTrimEmpty == false) {
					break;
				}
			}
		}
		return isTrimEmpty;
	}
	
	/**
	 * 判断trim(字符串)是否非空
	 * @param s 待判断字符串
	 * @return true:trim(字符串)非空; false:trim(字符串)为null或""
	 */
	public static boolean isNotTrimEmpty(String str) {
		return !isTrimEmpty(str);
	}
	
	/**
	 * 判断所有trim(字符串)是否均为非空
	 * @param strs 字符串集
	 * @return true:所有trim(字符串)非空; false:存在trim(字符串)为null或""
	 */
	public static boolean isNotTrimEmpty(String... strs) {
		boolean isNotTrimEmpty = true;
		if(strs != null && strs.length > 0) {
			for(String str : strs) {
				isNotTrimEmpty &= isNotTrimEmpty(str);
				if(isNotTrimEmpty == false) {
					break;
				}
			}
		} else {
			isNotTrimEmpty = false;
		}
		return isNotTrimEmpty;
	}
	
	/**
	 * 判断字符串是否全为空白字符
	 * @param s 待判断字符串
	 * @return 是否全空白字符串
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return false;
		}
		return isEmpty(str.trim());
	}
	
	/**
	 * 判断字符串是否非null
	 * @param s 待判断字符串
	 * @return true:字符串非null; false:字符串为null
	 */
	public static String toNotNull(String str) {
		return (str == null ? "" : str);
	}
	
	/**
	 * 判断字符串集中所有字符串是否均相同
	 * @param strs 字符串集
	 * @return true:所有字符串均相同; false:存在差异的字符串 或 字符串集数量<=1
	 */
	public static boolean equals(String... strs) {
		boolean isEquals = true;
		if(strs == null || strs.length <= 1) {
			isEquals = false;
			
		} else {
			String s = strs[0];
			if(s == null) {
				for(String str : strs) {
					isEquals &= (str == null);
					if(!isEquals) {
						break;
					}
				}
				
			} else {
				for(String str : strs) {
					isEquals &= (s.equals(str));
					if(!isEquals) {
						break;
					}
				}
			}
		}
		return isEquals;
	}
	
	/**
	 * 反转字符串
	 * @param str 原字符串
	 * @return 反转字符串
	 */
	public static String reverse(String str) {
		if (str == null) {
			return "";
		}
		return new StringBuilder(str).reverse().toString();
	}
	
	/**
	 * 把N个字符拼接成一个字符串
	 * @param c 字符
	 * @param num 数量
	 * @return 字符串(c*N)
	 */
	public static String multiChar(char c, int num) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < num; i++) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 连接多个对象为一个字符串
	 * @param objlist 多个对象
	 * @return 依次连接所有对象的字符串
	 */
	public static String concat(Object... objlist) {
		StringBuilder sb = new StringBuilder();
		if(objlist != null) {
			for(Object o : objlist) {
				if(o == null) {
					continue;
				}
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 连接多个字符串为一个字符串
	 * @param strlist 多个字符串
	 * @return 依次连接所有字符串的字符串
	 */
	public static String concat(String... strlist) {
		StringBuilder sb = new StringBuilder();
		if(strlist != null) {
			for(String str : strlist) {
				if(str == null) {
					continue;
				}
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 使用指定分隔符连接字符串
	 * @param set 字符串集合
	 * @param separator 分隔符
	 * @return 使用分隔符依次连接所有字符串的字符串
	 */
	public static <E> String concat(Set<E> set, String separator) {
		return _concat(set, separator);
	}
	
	/**
	 * 使用指定分隔符连接字符串
	 * @param list 字符串列表
	 * @param separator 分隔符
	 * @return 使用分隔符依次连接所有字符串的字符串
	 */
	public static <E> String concat(List<E> list, String separator) {
		return _concat(list, separator);
	}
	
	/**
	 * 使用指定分隔符连接字符串 (此方法的入参会与 Object...冲突)
	 * @param collection 字符串集合
	 * @param separator 分隔符
	 * @return 使用分隔符依次连接所有字符串的字符串
	 */
	private static <E> String _concat(Collection<E> collection, String separator) {
		StringBuilder sb = new StringBuilder();
		if(ListUtils.isNotEmpty(collection)) {
			separator = (separator == null ? "" : separator);
			for(E e : collection) {
				if(e == null) {
					continue;
				}
				sb.append(e.toString()).append(separator);
			}
			
			if(sb.length() > separator.length()) {
				sb.setLength(sb.length() - separator.length());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转换字符串的第一个字母大写
	 * @param str 要转换字符串
	 * @return 转换后的结果 ，如： string==>String
	 */
	public static String upperAtFirst(String str) {
		String first = str.substring(0, 1);
		String suffix = str.substring(1, str.length());
		return StrUtils.concat(first.toUpperCase(), suffix);
	}
	
	/**
	 * 转换字符串的第一个字母为小写
	 * @param str 要转换字符串
	 * @return 转换后的结果 ，如： String==>string
	 */
	public static String lowerAtFirst(String str) {
		String first = str.substring(0, 1);
		String suffix = str.substring(1, str.length());
		return StrUtils.concat(first.toLowerCase(), suffix);
	}
	
	/**
	 * 令字符串中的所有空字符可视
	 * @param str 字符串
	 * @return 所含空字符变成可视字符的字符串
	 */
	public static String view(final String str) {
		StringBuilder sb = new StringBuilder();
		char[] cs = str.toCharArray();
		for(char c : cs) {
			sb.append(view(c));
		}
		return sb.toString();
	}
	
	/**
	 * 令空字符变成可视字符串
	 * @param emptyChar 空字符
	 * @return 可视字符
	 */
	public static String view(final char emptyChar) {
		String str = null;
		switch(emptyChar) {
			case '\\' : { str = "\\\\"; break; } 
			case '\t' : { str = "\\t"; break; } 
			case '\r' : { str = "\\r"; break; } 
			case '\n' : { str = "\\n"; break; } 
			case '\0' : { str = "\\0"; break; } 
			case '\b' : { str = "\\b"; break; } 
			default : { str = String.valueOf(emptyChar); }
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 把对象o转换成字符串后, 在其[左边]用字符c补长, 使得新字符串的长度补长到size.
	 * 	(若原字符串长度>=size， 则不再补长)
	 * <PRE>
	 * @param o 待补长的对象
	 * @param c 用于补长的字符
	 * @param size 补长完成后字符串的总长度
	 * @return 左边填充了字符c且长度为size的字符串
	 */
	public static String leftPad(final Object o, final char c, final int size) {
		return leftPad((o == null ? "" : o.toString()), c, size);
	}
	
	/**
	 * <PRE>
	 * 在字符串s[左边]用字符c补长, 使得新字符串的长度补长到size.
	 * 	(若原字符串长度>=size， 则不再补长)
	 * <PRE>
	 * @param s 待补长的字符串
	 * @param c 用于补长的字符
	 * @param size 补长完成后字符串的总长度
	 * @return 左边填充了字符c且长度为size的字符串
	 */
	public static String leftPad(final String s, final char c, final int size) {
		String str = (s == null ? "" : s);
		int fillCnt = size - str.length();
		if(fillCnt > 0) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < fillCnt; i++) {
				sb.append(c);
			}
			sb.append(str);
			str = sb.toString();
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 把对象o转换成字符串后, 在其[右边]用字符c补长, 使得新字符串的长度补长到size.
	 * 	(若原字符串长度>=size， 则不再补长)
	 * <PRE>
	 * @param o 待补长的对象
	 * @param c 用于补长的字符
	 * @param size 补长完成后字符串的总长度
	 * @return 右边填充了字符c且长度为size的字符串
	 */
	public static String rightPad(final Object o, final char c, final int size) {
		return rightPad((o == null ? "" : o.toString()), c, size);
	}
	
	/**
	 * <PRE>
	 * 在字符串s[右边]用字符c补长, 使得新字符串的长度补长到size.
	 * 	(若原字符串长度>=size， 则不再补长)
	 * <PRE>
	 * @param s 待补长的字符串
	 * @param c 用于补长的字符
	 * @param size 补长完成后字符串的总长度
	 * @return 右边填充了字符c且长度为size的字符串
	 */
	public static String rightPad(final String s, final char c, final int size) {
		String str = (s == null ? "" : s);
		int fillCnt = size - str.length();
		if(fillCnt > 0) {
			StringBuilder sb = new StringBuilder(str);
			for(int i = 0; i < fillCnt; i++) {
				sb.append(c);
			}
			str = sb.toString();
		}
		return str;
	}
	
	/**
	 * 移除字符串中的头尾空字符
	 * @param str 原字符串
	 * @return 移除空字符后的字符串
	 */
	public static String trim(String str) {
		return (str == null ? "" : str.trim());
	}
	
	/**
	 * 移除字符串中的所有空字符
	 * @param str 原字符串
	 * @return 移除空字符后的字符串
	 */
	public static String trimAll(final String str) {
		return (str == null ? "" : str.replaceAll("\\s", ""));
	}
	
	/**
	 * <pre>
	 * 对字符串进行固定长度断行.
	 * ---------------------------
	 *  如原字符串为: ABCDEFGHIJK
	 *  若断行长度为: 3
	 *  则断行后的字符串为：
	 *  		  ABC
	 *  		  DEF
	 *  		  GHI
	 *  		  JK
	 * </pre>
	 * @param str 原字符串
	 * @param len 断行长度
	 * @return 断行后的字符串
	 */
	public static String breakLine(String str, int len) {
		return fill(str, len, Delimiter.CRLF);
	}
	
	/**
	 * 在字符串中，每隔特定距离填充另1个字符串
	 * @param str 原字符串
	 * @param interval 填充间隔
	 * @param fillStr 填充字符串
	 * @return 填充后的字符串
	 */
	public static String fill(String str, int interval, String fillStr) {
		StringBuilder sb = new StringBuilder();
		if(str != null) {
			if(interval <= 0) {
				sb.append(str);
				
			} else {
				int cnt = 0;
				char[] chs = str.toCharArray();
				for(char ch : chs) {
					sb.append(ch);
					if(++cnt >= interval) {
						cnt = 0;
						sb.append(fillStr);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * <PRE>
	 * 根据首尾切割符，切割字符串.
	 * 
	 * 	如: s = xyzAAqqqZZabcAAwwwZZrstAAeeeZZzyx
	 * 		bgnDelmiter = AA
	 * 		endDelmiter = ZZ
	 * 	则切割成 qqq、www、eee
	 * </PRE>
	 * @param s 被切割字符串
	 * @param bgnDelmiter 首切割符
	 * @param endDelmiter 尾切割符
	 * @return 切割字符串集
	 */
	public static String[] split(String s, String bgnDelmiter, String endDelmiter) {
		s = (s == null ? "" : s);
		String[] ss = null;
		
		if(isNotEmpty(bgnDelmiter) && isNotEmpty(endDelmiter)) {
			String regex = concat(ESCUtils.toRegexESC(bgnDelmiter), 
					"([\\s\\S]*?)", ESCUtils.toRegexESC(endDelmiter));
			List<String> subs = RegexUtils.findBrackets(s, regex);
			ss = new String[subs.size()];
			subs.toArray(ss);
			
		} else if(isNotEmpty(bgnDelmiter)) {
			ss = s.split(bgnDelmiter);
			
		} else if(isNotEmpty(endDelmiter)) {
			ss = s.split(endDelmiter);
			
		} else {
			ss = new String[] { s };
		}
		return ss;
	}
	
	/**
	 * 使用delmiter切割字符串，并把得到的切割子串转换成clazz对象
	 * @param s 被切割字符串
	 * @param delmiter 切割符
	 * @param clazz 子串的强制转换类型
	 * @return 被切割对象集
	 */
	public static Object[] split(String s, String delmiter, Class<?>[] clazz) {
		s = (s == null ? "" : s);
		delmiter = (delmiter == null ? "" : delmiter);
		if(clazz == null) {
			return s.split(delmiter);
		}
		
		String[] ss = s.split(delmiter, clazz.length);
		Object[] os = new Object[ss.length];
		for (int i = 0; i < os.length; i++) {
			os[i] = ObjUtils.toObj(ss[i], clazz[i]);
		}
		return os;
	}
	
	/**
	 * <PRE>
	 * 把字符串切割成不同长度的子串 (第i个子串的长度为len[i])
	 * </PRE>
	 * @param str 原字符串
	 * @param lens 每个子串的长度
	 * @return 切割后的子串
	 */
	public static String[] split(final String str, final int[] lens) {
		if(lens == null) {
			return (str == null ? new String[] {""} : new String[] {str});
		}
		
		String s = (str == null ? "" : str);
		String[] sAry = new String[lens.length];
		int sLen = s.length();
		
		for(int i = 0; i < lens.length; i++) {
			int len = lens[i];
			
			if(sLen >= len) {
				sAry[i] = s.substring(0, len);
				s = s.substring(len);
				sLen -= len;
				
			} else {
				sAry[i] = s.substring(0, sLen);
				s = "";
				sLen = 0;
			}
		}
		return sAry;
	}
	
	/**
	 * 从字符串s中截取bgn（不包括）和end（不包括）之间的子串
	 * @param s 原字符串
	 * @param bgn 离字符串首最近的起始标识
	 * @param end 离bgn最近的结束标识
	 * @return 子串
	 */
	public static String substr(String s, String bgn, String end) {
		String sub = "";
		if(isNotEmpty(s)) {
			bgn = (bgn == null ? "" : bgn);
			end = (end == null ? "" : end);
			
			int bgnIdx = s.indexOf(bgn);
			int bgnLen = bgn.length();
			if(bgnIdx < 0) {
				sub = "";
			} else {
				sub = s.substring(bgnIdx + bgnLen);
			}
			
			if(isNotEmpty(sub)) {
				int endIdx = sub.indexOf(end);
				if(endIdx < 0) {
					sub = "";
				} else if(endIdx == 0) {
					// UNDO
				} else {
					sub = sub.substring(0, endIdx);
				}
			}
		}
		return sub;
	}
	
	/**
	 * 在字符串s中截取第amount次出现的mark（不包括）之前的子串
	 * @param s 原字符串
	 * @param mark 标记字符串
	 * @param amount 标记字符串出现次数
	 * @return 子串
	 */
	public static String substr(String s, String mark, int amount) {
		String sub = (s == null ? "" : s);
		if(isNotEmpty(s) && isNotEmpty(mark) && amount > 0) {
			int len = mark.length();
			int sumIdx = 0;
			int subIdx = -1;
			int cnt = 0;
			do {
				subIdx = sub.indexOf(mark);
				if(subIdx < 0) {
					break;
				}
				
				sumIdx += (subIdx + len);
				sub = sub.substring(subIdx + len);
			} while(++cnt < amount);
			sub = (subIdx >= 0 ? s.substring(0, sumIdx - 1) : s);
		}
		return sub;
	}
	
	/**
	 * <PRE>
	 * 截取字符串摘要.
	 * 	若字符串长度超过128字符，则截取前128个字符，并在末尾补省略号[...]
	 * </PRE>
	 * @param str 原字符串
	 * @return 字符串摘要
	 */
	public static String showSummary(String str) {
		return showSummary(str, 128);
	}
	
	/**
	 * <PRE>
	 * 截取字符串摘要.
	 * 	若字符串长度超过limit个字符，则截取前limit个字符，并在末尾补省略号[...]
	 * </PRE>
	 * @param str 原字符串
	 * @param limit 限制字符数
	 * @return 字符串摘要
	 */
	public static String showSummary(String str, int limit) {
		limit = (limit <= 0 ? 128 : limit);
		String summary = "";
		if(str != null) {
			if(str.length() > limit) {
				summary = concat(str.substring(0, limit), "...");
				
			} else {
				summary = str;
			}
		}
		return summary;
	}
	
	/**
	 * 计算字符串中指定字符出现的个数
	 * @param s 原字符串
	 * @param c 被核查字符
	 * @return 字符出现次数
	 */
	public static int count(final String s, final char c) {
		int cnt = 0;
		if(s != null) {
			char[] chArray = s.toCharArray();
			for(char ch : chArray) {
				if(ch == c) {
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	/**
	 * 获取字符串中的中文个数
	 * @param s 原字符串
	 * @return 中文个数
	 */
	public static int countCh(final String s) {
		int cnt = 0;
		if(s != null) {
			char[] cs = s.toCharArray();
			for(char c : cs) {
				cnt += (VerifyUtils.isChinese(c) ? 1 : 0);
			}
		}
		return cnt;
	}
	
	/**
	 * 检查字符串中是否包含了中文
	 * @param s 原字符串
	 * @return true:含中文; false:不含
	 */
	public static boolean containsCh(final String s) {
		boolean isContains = false;
		if(s != null) {
			char[] cs = s.toCharArray();
			for(char c : cs) {
				if(VerifyUtils.isChinese(c)) {
					isContains = true;
					break;
				}
			}
		}
		return isContains;
	}
	
	/**
	 * <PRE>
	 * 计算字符串的中文长度.
	 * 	（默认情况下java的中文字符和英文字符均占长度为1字符，此方法以 [1中文长度=2英文长度] 换算字符串长度）
	 * </PRE>
	 * @param s 原字符串
	 * @return 中文长度
	 */
	public static int chLen(final String s) {
		int len = 0;
		if(s != null) {
			len = s.length() + countCh(s);
		}
		return len;
	}
	
	/**
	 * 把字符串中的 [全角/中文字符] 转换成 [半角/英文字符]
	 * @param fullWidth 含 [全角/中文字符] 的字符串
	 * @return 含 [半角/英文字符] 的字符串
	 */
	public static String toHalfWidth(final String fullWidth) {
		String halfWidth = "";
		if(fullWidth != null) {
			halfWidth = fullWidth;
			int size = FULL_WIDTH_CH.length;
			for(int i = 0; i < size; i++) {
				halfWidth = halfWidth.replace(
						FULL_WIDTH_CH[i], HALF_WIDTH_CH[i]);
			}
		}
		return halfWidth;
	}
	
	/**
	 * 检查变量的值是否在指定的范围数组内
	 * @param variable 变量（允许为null值）
	 * @param ranges 范围数组
	 * @return true:在范围内; false:在范围外
	 */
	public static boolean inRange(String variable, String... ranges) {
		boolean inRange = false;
		if(ranges != null && ranges.length > 0) {
			inRange = new HashSet<String>(Arrays.asList(ranges)).
					contains(variable);
		}
		return inRange;
	}
	
}
