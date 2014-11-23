package com.cruciform.states;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;

@NonNullByDefault
public abstract class MenuState extends State {
	private final Stage stage;
	private final Table table;
	
	public MenuState(final Cruciform game) {
		super(game);
		Viewport viewport = new ScreenViewport();
	    stage = new Stage(viewport);
	    Gdx.input.setInputProcessor(stage);
		
		table = new Table();
	    table.setFillParent(true);
	    table.center();
	    stage.addActor(table);
		
	}

	protected void addButtons(TextButton... newButtons) {
		table.clear();
		for (int i = 0; i < newButtons.length; i++) {
			table.add(newButtons[i]);
			table.row();
		}
	}
	
	@Override
	public void render (float delta) {
	    stage.act(delta);
	    stage.draw();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		// TODO this breaks mouse button events?
		stage.getViewport().update(Conf.screenWidth, Conf.screenHeight, true);
		super.show();
	}
	
	@Override
	public void dispose() {
	    stage.dispose();
	}
}