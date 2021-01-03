package com.cruciform.components;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.cruciform.utils.Metro;

public class Blinker extends AbstractComponent {
    public static final ComponentMapper<Blinker> mapper = ComponentMapper.getFor(Blinker.class);
    public Metro metro;
}