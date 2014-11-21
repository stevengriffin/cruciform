package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Blinker;
import com.cruciform.components.Renderer;

public class BlinkerSystem extends IteratingSystem {

	public BlinkerSystem() {
		super(Family.all(Blinker.class, Renderer.class).get());
		this.priority = 50;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Blinker blinker = Blinker.mapper.getSafe(entity);
		final Renderer renderer = Renderer.mapper.getSafe(entity);
		if (blinker.metro.tick(deltaTime)) {
			renderer.shouldRender = false;
		} else {
			renderer.shouldRender = true;
		}
	}
}
