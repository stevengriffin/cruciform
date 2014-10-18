package com.cruciform.states;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.input.MenuInputProcessor;

public abstract class MenuState extends State {

	private final MenuInputProcessor inputProcessor;
	
	public MenuState(final Cruciform game) {
		super(game);
		inputProcessor = new MenuInputProcessor(this);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputProcessor);
		super.show();
	}
	
	public abstract void confirm();
}