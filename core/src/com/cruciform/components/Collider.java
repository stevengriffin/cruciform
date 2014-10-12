package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.components.team.Team;


public class Collider extends Component {
    public static final ComponentMapper<Collider> mapper = ComponentMapper.getFor(Collider.class);
    public List<Class<? extends Team>> teamsToCollide = new ArrayList<Class<? extends Team>>();
	public float mass = 0.0f;
}
