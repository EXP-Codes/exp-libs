package exp.libs.warp.db.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.os.JavaUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.db.sql.bean.DataSourceBean;
import exp.libs.warp.db.sql.bean.PdmColumn;
import exp.libs.warp.db.sql.bean.PdmTable;
import exp.libs.warp.tpl.Template;

/**
 * <PRE>
 * 数据库工具.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
final class _DBUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(_DBUtils.class);
	
	private final static String TEMPLATE_DB_BEAN = "/exp/libs/warp/db/sql/db-bean.tpl";
	
	private final static String TEMPLATE_PROXOOL = "/exp/libs/warp/db/sql/proxool.tpl";
	
	private Set<String> registeredDS;
	
	private static volatile _DBUtils instance;
	
	private _DBUtils() {
		this.registeredDS = new HashSet<String>(4);
	}
	
	protected static _DBUtils getInstn() {
		if(instance == null) {
			synchronized (_DBUtils.class) {
				if(instance == null) {
					instance = new _DBUtils();
				}
			}
		}
		return instance;
	}
	
	protected boolean registerToProxool(DataSourceBean ds) {
		boolean isOk = false;
		if(ds != null && !StrUtils.isEmpty(ds.getId())) {
			if(registeredDS.contains(ds.getId())) {
				isOk = true;
				
			} else {
				synchronized (registeredDS) {
					if(registeredDS.contains(ds.getId())) {
						isOk = true;
						
					} else {
						try {
							String proxoolXml = createProxoolXml(ds);
							Reader reader = new StringReader(proxoolXml);
							JAXPConfigurator.configure(reader, false);
							registeredDS.add(ds.getId());
							isOk = true;
							
						} catch (Exception e) {
							log.error("注册数据源到proxool连接池失败.", e);
						}
					}
				}
			}
		}
		return isOk;
	}
	
	private static String createProxoolXml(DataSourceBean ds) throws Exception {
		Template tpl = new Template(TEMPLATE_PROXOOL, Charset.ISO);
		tpl.set("alias", ds.getId());
		tpl.set("driver-url", ds.getUrl());
		tpl.set("driver-class", ds.getDriver());
		tpl.set("username", ds.getUsername());
		tpl.set("password", ds.getPassword());
		tpl.set("characterEncoding", ds.getCharset());
		tpl.set("maximum-active-time", String.valueOf(ds.getMaximumActiveTime()));
		tpl.set("house-keeping-test-sql", ds.getHouseKeepingTestSql());
		tpl.set("house-keeping-sleep-time", String.valueOf(ds.getHouseKeepingSleepTime()));
		tpl.set("simultaneous-build-throttle", String.valueOf(ds.getSimultaneousBuildThrottle()));
		tpl.set("maximum-connection-count", String.valueOf(ds.getMaximumConnectionCount()));
		tpl.set("minimum-connection-count", String.valueOf(ds.getMinimumConnectionCount()));
		tpl.set("maximum-new-connections", String.valueOf(ds.getMaximumNewConnections()));
		tpl.set("prototype-count", String.valueOf(ds.getPrototypeCount()));
		tpl.set("maximum-connection-lifetime", String.valueOf(ds.getMaximumConnectionLifetime()));
		tpl.set("test-before-use", String.valueOf(ds.isTestBeforeUse()));
		tpl.set("test-after-use", String.valueOf(ds.isTestAfterUse()));
		tpl.set("trace", String.valueOf(ds.isTrace()));
		return tpl.getContent();
	}
	
	// FIXME: 当表中存在不同类型、但同名属性时，如 i_num 和 s_num， 则去除前缀后所生成的BEAN会报错
	/**
	 * 从数据库中的表信息创建javabean
	 * 
	 * @param conn 数据库连接
	 * @param packageName 导出实体类所在的包名,如：foo.bar.db.bean
	 * @param outDirPath 导出实体类的路径,如：./src/main/java/foo/bar/db/bean
	 * @param exportTableList 选择要导出的表，为空则导出所有表
	 * @throws Exception 异常
	 */
	protected static void createBeanFromDB(Connection conn, String packageName,
			String outDirPath, List<String> exportTableList) throws Exception {
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet tableInfos = getTableInfo(dmd);
		
		//迭代所有表，选择需要生成bean的表进行处理
		while (tableInfos.next()) {
			String tableName = tableInfos.getObject("TABLE_NAME").toString();
			if (tableName.toLowerCase().indexOf("bin$") != -1) {
				continue;
			}
			if (exportTableList != null && exportTableList.size() > 0 && 
					!exportTableList.contains(tableName)) {
				continue;
			}
			
			List<String> colNameList = new LinkedList<String>();//列名表
			Map<String, String> colTypeMap = 					//列名 - java类型
					new HashMap<String, String>();
			
			ResultSet colInfos = getColumnInfo(dmd, tableName);
			while (colInfos.next()) {
				String colType = toJavaType(colInfos.getString("TYPE_NAME"));
				String colName = colInfos.getString(4);
				
				colNameList.add(colName);
				colTypeMap.put(colName, colType);
			}
			
			//获取主键列名
			String pkColumnName = "";
			ResultSet pkRS = dmd.getPrimaryKeys(null, null, tableName);
			if(pkRS.next()) {
				pkColumnName = (String) pkRS.getObject(4);
			}
			
			//生成JavaBean内容
			String outData = createBeanData(tableName, colNameList, colTypeMap,
					pkColumnName, packageName);
			
			//生成JavaBean类文件
			String outFilePath = StrUtils.concat(outDirPath, "/", 
					getHumpTableName(tableName), ".java");
			File outFile = FileUtils.createFile(outFilePath);
			FileUtils.write(outFile, outData, Charset.ISO, false);
		}
	}
	
	/**
	 * 从PDM文件解析表信息创建javabean
	 * 
	 * @param pdmPath PDM文件所在路径
	 * @param conn 数据库连接
	 * @param packageName 导出实体类所在的包名,如：foo.bar.db.bean
	 * @param outDirPath 导出实体类的路径,如：./src/main/java/foo/bar/db/bean
	 * @param exportTableList 选择要导出的表，为空则导出所有表
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	protected static void createBeanFromPDM(String pdmPath, String packageName,
			String outDirPath, List<String> exportTableList) throws Exception {
		List<PdmTable> pdmTableList = new ArrayList<PdmTable>();
		Map<String, String> xmlnsMap = new HashMap<String, String>();
		xmlnsMap.put("o", "object");
		
		SAXReader saxReader = new SAXReader();
		Reader reader = new InputStreamReader(new FileInputStream(pdmPath), Charset.UTF8);
		Document document = saxReader.read(reader);
		
		XPath x = document.createXPath("//o:Table"); // 用XPath解析
		x.setNamespaceURIs(xmlnsMap);
		List<Element> elms = x.selectNodes(document);

		//析取所有表对象
		for (Iterator<Element> elmIts = elms.iterator(); elmIts.hasNext();) {
			Element eTable = elmIts.next();
			PdmTable pdmTable = new PdmTable();
			pdmTable.setTableName(eTable.elementText("Code"));
			
			for (Iterator<Element> eColumnIts = eTable.elementIterator(); 
					eColumnIts.hasNext();) {
				Element eColumn = eColumnIts.next();
				setColumnInfo(pdmTable, eColumn);
				pdmTableList.add(pdmTable);
			}
		}
		
		//迭代所有表，选择需要生成bean的表进行处理
		for (Iterator<PdmTable> iter = pdmTableList.iterator(); iter.hasNext();) {
			PdmTable pdmTable = (PdmTable) iter.next();
			String tableName = pdmTable.getTableName().toString();
			
			if (exportTableList != null && exportTableList.size() > 0 && 
					!exportTableList.contains(tableName)) {
				continue;
			}

			List<String> colNameList = new LinkedList<String>();//列名表
			Map<String, String> colTypeMap = 					//列名 - java类型
					new HashMap<String, String>();
			
			List<PdmColumn> pdmCols = pdmTable.getColumns();
			for (Iterator<PdmColumn> colIts = pdmCols.iterator(); 
					colIts.hasNext();) {
				PdmColumn pdmCol = colIts.next();
				String colName = pdmCol.getName();
				String colType = pdmCol.getType();
				
				colNameList.add(colName);
				colTypeMap.put(colName, colType);
			}
			
			//获取主键列名(FIXME:暂未有获取pdm文件主键的方法)
			String pkColumnName = "";
			
			//生成JavaBean内容
			String outData = createBeanData(tableName, colNameList, colTypeMap,
					pkColumnName, packageName);
			
			//生成JavaBean类文件
			String outFilePath = StrUtils.concat(outDirPath, "/", 
					getHumpTableName(tableName), ".java");
			File outFile = FileUtils.createFile(outFilePath);
			FileUtils.write(outFile, outData, Charset.ISO, false);
		}
	}
	
	/**
	 * 根据模板生成JavaBean类的内容。
	 * 
	 * @param tableName 表名
	 * @param colNameList 表的列名集
	 * @param colTypeMap 每个列对应的类型映射表
	 * @param pkColumnName 主键列名,根据是否为空影响update语句
	 * @param packageName 类所属的包名
	 * @return bean类的内容
	 */
	private static String createBeanData(String tableName, 
			List<String> colNameList, Map<String, String> colTypeMap, 
			String pkColumnName, String packageName) {
		StringBuilder sb = new StringBuilder();
		int colNum = colNameList.size();	//列数
		
		//取类模板
		Template beanClazz = new Template(TEMPLATE_DB_BEAN, Charset.ISO);
		
		//设置年份和日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		beanClazz.set("date", date);
		
		//设置包路径
		beanClazz.set("package_path", packageName);
		
		//设置类名：去前缀的驼峰表名
		String clazzName = getHumpTableName(tableName);
		beanClazz.set("class_name", clazzName);
		
		//设置成员变量
		sb.setLength(0);
		for(String colName : colNameList) {
			String colType = colTypeMap.get(colName);
			String humpColName = getHumpColumnName(colName, false);
			sb.append("    /** ").append(colName).append(" */\r\n");
			sb.append("    private ").append(colType).append(" ");
			sb.append(humpColName).append(";\r\n\r\n");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 "\r\n"
		beanClazz.set("class_member", sb.toString());

		//设置getter setter
		sb.setLength(0);
		for(String colName : colNameList) {
			String colType = colTypeMap.get(colName);
			String uppHumpColName = getHumpColumnName(colName, true);
			String lowHumpColName = getHumpColumnName(colName, false);
			
			//getter
			sb.append("    /**\r\n");
			sb.append("     * get").append(uppHumpColName).append("\r\n");
			sb.append("     * @return ").append(colType).append("\r\n");
			sb.append("     */\r\n");
			sb.append("    public ").append(colType);
			sb.append(" get").append(uppHumpColName).append("() {\r\n");
			sb.append("        return this.").append(lowHumpColName);
			sb.append(";\r\n    }\r\n\r\n");
			
			//setter
			sb.append("    /**\r\n");
			sb.append("     * set").append(uppHumpColName).append("\r\n");
			sb.append("     * @param ").append(lowHumpColName);
			sb.append(" ").append(lowHumpColName).append(" to set\r\n");
			sb.append("     */\r\n");
			sb.append("    public void");
			sb.append(" set").append(uppHumpColName);
			sb.append("(").append(colType).append(" ");
			sb.append(lowHumpColName).append(") {\r\n");
			sb.append("        this.").append(lowHumpColName).append(" = ");
			sb.append(lowHumpColName).append(";\r\n    }\r\n\r\n");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 "\r\n"
		beanClazz.set("getter_and_setter", sb.toString());
		
		//设置表名
		beanClazz.set("table_name", tableName);
		
		//设置insert列
		sb.setLength(0);
		for(String colName : colNameList) {
			sb.append(colName).append(", ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("insert_column", sb.toString());
		
		//设置insert占位符
		sb.setLength(0);
		for(int i = 0; i < colNum; i++) {
			sb.append("?, ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("insert_column_placeholder", sb.toString());
		
		//设置update列
		sb.setLength(0);
		for(String colName : colNameList) {
			if(colNum > 1 && //当只有1列时，不做主键判断
					pkColumnName != null && !"".equals(pkColumnName)) { //不update主键
				if(pkColumnName.equals(colName)) {
					continue;
				}
			}
			sb.append(colName).append(" = ?, ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("update_column", sb.toString());
		
		//设置select列
		sb.setLength(0);
		for(String colName : colNameList) {
			String lowHumpColName = getHumpColumnName(colName, false);
			
			sb.append(colName).append(" AS '");
			sb.append(lowHumpColName).append("', ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("select_column", sb.toString());
		
		//设置 insert用于替换占位符的参数 
		sb.setLength(0);
		for(String colName : colNameList) {
			String uppHumpColName = getHumpColumnName(colName, true);
			
			sb.append("                bean.get").append(uppHumpColName);
			sb.append("(),\r\n");
		}
		sb.setLength(sb.length() - 3);	//移除最后一个 ",\r\n"
		beanClazz.set("insert_params", sb.toString());
		
		//设置 update用于替换占位符的参数 
		sb.setLength(0);
		for(String colName : colNameList) {
			if(colNum > 1 && //当只有1列时，不做主键判断
					pkColumnName != null && !"".equals(pkColumnName)) { //不update主键
				if(pkColumnName.equals(colName)) {
					continue;
				}
			}
			String uppHumpColName = getHumpColumnName(colName, true);
			
			sb.append("                bean.get").append(uppHumpColName);
			sb.append("(),\r\n");
		}
		sb.setLength(sb.length() - 3);	//移除最后一个 ",\r\n"
		beanClazz.set("update_params", sb.toString());
		
		//设置 获取单个数据库字段域名称的方法
		sb.setLength(0);
		for(String colName : colNameList) {
			sb.append("    /**\r\n");
			sb.append("     * get column name\r\n");
			sb.append("     * @return ").append(colName).append("\r\n");
			sb.append("     */\r\n");
			sb.append("    public final static String CN$");
			sb.append(colName.toUpperCase()).append("() {\r\n");
			sb.append("        return \"").append(colName);
			sb.append("\";\r\n    }\r\n\r\n");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 "\r\n"
		beanClazz.set("get_column_name", sb.toString());
		
		//设置 获取单个类成员变量名称的方法
		sb.setLength(0);
		for(String colName : colNameList) {
			String lowHumpColName = getHumpColumnName(colName, false);
			sb.append("    /**\r\n");
			sb.append("     * get java name\r\n");
			sb.append("     * @return ").append(lowHumpColName);
			sb.append("\r\n     */\r\n");
			sb.append("    public final static String JN$");
			sb.append(colName.toUpperCase()).append("() {\r\n");
			sb.append("        return \"").append(lowHumpColName);
			sb.append("\";\r\n    }\r\n\r\n");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 "\r\n"
		beanClazz.set("get_java_name", sb.toString());
		
		//设置 获取所有数据库字段域名称的方法
		sb.setLength(0);
		for(String colName : colNameList) {
			sb.append(colName).append(", ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("all_column_names", sb.toString());
		
		//设置 获取所有类成员变量名称的方法
		sb.setLength(0);
		for(String colName : colNameList) {
			String lowHumpColName = getHumpColumnName(colName, false);
			sb.append(lowHumpColName).append(", ");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 ", "
		beanClazz.set("all_java_names", sb.toString());
		
		//设置 toString方法
		sb.setLength(0);
		for(String colName : colNameList) {
			String uppHumpColName = getHumpColumnName(colName, true);
			String lowHumpColName = getHumpColumnName(colName, false);
			
			sb.append("        sb.append(\"\\t").append(colName);
			sb.append('/').append(lowHumpColName).append("\").append(");
			sb.append("\" = \").append(get");
			sb.append(uppHumpColName).append("()).append(\"\\r\\n\");");
			sb.append("\r\n");
		}
		sb.setLength(sb.length() - 2);	//移除最后一个 "\r\n"
		beanClazz.set("to_string", sb.toString());
		
		//模板占位符替换完成，返回替换内容
		return beanClazz.getContent();
	}
	
	@SuppressWarnings("unchecked")
	private static void setColumnInfo(PdmTable pdmTable, Element eColumns) {
		if (eColumns.getName().equals("Columns")) { // 获取列集合
			List<PdmColumn> columnList = new ArrayList<PdmColumn>();

			for (Iterator<Element> childColumns = eColumns.elementIterator(); 
					childColumns.hasNext();) {
				Element column = childColumns.next();
				PdmColumn pdmCol = new PdmColumn();

				pdmCol.setCode(column.elementText("Code"));
				pdmCol.setName(column.elementText("Name"));
				pdmCol.setType(toJavaType(column.elementText("DataType")));
				pdmCol.setComment(column.elementText("Comment"));
				columnList.add(pdmCol);
			}
			pdmTable.setColumns(columnList);
		}
	}
	
	/**
	 * 把含下划线的表名构造为驼峰结构\.
	 * 不带下划线的表名,剔除前缀并将首字母大写后返回.
	 * 
	 * 若处理完得到的名称是java关键字，则前面补 $
	 * 
	 * @param tableName 原表名
	 * @return 若表名不含下划线则原样返回；否则返回驼峰结构的表名
	 */
	private static String getHumpTableName(final String tableName) {
		String tmpName = tableName;
		
		//含下划线的表名
		if(tmpName != null && tmpName.contains("_")) {
			StringBuffer sb = new StringBuffer();
			
			//若表名以下划线开头，剔除之
			while(tmpName.startsWith("_")) {
				tmpName = tmpName.substring(1);
			}
			
			//若表名以下划线结尾，剔除之，防止下面操作数组越界
			while(tmpName.endsWith("_")) {
				tmpName = tmpName.substring(0, tmpName.length() - 1);
			}
			
			//若表名以下划线开头，剔除之
			while(tmpName.startsWith("_")) {
				tmpName = tmpName.substring(1);
			}
			
			tmpName = tmpName.toLowerCase();
			char[] charArray = tmpName.toCharArray();
			
			//表名首字母大写
			sb.append((char) (charArray[0] - 32));
			
			//把下划线删除，其后的字母转为大写
			for(int i = 1; i < charArray.length; i++) {
				if(charArray[i] == '_') {
					i++;
					sb.append((char) (charArray[i] - 32));
				} else {
					sb.append(charArray[i]);
				}
			}
			tmpName = sb.toString();
			
		//不含下划线的表名
		} else {
			
			//首字母大写
			tmpName = StrUtils.upperAtFirst(tmpName);
		}
		
		// 表名是java关键字，则前面补 $
		if(JavaUtils.isJavaKeyWord(tmpName)) {
			tmpName = "$" + tmpName;
		}
		return tmpName;
	}
	
	/**
	 * 把含下划线的列名构造为驼峰结构.
	 * 不带下划线的列名则只改变首字母大小写.
	 * 
	 * 若处理完得到的名称是java关键字，则前面补 _
	 * 
	 * @param columnName 列名
	 * @param firstUpper 开头字母是否需要大写
	 * @return 驼峰形式列名
	 */
	private static String getHumpColumnName(
			String columnName, boolean firstUpper) {
		String tmpName = columnName;
		
		//含下划线的列名
		if(tmpName != null && tmpName.contains("_")) {
			StringBuilder sb = new StringBuilder();
			
			//若列名以下划开头，剔除之
			while(tmpName.startsWith("_")) {
				tmpName = tmpName.substring(1);
			}
			
			//删除字段类型前缀 I_ 、 S_ 、D_ 等
			if(tmpName.charAt(1) == '_') {
				tmpName = tmpName.substring(2);
			}
			
			//若列名以下划结尾，剔除之，防止下面操作数组越界
			while(tmpName.endsWith("_")) {
				tmpName = tmpName.substring(0, tmpName.length() - 1);
			}
			
			tmpName = tmpName.toLowerCase();
			char[] charArray = tmpName.toCharArray();
			
			//首字母大写
			if(firstUpper == true) {
				sb.append((char) (charArray[0] - 32));
				
			//首字母小写
			} else {
				sb.append(charArray[0]);	
			}
			
			//把下划线删除，其后的字母转为大写
			for(int i = 1; i < charArray.length; i++) {
				if(charArray[i] == '_') {
					i++;
					sb.append((char) (charArray[i] - 32));
				} else {
					sb.append(charArray[i]);
				}
			}
			tmpName = sb.toString();
			
		//不含下划线的列名
		} else {
			if(firstUpper == true) {
				tmpName = StrUtils.upperAtFirst(tmpName);
			} else {
				tmpName = StrUtils.lowerAtFirst(tmpName);
			}
		}
		
		// 表名是java关键字，则前面补 $
		if(JavaUtils.isJavaKeyWord(tmpName)) {
			tmpName = "_" + tmpName;
		}
		return tmpName;
	}
	
	private static ResultSet getTableInfo(DatabaseMetaData dmd) throws SQLException {
		String types[] = { "TABLE" };
		String dbName = dmd.getDatabaseProductName();
		
		ResultSet tableInfos = null;
		if (dbName.toLowerCase().equals("oracle")) {
			tableInfos = dmd.getTables(null, dmd.getUserName().toUpperCase(), null, types);
			
		} else if (dbName.toLowerCase().equals("mysql")) {
			tableInfos = dmd.getTables(null, null, null, types);
			
		} else if (dbName.toLowerCase().equals("sqlite")) {
			tableInfos = dmd.getTables(null, null, null, types);
			
		} else if (dbName.equals("Adaptive Server Enterprise")) {	// sybase
			tableInfos = dmd.getTables(null, null, null, types);
			
		} else if (dbName.toLowerCase().equals("sqlserver")) {
			tableInfos = dmd.getTables(null, null, null, types);
			
		}
		return tableInfos;
	}
	
	private static ResultSet getColumnInfo(DatabaseMetaData dmd,
			String tableName) throws SQLException {
		String dbName = dmd.getDatabaseProductName();
		ResultSet colInfo = null;

		if (dbName.toLowerCase().equals("oracle")) {
			colInfo = dmd.getColumns(null, dmd.getUserName().toUpperCase(), tableName, null);
			
		} else if (dbName.toLowerCase().equals("mysql")) {
			colInfo = dmd.getColumns(null, "%", tableName, "%");
			
		} else if (dbName.toLowerCase().equals("sqlite")) {
			colInfo = dmd.getColumns(null, "%", tableName, "%");
			
		} else if (dbName.equals("Adaptive Server Enterprise")) {	// sybase
			colInfo = dmd.getColumns(null, null, tableName, null);
			
		} else if (dbName.toLowerCase().equals("sqlserver")) {
			colInfo = dmd.getColumns(null, null, tableName, null);
			
		}
		return colInfo;
	}
	
	/**
	 * 数据库的数据类型转换java的数据类型
	 * @param dbType
	 * @return
	 */
	private static String toJavaType(String dbType) {
		String javaType = "";
		dbType = dbType.toLowerCase();

		if (dbType.equals("float")) {
			javaType = "float";
			
		} else if (dbType.equals("double")) {
			javaType = "Double";
			
		} else if (dbType.equals("int") || dbType.equals("smallint") || 
				dbType.equals("mediumint") || dbType.equals("tinyint") || 
				dbType.equals("integer") || dbType.matches("int\\([1-9]\\)")) {
			javaType = "Integer";
			
		} else if (dbType.equals("number") || dbType.equals("long") || 
				dbType.equals("bigint") || dbType.matches("int\\(\\d{2,}\\)")) {
			javaType = "Long";
			
		} else if (dbType.equals("date")) {
			javaType = "java.util.Date";
			
		} else if (dbType.equals("time")) {
			javaType = "java.sql.Time";
			
		} else if (dbType.equals("datetime") || dbType.startsWith("timestamp")) {
			javaType = "java.sql.Timestamp";
			
		} else if (dbType.startsWith("varchar") || dbType.startsWith("char") || 
				dbType.startsWith("varchar2") || dbType.startsWith("nvarchar2")  || 
				dbType.startsWith("longtext") || dbType.startsWith("text") || 
				dbType.equals("mediumtext") || dbType.equals("enum")) {
			javaType = "String";
			
		} else if (dbType.equals("numeric") || dbType.equals("decimal")) {
			javaType = "java.math.BigDecimal";
			
		} else if (dbType.equals("blob")) {
			javaType = "java.sql.Blob";
			
		} else if ("clob".equals(dbType)) {
			javaType = "java.sql.Clob";
			
		} else {
			javaType = "String";
			
		}
		return javaType;
	}
	
}
