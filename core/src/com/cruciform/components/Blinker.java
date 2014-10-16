package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.utils.Metro;

public class Blinker extends Component {
    public static final ComponentMapper<Blinker> mapper = ComponentMapper.getFor(Blinker.class);
    public Metro metro;
}