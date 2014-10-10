package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Shooter;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Mappers;
import com.cruciform.weapons.Weapon;

public class InputSystem extends IteratingSystem {

	private static final float ACCEL_CONSTANT = 0.1f;
	private Vector3 mousePos;
	private OrthographicCamera camera;
	
	public InputSystem(OrthographicCamera camera) {
		super(Family.getFor(PlayerInput.class, Position.class, Shooter.class));
		this.camera = camera;
		this.mousePos = new Vector3();
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		//PlayerInput playerInput = Mappers.playerInput.get(entity);
		Position position = Mappers.position.get(entity);
		Shooter shooter = Mappers.shooter.get(entity);
		Weapon weapon = shooter.weapon;
		weapon.update(deltaTime, position);
		position.rect.x += calculateMovement(Gdx.input.getDeltaX());
		position.rect.y -= calculateMovement(Gdx.input.getDeltaY());
		position.rect.x = MathUtils.clamp(position.rect.x, 0, Conf.screenWidth - position.rect.width);
		position.rect.y = MathUtils.clamp(position.rect.y, 0, Conf.screenHeight - position.rect.height);
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			weapon.fire(position);
        }
	}

	private float calculateMovement(int deltaPos) {
		float accel = 0.0f;
		if (deltaPos > 0) {
			accel = ACCEL_CONSTANT*deltaPos*deltaPos;
		} else {
			accel = -ACCEL_CONSTANT*deltaPos*deltaPos;
		}
		return deltaPos + accel;
	}
	
}
