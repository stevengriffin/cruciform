package com.cruciform.utils;

public class Metro {

	/**A metronome object for abstracting time flow.**/

	private float redTime, greenTime, time, tickTime;
	private boolean state;

	public Metro(float redTime, float greenTime,
			float delay, boolean initState) {
		// How many millis the metro will be in a "false" state.
		this.redTime = redTime;
		// How many millis the metro will be in a "true" state.
		this.greenTime = greenTime;
		this.time = -delay;
		this.state = initState;
		assignTickTime();
	}

	public boolean tick(float dt) {
		//Updates by a millisecond argument and returns its current state.//
		this.time = this.time + dt;
		if (this.time > this.tickTime) {
			this.time = 0;
			this.state = !this.state;
			this.assignTickTime();
		}
		return this.state;
	}

	private void assignTickTime() {
		//Assigns the current state length.//
		if (this.state) {
			this.tickTime = this.greenTime;
		} else {
			this.tickTime = this.redTime;
		}
	}

	public void zero() {
		//Resets the time to 0 seconds.//
		this.time = 0;
	}

	public float getPercentGreen() {
		//Returns the time divided by the greenTime.//
		return this.time / this.greenTime;
	}
	
	public float getTime() {
		return this.time;
	}
}
