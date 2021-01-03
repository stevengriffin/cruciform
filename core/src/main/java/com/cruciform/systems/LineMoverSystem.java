package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.cruciform.components.LineMover;
import com.cruciform.components.Velocity;

public class LineMoverSystem extends IteratingSystem {

	public LineMoverSystem() {
		super(Family.all(LineMover.class, Velocity.class).get());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		LineMover lineMover = LineMover.mapper.getSafe(entity);
		Velocity velocity = Velocity.mapper.getSafe(entity);
		if (lineMover.accelerates) {
			velocity.linear.x = MathUtils.clamp(velocity.linear.x + lineMover.accel.x*deltaTime,
					-lineMover.absMaxVelocity.x, lineMover.absMaxVelocity.x);
			velocity.linear.y = MathUtils.clamp(velocity.linear.y + lineMover.accel.y*deltaTime,
					-lineMover.absMaxVelocity.y, lineMover.absMaxVelocity.y);
		} else {
			velocity.linear = lineMover.maxVelocity;
			velocity.rotational = lineMover.maxRotationalVelocity;
		}
	}

}
