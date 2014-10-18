package com.cruciform.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.utils.CoolDownMetro;

public class Lifetime extends AbstractComponent {
    public static final ComponentMapper<Lifetime> mapper = ComponentMapper.getFor(Lifetime.class);
    private CoolDownMetro timeRemaining;
    
    public void setTimeRemaining(float time) {
    	timeRemaining = CoolDownMetro.asPrefired(time);
    }
    
    public boolean tick(float dt) {
    	return timeRemaining.tick(dt);
    }
}
