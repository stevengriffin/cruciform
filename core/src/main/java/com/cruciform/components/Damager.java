package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.cruciform.factories.ExplosionFactory.Exploder;

public class Damager extends AbstractComponent {
    public static final ComponentMapper<Damager> mapper = ComponentMapper.getFor(Damager.class);
    public float damage = 0;
    public Exploder exploder;
}
