package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;


public class Collider extends Component {
    public static enum Team { PLAYER, ENEMY, ROCKET };
    public static final ComponentMapper<Collider> mapper = ComponentMapper.getFor(Collider.class);
    public Team teamMember;
    public List<Team> teamsToCollide = new ArrayList<Team>();
    public List<Team> teamsToIgnore = new ArrayList<Team>();
	public float mass = 0.0f;
}
