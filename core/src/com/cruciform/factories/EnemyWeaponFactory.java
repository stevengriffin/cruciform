package com.cruciform.factories;

import java.util.Arrays;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
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
	private static final float ANGLE_DOWN = 190;
	
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
			case RADIAL_3PRONG:
				RadialWeapon weapon = createStraightRadialWeapon(480, 3, 3, 0.2f);
				weapon.bulletRuleHandler.originAngle = ANGLE_DOWN-30;
				weapon.bulletRuleHandler.spanAngle = 60;
				return new Weapon[] { weapon };
			case RADIAL_SPIRALER:
				return new Weapon[] { createSpiralingRadialWeapon(480, 3, 30, 0.2f, 60.0f) };
			case RADIAL_SPIRALER_SOLID:
				return new Weapon[] { createSpiralingRadialWeapon(120, 12, 12, 0.1f, 20.0f) };
			case RADIAL_STRAIGHT:
				return new Weapon[] { createStraightRadialWeapon(480, 3, 30, 0.2f) };
			case RADIAL_SPLITTER:
				return new Weapon[] { createSplittingRadialWeapon() };
			case PENTAGRAM:
				return new Weapon[] { createPentagramWeapon(480, 20, 0.1f) };
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
	
	private RadialWeapon createStraightRadialWeapon(float bulletSpeed, int patternMax,
			int spokes, float coolDown) {
		final float rotationalVelocity = 0.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(patternMax);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = spokes;
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(3.0f), patternMax);
		RadialWeapon radial = new RadialWeapon(coolDown, engine, explosionFactory,
				bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}
	
	private RadialWeapon createPentagramWeapon(float bulletSpeed, int patternMax,
			float coolDown) {
		final int spokes = 5;
		final float rotationalVelocity = 0.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(patternMax);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = spokes;
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		bulletRuleHandler.addRule(createPentagramBehavior());
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(3.0f), patternMax);
		RadialWeapon radial = new RadialWeapon(coolDown, engine, explosionFactory,
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
	
	private Weapon createSpiralingRadialWeapon(final float bulletSpeed, final int patternMax,
			final int spokes, final float coolDown, final float rotationalVelocity) {
		WrappedIncrementor incrementor = new WrappedIncrementor(patternMax);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = spokes;
		bulletRuleHandler.addRule(createSpiralingBehavior());
		bulletRuleHandler.addRule(createLineMoverBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(2.0f), patternMax);
		RadialWeapon radial = new RadialWeapon(coolDown, engine, explosionFactory,
				bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}

	private EntityMutator createLineMoverBehavior(final float rotationalVelocity, final float bulletSpeed) {
		return (entity, index) -> {	
			Position position = Position.mapper.get(entity);

			LineMover lineMover = new LineMover();
			lineMover.maxVelocity = Geometry.rotatedVector(position.bounds.getRotation(), bulletSpeed);
			lineMover.maxRotationalVelocity = rotationalVelocity;
			lineMover.accelerates = false;
			entity.add(lineMover);

			return entity;
		};
	}

	/**
	 * Add this before line mover behavior so position change is taken into account.
	 */
	private EntityMutator createSpiralingBehavior() {
		return (entity, index) -> {	
			Position position = Position.mapper.get(entity);
			position.incrementRotation(index*5);

			return entity;
		};
	}
	
	private EntityMutator createRadialSplitBehavior() {
		return (entity, index) -> {	
			final LineMover mover = LineMover.mapper.get(entity);
			final Position position = Position.mapper.get(entity);
			float rotation = index == 0 ?
					mover.maxVelocity.angle() - 30 + 270: mover.maxVelocity.angle() + 30 + 270;
			position.bounds.rotate(rotation);

			mover.maxVelocity = Geometry.rotatedVector(rotation, mover.maxVelocity.len());

			final Lifetime lifetime = Lifetime.mapper.get(entity);
			lifetime.setTimeRemaining(1);
			return entity;
		};
	}
	
	private EntityMutator createSplitterBehavior(final EntityMutator behavior) {
		return (entity, index) -> {	
			Splitter splitter = new Splitter();
			splitter.numberOfNewEntities = 2;
			splitter.customSplitBehavior = behavior;
			splitter.splitOnCollision = false;
			splitter.splitOnDeletion = true;
			entity.add(splitter);

			Lifetime lifetime = new Lifetime(entity);
			lifetime.setTimeRemaining(1);

			return entity;
		};
	}
	
	private EntityMutator createPentagramBehavior() {
		return (entity, index) -> {
			Timer.schedule(new Task() {

				@Override
				public void run() {
					LineMover lineMover = LineMover.mapper.get(entity);
					if (index % 2 == 0) {
						lineMover.maxVelocity.rotate90(1);
					} else {
						lineMover.maxVelocity.rotate90(-1);
					}
				}
			}
			, 0.3f);
			return entity;
		};
	}
}
