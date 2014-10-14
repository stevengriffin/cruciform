package com.cruciform.utils;

import com.badlogic.gdx.math.Polygon;

public class OutOfBoundsHandler {
	
	private boolean removeWhenSouth = false;
	private boolean removeWhenNorth = false;
	private boolean removeWhenWest = false;
	private boolean removeWhenEast = false;
	
	public static OutOfBoundsHandler none() {
		return new OutOfBoundsHandler();
	}
	
	public static OutOfBoundsHandler all() {
		OutOfBoundsHandler handler = new OutOfBoundsHandler();
		handler.removeWhenSouth = true;
		handler.removeWhenNorth = true;
		handler.removeWhenWest = true;
		handler.removeWhenEast = true;
		return handler;
	}
	
	public static OutOfBoundsHandler south() {
		OutOfBoundsHandler handler = new OutOfBoundsHandler();
		handler.removeWhenSouth = true;
		return handler;
	}
	
	public boolean isOutOfBounds(Polygon bounds) {
		return (bounds.getX() > Conf.screenWidth * 1.3f && removeWhenEast) ||
				(bounds.getX() < -Conf.screenWidth * 0.3f && removeWhenWest) ||
				(bounds.getY() > Conf.screenHeight * 1.3f && removeWhenNorth) ||
				(bounds.getY() < -Conf.screenHeight * 0.3f && removeWhenSouth);
	}
}
