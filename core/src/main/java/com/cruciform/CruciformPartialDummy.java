package com.cruciform;


import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogicmods.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.Collider;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Splitter;
import com.cruciform.serialization.ColliderSerializer;
import com.cruciform.serialization.RendererSerializer;
import com.cruciform.serialization.SplitterSerializer;
import com.cruciform.states.GameState;
import com.cruciform.states.State;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.tweening.VectorAccessor;
import com.cruciform.utils.Conf;
import com.cruciform.utils.SmartFontGenerator;
import com.esotericsoftware.kryo.Kryo;

public class CruciformPartialDummy extends Game {

	public GameManager manager;
	
	public static class GameManager {
		public final SpriteBatch batch;
		public final BitmapFont fontSmall;
		public final BitmapFont fontSmallLight;
		public final BitmapFont fontLarge;
		public final OrthographicCamera camera;
		public final Engine engine;
		public final ShapeRenderer shapeRenderer;
		public final Kryo kryo;
		public final TweenManager tweenManager;

		private static final Color DARK_FONT_COLOR = new Color(5.0f/255, 0.0f, 42.0f/255, 1.0f);
		private static final Color LIGHT_FONT_COLOR = new Color(186.0f/255, 186.0f/255, 186.0f/255, 1.0f);

		public GameManager(final CruciformPartialDummy game) {
			Conf.loadSettings();

			// Graphics
			Gdx.input.setCursorCatched(false);
			batch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);

			// Fonts
			final SmartFontGenerator fontGen = new SmartFontGenerator();
			fontGen.setReferenceScreenWidth(1920);
			final FileHandle fontFile = Gdx.files.local("fonts/opera-lyrics-smooth.ttf");
			if (fontFile == null) {
				// The font file was deleted. Use the default LibGdx font instead.
				fontSmall = new BitmapFont();
				fontSmallLight = new BitmapFont();
				fontLarge = new BitmapFont();
			} else {
				// Note: Delete the prefs file or change the fontName when changing the size.
				// Otherwise you'll get the old cached font instead.
				fontSmall = fontGen.createFont(fontFile, "opera-medium_1", 12);
				fontSmall.setColor(DARK_FONT_COLOR);
				fontSmallLight = fontGen.createFont(fontFile, "opera-medium_1", 12);
				fontSmallLight.setColor(LIGHT_FONT_COLOR);
				fontLarge = fontGen.createFont(fontFile, "opera-large", 24);
				fontLarge.setColor(DARK_FONT_COLOR);
			}

			// Serialization
			kryo = new Kryo();
			// Manually shallow copy fields because setImmutable does not seem to work
			kryo.addDefaultSerializer(Splitter.class, SplitterSerializer.class);
			kryo.addDefaultSerializer(Collider.class, ColliderSerializer.class);
			kryo.addDefaultSerializer(Renderer.class, RendererSerializer.class);

			// Entity engine
			engine = new Engine();

			// Tweening
			Tween.registerAccessor(Position.class, new PositionAccessor());
			Tween.registerAccessor(Vector2.class, new VectorAccessor());
			Tween.setWaypointsLimit(30);
			tweenManager = new TweenManager();
		}
		
	}
	
	@Override
	public void create() {
		this.manager = new GameManager(this);
		//this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		manager.batch.setProjectionMatrix(manager.camera.combined);
		super.render();
		//manager.deferrer.clear();
	}

	public State getState() {
		return (State) this.getScreen();
	}
	
	public GameState getGameState() {
		return (GameState) this.getState();
	}
}
