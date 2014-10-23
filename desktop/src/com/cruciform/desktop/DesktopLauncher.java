package com.cruciform.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.foregroundFPS = 240;
		config.backgroundFPS = 240;
		config.title = "Cruciform";
		Conf.setResolution(1024, 768);
		config.height = Conf.screenHeight;
		config.width = Conf.screenWidth;
		final Cruciform game = new Cruciform();
		final Application app = new LwjglApplication(game, config);
		game.application = app;
	}
}
