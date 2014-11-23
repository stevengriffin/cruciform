package com.cruciform.states;

import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.ui.StateButton;

public class MainMenuState extends MenuState {

	public MainMenuState(Cruciform game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		manager.batch.begin();
		super.render(delta);
		// Comment out super.render and uncomment this line to test mouse input lag
		//manager.batch.draw(ImageManager.get(Picture.PLAYER_SHIP_1), Gdx.input.getX(), 1080 - Gdx.input.getY());
        manager.batch.end();
	}

	@Override
	public void show() {
		super.show();
		addButtons(
			new StateButton("Continue", GameState.class, false),
			new StateButton("New Game", GameState.class, true),
			new StateButton("Settings", SettingsState.class, false),
			new StateButton("Controls", ControlsState.class, false),
			new StateButton("Quit", ExitState.class, true));
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
