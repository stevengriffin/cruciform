package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;

public class RenderSystem extends IteratingSystem {

	private Batch batch;
	private final ShapeRenderer shapeRenderer;
	private static final Color debugColor = Color.YELLOW;
	
	public RenderSystem(Batch batch, ShapeRenderer shapeRenderer) {
		super(Family.getFor(Position.class, Renderer.class));
		this.batch = batch;
		this.shapeRenderer = shapeRenderer;
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		//System.out.println("x: " + position.bounds.getX() + " y: " + position.bounds.getY());
		Rectangle rect = position.bounds.getBoundingRectangle();
		batch.draw(renderer.image, position.bounds.getX(),
				position.bounds.getY());
		shapeRenderer.setColor(debugColor);
		
		shapeRenderer.polygon(position.bounds.getTransformedVertices());
	}
}
