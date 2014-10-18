package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;

public class InsertCreditState extends MenuState {

	public InsertCreditState(Cruciform game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		game.batch.begin();
		// TODO Make buttons instead
		String insertCredit = "Insert Credit [ENTER]";
		String exitGame = "Exit To Menu [ESCAPE]";
        game.font.draw(game.batch, insertCredit, 
        		100, 100);
        game.font.draw(game.batch, exitGame, 100, 50);
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
}
