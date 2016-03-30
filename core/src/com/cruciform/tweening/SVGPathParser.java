package com.cruciform.tweening;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPath;
import aurelienribon.tweenengine.TweenPaths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.cruciform.components.Position;
import com.cruciform.factories.PathFactory;
import com.cruciform.utils.CollectionUtils;
import com.cruciform.weapons.BulletRuleHandler;

public class SVGPathParser {

	private final XmlReader reader;
	private final TweenManager tweenManager;
	
	public SVGPathParser(TweenManager tweenManager) {
		this.reader = new XmlReader();
		this.tweenManager = tweenManager;
	}
	
	private static class Waypoint {
		public static final Waypoint EMPTY = new Waypoint(TweenPaths.linear, Vector2.Zero);
		private final TweenPath pathType;
		private final Vector2 location;
		
		public Waypoint(TweenPath pathType, Vector2 location) {
			this.pathType = pathType;
			this.location = location;
		}
		
		public void pushPathEvent(Tween tween) {
			tween.path(pathType).waypoint(location.x, location.y);
			System.out.println("waypoint at " + location.x + ", " + location.y);
		}
	
		public void setOrigin(Vector2 origin) {
			location.sub(origin);
		}
		
		public Vector2 getLocation() {
			return location;
		}
		
		@Override
		public String toString() {
			return "Waypoint [pathType=" + pathType + ", location=" + location
					+ "]";
		}
	}

	public static class WaypointPath {
		private final List<Array<Waypoint>> paths;
		private final TweenManager tweenManager;
		
		public WaypointPath(List<Array<Waypoint>> paths, TweenManager tweenManager) {
			this.paths = paths;
			this.tweenManager = tweenManager;
		}
		
		public void producePathRules(BulletRuleHandler handler) {
			// TODO
			handler.addRule((entity, index) -> {
//				if (index >= paths.size()) {
//					return entity;
//				}
				final Position position = PathFactory.prepareEntity(entity);
				final Array<Waypoint> wPath = paths.get(0); //index);
				final Tween tween = Tween.to(position, PositionAccessor.POSITION_XY, 6.0f)
				.targetRelative(position.bounds.getX(), position.bounds.getY());
				for (int i = 0; i < wPath.size; i++) {
					wPath.get(i).pushPathEvent(tween);
				}
				tween.setCallback((type, source) -> { System.out.println("finished"); });
				tween.start(tweenManager);
				return entity;
			}, 1);
		}
	}
	
	public @NonNull WaypointPath createPathFromSVG(String fileName) {
		System.out.println("creating path from svg " + fileName);
		final Element root = findRootOfSVGFile(fileName);
		final Array<Element> array = new Array<Element>(true, 5, Element.class);
		array.addAll(root.getChildrenByNameRecursively("path"));
		final List<Array<Waypoint>> paths = 
				CollectionUtils.toStream(array)
				.map(e -> (e.get("d", "")))
				.map(SVGPathParser::parsePath)
				.collect(Collectors.toList());
		paths.forEach(waypoints -> (waypoints.forEach(System.out::println)));
		return new WaypointPath(paths, tweenManager);
	}

	private Element findRootOfSVGFile(String fileName) {
		try {
			return reader.parse(Gdx.files.internal("paths/" + fileName + ".svg"));
		} catch (IOException e) {
			throw new GdxRuntimeException("Could not parse SVG file " + fileName);
		}
	}
	
	private static Array<Waypoint> parsePath(String path) {
		System.out.println(path);
		final Array<Waypoint> waypoints = new Array<Waypoint>();
		final String[] words = path.split("\\p{Space}");
		@NonNull TweenPath currentTag = TweenPaths.linear;
		for (int i = 0; i < words.length; ++i) {
			final String[] data = words[i].split(",");
			if (data.length == 2) {
				// Process as coordinates
				waypoints.add(new Waypoint(currentTag,
						new Vector2(Float.parseFloat(data[0]),
								-Float.parseFloat(data[1]))));
			} else if (data.length == 1) {
				currentTag = processTag(data[0]);
			} else {
				throw new GdxRuntimeException("Parse error in SVG path, expected data " +
						"to have length 1 or 2 but got " + data.length + ".");
			}
		}
		if (waypoints.size > 0) {
			final Vector2 origin = waypoints.get(0).getLocation().cpy();
			waypoints.forEach(w -> w.setOrigin(origin));
		}
		return waypoints;
	}

	/**
	 * See http://www.w3.org/TR/SVG/paths.html.
	 * m = moveto relative
	 * M = moveto absolute
	 * l = lineto relative
	 * L = lineto absolute
	 * z = close the current subpath
	 * c = curve relative
	 */
	private static TweenPath processTag(String tagName) {
		System.out.println(tagName);
		if (tagName.equals("c")) {
			return TweenPaths.catmullRom;
		}
		return TweenPaths.linear;
	}
	
}
