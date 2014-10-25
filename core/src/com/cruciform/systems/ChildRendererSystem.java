package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Child;
import com.cruciform.components.Renderer;

public class ChildRendererSystem extends IteratingSystem {

	public ChildRendererSystem() {
		super(Family.getFor(Child.class, Renderer.class));
		this.priority = 6;
	}
	
	public void processEntity(final Entity entity, final float deltaTime) {
		final Child child = Child.mapper.get(entity);
		final Renderer childRenderer = Renderer.mapper.get(entity);
		final Renderer parentRenderer = Renderer.mapper.get(child.parent);
		if (parentRenderer == null) {
			return;
		}
		childRenderer.shouldRender = parentRenderer.shouldRender;
	}
}
