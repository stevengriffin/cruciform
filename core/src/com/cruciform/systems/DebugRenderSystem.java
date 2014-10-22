package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.cruciform.components.Collider;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;

/**
 * Renders debug information like hitboxes.
 */
public class DebugRenderSystem extends IteratingSystem {

	private final ShapeRenderer shapeRenderer;
	private static final Color debugColor = Color.MAGENTA;
	
	public DebugRenderSystem(Batch batch, ShapeRenderer shapeRenderer) {
		super(Family.getFor(Position.class, Renderer.class, Collider.class));
		this.shapeRenderer = shapeRenderer;
	}

	@Override
	public void update(float deltaTime) {
		shapeRenderer.begin(ShapeType.Line);
		super.update(deltaTime);
		shapeRenderer.end();
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		shapeRenderer.setColor(debugColor);
		shapeRenderer.polygon(position.bounds.getTransformedVertices());
	}
}
