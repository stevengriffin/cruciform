package com.cruciform.levels;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.Cruciform.GameManager;
import com.cruciform.components.Position;
import com.cruciform.factories.ShipFactory;
import com.cruciform.factories.StateFactory;
import com.cruciform.states.WinState;
import com.cruciform.utils.Conf;
import com.esotericsoftware.minlog.Log;

@NonNullByDefault
public abstract class Level {
	interface Wave {
		void create();
	}
	
	@Nullable private static Level currentLevel;
	Array<Wave> waves;
	private int waveIndex = 0;
	final GameManager manager;
	final Cruciform game;
	final ShipFactory shipFactory;
	
	public static @Nullable Level getCurrentLevel() {
		return currentLevel;
	}
	
	public Level(final Cruciform game, final ShipFactory shipFactory) {
		this.manager = game.manager;
		this.game = game;
		this.shipFactory = shipFactory;
		currentLevel = this;
		waves = new Array<Wave>();
	}
	
	public Entity createAndReturnPlayer() {
		final Entity player = new Entity();
		Position.defaultForPlayer(player, Conf.fractionX(0.5f), Conf.canonicalHeight*0.1f);
		return shipFactory.createPlayer(player, true);
	}
	
	public void createNextWave() {
		if (waveIndex >= waves.size) {
			// TODO next level
			manager.deferrer.run(() -> StateFactory.setState(WinState.class, game));
			return;
		}
		Log.debug("creating wave " + waveIndex);
		waves.get(waveIndex).create();
		waveIndex++;
	}
}
