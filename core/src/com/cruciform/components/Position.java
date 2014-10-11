package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Polygon;

public class Position extends Component {
    public static final ComponentMapper<Position> mapper = ComponentMapper.getFor(Position.class);
	public Polygon bounds;
}