package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.Position;
import com.cruciform.utils.CoolDownMetro;

public abstract class Weapon {
	CoolDownMetro coolDown;
	Engine engine;
	
	public Weapon(float coolDownTime, Engine engine) {
		coolDown = new CoolDownMetro(0.5f);
		this.engine = engine;
	}
	
	public boolean update(float dt, Position firerPos) {
		handleUpdate(dt, firerPos);
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
	
	public float getPercentReady() {
		return coolDown.getPercent();
	}
	
}
