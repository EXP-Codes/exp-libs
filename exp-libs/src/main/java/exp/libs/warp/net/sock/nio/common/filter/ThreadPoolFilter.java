package exp.libs.warp.net.sock.nio.common.filter;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

import exp.libs.warp.net.sock.nio.common.envm.Event;
import exp.libs.warp.net.sock.nio.common.filterchain.INextFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.IFilter;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;
import exp.libs.warp.thread.ThreadPool;

/**
 * <pre>
 * 线程池过滤器，需配合FilterEventExecutor（过滤器事件执行器）使用
 * 
 * 一般建议在服务端使用，
 * 虽然客户端也可以用，但是任务级的多线程会导致客户端发送的消息与接收的消息不配对。
 * 
 * 该过滤器默认对于所有触发事件都会放入线程池。
 * 若有些事件不需要放入线程池处理，只需要继承这个类，重写那些事件的处理逻辑即可。
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public final class ThreadPoolFilter extends ThreadPool implements IFilter {

	/**
	 * <pre>
	 * 构造函数
	 * 
	 * 根据八二定律初始化线程池（80%的任务可以由20%的核心线程处理）
	 * 作为基数的任务量 taskNum = maxClientLinkNum * maxEachClientTaskNum
	 * 则：
	 * 	核心线程数 corePoolSize = taskNum * 20%
	 * 	最大线程数 maxPoolSize = taskNum
	 * 	任务队列容量workQueueSize = taskNum * 80%
	 * 计算值不足1的，按1处理。
	 * 
	 * 若maxClientLinkNum <= 0（不受限连接），则以maxClientLinkNum = 10时处理
	 * 若maxEachClientTaskNum <= 0（不受限连接），则以maxEachClientTaskNum = 10时处理
	 * 
	 * 另外：
	 * 	线程允许空闲时间keepAliveTime = 5s
	 * 	对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
	 * </pre>
	 * @param maxClientLinkNum 最大客户端连接数
	 * @param maxEachClientTaskNum 允许单个客户端连续发送的任务数
	 */
	public ThreadPoolFilter(int maxClientLinkNum, int maxEachClientTaskNum) {
		super(maxClientLinkNum * maxEachClientTaskNum);
	}
	
	/**
	 * <pre>
	 * 构造函数
	 * 
	 * 线程允许空闲时间keepAliveTime = 5s
	 * 对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
	 * </pre>
	 * 
	 * @param corePoolSize 核心线程数
	 * @param maxPoolSize 线程池维护线程的最大数量
	 * @param keepAliveTime 线程池维护线程所允许的空闲时间
	 * @param workQueueSize 线程池所使用的任务队列容量
	 */
	public ThreadPoolFilter(int corePoolSize, int maxPoolSize,
			long keepAliveTime, int workQueueSize) {
		super(corePoolSize, maxPoolSize, keepAliveTime, workQueueSize);
	}

	/**
	 * <pre>
	 * 构造函数
	 * </pre>
	 * 
	 * @param corePoolSize 核心线程数
	 * @param maxPoolSize 线程池维护线程的最大数量
	 * @param keepAliveTime 线程池维护线程所允许的空闲时间
	 * @param unit 线程池维护线程所允许的空闲时间的单位
	 * @param workQueueSize 线程池所使用的任务队列容量
	 * @param reHandler 线程池对拒绝任务的处理策略
	 */
	public ThreadPoolFilter(int corePoolSize, int maxPoolSize,
			long keepAliveTime, TimeUnit unit, int workQueueSize,
			RejectedExecutionHandler reHandler) {
		super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueueSize, reHandler);
	}

	/**
	 * <pre>
	 * 把任务线程放入线程池执行
	 * </pre>
	 * 
	 * @param command 任务线程
	 */
	public void execute(Runnable command) {
		super.execute(command);
	}

	/**
	 * <pre>
	 * 关闭线程池（会自动等待所有线程运行结束再关闭）
	 * </pre>
	 */
	public void shutdown() {
		super.shutdown();
	}

	/**
	 * <pre>
	 * 会话验证事件
	 * 
	 * 使用过滤器事件执行器封装会话验证事件方法为线程，再交由线程池处理。
	 * </pre>
	 * 
	 * @param nextFilter 关系过滤器
	 * @param session 会话
	 * @throws Exception 异常
	 */
	@Override
	public void onSessionCreated(INextFilter nextFilter, ISession session)
			throws Exception {

		FilterEventExecutor feExecutor = new FilterEventExecutor(nextFilter,
				Event.SESSION, session, null, null);
		super.execute(feExecutor);
	}

	/**
	 * <pre>
	 * 消息接收事件
	 * 
	 * 使用过滤器事件执行器封装消息接收事件方法为线程，再交由线程池处理。
	 * </pre>
	 * 
	 * @param nextFilter 关系过滤器
	 * @param session 会话
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	@Override
	public void onMessageReceived(INextFilter nextFilter, ISession session,
			Object msg) throws Exception {

		FilterEventExecutor feExecutor = new FilterEventExecutor(nextFilter,
				Event.MESSAGE_REVC, session, msg, null);
		super.execute(feExecutor);
	}

	/**
	 * <pre>
	 * 消息发送事件
	 * 
	 * 使用过滤器事件执行器封装发送事件方法为线程，再交由线程池处理。
	 * </pre>
	 * 
	 * @param preFilter 关系过滤器
	 * @param session 会话
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	@Override
	public void onMessageSent(INextFilter preFilter, ISession session, Object msg)
			throws Exception {

		FilterEventExecutor feExecutor = new FilterEventExecutor(preFilter,
				Event.MESSAGE_SEND, session, msg, null);
		super.execute(feExecutor);
	}

	/**
	 * <pre>
	 * 异常处理事件
	 * 
	 * 使用过滤器事件执行器封装异常处理事件方法为线程，再交由线程池处理。
	 * </pre>
	 * 
	 * @param nextFilter 关系过滤器
	 * @param session 会话
	 * @param exception 异常
	 */
	@Override
	public void onExceptionCaught(INextFilter nextFilter, ISession session, 
			Throwable exception) {

		FilterEventExecutor feExecutor = new FilterEventExecutor(nextFilter,
				Event.EXCEPTION, null, null, exception);
		super.execute(feExecutor);
	}

	/**
	 * <pre>
	 * 过滤器被移除时清空相关资源
	 * </pre>
	 */
	@Override
	public void clean() {
		super.shutdown();
	}
	
	
	//////////////////////////////////////////////////////////////////////
	/**
	 * <pre>
	 * 过滤器事件执行器
	 * 
	 * 可以把过滤器的事件触发方法封装为线程，以便提交线程池处理。
	 * </pre>	
	 * <br/><B>PROJECT : </B> exp-libs
	 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
	 * @version   2015-12-27
	 * @author    EXP: ***REMOVED***@qq.com
	 * @since     jdk版本：jdk1.6
	 */
	private class FilterEventExecutor implements Runnable {

		/**
		 * 关系过滤器接口
		 */
		private INextFilter filter;

		/**
		 * 事件类型（根据事件类型选择封装的过滤器方法）
		 */
		private Event eventType;

		/**
		 * 会话
		 */
		private ISession session;

		/**
		 * 消息
		 */
		private Object msg;

		/**
		 * 异常
		 */
		private Throwable exception;

		/**
		 * <pre>
		 * 构造函数
		 * </pre>
		 * 
		 * @param filter 关系过滤器接口
		 * @param eventType 事件类型（根据事件类型选择封装的过滤器方法）
		 * @param session 会话
		 * @param msg 消息
		 * @param exception 异常
		 */
		public FilterEventExecutor(INextFilter filter, Event eventType,
				ISession session, Object msg, Throwable exception) {

			this.filter = filter;
			this.eventType = eventType;
			this.session = session;
			this.msg = msg;
			this.exception = exception;
		}

		/**
		 * 线程主体
		 */
		@Override
		public void run() {
			executor();
		}

		/**
		 * 根据事件类型筛选执行方法
		 */
		private void executor() {

			if (Event.SESSION.id == eventType.id) {
				filter.onSessionCreated(session);
				
			} else if (Event.MESSAGE_REVC.id == eventType.id) {
				filter.onMessageReceived(session, msg);
				
			} else if (Event.MESSAGE_SEND.id == eventType.id) {
				filter.onMessageSent(session, msg);
				
			} else if (Event.EXCEPTION.id == eventType.id) {
				filter.onExceptionCaught(session, exception);
			}
		}

	}

}
