package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Polygon;
import com.cruciform.utils.OutOfBoundsHandler;

public class Position extends AbstractComponent {
    public static final ComponentMapper<Position> mapper = ComponentMapper.getFor(Position.class);
	public Polygon bounds;
	public OutOfBoundsHandler outOfBoundsHandler = OutOfBoundsHandler.none();
	public int yDirection;
}