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
		final Array<DisplayMode> modes = Conf.getSortedDisplayModes();
	    UIManager.addDropDown(table,
	    		Conf.convertModesToStrings(modes),
	    		(index) -> {
	    			proposal.screenWidth.set(modes.get(index).width);
	    			proposal.screenHeight.set(modes.get(index).height);
	    		}, Conf.computeCurrentModeChoiceIndex(modes), "Resolution", Conf.screenWidth*0.2f);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addSettingSlider(table, proposal.mouseSensitivity, false);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addSettingSlider(table, proposal.volume, true);
	    table.row().padLeft(Conf.screenWidth*0.2f);
	    UIManager.addCheckBox(table, proposal.fullScreen);
	    table.row();
	    table.add(new StateButton("Save", () -> {
	    	if (proposal.persist()) {
				StateFactory.setState(MainMenuState.class, game);
			} else {
	    		// TODO failure alert
			}
	    }),
	    new StateButton("Cancel", MainMenuState.class, false));
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