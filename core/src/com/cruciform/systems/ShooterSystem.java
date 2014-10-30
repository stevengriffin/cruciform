package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;

public class ShooterSystem extends IteratingSystem {

	public ShooterSystem() {
		super(Family.getFor(Position.class, Shooter.class));
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Shooter shooter = Shooter.mapper.get(entity);
		shooter.weapons.forEach((w) -> w.update(deltaTime, position));
	}
}
