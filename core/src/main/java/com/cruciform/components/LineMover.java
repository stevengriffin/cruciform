package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class LineMover extends AbstractComponent {
    public static final ComponentMapper<LineMover> mapper = ComponentMapper.getFor(LineMover.class);
	public Vector2 maxVelocity;
	public Vector2 absMaxVelocity; // x, y always >= 0
	public Vector2 accel = new Vector2(0, 0);
	public float maxRotationalVelocity = 0.0f; // no support for accelerated rotation yet
	public boolean accelerates = false;
}
