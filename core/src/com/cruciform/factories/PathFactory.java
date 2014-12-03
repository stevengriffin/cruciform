package com.cruciform.factories;

import java.io.IOException;
import java.util.Arrays;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenPath;
import aurelienribon.tweenengine.TweenPaths;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.cruciform.Cruciform.GameManager;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Velocity;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.tweening.VectorAccessor;
import com.cruciform.utils.Conf;

public class PathFactory {
	
	private final GameManager manager;
	private final XmlReader reader;
	
	public PathFactory(final GameManager manager) {
		this.manager = manager;
		this.reader = new XmlReader();
		createPathFromSVG("svg_test");
	}

	private static class Waypoint {
		public static final Waypoint EMPTY = new Waypoint(TweenPaths.linear, Vector2.Zero);
		private final TweenPath pathType;
		private final Vector2 location;
		
		public Waypoint(TweenPath pathType, Vector2 location) {
			this.pathType = pathType;
			this.location = location;
		}
		
		public TweenPath getPathType() {
			return pathType;
		}

		public Vector2 getLocation() {
			return location;
		}

	}
	
	public void createPathFromSVG(String fileName) {
		try {
			final Element root = reader.parse(Gdx.files.internal("paths/" + fileName + ".svg"));
			// For some reason the method returns duplicates, so remove them.
			final Array<Element> array = new Array<Element>(true, 5, Element.class);
			array.addAll(root.getChildrenByNameRecursively("path"));
			Arrays.asList(array.toArray())
			.stream()
			.map((e) -> (e.get("d", "")))
			.distinct()
			.map((path) -> {
				System.out.println(path);
				final Array<Waypoint> waypoints = new Array<Waypoint>();
				final String[] words = path.split("\\p{Space}");
				for (int i = 0; i < words.length; ++i) {
					Waypoint currentPoint = Waypoint.EMPTY;
					final String[] data = words[i].split(",");
					if (data.length == 2) {
						// Process as coordinates
						if (currentPoint == Waypoint.EMPTY) {
							throw new GdxRuntimeException("Parse error in SVG path, expected " + 
									"coordinate to be preceded by tag.");
						} else {
						}
					} else if (data.length == 1){
						// Process as tag
					} else {
						throw new GdxRuntimeException("Parse error in SVG path, expected data " +
								"to have length 1 or 2 but got " + data.length + ".");
					}

				}
				return waypoints;
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private Position prepareEntity(final Entity entity) {
		entity.remove(LineMover.class);
		entity.remove(Velocity.class);
		Position position = Position.mapper.getSafe(entity);
		return position;
	}
}
