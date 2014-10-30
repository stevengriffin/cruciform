package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Position;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;

public class RadialWeapon extends Weapon {

	private static final TextureRegion BULLET_IMAGE = ImageManager.get(Picture.ENEMY_BULLET_ELONGATED);
	public float volume = 0.0f;
	public final BulletRuleHandler bulletRuleHandler;
	private final CoolDownRuleHandler coolDownRuleHandler; 
	
	public RadialWeapon(float coolDownTime, Engine engine,
			BulletRuleHandler ruleHandler,
			CoolDownRuleHandler coolDownRuleHandler) {
		super(coolDownTime, engine, TeamEnemy.class, BulletRuleHandler.DAMAGE_TO_INSTAKILL_PLAYER, "Radial");
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
