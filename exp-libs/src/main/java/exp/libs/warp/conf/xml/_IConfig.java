package exp.libs.warp.conf.xml;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import exp.libs.warp.db.redis.bean.RedisBean;
import exp.libs.warp.db.sql.bean.DataSourceBean;
import exp.libs.warp.net.mq.jms.bean.JmsBean;
import exp.libs.warp.net.sock.bean.SocketBean;

interface _IConfig {

	/** 磁盘上的配置文件 */
	public final static String DISK_FILE = "0";
	
	/** jar包内的配置文件 */
	public final static String JAR_FILE = "1";
	
	public String NAME();
	
	public boolean loadConfFiles(String... confFilePaths);
	
	public Element loadConfFile(String confFilePath);
	
	public boolean loadConfFilesInJar(String... confFilePaths);
	
	public Element loadConfFileInJar(String confFilePath);
	
	public boolean loadConfFilesByTomcat(String... confFilePaths);
	
	public Element loadConfFileByTomcat(String confFilePath);
	
	public XNode getNode(String xPath);
	
	public XNode getNode(String xName, String xId);
	
	public String getVal(String xPath);
	
	public String getVal(String xName, String xId);
	
	public int getInt(String xPath);
	
	public int getInt(String xName, String xId);
	
	public long getLong(String xPath);
	
	public long getLong(String xName, String xId);
	
	public boolean getBool(String xPath);
	
	public boolean getBool(String xName, String xId);
	
	public List<String> getEnums(String xPath);
	
	public List<String> getEnums(String xName, String xId);
	
	public String getAttribute(String xPath, String attributeName);
	
	public String getAttribute(String xName, String xId, String attributeName);
	
	public Map<String, String> getAttributes(String xPath);
	
	public Map<String, String> getAttributes(String xName, String xId);
	
	public DataSourceBean getDataSourceBean(String dsId);
	
	public RedisBean getRedisBean(String redisId);
	
	public SocketBean getSocketBean(String sockId);
	
	public JmsBean getJmsBean(String jmsId);
	
}
