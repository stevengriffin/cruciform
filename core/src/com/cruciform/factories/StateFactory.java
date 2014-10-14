package com.cruciform.factories;

import com.badlogic.gdx.utils.ObjectMap;
import com.cruciform.Cruciform;
import com.cruciform.states.State;

public class StateFactory {

	private static ObjectMap<Class<? extends State>, State> states = new ObjectMap<>();

	public static State setState(final Class<? extends State> state, final Cruciform game) {
		State result = getOrCreate(state, game);
		game.setScreen(result);
		return result;
	}
	
	private static State getOrCreate(final Class<? extends State> state, final Cruciform game) {
		try {
			State result = states.get(state);
			if (result == null) {
				result = state.getConstructor(Cruciform.class).newInstance(game);
				states.put(state, result);
			}
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
}
