package exp.libs.pattern.state;

/**
 * <PRE>
 * 状态接口.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class IState {

	protected StateMachine stateMachine;
	
	public IState(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public abstract String getDes();
	
	public abstract boolean _start();
	
	public abstract boolean _pause();
	
	public abstract boolean _resume();
	
	public abstract boolean _stop();

}
