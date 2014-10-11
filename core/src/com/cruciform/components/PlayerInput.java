package com.cruciform.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.cruciform.input.InputAction;
import com.cruciform.input.InputCode;

public class PlayerInput extends Component {
    public static final ComponentMapper<PlayerInput> mapper = ComponentMapper.getFor(PlayerInput.class);
	public Map<InputCode, InputAction> actions = new HashMap<InputCode, InputAction>();
}