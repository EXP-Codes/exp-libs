package exp.libs.pattern.state.impl;

import exp.libs.pattern.state.IState;
import exp.libs.pattern.state.StateMachine;

public class Running extends IState {

	public Running(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public String getDes() {
		return "Running";
	}
	
	@Override
	public boolean _start() {
		System.out.println("StateMachine Illegal Operation : Running -> start()");
		return false;
	}

	@Override
	public boolean _pause() {
		stateMachine.setState(stateMachine.getSleepState());
		return true;
	}

	@Override
	public boolean _resume() {
		System.out.println("StateMachine Illegal Operation : Running -> resume()");
		return false;
	}

	@Override
	public boolean _stop() {
		stateMachine.setState(stateMachine.getStopState());
		return true;
	}
	
	@Override
	public String toString() {
		return "StateMachine State : Running.";
	}

}
