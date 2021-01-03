package com.cruciform.factories;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.Cruciform.GameManager;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Velocity;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.tweening.SVGPathParser;
import com.cruciform.tweening.VectorAccessor;
import com.cruciform.tweening.SVGPathParser.WaypointPath;
import com.cruciform.utils.Conf;

public class PathFactory {
	
	private final GameManager manager;
	
	public final WaypointPath straightTestPath;
	
	public PathFactory(final GameManager manager, final TweenManager tweenManager) {
		this.manager = manager;
		SVGPathParser parser = new SVGPathParser(tweenManager); // TODO
		straightTestPath = parser.createPathFromSVG("svg_straight_test");
	}
	
	public Entity createBentPath(final Entity entity, boolean reversed) {
		float xMove1 = -Conf.fractionX(0.3f);
		float yMove1 = -Conf.fractionY(0.1f);
		float xMove2 = Conf.fractionX(0.4f);
		float yMove2 = -Conf.fractionX(0.3f);
		if (reversed) {
			xMove1 = -xMove1;
			xMove2 = -xMove2;
		}
		final Position position = prepareEntity(entity);
		
		Tween.to(position, PositionAccessor.POSITION_Y, 1.0f)
		.targetRelative(yMove1*2)
		.start(manager.tweenManager);
		
		Timeline.createSequence()
		.delay(1.0f)
		.push(Tween.to(position, PositionAccessor.POSITION_XY, 3.0f)
			.targetRelative(xMove1, yMove1))
		.pushPause(1.0f)
		.push(Tween.to(position, PositionAccessor.POSITION_XY, 3.0f)
			.targetRelative(xMove2, yMove2))
		.repeatYoyo(Tween.INFINITY, 0.3f)
			.start(manager.tweenManager);
		return entity;
	}

	public Entity createLurchingPath(final Entity entity) {
		// Make sure the LineMover has been added to the entity.
		manager.engine.processComponentOperations();
		final Vector2 maxV = LineMover.mapper.getSafe(entity).maxVelocity;
		final Vector2 v = Velocity.mapper.getSafe(entity).linear;
		entity.remove(LineMover.class);
		
		Timeline.createSequence()
		.push(Tween.to(v, VectorAccessor.VECTOR_XY, 0.2f)
		.targetRelative(maxV.x, maxV.y))
		.pushPause(0.4f)
		.push(Tween.to(v, VectorAccessor.VECTOR_XY, 0.2f)
				.target(0, 0))
		.repeat(Tween.INFINITY, 0.3f)
		.start(manager.tweenManager);
		return entity;
	}
	
	public static Position prepareEntity(final Entity entity) {
		entity.remove(LineMover.class);
		entity.remove(Velocity.class);
		Position position = Position.mapper.getSafe(entity);
		return position;
	}
}
