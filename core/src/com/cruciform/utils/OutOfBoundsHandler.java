package com.cruciform.utils;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

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
		Rectangle rect = bounds.getBoundingRectangle();
		return (rect.x > Conf.playRight && removeWhenEast) ||
				(rect.x + rect.width < Conf.playLeft && removeWhenWest) ||
				(rect.y > Conf.screenHeight * 1.3f && removeWhenNorth) ||
				(rect.y + rect.height < 0.0f && removeWhenSouth);
	}
}
