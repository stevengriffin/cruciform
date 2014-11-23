package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Animator;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Fader;
import com.cruciform.components.Health;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamRocket;
import com.cruciform.components.team.TeamSoul;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.GameCamera;
import com.cruciform.utils.Geometry;

public class ExplosionFactory {
	
	public interface Exploder {
		Entity explode(final Entity explosionCreator);
	}

	private final Engine engine;
	
	public ExplosionFactory(final Cruciform.GameManager manager) {
		this.engine = manager.engine;
	}

	public Entity createExplosion(final Entity explosionCreator) {
		final TeamEnemy teamEnemy = TeamEnemy.mapper.get(explosionCreator);
		if (teamEnemy != null) {
			for (int i = 0; i < teamEnemy.soulCount; i++) {
				createSoul(explosionCreator);
			}
		}
		final Damager damager = Damager.mapper.get(explosionCreator);
		if (damager != null && damager.exploder != null) {
			return damager.exploder.explode(explosionCreator);
		}
		final Health health = Health.mapper.get(explosionCreator);
		if (health != null && health.deathExploder != null) {
			return health.deathExploder.explode(explosionCreator);
		}
		return new Entity();
	}
	
	public Entity createRocketExplosion(final Entity lastRocketFired) {
		final Position oldPos = Position.mapper.getSafe(lastRocketFired);
		Rectangle rect = oldPos.bounds.getBoundingRectangle();
		final float x = rect.x + rect.width/2;
		final float y = rect.y + rect.height;
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.ROCKET_EXPLOSION;

		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, ImageManager.ROCKET_EXPLOSION.getRegionWidth(), ImageManager.ROCKET_EXPLOSION.getRegionHeight());
	
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(0.5f);
		
		final Collider collider = new Collider(entity);
		collider.teamsToCollide.add(TeamRocket.class);
		collider.teamsToCollide.add(TeamEnemy.class);
	
		final Damager damager = new Damager();
		damager.damage = 10.0f;
		entity.add(damager);
		
		SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.ROCKET_EXPLOSION);
		soundEffect.id = soundEffect.sound.play(.5f * Conf.volume);
		entity.add(soundEffect);
	
		new Fader(entity);
		
		engine.addEntity(entity);
		
		GameCamera.shake(GameCamera.SMALL_SHAKE);
		
		return entity;
	}
	
	public Entity createPlayerExplosion(final Entity deadPlayer) {
		final float frameTime = 0.1f;
		
		final Position oldPos = Position.mapper.getSafe(deadPlayer);
		Rectangle rect = oldPos.bounds.getBoundingRectangle();
		final float x = rect.x + rect.width/2;
		final float y = rect.y + rect.height;
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		final TextureRegion image = ImageManager.PLAYER_EXPLOSION[0];
		renderer.image = image;

		final Animator animator = new Animator(entity);
		animator.currentAnimation = new Animation(frameTime, ImageManager.PLAYER_EXPLOSION);
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, image.getRegionWidth(), image.getRegionHeight());
	
		final SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.CRUCIFORM);
		soundEffect.id = soundEffect.sound.play(.5f * Conf.volume);
		entity.add(soundEffect);

		// Fade out explosion after it's half over.
		fadeOutAfterDelay(entity, frameTime*ImageManager.PLAYER_EXPLOSION.length/2,
				frameTime*(ImageManager.PLAYER_EXPLOSION.length/2 + 1));
		
		engine.addEntity(entity);
		
		GameCamera.shake(GameCamera.LARGE_SHAKE);
		
		return entity;
	}

	public Entity createEnemyExplosion(final Entity deadEnemy) {
		final float frameTime = 0.1f;
		
		final Position oldPos = Position.mapper.getSafe(deadEnemy);
		Rectangle rect = oldPos.bounds.getBoundingRectangle();
		final float x = rect.x + rect.width/2;
		final float y = rect.y + rect.height;
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		final TextureRegion image = ImageManager.PENTAGRAM_EXPLOSION[0];
		renderer.image = image;

		final Animator animator = new Animator(entity);
		animator.currentAnimation = new Animation(frameTime, ImageManager.PENTAGRAM_EXPLOSION);
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, image.getRegionWidth(), image.getRegionHeight());
	
		final SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.ENEMY_SCREAM);
		soundEffect.id = soundEffect.sound.play(.5f * Conf.volume);
		entity.add(soundEffect);

		// Fade out explosion after it's half over.
		fadeOutAfterDelay(entity, frameTime*ImageManager.PENTAGRAM_EXPLOSION.length/2,
				frameTime*(ImageManager.PENTAGRAM_EXPLOSION.length/2 + 1));
		
		engine.addEntity(entity);
		
		GameCamera.shake(GameCamera.SMALL_SHAKE);
		
		return entity;
	}

	private void fadeOutAfterDelay(Entity entity, float delay, float lifeDelay) {
		Timer.schedule(new Task() {

			@Override
			public void run() {
				final Lifetime lifetime = new Lifetime(entity);
				lifetime.setTimeRemaining(lifeDelay);
				new Fader(entity);
			}
			
		}, delay);
	}
	
	public Entity createRifleExplosion(final Entity bullet) {
		final Position oldPos = Position.mapper.getSafe(bullet);
		final Rectangle rect = oldPos.bounds.getBoundingRectangle();
		final float x = rect.x;
		final float y = rect.y;
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		final TextureRegion image = ImageManager.RIFLE_MUZZLE_FLASH;
		renderer.image = image;

		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, image.getRegionWidth(), image.getRegionHeight());
		position.bounds.setRotation(MathUtils.random(0, 360));
		
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(0.1f);
		
		// TODO bullet hit noise
//		final SoundEffect soundEffect = new SoundEffect();
//		soundEffect.sound = AudioManager.get(Noise.ROCKET_EXPLOSION);
//		soundEffect.id = soundEffect.sound.play(.5f * Conf.volume);
//		entity.add(soundEffect);
		
		engine.addEntity(entity);
		
		EffectFactory.createParticleEmitter(engine, x, y, EffectFactory.RIFLE_FLASH, 1.0f, 2.0f);
		
		return entity;
	}
	
	private final static float SOUL_DROP_SPEED = -300.0f;
	
	public void createSoul(final Entity deadEntity) {
		final Position deadPosition = Position.mapper.getSafe(deadEntity);
		
		final Entity entity = new Entity();

		final TextureRegion image = ImageManager.SOUL;
		Renderer.defaultForBullet(entity, TeamSoul.class,
				image);

		Position.defaultForBullet(entity,
				deadPosition.bounds.getX(), deadPosition.bounds.getY(),
				image.getRegionWidth(),
				image.getRegionHeight(),
				0);

		final Velocity velocity = new Velocity();
		velocity.linear.set(MathUtils.random(-100.0f, 100.0f), 
				MathUtils.random(-100.0f, 100.0f));
		velocity.linearDragX = 20;
		entity.add(velocity);

		final LineMover lineMover = new LineMover();
		lineMover.absMaxVelocity = new Vector2(Math.abs(velocity.linear.x), Math.abs(SOUL_DROP_SPEED));
		lineMover.accel = new Vector2(0, SOUL_DROP_SPEED);
		lineMover.accelerates = true;
		entity.add(lineMover);
		
		new Collider(entity);
		
		final TeamSoul team = new TeamSoul();
		entity.add(team);

		engine.addEntity(entity);
	}
}
