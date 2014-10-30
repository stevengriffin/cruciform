package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cruciform.components.Parallax;
import com.cruciform.components.Position;
import com.cruciform.utils.Conf;

public class ParallaxSystem extends IteratingSystem {

	public ParallaxSystem() {
		super(Family.getFor(Position.class, Parallax.class));
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final Position position = Position.mapper.get(entity);
		final Parallax parallax = Parallax.mapper.get(entity);
		// How far parallaxed image can be moved without showing the edges in the play area.
		final float maxOffset = (position.bounds.getBoundingRectangle().width - 
				Conf.canonicalPlayWidth)/2;
		final float fractionOfPlayWidth = -parallax.referencePosition.bounds.getX()/Conf.canonicalPlayWidth;
		position.bounds.setPosition(parallax.xOffset + parallax.xMult*maxOffset*fractionOfPlayWidth,
				position.bounds.getY());
	}
}
