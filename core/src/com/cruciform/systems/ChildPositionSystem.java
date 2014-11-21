package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Child;
import com.cruciform.components.Position;
import com.cruciform.components.Recoil;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.Deferrer.RemovalUrgency;
import com.esotericsoftware.minlog.Log;

public class ChildPositionSystem extends IteratingSystem {

	private final Deferrer deferrer;
	
	public ChildPositionSystem(final Deferrer deferrer) {
		super(Family.all(Child.class, Position.class).get());
		this.deferrer = deferrer;
		this.priority = 5;
	}
	
	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		final Child child = Child.mapper.getSafe(entity);
		if (deferrer.entityToBeRemoved(child.parent)) {
			Log.debug("removing entity");
			deferrer.remove(entity, RemovalUrgency.UNIMPORTANT);
		}
		final Position childPosition = Position.mapper.getSafe(entity);
		final Position parentPosition = Position.mapper.get(child.parent);
		if (parentPosition == null) {
			return;
		}
		final Recoil recoil = Recoil.mapper.get(entity);
		if (recoil != null) {
			return;
		}
		childPosition.bounds.setPosition(parentPosition.bounds.getX(), parentPosition.bounds.getY());
		childPosition.bounds.setRotation(parentPosition.bounds.getRotation());
	}
}
