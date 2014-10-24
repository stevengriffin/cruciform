package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

public abstract class MenuState extends State {
	private final Stage stage;
	private final Table table;
	
	public MenuState(final Cruciform game) {
		super(game);
	    stage = new Stage(new ScreenViewport());
	    		//new StretchViewport(Conf.canonicalWidth, Conf.canonicalHeight));
	    Gdx.input.setInputProcessor(stage);
		
		table = new Table();
	    table.setFillParent(true);
	    table.center();
	    stage.addActor(table);
		
	}

	protected void addButtons(TextButton...newButtons) {
		for (int i = 0; i < newButtons.length; i++) {
			table.add(newButtons[i]);
			table.row();
		}
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
	
	public abstract void confirm();
	
	public void dispose() {
	    stage.dispose();
	}
}