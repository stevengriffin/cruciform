package com.cruciform;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.factories.ShipFactory;
import com.cruciform.factories.UIFactory;
import com.cruciform.states.MainMenuState;
import com.cruciform.systems.AISystem;
import com.cruciform.systems.CollisionSystem;
import com.cruciform.systems.DebugRenderSystem;
import com.cruciform.systems.HealthSystem;
import com.cruciform.systems.InputSystem;
import com.cruciform.systems.LifetimeSystem;
import com.cruciform.systems.LineMoverSystem;
import com.cruciform.systems.MovementSystem;
import com.cruciform.systems.RenderSystem;
import com.cruciform.systems.ShooterSystem;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;

public class Cruciform extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;
	public Pixmap pixmap;
	public Engine engine;
	public ShipFactory shipFactory;
	public ExplosionFactory explosionFactory;
	public UIFactory uiFactory;
	public ShapeRenderer shapeRenderer;
	public Deferrer deferrer;
	
	@Override
	public void create() {
		
		// Graphics
		Gdx.graphics.setDisplayMode(Conf.screenWidth, Conf.screenHeight, true);
		Gdx.graphics.setVSync(false);
		Gdx.input.setCursorCatched(true);
		font = new BitmapFont();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);

		// Entity engine
		engine = new Engine();
		
		// Factories
		deferrer = new Deferrer(engine);
		uiFactory = new UIFactory(engine);
		explosionFactory = new ExplosionFactory(engine);
		shipFactory = new ShipFactory(engine, explosionFactory);
		
		// Systems
		engine.addSystem(new RenderSystem(batch, font));
		engine.addSystem(new DebugRenderSystem(batch, shapeRenderer));
		engine.addSystem(new InputSystem());
		engine.addSystem(new LineMoverSystem());
		engine.addSystem(new ShooterSystem());
		engine.addSystem(new AISystem());
		engine.addSystem(new MovementSystem(deferrer));
		engine.addSystem(new LifetimeSystem(deferrer));
		engine.addSystem(new CollisionSystem(engine, deferrer));
		engine.addSystem(new HealthSystem(explosionFactory, deferrer));
		
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
	
}
