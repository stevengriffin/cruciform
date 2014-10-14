package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Health;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.utils.Deferrer;

public class HealthSystem extends IteratingSystem {

	private final Deferrer deferrer;
	private final ExplosionFactory explosionFactory;
	
	public HealthSystem(final ExplosionFactory explosionFactory, final Deferrer deferrer) {
		super(Family.getFor(Health.class));
		this.deferrer = deferrer;
		this.explosionFactory = explosionFactory;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Health health = Health.mapper.get(entity);
		if (health.currentHealth <= 0) {
			deferrer.remove(entity);
			deferrer.run(() -> explosionFactory.createRocketExplosion(entity));
		}
	}
}