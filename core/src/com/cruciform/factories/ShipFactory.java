package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.input.InputCode;
import com.cruciform.utils.Geometry;
import com.cruciform.weapons.CruciformWeapon;
import com.cruciform.weapons.RifleWeapon;
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
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_2);
		entity.add(renderer);
		
		CruciformWeapon cruciform = new CruciformWeapon(engine);
		RocketWeapon rocket = new RocketWeapon(0.1f, engine, explosionFactory);
		RifleWeapon rifle = new RifleWeapon(0.05f, engine, explosionFactory);
		
		Shooter shooter = new Shooter();
		shooter.weapons.add(cruciform);
		shooter.weapons.add(rocket);
		shooter.weapons.add(rifle);
		entity.add(shooter);
		
		PlayerInput playerInput = new PlayerInput();
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.LEFT), cruciform);
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.RIGHT), rocket);
		playerInput.actions.put(InputCode.fromKey(Input.Keys.Z), rifle);
		entity.add(playerInput);
		
		engine.addEntity(entity);
		return entity;
	}
	
	public Entity createEnemy(float x, float y) {
		// TODO incomplete
		Entity entity = new Entity();
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(x, y, 20, 20);
		entity.add(position);
		
		Renderer renderer = new Renderer();
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_1);
		entity.add(renderer);
		
		RifleWeapon rifle = new RifleWeapon(0.05f, engine, explosionFactory);
		
		Shooter shooter = new Shooter();
		shooter.weapons.add(rifle);
		entity.add(shooter);
		
		engine.addEntity(entity);
		return entity;
	}
}
