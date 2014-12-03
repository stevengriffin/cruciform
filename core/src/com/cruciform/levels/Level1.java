package com.cruciform.levels;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.factories.FormationFactory;
import com.cruciform.factories.PathFactory;
import com.cruciform.factories.ShipFactory;
import com.cruciform.utils.Conf;

@NonNullByDefault
public class Level1 extends Level {
	
	public Level1(final Cruciform game, final ShipFactory shipFactory) {
		super(game, shipFactory);
		final PathFactory pathFactory = new PathFactory(game.manager);
		this.waves = new Array<Level.Wave>(new Level.@NonNull Wave[] { 
				
				() -> (FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f))),
				
				() -> (FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.PENTAGRAM);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f))),
				
				() -> (FormationFactory.createBroadFormation(
                (x, y, i) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, i % 2 == 1);
                	return entity;
                },
				1.0f, 4, Conf.fractionX(0.4f), Conf.fractionX(0.6f))),
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				2.0f, Conf.fractionX(0.5f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				() -> (FormationFactory.createSingularShip(
				(x, y) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, Conf.fractionX(0.25f))),
				
				() -> (FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f))),
				
				
				() -> (FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_LURCHER);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f)))
		});
	}
	
}
