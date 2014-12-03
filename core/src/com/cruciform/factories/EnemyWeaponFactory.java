package com.cruciform.factories;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Seeker;
import com.cruciform.components.Shooter;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.images.ImageManager;
import com.cruciform.states.GameState;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.WrappedIncrementor;
import com.cruciform.weapons.BulletRuleHandler;
import com.cruciform.weapons.CoolDownRuleHandler;
import com.cruciform.weapons.RadialWeapon;
import com.cruciform.weapons.RifleWeapon;
import com.cruciform.weapons.Weapon;
import com.esotericsoftware.minlog.Log;

public class EnemyWeaponFactory {

	private final Engine engine;
	private final ExplosionFactory explosionFactory;
	private final PathFactory pathFactory;
	private final GameState gameState;
	private static final float ANGLE_DOWN = 270;
	
	public EnemyWeaponFactory(final Engine engine, final ExplosionFactory explosionFactory,
			final GameState gameState, PathFactory pathFactory) {
		this.engine = engine;
		this.explosionFactory = explosionFactory;
		this.pathFactory = pathFactory;
		this.gameState = gameState;
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
				RadialWeapon prong = createStraightRadialWeapon(480, 3, 3, 0.2f);
				prong.bulletRuleHandler.originAngle = ANGLE_DOWN-30;
				prong.bulletRuleHandler.spanAngle = 60;
				RadialWeapon tracker = createSeekingRadialWeapon(480, 3, 3, 0.2f);
				tracker.bulletRuleHandler.originAngle = -30;
				tracker.bulletRuleHandler.spanAngle = 60;
				// Offset the initial bullets
				tracker.coolDown = CoolDownMetro.asPrefired(1.5f);
				return new Weapon @NonNull[] { prong, tracker };
			case RADIAL_LURCHER:
				return new Weapon @NonNull[] {
						createLurchingRadialWeapon(480, 3, 30, 0.2f),
						createSpiralingRadialWeapon(120, 12, 12, 0.1f, 0.0f)
				};
			case RADIAL_SPIRALER:
				return new Weapon @NonNull[] { createSpiralingRadialWeapon(480, 3, 30, 0.2f, 0.0f) };
			case RADIAL_SPIRALER_SOLID:
				return new Weapon @NonNull[] { createSpiralingRadialWeapon(120, 12, 12, 0.1f, 0.0f) };
			case RADIAL_STRAIGHT:
				return new Weapon @NonNull[] { createStraightRadialWeapon(480, 3, 30, 0.2f) };
			case RADIAL_SPLITTER:
				return new Weapon @NonNull[] { createSplittingRadialWeapon() };
			case PENTAGRAM:
				return new Weapon @NonNull[] { createPentagramWeapon(480, 20, 0.1f) };
			default:
				return new Weapon @NonNull[] { createRifleWeapon() };
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
	
	private RadialWeapon createLurchingRadialWeapon(float bulletSpeed, int patternMax,
			int spokes, float coolDown) {
		final RadialWeapon radial = createStraightRadialWeapon(bulletSpeed, patternMax,
				spokes, coolDown);
		radial.bulletRuleHandler.addRule((entity, i) -> pathFactory.createLurchingPath(entity));
		return radial;
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
		RadialWeapon radial = new RadialWeapon(coolDown, engine,
				bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_ELONGATED);
		return radial;
	}
	
	private RadialWeapon createTrackingRadialWeapon(float bulletSpeed, int patternMax,
			int spokes, float coolDown) {
		WrappedIncrementor incrementor = new WrappedIncrementor(patternMax);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = spokes;
		bulletRuleHandler.addRule(createTrackingBehavior());
		bulletRuleHandler.addRule(createLineMoverBehavior(0.0f, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(3.0f), patternMax);
		RadialWeapon radial = new RadialWeapon(coolDown, engine,
				bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_DIAMOND);
		return radial;
	}
	
	private RadialWeapon createSeekingRadialWeapon(float bulletSpeed, int patternMax,
			int spokes, float coolDown) {
		WrappedIncrementor incrementor = new WrappedIncrementor(patternMax);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = spokes;
		bulletRuleHandler.addRule(createTrackingBehavior());
		bulletRuleHandler.addRule(createSeekingBehavior());
		bulletRuleHandler.addRule(createLineMoverBehavior(0.0f, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(3.0f), patternMax);
		RadialWeapon radial = new RadialWeapon(coolDown, engine,
				bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_DIAMOND);
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
		RadialWeapon radial = new RadialWeapon(coolDown, engine,
				bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_ELONGATED);
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
		RadialWeapon radial = new RadialWeapon(2.0f, engine, bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_ELONGATED);
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
		RadialWeapon radial = new RadialWeapon(coolDown, engine,
				bulletRuleHandler, coolDownRuleHandler,
				ImageManager.ENEMY_BULLET_ELONGATED);
		return radial;
	}

	private EntityMutator createLineMoverBehavior(final float rotationalVelocity, final float bulletSpeed) {
		return (entity, index) -> {	
			final Position position = Position.mapper.getSafe(entity);

			final LineMover lineMover = new LineMover();
			lineMover.maxVelocity = Geometry.rotatedVector(position.bounds.getRotation(), bulletSpeed);
			lineMover.maxRotationalVelocity = rotationalVelocity;
			lineMover.accelerates = false;
			return entity.add(lineMover);
		};
	}
	
	/**
	 * Add this before line mover behavior so position change is taken into account.
	 */
	private EntityMutator createTrackingBehavior() {
		return (entity, index) -> {	
			Position position = Position.mapper.getSafe(entity);
			final Vector2 bulletPos = position.getCenter();
			final Vector2 playerPos = Position.mapper.getSafe(gameState.getPlayer()).getCenter();
			Log.debug("bulletPos: " + bulletPos + " playerPos: " + playerPos);
			Log.debug("old rot: " + position.bounds.getRotation());
			position.bounds.rotate(bulletPos.angle(playerPos));
			Log.debug("rot: " + playerPos.angle(bulletPos) + " rot2: " + bulletPos.angle(playerPos));
			Log.debug("new rot: " + position.bounds.getRotation());
			return entity;
		};
	}
	
	private EntityMutator createSeekingBehavior() {
		return (entity, index) -> {	
			final Seeker seeker = new Seeker();
			entity.add(seeker);
			return entity;
		};
	}

	/**
	 * Add this before line mover behavior so position change is taken into account.
	 */
	private EntityMutator createSpiralingBehavior() {
		return (entity, index) -> {	
			Position position = Position.mapper.getSafe(entity);
			position.incrementRotation(index*5);

			return entity;
		};
	}
	
	private EntityMutator createRadialSplitBehavior() {
		return (entity, index) -> {	
			final LineMover mover = LineMover.mapper.getSafe(entity);
			final Position position = Position.mapper.getSafe(entity);
			float rotation = index == 0 ?
					mover.maxVelocity.angle() - 30: mover.maxVelocity.angle() + 30;
			position.bounds.setRotation(rotation);

			mover.maxVelocity = Geometry.rotatedVector(rotation, mover.maxVelocity.len());

			final Lifetime lifetime = Lifetime.mapper.getSafe(entity);
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
					LineMover lineMover = LineMover.mapper.getSafe(entity);
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
