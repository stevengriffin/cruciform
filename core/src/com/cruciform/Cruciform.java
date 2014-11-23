package com.cruciform;

import org.eclipse.jdt.annotation.NonNullByDefault;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cruciform.components.Collider;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Splitter;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.factories.UIFactory;
import com.cruciform.serialization.ColliderSerializer;
import com.cruciform.serialization.RendererSerializer;
import com.cruciform.serialization.SplitterSerializer;
import com.cruciform.states.GameState;
import com.cruciform.states.MainMenuState;
import com.cruciform.states.State;
import com.cruciform.systems.AISystem;
import com.cruciform.systems.AnimatorSystem;
import com.cruciform.systems.AnimatorTriggerSystem;
import com.cruciform.systems.BlinkerSystem;
import com.cruciform.systems.ChildPositionSystem;
import com.cruciform.systems.ChildRendererSystem;
import com.cruciform.systems.CollisionSystem;
import com.cruciform.systems.DebugRenderSystem;
import com.cruciform.systems.EnemyMarkerSystem;
import com.cruciform.systems.FaderSystem;
import com.cruciform.systems.HealthSystem;
import com.cruciform.systems.InputSystem;
import com.cruciform.systems.LifetimeSystem;
import com.cruciform.systems.LineMoverSystem;
import com.cruciform.systems.MovementSystem;
import com.cruciform.systems.ParallaxSystem;
import com.cruciform.systems.ParticleEmitterSystem;
import com.cruciform.systems.RecoilSystem;
import com.cruciform.systems.RenderSystem;
import com.cruciform.systems.SeekerSystem;
import com.cruciform.systems.ShooterSystem;
import com.cruciform.systems.SplitterSystem;
import com.cruciform.systems.WaveSystem;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.ui.StateButton;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.SmartFontGenerator;
import com.esotericsoftware.kryo.Kryo;

public class Cruciform extends Game {

	public GameManager manager;
	
	@NonNullByDefault
	public static class GameManager {
		public final SpriteBatch batch;
		public final BitmapFont fontSmall;
		public final BitmapFont fontSmallLight;
		public final BitmapFont fontLarge;
		public final OrthographicCamera camera;
		public final Engine engine;
		public final ExplosionFactory explosionFactory;
		public final UIFactory uiFactory;
		public final ShapeRenderer shapeRenderer;
		public final Deferrer deferrer;
		public final InputSystem inputSystem;
		public final Kryo kryo;
		public final TweenManager tweenManager;

		private static final Color DARK_FONT_COLOR = new Color(5.0f/255, 0.0f, 42.0f/255, 1.0f);
		private static final Color LIGHT_FONT_COLOR = new Color(186.0f/255, 186.0f/255, 186.0f/255, 1.0f);

		public GameManager(final Cruciform game) {
			Conf.loadSettings();

			// Graphics
			Gdx.input.setCursorCatched(false);
			batch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
			StateButton.init(game);

			// Fonts

			SmartFontGenerator fontGen = new SmartFontGenerator();
			fontGen.setReferenceScreenWidth(1920);
			FileHandle fontFile = Gdx.files.local("fonts/opera-lyrics-smooth.ttf");
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

			// Factories
			deferrer = new Deferrer(engine, game);
			uiFactory = new UIFactory(engine);
			explosionFactory = new ExplosionFactory(this);

			// Systems
			final RenderSystem renderSystem = new RenderSystem(game, batch, fontSmall);
			engine.addSystem(renderSystem);
			engine.addEntityListener(renderSystem.family, renderSystem);
			engine.addSystem(new DebugRenderSystem(batch, shapeRenderer));
			engine.addSystem(new EnemyMarkerSystem(batch, shapeRenderer));
			final ParticleEmitterSystem particleEmitterSystem = new ParticleEmitterSystem();
			engine.addSystem(particleEmitterSystem);
			engine.addEntityListener(ParticleEmitterSystem.family, particleEmitterSystem);
			engine.addSystem(new ChildPositionSystem(deferrer));
			engine.addSystem(new ChildRendererSystem());
			engine.addSystem(new RecoilSystem());
			engine.addSystem(new MovementSystem(deferrer));
			inputSystem = new InputSystem(game);
			engine.addSystem(inputSystem);
			engine.addSystem(new LineMoverSystem());
			engine.addSystem(new ParallaxSystem());
			engine.addSystem(new ShooterSystem());
			engine.addSystem(new AISystem());
			engine.addSystem(new AnimatorSystem());
			engine.addSystem(new AnimatorTriggerSystem(deferrer));
			engine.addSystem(new BlinkerSystem());
			engine.addSystem(new FaderSystem());
			engine.addSystem(new LifetimeSystem(deferrer));
			engine.addSystem(new CollisionSystem(engine, deferrer, explosionFactory));
			engine.addSystem(new SplitterSystem(engine, kryo));
			engine.addSystem(new SeekerSystem(game));
			engine.addSystem(new WaveSystem(engine));
			engine.addSystem(new HealthSystem(explosionFactory, deferrer));

			// Tweening
			Tween.registerAccessor(Position.class, new PositionAccessor());
			tweenManager = new TweenManager();
		}
		
	}
	
	@Override
	public void create() {
		this.manager = new GameManager(this);
		this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		manager.batch.setProjectionMatrix(manager.camera.combined);
		super.render();
		manager.deferrer.clear();
	}

	public State getState() {
		return (State) this.getScreen();
	}
	
	public GameState getGameState() {
		return (GameState) this.getState();
	}
}
