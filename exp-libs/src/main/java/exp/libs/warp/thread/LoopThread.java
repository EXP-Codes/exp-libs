package exp.libs.warp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <PRE>
 * 抽象循环线程（需继承使用）.
 * 
 * 使用示例:
 * 
 * 	DemoThread dt = new DemoThread();	// DemoThread 继承 LoopThread
 *  DemoThread 需实现3个方法:
 *    _before:	执行 _start() 后首先触发一次
 *    _loopRun: 执行 _start() 后每1ms触发一次
 *    _after:   执行 _stop()  后触发一次
 * 
 *  dt._start();	// 启动线程
 *  dt._pause();	// 暂停线程
 *  dt._resume();	// 唤醒线程
 *  dt._stop();		// 停止线程
 *  dt._join();		// 线程加塞（某些场景下在_stop后使用, 可令调用线程等待dt线程退出后再退出）
 *  
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class LoopThread extends Thread {
	
	/** 日志器 */
	protected final static Logger log = LoggerFactory.getLogger(LoopThread.class);
	
	/**
	 * 线程默认休眠时间.
	 * 一般不要修改这个值,【默认值1】可以保证在任何数据压下,线程可以用最大效能处理任务,同时又不会永久占用CPU.
	 * 
	 * 这个值若减少到0,会导致CPU占用过频（甚至100%）.
	 * 这个值若增大,会导致线程处理任务能力下降（即会降低线程吞吐量）.
	 * 
	 * 除非机器资源（尤其CPU）很紧张，可考虑适当增加这个值.
	 * -----------------------------------------------------------------------
	 * 
	 * 线程【吞吐量】计算:
	 * 	若线程每秒的【吞入量】为 N，则【吐出量】为  N/DEFAULT_SLEEP_MILLIS
	 *  【吞吐比】 = 【吐出量】 / 【吞入量】
	 *  即当 DEFAULT_SLEEP_MILLIS=1 时, 【吞吐比】为1, 此时线程性能达到峰值.
	 */
	protected final int DEFAULT_SLEEP_MILLIS = 1;
	
	/**
	 * 线程默认加塞等待的最长时间.
	 * 超时则不再加塞等待.
	 */
	protected final int DEFAULT_JOIN_MILLIS = 60000;
	
	/**
	 * 标记线程是否已经运行过（同一线程对象只能被start一次）
	 */
	private volatile boolean hasRun;
	
	/**
	 * 线程停止标识
	 */
	private volatile boolean isStop;
	
	/**
	 * 通知线程暂停标识
	 */
	private volatile boolean isNotifyPause;
	
	/**
	 * 线程暂停标识
	 */
	private volatile boolean isPause;
	
	/**
	 * 暂停锁
	 */
	private byte[] pauseLock;
	
	/**
	 * 构造函数
	 * @param name 线程名称
	 */
	protected LoopThread(final String name) {
		super(name);
		this.hasRun = false;
		this.isStop = true;
		this.isNotifyPause = false;
		this.isPause = false;
		this.pauseLock = new byte[1];
	}
	
	/**
	 * 运行线程.
	 * 固化为模板模式.
	 */
	@Override
	public final void run() {
		_before();
		while(isStop == false) {
			_loopRun();
			_sleep();
			_wait();
		}
		_after();
	}
	
	/**
	 * 线程进入循环体前操作.
	 * 子类实现.
	 */
	protected abstract void _before();
	
	/**
	 * 线程循环执行的循环体.
	 * 子类实现.
	 */
	protected abstract void _loopRun();
	
	/**
	 * 线程离开循环体后操作.
	 * 子类实现.
	 */
	protected abstract void _after();
	
	/**
	 * 启动线程
	 */
	public final synchronized boolean _start() {
		boolean isOk = false;
		if(hasRun == false) {
			hasRun = true;
			isOk = true;
			
			isStop = false;
			super.start();
		}
		return isOk;
	}
	
	/**
	 * 暂停线程
	 */
	public final void _pause() {
		isNotifyPause = true;
	}
	
	/**
	 * 恢复线程
	 */
	public final void _resume() {
		isNotifyPause = false;
		synchronized (pauseLock) {
			pauseLock.notify();
		}
	}
	
	/**
	 * 停止线程
	 */
	public final void _stop() {
		isStop = true;
		_resume();	// 避免通知停止时, 线程已陷入了阻塞状态
	}
	
	/**
	 * 线程加塞.
	 * 一般用于调用层在调用_stop()后, 在调用_join()可确实等待线程真正停止.
	 */
	public final void _join() {
		_join(DEFAULT_JOIN_MILLIS);
	}
	
	/**
	 * 线程加塞.
	 * 一般用于调用层在调用_stop()后, 在调用_join()可确实等待线程真正停止.
	 * @param millis 加塞等待超时(ms)
	 */
	public final void _join(int millis) {
		try {
			super.join(millis);
		} catch (InterruptedException e) {
			log.error("线程加塞异常.", e);
		}
	}
	
	/**
	 * 线程休眠
	 */
	protected void _sleep() {
		_sleep(DEFAULT_SLEEP_MILLIS);
	}
	
	/**
	 * 线程休眠
	 * @param millis 休眠时间(ms)
	 */
	protected final void _sleep(long millis) {
		if(millis > 0) {
			try {
				Thread.sleep(millis);
				
			} catch (InterruptedException e) {
				log.error("线程休眠异常.", e);
			}
		}
	}
	
	/**
	 * 线程阻塞
	 * @param millis 阻塞时长(ms)
	 */
	protected final void _wait(long millis) {
		if(millis > 0) {
			synchronized (pauseLock) {
				try {
					pauseLock.wait(millis);
					
				} catch (InterruptedException e) {
					log.error("线程阻塞异常.", e);
				}
			}
		}
	}
	
	/**
	 * 线程阻塞
	 */
	private final void _wait() {
		if(isNotifyPause == true) {
			synchronized (pauseLock) {
				if(isNotifyPause == true) {
					try {
						isPause = true;
						pauseLock.wait();
						isPause = false;
						
					} catch (InterruptedException e) {
						log.error("线程阻塞异常.", e);
						isNotifyPause = false;
					}
				}
			}
		}
	}
	
	/**
	 * 检测线程是否正在运行
	 * @return true:是; false:否
	 */
	public final boolean isRun() {
		return !isStop && !isPause;
	}
	
	/**
	 * 检测线程是否已停止
	 * @return true:是; false:否
	 */
	public final boolean isStop() {
		return isStop;
	}
	
	/**
	 * 父线程的启动方法.
	 * 已遗弃.调用也不会有任何效果.
	 */
	@Override
	@Deprecated
	public final synchronized void start() {
		System.err.println(LoopThread.class.getName() + 
				" : Thread.start() has been removed.");
	}
	
	/**
	 * 获取线程名称
	 * @return
	 */
	protected String NAME() {
		return getName();
	}
	
}
