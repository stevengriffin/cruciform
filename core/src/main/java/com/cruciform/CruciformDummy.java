package com.cruciform;


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
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.Collider;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Splitter;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.factories.PathFactory;
import com.cruciform.factories.UIFactory;
import com.cruciform.serialization.ColliderSerializer;
import com.cruciform.serialization.RendererSerializer;
import com.cruciform.serialization.SplitterSerializer;
import com.cruciform.states.GameState;
import com.cruciform.states.MainMenuState;
import com.cruciform.states.State;
import com.cruciform.systems.*;
import com.cruciform.tweening.PositionAccessor;
import com.cruciform.tweening.VectorAccessor;
import com.cruciform.ui.StateButton;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Deferrer;
import com.cruciform.utils.SmartFontGenerator;
import com.esotericsoftware.kryo.Kryo;

public class CruciformDummy extends Game {

	@Override
	public void create() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public State getState() {
		return (State) this.getScreen();
	}
	
	public GameState getGameState() {
		return (GameState) this.getState();
	}
}
