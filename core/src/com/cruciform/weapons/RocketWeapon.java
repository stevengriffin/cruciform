package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Health;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.components.team.TeamRocket;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;
import com.cruciform.utils.Priority;

public class RocketWeapon extends Weapon {

	private static final TextureRegion ROCKET_IMAGE = ImageManager.get(Picture.ROCKET);
	private static final TextureRegion FAST_ROCKET_IMAGE = ImageManager.get(Picture.ROCKET_FAST);
	private final ExplosionFactory explosionFactory;
	private Entity lastRocketFired;
	private int timesFired = -1;
	
	public RocketWeapon(float coolDownTime, Engine engine, ExplosionFactory explosionFactory,
			Class<? extends Team> team) {
		super(coolDownTime, engine, team, 100.0f, "Rocket");
		this.explosionFactory = explosionFactory;
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void handleFire(Position firerPos) {
		TextureRegion image;
		timesFired++;
		float maxSpeed;
		if (isFastRocket()) {
			maxSpeed = 2400.0f;
			coolDown = CoolDownMetro.asPrefired(1.0f);
			image = FAST_ROCKET_IMAGE;
		} else if (timesFired % 4 == 2) {
			maxSpeed = 600.0f;
			coolDown = CoolDownMetro.asPrefired(0.3f);
			image = ROCKET_IMAGE;
		} else {
			maxSpeed = 600.0f;
			coolDown = CoolDownMetro.asPrefired(0.1f);
			image = ROCKET_IMAGE;
		}
		createRocket(firerPos.bounds.getX(), firerPos.bounds.getY(), maxSpeed, image);
	}

	private void createRocket(float originX, float originY, float maxSpeed, TextureRegion image) {
		Entity entity = new Entity();

		Renderer renderer = new Renderer(entity);
		renderer.image = image;
		if (team == TeamPlayer.class) {
			// Render underneath player ship and exhaust.
			renderer.priority = new Priority(-5);
		}
		
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(
				originX, 
				originY, 
				renderer.image.getRegionWidth(),
				renderer.image.getRegionHeight());
		position.outOfBoundsHandler = OutOfBoundsHandler.all();
		
		Velocity velocity = new Velocity();
		entity.add(velocity);
		
		LineMover lineMover = new LineMover();
		lineMover.absMaxVelocity = new Vector2(0, maxSpeed);
		lineMover.accel = new Vector2(0, maxSpeed);
		lineMover.accelerates = true;
		entity.add(lineMover);
	
		TeamRocket teamRocket = new TeamRocket();
		entity.add(teamRocket);
		
		Health health = new Health();
		health.currentHealth = 1;
		health.maxHealth = 1;
		entity.add(health);
	
		SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.ROCKET_ZOOM);
		soundEffect.id = soundEffect.sound.play(0.2f * Conf.volume);
		entity.add(soundEffect);

		Collider.defaultForProjectile(entity, team);
		
		Damager damager = new Damager();
		damager.damage = 100.0f;
		entity.add(damager);
		
		engine.addEntity(entity);
		lastRocketFired = entity;
	}

	private boolean isFastRocket() {
		return timesFired % 4 == 3;
	}
	
	@Override
	public void setFiring(boolean shouldFire) {
		super.setFiring(shouldFire);
		if (!shouldFire && isFastRocket()) {
			explosionFactory.createRocketExplosion(lastRocketFired);
			engine.removeEntity(lastRocketFired);
		}
	}
	
}
