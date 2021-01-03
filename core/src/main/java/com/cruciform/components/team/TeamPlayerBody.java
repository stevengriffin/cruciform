package com.cruciform.components.team;

import com.badlogic.ashley.core.ComponentMapper;

public class TeamPlayerBody extends Team {
    public static final ComponentMapper<TeamPlayerBody> mapper = ComponentMapper.getFor(TeamPlayerBody.class);
    public int pointValue = 25;
}
