package com.cruciform.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Health;
import com.cruciform.components.Position;
import com.cruciform.components.team.Team;
import com.cruciform.utils.Deferrer;

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
					final Position otherPosition = Position.mapper.get(other);
					if (Intersector.overlapConvexPolygons(position.bounds, otherPosition.bounds)) {
						Health otherHealth = Health.mapper.get(other);
						Damager damager = Damager.mapper.get(entity);
						if (otherHealth != null && damager != null) {
							otherHealth.currentHealth -= damager.damage;
						}
						deferrer.run(() -> entity.remove(Collider.class));
					}
				}
			}
		}
	}
}