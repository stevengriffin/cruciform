package com.cruciform.utils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

@NonNullByDefault
public class Geometry {
	
	/** Constructs a rectangular polygon using the Rectangle constructor format. **/
	public static Polygon polyRect(float x, float y, float width, float height) {
		Polygon poly = new Polygon(new float[] { -width/2, -height/2, width/2, -height/2,
				width/2, height/2, -width/2, height/2 });
		poly.setPosition(x, y);
		return poly;
	}
	
	/** Constructs a trapezoidal polygon using the Rectangle constructor format. **/
	public static Polygon polyTrapezoid(float x, float y, float widthTop, float widthBottom, float height) {
		Polygon poly = new Polygon(new float[] { -widthTop/2, -height/2, widthTop/2, -height/2,
				widthBottom/2, height/2, -widthBottom/2, height/2 });
		poly.setPosition(x, y);
		return poly;
	}
	
	public static Vector2 rotatedVector(float rotation, float speed) {
		final Vector2 vec = new Vector2(speed, 0);
		vec.rotate(rotation);
		return vec;
//		return new Vector2(speed*MathUtils.cosDeg(rotation),
//				speed*MathUtils.sinDeg(rotation));
	}
}
