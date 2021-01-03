package com.cruciform.states;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.ui.StateButton;
import com.cruciform.ui.UIManager;
import com.cruciform.utils.CollectionUtils;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Conf.SettingsProposal;
import org.jetbrains.annotations.NotNull;

public class SettingsState extends MenuState {
	
	public SettingsState(@NotNull final Cruciform game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
	    table.clear();
	    
	    table.row().padLeft(Conf.screenWidth*0.2f);
		final SettingsProposal proposal = SettingsProposal.fromCurrentSettings();
		final Array<DisplayMode> modes = getSortedDisplayModes();
	    UIManager.addDropDown(table,
	    		convertModesToStrings(modes),
	    		(index) -> {
	    			proposal.screenWidth.set(modes.get(index).width);
	    			proposal.screenHeight.set(modes.get(index).height);
	    		}, computeCurrentModeChoiceIndex(modes), "Resolution", Conf.screenWidth*0.2f);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addSettingSlider(table, proposal.mouseSensitivity, false);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addSettingSlider(table, proposal.volume, true);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addCheckBox(table, proposal.fullScreen);
	    table.row();
	    table.add(new StateButton("Save", () -> {
	    	proposal.persist();
	    	StateFactory.setState(MainMenuState.class, game);
	    }),
	    new StateButton("Cancel", MainMenuState.class, false));
	}

	private static Array<String> convertModesToStrings(final Array<DisplayMode> modes) {
		return CollectionUtils.toStream(modes)
	    		.map(DisplayMode::toString)
	    		.collect(CollectionUtils.toArray());
	}

	private static int computeCurrentModeChoiceIndex(final Array<DisplayMode> modes) {
		int currentModeChoiceIndex = 0;
	    for (int i = 0; i < modes.size; i++) {
	    	final DisplayMode m = modes.get(i);
	    	if (m.width == Conf.screenWidth && m.height == Conf.screenHeight) {
	    		// TODO not correct due to multiple hz settings for one resolution.
	    		currentModeChoiceIndex = i;
	    	}
	    }
		return currentModeChoiceIndex;
	}

	private static Array<DisplayMode> getSortedDisplayModes() {
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
		return modes;
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