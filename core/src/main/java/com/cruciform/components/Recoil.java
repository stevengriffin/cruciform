package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;

public class Recoil extends AbstractComponent {
    public static final ComponentMapper<Recoil> mapper = ComponentMapper.getFor(Recoil.class);
    public float maxOffset = 15;
    public float recoveryRate = 50;
    public float yOffset = 0.0f;
    
    public Recoil(Entity entity) {
    	super(entity);
    }
}
