package exp.libs.pattern.state;

import exp.libs.pattern.state.impl.Created;
import exp.libs.pattern.state.impl.Running;
import exp.libs.pattern.state.impl.Sleep;
import exp.libs.pattern.state.impl.Stop;


/**
 * <PRE>
 * 状态模式 - 状态机
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class StateMachine {

	private final Created createdState;
	
	private final Running runState;
	
	private final Sleep sleepState;
	
	private final Stop stopState;
	
	protected IState state;
	
	protected StateMachine() {
		this.createdState = new Created(this);
		this.runState = new Running(this);
		this.sleepState = new Sleep(this);
		this.stopState = new Stop(this);
		
		this.state = createdState;
	}
	
	public final void setState(IState state) {
		this.state = state;
	}
	
	public final IState getState() {
		return state;
	}

	public final Created getCreatedState() {
		return createdState;
	}

	public final Running getRunState() {
		return runState;
	}

	public final Sleep getSleepState() {
		return sleepState;
	}

	public final Stop getStopState() {
		return stopState;
	}
	
}
