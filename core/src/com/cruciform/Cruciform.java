package com.cruciform;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.factories.ShipFactory;
import com.cruciform.states.MainMenuState;
import com.cruciform.systems.CollisionSystem;
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
	public ShapeRenderer shapeRenderer;
	public Deferrer deferrer;
	
	@Override
	public void create() {
		Gdx.graphics.setDisplayMode(Conf.screenWidth, Conf.screenHeight, true);
		Gdx.graphics.setVSync(false);
		Gdx.input.setCursorCatched(true);
		font = new BitmapFont();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
		engine = new Engine();
		deferrer = new Deferrer(engine);
		engine.addSystem(new RenderSystem(batch, shapeRenderer));
		engine.addSystem(new InputSystem());
		engine.addSystem(new LineMoverSystem());
		engine.addSystem(new ShooterSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new LifetimeSystem(deferrer));
		explosionFactory = new ExplosionFactory(engine);
		engine.addSystem(new CollisionSystem(engine, explosionFactory, deferrer));
		shipFactory = new ShipFactory(engine, explosionFactory);
		this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		shapeRenderer.begin(ShapeType.Line);
		super.render();
		
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
				Conf.screenWidth*0.9f, Conf.screenHeight*0.9f);
		shapeRenderer.end();
		batch.end();
		deferrer.clear();
	}
	
}
