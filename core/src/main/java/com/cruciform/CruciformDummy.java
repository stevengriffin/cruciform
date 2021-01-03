package com.cruciform;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.cruciform.states.GameState;
import com.cruciform.states.State;

public class CruciformDummy extends Game {

	@Override
	public void create() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public State getState() {
		return (State) this.getScreen();
	}
	
	public GameState getGameState() {
		return (GameState) this.getState();
	}
}
