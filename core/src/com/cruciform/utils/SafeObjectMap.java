package com.cruciform.utils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.utils.ObjectMap;

@NonNullByDefault
public class SafeObjectMap<K, V> extends ObjectMap<K, V> {

	/** Returns the value for the specified key, or the default value if the key is not in the map. */
	public V getSafe (K key, V defaultValue) {
		V value = super.get(key, defaultValue);
		if (value == null) { throw new NullPointerException(); }
		return value;
	}
}
