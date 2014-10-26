package com.cruciform.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.states.WinState;
import com.cruciform.utils.Conf;
import com.esotericsoftware.minlog.Log;

public abstract class Level {
	interface Wave {
		void create();
	}
	
	private static Level currentLevel;
	Array<Wave> waves;
	private int waveIndex = 0;
	final Cruciform game;
	
	public static Level getCurrentLevel() {
		return currentLevel;
	}
	
	public Level(final Cruciform game) {
		this.game = game;
		currentLevel = this;
		waves = new Array<Wave>();
	}
	
	public Entity createAndReturnPlayer() {
		return game.shipFactory.createPlayer(Conf.fractionX(0.5f), Conf.canonicalHeight*0.1f);
	}
	
	public void createNextWave() {
		if (waveIndex >= waves.size) {
			// TODO next level
			game.deferrer.run(() -> StateFactory.setState(WinState.class, game));
			return;
		}
		Log.debug("creating wave " + waveIndex);
		waves.get(waveIndex).create();
		waveIndex++;
	}
}
