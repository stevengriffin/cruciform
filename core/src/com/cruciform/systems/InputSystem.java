package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.input.InputAction;
import com.cruciform.input.InputCode;
import com.cruciform.utils.Conf;

public class InputSystem extends IteratingSystem implements InputProcessor {

	private static final float ACCEL_CONSTANT = 0.1f;
	private PlayerInput playerInput = null;
	private float sensitivity = 0.5f;
	
	public InputSystem() {
		super(Family.getFor(PlayerInput.class, Position.class));
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		playerInput = PlayerInput.mapper.get(entity);
		Position position = Position.mapper.get(entity);
		Rectangle rect = position.bounds.getBoundingRectangle();
		float x = position.bounds.getX();
		float y = position.bounds.getY();
		x += calculateMovement(Gdx.input.getDeltaX());
		y -= calculateMovement(Gdx.input.getDeltaY());
		x = MathUtils.clamp(x, Conf.playLeft, Conf.playRight - rect.width);
		y = MathUtils.clamp(y, 0, Conf.screenHeight - rect.height);
		position.bounds.setPosition(x, y);
		/*if (Gdx.input.isKeyJustPressed(Keys.ALT_LEFT)) {
			Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
		}*/
	}

	private float calculateMovement(int deltaPos) {
		float accel = 0.0f;
		if (deltaPos > 0) {
			accel = ACCEL_CONSTANT*deltaPos*deltaPos*sensitivity*sensitivity;
		} else {
			accel = -ACCEL_CONSTANT*deltaPos*deltaPos*sensitivity*sensitivity;
		}
		return deltaPos*sensitivity + accel;
	}

	@Override
	public boolean keyDown(int keycode) {
		return handleInput(InputCode.fromKey(keycode), true);
	}

	@Override
	public boolean keyUp(int keycode) {
		return handleInput(InputCode.fromKey(keycode), false);
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return handleInput(InputCode.fromButton(button), true);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return handleInput(InputCode.fromButton(button), false);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	private boolean handleInput(InputCode code, boolean down) {
		if (playerInput == null) {
			return false;
		}
		InputAction action = playerInput.actions.get(code);
		if (action != null) {
			action.setFiring(down);
		}
		return false;
	}
	
}
