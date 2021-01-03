package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.components.team.TeamPlayerBody;


public class Collider extends AbstractComponent {
    public static final ComponentMapper<Collider> mapper = ComponentMapper.getFor(Collider.class);
    public List<Class<? extends Team>> teamsToCollide = new ArrayList<Class<? extends Team>>();
    public Array<Entity> entitiesCollidedWith = new Array<Entity>();
	public float mass = 0.0f;

	public Collider(final Entity entity) {
		super(entity);
	}
	
	public static Collider defaultForProjectile(final Entity entity, final Class<? extends Team> team) {
		Collider collider = new Collider(entity);
		if (team == TeamEnemy.class) {
			collider.teamsToCollide.add(TeamPlayer.class);
			collider.teamsToCollide.add(TeamPlayerBody.class);
		} else {
			collider.teamsToCollide.add(TeamEnemy.class);
		}
		return collider;
	}	
}
