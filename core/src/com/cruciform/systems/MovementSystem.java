package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Position;
import com.cruciform.components.Velocity;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.Deferrer.RemovalUrgency;

public class MovementSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public MovementSystem(Deferrer deferrer) {
		super(Family.getFor(Position.class, Velocity.class));
		this.deferrer = deferrer;
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Velocity velocity = Velocity.mapper.get(entity);
		velocity.linear.x = applyDrag(velocity.linear.x, velocity.linearDragX, deltaTime);
		velocity.linear.y = applyDrag(velocity.linear.y, velocity.linearDragY, deltaTime);
		position.bounds.translate(velocity.linear.x*deltaTime, velocity.linear.y*deltaTime);
		position.bounds.rotate(velocity.rotational*deltaTime);
		if (position.outOfBoundsHandler.isOutOfBounds(position.bounds)) {
			deferrer.remove(entity, RemovalUrgency.URGENT);
		}
	}
	
	private float applyDrag(float veloc, final float drag, final float deltaTime) {
		if (drag > 0) {
			if (veloc > 0) {
				return Math.max(0, veloc - drag*deltaTime);
			} else if (veloc < 0) {
				return Math.min(0, veloc + drag*deltaTime);
			}
		}
		return veloc;
	}
}
