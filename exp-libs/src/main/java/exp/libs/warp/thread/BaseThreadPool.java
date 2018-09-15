package exp.libs.warp.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 线程池(自定义回调值类型).
 * 
 * 使用示例:
 * 
 *  final int taskNum = 10;
 *  BaseThreadPool&lt;自定义泛型&gt; btp = new BaseThreadPool&lt;自定义泛型&gt;(taskNum);
 *  
 *  btp.execute(实现 Runnable接口 的线程对象);	// 把无回调值的线程注入线程池
 *  Future&lt;自定义泛型&gt; rst = btp.submit(实现 Callable接口 的线程对象);	// 把有回调值的线程注入线程池
 *  
 *  btp.shutdown();		// 通知（但不强制）线程池内所有线程自行销毁
 *  btp.isTerminated();	// 检查线程池内是否所有线程均已销毁
 * 
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BaseThreadPool<T> {

	/**
	 * <pre>
	 * 核心线程数，线程池维护线程的最少数量。
	 * 
	 * 核心线程会一直存活，即使没有任务需要处理。
	 * 当线程数小于核心线程数时，即使现有的线程空闲，
	 * 		线程池也会优先创建新线程来处理任务，而不是直接交给现有的线程处理。
	 * 核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
	 * </pre>
	 */
	private int corePoolSize;

	/**
	 * <pre>
	 * 线程池维护线程的最大数量
	 * 
	 * 当线程数大于或等于核心线程，且任务队列已满时，
	 * 		线程池会创建新的线程，直到线程数量达到maxPoolSize。
	 * 如果线程数已等于maxPoolSize，且任务队列已满，
	 * 		则已超出线程池的处理能力，线程池会拒绝处理任务而抛出异常。
	 * </pre>
	 */
	private int maxPoolSize;

	/**
	 * <pre>
	 * 线程池维护线程所允许的空闲时间
	 * 
	 * 当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。
	 * 如果allowCoreThreadTimeout设置为true，则所有线程均会退出直到线程数量为0。
	 * </pre>
	 */
	private long keepAliveTime;

	/**
	 * 线程池维护线程所允许的空闲时间的单位
	 */
	private TimeUnit unit;

	/**
	 * 线程池所使用的任务队列容量
	 */
	private int workQueueSize;

	/**
	 * 任务队列
	 */
	private BlockingQueue<Runnable> workQueue;

	/**
	 * <pre>
	 * 线程池对拒绝任务的处理策略： 
	 * 	1、AbortPolicy为抛出异常；
	 * 	2、CallerRunsPolicy为重试添加当前的任务，会自动重复调用execute()方法；
	 * 	3、DiscardOldestPolicy为抛弃旧的任务；
	 * 	4、DiscardPolicy为抛弃当前的任务；
	 * </pre>
	 */
	private RejectedExecutionHandler reHandler;

	/**
	 * JDK线程池
	 */
	private ThreadPoolExecutor threadPool;

	/**
	 * <pre>
	 * 构造函数
	 * 
	 * 根据八二定律初始化线程池（80%的任务可以由20%的核心线程处理）
	 * 作为基数的任务量 taskNum
	 * 则：
	 * 	核心线程数 corePoolSize = taskNum * 20%
	 * 	最大线程数 maxPoolSize = taskNum
	 * 	任务队列容量workQueueSize = taskNum * 80%
	 * 计算值不足1的，按1处理。
	 * 
	 * 另外：
	 * 	线程允许空闲时间keepAliveTime = 5s
	 * 	对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
	 * </pre>
	 * @param taskNum 任务数
	 */
	public BaseThreadPool(int taskNum) {
		taskNum = (taskNum <= 0 ? 10 : taskNum);
		this.corePoolSize = (int)(taskNum * 0.2);
		this.corePoolSize = (this.corePoolSize <= 0 ? 1 : this.corePoolSize);
		this.maxPoolSize = taskNum;
		this.workQueueSize = taskNum - this.corePoolSize;
		this.workQueueSize = (this.workQueueSize <= 0 ? 1 : this.workQueueSize);
		
		this.keepAliveTime = 5;
		this.unit = TimeUnit.SECONDS;
		this.reHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		
		this.workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, unit, workQueue, reHandler);
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
	public BaseThreadPool(int corePoolSize, int maxPoolSize,
			long keepAliveTime, int workQueueSize) {

		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.keepAliveTime = keepAliveTime;
		this.unit = TimeUnit.SECONDS;
		this.workQueueSize = workQueueSize;
		this.reHandler = new ThreadPoolExecutor.CallerRunsPolicy();

		this.workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, unit, workQueue, reHandler);
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
	public BaseThreadPool(int corePoolSize, int maxPoolSize,
			long keepAliveTime, TimeUnit unit, int workQueueSize,
			RejectedExecutionHandler reHandler) {

		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.keepAliveTime = keepAliveTime;
		this.unit = unit;
		this.workQueueSize = workQueueSize;
		this.reHandler = reHandler;

		this.workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, unit, workQueue, reHandler);
	}

	/**
	 * <pre>
	 * 把任务线程放入线程池执行
	 * </pre>
	 * 
	 * @param command 任务线程
	 */
	public void execute(Runnable command) {
		threadPool.execute(command);
	}

	/**
	 * <pre>
	 * 把任务线程放入线程池执行（有返回值）
	 * </pre>
	 * 
	 * @param command 任务线程
	 * @return 线程返回值，通过Future.get()方法获取
	 */
	public Future<T> submit(Callable<T> command) {
		return threadPool.submit(command);
	}
	
	/**
	 * <pre>
	 * 获取活动线程数
	 * </pre>
	 * @return 活动线程数
	 */
	public int getActiveCount() {
		return threadPool.getActiveCount();
	}
	
	/**
	 * <pre>
	 * 关闭线程池（会自动等待所有线程运行结束再关闭）
	 * </pre>
	 */
	public void shutdown() {
		threadPool.shutdown();
	}
	
	/**
	 * 判断线程池中的任务是否全部执行完毕（该方法只有在shutdown执行后才会返回true）
	 * @return true:线程池已终止; false:存在线程运行中
	 */
	public boolean isTerminated() {
		return threadPool.isTerminated();
	}
	
}
