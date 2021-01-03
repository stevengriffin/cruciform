package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.EntityListener;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.cruciform.components.ParticleEmitter;
import com.cruciform.components.Position;
import com.cruciform.utils.Conf;

public class ParticleEmitterSystem extends IteratingSystem implements EntityListener {

	public final static Family family = Family.all(ParticleEmitter.class).get();
	
	public ParticleEmitterSystem() {
		super(family);
		this.priority = 140;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final ParticleEmitter emitter = ParticleEmitter.mapper.getSafe(entity);
		final Position position = Position.mapper.getSafe(entity);
		if (!emitter.coolDown.tick(deltaTime)) {
			emitter.coolDown.fire();
			final PooledEffect effect = emitter.pool.obtain();
			emitter.effects.add(effect);
		}
		emitter.effects.forEach((effect) ->
		effect.setPosition(Conf.playToScreenX(position.bounds.getX()),
				Conf.playToScreenY(position.bounds.getY())));
	}

	@Override
	public void entityAdded(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entityRemoved(Entity entity) {
		final ParticleEmitter particleEmitter = ParticleEmitter.mapper.getSafe(entity);
		// Reset all effects
		for (int i = particleEmitter.effects.size - 1; i >= 0; i--) {
		    particleEmitter.effects.get(i).free();
		}
		particleEmitter.effects.clear();
	}
}
