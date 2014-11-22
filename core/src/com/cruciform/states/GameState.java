package com.cruciform.states;

import org.eclipse.jdt.annotation.NonNull;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.cruciform.Cruciform;
import com.cruciform.components.Position;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.EffectFactory;
import com.cruciform.factories.FormationFactory;
import com.cruciform.factories.ShipFactory;
import com.cruciform.factories.StateFactory;
import com.cruciform.levels.Level1;
import com.cruciform.utils.Score;

public class GameState extends State {

	@NonNull private Entity player;
	public final ShipFactory shipFactory;
	
	public GameState(Cruciform game) {
		super(game);
		Timer.instance().clear();
		Timer.instance().start();
		Score.init();
		FormationFactory.reset();
		game.engine.removeAllEntities();
		shipFactory = new ShipFactory(game.engine, game.explosionFactory, this);
		player = new Level1(game, shipFactory).createAndReturnPlayer();
		EffectFactory.createLavaOnPlayer(player, game.engine);
		EffectFactory.createBackground(game.engine);
		EffectFactory.createForeground(game.engine);
		EffectFactory.createLavaBurst(game.engine);
		// Hack to create a bunch of "looping" backgrounds.
		for (int i = 0; i < 20; i++) {
			EffectFactory.createParallaxBackground(
					game.engine, Position.mapper.getSafe(player), i);
		}
	}

	@Override
	public void render(float delta) {
		Score.update();
		game.engine.update(delta);
		game.tweenManager.update(delta);
	}

	@Override
	public void show() {
		super.show();
		//game.camera.setToOrtho(false, Conf.screenWidth, Conf.screenHeight);
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(game.inputSystem);
		Timer.instance().start();
		if (game.engine.getEntitiesFor(Family.all(TeamPlayer.class).get()).size() == 0) {
			final Position position = Position.mapper.getSafe(player);
			final Entity newPlayer = new Entity();
			newPlayer.add(position);
			player = shipFactory.createPlayer(newPlayer, false);
			game.deferrer.shieldAndBlink(player, 3.0f);
		}
//		game.uiFactory.createSidePanel(true);
//		game.uiFactory.createSidePanel(false);
//		game.uiFactory.createBottomPanel();
		game.uiFactory.createUIBackground();
	}

	@Override
	public void hide() {
		super.hide();
		Timer.instance().stop();
		Gdx.input.setCursorCatched(false);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void escapeState() {
		StateFactory.setState(MainMenuState.class, this.game);
	}
	
	public Entity getPlayer() {
		return player;
	}
}
