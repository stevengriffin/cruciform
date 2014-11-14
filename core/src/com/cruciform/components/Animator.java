package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ObjectMap;

public class Animator extends AbstractComponent {
	public enum States {
		IDLE,
		FIRING,
	}
	
    public static final ComponentMapper<Animator> mapper = ComponentMapper.getFor(Animator.class);
	public Animation currentAnimation;
	public ObjectMap<States, Animation> animations = new ObjectMap<>();
	public float stateTime = 0;
	
	public Animator(Entity entity) {
		super(entity);
	}
}
