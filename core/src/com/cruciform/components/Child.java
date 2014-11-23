package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class Child extends AbstractComponent {
    public static final ComponentMapper<Child> mapper = ComponentMapper.getFor(Child.class);
    public Entity parent = AbstractComponent.EMPTY_ENTITY;
    
    public Child(Entity entity) {
    	super(entity);
    }
}
