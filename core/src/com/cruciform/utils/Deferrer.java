package com.cruciform.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class Deferrer {
	private final Engine engine;
	private final List<Entity> entitiesToRemove = new ArrayList<>();
	private final List<Runnable> deferredActions = new ArrayList<>();
	
	public Deferrer(Engine engine) {
		this.engine = engine;
	}
	
	public void remove(Entity entity) {
		entitiesToRemove.add(entity);
	}

	public void run(Runnable action) {
		deferredActions.add(action);
	}
	
	public void clear() {
		entitiesToRemove.forEach((e) -> engine.removeEntity(e));
		entitiesToRemove.clear();
		deferredActions.forEach(Runnable::run);
		deferredActions.clear();
	}
}
