package com.cruciform.events;

import com.badlogic.ashley.core.Entity;

public class CollisionEvent implements SignalEvent {
	public EventTag tag;
	public Entity culprit;
	public Entity victim;
	
	public CollisionEvent(EventTag tag, Entity culprit, Entity victim) {
		this.tag = tag;
		this.culprit = culprit;
		this.victim = victim;
	}
}
