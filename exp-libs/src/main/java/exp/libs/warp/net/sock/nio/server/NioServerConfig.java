package exp.libs.warp.net.sock.nio.server;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.nio.common.cache.MsgQueue;
import exp.libs.warp.net.sock.nio.common.cache.NioConfig;
import exp.libs.warp.net.sock.nio.common.filter.ThreadPoolFilter;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.FilterChain;
import exp.libs.warp.net.sock.nio.common.interfaze.IHandler;

/**
 * <pre>
 * NIOSocket服务器配置类。
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class NioServerConfig extends NioConfig {

	protected NioServerConfig(SocketBean socketBean, IHandler handler) {
		super(socketBean, handler);
	}
	
	/**
	 * 服务端默认添加线程池过滤器
	 */
	@Override
	protected void initFilterChain() {
		addFilter(ThreadPoolFilter.class.getSimpleName(), 
				new ThreadPoolFilter(getMaxConnectionCount(), MsgQueue.MAX_MSG_LIMIT));
	}
	
	/**
	 * 获取过滤链
	 * @return
	 */
	protected FilterChain getFilterChain() {
		return filterChain;
	}

}
