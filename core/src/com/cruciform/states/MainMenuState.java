package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;

public class MainMenuState extends State {

	public MainMenuState(Cruciform game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		game.font.draw(game.batch, "Cruciform", 100, 150);
        game.font.draw(game.batch, "Click to begin", 100, 100);
		if (Gdx.input.isTouched()) {
            game.setScreen(new GameState(game));
            dispose();
        }
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
