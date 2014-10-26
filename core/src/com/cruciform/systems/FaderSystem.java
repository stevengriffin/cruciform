package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Fader;
import com.cruciform.components.Lifetime;
import com.cruciform.components.Renderer;

public class FaderSystem extends IteratingSystem {

	public FaderSystem() {
		super(Family.getFor(Fader.class, Lifetime.class, Renderer.class));
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Lifetime lifetime = Lifetime.mapper.get(entity);
		final Renderer renderer = Renderer.mapper.get(entity);
		renderer.alpha = lifetime.getPercentAlive();
	}
}
