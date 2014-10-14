package com.cruciform.factories;

import com.badlogic.gdx.utils.ObjectMap;
import com.cruciform.Cruciform;
import com.cruciform.states.State;

public class StateFactory {

	private static ObjectMap<Class<? extends State>, State> states = new ObjectMap<>();

	public static State getOrCreate(final Class<? extends State> state, final Cruciform game) {
		try {
			State result = states.get(state, state.getConstructor(Cruciform.class).newInstance(game));
			states.put(state, result);
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
}
