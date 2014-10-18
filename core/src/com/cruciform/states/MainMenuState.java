package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;

public class MainMenuState extends MenuState {

	public MainMenuState(Cruciform game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		game.batch.begin();
		game.font.draw(game.batch, "Cruciform", 100, 500);
        game.font.draw(game.batch, "[ENTER] to continue", 100, 450);
        game.font.draw(game.batch, "[SPACE] for new game", 100, 400);
        game.font.draw(game.batch, "[ESCAPE] to exit game", 100, 350);
        game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO
		super.show();
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
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

	@Override
	public void confirm() {
		StateFactory.setState(GameState.class, game);
	}

	@Override
	public void escapeState() {
		Gdx.app.exit();
	}
	
}
