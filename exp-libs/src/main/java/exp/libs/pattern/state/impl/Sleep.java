package exp.libs.pattern.state.impl;

import exp.libs.pattern.state.IState;
import exp.libs.pattern.state.StateMachine;

public class Sleep extends IState {

	public Sleep(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public String getDes() {
		return "Sleep";
	}
	
	@Override
	public boolean _start() {
		System.out.println("StateMachine Illegal Operation : Sleep -> start()");
		return false;
	}

	@Override
	public boolean _pause() {
		System.out.println("StateMachine Illegal Operation : Sleep -> pause()");
		return false;
	}

	@Override
	public boolean _resume() {
		stateMachine.setState(stateMachine.getRunState());
		return true;
	}

	@Override
	public boolean _stop() {
		System.out.println("StateMachine Illegal Operation : Sleep -> stop()");
		return false;
	}

	@Override
	public String toString() {
		return "StateMachine State : Sleep.";
	}
}
