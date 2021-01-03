package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.SafeObjectMap;

public class Animator extends AbstractComponent {
	public enum States {
		IDLE,
		FIRING,
	}

	public static final Animation<TextureRegion> NULL_ANIMATION =  new Animation<>(1.0f, ImageManager.BLANK);

    public static final ComponentMapper<Animator> mapper = ComponentMapper.getFor(Animator.class);
	public Animation<TextureRegion> currentAnimation = NULL_ANIMATION;
	public SafeObjectMap<States, Animation<TextureRegion>> animations = new SafeObjectMap<>();
	public float stateTime = 0;
	
	public Animator(Entity entity) {
		super(entity);
	}
}
