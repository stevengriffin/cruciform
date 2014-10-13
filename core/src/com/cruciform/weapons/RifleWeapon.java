package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.Collider;
import com.cruciform.components.Health;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamRocket;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Geometry;

public class RifleWeapon extends Weapon {

	private static final Texture RIFLE_BULLET_IMAGE = ImageManager.get(Picture.RIFLE_BULLET);
	private final ExplosionFactory explosionFactory;
	
	public RifleWeapon(float coolDownTime, Engine engine, ExplosionFactory explosionFactory) {
		super(coolDownTime, engine);
		this.explosionFactory = explosionFactory;
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void handleFire(Position firerPos) {
		createBullet(firerPos.bounds.getX(), firerPos.bounds.getY());
	}

	private void createBullet(float originX, float originY) {
		System.out.println("Bullet created");
		Entity entity = new Entity();

		Renderer renderer = new Renderer();
		renderer.image = RIFLE_BULLET_IMAGE;
		entity.add(renderer);
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(
				originX, 
				originY, 
				renderer.image.getWidth(),
				renderer.image.getHeight());
		position.isProjectile = true;
		entity.add(position);
		
		Velocity velocity = new Velocity();
		entity.add(velocity);
		
		LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(0, 10.0f);
		lineMover.accelerates = false;
		entity.add(lineMover);
	
		Collider collider = new Collider();
		collider.teamsToCollide.add(TeamEnemy.class);
		entity.add(collider);
	
		/*SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.ROCKET_ZOOM);
		soundEffect.id = soundEffect.sound.play(0.2f * Conf.volume);
		entity.add(soundEffect);*/
		
		engine.addEntity(entity);
	}
	
}
