package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.components.AI;
import com.cruciform.components.Collider;
import com.cruciform.components.Health;
import com.cruciform.components.LineMover;
import com.cruciform.components.PlayerInput;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.Shooter;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.TeamEnemy;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.enemies.EnemyTypes;
import com.cruciform.images.ImageManager;
import com.cruciform.images.Picture;
import com.cruciform.input.InputCode;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;
import com.cruciform.utils.Priority;
import com.cruciform.weapons.CruciformWeapon;
import com.cruciform.weapons.RifleWeapon;
import com.cruciform.weapons.RocketWeapon;
import com.cruciform.weapons.SweepWeapon;

public class ShipFactory {
	private final Engine engine;
	private final ExplosionFactory explosionFactory;
	private final EnemyWeaponFactory weaponFactory;
	public interface ShipCreator {
		public Entity createAt(float x, float y);
	}
	
	public interface ShipCreatorIndexed {
		public Entity createAt(float x, float y, float i);
	}
	
	public ShipFactory(final Engine engine, final ExplosionFactory explosionFactory) {
		this.engine = engine;
		this.explosionFactory = explosionFactory;
	    this.weaponFactory = new EnemyWeaponFactory(engine, explosionFactory);
	}
	
	public Entity createPlayer(float x, float y) {
		Entity entity = new Entity();
		
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, 5, 5);
		position.yDirection = 1;
		
		Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_GOLD_COCKPIT);
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -45;
		renderer.customOffset = true;
		renderer.priority = new Priority(1);
		
		TeamPlayer team = new TeamPlayer();
		entity.add(team);
		
		CruciformWeapon cruciform = new CruciformWeapon(engine, team.getClass());
		RocketWeapon rocket = new RocketWeapon(0.1f, engine, explosionFactory, team.getClass());
		RifleWeapon rifle = new RifleWeapon(0.05f, engine, explosionFactory, team.getClass());
		SweepWeapon sweep = new SweepWeapon(2.0f, engine, explosionFactory, team.getClass());
		
		Shooter shooter = new Shooter();
		shooter.weapons.add(cruciform);
		shooter.weapons.add(rocket);
		shooter.weapons.add(rifle);
		shooter.weapons.add(sweep);
		entity.add(shooter);
		
		PlayerInput playerInput = new PlayerInput();
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.LEFT), cruciform);
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.RIGHT), rocket);
		playerInput.actions.put(InputCode.fromKey(Input.Keys.Z), rifle);
		playerInput.actions.put(InputCode.fromKey(Input.Keys.X), sweep);
		entity.add(playerInput);

		Health health = new Health();
		health.maxHealth = 1;
		health.currentHealth = 1;
		entity.add(health);
	
		Collider collider = new Collider();
		entity.add(collider);
		
		engine.addEntity(entity);
		
		// Create graphical player effects
		EffectFactory.createPlayerExhaust(entity, engine);
		EffectFactory.createPlayerBody(x, y, entity, engine);
		
		
		return entity;
	}
	
	public Entity createEnemy(float x, float y, EnemyTypes type) {
		Entity entity = new Entity();
		
		Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.get(Picture.PLAYER_SHIP_1);
		
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, 
				renderer.image.getRegionWidth(),
				renderer.image.getRegionHeight());
		position.yDirection = -1;
		position.outOfBoundsHandler = OutOfBoundsHandler.south();
		
		LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(0.0f, -100.0f);
		lineMover.accelerates = false;
		entity.add(lineMover);
	
		entity.add(new Velocity());

		Collider collider = new Collider();
		entity.add(collider);
	
		TeamEnemy team = new TeamEnemy();
		entity.add(team);
	
		weaponFactory.createShooter(type, entity);
	
		AI ai = new AI();
		entity.add(ai);
		
		Health health = new Health();
		health.maxHealth = 100;
		health.currentHealth = 100;
		entity.add(health);
	
		engine.addEntity(entity);
		return entity;
	}
}
