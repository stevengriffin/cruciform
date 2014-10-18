package com.cruciform.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.components.Health;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.StateFactory;
import com.cruciform.utils.Conf;

public class GameState extends State {

	private Entity player;
	
	public GameState(Cruciform game) {
		super(game);
		game.engine.removeAllEntities();
		game.uiFactory.createSidePanel(true);
		game.uiFactory.createSidePanel(false);
		player = game.shipFactory.createPlayer(500, 500);
		game.shipFactory.createEnemy(Conf.playLeft + 25, 800);
		game.shipFactory.createEnemy(Conf.playLeft + 225, 800);
		game.shipFactory.createEnemy(Conf.playLeft + 425, 800);
		game.shipFactory.createEnemy(Conf.playLeft + 625, 800);
		game.shipFactory.createEnemy(Conf.playLeft + 825, 800);
		game.shipFactory.createEnemy(Conf.playLeft + 125, 900);
		game.shipFactory.createEnemy(Conf.playLeft + 325, 900);
		game.shipFactory.createEnemy(Conf.playLeft + 525, 900);
		game.shipFactory.createEnemy(Conf.playLeft + 725, 900);
		game.shipFactory.createEnemy(Conf.playLeft + 25, 1000);
		game.shipFactory.createEnemy(Conf.playLeft + 225, 1000);
		game.shipFactory.createEnemy(Conf.playLeft + 425, 1000);
		game.shipFactory.createEnemy(Conf.playLeft + 625, 1000);
		game.shipFactory.createEnemy(Conf.playLeft + 825, 1000);
		game.shipFactory.createEnemy(Conf.playLeft + 125, 1200);
		game.shipFactory.createEnemy(Conf.playLeft + 325, 1200);
		game.shipFactory.createEnemy(Conf.playLeft + 525, 1200);
		game.shipFactory.createEnemy(Conf.playLeft + 725, 1200);
	}

	@Override
	public void render(float delta) {
		game.engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(game.inputSystem);
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
}
