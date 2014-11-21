package com.cruciform.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

@NonNullByDefault
public class Child extends AbstractComponent {
    public static final ComponentMapper<Child> mapper = ComponentMapper.getFor(Child.class);
    public Entity parent = AbstractComponent.EMPTY_ENTITY;
    
    public Child(Entity entity) {
    	super(entity);
    }
}
