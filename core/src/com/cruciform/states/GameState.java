package com.cruciform.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.cruciform.Cruciform;
import com.cruciform.components.Health;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.FormationFactory;
import com.cruciform.factories.StateFactory;
import com.cruciform.levels.Level1;
import com.cruciform.utils.Score;

public class GameState extends State {

	private Entity player;
	
	public GameState(Cruciform game) {
		super(game);
		Timer.instance().clear();
		Timer.instance().start();
		Score.init();
		FormationFactory.reset();
		game.engine.removeAllEntities();
		game.uiFactory.createSidePanel(true);
		game.uiFactory.createSidePanel(false);
		game.uiFactory.createBottomPanel();
		player = new Level1(game).createAndReturnPlayer();
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
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(game.inputSystem);
		Timer.instance().start();
		if (game.engine.getEntitiesFor(Family.getFor(TeamPlayer.class)).size() == 0) {
			game.engine.addEntity(player);
			Health health = Health.mapper.get(player);
			health.currentHealth = health.maxHealth;
			game.deferrer.shieldAndBlink(player, 3.0f);
		}
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
