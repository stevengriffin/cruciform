package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Lifetime;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.team.TeamRocket;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;

public class ExplosionFactory {

	private final Engine engine;
	
	public ExplosionFactory(final Engine engine) {
		this.engine = engine;
	}
	
	public Entity createRocketExplosion(final Entity lastRocketFired) {
		final Position oldPos = Position.mapper.get(lastRocketFired);
		final float x = oldPos.bounds.getX();
		final float y = oldPos.bounds.getY();
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer();
		renderer.image = new Texture("rocket_explosion.png");
		entity.add(renderer);

		final Position position = new Position();
		position.bounds = Geometry.polyRect(x, y, renderer.image.getWidth(), renderer.image.getHeight());
		entity.add(position);
	
		final Lifetime lifetime = new Lifetime();
		lifetime.timeRemaining = CoolDownMetro.asPrefired(0.5f);
		entity.add(lifetime);
		
		final Collider collider = new Collider();
		collider.teamsToCollide.add(TeamRocket.class);
		entity.add(collider);
	
		final Damager damager = new Damager();
		damager.damage = 10.0f;
		entity.add(damager);
		
		engine.addEntity(entity);
		return entity;
	}
}
