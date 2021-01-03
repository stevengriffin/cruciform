package com.cruciform.weapons;



import com.badlogicmods.ashley.core.Engine;
import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.EffectFactory;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Priority;
import org.jetbrains.annotations.NotNull;

public class RifleWeapon extends Weapon {

	@NotNull private static final TextureRegion RIFLE_BULLET_IMAGE = ImageManager.RIFLE_BULLET;
	private final ExplosionFactory explosionFactory;
	private float currentRecoil = 0.0f;
	public float rotationalVelocity = 0;
	public float volume = 0.2f;
	public float bulletSpeed = 2400.0f;
	private static final float RECOIL_PER_BULLET = 0.5f;
	private static final float RECOIL_RESET_RATE = 20.0f;
	private static final float MAX_RECOIL = 20.0f;
	public float reloadTime = 5.0f;
	/*
	 * Setting bullets per clip to 0 will give unlimited clip size.
	 */
	public int bulletsPerClip = 30;
	private int bulletsFired = 0;
	private float recoilGapTime;
	private final float coolDownTime;
	private CoolDownMetro recoilGap = new CoolDownMetro(recoilGapTime);
	
	public RifleWeapon(float coolDownTime, @NotNull Engine engine, ExplosionFactory explosionFactory,
			Class<? extends Team> team) {
		super(coolDownTime, engine, team, 10.0f, "Rifle");
		this.coolDownTime = coolDownTime;
		this.recoilGapTime = coolDownTime*4;
		this.explosionFactory = explosionFactory;
	}

	@Override
	void handleUpdate(final float dt, final Position firerPos) {
		if (!recoilGap.tick(dt)) {
			currentRecoil -= RECOIL_RESET_RATE*dt;
		}
		currentRecoil = MathUtils.clamp(currentRecoil, 0, MAX_RECOIL);
	}

	@Override
	void handleFire(final Position firerPos) {
		currentRecoil += RECOIL_PER_BULLET;
		bulletsFired++;
		if (bulletsPerClip > 0) {
			if (bulletsFired % bulletsPerClip == 0) {
				this.coolDown = CoolDownMetro.asPrefired(reloadTime);
			} else if (bulletsFired % bulletsPerClip == 1) {
				this.coolDown = CoolDownMetro.asPrefired(coolDownTime);
			}
		}
		recoilGap = CoolDownMetro.asPrefired(recoilGapTime);
		AudioManager.get(Noise.RIFLE_FIRE).play(Conf.volume*volume);
		createBullet(firerPos.bounds.getX() - 5, firerPos.bounds.getY(), 1, firerPos.yDirection);
		createBullet(firerPos.bounds.getX() + 5, firerPos.bounds.getY(), -1, firerPos.yDirection);
	}

	private void createBullet(float originX, float originY, int directionX, int directionY) {
		Entity entity = new Entity();

		Renderer renderer = Renderer.defaultForBullet(entity, team, RIFLE_BULLET_IMAGE);
		if (team == TeamPlayer.class) {
			// Render underneath player ship.
			renderer.priority = new Priority(-1);
		}
		
		@NotNull final Position position = Position.defaultForBullet(entity,
				originX, originY,
				RIFLE_BULLET_IMAGE.getRegionWidth(),
				RIFLE_BULLET_IMAGE.getRegionHeight(),
				-currentRecoil*directionX*directionY);
		
		final Velocity velocity = new Velocity();
		entity.add(velocity);
		
		final LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(bulletSpeed*MathUtils.sinDeg(currentRecoil)*directionX,
				bulletSpeed*MathUtils.cosDeg(currentRecoil)*directionY);
		lineMover.maxRotationalVelocity = -rotationalVelocity*directionX*directionY;
		lineMover.accelerates = false;
		entity.add(lineMover);

		Collider.defaultForProjectile(entity, team);
		
		final Damager damager = new Damager();
		damager.damage = damage;
		damager.exploder = (bullet) -> explosionFactory.createRifleExplosion(bullet);
		entity.add(damager);
		
		engine.addEntity(entity);
		
		EffectFactory.createMuzzleFlash(engine, ImageManager.RIFLE_MUZZLE_FLASH, position);
	}	

	/**
	 * Return percent of magazine left or reload progress if empty.
	 */
	@Override
	public float getPercentReady() {
		if (bulletsFired % bulletsPerClip == 0) {
			return coolDown.getPercent();
		} else {
			return 1.0f - (float) (bulletsFired % bulletsPerClip) / bulletsPerClip;
		}
	}
}
