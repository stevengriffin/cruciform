package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemyBullet;
import com.cruciform.utils.AbstractRuleHandler;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.WrappedIncrementor;

public class BulletRuleHandler extends AbstractRuleHandler<EntityMutator> {
	public final static float DAMAGE_TO_INSTAKILL_PLAYER = 5.0f;
	public float spanAngle = 360.0f;
	public float originAngle = 0.0f;
	public int spokes = 6;
	private final Engine engine;
	
	public BulletRuleHandler(final WrappedIncrementor pattern, final Engine engine) {
		super(pattern);
		this.engine = engine;
	}

	public Array<Entity> createBullets(final float originX, final float originY, 
			final TextureRegion image, final Class<? extends Team> team) {
		incrementPattern();
		Array<Entity> bullets = constructDefaultBullets(originX, originY,
				image, team);
		mutateBullets(bullets);
		return bullets;
	}

	/** Evenly distribute default bullets across span. **/
	private Array<Entity> constructDefaultBullets(final float originX,
			final float originY, final TextureRegion image,
			final Class<? extends Team> team) {
		Array<Entity> bullets = new Array<>();
		final float rotationIncrement = calculateRotationIncrement();
		for (float rotation = originAngle; rotation <= originAngle + spanAngle;
				rotation += rotationIncrement) {
			final Entity entity = new Entity();

			final Renderer renderer = Renderer.defaultForBullet(entity, team, image);

			Position.defaultForBullet(entity,
					originX, originY,
					renderer.image.getRegionWidth(),
					renderer.image.getRegionHeight(),
					rotation);
			
			final Velocity velocity = new Velocity();
			entity.add(velocity);
			
			Collider.defaultForProjectile(entity, team);
		
			final Damager damager = new Damager();
			damager.damage = DAMAGE_TO_INSTAKILL_PLAYER;
			entity.add(damager);
		
			final TeamEnemyBullet bulletTeam = new TeamEnemyBullet();
			entity.add(bulletTeam);
			
			engine.addEntity(entity);
			bullets.add(entity);
		}
		return bullets;
	}

	private float calculateRotationIncrement() {
		final float rotationIncrement;
		if (spanAngle == 360) {
			rotationIncrement = spanAngle/spokes;
		} else if (spokes > 1) {
			rotationIncrement = spanAngle/(spokes - 1);
		} else {
			rotationIncrement = spanAngle*2;
		}
		return rotationIncrement;
	}

	private void mutateBullets(Array<Entity> bullets) {
		Array<EntityMutator> rules = map.get(pattern.getIndex());
		if (rules != null) {
			for (int i = 0; i < bullets.size; i++) {
				for (int j = 0; j < rules.size; j++) {
					rules.get(j).mutate(bullets.get(i), pattern.getIndex());
				}
			}
		}
	}
	
}
