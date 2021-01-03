package com.cruciform.components.team;

import com.badlogicmods.ashley.core.ComponentMapper;

public class TeamEnemy extends Team {
	public static final ComponentMapper<TeamEnemy> mapper = ComponentMapper
			.getFor(TeamEnemy.class);
	public int soulCount = 5;
}
