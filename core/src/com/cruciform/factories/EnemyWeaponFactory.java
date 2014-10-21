package com.cruciform.factories;

import java.util.Arrays;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.WrappedIncrementor;
import com.cruciform.weapons.BulletRuleHandler;
import com.cruciform.weapons.CoolDownRuleHandler;
import com.cruciform.weapons.RadialWeapon;
import com.cruciform.weapons.RifleWeapon;
import com.cruciform.weapons.Weapon;

public class EnemyWeaponFactory {

	private final Engine engine;
	private final ExplosionFactory explosionFactory;
	
	public EnemyWeaponFactory(final Engine engine, final ExplosionFactory explosionFactory) {
		this.engine = engine;
		this.explosionFactory = explosionFactory;
	}
	
	public Shooter createShooter(EnemyTypes type, Entity entity) {
		Shooter shooter = new Shooter();
		shooter.weapons = Arrays.asList(createWeapons(type));
		entity.add(shooter);
		return shooter;
	}
	
	private Weapon[] createWeapons(EnemyTypes type) {
		switch(type) {
			case RADIAL_SPIRALER:
				return new Weapon[] { createSpiralingRadialWeapon() };
			case RADIAL_STRAIGHT:
				return new Weapon[] { createStraightRadialWeapon() };
			case RADIAL_SPLITTER:
				return new Weapon[] { createSplittingRadialWeapon() };
			default:
				return new Weapon[] { createRifleWeapon() };
		}
	}
	
	private Weapon createRifleWeapon() {
		RifleWeapon rifle = new RifleWeapon(0.4f, engine, explosionFactory, TeamEnemy.class);
		rifle.volume = 0.0f;
		rifle.bulletSpeed = 120.0f;
		rifle.bulletsPerClip = 5;
		rifle.reloadTime = 3.0f;
		rifle.rotationalVelocity = 60.0f;
		return rifle;
	}
	
	private Weapon createStraightRadialWeapon() {
		final float rotationalVelocity = 0.0f;
		final float bulletSpeed = 480.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(3);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = 30;
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(3.0f), 3);
		RadialWeapon radial = new RadialWeapon(0.2f, engine, explosionFactory,
				bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}
	
	private Weapon createSplittingRadialWeapon() {
		final float rotationalVelocity = 0;
		final float bulletSpeed = 240.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(3);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.addRule(createSplitterBehavior(createRadialSplitBehavior()));
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(2.0f), 3);
		RadialWeapon radial = new RadialWeapon(2.0f, engine, explosionFactory, bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}
	
	private Weapon createSpiralingRadialWeapon() {
		final float rotationalVelocity = 60.0f;
		final float bulletSpeed = 480.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(3);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = 30;
		bulletRuleHandler.addRule(createSpiralingBehavior());
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(2.0f), 3);
		RadialWeapon radial = new RadialWeapon(0.2f, engine, explosionFactory,
				bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}

	private EntityMutator createLineMoverBehavior(final float rotationalVelocity, final float bulletSpeed) {
		return new EntityMutator() {	
		@Override
			public Entity mutate(final Entity entity, final int index) {
				Position position = Position.mapper.get(entity);
				
				LineMover lineMover = new LineMover();
				lineMover.maxVelocity = Geometry.rotatedVector(position.bounds.getRotation(), bulletSpeed);
				lineMover.maxRotationalVelocity = rotationalVelocity;
				lineMover.accelerates = false;
				entity.add(lineMover);

				return entity;
			}
		};
	}

	/**
	 * Add this before line mover behavior so position change is taken into account.
	 */
	private EntityMutator createSpiralingBehavior() {
		return new EntityMutator() {	
		@Override
			public Entity mutate(final Entity entity, final int index) {
				Position position = Position.mapper.get(entity);
				position.incrementRotation(index*5);
				
				return entity;
			}
		};
	}
	
	private EntityMutator createRadialSplitBehavior() {
		return new EntityMutator() {
			@Override
			public Entity mutate(Entity entity, final int index) {
				final LineMover mover = LineMover.mapper.get(entity);
				final Position position = Position.mapper.get(entity);
				float rotation = index == 0 ?
						mover.maxVelocity.angle() - 30 + 270: mover.maxVelocity.angle() + 30 + 270;
				position.bounds.rotate(rotation);

				mover.maxVelocity = Geometry.rotatedVector(rotation, mover.maxVelocity.len());

				final Lifetime lifetime = Lifetime.mapper.get(entity);
				lifetime.setTimeRemaining(1);
				return entity;
			}
		};
	}
	
	private EntityMutator createSplitterBehavior(final EntityMutator behavior) {
		return new EntityMutator() {	
		@Override
			public Entity mutate(final Entity entity, final int index) {
				Splitter splitter = new Splitter();
				splitter.numberOfNewEntities = 2;
				splitter.customSplitBehavior = behavior;
				splitter.splitOnCollision = false;
				splitter.splitOnDeletion = true;
				entity.add(splitter);

				Lifetime lifetime = new Lifetime();
				lifetime.setTimeRemaining(1);
				entity.add(lifetime);
				
				return entity;
			}
		};
	}
}
