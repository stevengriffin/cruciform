package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;

public class Renderer extends Component {
    public static final ComponentMapper<Renderer> mapper = ComponentMapper.getFor(Renderer.class);
	public Texture image;
	public float customXOffset = 0;
	public float customYOffset = 0;
	public boolean customOffset = false;
	/**
	 * What layer of the screen the entity will be drawn in. Higher is better.
	 */
	public int priority = 0; 
}
