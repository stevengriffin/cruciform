package com.cruciform.serialization;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cruciform.components.Renderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class RendererSerializer extends FieldSerializer<Renderer> {
	public RendererSerializer(Kryo kryo, Class<? extends Renderer> type) {
		super(kryo, type);
	}

	@Override
	public Renderer copy(Kryo kryo, Renderer original) {
		TextureRegion image = original.image;
		original.image = null;
		Renderer copied = super.copy(kryo, original);
		original.image = image;
		copied.image = image;
		return copied;
	}
}
