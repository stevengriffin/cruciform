package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.utils.CoolDownMetro;

public class Lifetime extends Component {
    public static final ComponentMapper<Lifetime> mapper = ComponentMapper.getFor(Lifetime.class);
    public CoolDownMetro timeRemaining;
}
