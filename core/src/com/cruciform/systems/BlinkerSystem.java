package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Blinker;
import com.cruciform.components.Renderer;

public class BlinkerSystem extends IteratingSystem {

	public BlinkerSystem() {
		super(Family.getFor(Blinker.class, Renderer.class));
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Blinker blinker = Blinker.mapper.get(entity);
		final Renderer renderer = Renderer.mapper.get(entity);
		if (blinker.metro.tick(deltaTime)) {
			renderer.shouldRender = false;
		} else {
			renderer.shouldRender = true;
		}
	}
}
