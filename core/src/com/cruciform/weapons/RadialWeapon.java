package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Position;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.utils.Conf;

public class RadialWeapon extends Weapon {

	private final TextureRegion bulletImage;
	public float volume = 0.05f;
	public final BulletRuleHandler bulletRuleHandler;
	private final CoolDownRuleHandler coolDownRuleHandler; 
	
	public RadialWeapon(float coolDownTime, Engine engine,
			BulletRuleHandler ruleHandler,
			CoolDownRuleHandler coolDownRuleHandler,
			TextureRegion bulletImage) {
		super(coolDownTime, engine, TeamEnemy.class, BulletRuleHandler.DAMAGE_TO_INSTAKILL_PLAYER, "Radial");
		this.bulletRuleHandler = ruleHandler;
		this.coolDownRuleHandler = coolDownRuleHandler;
		this.coolDownRuleHandler.setDefaultDoolDown(coolDown);
		this.bulletImage = bulletImage;
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
	}

	@Override
	void handleFire(Position firerPos) {
		AudioManager.get(Noise.BULLET_CLICK).play(Conf.volume*volume);
		bulletRuleHandler.createBullets(firerPos.bounds.getX(), firerPos.bounds.getY(), bulletImage, team);
		coolDown = coolDownRuleHandler.updateCoolDown(coolDown);
	}
}
