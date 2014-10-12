package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.Collider;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamRocket;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;

public class RocketWeapon extends Weapon {

	private final Texture rocketImage = new Texture("rocket.png");
	private final Texture fastRocketImage = new Texture("rocket_fast.png");
	private final ExplosionFactory explosionFactory;
	private Entity lastRocketFired;
	private int timesFired = 0;
	
	public RocketWeapon(float coolDownTime, Engine engine, ExplosionFactory explosionFactory) {
		super(coolDownTime, engine);
		this.explosionFactory = explosionFactory;
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void handleFire(Position firerPos) {
		Texture image;
		timesFired++;
		float maxSpeed;
		if (isFastRocket()) {
			maxSpeed = 40.0f;
			coolDown = CoolDownMetro.asPrefired(1.0f);
			image = fastRocketImage;
		} else if (timesFired % 4 == 2) {
			maxSpeed = 10.0f;
			coolDown = CoolDownMetro.asPrefired(0.3f);
			image = rocketImage;
		} else {
			maxSpeed = 10.0f;
			coolDown = CoolDownMetro.asPrefired(0.1f);
			image = rocketImage;
		}
		createRocket(firerPos.bounds.getX(), firerPos.bounds.getY(), maxSpeed, image);
	}

	private void createRocket(float originX, float originY, float maxSpeed, Texture image) {
		Entity entity = new Entity();

		Renderer renderer = new Renderer();
		renderer.image = image;
		entity.add(renderer);
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(
				originX, 
				originY, 
				renderer.image.getWidth(),
				renderer.image.getHeight());
		entity.add(position);
		
		Velocity velocity = new Velocity();
		entity.add(velocity);
		
		LineMover lineMover = new LineMover();
		lineMover.absMaxVelocity = new Vector2(0, maxSpeed);
		lineMover.accel = new Vector2(0, maxSpeed/50);
		lineMover.accelerates = true;
		entity.add(lineMover);
	
		Collider collider = new Collider();
		entity.add(collider);
	
		TeamRocket team = new TeamRocket();
		entity.add(team);
		
		engine.addEntity(entity);
		lastRocketFired = entity;
	}

	private boolean isFastRocket() {
		return timesFired % 4 == 3;
	}
	
	@Override
	public void setFiring(boolean shouldFire) {
		super.setFiring(shouldFire);
		if (!shouldFire && isFastRocket()) {
			explosionFactory.createRocketExplosion(lastRocketFired);
			engine.removeEntity(lastRocketFired);
		}
	}
	
}
