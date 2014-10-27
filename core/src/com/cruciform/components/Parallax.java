package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class Parallax extends AbstractComponent {
    public static final ComponentMapper<Parallax> mapper = ComponentMapper.getFor(Parallax.class);
	public Position referencePosition;
	public float xMult = 0.5f;
	public float xOffset = 0.0f;
	
	public Parallax(final Entity entity) {
		super(entity);
	}
}
