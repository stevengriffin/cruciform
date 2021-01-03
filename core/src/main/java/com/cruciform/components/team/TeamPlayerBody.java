package com.cruciform.components.team;

import com.badlogicmods.ashley.core.ComponentMapper;

public class TeamPlayerBody extends Team {
    public static final ComponentMapper<TeamPlayerBody> mapper = ComponentMapper.getFor(TeamPlayerBody.class);
    public int pointValue = 25;
}
