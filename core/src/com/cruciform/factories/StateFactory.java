package com.cruciform.factories;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.utils.ObjectMap;
import com.cruciform.Cruciform;
import com.cruciform.states.MainMenuState;
import com.cruciform.states.State;

@NonNullByDefault({})
public class StateFactory {

	private static ObjectMap<Class<? extends State>, State> states = new ObjectMap<>();

	public static State setState(final Class<? extends State> state, @NonNull final Cruciform game) {
		State result = getOrCreate(state, game, false);
		game.setScreen(result);
		return result;
	}
	
	public static State setAndRenewState(final Class<? extends State> state, @NonNull final Cruciform game) {
		State result = getOrCreate(state, game, true);
		game.setScreen(result);
		return result;
	}
	
	private static State getOrCreate(final Class<? extends State> state, @NonNull final Cruciform game,
			final boolean shouldRenew) {
		try {
			State result = shouldRenew ? null : states.get(state);
			if (result == null) {
				result = state.getConstructor(Cruciform.class).newInstance(game);
				states.put(state, result);
			}
			return result;
		} catch(Exception e) {
			return states.get(MainMenuState.class);
		}
	}
	
}
