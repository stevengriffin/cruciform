package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class Damager extends Component {
    public static final ComponentMapper<Damager> mapper = ComponentMapper.getFor(Damager.class);
    public float damage = 0;
}
