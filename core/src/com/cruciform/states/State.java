package com.cruciform.states;

import com.badlogic.gdx.Screen;
import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;
import com.cruciform.factories.StateFactory;

public abstract class State implements Screen {
	
	public final Cruciform game;
	
	public State(final Cruciform game) {
		this.game = game;
	}
	
	public void escapeState() {
		StateFactory.setState(MainMenuState.class, game);
	}
	
	@Override
	public void show() {
		Class<? extends State> runtimeClass = this.getClass();
		System.out.println(runtimeClass.toString());
		AudioManager.initMusic(runtimeClass);
	}
	
	@Override
	public void hide() {
		AudioManager.stopMusic(this.getClass());
	}
}