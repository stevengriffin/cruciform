package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.Animator;
import com.cruciform.components.Shooter;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.utils.Deferrer;

/** Handles setting the animation of an enemy if it has just fired a weapon. **/
public class AnimatorTriggerSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public AnimatorTriggerSystem(Deferrer deferrer) {
		super(Family.getFor(Shooter.class, Animator.class, TeamEnemy.class));
		this.deferrer = deferrer;
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final Shooter shooter = Shooter.mapper.get(entity);
		final Animator animator = Animator.mapper.get(entity);
		shooter.weapons.forEach((w) -> {
			if (w.getJustFired()) {
				animator.currentAnimation = animator.animations.get(Animator.States.FIRING);
				animator.currentAnimation.setPlayMode(PlayMode.LOOP);
				deferrer.runIfComplete(
					() -> animator.currentAnimation = animator.animations.get(Animator.States.IDLE),
					() -> w.getLastTimeFired(), 0.6f);
			}
		});
	}
}
