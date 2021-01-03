package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.badlogicmods.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.cruciform.utils.CoolDownMetro;

public class ParticleEmitter extends AbstractComponent {
    public static final ComponentMapper<ParticleEmitter> mapper = ComponentMapper.getFor(ParticleEmitter.class);
    public ParticleEffectPool pool;
    public Array<PooledEffect> effects = new Array<>();
    public CoolDownMetro coolDown;
    
    public ParticleEmitter(final Entity entity) {
    	super(entity);
    }
}
