package exp.libs.utils.format;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.DateFormat;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.ObjUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 数据格式转换工具.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ESCUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(ESCUtils.class);
	
	/** 私有化构造函数. */
	protected ESCUtils() {}

	/**
	 * <PRE>
	 * 把字符串中的特殊字符转义为xml的转义字符.
	 * 
	 * [&] 转义 [&amp;amp;] 意为 [地址符].
	 * [<] 转义 [&amp;lt;] 意为 [小于].
	 * [>] 转义 [&amp;gt;] 意为 [大于号].
	 * ['] 转义 [&amp;apos;] 意为 [单引号].
	 * ["] 转义 [&amp;quot;] 意为 [双引号].
	 * [ ] 转义 [&amp;nbsp;] 意为 [空格].
	 * [©] 转义 [&amp;copy;] 意为 [版权符].
	 * [®] 转义 [&amp;reg;] 意为 [注册符].
	 * </PRE>
	 * @param str 原字符串 
	 * @return 转义后的字符串
	 */
	public static String toXmlESC(final String str) {
		String xml = "";
		if(str != null) {
			xml = str;
			xml = xml.replace("&", "&amp;");
			xml = xml.replace("<", "&lt;");
			xml = xml.replace(">", "&gt;");
			xml = xml.replace("'", "&apos;");
			xml = xml.replace("\"", "&quot;");
			xml = xml.replace("©", "&copy;");
			xml = xml.replace("®", "&reg;");
		}
		return xml;
	}
	
	/**
	 * <PRE>
	 * 把含有xml转义字符的字符串还原成普通字符串
	 * 
	 * [&amp;amp;] 反转义 [&] 意为 [地址符].
	 * [&amp;lt;] 反转义 [<] 意为 [小于].
	 * [&amp;gt;] 反转义 [>] 意为 [大于号].
	 * [&amp;apos;] 反转义 ['] 意为 [单引号].
	 * [&amp;quot;] 反转义 ["] 意为 [双引号].
	 * [&amp;nbsp;] 反转义 [ ] 意为 [空格].
	 * [&amp;copy;] 反转义 [©] 意为 [版权符].
	 * [&amp;reg;] 反转义 [®] 意为 [注册符].
	 * </PRE>
	 * 
	 * @param 含有xml转义字符的字符串
	 * @return 普通字符串
	 */
	public static String unXmlESC(final String xmlStr) {
		String str = "";
		if(xmlStr != null) {
			str = xmlStr;
			str = str.replace("&amp;", "&");
			str = str.replace("&lt;", "<");
			str = str.replace("&gt;", ">");
			str = str.replace("&apos;", "'");
			str = str.replace("&quot;", "\"");
			str = str.replace("&copy;", "©");
			str = str.replace("&reg;", "®");
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 把字符串中的特殊字符转义为html的转义字符.
	 * </PRE>
	 * @param str 原字符串 
	 * @return 转义后的字符串
	 */
	public static String toHtmlESC(final String str) {
		String html = "";
		if(str != null) {
			html = StringEscapeUtils.escapeHtml4(str);
		}
		return html;
	}
	
	/**
	 * <PRE>
	 * 把含有html转义字符的字符串还原成普通字符串。
	 * （若含有 &amp;#21378; 这里unicode编码也会被还原成原本字符）
	 * </PRE>
	 * 
	 * @param 含有html转义字符的字符串
	 * @return 普通字符串
	 */
	public static String unHtmlESC(final String htmlStr) {
		String str = "";
		if(htmlStr != null) {
			str = StringEscapeUtils.unescapeHtml4(htmlStr);
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 把普通字符串中由[两个连续字符构成的特殊字符]转换为java的[转义字符].
	 * 
	 *  [\\\\] 转义 [\\].
	 * 	[\\t] 转义 [\t].
	 *  [\\r] 转义 [\r].
	 *  [\\n] 转义 [\\n].
	 *  [\\\"] 转义 [\"].
	 *  [\\0] 转义 [\0].
	 *  [\\b] 转义 [\b].
	 *  [\\f] 转义 [\f].
	 * </PRE>
	 *  
	 * @param str 原字符串
	 * @return 含java转义字符的字符串
	 */
	public static String toJavaESC(final String str) {
		String javaStr = "";
		if(str != null) {
			javaStr = str;
			javaStr = javaStr.replace("\\\\", "\\");
			javaStr = javaStr.replace("\\t", "\t");
			javaStr = javaStr.replace("\\r", "\r");
			javaStr = javaStr.replace("\\n", "\n");
			javaStr = javaStr.replace("\\\"", "\"");
			javaStr = javaStr.replace("\\0", "\0");
			javaStr = javaStr.replace("\\b", "\b");
			javaStr = javaStr.replace("\\f", "\f");
		}
		return javaStr;
	}
	
	/**
	 * <PRE>
	 * 为正则表达式中所有特殊字符添加前置反斜杠, 使其转义为普通字符
	 * 
	 * 	[ \ ] -> [ \\ ]
	 * 	[ ( ] -> [ \( ]
	 *  [ ) ] -> [ \) ]
	 *  [ [ ] -> [ \[ ]
	 *  [ ] ] -> [ \] ]
	 *  [ { ] -> [ \{ ]
	 *  [ } ] -> [ \} ]
	 *  [ + ] -> [ \+ ]
	 *  [ - ] -> [ \- ]
	 *  [ . ] -> [ \. ]
	 *  [ * ] -> [ \* ]
	 *  [ ? ] -> [ \? ]
	 *  [ ^ ] -> [ \^ ]
	 *  [ $ ] -> [ \$ ]
	 * </PRE>
	 * 
	 * @param regex 正则表达式
	 * @return 转义后的正则表达式
	 */
	public static String toRegexESC(final String regex) {
		String str = "";
		if(regex != null) {
			str = regex;
			str = str.replace("\\", "\\\\");
			str = str.replace("(", "\\(");
			str = str.replace(")", "\\)");
			str = str.replace("[", "\\[");
			str = str.replace("]", "\\]");
			str = str.replace("{", "\\{");
			str = str.replace("}", "\\}");
			str = str.replace("+", "\\+");
			str = str.replace("-", "\\-");
			str = str.replace(".", "\\.");
			str = str.replace("*", "\\*");
			str = str.replace("?", "\\?");
			str = str.replace("^", "\\^");
			str = str.replace("$", "\\$");
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 为json串中所有特殊字符添加前置反斜杠, 使其转义为普通字符, 同时删除所有空字符
	 * 
	 * 	[ \ ] -> [ \\ ]
	 * 	[ , ] -> [ \, ]
	 *  [ " ] -> [ \" ]
	 *  [ : ] -> [ \: ]
	 *  
	 *  删除: \r \n \b \t \f
	 * </PRE>
	 * 
	 * @param jsonKV java字符
	 * @return json转义字符
	 */
	public static String toJsonESC(final String json) {
		String str = "";
		if(json != null) {
			str = json;
			str = str.replace("\\", "\\\\");
			str = str.replace(",", "\\,");
			str = str.replace("\"", "\\\"");
			str = str.replace(":", "\\:");
			str = str.replace("\r", "");
			str = str.replace("\n", "");
			str = str.replace("\b", "");
			str = str.replace("\t", "");
			str = str.replace("\f", "");
		}
		return str;
	}
	
	/**
	 * <PRE>
	 * 把一个对象转换成json字符串.
	 * 
	 * 	此方法仅适用于简单对象的转换, 如 String、Integer、 Map<?, ?>等， 且Map对象不允许嵌套.
	 * <PRE>
	 * @param obj 简单对象
	 * @return 若转换失败返回""
	 */
	@SuppressWarnings("deprecation")
	public static String toJson(Object obj) {
		String json = "";
		if (obj == null) {
			return json;
		}
			
		StringWriter writer = new StringWriter();
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.YMDHMSS);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setDateFormat(sdf);
		
		try {
			mapper.writeValue(writer, obj);
		} catch (Exception e) {
			log.error("转换JSON失败: {}", obj, e);
		}
		json = writer.toString();
		
		try {
			writer.close();
		} catch (IOException e) {
			log.error("转换JSON异常: 关闭输入流失败.", e);
		}
		return json;
	}
	
	/**
	 * <PRE>
	 * 把一个对象转换成json字符串.
	 * 若对象中存在日期属性, 则使用指定日期格式转换.
	 * 
	 * 	此方法仅适用于简单对象的转换, 如 String、Integer、 Map<?, ?>等， 且Map对象不允许嵌套.
	 * <PRE>
	 * @param obj 简单对象
	 * @param dateFormat 日期格式
	 * @return 若转换失败返回""
	 */
	@SuppressWarnings("deprecation")
	public static String toJson(Object obj, String dateFormat) {
		String json = "";
		if (obj == null) {
			return json;
		}
			
		StringWriter writer = new StringWriter();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setDateFormat(sdf);
		
		try {
			mapper.writeValue(writer, obj);
		} catch (Exception e) {
			log.error("转换JSON失败: {}", obj, e);
		}
		json = writer.toString();
		
		try {
			writer.close();
		} catch (IOException e) {
			log.error("转换JSON异常: 关闭输入流失败.", e);
		}
		return json;
	}

	/**
	 * <PRE>
	 * 把json字符串转换成Map对象.
	 * 
	 * 	此方法仅适用于纯KV键值对的json字符串, 多重嵌套的json字符串可能会转换失败.
	 * <PRE>
	 * @param json 纯KV键值对的json字符串
	 * @return 若转换失败返回null
	 */
	public static Map<?, ?> unJson(String json) {
		Map<?, ?> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(json, LinkedHashMap.class);
			
		} catch (Exception e) {
			log.error("转换JSON失败: {}", json, e);
		}
		return map;
	}
	
	/**
	 * 把二维数组转换成BCP格式字符串
	 * 
	 * @param table 二维数组
	 * @param rowDelimiter 行分隔符
	 * @param colDelimiter 列分隔符
	 * @return BCP字符串
	 */
	public static String toBCP(List<List<Object>> table, 
			String rowDelimiter, String colDelimiter) {
		if(table == null) {
			return "";
		}
		
		rowDelimiter = (rowDelimiter == null ? "" : rowDelimiter);
		colDelimiter = (colDelimiter == null ? "" : colDelimiter);
		
		StringBuilder sb = new StringBuilder();
		for (List<Object> row : table) {
			for (Object col : row) {
				String data = (col == null ? "" : col.toString());
				sb.append(data).append(colDelimiter);
			}

			if (row.size() > 0) {
				sb.setLength(sb.length() - colDelimiter.length());
			}
			sb.append(rowDelimiter);
		}

		if (table.size() > 0) {
			sb.setLength(sb.length() - rowDelimiter.length());
		}
		return sb.toString();
	}

	/**
	 * 把BCP字符串还原成二维数组
	 * 
	 * @param bcpString BCP字符串
	 * @param rowDelimiter 行分隔符
	 * @param colDelimiter 列分隔符
	 * @return 二维数组
	 */
	public static List<List<String>> unBCP(String bcpString, 
			String rowDelimiter, String colDelimiter) {
		bcpString = (bcpString == null ? "" : bcpString);
		rowDelimiter = (rowDelimiter == null ? "" : rowDelimiter);
		
		String[] bcpTable = bcpString.split(rowDelimiter);
		List<List<String>> table = new ArrayList<List<String>>(bcpTable.length);
		for(String bcpListString : bcpTable) {
			table.add(unBCP(bcpListString, colDelimiter));
		}
		return table;
	}
	
	/**
	 * 把一维队列转换成BCP格式字符串
	 * 
	 * @param list 一维队列
	 * @param delimiter 分隔符
	 * @return BCP字符串
	 */
	public static String toBCP(List<Object> list, String delimiter) {
		if(list == null || list.size() <= 0) {
			return "";
		}
		
		delimiter = (delimiter == null ? "" : delimiter);
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			String data = (o == null ? "" : o.toString());
			sb.append(data).append(delimiter);
		}
		sb.setLength(sb.length() - delimiter.length());
		return sb.toString();
	}
	
	/**
	 * 把BCP字符串还原成一维队列
	 * 
	 * @param bcpString BCP字符串
	 * @param delimiter 分隔符
	 * @return 一维队列
	 */
	public static List<String> unBCP(String bcpString, String delimiter) {
		bcpString = (bcpString == null ? "" : bcpString);
		delimiter = (delimiter == null ? "" : delimiter);
		String[] bcpList = bcpString.split(delimiter);
		return Arrays.asList(bcpList);
	}

	/**
	 * 把二维数组转换成CSV字符串
	 * @param table 二维数组
	 * @return CSV字符串
	 */
	public static String toCSV(List<List<Object>> table) {
		if(table == null) {
			return "";
		}
		
		final String rowDelimiter = "\r\n";
		final String colDelimiter = ",";
		
		StringBuilder sb = new StringBuilder();
		for (List<Object> row : table) {
			for (Object col : row) {
				String data = (col == null ? "" : col.toString());
				sb.append(_toCSV(data)).append(colDelimiter);
			}

			if (row.size() > 0) {
				sb.setLength(sb.length() - colDelimiter.length());
			}
			sb.append(rowDelimiter);
		}

		if (table.size() > 0) {
			sb.setLength(sb.length() - rowDelimiter.length());
		}
		return sb.toString();
	}

	/**
	 * 把CSV字符串转换成二维数组
	 * @param csv CSV字符串
	 * @return 二维数组
	 */
	public static List<List<String>> unCsvTable(String csv) {
		csv = (csv == null ? "" : csv);
		final String rowDelimiter = "\r\n";
		
		String[] csvTable = csv.split(rowDelimiter);
		List<List<String>> table = new ArrayList<List<String>>(csvTable.length);
		for(String csvListString : csvTable) {
			table.add(unCsvList(csvListString));
		}
		return table;
	}
	
	/**
	 * 把一维队列转换成CSV字符串
	 * @param list 一维队列
	 * @return CSV字符串
	 */
	public static String toCsv(List<Object> list) {
		if(list == null || list.size() <= 0) {
			return "";
		}
		
		final String delimiter = ",";
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			String data = (o == null ? "" : o.toString());
			sb.append(_toCSV(data)).append(delimiter);
		}
		sb.setLength(sb.length() - delimiter.length());
		return sb.toString();
	}

	/**
	 * 把CSV字符串转换成一维队列
	 * @param csv CSV字符串
	 * @return 一维队列
	 */
	public static List<String> unCsvList(String csv) {
		csv = (csv == null ? "" : csv);
		final String delimiter = ",";
		
		String[] csvList = csv.split(delimiter);
		List<String> list = new ArrayList<String>(csvList.length);
		for(String csvData : csvList) {
			list.add(_unCSV(csvData));
		}
		return list;
	}
	
	/**
	 * 把普通字符串转换成CSV格式字符串
	 * @param s 普通字符串
	 * @return CSV格式字符串
	 */
	private static String _toCSV(String str) {
		return StringEscapeUtils.escapeCsv(str);
	}
	
	/**
	 * 把CSV格式字符串转换成普通字符串
	 * @param csv CSV格式字符串
	 * @return 普通字符串
	 */
	private static String _unCSV(String csv) {
		return StringEscapeUtils.unescapeCsv(csv);
	}
	
	/**
	 * 把二维数组转换成TSV格式字符串
	 * @param table 二维数组
	 * @return TSV格式字符串
	 */
	public static String toTSV(List<List<Object>> table) {
		return toBCP(table, "\r\n", "\t");
	}

	/**
	 * 把TSV格式字符串转换成二维数组
	 * @param tsv TSV字符串
	 * @return 二维数组
	 */
	public static List<List<String>> unTsvTable(String tsv) {
		return unBCP(tsv, "\r\n", "\t");
	}
	
	/**
	 * 把一维队列转换成TSV格式
	 * @param list 一维队列
	 * @return TSV字符串
	 */
	public static String toTsv(List<Object> list) {
		return toBCP(list, "\t");
	}
	
	/**
	 * 把TSV字符串转换成一维队列
	 * @param tsv TSV字符串
	 * @return 一维队列
	 */
	public static List<String> unTsvList(String tsv) {
		return unBCP(tsv, "\t");
	}
	
	/**
	 * <PRE>
	 * 把二维数组转换成TXT表单字符串.
	 * 	生成的TXT表单是行列对齐的,若不对齐则是字体等宽问题, 宋体为非等宽字体， 幼圆则为等宽字体.
	 * <PRE>
	 * 
	 * @param table 二维数组
	 * @param header 是否存在表头(若为true则取第一行为表头)
	 * @return TXT表单字符串
	 */
	public static <T> String toTXT(List<List<T>> table, boolean header) {
		if(table == null || table.size() <= 0) {
			return "";
		}
		
		int rowNum = table.size();
		int colNum = 0;
		for(List<T> row : table) {
			colNum = NumUtils.max(colNum, row.size());
		}
		
		if(colNum <= 0) {
			return "";
		}
		
		String[][] txtTable = new String[rowNum][colNum];
		int[] colLens = new int[colNum];
		Arrays.fill(colLens, 0);
		
		for(int r = 0; r < rowNum; r++) {
			List<T> row = table.get(r);
			for(int c = 0; c < row.size(); c++) {
				T col = row.get(c);
				txtTable[r][c] = ObjUtils.toStr(col);
				colLens[c] = NumUtils.max(colLens[c], 
						StrUtils.chLen(txtTable[r][c]));
			}
		}
		
		StringBuffer txt = new StringBuffer();
		txt.append(getTxtLine(colLens));
		for(int r = 0; r < rowNum; r++) {
			for(int c = 0; c < colNum; c++) {
				if(txtTable[r][c] == null) {
					txtTable[r][c] = "";
				}
				
				txt.append("| ");
				txt.append(txtTable[r][c]);
				int whitespace = colLens[c] - 
						StrUtils.chLen(txtTable[r][c]) + 1;
				txt.append(StrUtils.multiChar(' ', whitespace));
			}
			txt.append("|\r\n");
			
			if(header && r == 0) {
				txt.append(getTxtLine(colLens));
			}
		}
		txt.append(getTxtLine(colLens));
		return txt.toString();
	}
	
	private static String getTxtLine(int[] colLens) {
		StringBuffer line = new StringBuffer();
		for(int i = 0; i < colLens.length; i++) {
			line.append('+');
			line.append(StrUtils.multiChar('-', (colLens[i] + 2)));
		}
		line.append("+\r\n");
		return line.toString();
	}
	
}
