package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.Animator;
import com.cruciform.components.Child;
import com.cruciform.components.Collider;
import com.cruciform.components.Lifetime;
import com.cruciform.components.LineMover;
import com.cruciform.components.Parallax;
import com.cruciform.components.ParticleEmitter;
import com.cruciform.components.Position;
import com.cruciform.components.Recoil;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamPlayerBody;
import com.cruciform.components.team.TeamSoul;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.Priority;

public class EffectFactory {

	private static ParticleEffect newParticle(String name) {
		final ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/" + name + ".p"), Gdx.files.internal("images"));
		// TODO existing particle effects will not be scaled correctly if player continues after resolution change.
		effect.scaleEffect(Conf.scaleFactor);
		return effect;
	}
	
	public static final ParticleEffect PLAYER_EXHAUST = newParticle("player_exhaust");
	public static final ParticleEffect LAVA_ERUPTION = newParticle("lava_eruption_pixel");
	public static final ParticleEffect RIFLE_FLASH = newParticle("rifle_flash");
	
	public static void createPlayerExhaust(final Entity player, final Entity body, final Engine engine) {
		// Player ship exhaust fumes
		final Entity exhaust = new Entity();
		
		final Child child = new Child(exhaust);
		child.parent = player;
		
		final Position position = new Position(exhaust);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		final Renderer renderer = new Renderer(exhaust);
		renderer.image = ImageManager.PLAYER_SHIP_EXHAUST[0];
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -renderer.image.getRegionHeight();
		renderer.customOffset = true;
		// Render above background and below player bullets
		renderer.priority = new Priority(-4);
	
		final Animator animator = new Animator(exhaust);
		animator.currentAnimation = new Animation(0.125f, ImageManager.PLAYER_SHIP_EXHAUST);
		animator.currentAnimation.setPlayMode(PlayMode.LOOP);
	
		final ParticleEmitter emitter = new ParticleEmitter(exhaust);
		emitter.pool = new ParticleEffectPool(PLAYER_EXHAUST, 20, 20);
		emitter.coolDown = new CoolDownMetro(3.0f);
		
		engine.addEntity(exhaust);
		
		// Crosses at player engine
		createCross(body, -32.5f-16, engine);
		createCross(body, -32.5f+16, engine);
		
		createGlow(body, engine);
		
	}
	
	private static void createCross(final Entity ship, final float xOffset, final Engine engine) {
		final Entity exhaustCross = new Entity();
		
		final Child child = new Child(exhaustCross);
		child.parent = ship;
		
		final Position position = new Position(exhaustCross);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		final Renderer renderer = new Renderer(exhaustCross);
		renderer.image = ImageManager.PLAYER_EXHAUST_CROSS[0];
		renderer.customXOffset = xOffset;
		renderer.customYOffset = -renderer.image.getRegionHeight()-10;
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
	
		final Animator animator = new Animator(exhaustCross);
		animator.currentAnimation = new Animation(0.3f, ImageManager.PLAYER_EXHAUST_CROSS);
		animator.currentAnimation.setPlayMode(PlayMode.LOOP);
		
		engine.addEntity(exhaustCross);
	}
	
	private static void createGlow(final Entity ship, final Engine engine) {
		// TODO Rarely refuses to die when player does. Race condition?
		final Entity glow = new Entity();
		
		final Child child = new Child(glow);
		child.parent = ship;
		
		final Position position = new Position(glow);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		final Renderer renderer = new Renderer(glow);
		renderer.image = ImageManager.PLAYER_EXHAUST_GLOW;
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = -renderer.image.getRegionHeight();
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
		
		engine.addEntity(glow);
	}
	
	public static Entity createPlayerBody(final Entity player, final Engine engine) {
		final Polygon playerBounds = Position.mapper.get(player).bounds;
		
		// Player ship exhaust fumes
		final Entity body = new Entity();
		
		final Child child = new Child(body);
		child.parent = player;
		
		final Position position = new Position(body);
		position.bounds = Geometry.polyRect(playerBounds.getX(), playerBounds.getY(), 65, 110);
		position.yDirection = 1;
		
		final Renderer renderer = new Renderer(body);
		renderer.image = ImageManager.PLAYER_SHIP_GOLD_BODY;
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -45;
		renderer.customOffset = true;
		// Render below player cockpit and below exhaust
		renderer.priority = new Priority(0);

		new Recoil(body);
	
		final TeamPlayerBody team = new TeamPlayerBody();
		body.add(team);
		
		final Collider collider = new Collider(body);
		collider.teamsToCollide.add(TeamSoul.class);
		
		engine.addEntity(body);
		return body;
	}
	
	public static Entity createLavaOnPlayer(final Entity player, final Engine engine) {
		// Intro lava effect
		final Entity lava = new Entity();
		
		final Child child = new Child(lava);
		child.parent = player;
		
		final Position position = new Position(lava);
		position.bounds = Geometry.polyRect(0, 0, 65, 110);
		position.yDirection = 1;
		
		final Renderer renderer = new Renderer(lava);
		renderer.image = ImageManager.LAVA_ON_PLAYER[0];
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -45;
		renderer.customOffset = true;
		renderer.priority = new Priority(95);
		
		final float frameTime = 0.2f;
		final Animator animator = new Animator(lava);
		animator.currentAnimation = new Animation(0.2f, ImageManager.LAVA_ON_PLAYER);
		animator.currentAnimation.setPlayMode(PlayMode.NORMAL);

		final Lifetime lifetime = new Lifetime(lava);
		lifetime.setTimeRemaining(frameTime*5);
		
		engine.addEntity(lava);
		return lava;
	}
	
