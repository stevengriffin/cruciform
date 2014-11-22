package com.cruciform.factories;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.badlogic.ashley.core.Entity;
import com.cruciform.Cruciform;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Velocity;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.utils.Conf;

public class PathFactory {
	
	private final Cruciform game;
	
	public PathFactory(final Cruciform game) {
		this.game = game;
	}
	
	public void createBentPath(final Entity entity, boolean reversed) {
		float xMove1 = -Conf.fractionX(0.3f);
		float yMove1 = -Conf.fractionY(0.1f);
		float xMove2 = Conf.fractionX(0.4f);
		float yMove2 = -Conf.fractionX(0.3f);
		if (reversed) {
			xMove1 = -xMove1;
			xMove2 = -xMove2;
		}
		Position position = prepareEntity(entity);
		
		Tween.to(position, PositionAccessor.POSITION_Y, 1.0f)
		.targetRelative(yMove1*2)
		.start(game.tweenManager);
		
		Timeline.createSequence()
		.delay(1.0f)
		.push(Tween.to(position, PositionAccessor.POSITION_XY, 3.0f)
			.targetRelative(xMove1, yMove1))
		.pushPause(1.0f)
		.push(Tween.to(position, PositionAccessor.POSITION_XY, 3.0f)
			.targetRelative(xMove2, yMove2))
		.repeatYoyo(Tween.INFINITY, 0.3f)
			.start(game.tweenManager);
	}

	private Position prepareEntity(final Entity entity) {
		entity.remove(LineMover.class);
		entity.remove(Velocity.class);
		Position position = Position.mapper.getSafe(entity);
		return position;
	}
}
