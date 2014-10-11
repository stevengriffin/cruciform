package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;

public class Renderer extends Component {
    public static final ComponentMapper<Renderer> mapper = ComponentMapper.getFor(Renderer.class);
	public Texture image;
}
