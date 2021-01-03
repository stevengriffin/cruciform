package com.cruciform.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractComponent extends Component {

	@NotNull public static final Entity EMPTY_ENTITY = new Entity();
	
	public AbstractComponent(Entity e) {
		super();
		e.add(this);
	}
	
	public AbstractComponent() {
		super();
	}
}
