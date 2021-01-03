package com.cruciform.factories;

import com.badlogicmods.ashley.core.Engine;
import com.badlogicmods.ashley.core.Entity;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.Priority;

public class UIFactory {
	private final Engine engine;
	
	public UIFactory(final Engine engine) {
		this.engine = engine;
	}
	
	public Entity createUIBackground() {
		final Entity entity = new Entity();

		final Renderer renderer = Renderer.defaultForUI(entity, ImageManager.UI_BG);
		renderer.renderAtPlayCoordinates = false;
		renderer.priority = new Priority(100); 
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyRect(0,
				0, Conf.screenWidth, Conf.screenHeight);
		
		engine.addEntity(entity);
		return entity;
	}
		
}
