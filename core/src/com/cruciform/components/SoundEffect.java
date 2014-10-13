package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.audio.Sound;

public class SoundEffect extends Component {
    public static final ComponentMapper<SoundEffect> mapper = ComponentMapper.getFor(SoundEffect.class);
	public long id;
	public Sound sound;
}
