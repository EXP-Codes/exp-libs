package exp.libs.envm;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 枚举类：正则表达式
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public enum Regex {

	REAL("([+-]?(\\d|[1-9]\\d+)(\\.\\d+)?)", "实数"),
	
	REAL_POSITIVE("(\\+?([1-9]|[1-9]\\d+)(\\.\\d+)?)", "正实数"),
	
	REAL_NEGATIVE("(-([1-9]|[1-9]\\d+)(\\.\\d+)?)", "负实数"),
	
	REAL_NOT_NEGATIVE("(\\+?(\\d|[1-9]\\d+)(\\.\\d+)?)", "非负实数"),
	
	INTEGER("([+-]?(\\d|[1-9]\\d+))", "整数"),
	
	INTEGER_POSITIVE("(\\+?([1-9]|[1-9]\\d+))", "正整数"),
	
	INTEGER_NEGATIVE("(-([1-9]|[1-9]\\d+))", "负整数"),
	
	INTEGER_NOT_NEGATIVE("(\\+?(\\d|[1-9]\\d+))", "非负整数"),
	
	FLOAT("([+-]?(\\d|[1-9]\\d+)(\\.\\d+)?)", "浮点数"),
	
	FLOAT_POSITIVE("(\\+?([1-9]|[1-9]\\d+)(\\.\\d+)?)", "正浮点数"),
	
	FLOAT_NEGATIVE("(-([1-9]|[1-9]\\d+)(\\.\\d+)?)", "负浮点数"),
	
	FLOAT_NOT_NEGATIVE("(\\+?(\\d|[1-9]\\d+)(\\.\\d+)?)", "非负浮点数"),
	
	DIGITS("(\\d+)", "数字"), 
	
	LETTER("([A-Za-z]+)", "字母"), 
	
	LETTER_UPPER("([A-Z]+)", "大写字母"), 
	
	LETTER_LOWER("([a-z]+)", "小写字母"), 
	
	DIGITS_LETTER("([0-9A-Za-z]+)", "数字+字母"), 
	
	WORD("(\\w+)", "单词: 字母+数字+下划线"),
	
	USERNAME("([a-zA-Z]\\w{5,})", "用户名: 字母开头, 至少6位, 字母+数字+下划线"),
	
	PASSWORD("([\\w\\-+`=\\[\\];',./~!@#$%^&*()\\|{}:\"<>\\?\\\\]{6,})", "密码: 至少6位, 字母+数字+符号"),
	
	CHINESE("([\u4e00-\u9fa5，。！？：《》…；【】、“”‘’—]+)", "汉字字符"), 
	
	FULL_WIDTH_CHAR("([\uFF00-\uFFFF]*)", "全角字符"), 
	
	EMAIL("([\\w\\-+.]+@[\\w\\-+.]+)", "Email地址"), 
	
	HTTP("(https?://[\\w\\-+\\./\\?%&#=\\*:]+)", "Http地址"), 
	
	TELEPHONE("((\\d{3,4}-)?\\d{5,8})", "电话号码: 区号+号码"), 
	
	MOBILEPHONE("1(\\d{10})", "手机号码"), 
	
	ID_CARD("((\\d{15})|(\\d{17}[\\dxX]))", "身份证号"), 
	
	YEAR("(\\d|[1-9]\\d+)", "年份"),
	
	MONTH("(0?[1-9]|1[0-2])", "月份"), 
	
	DAY("((0?[1-9])|([12]\\d)|3[01])", "天数"), 
	
	DATE(StrUtils.concat("(", YEAR, "-", MONTH, "-", DAY, ")"), "日期"), 
	
	HOUR("(0?\\d|1\\d|2[0-3])", "小时"),
	
	MINUTE("(0?\\d|[1-5]\\d)", "分钟"),
	
	SECOND("(0?\\d|[1-5]\\d)", "秒"),
	
	MILLIS("(\\d{1,3})", "毫秒"),
	
	TIME(StrUtils.concat("(", HOUR, ":", MINUTE, ":", SECOND, "(\\.", MILLIS, ")?)"), "时间"),
	
	DATE_TIME(StrUtils.concat("(", DATE, " ", TIME, ")"), "日期时间"), 
	
	MAC("([a-fA-F\\d]{2}([:\\s-]?[a-fA-F\\d]{2}){5})", "MAC: 物理地址"), 
	
	IPV4("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3})", "IPV4"),
	
	IPV6(StrUtils.concat(
			"(((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|", IPV4, "|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:", IPV4, "|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:", IPV4, ")|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:", IPV4, ")|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:", IPV4, ")|:))|", 
			  "(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:", IPV4, ")|:))|", 
			  "(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:", IPV4, ")|:)))(%.+)?)"), "IPV6"),
	
	PORT("(\\d|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])", "端口"),
		
	SOCKET(StrUtils.concat("(", IPV4, ":", PORT, ")"), "套接字"), 
	
	MD5(StrUtils.concat("(([0-9|A-F|a-f]{16}){1,2})"), "MD5码"), 
	
	;
	
	public String VAL;
	
	public String DES;
	
	private Regex(String val, String des) {
		this.VAL = val;
		this.DES = des;
	}
	
	@Override
	public String toString() {
		return VAL;
	}

}
