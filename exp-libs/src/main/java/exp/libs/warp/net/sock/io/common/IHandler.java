package exp.libs.warp.net.sock.io.common;


/**
 * <pre>
 * Socket业务逻辑处理接口(IO-阻塞模式)
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-21
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public interface IHandler {

	/**
	 * <PRE>
	 * 会话登陆逻辑.
	 * 	只有登陆成功的会话才会触发{@link #_handle}方法调用
	 * </PRE>
	 * @param session 客户端代理会话
	 * @return true:登陆成功; false:登陆失败
	 */
	public boolean _login(ISession session);
	
	/**
	 * <PRE>
	 * 业务处理逻辑.
	 * 	此方法只会触发1次, 离开此方法的控制域后, session会话依然存活.
	 * 	默认的session会话关闭方式是 客户端关闭 或 超时关闭, 但根据实际需要也可在此方法内直接close会话
	 * </PRE>
	 * @param session 客户端代理会话
	 */
	public void _handle(ISession session);
	
	/**
	 * <PRE>
	 * 若实现类的构造函数中存在持久化对象(即退出_handle方法后不会被注销的非常量的活动对象), 则必须实现此接口.
	 *  若不实现克隆接口, 此实现类的引用会被 Socket服务端的所有 客户端代理线程共用, 该活动对象可能出现问题.
	 *  
	 * 若实现类的构造函数中不存在持久化对象, 则可以不实现此接口.
	 * 	此时意味着允许 Socket服务端的所有 客户端代理线程共用此实现类的引用.
	 * </PRE>
	 * @return Socket业务逻辑处理器克隆
	 */
	public IHandler _clone();
	
}
