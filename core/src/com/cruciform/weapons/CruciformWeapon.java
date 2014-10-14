package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Damager;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.team.Team;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Geometry;

public class CruciformWeapon extends Weapon {

	private final static float COOL_DOWN_TIME = 3.0f;
	
	public CruciformWeapon(Engine engine, Class<? extends Team> team) {
		super(COOL_DOWN_TIME, engine, team);
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		
	}

	@Override
	void handleFire(Position firerPos) {
		Entity entity = new Entity();
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(
				firerPos.bounds.getX(), 
				firerPos.bounds.getY(), 
				8, 
				2000);
		entity.add(position);
		
		Renderer renderer = new Renderer();
		renderer.image = ImageManager.get(Picture.CRUCIFORM_1);
		entity.add(renderer);
		
		SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.CRUCIFORM);
		soundEffect.id = soundEffect.sound.play(1.0f * Conf.volume);
		entity.add(soundEffect);
		
		addColliderComponent(entity);
		
		Damager damager = new Damager();
		damager.damage = 50.0f;
		entity.add(damager);
		
		engine.addEntity(entity);
	}

}
