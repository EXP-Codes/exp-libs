package exp.libs.pattern.share;

/**
 * <PRE>
 * 临界资源对象
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class SharedResources<RES> extends SharedRoom<RES> {

	/** serialVersionUID */
	private static final long serialVersionUID = -7156609676905101693L;

	/**
	 * 当前持有的资源
	 */
	protected RES res;
	
	/**
	 * 构造函数
	 */
	public SharedResources() {
		init();
	}
	
	/**
	 * 初始化[当前持有的资源].
	 */
	protected abstract void init();  
	
	/**
	 * 向临界空间放入一份[新资源]
	 * @param newRes 新资源
	 * @return 当临界空间中的资源尚未被其他线程取出时, 返回false.
	 */
	public final boolean putIn(RES newRes) {
		return super.add(newRes);
	}
	
	/**
	 * 从临界空间取出一份[资源].
	 * @return 若临界空间为空, 则返回[当前持有的资源]; 反之用[新资源]替代[当前持有的资源], 然后返回之.
	 */
	public final RES takeOut() {
		RES newRes = super.get();
		if(newRes != null) {
			update(newRes);
		}
		return res;
	}
	
	/**
	 * 利用[新资源]更新[当前持有的资源].
	 * 	此方法多线程安全, 且[新资源]绝对不为null.
	 * 
	 * @param newRes 新资源
	 */
	protected abstract void update(RES newRes);  
	
	/**
	 * 返回[当前持有的资源]的数量.
	 */
	public abstract int size();
	
}
