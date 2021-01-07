package com.cruciform.states;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cruciform.Cruciform;
import com.cruciform.utils.Conf;


public abstract class MenuState extends State {
	protected final Stage stage;
	protected final Table table;
	
	public MenuState(final Cruciform game) {
		super(game);
		final Viewport viewport = new ScreenViewport(game.manager.camera);
	    stage = new Stage(viewport);
	    Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.setDebug(true);
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
	public void resize(int width, int height) {
		if (width != Conf.screenWidth || height != Conf.screenHeight) {
			final Conf.SettingsProposal proposal = Conf.SettingsProposal.fromCurrentSettings();
			proposal.screenWidth.set(width);
			proposal.screenHeight.set(height);
			if (proposal.persist()) {
				stage.getViewport().update(Conf.screenWidth, Conf.screenHeight, true);
			}
		} else {
			stage.getViewport().update(Conf.screenWidth, Conf.screenHeight, true);
		}
	}

	@Override
	public void dispose() {
	    stage.dispose();
	}
}