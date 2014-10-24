package com.cruciform.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.images.ImageManager;
import com.cruciform.images.NinePatches;
import com.cruciform.states.State;

public class StateButton extends TextButton {

	private static final TextButtonStyle style = new TextButtonStyle();
	private static Cruciform game;
	
	public static void init(final Cruciform currentGame) {
		final NinePatchDrawable patchUp = 
				new NinePatchDrawable(ImageManager.getPatch(NinePatches.BUTTON_1));
		style.font = currentGame.font;
		style.up = patchUp;
		style.down = patchUp; //TODO
		game = currentGame;
	}
	
	public StateButton(final String text, Class<? extends State> newState, boolean renewState) {
		super(text, style);
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (renewState) {
					StateFactory.setAndRenewState(newState, game);
				} else {
					StateFactory.setState(newState, game);
				}
			}
		});
	}
	
	public StateButton(final String text, Runnable changedAction) {
		super(text, style);
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changedAction.run();
			}
		});
	}
	
}
