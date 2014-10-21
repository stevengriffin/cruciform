package com.cruciform.levels;

import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.factories.FormationFactory;
import com.cruciform.utils.Conf;

public class Level1 extends Level {
	
	public Level1(final Cruciform game) {
		super(game);
		this.waves = new Array<Level.Wave>(new Level.Wave[] { 
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				2.0f, Conf.playCenter)),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
				1.0f, 3, (int) (Conf.playLeft*1.1f), (int) (Conf.screenWidth - Conf.playLeft*1.1f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER),
				1.0f, 3, (int) (Conf.playLeft*1.1f), (int) (Conf.screenWidth - Conf.playLeft*1.1f))),
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, (Conf.playLeft + Conf.playCenter)/2))
				
		});
//		FormationFactory.createSingularShip(
//				(x, y) -> game.shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
//				2.0f, Conf.playCenter);
		
	}
	
}
