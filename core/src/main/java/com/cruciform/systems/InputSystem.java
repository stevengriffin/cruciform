package com.cruciform.systems;

import com.badlogicmods.ashley.core.Entity;
import com.badlogicmods.ashley.core.Family;
import com.badlogicmods.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.Cruciform;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.input.InputAction;
import com.cruciform.input.InputCode;
import com.cruciform.utils.Conf;

public class InputSystem extends IteratingSystem implements InputProcessor {

	private static final float ACCEL_CONSTANT = 0.0f;
	private static final float SENSITIVITY_CONSTANT = 240.0f;
	private PlayerInput playerInput = new PlayerInput();
	private float sensitivity;
	private float keysSpeed = 480.0f;
	private final Cruciform game;
	
	public InputSystem(final Cruciform game) {
		super(Family.all(PlayerInput.class, Position.class, Renderer.class).get());
		this.game = game;
	}

	@Override
	public void processEntity(final Entity entity, final float deltaTime) {
		sensitivity = SENSITIVITY_CONSTANT*Conf.mouseSensitivity;
		float focusMultiplier; 
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
			focusMultiplier = 0.5f;
		} else {
			focusMultiplier = 1.0f;
		}
		playerInput = PlayerInput.mapper.getSafe(entity);
		final Renderer renderer = Renderer.mapper.getSafe(entity);
		final Position position = Position.mapper.getSafe(entity);
		final Rectangle rect = position.bounds.getBoundingRectangle();
		float x = position.bounds.getX();
		float y = position.bounds.getY();
		final float deltaX = Gdx.input.getDeltaX()*deltaTime;
		final float deltaY = Gdx.input.getDeltaY()*deltaTime;
		//System.out.println("dx: " + deltaX + " dy: " + deltaY);
		x += focusMultiplier*calculateMovement(deltaX);
		y -= focusMultiplier*1.3f*calculateMovement(deltaY);
		// TODO allow configurable
		if (Gdx.input.isKeyPressed(Keys.A)) {
			x -= keysSpeed*deltaTime*focusMultiplier;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			x += keysSpeed*deltaTime*focusMultiplier;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			y += keysSpeed*deltaTime*focusMultiplier;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			y -= keysSpeed*deltaTime*focusMultiplier;
		}
		
		x = MathUtils.clamp(x, 0, Conf.canonicalPlayWidth - rect.width);
		y = MathUtils.clamp(y, 0 - renderer.customYOffset, Conf.canonicalPlayHeight + renderer.customYOffset);
		position.bounds.setPosition(x, y);
	}

	private float calculateMovement(final float deltaPos) {
		final float accel = deltaPos > 0 ?
			ACCEL_CONSTANT*deltaPos*deltaPos*sensitivity*sensitivity
		    : -ACCEL_CONSTANT*deltaPos*deltaPos*sensitivity*sensitivity;
		return deltaPos*sensitivity + accel;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO allow configurable
		if (keycode == Keys.ESCAPE) {
			game.getState().escapeState();
		}
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
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	private boolean handleInput(InputCode code, boolean down) {
		InputAction action = playerInput.actions.get(code);
		if (action != null) {
			action.setFiring(down);
		}
		return false;
	}
	
}
