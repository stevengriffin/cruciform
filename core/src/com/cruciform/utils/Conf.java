package com.cruciform.utils;

import com.esotericsoftware.minlog.Log;

public class Conf {
	// TODO persist with Preferences
	// TODO support smaller resolutions
	// 1920x1080
	// 1600x900
	// 1680x1050
	// 1440x900
	// Play coordinates go from x: 0 to canonicalPlayWidth, y: 0 to canonicalPlayHeight
	// UI coordinates go from 0 to screenWidth, y: 0 to screenHeight
	public static final int canonicalWidth = 1920;
	public static final int canonicalHeight = 1080;
	public static final int canonicalPlayWidth = (int) (canonicalHeight/1.2f);
	public static final int canonicalPlayHeight = (int) (canonicalHeight*0.95f);
	public static int screenHeight = 1080;
	public static int screenWidth = 1920;
	public static int playWidth = (int)(screenHeight / 1.2f);
	public static int playLeft = (screenWidth - playWidth)/2;
	public static int playRight = screenWidth - (screenWidth - playWidth)/2;
	public static int playCenter = (playLeft + playRight) / 2;
	public static int playBottom = fractionY(0.05f);
	public static float scaleFactor;
	public static float volume = 1.0f;

	public static void setResolution(int width, int height) {
		scaleFactor = ((float) height)/canonicalHeight;
		Log.debug("sF: " + scaleFactor);
		screenWidth = width;
		screenHeight = height;
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
