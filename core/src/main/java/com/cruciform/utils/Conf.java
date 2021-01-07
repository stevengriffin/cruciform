package com.cruciform.utils;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.cruciform.images.ImageManager;
import com.esotericsoftware.minlog.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	/** 900 **/
	public static final int canonicalPlayWidth = (int) (canonicalHeight/1.2f);
	/** 1026 **/
	public static final int canonicalPlayHeight = (int) (canonicalHeight*0.95f);
	public static final int canonicalPlayRight = calculatePlayRight(canonicalWidth, canonicalPlayWidth);
	public static final int canonicalPlayLeft = canonicalPlayRight - canonicalPlayWidth;
	
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
		private T value;
		private final T min;
		private final T max;
		private final T defaultValue;
		private boolean hasMinMax;
		
		public Setting(final String name, @NotNull final T defaultValue) {
			this.name = name;
			this.min = defaultValue;
			this.max = defaultValue;
			this.hasMinMax = false;
			this.defaultValue = defaultValue;
		}
		
		public Setting(final String name, @NotNull final T defaultValue, final T min, final T max) {
			this.name = name;
			this.min = min;
			this.max = max;
			this.hasMinMax = true;
			this.defaultValue = defaultValue;
		}
	
		public void set(final @Nullable T value) {
			this.value = value;
		}
		
		public @NotNull T get() {
			if (this.value != null) {
				return this.value;
			} else {
				return this.defaultValue;
			}
		}
		
		public T getMin() {
			return min;
		}
		
		public T getMax() {
			return max;
		}
		
		public boolean hasMinMax() {
			return hasMinMax;
		}
	}
	
	public static class SettingsProposal {
		public final Setting<Boolean> settingsInitialized = new Setting<Boolean>("Settings Initialized", true);
		public final Setting<Float> volume = new Setting<Float>("Volume", 1.0f, 0.0f, 1.0f);
		public final Setting<Integer> screenWidth = new Setting<Integer>("Screen Width",
				new Integer(Gdx.graphics.getWidth()));
		public final Setting<Integer> screenHeight = new Setting<Integer>("Screen Height",
				new Integer(Gdx.graphics.getHeight()));
		public final Setting<Boolean> fullScreen = new Setting<Boolean>("Full Screen", true);
		public final Setting<Float> mouseSensitivity = new Setting<Float>("Mouse Sensitivity", 0.5f, 0.05f, 2.0f);
	
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
			if (!preferences.getBoolean(proposal.settingsInitialized.name)) {
				return proposal;
			}
			proposal.volume.set(preferences.getFloat(proposal.volume.name));
			proposal.screenWidth.set(preferences.getInteger(proposal.screenWidth.name));
			proposal.screenHeight.set(preferences.getInteger(proposal.screenHeight.name));
			proposal.fullScreen.set(preferences.getBoolean(proposal.fullScreen.name));
			proposal.mouseSensitivity.set(preferences.getFloat(proposal.mouseSensitivity.name));
			return proposal;
		}
	
		public boolean syncWithCurrentSettings() {
			//Log.debug("vol: " + Conf.volume + " this.vol: " + this.volume.get());
			Log.debug(this.volume.get().getClass().toString());
			Conf.volume = this.volume.get();
			final int oldScreenWidth = Conf.screenWidth;
			final int oldScreenHeight = Conf.screenHeight;
			final boolean oldFullscreen = Conf.fullScreen;
			Conf.screenWidth = this.screenWidth.get();
			Conf.screenHeight = this.screenHeight.get();
			Conf.fullScreen = this.fullScreen.get();
			Conf.mouseSensitivity = this.mouseSensitivity.get();
			// Try to set the resolution. Revert resolution settings on failure.
			if (!setResolution()) {
				Conf.screenWidth = oldScreenWidth;
				Conf.screenHeight = oldScreenHeight;
				Conf.fullScreen = oldFullscreen;
				return false;
			}
			return true;
		}
		
		public boolean persist() {
			final boolean success = syncWithCurrentSettings();
			final Preferences preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
			preferences.putBoolean(this.settingsInitialized.name, this.settingsInitialized.get());
			preferences.putFloat(this.volume.name, this.volume.get());
			preferences.putInteger(this.screenWidth.name, this.screenWidth.get());
			preferences.putInteger(this.screenHeight.name, this.screenHeight.get());
			preferences.putBoolean(this.fullScreen.name, this.fullScreen.get());
			preferences.putFloat(this.mouseSensitivity.name, this.mouseSensitivity.get());
			preferences.flush();
			return success;
		}
	}
	
	public static boolean loadSettings() {
		return SettingsProposal.fromPersistedSettings().syncWithCurrentSettings();
	}

	public static boolean saveSettings() {
		return SettingsProposal.fromCurrentSettings().persist();
	}
	
	public static boolean setResolution() {
		final boolean success;
		Log.debug("Setting res to " + screenWidth + " x " + screenHeight);
		// TODO delete
//		screenWidth = 1920;
//		screenHeight = 1080;
//		screenWidth = 1440;
//		screenHeight = 900;
//		screenWidth = 2560;
//		screenHeight = 1440;
		if (fullScreen) {
			final Array<Graphics.DisplayMode> modes = getSortedDisplayModes();
			final int modeChoiceIndex = computeCurrentModeChoiceIndex(modes);
			success = Gdx.graphics.setFullscreenMode(modes.get(modeChoiceIndex));
		} else {
			success = Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
		}
		if (success) {
			final float oldScaleFactor = scaleFactor;
			scaleFactor = ((float) screenHeight) / canonicalHeight;
			ImageManager.scalePatches(scaleFactor / oldScaleFactor);
			Log.debug("sF: " + scaleFactor);
			screenCenterX = screenWidth / 2;
			playWidth = (int) (screenHeight / 1.2f);
			playRight = calculatePlayRight(screenWidth, playWidth);
			playLeft = playRight - playWidth;
			playCenter = (playLeft + playRight) / 2;
			playBottom = fractionY(0.05f);
			Log.debug("pw: " + playWidth + " pr: " + playRight + " pl: " + playLeft + " pc: " + playCenter + " pb: " + playBottom);
		} else {
			Log.error("Setting resolution failed!");
		}
		return success;
	}

	/** Need at least 0.25 of width on the right. **/
	private static int calculatePlayRight(int cScreenWidth, int cPlayWidth) {
		return Math.min(cScreenWidth - (cScreenWidth - cPlayWidth)/2, (int) (cScreenWidth*0.75f));
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
		return x*scaleFactor + playLeft + GameCamera.getX();
	}
	public static float playToScreenY(float y) {
		return y*Conf.scaleFactor + Conf.playBottom + GameCamera.getY();
	}

	public static Array<String> convertModesToStrings(final Array<Graphics.DisplayMode> modes) {
		return CollectionUtils.toStream(modes)
				.map(Graphics.DisplayMode::toString)
				.collect(CollectionUtils.toArray());
	}

	public static int computeCurrentModeChoiceIndex(final Array<Graphics.DisplayMode> modes) {
		int currentModeChoiceIndex = modes.size - 1;
		for (int i = 0; i < modes.size; i++) {
			final Graphics.DisplayMode m = modes.get(i);
			if (m.width == Conf.screenWidth && m.height == Conf.screenHeight) {
				// TODO not correct due to multiple hz settings for one resolution.
				currentModeChoiceIndex = i;
			}
		}
		return currentModeChoiceIndex;
	}

	public static Array<Graphics.DisplayMode> getSortedDisplayModes() {
		final Array<Graphics.DisplayMode> modes = new Array<>(Gdx.graphics.getDisplayModes());
		modes.sort((mode1, mode2) -> {
			if (mode1.width > mode2.width) {
				return 1;
			} else if (mode2.width > mode1.width) {
				return -1;
			} else if (mode1.height > mode2.height) {
				return 1;
			} else if (mode2.height > mode1.height) {
				return -1;
			} else {
				return 0;
			}
		});
		return modes;
	}
}
