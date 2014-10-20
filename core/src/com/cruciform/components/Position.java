package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Polygon;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;

public class Position extends AbstractComponent {
    public static final ComponentMapper<Position> mapper = ComponentMapper.getFor(Position.class);
	public Polygon bounds;
	public OutOfBoundsHandler outOfBoundsHandler = OutOfBoundsHandler.none();
	public int yDirection;
	
	public Position(Entity entity) {
		super(entity);
	}

	public static Position defaultForBullet(Entity entity, float x, float y, float height, float width,
			float rotation) {
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, height, width);
		position.bounds.rotate(rotation);
		position.outOfBoundsHandler = OutOfBoundsHandler.all();
		return position;
	}
	
	public void incrementRotation(float degrees) {
		bounds.rotate(bounds.getRotation() + degrees);
	}
	
}