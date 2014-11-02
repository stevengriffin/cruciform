package com.cruciform.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameCamera {
	
	public static final float SMALL_SHAKE = 5.0f; 
	
	private static float x = 0;
	private static float y = 0;
	
	public static void setX(float newX) {
		x = newX*Conf.scaleFactor;
	}
	
	public static void setY(float newY) {
		y = newY*Conf.scaleFactor;
	}
	
	public static float getX() {
		return x;
	}
	
	public static float getY() {
		return y;
	}
	
	public static void shake(float initialIntensity) {
		final float initialX = 0;
		final float initialY = 0;
		final float repeatInterval = 1/30.0f;
		final int repeatCount = 10;
		Timer.schedule(new Task() {
			private float intensity = initialIntensity;
			
			@Override
			public void run() {
				setX(calculateShakeOffset(intensity));
				setY(calculateShakeOffset(intensity));
				intensity -= initialIntensity/repeatCount;
			}
			
		}, 0, repeatInterval, repeatCount);
		
		Timer.schedule(new Task() {

			@Override
			public void run() {
				setX(initialX);
				setY(initialY);
			}
			
		}, repeatInterval*(repeatCount+1));
	}
	
	private static float calculateShakeOffset(float intensity) {
		return MathUtils.random(-intensity, intensity);
	}
}
