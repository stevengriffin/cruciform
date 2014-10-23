package com.cruciform.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.cruciform.Cruciform;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.images.ImageManager;
import com.cruciform.images.NinePatches;
import com.cruciform.images.Picture;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Priority;
import com.cruciform.utils.Score;
import com.cruciform.weapons.Weapon;

public class RenderSystem extends EntitySystem implements EntityListener {

	private final Batch batch;
	private final BitmapFont font;
	private final Cruciform game;
	private final OrderedMap<Priority, Array<Entity>> entityMap = new OrderedMap<>(); 
	public final Family family;
	private final NinePatch patch = ImageManager.getPatch(NinePatches.BUTTON_1);
	
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
				Conf.fractionXLeftUI(0.2f), Conf.screenHeight*0.9f);
		font.draw(batch, "Score: " + Score.getScore() + " Multiplier: " + Score.getMultiplier(),
				Conf.fractionXLeftUI(0.2f), Conf.screenHeight*0.85f);
		font.draw(batch, "Credits Used: " + Score.getCreditsUsed(),
				Conf.fractionXLeftUI(0.2f), Conf.screenHeight*0.8f);
		draw(ImageManager.get(Picture.WEAPONS_PANEL), Conf.fractionXLeftUI(0.05f),
				Conf.screenHeight*0.07f, 0);
		drawPlayerWeaponInfo();
		batch.end();
	}
	
	private void drawPlayerWeaponInfo() {
		// Adjust to be 1:1 pixel on a 1920x1080 display.
		final float coolDownBarWidth = Conf.screenWidth/(1920/256.0f);
		final float coolDownBarHeight = Conf.screenHeight/(1080/128.0f);
		PlayerInput input = game.getGameState().getPlayer().getComponent(PlayerInput.class);
		Shooter shooter = game.getGameState().getPlayer().getComponent(Shooter.class);
		for (int i = 0; i < shooter.weapons.size(); i++) {
			final int index = i;
			Weapon weapon = shooter.weapons.get(i);
			input.actions.forEach((k, v) -> { 
				if (v == weapon) {
					final float x = Conf.fractionXLeftUI(0.075f);
					final float y = Conf.screenHeight*0.11f + Conf.screenHeight*0.15f*index;
					font.draw(batch, k.toString() + " --- " + weapon.name + ": " + weapon.getPercentReady(),
							x, y);
					patch.draw(batch, x, y, coolDownBarWidth*(weapon.getPercentReady() + 0.5f),
							coolDownBarHeight);
				}
			});
		}
	}

	private static int LEFT = 0;
	private static int BOTTOM = 1;
	
	private void processEntity(Entity entity, float deltaTime) {
		Position position = Position.mapper.get(entity);
		Renderer renderer = Renderer.mapper.get(entity);
		if (renderer.patch != null) {
			Rectangle rect = position.bounds.getBoundingRectangle();
			renderer.patch.draw(batch, (rect.x-2)*Conf.scaleFactor, (rect.y-2)*Conf.scaleFactor,
					(rect.width+4)*Conf.scaleFactor, (rect.height+4)*Conf.scaleFactor);
		}
		if (!renderer.shouldRender || renderer.image == null) {
			return;
		}
		if (renderer.customOffset) {
			draw(renderer.image, position.bounds.getX() + renderer.customXOffset,
				position.bounds.getY() + renderer.customYOffset, position.bounds.getRotation());
		} else {
			float[] vertices = position.bounds.getTransformedVertices();
			draw(renderer.image, vertices[LEFT],
				vertices[BOTTOM], position.bounds.getRotation());
		}
	}

	private void draw(TextureRegion region, float x, float y, float rotation) {
		batch.draw(region, x*Conf.scaleFactor, y*Conf.scaleFactor, 0, 0, 
				region.getRegionWidth()*Conf.scaleFactor,
				region.getRegionHeight()*Conf.scaleFactor,
					1, 1, rotation);
	}
	
	@Override
	public void entityAdded(Entity entity) {
		Renderer renderer = Renderer.mapper.get(entity);
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
		Renderer renderer = Renderer.mapper.get(entity);
		Array<Entity> entities = entityMap.get(renderer.priority);
		if (entities != null) {
			entities.removeValue(entity, true);
		}
	}
}
