package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.ui.StateButton;
import com.cruciform.utils.Score;

public class InsertCreditState extends MenuState {

	public InsertCreditState(Cruciform game) {
		super(game);
		addButtons(
			new StateButton("Insert Credit", 
				() -> {
					Score.useCredit();
					StateFactory.setState(GameState.class, game);
				}),
			new StateButton("New Game", GameState.class, true),
			new StateButton("Exit to Menu", MainMenuState.class, false));
	}

	@Override
	public void render(final float delta) {
		manager.batch.begin();
		super.render(delta);
        manager.batch.end();
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
	}
}
