package com.cruciform.utils;



import com.badlogic.gdx.utils.ObjectMap;
import org.jetbrains.annotations.NotNull;

public class SafeObjectMap<K, V> extends ObjectMap<K, V> {

	/** Returns the value for the specified key, or the default value if the key is not in the map. */
	public @NotNull V getSafe (K key, V defaultValue) {
		if (key == null || defaultValue == null) { throw new NullPointerException(); }
		V value = super.get(key, defaultValue);
		if (value == null) { throw new NullPointerException(); }
		return value;
	}
}
