package com.cruciform.utils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.ashley.core.Entity;

@NonNullByDefault
public interface EntityMutator {
	public Entity mutate(Entity entity, int index);
}
