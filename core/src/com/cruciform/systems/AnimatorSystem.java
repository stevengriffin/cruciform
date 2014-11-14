package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Animator;
import com.cruciform.components.Renderer;

public class AnimatorSystem extends IteratingSystem {

	public AnimatorSystem() {
		super(Family.getFor(Animator.class, Renderer.class));
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Animator animator = Animator.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		animator.stateTime += deltaTime;
		renderer.image = animator.currentAnimation.getKeyFrame(animator.stateTime);
	}
}
