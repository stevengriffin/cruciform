package com.cruciform.systems;

import org.eclipse.jdt.annotation.Nullable;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Child;
import com.cruciform.components.Renderer;

public class ChildRendererSystem extends IteratingSystem {

	public ChildRendererSystem() {
		super(Family.all(Child.class, Renderer.class).get());
		this.priority = 6;
	}
	
	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Child child = Child.mapper.getSafe(entity);
		final Renderer childRenderer = Renderer.mapper.getSafe(entity);
		@Nullable final Renderer parentRenderer = Renderer.mapper.get(child.parent);
		if (parentRenderer == null) {
			return;
		}
		childRenderer.shouldRender = parentRenderer.shouldRender;
	}
}
