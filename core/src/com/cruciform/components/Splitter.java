package com.cruciform.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Array;
import com.cruciform.utils.EntityMutator;

public class Splitter extends AbstractComponent {
    public static final ComponentMapper<Splitter> mapper = ComponentMapper.getFor(Splitter.class);
	public boolean deleteOldEntity = false;
	public Array<Class<? extends Component>> componentsToRemoveFromChildren = new Array<>();
	public int numberOfNewEntities;
	public EntityMutator customSplitBehavior;
	public boolean splitOnCollision = true;
	public boolean splitOnDeletion = false;
	public boolean splitOnNextUpdate = false;
	public float collisionY = 0.0f; // TODO refactor
}
