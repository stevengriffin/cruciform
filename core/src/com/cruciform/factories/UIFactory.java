package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Geometry;

public class UIFactory {
	private final Engine engine;
	
	public UIFactory(final Engine engine) {
		this.engine = engine;
	}
	public Entity createSidePanel(boolean isLeft) {
		final Entity entity = new Entity();

		final Renderer renderer = Renderer.defaultForUI(entity, ImageManager.get(Picture.SIDE_PANEL));
		
		final Position position = new Position(entity);
		if (isLeft) {
			position.bounds = Geometry.polyRect(Conf.playLeft - renderer.image.getRegionWidth(),
					0, Conf.playLeft, Conf.screenHeight);
		} else {
			position.bounds = Geometry.polyRect(Conf.playRight, 0, Conf.screenWidth,
					Conf.screenHeight);
		}
		
		engine.addEntity(entity);
		return entity;
	}
	
	public Entity createBottomPanel() {
		final Entity entity = new Entity();

		Renderer renderer = Renderer.defaultForUI(entity, ImageManager.get(Picture.BOTTOM_PANEL));
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(Conf.playCenter - renderer.image.getRegionWidth()/2,
				Conf.playBottom - renderer.image.getRegionHeight(), 
				renderer.image.getRegionWidth(), renderer.image.getRegionHeight());
		
		engine.addEntity(entity);
		return entity;
	}
}
