package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Position extends Component {
	public Rectangle rect = new Rectangle(0, 0, 0, 0);
	public Vector2 rotation = new Vector2(0, 0);
}