	public static void createMuzzleFlash(final Engine engine, final TextureRegion image,
			final Position bulletPosition) {
		final Entity flash = new Entity();
		
		final Position position = new Position(flash);
		position.bounds = Geometry.polyRect(bulletPosition.bounds.getX(), 
				bulletPosition.bounds.getY(), 0, 0);
		
		final Renderer renderer = new Renderer(flash);
		renderer.image = image;
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = 5;
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
	
		final Lifetime lifetime = new Lifetime(flash);
		lifetime.setTimeRemaining(0.01f);
		
		engine.addEntity(flash);
	}
	
	public static Entity createLavaBurst(final Engine engine) {
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.priority = new Priority(99);
		renderer.image = ImageManager.BURST_LAVA[0];
		renderer.customOffset = true;
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = -renderer.image.getRegionHeight()/2;
		renderer.renderAtPlayCoordinates = true;
		
		final float frameTime = 0.05f;
		final Animator animator = new Animator(entity);
		animator.currentAnimation = new Animation(frameTime, ImageManager.BURST_LAVA);
		animator.currentAnimation.setPlayMode(PlayMode.NORMAL);
		
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(frameTime*4);
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(Conf.fractionX(0.5f),
				Conf.fractionY(0.2f), 0, 0);
	
		engine.addEntity(entity);
		createLavaEruptionParticleEffect(engine);
		
		return entity;
	}
	
	public static Entity createLavaEruptionParticleEffect(final Engine engine) {
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.priority = new Priority(99);
		renderer.image = ImageManager.BLANK;
		renderer.customOffset = true;
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = -renderer.image.getRegionHeight()/2;
		renderer.renderAtPlayCoordinates = true;
		
		final ParticleEmitter emitter = new ParticleEmitter(entity);
		emitter.pool = new ParticleEffectPool(LAVA_ERUPTION, 20, 20);
		// Fire only once
		emitter.coolDown = new CoolDownMetro(2);
		
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(1.0f);
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(Conf.fractionX(0.5f),
				Conf.fractionY(0.2f), 0, 0);
	
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity createBackground(final Engine engine) {
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.customOffset = true;
		renderer.priority = new Priority(-100);
		renderer.image = ImageManager.BACKGROUND_LAVA;
		renderer.renderAtPlayCoordinates = false;
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(Conf.playLeft,
				0, Conf.playWidth, Conf.screenHeight);

		scheduleMoveOutOfSight(entity);
		
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity createForeground(final Engine engine) {
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.customOffset = true;
		renderer.priority = new Priority(90);
		renderer.image = ImageManager.FOREGROUND_LAVA;
		renderer.renderAtPlayCoordinates = false;
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(Conf.playLeft,
				0, Conf.playWidth, Conf.screenHeight);
	
		scheduleMoveOutOfSight(entity);
		
		engine.addEntity(entity);
		return entity;
	}

	private static float INTRO_DELAY = 0.75f;
	
	public static void scheduleMoveOutOfSight(final Entity entity) {
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(3.0f);
		
		entity.remove(LineMover.class);
		final LineMover mover = new LineMover();
		mover.accelerates = false;
		mover.maxVelocity = new Vector2(0, -1000);
		
		Timer.schedule(new Task() {

			@Override
			public void run() {
				entity.add(mover);
				entity.add(new Velocity());
			}

		}, INTRO_DELAY);
	}
	
	public static Entity createParallaxBackground(final Engine engine,
			final Position playerPosition, final int index) {
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.customOffset = true;
		renderer.priority = new Priority(-150);
		renderer.image = ImageManager.PARALLAX_BG_LVL1_1;
		renderer.renderAtPlayCoordinates = true;
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(0,
				-Conf.fractionY(0.7f) + renderer.image.getRegionHeight()*index,
				renderer.image.getRegionWidth(), renderer.image.getRegionHeight());

		final LineMover mover = new LineMover();
		mover.accelerates = false;
		mover.maxVelocity = new Vector2(0, -50);
		Timer.schedule(new Task() {

			@Override
			public void run() {
				entity.add(mover);
			}

		}, INTRO_DELAY);
		
		entity.add(new Velocity());
	
		final Parallax parallax = new Parallax(entity);
		parallax.referencePosition = playerPosition;
		// Adjust so that lava fall doesn't create bad composition.
		parallax.xOffset = -30;
		
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity createParticleEmitter(final Engine engine, final float x, final float y,
			final ParticleEffect effect, final float timeRemaining, final float emitterCoolDown) {
		final Entity entity = new Entity();
		
		final Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.BLANK;

		final ParticleEmitter emitter = new ParticleEmitter(entity);
		emitter.pool = new ParticleEffectPool(effect, 20, 20);
		emitter.coolDown = new CoolDownMetro(emitterCoolDown);
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, 0, 0);
		
		final Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(timeRemaining);
		
		engine.addEntity(entity);
		return entity;
	}
	
}
