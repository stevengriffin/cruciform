package com.cruciform.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamEnemyBullet;
import com.cruciform.factories.FormationFactory;
import com.cruciform.levels.Level;

public class WaveSystem extends EntitySystem {

	private final ImmutableArray<Entity> enemies;
	private final ImmutableArray<Entity> enemyBullets;
	
	public WaveSystem(final Engine engine) {
		enemies = engine.getEntitiesFor(Family.all(TeamEnemy.class).get());
		enemyBullets = engine.getEntitiesFor(Family.all(TeamEnemyBullet.class).get());
	}

	@Override
	public void update(float deltaTime) {
		if (enemies.size() == 0 && enemyBullets.size() == 0 && FormationFactory.allTasksFinished()) {
			Level currentLevel = Level.getCurrentLevel();
			if (currentLevel != null) {
				currentLevel.createNextWave();
			}
		}
	}
	
}
