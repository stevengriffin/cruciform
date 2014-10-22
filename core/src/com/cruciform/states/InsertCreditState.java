package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Score;

public class InsertCreditState extends MenuState {

	public InsertCreditState(Cruciform game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		game.batch.begin();
		// TODO Make buttons instead
		final String insertCredit = "Insert Credit [ENTER]";
		final String exitGame = "Exit To Menu [ESCAPE]";
		final String newGame = "New Game [SPACE]";
        drawer.drawCentered(insertCredit, Conf.playCenter, Conf.screenHeight/2);
		drawer.drawCentered(exitGame, Conf.playCenter, Conf.fractionY(0.4f));
		drawer.drawCentered(newGame, Conf.playCenter, Conf.fractionY(0.3f));
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
		Score.useCredit();
		StateFactory.setState(GameState.class, game);
	}
}
