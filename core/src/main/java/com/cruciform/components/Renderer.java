package com.cruciform.components;



import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.images.ImageManager;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Priority;
import org.jetbrains.annotations.NotNull;

public class Renderer extends AbstractComponent {
    @NotNull public static final ComponentMapper<Renderer> mapper = ComponentMapper.getFor(Renderer.class);
	@NotNull public TextureRegion image = ImageManager.BLANK;
	public NinePatch patch = null;
	public float alpha = 1.0f;
	public Color tint = null;
	public float customXOffset = 0;
	public float customYOffset = 0;
	public boolean customOffset = false;
	@NotNull public Priority priority = new Priority(0);
	public boolean shouldRender = true;
	public boolean renderAtPlayCoordinates = true;
	public boolean renderAsShape = false;
	
	public Renderer(Entity entity) {
		super(entity);
	}

	public static Renderer defaultForBullet(@NotNull Entity entity, Class<? extends Team> team, @NotNull TextureRegion image) {
		Renderer renderer = new Renderer(entity);
		renderer.image = image;
		if (team == TeamEnemy.class) {
			renderer.priority = new Priority(5);
		}
		return renderer;
	}
	
	public static Renderer defaultForUI(@NotNull Entity entity, @NotNull TextureRegion image) {
		Renderer renderer = new Renderer(entity);
		renderer.customOffset = true;
		renderer.customXOffset = Conf.playLeft - Conf.canonicalPlayLeft;
		renderer.priority = new Priority(6);
		renderer.image = image;
		return renderer;
	}
}
