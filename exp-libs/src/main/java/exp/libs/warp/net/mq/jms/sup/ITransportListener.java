package exp.libs.warp.net.mq.jms.sup;

import org.apache.activemq.transport.TransportListener;

/**
 * 实时监控连接的接口，对外提供4个调用接口<br/>
 * 
 * <pre>
 * public void onCommand(Object command)<br/>
 * public void onException(IOException error)<br/>
 * public void transportInterupted()<br/>
 * public void transportResumed()<br/>
 * </pre>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public interface ITransportListener extends TransportListener {
	/**
	 * 或许仅仅是为了使用I****来表示接口，该接口由ActiveMq提供，能 实现以上四类事件的监听。
	 */
}
