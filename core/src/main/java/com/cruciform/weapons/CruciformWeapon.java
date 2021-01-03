package com.cruciform.weapons;



import com.badlogicmods.ashley.core.Engine;
import com.badlogicmods.ashley.core.Entity;
import com.cruciform.audio.AudioManager;
import com.cruciform.audio.Noise;
import com.cruciform.components.Collider;
import com.cruciform.components.Damager;
import com.cruciform.components.Fader;
import com.cruciform.components.Lifetime;
import com.cruciform.components.Position;
import com.cruciform.components.Renderer;
import com.cruciform.components.SoundEffect;
import com.cruciform.components.Splitter;
import com.cruciform.components.team.Team;
import com.cruciform.images.ImageManager;
import com.cruciform.images.NinePatches;
import com.cruciform.utils.Conf;
import com.cruciform.utils.EntityMutator;
import com.cruciform.utils.Geometry;
import com.cruciform.utils.Priority;
import org.jetbrains.annotations.NotNull;

public class CruciformWeapon extends Weapon {

	private final static float COOL_DOWN_TIME = 3.0f;
	@NotNull private final static CruciformSplitBehavior CRUCIFORM_SPLIT_BEHAVIOR = new CruciformSplitBehavior();
	private final static float BEAM_WIDTH = 8.0f;
	private final static float BEAM_HEIGHT = Conf.canonicalPlayWidth*2;
	
	public CruciformWeapon(@NotNull final Engine engine, final Class<? extends Team> team) {
		super(COOL_DOWN_TIME, engine, team, 50.0f, "Cruciform");
	}

	@Override
	void handleUpdate(final float dt, final Position firerPos) {
		
	}

	@Override
	void handleFire(final Position firerPos) {
		createCruciform(firerPos, false, true);
	}
	
	private static class CruciformSplitBehavior implements EntityMutator {

		@Override
		public Entity mutate(Entity entity, final int index) {
			final Splitter splitter = Splitter.mapper.getSafe(entity);
			final Position position = Position.mapper.getSafe(entity);
			final Renderer renderer = Renderer.mapper.getSafe(entity);
			renderer.image = ImageManager.CRUCIFORM_WEAPON_CROSS;
			renderer.customOffset = true;
			renderer.customXOffset = ImageManager.CRUCIFORM_WEAPON_CROSS.getRegionWidth()/2;
			renderer.customYOffset = -ImageManager.CRUCIFORM_WEAPON_CROSS.getRegionHeight()/2;
			renderer.priority = new Priority(-1);
			renderer.patch = ImageManager.getPatch(NinePatches.CRUCIFORM_WEAPON_BEAM_HORIZONTAL);
			position.bounds.rotate(90.0f);
			position.bounds.setPosition(position.bounds.getX(), splitter.collisionY);
			entity.remove(Splitter.class);
			return entity;
		}
		
	}
	
	private void createCruciform(final Position firerPos, final boolean isHorizontal,
			final boolean splits) {
		Entity entity = new Entity();
		
		Position position = new Position(entity);
		position.bounds = Geometry.polyRect(
				firerPos.bounds.getX(), 
				firerPos.bounds.getY() + BEAM_HEIGHT/2, 
				BEAM_WIDTH, 
				BEAM_HEIGHT);
		
		Renderer renderer = new Renderer(entity);
		renderer.patch = ImageManager.getPatch(NinePatches.CRUCIFORM_WEAPON_BEAM_VERTICAL);
		// Render underneath cross and player ship.
		renderer.priority = new Priority(-2);
		
		SoundEffect soundEffect = new SoundEffect();
		soundEffect.sound = AudioManager.get(Noise.CRUCIFORM);
		soundEffect.id = soundEffect.sound.play(1.0f * Conf.volume);
		entity.add(soundEffect);
		
		Collider.defaultForProjectile(entity, team);
		
		Damager damager = new Damager();
		damager.damage = damage;
		entity.add(damager);
	
		Lifetime lifetime = new Lifetime(entity);
		lifetime.setTimeRemaining(1.5f);
		
		Splitter splitter = new Splitter();
		splitter.componentsToRemoveFromChildren.add(SoundEffect.class);
		splitter.numberOfNewEntities = 1;
		splitter.customSplitBehavior = CRUCIFORM_SPLIT_BEHAVIOR;
		entity.add(splitter);
	
		new Fader(entity);
		
		engine.addEntity(entity);
	}

}
