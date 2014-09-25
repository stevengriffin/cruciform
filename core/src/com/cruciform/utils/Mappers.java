package com.cruciform.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.components.Collider;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Velocity;

public class Mappers {
	public static final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    public static final ComponentMapper<Velocity> velocity = ComponentMapper.getFor(Velocity.class);
    public static final ComponentMapper<Collider> collider = ComponentMapper.getFor(Collider.class);
    public static final ComponentMapper<Renderer> renderer = ComponentMapper.getFor(Renderer.class);
}
