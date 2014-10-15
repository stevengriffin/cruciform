package com.cruciform.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.foregroundFPS = 120;
		config.backgroundFPS = 120;
		config.title = "Cruciform";
		config.height = Conf.screenHeight;
		config.width = Conf.screenWidth;
		new LwjglApplication(new Cruciform(), config);
	}
}
