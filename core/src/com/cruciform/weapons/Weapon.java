package com.cruciform.weapons;

import org.eclipse.jdt.annotation.NonNull;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.TimeUtils;
import com.cruciform.components.Position;
import com.cruciform.components.team.Team;
import com.cruciform.input.InputAction;
import com.cruciform.utils.CoolDownMetro;

public abstract class Weapon implements InputAction {
	public CoolDownMetro coolDown;
	@NonNull final Engine engine;
	final Class<? extends Team> team;
	private boolean shouldFire = false;
	private boolean justFired = false;
	private long lastTimeFired = 0;
	public final String name;
	public final float damage;
	
	public Weapon(final float coolDownTime, @NonNull final Engine engine, final Class<? extends Team> team,
			final float damage, final String name) {
		coolDown = new CoolDownMetro(coolDownTime);
		this.engine = engine;
		this.team = team;
		this.damage = damage;
		this.name = name;
	}
	
	public boolean update(float dt, Position firerPos) {
		justFired = false;
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
			justFired = true;
			lastTimeFired = TimeUtils.millis();
		}
		return firedSuccessfully;
	}
	
	@Override
	public void setFiring(boolean shouldFire) {
		this.shouldFire  = shouldFire;
	}

	@Override
	public boolean getFiring() {
		return shouldFire;
	}
	
	public float getPercentReady() {
		return coolDown.getPercent();
	}

	public boolean getJustFired() {
		return justFired;
	}

	public long getLastTimeFired() {
		return lastTimeFired;
	}
}
