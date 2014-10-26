package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends AbstractComponent {
    public static final ComponentMapper<Velocity> mapper = ComponentMapper.getFor(Velocity.class);
	public Vector2 linear = new Vector2(0, 0);
	/** Moves vx towards 0. Should always be positive. **/
	public float linearDragX = 0;
	/** Moves vy towards 0. Should always be positive. **/
	public float linearDragY = 0;
	public float rotational = 0.0f;
}
