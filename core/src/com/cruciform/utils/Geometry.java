package com.cruciform.utils;

import com.badlogic.gdx.math.Polygon;

public class Geometry {
	
	// Constructs a rectangular polygon using the Rectangle constructor format.
	public static Polygon polyRect(float x, float y, float width, float height) {
		Polygon poly = new Polygon(new float[] { -width/2, -height/2, width/2, -height/2, width/2, height/2, -width/2, height/2 });
		poly.setPosition(x, y);
		return poly;
	}
}
