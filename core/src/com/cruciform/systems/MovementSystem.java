package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Polygon;
import com.cruciform.components.Position;
import com.cruciform.components.Velocity;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;

public class MovementSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public MovementSystem(Deferrer deferrer) {
		super(Family.getFor(Position.class, Velocity.class));
		this.deferrer = deferrer;
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Velocity velocity = Velocity.mapper.get(entity);
		position.bounds.translate(velocity.linear.x, velocity.linear.y);
		if (position.isProjectile && isOutOfBounds(position.bounds)) {
			deferrer.remove(entity);
		}
	}
	
	private boolean isOutOfBounds(Polygon bounds) {
		return bounds.getX() > Conf.screenWidth * 1.3f || bounds.getX() < -Conf.screenWidth * 0.3f
				|| bounds.getY() > Conf.screenHeight * 1.3f || bounds.getY() < -Conf.screenHeight * 0.3f;
	}
}
