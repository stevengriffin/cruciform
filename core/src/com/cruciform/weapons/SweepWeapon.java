package com.cruciform.weapons;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.LineMover;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Velocity;
import com.cruciform.components.team.Team;
import com.cruciform.components.team.TeamPlayer;
import com.cruciform.factories.ExplosionFactory;
import com.cruciform.utils.Conf;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.OutOfBoundsHandler;
import com.cruciform.utils.Priority;

public class SweepWeapon extends Weapon {

	private final ExplosionFactory explosionFactory;
	public float volume = 0.2f;
	public float sweepSpeed = 800.0f;
	public float widthMax = 32*4;
	public float damageNumerator = 1600.0f;
	public int shotGrowthMultiplier = 1;
	private Entity lastSweepFired = null;
	
	public SweepWeapon(final float coolDownTime, final Engine engine, final ExplosionFactory explosionFactory,
			final Class<? extends Team> team) {
		super(coolDownTime, engine, team, "Sweep");
		this.explosionFactory = explosionFactory;
	}

	// TODO: Are these accurate?
	private final static int BOTTOM_LEFT = 6;
	private final static int BOTTOM_RIGHT = 4;
	private final static int TOP_LEFT = 0;
	private final static int TOP_RIGHT = 2;
	private float xChangeSpeed = sweepSpeed/10;
	
	@Override
	void handleUpdate(final float dt, final Position firerPos) {
		if (lastSweepFired != null) {
			final Position position = Position.mapper.get(lastSweepFired);
			float[] vertices = position.bounds.getVertices();
			vertices[BOTTOM_LEFT] += dt*xChangeSpeed*shotGrowthMultiplier;
			vertices[BOTTOM_RIGHT] -= dt*xChangeSpeed*shotGrowthMultiplier;
			vertices[TOP_LEFT] += dt*xChangeSpeed*shotGrowthMultiplier;
			vertices[TOP_RIGHT] -= dt*xChangeSpeed*shotGrowthMultiplier;
			//Log.debug("width bottom: " + (vertices[BOTTOM_RIGHT] - vertices[BOTTOM_LEFT]));
			//Log.debug("width top: " + (vertices[TOP_RIGHT] - vertices[TOP_LEFT]));
			position.bounds.setVertices(vertices);
			final Damager damager = Damager.mapper.get(lastSweepFired);
			damager.damage = damageNumerator/position.bounds.getBoundingRectangle().width;
		}
	}

	@Override
	void handleFire(final Position firerPos) {
		shotGrowthMultiplier = 1;
		final Entity entity = new Entity();

		final Renderer renderer = new Renderer(entity);
		renderer.image = null;
		renderer.renderAsShape = true;
		if (team == TeamPlayer.class) {
			// Render underneath player ship.
			renderer.priority = new Priority(-1);
		}
		
		final Position position = new Position(entity);
		position.bounds = Geometry.polyTrapezoid(
				firerPos.bounds.getX(), 
				firerPos.bounds.getY(), 
				widthMax/16,
				0,
				32);
		position.outOfBoundsHandler = OutOfBoundsHandler.all();
		
		final Velocity velocity = new Velocity();
		entity.add(velocity);
		
		final LineMover lineMover = new LineMover();
		lineMover.maxVelocity = new Vector2(0, sweepSpeed);
		lineMover.accelerates = false;
		entity.add(lineMover);
	
		final SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.RIFLE_BULLET);
		soundEffect.id = soundEffect.sound.play(0.2f * Conf.volume);
		entity.add(soundEffect);

		Collider.defaultForProjectile(entity, team);
		
		final Damager damager = new Damager();
		damager.damage = 25.0f;
		entity.add(damager);
		
		engine.addEntity(entity);
		lastSweepFired = entity;
	}
	
	@Override
	public void setFiring(boolean shouldFire) {
		super.setFiring(shouldFire);
		if (!shouldFire) {
			// Reverse the growth of the shot
			shotGrowthMultiplier = -1;
		}
	}
	
}
