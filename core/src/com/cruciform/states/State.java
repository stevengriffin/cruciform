package com.cruciform.states;

import org.eclipse.jdt.annotation.NonNull;

import com.badlogic.gdx.Screen;
import com.cruciform.Cruciform;
import com.cruciform.Cruciform.GameManager;
import com.cruciform.audio.AudioManager;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.TextDrawer;
import com.esotericsoftware.minlog.Log;

public abstract class State implements Screen {
	
	@NonNull public final Cruciform game;
	@NonNull public final GameManager manager;
	@NonNull protected final TextDrawer drawer;
	
	
	public State(@NonNull final Cruciform game) {
		this.game = game;
		this.manager = game.manager;
		this.drawer = new TextDrawer(manager.batch, manager.fontSmallLight);
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
		manager.camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
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