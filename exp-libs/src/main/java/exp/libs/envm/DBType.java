package exp.libs.envm;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 枚举类：数据库类型
 * 	(提供数据库驱动名、以及JDBC-URL模板)
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public enum DBType {

	UNKNOW("unknow", "", ""), 
	
	REDIS("redis", "redis.clients.jedis.Jedis", ""),
	
	PROXOOL("proxool", "org.logicalcobwebs.proxool.ProxoolDriver", StrUtils.concat("proxool.", DBType.PH_ALIAS)), 
	
	MYSQL("mysql", "com.mysql.jdbc.Driver", StrUtils.concat("jdbc:mysql://", DBType.PH_HOST, ":", DBType.PH_PORT, "/", DBType.PH_DBNAME, "?autoReconnect=true&amp;useUnicode=true&amp;zeroDateTimeBehavior=convertToNull&amp;socketTimeout=", DBType.PH_TIMEOUT, "&amp;characterEncoding=", DBType.PH_CHARSET, "")),
	
	SQLITE("sqlite", "org.sqlite.JDBC", StrUtils.concat("jdbc:sqlite:", DBType.PH_DBNAME, "")),
	
	/**
	 * Java JDBC 连接 Oracle有三种方法:
	 * -----------------------------------------
	 * 格式一: 使用ServiceName方式:
	 * 		jdbc:oracle:thin:@//", DBType.PH_HOST, ":", DBType.PH_PORT, "/<service_name>
	 * 	   如： jdbc:oracle:thin:@//xxx.xxx.xxx.xxx:1526/CDEV
	 * 	   注： @后面有//, 这是与使用SID的主要区别。（11g在@后可不加//）
	 *  	这是Oracle推荐的格式, 因为对于集群而言, 每个节点的SID是不一样的, 而SERVICE NAME可以包含所有节点。
	 *  
	 * 格式二: 使用SID方式: 
	 * 		jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":<SID> 
	 *   如： jdbc:oracle:thin:@xxx.xxx.xxx.xxx:1526:CDEV2
	 *   
	 * 格式三：使用TNSName方式: 
	 * 		jdbc:oracle:thin:@<TNSName>
	 * 	   如： jdbc:oracle:thin:@CDEV
	 * 	   注： ORACLE从10.2.0.1后支持TNSNames
	 */
	ORACLE("oracle", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_8I("oracle-8i", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_8I_OCI("oracle-8i-oci", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_9I("oracle-9i", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_10G("oracle-11g", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_10G_OCI("oracle-10g-oci", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_11G("oracle-11g", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	ORACLE_11G_OCI("oracle-11g-oci", "oracle.jdbc.driver.OracleDriver", StrUtils.concat("jdbc:oracle:thin:@", DBType.PH_HOST, ":", DBType.PH_PORT, ":", DBType.PH_DBNAME, "")),
	
	SYBASE("sybase", "com.sybase.jdbc3.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=6")),
	
	SYBASE_IQ("sybase-iq", "com.sybase.jdbc3.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=6")),
	
	SYBASE_ASE("sybase-ase", "com.sybase.jdbc3.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=6")),
	
	SYBASE_125ASE("sybase-125ase", "com.sybase.jdbc3.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=6")),
	
	SYBASE_15ASE("sybase-15ase", "com.sybase.jdbc3.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=6")),
	
	SYBASE_155ASE("sybase-155ase", "com.sybase.jdbc4.jdbc.SybDriver", StrUtils.concat("jdbc:sybase:Tds:", DBType.PH_HOST, ":", DBType.PH_PORT, "?ServiceName=", DBType.PH_DBNAME, "&amp;charset=", DBType.PH_CHARSET, "&amp;jconnect_version=7")),
	
	MSSQL("mssql", "com.microsoft.jdbc.sqlserver.SQLServerDriver", StrUtils.concat("jdbc:microsoft:sqlserver://", DBType.PH_HOST, ":", DBType.PH_PORT, ";DatabaseName=", DBType.PH_DBNAME, "")),
	
	MSSQL2000("mssql2000", "com.microsoft.jdbc.sqlserver.SQLServerDriver", StrUtils.concat("jdbc:microsoft:sqlserver://", DBType.PH_HOST, ":", DBType.PH_PORT, ";DatabaseName=", DBType.PH_DBNAME, "")),
	
	MSSQL2005("mssql2005", "com.microsoft.sqlserver.jdbc.SQLServerDriver", StrUtils.concat("jdbc:microsoft:sqlserver://", DBType.PH_HOST, ":", DBType.PH_PORT, ";DatabaseName=", DBType.PH_DBNAME, "")),
	
	MSSQL2008("mssql2008", "com.microsoft.sqlserver.jdbc.SQLServerDriver", StrUtils.concat("jdbc:microsoft:sqlserver://", DBType.PH_HOST, ":", DBType.PH_PORT, ";DatabaseName=", DBType.PH_DBNAME, "")),
	
	POSTGRESQL("postgresql", "org.postgresql.Driver", StrUtils.concat("jdbc:postgresql://", DBType.PH_HOST, ":", DBType.PH_PORT, "/", DBType.PH_DBNAME, "")),
	
	ACCESS("access", "org.objectweb.rmijdbc.Driver", StrUtils.concat("jdbc:rmi://", DBType.PH_HOST, ":", DBType.PH_PORT, "/jdbc:odbc:", DBType.PH_DBNAME, "")),
	
	INFORMIX("informix", "com.informix.jdbc.IfxDriver", StrUtils.concat("jdbc:informix-sqli://", DBType.PH_HOST, ":", DBType.PH_PORT, "/", DBType.PH_DBNAME, ":INFORMIXSERVER=dbserver;newlocale=en_us,zh_cn;newcodeset=", DBType.PH_CHARSET, "")),
	
	IBM("ibm", "com.ibm.db2.jcc.DB2Driver", StrUtils.concat("jdbc:db2://", DBType.PH_HOST, ":", DBType.PH_PORT, "/", DBType.PH_DBNAME, "")),
	
	;
	
	public final static String PH_ALIAS = "<alias>";
	
	public final static String PH_HOST = "<host>";
	
	public final static String PH_PORT = "<port>";
	
	public final static String PH_DBNAME = "<dbname>";
	
	public final static String PH_TIMEOUT = "<timeout>";
	
	public final static String PH_CHARSET = "<charset>";
	
	public String NAME;
	
	public String DRIVER;
	
	public String JDBCURL;
	
	private DBType(String name, String driver, String jdbcUrl) {
		this.NAME = (name != null ? name.trim().toLowerCase() : "");
		this.DRIVER = driver;
		this.JDBCURL = jdbcUrl;
	}
	
}
