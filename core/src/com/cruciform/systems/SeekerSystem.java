package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.Cruciform;
import com.cruciform.components.Position;
import com.cruciform.components.Seeker;
import com.cruciform.components.Velocity;

public class SeekerSystem extends IteratingSystem {

	private final Cruciform game;
	
	public SeekerSystem(final Cruciform game) {
		super(Family.getFor(Seeker.class, Velocity.class, Position.class));
		this.game = game;
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Seeker seeker = Seeker.mapper.get(entity);
		Position position = Position.mapper.get(entity);
		Velocity velocity = Velocity.mapper.get(entity);
		final Vector2 bulletPos = position.getCenter();
		final Vector2 playerPos = Position.mapper.get(game.getGameState().getPlayer()).getCenter();
		if (bulletPos.angle(playerPos) > 0) {
			velocity.linear.rotate(seeker.speed*deltaTime);
		} else {
			velocity.linear.rotate(-seeker.speed*deltaTime);
		}
		position.bounds.setRotation(velocity.linear.angle());
	}
}