package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Splitter;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.DebugUtils;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.WrappedIncrementor;

public class RadialWeapon extends Weapon {

	private static final TextureRegion BULLET_IMAGE = ImageManager.get(Picture.ENEMY_BULLET_ELONGATED);
	private final ExplosionFactory explosionFactory;
	public float rotationalVelocity = 0;
	public float volume = 0.0f;
	public float bulletSpeed = 120.0f;
	public float damagePerBullet = 5.0f;
	public float bulletLifetime = 2.0f;
	private final BulletRuleHandler ruleHandler;
	private final RadialSplitBehavior behavior;
	
	public RadialWeapon(float coolDownTime, Engine engine, ExplosionFactory explosionFactory) {
		super(coolDownTime, engine, TeamEnemy.class);
		this.explosionFactory = explosionFactory;
		behavior = new RadialSplitBehavior();
		ruleHandler = new BulletRuleHandler(new WrappedIncrementor(30));
		ruleHandler.spokes = 6;
		ruleHandler.addRule((config) -> config.angle += config.patternIndex*5);
	}

	private class RadialSplitBehavior implements EntityMutator {

		@Override
		public Entity mutate(Entity entity, final int index) {
			final LineMover mover = LineMover.mapper.get(entity);
			final Position position = Position.mapper.get(entity);
			float rotation = index == 0 ?
					 mover.maxVelocity.angle() - 90 : mover.maxVelocity.angle() - 120;
			position.bounds.rotate(rotation);
		
			mover.maxVelocity = Geometry.rotatedVector(rotation, mover.maxVelocity.len());
			
			final Lifetime lifetime = Lifetime.mapper.get(entity);
			lifetime.setTimeRemaining(1);
			return entity;
		}
		
	}
	
	@Override
	void handleUpdate(float dt, Position firerPos) {
	}

	@Override
	void handleFire(Position firerPos) {
		AudioManager.get(Noise.RIFLE_FIRE).play(Conf.volume*volume);
		ruleHandler.fire().forEach((config) ->
		createBullet(firerPos.bounds.getX(), firerPos.bounds.getY(), config.angle));
	}

	private void createBullet(float originX, float originY, float rotation) {
		Entity entity = new Entity();

		Renderer renderer = Renderer.defaultForBullet(entity, team, BULLET_IMAGE);
		
		Position.defaultForBullet(entity,
				originX, originY,
				renderer.image.getRegionWidth(),
				renderer.image.getRegionHeight(),
				rotation);
		
		Velocity velocity = new Velocity();
		entity.add(velocity);
		
		LineMover lineMover = new LineMover();
		lineMover.maxVelocity = Geometry.rotatedVector(rotation, bulletSpeed);
		lineMover.maxRotationalVelocity = rotationalVelocity;
		lineMover.accelerates = false;
		entity.add(lineMover);

		Collider.defaultForProjectile(entity, team);
		
		Damager damager = new Damager();
		damager.damage = damagePerBullet;
		entity.add(damager);
		
		Splitter splitter = new Splitter();
		splitter.numberOfNewEntities = 2;
		splitter.customSplitBehavior = behavior;
		splitter.splitOnCollision = false;
		splitter.splitOnDeletion = true;
		entity.add(splitter);
	
		Lifetime lifetime = new Lifetime();
		lifetime.setTimeRemaining(1);
		entity.add(lifetime);
		
		engine.addEntity(entity);
	}	
	
}
