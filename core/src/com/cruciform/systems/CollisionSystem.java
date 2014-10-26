package com.cruciform.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Health;
import com.cruciform.components.Position;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamSoul;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.Score;
import com.esotericsoftware.minlog.Log;

public class CollisionSystem extends EntitySystem {

	private final Engine engine;
	private final ImmutableArray<Entity> entities;
	private final Deferrer deferrer;
	
	public CollisionSystem(final Engine engine, final Deferrer deferrer) {
		this.entities = engine.getEntitiesFor(Family.getFor(Collider.class, Position.class));
		this.engine = engine;
		this.deferrer = deferrer;
	}

	@Override
	public void update(final float deltaTime) {
		for(int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final Collider collider = Collider.mapper.get(entity);
			final Position position = Position.mapper.get(entity);
			for(Class<? extends Team> team : collider.teamsToCollide) {
				// Find all entities that possess a team we want to collide with.
				final ImmutableArray<Entity> entitiesToCollideWith = engine.getEntitiesFor(
						Family.getFor(Collider.class, Position.class, team));
				for (int j = 0; j < entitiesToCollideWith.size(); ++j) {
					final Entity other = entitiesToCollideWith.get(j);
					if (collider.entitiesCollidedWith.contains(other, true)) {
						continue;
					}
					final Position otherPosition = Position.mapper.get(other);
					if (Intersector.overlapConvexPolygons(position.bounds, otherPosition.bounds)) {
						collider.entitiesCollidedWith.add(other);
						performDamagerEvent(entity, other);
						performSplitterEvent(entity, other);
						performSplitterEvent(other, entity);
						performScoreEvent(entity, other);
					}
				}
			}
		}
	}
	
	private void performDamagerEvent(final Entity culprit, final Entity victim) {
		final Health victimHealth = Health.mapper.get(victim);
		final Damager damager = Damager.mapper.get(culprit);
		if (victimHealth != null && damager != null) {
			final TeamEnemy enemy = TeamEnemy.mapper.get(victim);
			if (enemy != null) {
				Score.incrementFromDamagerEvent(damager.damage, victimHealth.currentHealth);
			}
			victimHealth.currentHealth -= damager.damage;
			Log.debug("Damaged for " + damager.damage);
		}
	}
	
	private void performSplitterEvent(final Entity entity, final Entity other) {
		final Splitter splitter = Splitter.mapper.get(entity);
		if (splitter != null && splitter.splitOnCollision) {
			splitter.splitOnNextUpdate = true;
			final float newY = Position.mapper.get(other).bounds.getY();
			// TODO refactor
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
		AudioManager.get(Noise.CRUCIFORM).play(Conf.volume, 2, 0);
	}
}