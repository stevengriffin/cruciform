package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.utils.Conf;

public class EnemyMarkerSystem extends IteratingSystem {

	private final ShapeRenderer shapeRenderer;
	private static final Color markerColor = Color.RED;
	
	public EnemyMarkerSystem(Batch batch, ShapeRenderer shapeRenderer) {
		super(Family.getFor(Position.class, Renderer.class, TeamEnemy.class));
		this.shapeRenderer = shapeRenderer;
	}

	@Override
	public void update(float deltaTime) {
		shapeRenderer.begin(ShapeType.Filled);
		super.update(deltaTime);
		shapeRenderer.end();
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		shapeRenderer.setColor(markerColor);
		Rectangle rect = position.bounds.getBoundingRectangle();
		shapeRenderer.rect(rect.x*Conf.scaleFactor,
				Conf.playBottom - rect.height*Conf.scaleFactor,
				rect.width*Conf.scaleFactor,
				rect.height*Conf.scaleFactor);
	}
}
