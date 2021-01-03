package com.cruciform.systems;

import com.badlogicmods.ashley.core.Engine;
import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.EntitySystem;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.TimeUtils;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Health;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamPlayerBody;
import com.cruciform.components.team.TeamSoul;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.Score;
import com.esotericsoftware.minlog.Log;

public class CollisionSystem extends EntitySystem {

	private final Engine engine;
	private final ImmutableArray<Entity> entities;
	private final Deferrer deferrer;
	private final ExplosionFactory explosionFactory;
	
	public CollisionSystem(final Engine engine, final Deferrer deferrer, 
			final ExplosionFactory explosionFactory) {
		this.entities = engine.getEntitiesFor(Family.all(Collider.class, Position.class).get());
		this.engine = engine;
		this.deferrer = deferrer;
		this.explosionFactory = explosionFactory;
	}

	@Override
	public void update(final float deltaTime) {
		for(int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final Collider collider = Collider.mapper.getSafe(entity);
			final Position position = Position.mapper.getSafe(entity);
			for(Class<? extends Team> team : collider.teamsToCollide) {
				// Find all entities that possess a team we want to collide with.
				final ImmutableArray<Entity> entitiesToCollideWith = engine.getEntitiesFor(
						Family.all(Collider.class, Position.class, team).get());
				for (int j = 0; j < entitiesToCollideWith.size(); ++j) {
					final Entity other = entitiesToCollideWith.get(j);
					if (collider.entitiesCollidedWith.contains(other, true)) {
						continue;
					}
					final Position otherPosition = Position.mapper.get(other);
					if (otherPosition == null) {
						continue;
					}
					if (Intersector.overlapConvexPolygons(position.bounds, otherPosition.bounds)) {
						collider.entitiesCollidedWith.add(other);
						performDamagerEvent(entity, other);
						performSplitterEvent(entity, other, otherPosition);
						performSplitterEvent(other, entity, position);
						performScoreEvent(entity, other);
						performGrazeEvent(entity, other);
					}
				}
			}
		}
	}
	
	private void performDamagerEvent(final Entity culprit, final Entity victim) {
		final Health victimHealth = Health.mapper.get(victim);
		final Damager damager = Damager.mapper.get(culprit);
		if (victimHealth != null && damager != null) {
			victimHealth.lastTimeDamaged = TimeUtils.millis();
			final TeamEnemy enemy = TeamEnemy.mapper.get(victim);
			if (enemy != null) {
				Score.incrementFromDamagerEvent(damager.damage, victimHealth.currentHealth);
				final Renderer renderer = Renderer.mapper.get(victim);
				if (renderer != null) {
					tintEntity(victimHealth, renderer);
				}
			}
			victimHealth.currentHealth -= damager.damage;
			Log.debug("Damaged for " + damager.damage);
			explosionFactory.createExplosion(culprit);
		}
	}
	
	private static final Color TINT_COLOR = new Color(0.7f, 0.3f, 0.5f, 1.0f);
	
	private void tintEntity(Health health, Renderer renderer) {
		renderer.tint = TINT_COLOR;
		deferrer.runIfComplete(() -> renderer.tint = null, () -> health.lastTimeDamaged, 0.2f);  
	}

	private void performSplitterEvent(final Entity entity, final Entity other, final Position otherPosition) {
		final Splitter splitter = Splitter.mapper.get(entity);
		if (splitter != null && splitter.splitOnCollision) {
			splitter.splitOnNextUpdate = true;
			final float newY = otherPosition.bounds.getY();
			if (splitter.collisionY == 0.0f || splitter.collisionY > newY) {
				splitter.collisionY = newY;
			}
		}
	}
	
	private void performScoreEvent(final Entity entity, final Entity other) {
		final TeamSoul team = TeamSoul.mapper.get(other);
		if (team == null) {
			return;
		}
		Score.incrementScore(team.pointValue);
		deferrer.remove(other);
		AudioManager.get(Noise.CRUCIFORM).play(Conf.volume*0.2f, 2, 0);
	}
	
	private void performGrazeEvent(final Entity entity, final Entity other) {
		final TeamPlayerBody team = TeamPlayerBody.mapper.get(other);
		if (team == null) {
			return;
		}
		Score.incrementGraze(team.pointValue);
	}
}