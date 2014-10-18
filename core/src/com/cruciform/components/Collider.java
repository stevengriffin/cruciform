package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.components.team.Team;


public class Collider extends AbstractComponent {
    public static final ComponentMapper<Collider> mapper = ComponentMapper.getFor(Collider.class);
    public List<Class<? extends Team>> teamsToCollide = new ArrayList<Class<? extends Team>>();
    public Array<Entity> entitiesCollidedWith = new Array<Entity>();
	public float mass = 0.0f;
}
