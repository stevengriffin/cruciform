package com.cruciform.audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	
	private static Map<Noise, Sound> map = new HashMap<Noise, Sound>();
	
	static {
		map.put(Noise.ROCKET_ZOOM, newSound("rocket_zoom_short.mp3"));
		map.put(Noise.ROCKET_EXPLOSION, newSound("rocket_explosion.mp3"));
		map.put(Noise.CRUCIFORM, newSound("cruciform.mp3"));
	}
	
	private static Sound newSound(String name) {
		return Gdx.audio.newSound(Gdx.files.internal("sounds/" + name));
	}
	
	public static Sound get(Noise noise) {
		return map.get(noise);
	}
}
