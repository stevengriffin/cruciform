package com.cruciform.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.SoundEffect;

public class Deferrer {
	private final Engine engine;
	private final List<Entity> entitiesToRemove = new ArrayList<>();
	private final List<Runnable> deferredActions = new ArrayList<>();
	
	public Deferrer(Engine engine) {
		this.engine = engine;
	}
	
	public void remove(Entity entity) {
		SoundEffect soundEffect = SoundEffect.mapper.get(entity);
		if (soundEffect != null) {
			System.out.println("stopping: " + soundEffect.id);
			/*Timer.schedule(new Task(){
			   @Override
			   public void run(){
				   soundEffect.sound.stop(soundEffect.id);
			      }
			   } , 0.1f);*/
			soundEffect.sound.stop(soundEffect.id);
		}
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
