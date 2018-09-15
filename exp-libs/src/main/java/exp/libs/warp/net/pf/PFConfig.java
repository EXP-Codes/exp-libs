package exp.libs.warp.net.pf;

import exp.libs.utils.other.StrUtils;

/**
 * <pre>
 * (数据流)端口转发代理服务配置
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-07-28
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class PFConfig {

	private final static String DEFAULT_NAME = "DEFAULT";
	
	private final static int DEFAULT_OVERTIME = 10000;
	
	private final static int DEFAULT_MAX_CONN = 100;
	
	private String serverName;
	
	private int localListenPort;
	
	private String remoteIP;
	
	private int remotePort;
	
	private int overtime;
	
	private int maxConn;
	
	public PFConfig(String serverName, int localListenPort, 
			String remoteIP, int remotePort) {
		this(serverName, localListenPort, remoteIP, remotePort, 
				DEFAULT_OVERTIME, DEFAULT_MAX_CONN);
	}
	
	/**
	 * 
	 * @param serverName 代理服务名称（唯一即可）
	 * @param localListenPort 本地监听端口
	 * @param remoteIP 远程代理IP（真实服务IP）
	 * @param remotePort 远程代理端口（真实服务端口）
	 * @param overtime 超时时间(单位ms)
	 * @param maxConn 单个代理服务的最大连接数
	 */
	public PFConfig(String serverName, int localListenPort, 
			String remoteIP, int remotePort, int overtime, int maxConn) {
		this.serverName = StrUtils.isEmpty(serverName) ? DEFAULT_NAME : serverName;
		this.localListenPort = localListenPort;
		this.remoteIP = remoteIP;
		this.remotePort = remotePort;
		this.overtime = (overtime <= 0 ? DEFAULT_OVERTIME : overtime);
		this.maxConn = (maxConn <= 0 ? DEFAULT_MAX_CONN : maxConn);
	}

	public String getServerName() {
		return serverName;
	}

	public int getLocalListenPort() {
		return localListenPort;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public int getOvertime() {
		return overtime;
	}

	public int getMaxConn() {
		return maxConn;
	}

}
