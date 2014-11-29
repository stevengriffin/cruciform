package com.cruciform.states;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.input.StateInputProcessor;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Score;

@NonNullByDefault
public class WinState extends State {
	protected final StateInputProcessor processor;
	
	public WinState(Cruciform game) {
		super(game);
		processor = new StateInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		manager.batch.begin();
		//super.render(delta);
        
        drawer.drawCentered("You Win!", Conf.screenCenterX, Conf.screenHeight*0.7f);
        drawer.drawCentered("Score: " + Score.getScore(), Conf.screenCenterX, Conf.screenHeight*0.6f);
        drawer.drawCentered("Graze: " + Score.getGraze(), Conf.screenCenterX, Conf.screenHeight*0.55f);
        drawer.drawCentered("Credits Used: " + Score.getCreditsUsed(), Conf.screenCenterX, Conf.screenHeight*0.5f);
		// TODO Make buttons instead
		final String exitmanager = "Exit To Main Menu [ESCAPE]";
		drawer.drawCentered(exitmanager, Conf.screenCenterX, Conf.screenHeight*0.4f);
		final String newmanager = "New Game [SPACE]";
		drawer.drawCentered(newmanager, Conf.screenCenterX, Conf.screenHeight*0.3f);
        manager.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(processor);
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
	public void newGameAction() {
		StateFactory.setAndRenewState(GameState.class, game);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
