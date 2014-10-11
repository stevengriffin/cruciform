package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.cruciform.components.Position;
import com.cruciform.input.InputAction;
import com.cruciform.utils.CoolDownMetro;

public abstract class Weapon implements InputAction {
	CoolDownMetro coolDown;
	Engine engine;
	private boolean shouldFire = false;
	
	public Weapon(float coolDownTime, Engine engine) {
		coolDown = new CoolDownMetro(coolDownTime);
		this.engine = engine;
	}
	
	public boolean update(float dt, Position firerPos) {
		handleUpdate(dt, firerPos);
		if (shouldFire) {
			fire(firerPos);
		}
		return coolDown.tick(dt);
	}
	
	abstract void handleUpdate(float dt, Position firerPos);
	abstract void handleFire(Position firerPos);
	
	public boolean fire(Position firerPos) {
		boolean firedSuccessfully = coolDown.fire();
		if (firedSuccessfully) {
			handleFire(firerPos);
		}
		return firedSuccessfully;
	}
	
	public void setFiring(boolean shouldFire) {
		this.shouldFire  = shouldFire;
	}

	public boolean getFiring() {
		return shouldFire;
	}
	
	public float getPercentReady() {
		return coolDown.getPercent();
	}
	
}
