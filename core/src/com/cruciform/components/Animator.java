package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Animator extends AbstractComponent {
    public static final ComponentMapper<Animator> mapper = ComponentMapper.getFor(Animator.class);
	public Animation animation;
	public float stateTime = 0;
	
	public Animator(Entity entity) {
		super(entity);
	}
}
