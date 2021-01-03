package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.cruciform.components.Child;
import com.cruciform.components.Position;
import com.cruciform.components.Recoil;
import com.cruciform.components.Shooter;
import com.cruciform.weapons.Weapon;

public class RecoilSystem extends IteratingSystem {

	private static final float RECOIL_MULT = 1;
	
	public RecoilSystem() {
		super(Family.all(Child.class, Position.class, Recoil.class).get());
		// Execute after ChildPositionSystem
		this.priority = 100;
	}
	
	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Child child = Child.mapper.getSafe(entity);
		final Position childPosition = Position.mapper.getSafe(entity);
		final Recoil childRecoil = Recoil.mapper.getSafe(entity);
		final Position parentPosition = Position.mapper.get(child.parent);
		final Shooter parentShooter = Shooter.mapper.get(child.parent);
		if (parentPosition == null || parentShooter == null) {
			return;
		}
		float yOffset = childRecoil.yOffset;
		for (Weapon weapon : parentShooter.weapons) {
			if (weapon.getJustFired()) {
				yOffset += childPosition.yDirection*weapon.damage*RECOIL_MULT;
			}
		}
		yOffset -= childPosition.yDirection*childRecoil.recoveryRate*deltaTime;
		if (childPosition.yDirection == 1) {
			yOffset = MathUtils.clamp(yOffset, 0, childRecoil.maxOffset);
		} else {
			yOffset = MathUtils.clamp(yOffset, -childRecoil.maxOffset, 0);
		}
		childPosition.bounds.setPosition(parentPosition.bounds.getX(),
				parentPosition.bounds.getY() - yOffset);
		childRecoil.yOffset = yOffset;
	}
}
