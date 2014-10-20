package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.Team;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.WrappedIncrementor;

public class BulletRuleHandler {
	private final static float DAMAGE_TO_INSTAKILL_PLAYER = 5.0f;
	public float spanAngle = 360.0f;
	public float originAngle = 0.0f;
	public int spokes = 6;
	private final WrappedIncrementor pattern;
	private IntMap<Array<EntityMutator>> map = new IntMap<>();
	private final Engine engine;
	
	public BulletRuleHandler(final WrappedIncrementor pattern, final Engine engine) {
		this.pattern = pattern;
		this.engine = engine;
	}

	public Array<Entity> createBullets(final float originX, final float originY, 
			final TextureRegion image, final Class<? extends Team> team) {
		Array<Entity> bullets = new Array<>();
		for (float rotation = originAngle; rotation < originAngle + spanAngle;
				rotation += (spanAngle - originAngle)/spokes) {
			Entity entity = new Entity();

			Renderer renderer = Renderer.defaultForBullet(entity, team, image);

			Position.defaultForBullet(entity,
					originX, originY,
					renderer.image.getRegionWidth(),
					renderer.image.getRegionHeight(),
					rotation);
			
			Velocity velocity = new Velocity();
			entity.add(velocity);
			
			Collider.defaultForProjectile(entity, team);
		
			Damager damager = new Damager();
			damager.damage = DAMAGE_TO_INSTAKILL_PLAYER;
			entity.add(damager);
			
			engine.addEntity(entity);
			bullets.add(entity);
		}
		mutateBullets(bullets);
		return bullets;
	}
	
	private void mutateBullets(Array<Entity> bullets) {
		pattern.increment();
		Array<EntityMutator> rules = map.get(pattern.getIndex());
		if (rules != null) {
			for (int i = 0; i < bullets.size; i++) {
				for (int j = 0; j < rules.size; j++) {
					rules.get(j).mutate(bullets.get(i), pattern.getIndex());
				}
			}
		}
	}
	
	public BulletRuleHandler addRule(EntityMutator rule, int index) {
		Array<EntityMutator> rules = map.get(index, new Array<EntityMutator>());
		rules.add(rule);
		map.put(index, rules);
		return this;
	}
	
	public BulletRuleHandler addRule(EntityMutator rule) {
		for (int index = pattern.getMin(); index < pattern.getMax(); index++) {
			Array<EntityMutator> rules = map.get(index, new Array<EntityMutator>());
			rules.add(rule);
			map.put(index, rules);
		}
		return this;
	}
}
