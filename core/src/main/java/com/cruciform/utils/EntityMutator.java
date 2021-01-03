package com.cruciform.utils;



import com.badlogicmods.ashley.core.Entity;


public interface EntityMutator {
	public Entity mutate(Entity entity, int index);
}
