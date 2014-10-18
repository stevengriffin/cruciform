package com.cruciform.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.cruciform.factories.StateFactory;
import com.cruciform.states.GameState;
import com.cruciform.states.MenuState;

public class MenuInputProcessor implements InputProcessor {

	private MenuState state;
	
	public MenuInputProcessor(MenuState state) {
		this.state = state;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO allow configurable
		if (keycode == Keys.ENTER) {
			state.confirm();
		} else if (keycode == Keys.ESCAPE) {
			state.escapeState();
		} else if (keycode == Keys.SPACE) {
			StateFactory.setAndRenewState(GameState.class, state.game);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
