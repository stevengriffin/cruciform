package com.cruciform.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cruciform.Cruciform;
import com.cruciform.factories.StateFactory;
import com.cruciform.states.State;

public class StateButton extends TextButton {

	private static Cruciform game;
	
	public static void init(final Cruciform currentGame) {
		game = currentGame;
	}
	
	public StateButton(final String text, Class<? extends State> newState, boolean renewState) {
		super(text, UIManager.buttonStyle);
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
		super(text, UIManager.buttonStyle);
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changedAction.run();
			}
		});
	}
	
}
