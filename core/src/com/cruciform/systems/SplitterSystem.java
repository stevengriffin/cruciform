package com.cruciform.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.cruciform.Cruciform;
import com.cruciform.components.Splitter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.minlog.Log;

public class SplitterSystem extends IteratingSystem {

	private final Engine engine;
	private final Kryo kryo;
	
	public SplitterSystem(final Cruciform game) {
		super(Family.getFor(Splitter.class));
		this.engine = game.engine;
		this.kryo = game.kryo;
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
					if (!splitter.componentsToRemoveFromChildren.contains(component.getClass(), false)) {
						child.add(kryo.copy(component));
					}
				}
				child = splitter.customSplitBehavior.mutate(child, i);
				engine.addEntity(child);
			}
			if (splitter.deleteOldEntity) {
				engine.removeEntity(entity);
			} else {
				entity.remove(Splitter.class);
			}
		}
	}
}
