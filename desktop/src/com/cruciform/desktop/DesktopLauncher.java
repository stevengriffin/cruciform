package com.cruciform.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cruciform.Cruciform;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Cruciform";
		config.height = 900;
		config.width = 1600;
		new LwjglApplication(new Cruciform(), config);
	}
}
