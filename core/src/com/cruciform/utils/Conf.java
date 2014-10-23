package com.cruciform.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.cruciform.images.ImageManager;
import com.esotericsoftware.minlog.Log;

/** Stores and persists application-wide settings.
    Play coordinates go from x: 0 to canonicalPlayWidth, y: 0 to canonicalPlayHeight
	UI coordinates go from 0 to screenWidth, y: 0 to screenHeight
	Both play objects and UI objects are scaled by scaleFactor when drawn.
	Text is NOT scaled yet. **/
public class Conf {
	private static final String PREFERENCES_NAME = "com.cruciform.settings";
	private static final String SCREEN_HEIGHT_NAME = "screenHeight";
	private static final String SCREEN_WIDTH_NAME = "screenWidth";
	private static final String FULL_SCREEN_NAME = "fullScreen";
	private static final String VOLUME_NAME = "volume";
	public static final int canonicalWidth = 1920;
	public static final int canonicalHeight = 1080;
	public static final int canonicalPlayWidth = (int) (canonicalHeight/1.2f);
	public static final int canonicalPlayHeight = (int) (canonicalHeight*0.95f);
	public static int screenHeight;
	public static int screenWidth;
	public static int playWidth;
	public static int playLeft;
	public static int playRight;
	public static int playCenter;
	public static int playBottom;
	public static float scaleFactor = 1;
	public static int screenCenterX;
	public static float volume;
	public static boolean fullScreen = true;

	public static void loadSettings(Application app) {
		Preferences prefs = app.getPreferences(PREFERENCES_NAME);
		// TODO set to reasonable default based on client PC.
		setResolution(prefs.getInteger(SCREEN_WIDTH_NAME, 1920),
				prefs.getInteger(SCREEN_HEIGHT_NAME, 1080),
				prefs.getBoolean(FULL_SCREEN_NAME, true));
		setVolume(prefs.getFloat(VOLUME_NAME, 1.0f));
	}

	public static void saveSettings(Application app) {
		Preferences prefs = app.getPreferences(PREFERENCES_NAME);
		prefs.putInteger(SCREEN_WIDTH_NAME, screenWidth);
		prefs.putInteger(SCREEN_HEIGHT_NAME, screenHeight);
		prefs.putBoolean(FULL_SCREEN_NAME, fullScreen);
		prefs.putFloat(VOLUME_NAME, volume);
		prefs.flush();
	}
	
	public static void setVolume(float newVolume) {
		// TODO adjust currently playing music volumes.
		volume = newVolume;
	}
	
	public static void setResolution(int width, int height, boolean isFullScreen) {
		fullScreen = isFullScreen;
		Gdx.graphics.setDisplayMode(width, height, fullScreen);
		final float oldScaleFactor = scaleFactor; 
		scaleFactor = ((float) height)/canonicalHeight;
		ImageManager.scalePatches(scaleFactor/oldScaleFactor);
		Log.debug("sF: " + scaleFactor);
		screenWidth = width;
		screenHeight = height;
		screenCenterX = width/2;
		playWidth = (int)(height / 1.2f);
		// Need at least 0.25 of width on the right.
		playRight = Math.min(screenWidth - (screenWidth - playWidth)/2, (int) (screenWidth*0.75f));
		playLeft = playRight - playWidth;
		playCenter = (playLeft + playRight) / 2;
		playBottom = fractionY(0.05f);
		Log.debug("pw: " + playWidth + " pr: " + playRight + " pl: " + playLeft + " pc: " + playCenter + " pb: " + playBottom);
	}
	
	public static int fractionX(float fraction) {
		return (int) (canonicalPlayWidth*fraction);
	}
	
	public static int fractionY(float fraction) {
		return (int) (canonicalPlayHeight*fraction);
	}
	
	public static int fractionXLeftUI(float fraction) {
		return (int) (playRight + (screenWidth - playRight)*fraction);
	}
	
	public static float playToScreenX(float x) {
		return x*scaleFactor + playLeft;
	}
	public static float playToScreenY(float y) {
		return y*Conf.scaleFactor + Conf.playBottom;
	}
	
}
