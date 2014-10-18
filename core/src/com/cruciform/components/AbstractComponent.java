package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public abstract class AbstractComponent extends Component {

	public AbstractComponent(Entity e) {
		super();
		e.add(this);
	}
	
	public AbstractComponent() {
		super();
	}
}
