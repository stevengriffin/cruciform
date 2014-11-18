package com.cruciform;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
	public SpriteBatch batch;
	public BitmapFont fontSmall;
	public BitmapFont fontSmallLight;
	public BitmapFont fontLarge;
	public OrthographicCamera camera;
	public Pixmap pixmap;
	public Engine engine;
	public ExplosionFactory explosionFactory;
	public UIFactory uiFactory;
	public ShapeRenderer shapeRenderer;
	public Deferrer deferrer;
	public InputSystem inputSystem;
	public Kryo kryo;
	public Application application = null;
	public TweenManager tweenManager;

	private static final Color DARK_FONT_COLOR = new Color(5.0f/255, 0.0f, 42.0f/255, 1.0f);
	private static final Color LIGHT_FONT_COLOR = new Color(186.0f/255, 186.0f/255, 186.0f/255, 1.0f);
	
	@Override
	public void create() {
		Conf.loadSettings();
		
		// Graphics
		Gdx.input.setCursorCatched(false);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
		StateButton.init(this);
	
		// Fonts

		SmartFontGenerator fontGen = new SmartFontGenerator();
		fontGen.setReferenceScreenWidth(1920);
		FileHandle fontFile = Gdx.files.local("fonts/opera-lyrics-smooth.ttf");
		// Note: Delete the prefs file or change the fontName when changing the size.
		// Otherwise you'll get the old cached font instead.
		fontSmall = fontGen.createFont(fontFile, "opera-medium_1", 12);
		fontSmall.setColor(DARK_FONT_COLOR);
		fontSmallLight = fontGen.createFont(fontFile, "opera-medium_1", 12);
		fontSmallLight.setColor(LIGHT_FONT_COLOR);
		fontLarge = fontGen.createFont(fontFile, "opera-large", 24);
		fontLarge.setColor(DARK_FONT_COLOR);
		
		// Serialization
		kryo = new Kryo();
		// Manually shallow copy fields because setImmutable does not seem to work
		kryo.addDefaultSerializer(Splitter.class, SplitterSerializer.class);
		kryo.addDefaultSerializer(Collider.class, ColliderSerializer.class);
		kryo.addDefaultSerializer(Renderer.class, RendererSerializer.class);

		// Entity engine
		engine = new Engine();
		
		// Factories
		deferrer = new Deferrer(engine, this);
		uiFactory = new UIFactory(engine);
		explosionFactory = new ExplosionFactory(this);
		
		// Systems
		final RenderSystem renderSystem = new RenderSystem(this, batch, fontSmall);
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
		inputSystem = new InputSystem(this);
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
		engine.addSystem(new SplitterSystem(this));
		engine.addSystem(new SeekerSystem(this));
		engine.addSystem(new WaveSystem(engine));
		engine.addSystem(new HealthSystem(explosionFactory, deferrer));
	
		// Tweening
		Tween.registerAccessor(Position.class, new PositionAccessor());
		tweenManager = new TweenManager();
		
		this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		super.render();
		deferrer.clear();
	}

	public State getState() {
		return (State) this.getScreen();
	}
	
	public GameState getGameState() {
		return (GameState) this.getState();
	}
}
