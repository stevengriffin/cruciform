package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.cruciform.Cruciform;

public abstract class State implements Screen {
	
	public final Cruciform game;
	
	public State(final Cruciform game) {
		this.game = game;
	}
	
	public void exitGame() {
		Gdx.app.exit();
	}
}