package com.cruciform.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.Position;
import com.cruciform.utils.Conf;
import com.esotericsoftware.minlog.Log;

public class FormationFactory {
	public static void createBroadFormation(final ShipFactory.ShipCreator creator,
			final float timeDelay, final int number, final int left, final int right) {
		final int increment = (right - left)/(number - 1);
		final float y = 0; // For now this is useless as it gets immediately adjusted after creation
		Log.debug("left: " + left + " right: " + right + " inc " + increment);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				for (int x = left; x <= right; x += increment) {
					Log.debug(" creating ship at " + x);
					Entity ship = creator.createAt(x, y);
					Position position = Position.mapper.get(ship);
					position.bounds.setPosition(position.bounds.getX(),
							Conf.screenHeight + position.bounds.getBoundingRectangle().height);
				}
			}
		}, timeDelay);
	}
}
