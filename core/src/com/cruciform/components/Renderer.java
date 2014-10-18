package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.images.Picture;
import com.cruciform.utils.Priority;

public class Renderer extends AbstractComponent {
    public static final ComponentMapper<Renderer> mapper = ComponentMapper.getFor(Renderer.class);
	public TextureRegion image;
	public Picture imageName;
	public float customXOffset = 0;
	public float customYOffset = 0;
	public boolean customOffset = false;
	public Priority priority = new Priority(0); 
	public boolean shouldRender = true;
}
