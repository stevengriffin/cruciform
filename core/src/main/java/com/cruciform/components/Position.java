package com.cruciform.components;



import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;
import org.jetbrains.annotations.NotNull;

public class Position extends AbstractComponent {
    public static final ComponentMapper<Position> mapper = ComponentMapper.getFor(Position.class);
	@NotNull public Polygon bounds = Geometry.polyRect(0, 0, 0, 0);
	@NotNull public OutOfBoundsHandler outOfBoundsHandler = OutOfBoundsHandler.none();
	/** Set to 1 if this entity faces the top of the screen. **/
	public int yDirection = -1;
	
	public Position(Entity entity) {
		super(entity);
	}

	public static @NotNull Position defaultForBullet(Entity entity, float x, float y, float height, float width,
			float rotation) {
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, height, width);
		position.bounds.setRotation(rotation);
		position.outOfBoundsHandler = OutOfBoundsHandler.all();
		return position;
	}

	public static @NotNull Position defaultForPlayer(final Entity entity, final float x, final float y) {
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, 5, 5);
		position.yDirection = 1;
		return position;
	}
	
	public void incrementRotation(float degrees) {
		bounds.rotate(bounds.getRotation() + degrees);
	}

	public Vector2 getCenter() {
		return new Vector2(bounds.getX(), bounds.getY());
	}
	
}