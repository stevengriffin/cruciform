package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.utils.Geometry;

public class CruciformWeapon extends Weapon {

	private final static float COOL_DOWN_TIME = 0.5f;
	
	public CruciformWeapon(Engine engine) {
		super(COOL_DOWN_TIME, engine);
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		
	}

	@Override
	void handleFire(Position firerPos) {
		System.out.println("cruc handle fire");
		Entity entity = new Entity();
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(
				firerPos.bounds.getX(), 
				firerPos.bounds.getY(), 
				8, 
				2000);
		entity.add(position);
		Renderer renderer = new Renderer();
		renderer.image = new Texture("cruciform_weapon1.png");
		entity.add(renderer);
		
		engine.addEntity(entity);
	}

}
