package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.input.InputCode;
import com.cruciform.utils.Geometry;
import com.cruciform.weapons.CruciformWeapon;
import com.cruciform.weapons.RocketWeapon;

public class ShipFactory {
	private final Engine engine;
	private final ExplosionFactory explosionFactory;
	
	public ShipFactory(final Engine engine, final ExplosionFactory explosionFactory) {
		this.engine = engine;
		this.explosionFactory = explosionFactory;
	}
	
	public Entity createPlayer(float x, float y) {
		Entity entity = new Entity();
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(x, y, 20, 20);
		entity.add(position);
		
		Renderer renderer = new Renderer();
		renderer.image = new Texture("player_ship2.png");
		entity.add(renderer);
		
		CruciformWeapon cruciform = new CruciformWeapon(engine);
		RocketWeapon rocket = new RocketWeapon(0.1f, engine, explosionFactory);
		
		Shooter shooter = new Shooter();
		shooter.weapons.add(cruciform);
		shooter.weapons.add(rocket);
		entity.add(shooter);
		
		PlayerInput playerInput = new PlayerInput();
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.LEFT), cruciform);
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.RIGHT), rocket);
		entity.add(playerInput);
		
		engine.addEntity(entity);
		return entity;
	}
}
