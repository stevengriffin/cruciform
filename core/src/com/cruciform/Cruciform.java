package com.cruciform;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cruciform.factories.ObjectFactory;
import com.cruciform.states.MainMenuState;
import com.cruciform.systems.RenderSystem;

public class Cruciform extends Game {
	public SpriteBatch batch;
	public Texture img;
	public OrthographicCamera camera;
	public Engine engine;
	public ObjectFactory objectFactory;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		engine = new Engine();
		engine.addSystem(new RenderSystem(batch));
		objectFactory = new ObjectFactory(engine);
		img = new Texture("badlogic.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);
		this.setScreen(new MainMenuState(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		super.render();
		batch.end();
	}
}
