package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Child;
import com.cruciform.components.Position;

public class ChildPositionSystem extends IteratingSystem {

	public ChildPositionSystem() {
		super(Family.getFor(Child.class, Position.class));
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Child child = Child.mapper.get(entity);
		Position childPosition = Position.mapper.get(entity);
		Position parentPosition = Position.mapper.get(child.parent);
		if (parentPosition == null) {
			return;
		}
		childPosition.bounds.setPosition(parentPosition.bounds.getX(), parentPosition.bounds.getY());
		childPosition.bounds.setRotation(parentPosition.bounds.getRotation());
	}
}
