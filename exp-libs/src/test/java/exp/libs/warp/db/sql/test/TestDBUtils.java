package exp.libs.warp.db.sql.test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import exp.libs.bean.test.TMtFault;
import exp.libs.warp.conf.xml.XConfigFactory;
import exp.libs.warp.conf.xml.XConfig;
import exp.libs.warp.db.sql.DBUtils;
import exp.libs.warp.db.sql.bean.DataSourceBean;

public class TestDBUtils {

	private static XConfig conf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		conf = XConfigFactory.createConfig("test");
		conf.loadConfFile("./conf/conf.xml");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		conf.destroy();
	}

	@Ignore
	public void testDBUtils() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetConn() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetConnByPool() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetConnByJDBC() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetConnNeverOT() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testSetAutoCommit() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateBeanFromDB() {
		DataSourceBean ds = conf.getDataSourceBean("TEST");
		Connection conn = DBUtils.getConn(ds);
		DBUtils.createBeanFromDB(conn, 
				"exp.libs.bean.test", 
				"./src/test/java/exp/libs/bean/test", 
				new String[] {
					"t_mt_ems",
					"t_mt_fault",
		});
		DBUtils.close(conn);
		System.out.println("ok");
	}

	@Ignore
	public void testCreateBeanFromPDM() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testJudgeDBType() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testQueryKvs() {
		fail("Not yet implemented");
	}
	
	@Ignore
	public void testQueryKvo() {
		fail("Not yet implemented");
	}

	@Test
	public void testQuery() {
		DataSourceBean ds = conf.getDataSourceBean("TEST");
		Connection conn = DBUtils.getConn(ds);
		List<TMtFault> list = DBUtils.query(TMtFault.class, conn, TMtFault.SQL_SELECT);
		for(TMtFault bean : list) {
			System.out.println(bean);
		}
		DBUtils.close(conn);
	}

	@Ignore
	public void testQueryNum() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testExecuteConnectionStringObjectArray() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testExecuteConnectionString() {
		fail("Not yet implemented");
	}

}
