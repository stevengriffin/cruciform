package com.cruciform.components.team;

import com.badlogic.ashley.core.ComponentMapper;

public class TeamSoul extends Team {
    public static final ComponentMapper<TeamSoul> mapper = ComponentMapper.getFor(TeamSoul.class);
    public int pointValue = 100;
}
