package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Seeker extends AbstractComponent {
    public static final ComponentMapper<Seeker> mapper = ComponentMapper.getFor(Seeker.class);
    public float speed = 50.0f;
}
