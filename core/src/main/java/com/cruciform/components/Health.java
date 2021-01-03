package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.cruciform.factories.ExplosionFactory.Exploder;

public class Health extends AbstractComponent {
    public static final ComponentMapper<Health> mapper = ComponentMapper.getFor(Health.class);
    public float maxHealth = 0;
    public float currentHealth = 0;
    public Exploder deathExploder = null;
    public long lastTimeDamaged = 0;
}
