package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Fader;
import com.cruciform.components.Lifetime;
import com.cruciform.components.Renderer;

public class FaderSystem extends IteratingSystem {

	public FaderSystem() {
		super(Family.all(Fader.class, Lifetime.class, Renderer.class).get());
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Lifetime lifetime = Lifetime.mapper.getSafe(entity);
		final Renderer renderer = Renderer.mapper.getSafe(entity);
		renderer.alpha = lifetime.getPercentAlive();
	}
}
