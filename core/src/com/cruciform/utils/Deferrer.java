package com.cruciform.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.Cruciform;
import com.cruciform.components.Blinker;
import com.cruciform.components.Collider;
import com.cruciform.components.Lifetime;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.StateFactory;
import com.cruciform.states.InsertCreditState;

public class Deferrer {
	
	public enum RemovalUrgency {
		URGENT,
		UNIMPORTANT
	}
	
	private final Engine engine;
	private final Cruciform game;
	private final List<Entity> entitiesToRemove = new ArrayList<>();
	private final List<Runnable> deferredActions = new ArrayList<>();
	
	public Deferrer(final Engine engine, final Cruciform game) {
		this.engine = engine;
		this.game = game;
	}
	
	public void remove(final Entity entity) {
		remove(entity, RemovalUrgency.UNIMPORTANT);
	}

	public boolean entityToBeRemoved(final Entity entity) {
		return entitiesToRemove.contains(entity);
	}
	
	private static final int MAX_SPLITS = 4; // Prevent unlimited exponential growth.
	
	public void remove(final Entity entity, final RemovalUrgency urgency) {
		SoundEffect soundEffect = SoundEffect.mapper.get(entity);
		if (soundEffect != null) {
			soundEffect.sound.stop(soundEffect.id);
		}
		TeamPlayer teamPlayer = TeamPlayer.mapper.get(entity);
		if (teamPlayer != null) {
			Timer.schedule(new Task() {
				@Override
				public void run() {
					StateFactory.setState(InsertCreditState.class, game);
				}
			}, 1.0f);
		}
		if (urgency != RemovalUrgency.URGENT) {
			Splitter splitter = Splitter.mapper.get(entity);
			if (splitter != null && splitter.splitOnDeletion) {
				if (splitter.timesSplit >= MAX_SPLITS) {
					entity.remove(Splitter.class);
					entity.remove(Lifetime.class);
					return;
				}
				splitter.splitOnNextUpdate = true;
			}
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
	
	private static final float BLINK_TIME = 0.15f;
	
	public void shieldAndBlink(Entity entity, float duration) {
		final Collider collider = (Collider) entity.remove(Collider.class);
		final Renderer renderer = Renderer.mapper.get(entity);
		if (collider == null || renderer == null) {
			return;
		}
		Blinker blinker = new Blinker();
		blinker.metro = new Metro(BLINK_TIME, BLINK_TIME, 0.0f, false);
		entity.add(blinker);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				entity.add(collider);
				entity.remove(Blinker.class);
				renderer.shouldRender = true;
			}
		}, duration);
	}
}
