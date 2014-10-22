package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamPlayer;
import com.esotericsoftware.minlog.Log;


public class Collider extends AbstractComponent {
    public static final ComponentMapper<Collider> mapper = ComponentMapper.getFor(Collider.class);
    public List<Class<? extends Team>> teamsToCollide = new ArrayList<Class<? extends Team>>();
    public Array<Entity> entitiesCollidedWith = new Array<Entity>();
	public float mass = 0.0f;
	
	public static Collider defaultForProjectile(Entity entity, Class<? extends Team> team) {
		Collider collider = new Collider();
		if (team == TeamEnemy.class) {
			collider.teamsToCollide.add(TeamPlayer.class);
		} else {
			collider.teamsToCollide.add(TeamEnemy.class);
		}
		entity.add(collider);
		return collider;
	}	
}
