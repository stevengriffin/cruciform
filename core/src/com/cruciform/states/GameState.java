package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;

public class GameState extends State {

	public GameState(Cruciform game) {
		super(game);
		game.uiFactory.createSidePanel(true);
		game.uiFactory.createSidePanel(false);
		game.shipFactory.createPlayer(500, 500);
		game.shipFactory.createEnemy(800, 800);
		game.shipFactory.createEnemy(600, 800);
		game.shipFactory.createEnemy(400, 800);
		game.shipFactory.createEnemy(200, 800);
		game.shipFactory.createEnemy(700, 900);
		game.shipFactory.createEnemy(500, 900);
		game.shipFactory.createEnemy(300, 900);
		game.shipFactory.createEnemy(100, 900);
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
