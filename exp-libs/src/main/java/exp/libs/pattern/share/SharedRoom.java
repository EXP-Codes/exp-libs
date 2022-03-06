package exp.libs.pattern.share;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 * 临界资源空间 - 读写同步锁解耦.
 * 
 * 	以PC机制替代多线程的同步锁, 实现临界资源的读写解耦.
 * 	只要保证[读频率] > [写频率], 则可保证每次最新的资源都被更新都临界空间.
 * 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class SharedRoom<O> extends ArrayBlockingQueue<O> {

	/** 序列化标识 */
	private static final long serialVersionUID = 5794323087794769975L;
	
	/**
	 * 构造函数
	 */
	public SharedRoom() {
		super(1, 	// 由于模拟临界资源，因此空间永远是1. 
			true);	// 大小为1的空间, 是否FIFO也无所谓.
	}
	
	/**
	 * 往临界资源放入一个资源对象.
	 * （非阻塞操作）
	 * 
	 * @param o 资源对象
	 * @return 是否放入成功 （若队列已满则马上返回false）
	 */
	public final boolean add(O o) {
		boolean isAdd = false;
		if(o != null) {
			isAdd = super.offer(o);
		}
		return isAdd;
	}
	
	/**
	 * 从临界资源取出一个资源对象.
	 * （非阻塞等待）
	 * 
	 * @return 资源对象（若无资源马上返回null）
	 */
	public final O get() {
		return super.poll();
	}
	
	
	//////////////////////////////////////////////////////////////
	// 为了控制队列中的对象，禁止基类提供的其他增减对象的方法  ///////////////////
	//////////////////////////////////////////////////////////////

	/**
	 * 不执行任何处理，永远返回false
	 */
	@Override
	@Deprecated
	public final boolean addAll(Collection<? extends O> objs) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.addAll() has been removed.");
		return false;
	}

	/**
	 * 不执行任何处理，永远返回false
	 */
	@Override
	@Deprecated
	public final boolean offer(O o) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.offer() has been removed.");
		return false;
	}

	/**
	 * 不执行任何处理，永远返回false
	 */
	@Override
	@Deprecated
	public final boolean offer(O o, long timeout, TimeUnit unit) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.offer() has been removed.");
		return false;
	}

	/**
	 * 不执行任何处理
	 */
	@Override
	@Deprecated
	public final void put(O o) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.put() has been removed.");
	}

	/**
	 * 不执行任何处理，永远返回null
	 */
	@Override
	@Deprecated
	public final O poll() {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.poll() has been removed.");
		return null;
	}

	/**
	 * 不执行任何处理，永远返回null
	 */
	@Override
	@Deprecated
	public final O poll(long timeout, TimeUnit unit) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.poll() has been removed.");
		return null;
	}

	/**
	 * 不执行任何处理，永远返回null
	 */
	@Override
	@Deprecated
	public final O take() {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.take() has been removed.");
		return null;
	}

	/**
	 * 不执行任何处理，永远返回null
	 */
	@Override
	@Deprecated
	public final O peek() {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.peek() has been removed.");
		return null;
	}

	/**
	 * 不执行任何处理，永远返回null
	 */
	@Override
	@Deprecated
	public final O remove() {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.remove() has been removed.");
		return null;
	}

	/**
	 * 不执行任何处理，永远返回false
	 */
	@Override
	@Deprecated
	public final boolean remove(Object o) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.remove() has been removed.");
		return false;
	}

	/**
	 * 不执行任何处理，永远返回false
	 */
	@Override
	@Deprecated
	public final boolean removeAll(Collection<?> objs) {
		System.err.println(SharedRoom.class.getName()
				+ " : ArrayBlockingQueue.removeAll() has been removed.");
		return false;
	}
	
}
