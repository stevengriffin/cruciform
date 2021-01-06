package com.cruciform.components;



import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;
import org.jetbrains.annotations.NotNull;

public class Child extends AbstractComponent {
    public static final ComponentMapper<Child> mapper = ComponentMapper.getFor(Child.class);
    @NotNull public Entity parent = AbstractComponent.EMPTY_ENTITY;
    
    public Child(Entity entity) {
    	super(entity);
    }
}