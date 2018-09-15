package exp.libs.algorithm.struct.queue.sc;

/**
 * <PRE>
 * 流式并发队列元素
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class SCQBean<E> implements Runnable {

	private boolean isDone;
	
	private E e;
	
	private byte[] lock;
	
	public SCQBean(E e) {
		this.isDone = false;
		this.e = e;
		this.lock = new byte[1];
	}
	
	@Override
	public void run() {
		handle(e);
		
		synchronized (lock) {
			isDone = true;
		}
	}
	
	protected abstract void handle(E e);

	public boolean isDone() {
		if(isDone == true) {
			return true;
			
		} else {
			synchronized (lock) {
				return isDone;
			}
		}
	}

	public E getBean() {
		return e;
	}
	
}
