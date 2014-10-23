package com.cruciform.states;

import com.badlogic.gdx.Screen;
import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.TextDrawer;

public abstract class State implements Screen {
	
	public final Cruciform game;
	protected final TextDrawer drawer;
	
	
	public State(final Cruciform game) {
		this.game = game;
		this.drawer = new TextDrawer(game.batch, game.font);
		AudioManager.initMusic(this.getClass());
	}
	
	public void escapeState() {
		StateFactory.setState(MainMenuState.class, game);
	}
	
	@Override
	public void show() {
		Class<? extends State> runtimeClass = this.getClass();
		System.out.println(runtimeClass.toString());
		AudioManager.resumeMusic(runtimeClass);
	}
	
	@Override
	public void hide() {
		AudioManager.stopMusic(this.getClass());
	}
}