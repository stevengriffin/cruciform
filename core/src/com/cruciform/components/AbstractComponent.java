package com.cruciform.components;

import org.eclipse.jdt.annotation.NonNull;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public abstract class AbstractComponent extends Component {

	@NonNull public static final Entity EMPTY_ENTITY = new Entity();
	
	public AbstractComponent(Entity e) {
		super();
		e.add(this);
	}
	
	public AbstractComponent() {
		super();
	}
}
