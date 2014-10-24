package com.cruciform.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.Position;
import com.cruciform.utils.Conf;

public class FormationFactory {

	private static int activeFormationTasks = 0;
	
	public static void reset() {
		activeFormationTasks = 0;
	}

	public static boolean allTasksFinished() {
		return activeFormationTasks == 0;
	}
	
	private static void schedule(final Runnable formationTask, final float timeDelay) {
		activeFormationTasks++;
		Timer.schedule(new Task() {
			public void run() {
				formationTask.run();
				activeFormationTasks--;
			}
		}, timeDelay);
		
	}
	
	public static void createBroadFormation(final ShipFactory.ShipCreatorIndexed creator,
			final float timeDelay, final int number, final int left, final int right) {
		final int increment = (right - left)/(number - 1);
		schedule(() -> {
			int i = 0;
			for (int x = left; x <= right; x += increment) {
				Entity ship = creator.createAt(x, 0, i);
				Position position = Position.mapper.get(ship);
				position.bounds.setPosition(position.bounds.getX(),
						Conf.canonicalHeight + position.bounds.getBoundingRectangle().height);
				i++;
			}
		}, timeDelay);
	}
	
	public static void createSingularShip(final ShipFactory.ShipCreator creator, final float timeDelay,
			float x) {
		schedule(() -> {
			Entity ship = creator.createAt(x, 0);
			Position position = Position.mapper.get(ship);
			position.bounds.setPosition(position.bounds.getX(),
					Conf.canonicalHeight + position.bounds.getBoundingRectangle().height);
		}, timeDelay);
	}
}
