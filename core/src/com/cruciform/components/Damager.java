package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Damager extends AbstractComponent {
    public static final ComponentMapper<Damager> mapper = ComponentMapper.getFor(Damager.class);
    public float damage = 0;
}
