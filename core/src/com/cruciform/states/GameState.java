package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;

public class GameState extends State {

	public GameState(Cruciform game) {
		super(game);
		game.shipFactory.createPlayer(500, 500);
		AudioManager.initMusic(GameState.class);
	}

	@Override
	public void render(float delta) {
		game.engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
