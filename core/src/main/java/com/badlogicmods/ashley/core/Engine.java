/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicmods.ashley.core;

import java.util.Comparator;


import com.badlogicmods.ashley.signals.Listener;
import com.badlogicmods.ashley.signals.Signal;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogicmods.ashley.utils.ImmutableArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The heart of the Entity framework. It is responsible for keeping track of {@link com.badlogicmods.ashley.core.Entity} and managing {@link EntitySystem}
 * objects. The Engine should be updated every tick via the {@link #update(float)} method. With the Engine you can:
 * <ul>
 * <li>Add/Remove {@link com.badlogicmods.ashley.core.Entity} objects</li>
 * <li>Add/Remove {@link EntitySystem}s</li>
 * <li>Obtain a list of entities for a specific {@link com.badlogicmods.ashley.core.Family}</li>
 * <li>Update the main loop</li>
 * <li>Register/unregister {@link EntityListener} objects</li>
 * </ul>
 * @author Stefan Bachmann
 */
public class Engine {
	private static SystemComparator comparator = new SystemComparator();

	private Array<com.badlogicmods.ashley.core.Entity> entities;
	private Array<EntityOperation> entityOperations;
	private EntityOperationPool entityOperationPool;
	private Array<EntitySystem> systems;
	private com.badlogicmods.ashley.utils.ImmutableArray<EntitySystem> immutableSystems;
	private ObjectMap<Class<?>, EntitySystem> systemsByClass;
	private ObjectMap<com.badlogicmods.ashley.core.Family, Array<com.badlogicmods.ashley.core.Entity>> families;
	private ObjectMap<com.badlogicmods.ashley.core.Family, com.badlogicmods.ashley.utils.ImmutableArray<com.badlogicmods.ashley.core.Entity>> immutableFamilies;
	private SnapshotArray<EntityListener> listeners;
	private ObjectMap<com.badlogicmods.ashley.core.Family, SnapshotArray<EntityListener>> familyListeners;
	private ComponentOperationPool componentOperationsPool;
	private Array<ComponentOperation> componentOperations;
	private ComponentOperationHandler componentOperationHandler;
	private boolean updating;
	private boolean notifying;
	private long nextEntityId = 1;

	private final Listener<com.badlogicmods.ashley.core.Entity> componentAdded;
	private final Listener<com.badlogicmods.ashley.core.Entity> componentRemoved;

	public Engine () {
		entities = new Array<com.badlogicmods.ashley.core.Entity>(false, 16);
		entityOperations = new Array<EntityOperation>(false, 16);
		entityOperationPool = new EntityOperationPool();
		systems = new Array<EntitySystem>(false, 16);
		immutableSystems = new com.badlogicmods.ashley.utils.ImmutableArray<EntitySystem>(systems);
		systemsByClass = new ObjectMap<Class<?>, EntitySystem>();
		families = new ObjectMap<com.badlogicmods.ashley.core.Family, Array<com.badlogicmods.ashley.core.Entity>>();
		immutableFamilies = new ObjectMap<com.badlogicmods.ashley.core.Family, com.badlogicmods.ashley.utils.ImmutableArray<com.badlogicmods.ashley.core.Entity>>();
		listeners = new SnapshotArray<EntityListener>(false, 16);
		familyListeners = new ObjectMap<com.badlogicmods.ashley.core.Family, SnapshotArray<EntityListener>>();

		componentAdded = new ComponentListener(this);
		componentRemoved = new ComponentListener(this);

		updating = false;
		notifying = false;

		componentOperationsPool = new ComponentOperationPool();
		componentOperations = new Array<ComponentOperation>();
		componentOperationHandler = new ComponentOperationHandler(this);
	}

	private long obtainEntityId () {
		return nextEntityId++;
	}

	/** Adds an entity to this Engine. */
	public void addEntity (com.badlogicmods.ashley.core.Entity entity) {
		entity.uuid = obtainEntityId();
		if (notifying) {
			EntityOperation operation = entityOperationPool.obtain();
			operation.entity = entity;
			operation.type = EntityOperation.Type.Add;
			entityOperations.add(operation);
		} else {
			addEntityInternal(entity);
		}
	}

	/** Removes an entity from this Engine. */
	public void removeEntity (com.badlogicmods.ashley.core.Entity entity) {
		if (updating || notifying) {
			if (entity.scheduledForRemoval) {
				return;
			}
			entity.scheduledForRemoval = true;
			EntityOperation operation = entityOperationPool.obtain();
			operation.entity = entity;
			operation.type = EntityOperation.Type.Remove;
			entityOperations.add(operation);
		} else {
			removeEntityInternal(entity);
		}
	}

	/** Removes all entities registered with this Engine. */
	public void removeAllEntities () {
		if (updating || notifying) {
			for (com.badlogicmods.ashley.core.Entity entity : entities) {
				entity.scheduledForRemoval = true;
			}
			EntityOperation operation = entityOperationPool.obtain();
			operation.type = EntityOperation.Type.RemoveAll;
			entityOperations.add(operation);
		} else {
			while (entities.size > 0) {
				removeEntity(entities.first());
			}
		}
	}

	/** Adds the {@link EntitySystem} to this Engine. */
	public void addSystem (EntitySystem system) {
		@SuppressWarnings("null")
		@NotNull Class<? extends EntitySystem> systemType = system.getClass();

		if (!systemsByClass.containsKey(systemType)) {
			systems.add(system);
			systemsByClass.put(systemType, system);
			system.addedToEngine(this);

			systems.sort(comparator);
		}
	}

	/** Removes the {@link EntitySystem} from this Engine. */
	@SuppressWarnings("null")
	public void removeSystem (EntitySystem system) {
		if (systems.removeValue(system, true)) {
			systemsByClass.remove(system.getClass());
			system.removedFromEngine(this);
		}
	}

	/** Quick {@link EntitySystem} retrieval. */
	@SuppressWarnings("unchecked")
	public @Nullable <T extends EntitySystem> T getSystem (Class<T> systemType) {
		return (T)systemsByClass.get(systemType);
	}

	/** @return immutable array of all entity systems managed by the {@link Engine}. */
	public com.badlogicmods.ashley.utils.ImmutableArray<EntitySystem> getSystems () {
		return immutableSystems;
	}

	/** Returns immutable collection of entities for the specified {@link com.badlogicmods.ashley.core.Family}. Will return the same instance every time. */
	public com.badlogicmods.ashley.utils.ImmutableArray<com.badlogicmods.ashley.core.Entity> getEntitiesFor (com.badlogicmods.ashley.core.Family family) {
		return registerFamily(family);
	}

	/**
	 * Adds an {@link EntityListener}. The listener will be notified every time an entity is added/removed to/from the engine.
	 */
	public void addEntityListener (EntityListener listener) {
		listeners.add(listener);
	}

	/**
	 * Adds an {@link EntityListener} for a specific {@link com.badlogicmods.ashley.core.Family}. The listener will be notified every time an entity is
	 * added/removed to/from the given family.
	 */
	@SuppressWarnings({ "unused", "null" })
	public void addEntityListener (com.badlogicmods.ashley.core.Family family, EntityListener listener) {
		registerFamily(family);
		SnapshotArray<EntityListener> listeners = familyListeners.get(family);

		if (listeners == null) {
			listeners = new SnapshotArray<EntityListener>(false, 16);
			familyListeners.put(family, listeners);
		}

		listeners.add(listener);
	}

	/** Removes an {@link EntityListener} */
	public void removeEntityListener (EntityListener listener) {
		listeners.removeValue(listener, true);

		for (SnapshotArray<EntityListener> familyListenerArray : familyListeners.values()) {
			familyListenerArray.removeValue(listener, true);
		}
	}

	/**
	 * Updates all the systems in this Engine.
	 * @param deltaTime The time passed since the last frame.
	 */
	public void update (float deltaTime) {
		updating = true;
		for (int i = 0; i < systems.size; i++) {
			EntitySystem system = systems.get(i);
			if (system.checkProcessing()) {
				system.update(deltaTime);
			}

			processComponentOperations();
			processPendingEntityOperations();
		}

		updating = false;
	}

	private void updateFamilyMembership (com.badlogicmods.ashley.core.Entity entity) {
		for (Entry<com.badlogicmods.ashley.core.Family, Array<com.badlogicmods.ashley.core.Entity>> entry : families.entries()) {
			com.badlogicmods.ashley.core.Family family = entry.key;
			Array<com.badlogicmods.ashley.core.Entity> entities = entry.value;
			int familyIndex = family.getIndex();

			boolean belongsToFamily = entity.getFamilyBits().get(familyIndex);
			boolean matches = family.matches(entity);

			if (!belongsToFamily && matches) {
				entities.add(entity);
				entity.getFamilyBits().set(familyIndex);

				notifyFamilyListenersAdd(family, entity);
			} else if (belongsToFamily && !matches) {
				entities.removeValue(entity, true);
				entity.getFamilyBits().clear(familyIndex);

				notifyFamilyListenersRemove(family, entity);
			}
		}
	}

	protected void removeEntityInternal (com.badlogicmods.ashley.core.Entity entity) {
		entity.scheduledForRemoval = false;
		entities.removeValue(entity, true);

		if (!entity.getFamilyBits().isEmpty()) {
			for (Entry<com.badlogicmods.ashley.core.Family, Array<com.badlogicmods.ashley.core.Entity>> entry : families.entries()) {
				com.badlogicmods.ashley.core.Family family = entry.key;
				Array<com.badlogicmods.ashley.core.Entity> entities = entry.value;

				if (family.matches(entity)) {
					entities.removeValue(entity, true);
					entity.getFamilyBits().clear(family.getIndex());
					notifyFamilyListenersRemove(family, entity);
				}
			}
		}

		entity.componentAdded.remove(componentAdded);
		entity.componentRemoved.remove(componentRemoved);
		entity.componentOperationHandler = null;

		notifying = true;
		Object[] items = listeners.begin();
		for (int i = 0, n = listeners.size; i < n; i++) {
			EntityListener listener = (EntityListener)items[i];
			listener.entityRemoved(entity);
		}
		listeners.end();
		notifying = false;
	}

	protected void addEntityInternal (com.badlogicmods.ashley.core.Entity entity) {
		entities.add(entity);

		updateFamilyMembership(entity);

		entity.componentAdded.add(componentAdded);
		entity.componentRemoved.add(componentRemoved);
		entity.componentOperationHandler = componentOperationHandler;

		notifying = true;
		Object[] items = listeners.begin();
		for (int i = 0, n = listeners.size; i < n; i++) {
			EntityListener listener = (EntityListener)items[i];
			listener.entityAdded(entity);
		}
		listeners.end();
		notifying = false;
	}

	private void notifyFamilyListenersAdd (com.badlogicmods.ashley.core.Family family, com.badlogicmods.ashley.core.Entity entity) {
		SnapshotArray<EntityListener> listeners = familyListeners.get(family);

		if (listeners != null) {
			notifying = true;
			Object[] items = listeners.begin();
			for (int i = 0, n = listeners.size; i < n; i++) {
				EntityListener listener = (EntityListener)items[i];
				listener.entityAdded(entity);
			}
			listeners.end();
			notifying = false;
		}
	}

	private void notifyFamilyListenersRemove (com.badlogicmods.ashley.core.Family family, com.badlogicmods.ashley.core.Entity entity) {
		SnapshotArray<EntityListener> listeners = familyListeners.get(family);

		if (listeners != null) {
			notifying = true;
			Object[] items = listeners.begin();
			for (int i = 0, n = listeners.size; i < n; i++) {
				EntityListener listener = (EntityListener)items[i];
				listener.entityRemoved(entity);
			}
			listeners.end();
			notifying = false;
		}
	}

	@SuppressWarnings({ "null", "unused" })
	private com.badlogicmods.ashley.utils.ImmutableArray<com.badlogicmods.ashley.core.Entity> registerFamily (Family family) {
		com.badlogicmods.ashley.utils.ImmutableArray<com.badlogicmods.ashley.core.Entity> immutableEntities = immutableFamilies.get(family);

		if (immutableEntities == null) {
			Array<com.badlogicmods.ashley.core.Entity> entities = new Array<com.badlogicmods.ashley.core.Entity>(false, 16);
			immutableEntities = new ImmutableArray<com.badlogicmods.ashley.core.Entity>(entities);
			families.put(family, entities);
			immutableFamilies.put(family, immutableEntities);

			for (com.badlogicmods.ashley.core.Entity e : this.entities) {
				if (e == null) { continue; }
				if (family.matches(e)) {
					entities.add(e);
					e.getFamilyBits().set(family.getIndex());
				}
			}
		}

		return immutableEntities;
	}

	private void processPendingEntityOperations () {
		while (entityOperations.size > 0) {
			EntityOperation operation = entityOperations.removeIndex(entityOperations.size - 1);
			final Engine.EntityOperation.Type type = operation.type;
			if (type == null) {
				continue;
			}
			final com.badlogicmods.ashley.core.Entity entity = operation.entity;
			if (entity == null) {
				continue;
			}
			switch (type) {
			case Add:
				addEntityInternal(entity);
				break;
			case Remove:
				removeEntityInternal(entity);
				break;
			case RemoveAll:
				while (entities.size > 0) {
					removeEntityInternal(entities.first());
				}
				break;
			}

			entityOperationPool.free(operation);
		}

		entityOperations.clear();
	}

	public void processComponentOperations () {
		int numOperations = componentOperations.size;

		for (int i = 0; i < numOperations; ++i) {
			ComponentOperation operation = componentOperations.get(i);
			switch (operation.type) {
			case Add:
				final com.badlogicmods.ashley.core.Component component = operation.component;
				if (component != null) {
					operation.entity.addInternal(component);
				}
				break;
			case Remove:
				operation.entity.removeInternal(operation.componentClass);
				break;
			}

			componentOperationsPool.free(operation);
		}

		componentOperations.clear();
	}

	private static class ComponentListener implements Listener<com.badlogicmods.ashley.core.Entity> {
		private Engine engine;

		public ComponentListener (Engine engine) {
			this.engine = engine;
		}

		@Override
		public void receive (Signal<com.badlogicmods.ashley.core.Entity> signal, com.badlogicmods.ashley.core.Entity object) {
			engine.updateFamilyMembership(object);
		}
	}

	static class ComponentOperationHandler {
		@NotNull private Engine engine;

		public ComponentOperationHandler (@NotNull Engine engine) {
			this.engine = engine;
		}

		public void add (@NotNull com.badlogicmods.ashley.core.Entity entity, @NotNull com.badlogicmods.ashley.core.Component component) {
			if (engine.updating) {
				ComponentOperation operation = engine.componentOperationsPool.obtain();
				operation.makeAdd(entity, component);
				engine.componentOperations.add(operation);
			} else {
				entity.addInternal(component);
			}
		}

		public void remove (@NotNull com.badlogicmods.ashley.core.Entity entity, Class<? extends com.badlogicmods.ashley.core.Component> componentClass) {
			if (engine.updating) {
				ComponentOperation operation = engine.componentOperationsPool.obtain();
				operation.makeRemove(entity, componentClass);
				engine.componentOperations.add(operation);
			} else {
				entity.removeInternal(componentClass);
			}
		}
	}

	private static class ComponentOperation {
		public enum Type {
			Add, Remove,
		}

		private static final com.badlogicmods.ashley.core.Entity NULL_ENTITY = new com.badlogicmods.ashley.core.Entity();
		public Type type = Type.Add;
		public com.badlogicmods.ashley.core.Entity entity = NULL_ENTITY;
		@Nullable public com.badlogicmods.ashley.core.Component component;
		public Class<? extends com.badlogicmods.ashley.core.Component> componentClass;
		
		public void makeAdd (com.badlogicmods.ashley.core.Entity entity, com.badlogicmods.ashley.core.Component component) {
			this.type = Type.Add;
			this.entity = entity;
			this.component = component;
			this.componentClass = null;
		}

		public void makeRemove (@NotNull com.badlogicmods.ashley.core.Entity entity, Class<? extends Component> componentClass) {
			this.type = Type.Remove;
			this.entity = entity;
			this.component = null;
			this.componentClass = componentClass;
		}
	}

	private static class ComponentOperationPool extends Pool<ComponentOperation> {
		@Override
		protected ComponentOperation newObject () {
			return new ComponentOperation();
		}
	}

	private static class SystemComparator implements Comparator<EntitySystem> {
		@Override
		public int compare (@Nullable EntitySystem a, @Nullable EntitySystem b) {
			if (a == null || b == null) { throw new NullPointerException(); }
			return a.priority > b.priority ? 1 : (a.priority == b.priority) ? 0 : -1;
		}
	}

	private static class EntityOperation {
		public enum Type {
			Add, Remove, RemoveAll
		}

		@Nullable public Type type;
		@Nullable public Entity entity;
	}

	private static class EntityOperationPool extends Pool<EntityOperation> {
		@Override
		protected EntityOperation newObject () {
			return new EntityOperation();
		}
	}
}
