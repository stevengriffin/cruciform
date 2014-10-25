package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.cruciform.components.Animator;
import com.cruciform.components.Child;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.Priority;

public class EffectFactory {
	public static void createPlayerExhaust(final Entity player, final Engine engine) {
		// Player ship exhaust fumes
		final Entity exhaust = new Entity();
		
		Child child = new Child(exhaust);
		child.parent = player;
		
		Position position = new Position(exhaust);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		Renderer renderer = new Renderer(exhaust);
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_1);
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -renderer.image.getRegionHeight();
		renderer.customOffset = true;
		// Render above background and below player bullets
		renderer.priority = new Priority(-4);
	
		Animator animator = new Animator(exhaust);
		animator.animation = new Animation(0.125f, ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_1),
				ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_2),
				ImageManager.get(Picture.PLAYER_SHIP_EXHAUST_3));
		animator.animation.setPlayMode(PlayMode.LOOP);
		
		engine.addEntity(exhaust);
		
		// Crosses at player engine
		createCross(player, -32.5f-16, engine);
		createCross(player, -32.5f+16, engine);
		
	}
	
	private static void createCross(final Entity ship, final float xOffset, final Engine engine) {
		final Entity exhaustCross = new Entity();
		
		Child child = new Child(exhaustCross);
		child.parent = ship;
		
		Position position = new Position(exhaustCross);
		position.bounds = Geometry.polyRect(0, 0, 0, 0);
		
		Renderer renderer = new Renderer(exhaustCross);
		renderer.image = ImageManager.get(Picture.PLAYER_EXHAUST_CROSS);
		renderer.customXOffset = xOffset;
		renderer.customYOffset = -renderer.image.getRegionHeight()-10;
		renderer.customOffset = true;
		// Render above background and below enemy bullets but above exhaust and player ship
		renderer.priority = new Priority(1);
	
		Animator animator = new Animator(exhaustCross);
		animator.animation = new Animation(0.125f, ImageManager.get(Picture.PLAYER_EXHAUST_CROSS_2),
				ImageManager.get(Picture.PLAYER_EXHAUST_CROSS_2));
		animator.animation.setPlayMode(PlayMode.LOOP);
		
		engine.addEntity(exhaustCross);
		
	}
}
