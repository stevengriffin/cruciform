package com.cruciform.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.cruciform.components.Splitter;

public class SplitterSystem extends IteratingSystem {

	private final Engine engine;
	
	public SplitterSystem(final Engine engine) {
		super(Family.getFor(Splitter.class));
		this.engine = engine;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Splitter splitter = Splitter.mapper.get(entity);
		if (splitter.splitOnNextUpdate) {
			final ImmutableArray<Component> parentComponents = entity.getComponents();
			for (int i = 0; i < splitter.numberOfNewEntities; i++) {
				Entity child = new Entity();
				for (int j = 0; j < parentComponents.size(); j++) {
					Component component = parentComponents.get(j);
					if (!splitter.componentsToRemoveFromChildren.contains(component.getClass(), true)) {
						// TODO clone, not add
						child.add(component);
					}
				}
				child = splitter.customSplitBehavior.mutate(child, i);
				engine.addEntity(child);
			}
			if (!splitter.deleteOldEntity) {
				engine.removeEntity(entity);
			}
		}
	}
}
