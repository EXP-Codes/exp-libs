package exp.libs.warp.ver;

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import exp.libs.envm.Charset;
import exp.libs.envm.DBType;
import exp.libs.envm.Delimiter;
import exp.libs.utils.format.ESCUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.io.JarUtils;
import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.PathUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.db.sql.DBUtils;
import exp.libs.warp.db.sql.SqliteUtils;
import exp.libs.warp.db.sql.bean.DataSourceBean;

/**
 * <PRE>	
 * 版本库管理器
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class _VerDBMgr {
	
	protected final static String APP_NAME = "应用名称";
	
	protected final static String APP_DESC = "应用描述";
	
	protected final static String LAST_VER = "版本号";
	
	protected final static String RELEASE = "定版时间";
	
	protected final static String AUTHOR = "最后责任人";

	/** 版本信息库的脚本 */
	private final static String VER_DB_SCRIPT = "/exp/libs/warp/ver/VERSION-INFO-DB.sql";
	
	/** 版本库库名 */
	private final static String DB_NAME = ".verinfo";
	
	/** 资源目录 */
	private final static String RES_DIR = "./src/main/resources";
	
	/**
	 * 存储版本信息的文件数据库位置.
	 * 	[src/main/resources] 为Maven项目默认的资源目录位置（即使非Maven项目也可用此位置）
	 */
	private final static String VER_DB = RES_DIR.concat("/").concat(DB_NAME);
	
	/** 临时版本库位置（仅用于查看版本信息） */
	private final static String TMP_VER_DB = OSUtils.isRunByTomcat() ? 
			PathUtils.getProjectCompilePath().concat(DB_NAME) : 
			"./conf/".concat(DB_NAME);
	
	/** 版本信息文件的数据源 */
	private DataSourceBean ds;
	
	/** 是否已初始化 */
	private boolean inited;
	
	/** 单例 */
	private static volatile _VerDBMgr instance;
	
	/**
	 * 私有化构造函数
	 */
	private _VerDBMgr() {
		initDS();
		this.inited = false;
	}
	
	/**
	 * 获取单例
	 * @return 单例
	 */
	protected static _VerDBMgr getInstn() {
		if(instance == null) {
			synchronized (_VerDBMgr.class) {
				if(instance == null) {
					instance = new _VerDBMgr();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化版本库数据源
	 */
	private void initDS() {
		this.ds = new DataSourceBean();
		ds.setDriver(DBType.SQLITE.DRIVER);
		ds.setCharset(Charset.UTF8);
		ds.setName(VER_DB);
		
		// 对于非开发环境, Sqlite无法直接读取jar包内的版本库, 需要先将其拷贝到硬盘
		if(!SqliteUtils.testConn(ds)) {
			
			// 对于J2SE项目, 若若同位置存在版本文件, 先删除再复制, 避免复制失败使得显示的版本依然为旧版
			if(!OSUtils.isRunByTomcat()) {
				FileUtils.delete(TMP_VER_DB);	
				JarUtils.copyFile(VER_DB.replace(RES_DIR, ""), TMP_VER_DB);
				
			// 当程序运行在Tomcat时, Tomcat会自动把版本库拷贝到classes目录下, 一般无需再拷贝(但以防万一, 若不存在版本文件还是拷贝一下)
			} else if(!FileUtils.exists(TMP_VER_DB)){
				JarUtils.copyFile(VER_DB.replace(RES_DIR, ""), TMP_VER_DB);
			}
			
			FileUtils.hide(TMP_VER_DB);
			ds.setName(TMP_VER_DB);
		}
	}
	
	/**
	 * 获取版本库数据源
	 * @return 版本库数据源
	 */
	protected DataSourceBean getDS() {
		return ds;
	}
	
	/**
	 * 初始化版本库
	 * @return
	 */
	protected boolean initVerDB() {
		if(inited == true) {
			return true;
		}
		
		boolean isOk = true;
		Connection conn = SqliteUtils.getConn(ds);
		String script = JarUtils.read(VER_DB_SCRIPT, Charset.UTF8);
		try {
			String[] sqls = script.split(";");
			for(String sql : sqls) {
				if(StrUtils.isNotTrimEmpty(sql)) {
					isOk &= DBUtils.execute(conn, sql);
				}
			}
		} catch(Exception e) {
			isOk = false;
			e.printStackTrace();
		}
		
		inited = isOk;
		if(inited == false) {
			System.err.println("初始化项目版本信息库失败");
		}
		
		SqliteUtils.releaseDisk(conn);
		SqliteUtils.close(conn);
		return isOk;
	}
	
	/**
	 * 获取项目版本信息
	 * @return
	 */
	protected Map<String, String> getPrjVerInfo() {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = StrUtils.concat("SELECT S_PROJECT_NAME, S_PROJECT_DESC, ", 
				"S_TEAM_NAME, S_PROJECT_CHARSET, S_DISK_SIZE, S_CACHE_SIZE, ",
				"S_APIS FROM T_PROJECT_INFO ORDER BY I_ID DESC LIMIT 1");
		Map<String, String> prjInfo = SqliteUtils.queryFirstRowStr(conn, sql);
		SqliteUtils.close(conn);
		return prjInfo;
	}
	
	/**
	 * 保存项目信息
	 * @param prjVerInfo
	 * @return
	 */
	protected boolean savePrjInfo(_PrjVerInfo prjVerInfo) {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = "DELETE FROM T_PROJECT_INFO";
		SqliteUtils.execute(conn, sql);
		
		sql = StrUtils.concat("INSERT INTO T_PROJECT_INFO(S_PROJECT_NAME, ", 
				"S_PROJECT_DESC, S_TEAM_NAME, S_PROJECT_CHARSET, S_DISK_SIZE, ", 
				"S_CACHE_SIZE, S_APIS) VALUES(?, ?, ?, ?, ?, ?, ?)");
		boolean isOk = SqliteUtils.execute(conn, sql, new Object[] {
				prjVerInfo.getPrjName(), prjVerInfo.getPrjDesc(), 
				prjVerInfo.getTeamName(), prjVerInfo.getPrjCharset(), 
				prjVerInfo.getDiskSize(), prjVerInfo.getCacheSize(),
				prjVerInfo.getAPIs()
		});
		SqliteUtils.close(conn);
		return isOk;
	}
	
	/**
	 * 获取当前版本信息
	 * @return
	 */
	protected String getCurVerInfo() {
		Connection conn = SqliteUtils.getConn(ds);
		
		final String PRJ_SQL = "SELECT S_PROJECT_NAME, S_PROJECT_DESC FROM T_PROJECT_INFO LIMIT 1";
		Map<String, String> kvs = SqliteUtils.queryFirstRowStr(conn, PRJ_SQL);
		String prjName = kvs.get("S_PROJECT_NAME");
		String prjDesc = kvs.get("S_PROJECT_DESC");
		
		final String VER_SQL = "SELECT S_VERSION, S_DATETIME, S_AUTHOR FROM T_HISTORY_VERSIONS ORDER BY I_ID DESC LIMIT 1";
		kvs = SqliteUtils.queryFirstRowStr(conn, VER_SQL);
		String version = kvs.get("S_VERSION");
		String datetime = kvs.get("S_DATETIME");
		String author = kvs.get("S_AUTHOR");
		
		SqliteUtils.close(conn);
		return toCurVerInfo(prjName, prjDesc, version, datetime, author);
	}
	
	/**
	 * 生成当前版本信息
	 * @param prjName
	 * @param prjDesc
	 * @param version
	 * @param datetime
	 * @param author
	 * @return
	 */
	protected String toCurVerInfo(String prjName, String prjDesc, 
			String version, String datetime, String author) {
		prjName = (prjName == null ? "" : prjName);
		prjDesc = (prjDesc == null ? "" : prjDesc);
		version = (version == null ? "" : version);
		datetime = (datetime == null ? "" : datetime);
		author = (author == null ? "" : author);
		
		List<List<String>> curVerInfo = new LinkedList<List<String>>();
		curVerInfo.add(Arrays.asList(new String[] { APP_NAME, prjName }));
		curVerInfo.add(Arrays.asList(new String[] { "", "" }));
		curVerInfo.add(Arrays.asList(new String[] { APP_DESC, prjDesc }));
		curVerInfo.add(Arrays.asList(new String[] { "", "" }));
		curVerInfo.add(Arrays.asList(new String[] { LAST_VER, version }));
		curVerInfo.add(Arrays.asList(new String[] { "", "" }));
		curVerInfo.add(Arrays.asList(new String[] { RELEASE, datetime }));
		curVerInfo.add(Arrays.asList(new String[] { "", "" }));
		curVerInfo.add(Arrays.asList(new String[] { AUTHOR, author }));
		return ESCUtils.toTXT(curVerInfo, false);
	}
	
	/**
	 * 获取历史版本信息
	 * @return
	 */
	protected List<Map<String, String>> getHisVerInfos() {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = StrUtils.concat("SELECT S_AUTHOR, S_VERSION, S_DATETIME, ", 
				"S_UPGRADE_CONTENT, S_UPGRADE_STEP FROM T_HISTORY_VERSIONS ", 
				"ORDER BY I_ID ASC");
		List<Map<String, String>> hisVerInfos = SqliteUtils.queryKVSs(conn, sql);
		SqliteUtils.close(conn);
		return hisVerInfos;
	}
	
	/**
	 * 生成历史版本信息列表
	 * @return 历史版本信息列表
	 */
	protected String toHisVerInfos() {
		return toHisVerInfos(true);
	}
	
	/**
	 * 生成历史版本信息列表
	 * @param detail 是否生成详单
	 * @return 历史版本信息列表
	 */
	protected String toHisVerInfos(boolean detail) {
		final String SPLIT_LINE = "============================================================";
		StringBuilder infos = new StringBuilder(SPLIT_LINE);
		infos.append(SPLIT_LINE).append(Delimiter.CRLF);
		
		List<Map<String, String>> hisVerInfos = getHisVerInfos();
		for(int i = hisVerInfos.size() - 1; i >= 0; i--) {
			Map<String, String> hisVerInfo = hisVerInfos.get(i);
			String author = hisVerInfo.get("S_AUTHOR");
			String version = hisVerInfo.get("S_VERSION");
			String datetime = hisVerInfo.get("S_DATETIME");
			String upgradeContent = hisVerInfo.get("S_UPGRADE_CONTENT");
			
			infos.append("v").append(version).append(" ");
			infos.append("(").append(datetime).append(") ");
			infos.append("By ").append(author);
			
			if(detail == true) {
				infos.append(" : ").append(Delimiter.CRLF);
				
				String[] lines = upgradeContent.split(Delimiter.LF);
				for(String line : lines) {
					if(StrUtils.isTrimEmpty(line)) {
						continue;
					}
					infos.append("  ").append(line).append(Delimiter.CRLF);
				}
				infos.append(Delimiter.CRLF).append(SPLIT_LINE);
			}
			infos.append(Delimiter.CRLF);
		}
		return infos.toString();
	}
	
	/**
	 * 新增版本信息
	 * @param verInfo
	 * @return
	 */
	protected boolean addVerInfo(_VerInfo verInfo) {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = StrUtils.concat("INSERT INTO T_HISTORY_VERSIONS(", 
				"S_AUTHOR, S_VERSION, S_DATETIME, S_UPGRADE_CONTENT, ", 
				"S_UPGRADE_STEP) VALUES(?, ?, ?, ?, ?)");
		boolean isOk = SqliteUtils.execute(conn, sql, new Object[] {
				verInfo.getAuthor(), verInfo.getVersion(), 
				verInfo.getDatetime(), verInfo.getUpgradeContent(), 
				verInfo.getUpgradeStep()
		});
		SqliteUtils.close(conn);
		return isOk;
	}
	
	/**
	 * 修改当前版本信息
	 * @return
	 */
	protected boolean modifyCurVerInfo(_VerInfo curVerInfo) {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = StrUtils.concat("UPDATE T_HISTORY_VERSIONS ", 
				"SET S_DATETIME = '", curVerInfo.getDatetime(), "', ", 
				"S_UPGRADE_CONTENT = '", curVerInfo.getUpgradeContent(), "', ", 
				"S_UPGRADE_STEP = '", curVerInfo.getUpgradeStep(), "' ", 
				"WHERE S_VERSION = '", curVerInfo.getVersion(), "'");
		boolean isOk = SqliteUtils.execute(conn, sql);
		SqliteUtils.close(conn);
		return isOk;
	}
	
	/**
	 * 删除历史版本信息
	 * @param verInfo 指定版本信息
	 * @return
	 */
	protected boolean delVerInfo(_VerInfo verInfo) {
		Connection conn = SqliteUtils.getConn(ds);
		String sql = StrUtils.concat("DELETE FROM T_HISTORY_VERSIONS ", 
				"WHERE S_VERSION = '", verInfo.getVersion(), "'");
		boolean isOk = SqliteUtils.execute(conn, sql);
		SqliteUtils.close(conn);
		return isOk;
	}
	
}
