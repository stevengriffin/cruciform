package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.images.Picture;
import com.cruciform.utils.Priority;

public class Renderer extends AbstractComponent {
    public static final ComponentMapper<Renderer> mapper = ComponentMapper.getFor(Renderer.class);
	public TextureRegion image = null;
	public NinePatch patch = null;
	public Picture imageName;
	public float customXOffset = 0;
	public float customYOffset = 0;
	public boolean customOffset = false;
	public Priority priority = new Priority(0); 
	public boolean shouldRender = true;
	public boolean renderAtPlayCoordinates = true;
	public boolean renderAsShape = false;
	
	public Renderer(Entity entity) {
		super(entity);
	}

	public static Renderer defaultForBullet(Entity entity, Class<? extends Team> team, TextureRegion image) {
		Renderer renderer = new Renderer(entity);
		renderer.image = image;
		if (team == TeamEnemy.class) {
			renderer.priority = new Priority(5);
		}
		return renderer;
	}
	
	public static Renderer defaultForUI(Entity entity, TextureRegion image) {
		Renderer renderer = new Renderer(entity);
		renderer.customOffset = true;
		renderer.priority = new Priority(6);
		renderer.image = image;
		return renderer;
	}
}
