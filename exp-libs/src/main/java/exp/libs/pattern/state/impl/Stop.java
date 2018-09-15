package exp.libs.pattern.state.impl;

import exp.libs.pattern.state.IState;
import exp.libs.pattern.state.StateMachine;

public class Stop extends IState {

	public Stop(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public String getDes() {
		return "Stop";
	}
	
	@Override
	public boolean _start() {
		System.out.println("StateMachine Illegal Operation : Stop -> start()");
		return false;
	}

	@Override
	public boolean _pause() {
		System.out.println("StateMachine Illegal Operation : Stop -> pause()");
		return false;
	}

	@Override
	public boolean _resume() {
		System.out.println("StateMachine Illegal Operation : Stop -> resume()");
		return false;
	}

	@Override
	public boolean _stop() {
		System.out.println("StateMachine Illegal Operation : Stop -> stop()");
		return false;
	}

	@Override
	public String toString() {
		return "StateMachine State : Stop.";
	}
}
