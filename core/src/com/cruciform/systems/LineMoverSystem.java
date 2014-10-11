package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.cruciform.components.LineMover;
import com.cruciform.components.Velocity;

public class LineMoverSystem extends IteratingSystem {

	public LineMoverSystem() {
		super(Family.getFor(LineMover.class, Velocity.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		LineMover lineMover = LineMover.mapper.get(entity);
		Velocity velocity = Velocity.mapper.get(entity);
		if (lineMover.accelerates) {
			velocity.linear.x = MathUtils.clamp(velocity.linear.x + lineMover.accel.x,
					-lineMover.absMaxVelocity.x, lineMover.absMaxVelocity.x);
			velocity.linear.y = MathUtils.clamp(velocity.linear.y + lineMover.accel.y,
					-lineMover.absMaxVelocity.y, lineMover.absMaxVelocity.y);
		} else {
			velocity.linear = lineMover.maxVelocity;
		}
	}

}
