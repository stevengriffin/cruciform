package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Splitter;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.WrappedIncrementor;
import com.cruciform.weapons.BulletRuleHandler;
import com.cruciform.weapons.CoolDownRuleHandler;
import com.cruciform.weapons.RadialWeapon;
import com.cruciform.weapons.Weapon;

public class WeaponFactory {
	public static Weapon createSplittingRadialWeapon(final Engine engine,
			final ExplosionFactory explosionFactory) {
		final float rotationalVelocity = 0;
		final float bulletSpeed = 120.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(30);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.addRule(createSplitterBehavior(rotationalVelocity, bulletSpeed, createRadialSplitBehavior()));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		RadialWeapon radial = new RadialWeapon(2.0f, engine, explosionFactory, bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}
	
	public static Weapon createSpiralingRadialWeapon(final Engine engine,
			final ExplosionFactory explosionFactory) {
		final float rotationalVelocity = 60.0f;
		final float bulletSpeed = 240.0f;
		WrappedIncrementor incrementor = new WrappedIncrementor(6);
		BulletRuleHandler bulletRuleHandler = new BulletRuleHandler(incrementor, engine);
		bulletRuleHandler.spokes = 30;
		bulletRuleHandler.addRule(createSpiralingBehavior(rotationalVelocity, bulletSpeed));
		CoolDownRuleHandler coolDownRuleHandler = new CoolDownRuleHandler(incrementor);
		//coolDownRuleHandler.addRule((cD, index) -> CoolDownMetro.asPrefired(2.0f), 6);
		RadialWeapon radial = new RadialWeapon(0.2f, engine, explosionFactory,
				bulletRuleHandler, coolDownRuleHandler);
		return radial;
	}

	private static EntityMutator createSpiralingBehavior(final float rotationalVelocity, final float bulletSpeed) {
		return new EntityMutator() {	
		@Override
			public Entity mutate(final Entity entity, final int index) {
				Position position = Position.mapper.get(entity);
				position.incrementRotation(index*5);
				
				LineMover lineMover = new LineMover();
				lineMover.maxVelocity = Geometry.rotatedVector(position.bounds.getRotation(), bulletSpeed);
				lineMover.maxRotationalVelocity = rotationalVelocity;
				lineMover.accelerates = false;
				entity.add(lineMover);

				return entity;
			}
		};
	}
	
	private static EntityMutator createRadialSplitBehavior() {
		return new EntityMutator() {
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
		};
	}
	
	private static EntityMutator createSplitterBehavior(final float rotationalVelocity, final float bulletSpeed,
			final EntityMutator behavior) {
		return new EntityMutator() {	
		@Override
			public Entity mutate(final Entity entity, final int index) {
				Position position = Position.mapper.get(entity);
				
				LineMover lineMover = new LineMover();
				lineMover.maxVelocity = Geometry.rotatedVector(position.bounds.getRotation(), bulletSpeed);
				lineMover.maxRotationalVelocity = rotationalVelocity;
				lineMover.accelerates = false;
				entity.add(lineMover);

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
