package com.cruciform.utils;

public class CoolDownMetro {

	/**Handles a timer that fires, then can't fire for awhile.**/

	private final Metro metro;
	private boolean hasFired = false;

	public static CoolDownMetro asPrefired(final float lifetime) {
		CoolDownMetro life = new CoolDownMetro(lifetime);
		life.fire();
		return life;
	}
	
	public CoolDownMetro(final float coolDown) {
		this.metro = new Metro(0, coolDown, 0, false);
	}

	public boolean tick(final float dt) {
		/*
            Updates the internal metro by the passed milliseconds and sets the
            state.
		 */
		if (this.hasFired) {
			this.hasFired = this.metro.tick(dt);
		}
		if (!this.hasFired && !(this.metro.getTime() == 0.0f)) {
			this.metro.zero();
		}
		return this.hasFired;
	}

	public boolean fire() {
        //Goes to the fired state if possible.
		if (canFire()) {
			hasFired = true;
			return true;
		}
		return false;
	}

	public void forceFire() {
		// Goes to the fired state.
		this.hasFired = true;
	}
	
	public void fizzle() {
		// Goes back to the unfired state prematurely.
		this.hasFired = false;
	}

	public boolean getState() {
		return this.hasFired;
	}

	public boolean canFire() {
		return !this.hasFired;
	}

	public float getPercent() {
		/*
            If in the fired state, returns what fraction of the state we've
            passed. Otherwise returns 1.
		 */
		if (this.hasFired) {
			return this.metro.getPercentGreen();
		} else {
			return 1;
		}
	}

	public float getTime() {
		return this.metro.getTime();
	}

}