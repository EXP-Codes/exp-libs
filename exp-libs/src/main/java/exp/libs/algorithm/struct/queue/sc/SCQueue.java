package exp.libs.algorithm.struct.queue.sc;

import exp.libs.utils.os.ThreadUtils;
import exp.libs.algorithm.struct.queue.loop.LoopQueue;
import exp.libs.warp.thread.ThreadPool;

/**
 * <PRE>
 * 流式并发队列
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SCQueue<E> {

	/** 队列的默认大小 */
	private final static int DEFAULT_QUEUE_SIZE = 16;
	
	private int capacity;
	
	private LoopQueue<SCQBean<E>> queue;
	
	private ThreadPool tp;
	
	public SCQueue(final int capacity) {
		this.capacity = (capacity > 0 ? capacity : DEFAULT_QUEUE_SIZE);
		this.queue = new LoopQueue<SCQBean<E>>(this.capacity);
		this.tp = new ThreadPool(DEFAULT_QUEUE_SIZE);
	}
	
	/**
	 * 阻塞写.
	 * (只允许单线程写)
	 * @param bean 待插入队尾的元素
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean add(SCQBean bean) {
		boolean isOk = false;
		if(bean != null) {
			
			// 阻塞插入
			do { 
				isOk = queue.add(bean);
				if(isOk) { break; }
				ThreadUtils.tSleep(10);
			} while(true);
			
			// 并发处理
			tp.execute(bean);
		}
		return isOk;
	}
	
	/**
	 * 快速写.
	 * (只允许单线程写)
	 * @param bean 待插入队尾的元素
	 * @return 若队列已满则返回false
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addQuickly(SCQBean bean) {
		boolean isOk = false;
		if(bean != null) {
			isOk = queue.add(bean);
			if(isOk) {
				tp.execute(bean);	// 并发处理
			}
		}
		return isOk;
	}
	
	/**
	 * 阻塞读.
	 * (只允许单线程读)
	 * @return 若队列为空则返回null
	 */
	@SuppressWarnings("rawtypes")
	public SCQBean get() {
		SCQBean bean = null;
		do {
			bean = queue.get();	// 试探元素状态
			if(bean == null || bean.isDone()) { break; }
			ThreadUtils.tSleep(10);
		} while(true);
		return (bean == null ? null : queue.take());	// 真正取出元素
	}
	
	/**
	 * 快速读.
	 * (只允许单线程读)
	 * @return 若队列为空则返回null
	 */
	@SuppressWarnings("rawtypes")
	public SCQBean getQuickly() {
		SCQBean bean = queue.get();	// 试探
		if(bean != null && bean.isDone()) {
			bean = queue.take();
			
		} else {
			bean = null;
		}
		return bean;
	}
	
	/**
	 * 释放资源
	 */
	public void clear() {
		queue.clear();
		tp.shutdown();
	}
	
}
