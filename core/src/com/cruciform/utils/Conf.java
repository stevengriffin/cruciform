package com.cruciform.utils;

public class Conf {
	// TODO persist with Preferences
	public static int screenHeight = 1080;
	public static int screenWidth = 1920;
	public static int playWidth = (int)(screenHeight / 1.2f);
	public static int playLeft = (screenWidth - playWidth)/2;
	public static int playRight = screenWidth - (screenWidth - playWidth)/2;
	public static int playCenter = (playLeft + playRight) / 2;
	public static int playBottom = screenHeight/20;
	public static float volume = 1.0f;
}
