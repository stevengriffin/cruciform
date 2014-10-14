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
		game.font.draw(game.batch, "Cruciform", 100, 150);
        game.font.draw(game.batch, "Insert Credit [ENTER]", 100, 100);
        game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		super.show();
		System.out.println("InsertCredit");
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

	@Override
	public void confirm() {
		StateFactory.setState(GameState.class, game);
	}
	
}
