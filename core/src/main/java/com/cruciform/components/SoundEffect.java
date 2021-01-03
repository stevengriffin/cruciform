package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogic.gdx.audio.Sound;

public class SoundEffect extends AbstractComponent {
    public static final ComponentMapper<SoundEffect> mapper = ComponentMapper.getFor(SoundEffect.class);
	public long id;
	public Sound sound;
}
