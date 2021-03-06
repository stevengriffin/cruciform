package com.cruciform.systems;

import org.eclipse.jdt.annotation.NonNull;

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
import com.cruciform.utils.ColorUtils;
import com.cruciform.utils.Conf;
import com.esotericsoftware.minlog.Log;

/**
 * Renders debug information like hitboxes.
 */
public class DebugRenderSystem extends IteratingSystem {

	private final ShapeRenderer shapeRenderer;
	private static final Color debugColor = ColorUtils.getColor(Color.MAGENTA);
	
	public DebugRenderSystem(Batch batch, ShapeRenderer shapeRenderer) {
		super(Family.all(Position.class, Renderer.class, Collider.class).get());
		this.shapeRenderer = shapeRenderer;
		this.priority = 170;
	}

	@Override
	public void update(float deltaTime) {
		shapeRenderer.begin(ShapeType.Line);
		super.update(deltaTime);
		shapeRenderer.end();
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final Position position = Position.mapper.getSafe(entity);
		final Renderer renderer = Renderer.mapper.getSafe(entity);
		shapeRenderer.setColor(debugColor);
		// Quick hack for drawing sweep
		if (Log.DEBUG || (renderer.renderAsShape && renderer.shouldRender)) {
			@NonNull final float[] vertices = position.bounds.getTransformedVertices();
			draw(vertices, renderer.renderAtPlayCoordinates);
		}
	}
	
	private void draw(float[] vertices, boolean renderAtPlayCoordinates) {
		// Don't need to see the collision outline for UI elements.
		if (renderAtPlayCoordinates) {
			float[] verticesCopy = vertices.clone();
			for (int i = 0; i < verticesCopy.length; i+= 2) {
				// x
				verticesCopy[i] = Conf.playToScreenX(verticesCopy[i]);
				// y
				verticesCopy[i + 1] = Conf.playToScreenY(verticesCopy[i + 1]);
			}
			shapeRenderer.polygon(verticesCopy);
		}
	}
}
