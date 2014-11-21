package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Animator;
import com.cruciform.components.Renderer;

public class AnimatorSystem extends IteratingSystem {

	public AnimatorSystem() {
		super(Family.all(Animator.class, Renderer.class).get());
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Animator animator = Animator.mapper.getSafe(entity);
		Renderer renderer = Renderer.mapper.getSafe(entity);
		animator.stateTime += deltaTime;
		renderer.image = animator.currentAnimation.getKeyFrame(animator.stateTime);
	}
}
