package com.cruciform.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.weapons.Weapon;

public class Shooter extends AbstractComponent {
    public static final ComponentMapper<Shooter> mapper = ComponentMapper.getFor(Shooter.class);
	public List<Weapon> weapons = new ArrayList<Weapon>();
}
