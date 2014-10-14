package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;

public class RifleWeapon extends Weapon {

	private static final Texture RIFLE_BULLET_IMAGE = ImageManager.get(Picture.RIFLE_BULLET);
	private final ExplosionFactory explosionFactory;
	private float currentRecoil = 0.0f;
	private static final float BULLET_SPEED = 40.0f;
	private static final float RECOIL_PER_BULLET = 0.5f;
	private static final float RECOIL_RESET_RATE = 20.0f;
	private static final float MAX_RECOIL = 20.0f;
	private static final float RECOIL_GAP_TIME = 0.2f;
	private CoolDownMetro recoilGap = new CoolDownMetro(RECOIL_GAP_TIME);
	
	public RifleWeapon(float coolDownTime, Engine engine, ExplosionFactory explosionFactory) {
		super(coolDownTime, engine);
		this.explosionFactory = explosionFactory;
	}

	@Override
	void handleUpdate(float dt, Position firerPos) {
		if (!recoilGap.tick(dt)) {
			currentRecoil -= RECOIL_RESET_RATE*dt;
		}
		currentRecoil = MathUtils.clamp(currentRecoil, 0, MAX_RECOIL);
	}

	@Override
	void handleFire(Position firerPos) {
		currentRecoil += RECOIL_PER_BULLET;
		recoilGap = CoolDownMetro.asPrefired(RECOIL_GAP_TIME);
		AudioManager.get(Noise.RIFLE_FIRE).play(Conf.volume*0.2f);
		createBullet(firerPos.bounds.getX() - 5, firerPos.bounds.getY(), 1, firerPos.yDirection);
		createBullet(firerPos.bounds.getX() + 5, firerPos.bounds.getY(), -1, firerPos.yDirection);
	}

	private void createBullet(float originX, float originY, int directionX, int directionY) {
		Entity entity = new Entity();

		Renderer renderer = new Renderer();
		renderer.image = RIFLE_BULLET_IMAGE;
		entity.add(renderer);
		
		Position position = new Position();
		position.bounds = Geometry.polyRect(
				originX, 
				originY, 
				renderer.image.getWidth(),
				renderer.image.getHeight());
		position.outOfBoundsHandler = OutOfBoundsHandler.all();
		entity.add(position);
		
		Velocity velocity = new Velocity();
		entity.add(velocity);
		
		LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(BULLET_SPEED*MathUtils.sinDeg(currentRecoil)*directionX,
				BULLET_SPEED*MathUtils.cosDeg(currentRecoil)*directionY);
		lineMover.accelerates = false;
		entity.add(lineMover);
	
		Collider collider = new Collider();
		collider.teamsToCollide.add(TeamEnemy.class);
		entity.add(collider);
		
		engine.addEntity(entity);
	}
	
}
