package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
    public static final ComponentMapper<Velocity> mapper = ComponentMapper.getFor(Velocity.class);
	public Vector2 linear = new Vector2(0, 0);
	public Vector2 rotational = new Vector2(0, 0);
}
