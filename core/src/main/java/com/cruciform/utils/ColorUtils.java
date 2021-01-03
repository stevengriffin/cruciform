package com.cruciform.utils;




import com.badlogic.gdx.graphics.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorUtils {
	/** Returns a non-null color. **/
	public static @NotNull Color getColor(@Nullable Color color) {
		if (color == null) { throw new NullPointerException("Color was null."); }
		return color;
	}
}
