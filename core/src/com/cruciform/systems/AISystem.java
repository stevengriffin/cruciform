package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.AI;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;

public class AISystem extends IteratingSystem {

	public AISystem() {
		super(Family.getFor(Position.class, Shooter.class, AI.class));
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Shooter shooter = Shooter.mapper.get(entity);
		shooter.weapons.get(0).fire(position);
	}
}