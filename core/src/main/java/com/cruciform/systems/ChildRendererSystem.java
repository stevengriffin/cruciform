package com.cruciform.systems;



import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.cruciform.components.Child;
import com.cruciform.components.Renderer;
import org.jetbrains.annotations.Nullable;

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
