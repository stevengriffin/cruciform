package com.cruciform.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.audio.AudioManager;
import com.cruciform.components.Health;
import com.cruciform.components.team.TeamPlayer;

public class GameState extends State {

	private Entity player;
	
	public GameState(Cruciform game) {
		super(game);
		game.uiFactory.createSidePanel(true);
		game.uiFactory.createSidePanel(false);
		player = game.shipFactory.createPlayer(500, 500);
		game.shipFactory.createEnemy(800, 800);
		game.shipFactory.createEnemy(600, 800);
		game.shipFactory.createEnemy(400, 800);
		game.shipFactory.createEnemy(200, 800);
		game.shipFactory.createEnemy(700, 900);
		game.shipFactory.createEnemy(500, 900);
		game.shipFactory.createEnemy(300, 900);
		game.shipFactory.createEnemy(100, 900);
		AudioManager.initMusic(GameState.class);
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
		System.out.println("Game");
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

}
