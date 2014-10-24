package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

public class SettingsState extends State {
	private final Stage stage;
	private final Table table;
	
	public SettingsState(final Cruciform game) {
		super(game);
	    stage = new Stage(new StretchViewport(Conf.canonicalWidth, Conf.canonicalHeight));
	    Gdx.input.setInputProcessor(stage);
		
		table = new Table();
	    table.setFillParent(true);
	    table.center();
	    stage.addActor(table);
		
	}

	public void render (float delta) {
	    stage.act(delta);
	    stage.draw();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	    stage.getViewport().update(Conf.screenWidth, Conf.screenHeight, true);
		super.show();
	}
	
	public void dispose() {
	    stage.dispose();
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}