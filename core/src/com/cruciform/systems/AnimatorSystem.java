package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.cruciform.components.Animator;
import com.cruciform.components.Animator.States;
import com.cruciform.components.Renderer;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.SafeObjectMap;

public class AnimatorSystem extends IteratingSystem {

	public static SafeObjectMap<EnemyTypes, SafeObjectMap<States, Animation>> 
	enemyAnimations = new SafeObjectMap<>();

	static {
		SafeObjectMap<States, Animation> ghostAnimations = new SafeObjectMap<>();
		ghostAnimations.put(Animator.States.IDLE,
				new Animation(0.125f, ImageManager.GHOST_1));
		ghostAnimations.put(Animator.States.FIRING,
				new Animation(0.125f, ImageManager.GHOST_1_FIRING));
		
		SafeObjectMap<States, Animation> pentahorrorAnimations = new SafeObjectMap<>();
		pentahorrorAnimations.put(Animator.States.IDLE,
				new Animation(0.167f, ImageManager.PENTAHORROR));
		pentahorrorAnimations.put(Animator.States.FIRING,
				new Animation(0.167f, ImageManager.PENTAHORROR));
		
		enemyAnimations.put(EnemyTypes.PENTAGRAM, pentahorrorAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_3PRONG, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_LURCHER, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_SPIRALER, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_SPIRALER_SOLID, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_SPLITTER, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_STRAIGHT, ghostAnimations);
		enemyAnimations.put(EnemyTypes.RADIAL_SPLITTER, ghostAnimations);
	}
	
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
