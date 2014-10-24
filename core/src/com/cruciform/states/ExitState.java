package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

public class ExitState extends State {

	public ExitState(Cruciform game) {
		super(game);
		Conf.saveSettings();
		Gdx.app.exit();
	}

	@Override
	public void render(float delta) {
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
