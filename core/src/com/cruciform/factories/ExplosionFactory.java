package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.utils.Geometry;

public class ExplosionFactory {

	private final Engine engine;
	
	public ExplosionFactory(final Engine engine) {
		this.engine = engine;
	}
	
	public Entity createRocketExplosion(Entity lastRocketFired) {
		Position oldPos = Position.mapper.get(lastRocketFired);
		float x = oldPos.bounds.getX();
		float y = oldPos.bounds.getY();
		engine.removeEntity(lastRocketFired);
		
		Entity entity = new Entity();
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(x, y, 20, 20);
		entity.add(position);
		
		Renderer renderer = new Renderer();
		renderer.image = new Texture("rocket_explosion.png");
		entity.add(renderer);
		
		engine.addEntity(entity);
		return entity;
	}
}
