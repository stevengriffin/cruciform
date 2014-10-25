package com.cruciform.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cruciform.Cruciform;
import com.esotericsoftware.minlog.Log;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// Debug
		Log.set(Log.LEVEL_DEBUG);
		
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.foregroundFPS = 240;
		config.backgroundFPS = 240;
		config.title = "Cruciform";
		config.height = 600;
		config.width = 800;
		final Cruciform game = new Cruciform();
		final Application app = new LwjglApplication(game, config);
		game.application = app;
	}
}
