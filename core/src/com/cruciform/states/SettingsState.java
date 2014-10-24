package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.ui.StateButton;
import com.cruciform.ui.UIManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Conf.SettingsProposal;

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
	    table.setWidth(Conf.canonicalWidth);
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
	    table.clear();
	    
		final SettingsProposal proposal = SettingsProposal.fromCurrentSettings();
	    
		final Array<DisplayMode> modes = new Array<>(Gdx.graphics.getDisplayModes());
	    modes.sort((mode1, mode2) -> {
	    	if (mode1.width > mode2.width) {
	    		return 1;
	    	} else if (mode2.width > mode1.width) {
	    		return -1;
	    	} else if (mode1.height > mode2.height) {
	    		return 1;
	    	} else if (mode2.height > mode1.height) {
	    		return -1;
	    	} else {
	    		return 0;
	    	}
	    });
	    final Array<String> modeChoices = new Array<>();
	    int currentModeChoiceIndex = 0;
	    for (int i = 0; i < modes.size; i++) {
	    	if (modes.get(i).width == Conf.screenWidth && modes.get(i).height == Conf.screenHeight) {
	    		currentModeChoiceIndex = i;
	    	}
	    	modeChoices.add(modes.get(i).width + " x " + modes.get(i).height);
	    }
	    UIManager.addDropDown(table, modeChoices, (index) -> {
	    	proposal.screenHeight.set(modes.get(index).height);
	    	proposal.screenWidth.set(modes.get(index).width);
	    }, currentModeChoiceIndex, "Resolution");
	    table.row().padLeft(200);
	    UIManager.addSettingSlider(table, proposal.mouseSensitivity, false);
	    table.row().padLeft(200);
	    UIManager.addSettingSlider(table, proposal.volume, true);
	    table.row().padLeft(200);
	    UIManager.addCheckBox(table, proposal.fullScreen);
	    table.row();
	    table.add(new StateButton("Save", () -> {
	    	proposal.persist();
	    	StateFactory.setState(MainMenuState.class, game);
	    }),
	    new StateButton("Cancel", MainMenuState.class, false));
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