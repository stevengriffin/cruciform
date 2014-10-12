package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.utils.Conf;

public class RenderSystem extends IteratingSystem {

	private final Batch batch;
	private final BitmapFont font;
	
	public RenderSystem(Batch batch, BitmapFont font) {
		super(Family.getFor(Position.class, Renderer.class));
		this.batch = batch;
		this.font = font;
	}

	@Override
	public void update(float deltaTime) {
		batch.begin();
		super.update(deltaTime);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
				Conf.screenWidth*0.9f, Conf.screenHeight*0.9f);
		batch.end();
	}
	
	public void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		Rectangle rect = position.bounds.getBoundingRectangle();
		batch.draw(renderer.image, position.bounds.getX() - rect.width/2,
				position.bounds.getY() - rect.height/2);
	}
}
