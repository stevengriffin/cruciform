package com.cruciform.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogicmods.ashley.core.ComponentMapper;
import com.cruciform.input.InputAction;
import com.cruciform.input.InputCode;

public class PlayerInput extends AbstractComponent {
    public static final ComponentMapper<PlayerInput> mapper = ComponentMapper.getFor(PlayerInput.class);
	public Map<InputCode, InputAction> actions = new HashMap<InputCode, InputAction>();
}