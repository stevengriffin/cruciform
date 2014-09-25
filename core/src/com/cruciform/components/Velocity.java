package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
	public Vector2 linear = new Vector2(0, 0);
	public Vector2 rotational = new Vector2(0, 0);
}
