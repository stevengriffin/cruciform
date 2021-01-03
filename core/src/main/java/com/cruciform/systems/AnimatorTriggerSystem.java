package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.cruciform.components.Animator;
import com.cruciform.components.Shooter;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.utils.Deferrer;

/** Handles setting the animation of an enemy if it has just fired a weapon. **/
public class AnimatorTriggerSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public AnimatorTriggerSystem(Deferrer deferrer) {
		super(Family.all(Shooter.class, Animator.class, TeamEnemy.class).get());
		this.deferrer = deferrer;
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final Shooter shooter = Shooter.mapper.getSafe(entity);
		final Animator animator = Animator.mapper.getSafe(entity);
		shooter.weapons.forEach((w) -> {
			if (w.getJustFired() && animator.animations.containsKey(Animator.States.FIRING)) {
				animator.currentAnimation = animator.animations.getSafe(Animator.States.FIRING, Animator.NULL_ANIMATION);
				animator.currentAnimation.setPlayMode(PlayMode.LOOP);
				deferrer.runIfComplete(
					() -> animator.currentAnimation = animator.animations.getSafe(Animator.States.IDLE, Animator.NULL_ANIMATION),
					() -> w.getLastTimeFired(), 0.6f);
			}
		});
	}
}
