package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.weapons.CruciformWeapon;

public class ObjectFactory {
	private final Engine engine;
	
	public ObjectFactory(final Engine engine) {
		this.engine = engine;
	}
	
	public Entity player(float x, float y) {
		Entity entity = new Entity();
		
		Position position = new Position();
		position.rect = new Rectangle(x, y, 20, 20);
		entity.add(position);
		
		Renderer renderer = new Renderer();
		renderer.image = new Texture("player_ship2.png");
		entity.add(renderer);
		
		PlayerInput playerInput = new PlayerInput();
		entity.add(playerInput);
	
		Shooter shooter = new Shooter();
		shooter.weapon = new CruciformWeapon(engine);
		entity.add(shooter);
		
		engine.addEntity(entity);
		return entity;
	}
}
