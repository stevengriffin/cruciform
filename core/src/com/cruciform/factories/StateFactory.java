package com.cruciform.factories;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.utils.ObjectMap;
import com.cruciform.Cruciform;
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
		State result = shouldRenew ? null : states.get(state);
		if (result == null) {
			try {
				result = state.getConstructor(Cruciform.class).newInstance(game);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			states.put(state, result);
		}
		return result;
	}

}
