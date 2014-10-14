package com.cruciform.audio;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.cruciform.states.State;
import com.cruciform.utils.Conf;

public class AudioManager {
	
	private static Map<Noise, Sound> map = new HashMap<>();
	private static List<Music> tracks = Arrays.asList(
			newMusic("track1.mp3"),
			newMusic("track2.mp3"),
			newMusic("track3.mp3"),
			newMusic("track4.mp3"),
			newMusic("track5.mp3"),
			newMusic("track6.mp3"),
			newMusic("track7.mp3"),
			newMusic("track8.mp3"),
			newMusic("track9.mp3"),
			newMusic("track10.mp3"),
			newMusic("track11.mp3"),
			newMusic("track12.mp3"),
			newMusic("track13.mp3"));
	private static int currentTrackNumber = -1;
	private static MusicListener musicListener = new MusicListener();
	
	static {
		map.put(Noise.ROCKET_ZOOM, newSound("rocket_zoom_short.mp3"));
		map.put(Noise.ROCKET_EXPLOSION, newSound("rocket_explosion.mp3"));
		map.put(Noise.CRUCIFORM, newSound("cruciform.mp3"));
		map.put(Noise.RIFLE_BULLET, newSound("rifle_bullet.mp3"));
		map.put(Noise.RIFLE_FIRE, newSound("rifle_fire_single_deep.mp3"));
		Collections.shuffle(tracks);
	}
	
	private static Sound newSound(String name) {
		return Gdx.audio.newSound(Gdx.files.internal("sounds/" + name));
	}
	
	private static Music newMusic(String name) {
		Music newMusic = Gdx.audio.newMusic(Gdx.files.internal("music/" + name));
		newMusic.setOnCompletionListener(musicListener);
		return newMusic;
	}
	
	public static Sound get(Noise noise) {
		return map.get(noise);
	}
	
	public static void initMusic(Class<? extends State> state) {
		// TODO: different for different states
		musicListener.onCompletion(null);
	}

	private static class MusicListener implements Music.OnCompletionListener {
		@Override
		public void onCompletion(Music music) {
			System.out.println("completed, track#: " + currentTrackNumber);
			if (music == null) {
				currentTrackNumber = -1;
			}
			currentTrackNumber++;
			if (currentTrackNumber >= tracks.size()) {
				currentTrackNumber = 0;
			}
			Music newMusic = tracks.get(currentTrackNumber);
			newMusic.setVolume(Conf.volume);
			newMusic.play();
		}
	}
}