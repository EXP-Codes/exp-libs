package exp.libs.warp.net.pf;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 单机数据流-端口转发代理程序.
 * 
 * 使用示例：
 * 	PFConfig pfc = new PFConfig("serverName", localListenPort, remoteIP, remotePort);
 * 	PFAgent agent = new PFAgent(pfc);
 * 	agent._start();
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-07-28
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class PFAgent {

	private Logger log = LoggerFactory.getLogger(PFAgent.class);
	
	private List<_PFServer> servers;
	
	/**
	 * 单个端口转发代理服务
	 * @param localListenPort 本地监听端口
	 * @param remoteIP 远程代理IP（真实服务IP）
	 * @param remotePort 远程代理端口（真实服务端口）
	 */
	public PFAgent(int localListenPort, String remoteIP, int remotePort) {
		this(new PFConfig("", localListenPort, remoteIP, remotePort));
	}
	
	/**
	 * 多个端口转发代理服务
	 * @param serverConfigs
	 */
	public PFAgent(PFConfig... serverConfigs) {
		this.servers = new LinkedList<_PFServer>();
		if(serverConfigs != null) {
			for(PFConfig config : serverConfigs) {
				_PFServer server = new _PFServer(config);
				servers.add(server);
			}
		}
	}
	
	public void _start() {
		boolean isOk = true;
		for(_PFServer server : servers) {
			isOk &= server._start();
		}
		
		if(isOk == true) {
			log.info("所有端口转发服务启动成功");
		} else {
			log.warn("存在端口转发服务启动失败");
		}
	}
	
	public void _stop() {
		for(_PFServer server : servers) {
			server._stop();
		}
		servers.clear();
		
		log.info("所有端口转发服务已停止");
	}
	
}
