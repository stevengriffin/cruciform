package com.cruciform;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cruciform.factories.ObjectFactory;
import com.cruciform.states.MainMenuState;
import com.cruciform.systems.InputSystem;
import com.cruciform.systems.RenderSystem;
import com.cruciform.utils.Conf;

public class Cruciform extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;
	public Pixmap pixmap;
	public Engine engine;
	public ObjectFactory objectFactory;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Conf.screenWidth, Conf.screenHeight, true);
		Gdx.graphics.setVSync(false);
		//pixmap = new Pixmap(Gdx.files.local("player_ship2.png"));
		//Gdx.input.setCursorImage(pixmap, 0, 0);
		Gdx.input.setCursorCatched(true);
		font = new BitmapFont();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
		engine = new Engine();
		engine.addSystem(new RenderSystem(batch));
		engine.addSystem(new InputSystem(camera));
		objectFactory = new ObjectFactory(engine);
		this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		super.render();
		
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
				Conf.screenWidth*0.9f, Conf.screenHeight*0.9f);
		batch.end();
	}
}
