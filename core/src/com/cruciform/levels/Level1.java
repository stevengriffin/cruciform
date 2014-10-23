package com.cruciform.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.factories.FormationFactory;
import com.cruciform.factories.PathFactory;
import com.cruciform.utils.Conf;

public class Level1 extends Level {
	
	public Level1(final Cruciform game) {
		super(game);
		final PathFactory pathFactory = new PathFactory(game);
		this.waves = new Array<Level.Wave>(new Level.Wave[] { 
				
				() -> (FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.playCenter)),
				
				() -> (FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = game.shipFactory.createEnemy(x, y, EnemyTypes.PENTAGRAM);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.playCenter)),
				
				() -> (FormationFactory.createBroadFormation(
                (x, y, i) -> {
                	Entity entity = game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, i % 2 == 1);
                	return entity;
                },
				1.0f, 4, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				2.0f, Conf.playCenter)),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, (Conf.playLeft + Conf.playCenter)/2)),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f)))
				
		});
//		FormationFactory.createSingularShip(
//				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
//				2.0f, Conf.playCenter);
		
	}
	
}
