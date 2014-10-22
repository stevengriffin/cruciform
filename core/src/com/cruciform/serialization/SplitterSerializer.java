package com.cruciform.serialization;

import com.cruciform.components.Splitter;
import com.cruciform.utils.EntityMutator;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class SplitterSerializer extends FieldSerializer<Splitter> {
	public SplitterSerializer(Kryo kryo, Class<? extends Splitter> type) {
		super(kryo, type);
	}

	@Override
	public Splitter copy(Kryo kryo, Splitter original) {
		EntityMutator customSplitBehavior = original.customSplitBehavior;
		original.customSplitBehavior = null;
		Splitter newObject = super.copy(kryo, original);
		original.customSplitBehavior = customSplitBehavior;
		newObject.customSplitBehavior = customSplitBehavior;
		return newObject;
	}
}
