package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Score;

public class WinState extends MenuState {
	
	public WinState(Cruciform game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		game.batch.begin();
		super.render(delta);
        
        drawer.drawCentered("You Win!", Conf.screenCenterX, Conf.screenHeight*0.7f);
        drawer.drawCentered("Score: " + Score.getScore(), Conf.screenCenterX, Conf.screenHeight*0.6f);
        drawer.drawCentered("Credits Used: " + Score.getCreditsUsed(), Conf.screenCenterX, Conf.screenHeight*0.5f);
		// TODO Make buttons instead
		final String exitGame = "Exit To Menu [ESCAPE]";
		final String newGame = "New Game [SPACE] or [ENTER]";
		drawer.drawCentered(exitGame, Conf.screenCenterX, Conf.screenHeight*0.4f);
		drawer.drawCentered(newGame, Conf.screenCenterX, Conf.screenHeight*0.3f);
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
		StateFactory.setAndRenewState(GameState.class, game);
	}
}
