package com.cruciform.states;

import com.badlogic.gdx.Screen;
import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.TextDrawer;
import com.esotericsoftware.minlog.Log;

public abstract class State implements Screen {
	
	public final Cruciform game;
	protected final TextDrawer drawer;
	
	
	public State(final Cruciform game) {
		this.game = game;
		this.drawer = new TextDrawer(game.batch, game.fontSmallLight);
		AudioManager.initMusic(this.getClass());
	}
	
	public void escapeState() {
		StateFactory.setState(MainMenuState.class, game);
	}
	
	public void confirm() {
		StateFactory.setState(MainMenuState.class, game);
	}
	
	/**
	 * Action when user presses new game key in this state.
	 * Does nothing by default.
	 */
	public void newGameAction() {}
	
	@Override
	public void show() {
		game.camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
		Class<? extends State> runtimeClass = this.getClass();
		Log.debug(runtimeClass.toString());
		AudioManager.resumeMusic(runtimeClass);
	}
	
	/** States do not currently support resizing. */
	@Override
	public void resize(int width, int height) {
	
	}
	
	@Override
	public void hide() {
		AudioManager.stopMusic(this.getClass());
	}
}