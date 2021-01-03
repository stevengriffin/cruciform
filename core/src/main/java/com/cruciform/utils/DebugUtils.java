package com.cruciform.utils;

import com.badlogic.gdx.Gdx;

public class DebugUtils {
	public static void prepareForBreakPoint() {
		Gdx.input.setCursorCatched(false);
	}
}
