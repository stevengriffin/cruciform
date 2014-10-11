package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;

public class RenderSystem extends IteratingSystem {

	private Batch batch;
	
	public RenderSystem(Batch batch) {
		super(Family.getFor(Position.class, Renderer.class));
		this.batch = batch;
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		//System.out.println("x: " + position.bounds.getX() + " y: " + position.bounds.getY());
		Rectangle rect = position.bounds.getBoundingRectangle();
		batch.draw(renderer.image, position.bounds.getX() - rect.width/2,
				position.bounds.getY() - rect.height/2);
	}
}
