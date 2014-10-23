package com.cruciform.utils;

public class Conf {
	// TODO persist with Preferences
	// TODO support smaller resolutions
	// 1920x1080
	// 1600x900
	// 1680x1050
	// 1440x900
	public static int screenHeight = 1080;
	public static int screenWidth = 1920;
	public static int playWidth = (int)(screenHeight / 1.2f);
	public static int playLeft = (screenWidth - playWidth)/2;
	public static int playRight = screenWidth - (screenWidth - playWidth)/2;
	public static int playCenter = (playLeft + playRight) / 2;
	public static int playBottom = fractionY(0.05f);
	public static float volume = 1.0f;

	public static void setResolution(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		playWidth = (int)(height / 1.2f);
		// Need at least 0.25 of width on the right.
		playRight = Math.min(screenWidth - (screenWidth - playWidth)/2, (int) (screenWidth*0.75f));
		playLeft = playRight - playWidth;
		playCenter = (playLeft + playRight) / 2;
		playBottom = fractionY(0.05f);
	}
	
	public static int fractionX(float fraction) {
		return (int) (playLeft + playWidth*fraction);
	}
	
	public static int fractionXRel(float fraction) {
		return (int) (playWidth*fraction);
	}
	
	public static int fractionY(float fraction) {
		return (int) (screenHeight*fraction);
	}
}
