package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Health extends AbstractComponent {
    public static final ComponentMapper<Health> mapper = ComponentMapper.getFor(Health.class);
    public float maxHealth = 0;
    public float currentHealth = 0;
}
