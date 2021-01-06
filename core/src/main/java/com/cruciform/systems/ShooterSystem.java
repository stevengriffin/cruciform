package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;

public class ShooterSystem extends IteratingSystem {

	public ShooterSystem() {
		super(Family.all(Position.class, Shooter.class).get());
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final Position position = Position.mapper.getSafe(entity);
		final Shooter shooter = Shooter.mapper.getSafe(entity);
		shooter.weapons.forEach((w) -> w.update(deltaTime, position));
	}
}