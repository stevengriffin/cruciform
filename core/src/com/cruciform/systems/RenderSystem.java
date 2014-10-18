package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.cruciform.Cruciform;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Priority;

public class RenderSystem extends EntitySystem implements EntityListener {

	private final Batch batch;
	private final BitmapFont font;
	private final Cruciform game;
	private final OrderedMap<Priority, Array<Entity>> entityMap = new OrderedMap<>(); 
	public final Family family;
	
	public RenderSystem(final Cruciform game, final Batch batch, final BitmapFont font) {
		this.family = Family.getFor(Position.class, Renderer.class);
		this.batch = batch;
		this.font = font;
		this.game = game;
	}

	@Override
	public void update(float deltaTime) {
		batch.begin();
		for(Array<Entity> entities : entityMap.values()) {
			for(int i = 0; i < entities.size; i++) {
				processEntity(entities.get(i), deltaTime);
			}
		}
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
				Conf.screenWidth*0.9f, Conf.screenHeight*0.9f);
		font.draw(batch, "Java heap: " + game.application.getJavaHeap(),
				Conf.screenWidth*0.8f, Conf.screenHeight*0.8f);
		font.draw(batch, "Native heap: " + game.application.getNativeHeap(),
				Conf.screenWidth*0.8f, Conf.screenHeight*0.7f);
		batch.end();
	}

	private void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		// TODO Switch to family-based when Ashley is updated
		if (position == null || renderer == null || !renderer.shouldRender) {
			return;
		}
		Rectangle rect = position.bounds.getBoundingRectangle();
		if (renderer.customOffset) {
			draw(renderer.image, position.bounds.getX() + renderer.customXOffset,
				position.bounds.getY() + renderer.customYOffset, position.bounds.getRotation());
		} else {
			float[] vertices = position.bounds.getTransformedVertices();
			draw(renderer.image, vertices[0],
				vertices[1], position.bounds.getRotation());
		}
	}

	private void draw(TextureRegion region, float x, float y, float rotation) {
		batch.draw(region, x, y, 0, 0, region.getRegionWidth(), region.getRegionHeight(),
					1, 1, rotation);
	}
	
	@Override
	public void entityAdded(Entity entity) {
		// TODO Switch to family-based when Ashley is updated
		// TODO This version may be bugged if Renderers are removed from an Entity without destroying it.
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		if (position == null || renderer == null) {
			return;
		}
		Array<Entity> entities = entityMap.get(renderer.priority);
		if (entities == null) {
			entities = new Array<Entity>();
			entities.ordered = false;
			entityMap.put(renderer.priority, entities);
			entityMap.orderedKeys().sort();
		}
		entities.add(entity);
	}

	@Override
	public void entityRemoved(Entity entity) {
		// TODO Switch to family-based when Ashley is updated
		// TODO This version may be bugged if Renderers are removed from an Entity without destroying it.
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		if (position == null || renderer == null) {
			return;
		}
		Array<Entity> entities = entityMap.get(renderer.priority);
		if (entities != null) {
			entities.removeValue(entity, true);
		}
	}
}
