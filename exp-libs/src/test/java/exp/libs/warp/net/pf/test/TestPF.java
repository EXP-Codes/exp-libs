package exp.libs.warp.net.pf.test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import exp.libs.envm.Charset;
import exp.libs.envm.DBType;
import exp.libs.warp.db.sql.DBUtils;
import exp.libs.warp.db.sql.bean.DataSourceBean;

public class TestPF {

	public static void main(String[] args) {
		DataSourceBean ds = new DataSourceBean();
		ds.setAlias("testPF");
		ds.setDriver(DBType.MYSQL.DRIVER);
		ds.setCharset(Charset.UTF8);
		ds.setIp("172.168.10.26");
		ds.setPort(9998);
//		ds.setIp("172.168.10.63");
//		ds.setPort(3306);
		ds.setName("exp_db");
		ds.setUsername("root");
		ds.setPassword("root");
		
		Connection conn = DBUtils.getConn(ds);
		String sql = "SELECT COUNT(1) FROM T_EXP";
		int rst = DBUtils.queryInt(conn, sql);
		System.out.println(rst);
//		DBUtils.close(conn);
//		
//		conn = DBUtils.getConnByJDBC(ds);
		sql = "SELECT * FROM ccic_op_exe_log";
		List<Map<String, String>> datas = DBUtils.queryKVSs(conn, sql);
		System.out.println(datas);
		DBUtils.close(conn);
		
	}
	
}
