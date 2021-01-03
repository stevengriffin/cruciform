package com.cruciform.levels;



import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.Cruciform;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.factories.FormationFactory;
import com.cruciform.factories.PathFactory;
import com.cruciform.factories.ShipFactory;
import com.cruciform.utils.Conf;


public class Level1 extends Level {

	/* Lore
	 * Although the Naphil wish it forgotten,
	 * in those days also the angels worked at sacred sculpture
	 * and from their unskilled hands were born gravely imperfect beings
	 * to whom immortality was an eternal curse.
	 * For their flaws, the sun-warmed kingdoms cast out these children
	 * and in everlasting torment they wander deeply in the earth.
	 * 
	 * One we called Ache:
	 * for from deep in his head issued an agony
	 * terrible beyond all the sum of man's fire and teeth and hate.
	 * Such was his pain that each day with his long hands he tore through his eyes and into his skull
	 * rending the stuff within until all that was hurt and all that was he receded to dimness.
	 * But each day his head would form anew, and so too would the ache.
	 */
	
	
	public Level1(final Cruciform game, final ShipFactory shipFactory, PathFactory pathFactory) {
		super(game, shipFactory);
		this.waves = new Array<Level.Wave>(new Level.Wave[] { 
				
//				() -> (FormationFactory.createSingularShip(
//                (x, y) -> {
//                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.ZIGZAG);
//                	pathFactory.createBentPath(entity, false);
//                	return entity;
//                },
//				2.0f, Conf.fractionX(0.5f))),
				
				() -> FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f)),
				
				() -> FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.PENTAGRAM);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f)),
				
				() -> FormationFactory.createBroadFormation(
                (x, y, i) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_3PRONG);
                	pathFactory.createBentPath(entity, i % 2 == 1);
                	return entity;
                },
				1.0f, 4, Conf.fractionX(0.4f), Conf.fractionX(0.6f)),
				
				() -> FormationFactory.createSingularShip(
				(x, y) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				2.0f, Conf.fractionX(0.5f)),
				
				() -> FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPLITTER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f)),
				
				() -> FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_STRAIGHT),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f)),
				
				() -> FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f)),
				
				() -> FormationFactory.createSingularShip(
				(x, y) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, Conf.fractionX(0.25f)),
				
				() -> FormationFactory.createBroadFormation(
				(x, y, i) -> shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_SPIRALER_SOLID),
				1.0f, 3, Conf.fractionX(0.1f), Conf.fractionX(0.9f)),
				
				() -> FormationFactory.createSingularShip(
                (x, y) -> {
                	Entity entity = shipFactory.createEnemy(x, y, EnemyTypes.RADIAL_LURCHER);
                	pathFactory.createBentPath(entity, false);
                	return entity;
                },
				2.0f, Conf.fractionX(0.5f))
		});
	}
	
}
