package com.cruciform.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cruciform.components.AI;
import com.cruciform.components.Animator;
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
	
	public Entity createPlayer(final Entity entity, final boolean playIntro) {
		
		Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.PLAYER_SHIP_GOLD_COCKPIT;
		renderer.customXOffset = -32.5f;
		renderer.customYOffset = -45;
		renderer.customOffset = true;
		renderer.priority = new Priority(1);
		
		final TeamPlayer team = new TeamPlayer();
		entity.add(team);
		
		final CruciformWeapon cruciform = new CruciformWeapon(engine, team.getClass());
		final RocketWeapon rocket = new RocketWeapon(0.1f, engine, explosionFactory, team.getClass());
		final RifleWeapon rifle = new RifleWeapon(0.05f, engine, explosionFactory, team.getClass());
		final SweepWeapon sweep = new SweepWeapon(2.0f, engine, team.getClass());
		
		final Shooter shooter = new Shooter();
		shooter.weapons.add(cruciform);
		shooter.weapons.add(rocket);
		shooter.weapons.add(rifle);
		shooter.weapons.add(sweep);
		entity.add(shooter);
	
		final PlayerInput playerInput = new PlayerInput();
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.LEFT), cruciform);
		playerInput.actions.put(InputCode.fromButton(Input.Buttons.RIGHT), rocket);
		playerInput.actions.put(InputCode.fromKey(Input.Keys.Z), rifle);
		playerInput.actions.put(InputCode.fromKey(Input.Keys.X), sweep);
	
		if (playIntro) {
			// Don't add playerInput until intro animation is finished.
			// Move the ship up quickly.
			final LineMover mover = new LineMover();
			mover.accelerates = false;
			mover.maxVelocity = new Vector2(0, 1000);
			entity.add(mover);

			entity.add(new Velocity());
			// Move ship back towards center of screen after awhile.
			Timer.schedule(new Task() {

				@Override
				public void run() {
					mover.maxVelocity.set(0, -1000);
				}

			}, 0.75f);
			
			// Allow player to control ship and end intro animation.
			Timer.schedule(new Task() {

				@Override
				public void run() {
					entity.remove(Velocity.class);
					entity.remove(LineMover.class);
					entity.add(playerInput);
				}

			}, 1);
		} else {
			entity.add(playerInput);
		}
		
		final Health health = new Health();
		health.maxHealth = 1;
		health.currentHealth = 1;
		health.deathExploder = (deadEntity) -> explosionFactory.createRocketExplosion(deadEntity);
		entity.add(health);
	
		new Collider(entity);
		
		engine.addEntity(entity);
		
		// Create graphical player effects
		final Entity body = EffectFactory.createPlayerBody(entity, engine);
		EffectFactory.createPlayerExhaust(entity, body, engine);
		
		
		return entity;
	}
	
	public Entity createEnemy(float x, float y, EnemyTypes type) {
		Entity entity = new Entity();
		
		Renderer renderer = new Renderer(entity);
		renderer.image = ImageManager.GHOST_1;
		
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(x, y, 
				renderer.image.getRegionWidth(),
				renderer.image.getRegionHeight());
		position.yDirection = -1;
		position.outOfBoundsHandler = OutOfBoundsHandler.south();
		
		final Animator animator = new Animator(entity);
		animator.animations.put(Animator.States.IDLE,
				new Animation(0.125f, ImageManager.GHOST_1));
		animator.animations.put(Animator.States.FIRING,
				new Animation(0.125f, ImageManager.GHOST_1_FIRING));
		animator.currentAnimation = animator.animations.get(Animator.States.IDLE);
		animator.currentAnimation.setPlayMode(PlayMode.LOOP);
		
		LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(0.0f, -100.0f);
		lineMover.accelerates = false;
		entity.add(lineMover);
	
		entity.add(new Velocity());

		new Collider(entity);
	
		TeamEnemy team = new TeamEnemy();
		entity.add(team);
	
		weaponFactory.createShooter(type, entity);
	
		AI ai = new AI();
		entity.add(ai);
		
		Health health = new Health();
		health.maxHealth = 100;
		health.currentHealth = 100;
		health.deathExploder = (deadEntity) -> explosionFactory.createRocketExplosion(deadEntity);
		entity.add(health);
	
		engine.addEntity(entity);
		return entity;
	}
}
