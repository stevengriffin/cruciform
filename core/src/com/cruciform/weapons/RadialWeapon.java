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
	public float volume = 0.0f;
	private final BulletRuleHandler bulletRuleHandler;
	private final CoolDownRuleHandler coolDownRuleHandler; 
	
	public RadialWeapon(float coolDownTime, Engine engine,
			ExplosionFactory explosionFactory, BulletRuleHandler ruleHandler,
			CoolDownRuleHandler coolDownRuleHandler) {
		super(coolDownTime, engine, TeamEnemy.class);
		this.explosionFactory = explosionFactory;
		this.bulletRuleHandler = ruleHandler;
		this.coolDownRuleHandler = coolDownRuleHandler;
		this.coolDownRuleHandler.setDefaultDoolDown(coolDown);
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
	}

	@Override
	void handleFire(Position firerPos) {
		AudioManager.get(Noise.RIFLE_FIRE).play(Conf.volume*volume);
		bulletRuleHandler.createBullets(firerPos.bounds.getX(), firerPos.bounds.getY(), BULLET_IMAGE, team);
		coolDown = coolDownRuleHandler.updateCoolDown(coolDown);
	}
}
