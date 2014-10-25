package com.cruciform.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import com.cruciform.images.ImageManager;
import com.esotericsoftware.minlog.Log;

/** Stores and persists application-wide settings.
    Play coordinates go from x: 0 to canonicalPlayWidth, y: 0 to canonicalPlayHeight
	UI coordinates go from 0 to screenWidth, y: 0 to screenHeight
	Both play objects and UI objects are scaled by scaleFactor when drawn.
	Text is NOT scaled yet. **/
public class Conf {
	
	// Immutable settings
	private static final String PREFERENCES_NAME = "com.cruciform.settings";
	public static final int canonicalWidth = 1920;
	public static final int canonicalHeight = 1080;
	public static final int canonicalPlayWidth = (int) (canonicalHeight/1.2f);
	public static final int canonicalPlayHeight = (int) (canonicalHeight*0.95f);
	
	// Derived settings
	public static int playWidth;
	public static int playLeft;
	public static int playRight;
	public static int playCenter;
	public static int playBottom;
	public static float scaleFactor = 1;
	public static int screenCenterX;
	
	// Persisted settings
	public static int screenHeight;
	public static int screenWidth;
	public static float volume;
	public static boolean fullScreen = true;
	public static float mouseSensitivity;

	public static class Setting<T> {
		public final String name;
		private T value = null;
		public final T min;
		public final T max;
		
		public Setting(final String name) {
			this.name = name;
			this.min = null;
			this.max = null;
		}
		
		public Setting(final String name, final T min, final T max) {
			this.name = name;
			this.min = min;
			this.max = max;
		}
	
		public void set(final T value) {
			this.value = value;
		}
		
		public T get() {
			return this.value;
		}
	}
	
	public static class SettingsProposal {
		public final Setting<Float> volume = new Setting<>("Volume", 0.0f, 1.0f);
		public final Setting<Integer> screenWidth = new Setting<>("Screen Width");
		public final Setting<Integer> screenHeight = new Setting<>("Screen Height");
		public final Setting<Boolean> fullScreen = new Setting<>("Full Screen");
		public final Setting<Float> mouseSensitivity = new Setting<>("Mouse Sensitivity", 0.05f, 2.0f);
	
		public static SettingsProposal fromCurrentSettings() {
			final SettingsProposal proposal = new SettingsProposal();
			proposal.volume.set(Conf.volume);
			proposal.screenWidth.set(Conf.screenWidth);
			proposal.screenHeight.set(Conf.screenHeight);
			proposal.fullScreen.set(Conf.fullScreen);
			proposal.mouseSensitivity.set(Conf.mouseSensitivity);
			return proposal;
		}
		
		public static SettingsProposal fromPersistedSettings() {
			final Preferences preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
			final SettingsProposal proposal = new SettingsProposal();
			proposal.volume.set(preferences.getFloat(proposal.volume.name, 1.0f));
			// Set to reasonable default based on client PC.
			DisplayMode displayMode = Gdx.graphics.getDesktopDisplayMode();
			proposal.screenWidth.set(preferences.getInteger(proposal.screenWidth.name, displayMode.width));
			proposal.screenHeight.set(preferences.getInteger(proposal.screenHeight.name, displayMode.height));
			proposal.fullScreen.set(preferences.getBoolean(proposal.fullScreen.name, true));
			proposal.mouseSensitivity.set(preferences.getFloat(proposal.mouseSensitivity.name, 0.5f));
			return proposal;
		}
	
		public void syncWithCurrentSettings() {
			//Log.debug("vol: " + Conf.volume + " this.vol: " + this.volume.get());
			Log.debug(this.volume.get().getClass().toString());
			Conf.volume = this.volume.get();
			Conf.screenWidth = this.screenWidth.get();
			Conf.screenHeight = this.screenHeight.get();
			Conf.fullScreen = this.fullScreen.get();
			Conf.mouseSensitivity = this.mouseSensitivity.get();
			setResolution();
		}
		
		public void persist() {
			syncWithCurrentSettings();
			final Preferences preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
			preferences.putFloat(this.volume.name, this.volume.get());
			preferences.putInteger(this.screenWidth.name, this.screenWidth.get());
			preferences.putInteger(this.screenHeight.name, this.screenHeight.get());
			preferences.putBoolean(this.fullScreen.name, this.fullScreen.get());
			preferences.putFloat(this.mouseSensitivity.name, this.mouseSensitivity.get());
			preferences.flush();
		}
	}
	
	public static void loadSettings() {
		SettingsProposal.fromPersistedSettings().syncWithCurrentSettings();
	}

	public static void saveSettings() {
		SettingsProposal.fromCurrentSettings().persist();
	}
	
	public static void setResolution() {
		Log.debug("Setting res to " + screenWidth + " x " + screenHeight);
		// TODO delete
//		screenWidth = 1920;
//		screenHeight = 1080;
//		screenWidth = 1440;
//		screenHeight = 900;
		Gdx.graphics.setDisplayMode(screenWidth, screenHeight, fullScreen);
		final float oldScaleFactor = scaleFactor; 
		scaleFactor = ((float) screenHeight)/canonicalHeight;
		ImageManager.scalePatches(scaleFactor/oldScaleFactor);
		Log.debug("sF: " + scaleFactor);
		screenCenterX = screenWidth/2;
		playWidth = (int)(screenHeight / 1.2f);
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
