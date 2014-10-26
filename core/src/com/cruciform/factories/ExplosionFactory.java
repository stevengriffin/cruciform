package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Fader;
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
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Geometry;

public class ExplosionFactory {

	private final Engine engine;
	
	public ExplosionFactory(final Engine engine) {
		this.engine = engine;
	}

	public Entity createExplosion(final Entity deadEntity) {
		TeamEnemy teamEnemy = TeamEnemy.mapper.get(deadEntity);
		if (teamEnemy != null) {
			for (int i = 0; i < teamEnemy.soulCount; i++) {
				createSoul(deadEntity);
			}
		}
		return createRocketExplosion(deadEntity);
	}
	
	public Entity createRocketExplosion(final Entity lastRocketFired) {
		final Position oldPos = Position.mapper.get(lastRocketFired);
		Rectangle rect = oldPos.bounds.getBoundingRectangle();
		final float x = rect.x + rect.width/2;
		final float y = rect.y + rect.height;
		
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.get(Picture.ROCKET_EXPLOSION);

		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, renderer.image.getRegionWidth(), renderer.image.getRegionHeight());
	
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
		return entity;
	}

	private final static float SOUL_DROP_SPEED = -300.0f;
	
	public void createSoul(final Entity deadEntity) {
		final Position deadPosition = Position.mapper.get(deadEntity);
		
		final Entity entity = new Entity();

		final Renderer renderer = Renderer.defaultForBullet(entity, TeamSoul.class,
				ImageManager.get(Picture.SOUL));

		Position.defaultForBullet(entity,
				deadPosition.bounds.getX(), deadPosition.bounds.getY(),
				renderer.image.getRegionWidth(),
				renderer.image.getRegionHeight(),
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
