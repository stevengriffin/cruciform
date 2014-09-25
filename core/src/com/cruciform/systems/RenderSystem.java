package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.utils.Mappers;

public class RenderSystem extends IteratingSystem {

	private Batch batch;
	
	public RenderSystem(Batch batch) {
		super(Family.getFor(Position.class, Renderer.class));
		this.batch = batch;
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Mappers.position.get(entity);
		Renderer renderer = Mappers.renderer.get(entity);
		batch.draw(renderer.image, position.rect.x, position.rect.y);
	}
}
