package com.cruciform.serialization;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.components.Collider;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class ColliderSerializer extends FieldSerializer<Collider> {
	public ColliderSerializer(Kryo kryo, Class<? extends Collider> type) {
		super(kryo, type);
	}

	@Override
	public Collider copy(Kryo kryo, Collider original) {
		Array<Entity> originalArray = original.entitiesCollidedWith;
		original.entitiesCollidedWith = new Array<Entity>();
		Collider copied = super.copy(kryo, original);
		original.entitiesCollidedWith = originalArray;
		return copied;
	}
}
