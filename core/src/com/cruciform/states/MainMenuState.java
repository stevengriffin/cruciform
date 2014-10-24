package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.ui.StateButton;

public class MainMenuState extends MenuState {

	public MainMenuState(Cruciform game) {
		super(game);
		addButtons(
			new StateButton("Continue", GameState.class, false),
			new StateButton("New Game", GameState.class, true),
			new StateButton("Quit", ExitState.class, true));
	}

	@Override
	public void render(float delta) {
		game.batch.begin();
		super.render(delta);
        game.batch.end();
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
	}
	
}
