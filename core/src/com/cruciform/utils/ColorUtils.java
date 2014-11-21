package com.cruciform.utils;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {
	/** Returns a non-null color. **/
	public static @NonNull Color getColor(@Nullable Color color) {
		if (color == null) { throw new NullPointerException("Color was null."); }
		return color;
	}
}
