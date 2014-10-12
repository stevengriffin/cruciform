package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Lifetime;
import com.cruciform.utils.Deferrer;

public class LifetimeSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public LifetimeSystem(final Deferrer deferrer) {
		super(Family.getFor(Lifetime.class));
		this.deferrer = deferrer;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Lifetime lifetime = Lifetime.mapper.get(entity);
		if (!lifetime.timeRemaining.tick(deltaTime)) {
			deferrer.remove(entity);
		}
	}
}
