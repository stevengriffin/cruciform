package com.cruciform.factories;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.cruciform.components.Animator;
import com.cruciform.components.Child;
import com.cruciform.components.Lifetime;
import com.cruciform.components.ParticleEmitter;
import com.cruciform.components.Position;
import com.cruciform.components.Recoil;
import com.cruciform.components.Renderer;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.Priority;

public class EffectFactory {

	public static enum Particles {
		PLAYER_EXHAUST
	}
	
	private static final Map<Particles, ParticleEffect> particlesMap = new HashMap<>();

	private static ParticleEffect newParticle(String name) {
		final ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/" + name + ".p"), Gdx.files.internal("images"));
		// TODO existing particle effects will not be scaled correctly if player continues after resolution change.
		effect.scaleEffect(Conf.scaleFactor);
		return effect;
	}
	
	static {
		particlesMap.put(Particles.PLAYER_EXHAUST, newParticle("player_exhaust"));
	}
	
	public static void createPlayerExhaust(final Entity player, final Entity body, final Engine engine) {
		// Player ship exhaust fumes
		final Entity exhaust = new Entity();
		
		final Child child = new Child(exhaust);
		child.parent = player;
		
		final Position position = new Position(exhaust);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		final Renderer renderer = new Renderer(exhaust);
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_1);
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -renderer.image.getRegionHeight();
		renderer.customOffset = true;
		// Render above background and below player bullets
		renderer.priority = new Priority(-4);
	
		final Animator animator = new Animator(exhaust);
		animator.animation = new Animation(0.125f, ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_1),
				ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_2),
				ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_3));
		animator.animation.setPlayMode(PlayMode.LOOP);
	
		final ParticleEmitter emitter = new ParticleEmitter(exhaust);
		emitter.pool = new ParticleEffectPool(particlesMap.get(Particles.PLAYER_EXHAUST), 20, 20);
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
		renderer.image = ImageManager.get(Picture.PLAYER_EXHAUST_CROSS);
		renderer.customXOffset = xOffset;
		renderer.customYOffset = -renderer.image.getRegionHeight()-10;
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
	
		final Animator animator = new Animator(exhaustCross);
		animator.animation = new Animation(0.3f, ImageManager.get(Picture.PLAYER_EXHAUST_CROSS_2),
				ImageManager.get(Picture.PLAYER_EXHAUST_CROSS_3),
				ImageManager.get(Picture.PLAYER_EXHAUST_CROSS_4));
		animator.animation.setPlayMode(PlayMode.LOOP);
		
		engine.addEntity(exhaustCross);
	}
	
	private static void createGlow(final Entity ship, final Engine engine) {
		final Entity glow = new Entity();
		
		final Child child = new Child(glow);
		child.parent = ship;
		
		final Position position = new Position(glow);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		final Renderer renderer = new Renderer(glow);
		renderer.image = ImageManager.get(Picture.PLAYER_EXHAUST_GLOW);
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = -renderer.image.getRegionHeight();
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
		
		engine.addEntity(glow);
	}
	
	public static Entity createPlayerBody(final float x, final float y, 
			final Entity player, final Engine engine) {
		// Player ship exhaust fumes
		final Entity body = new Entity();
		
		final Child child = new Child(body);
		child.parent = player;
		
		final Position position = new Position(body);
		position.bounds = Geometry.polyRect(x, y, 5, 5);
		position.yDirection = 1;
		
		final Renderer renderer = new Renderer(body);
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_GOLD_BODY);
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -45;
		renderer.customOffset = true;
		// Render below player cockpit and below exhaust
		renderer.priority = new Priority(0);

		new Recoil(body);
		
		engine.addEntity(body);
		return body;
	}
	
	public static void createMuzzleFlash(final Engine engine, final Picture picture,
			final Position bulletPosition) {
		final Entity flash = new Entity();
		
		final Position position = new Position(flash);
		position.bounds = Geometry.polyRect(bulletPosition.bounds.getX(), 
				bulletPosition.bounds.getY(), 0, 0);
		
		final Renderer renderer = new Renderer(flash);
		renderer.image = ImageManager.get(picture);
		renderer.customXOffset = -renderer.image.getRegionWidth()/2;
		renderer.customYOffset = 5;
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(2);
	
		final Lifetime lifetime = new Lifetime(flash);
		lifetime.setTimeRemaining(0.01f);
		
		engine.addEntity(flash);
	}
}
