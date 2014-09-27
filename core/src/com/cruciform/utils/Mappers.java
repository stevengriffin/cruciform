package com.cruciform.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.components.Collider;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.components.Velocity;

public class Mappers {
	public static final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    public static final ComponentMapper<Velocity> velocity = ComponentMapper.getFor(Velocity.class);
    public static final ComponentMapper<Collider> collider = ComponentMapper.getFor(Collider.class);
    public static final ComponentMapper<Renderer> renderer = ComponentMapper.getFor(Renderer.class);
    public static final ComponentMapper<PlayerInput> playerInput = ComponentMapper.getFor(PlayerInput.class);
    public static final ComponentMapper<Shooter> shooter = ComponentMapper.getFor(Shooter.class);
}
