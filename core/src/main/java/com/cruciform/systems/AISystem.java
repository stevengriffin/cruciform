package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.cruciform.components.AI;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;
import com.cruciform.weapons.Weapon;

public class AISystem extends IteratingSystem {

	public AISystem() {
		super(Family.all(Position.class, Shooter.class, AI.class).get());
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.getSafe(entity);
		Shooter shooter = Shooter.mapper.getSafe(entity);
		for (Weapon weapon : shooter.weapons) {
			weapon.fire(position);
		}
	}
}
