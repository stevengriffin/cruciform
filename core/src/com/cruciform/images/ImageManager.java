package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@NonNullByDefault
public class ImageManager {
	public static final TextureRegion BLANK = newTexture("blank");
	public static final TextureRegion CRUCIFORM_1 = newTexture("cruciform_weapon1");
	public static final TextureRegion CRUCIFORM_WEAPON_CROSS = newTexture("cruciform_weapon_cross");
	public static final TextureRegion PLAYER_SHIP_2 = newTexture("player_ship2");
	public static final TextureRegion PLAYER_SHIP_GOLD = newTexture("player_ship_gold");
	public static final TextureRegion PLAYER_SHIP_GOLD_BODY = newTexture("player_ship_gold_body");
	public static final TextureRegion PLAYER_SHIP_GOLD_COCKPIT = newTexture("player_ship_gold_cockpit");
	public static final TextureRegion PLAYER_EXHAUST_GLOW = newTexture("player_exhaust_glow");
	public static final TextureRegion RIFLE_BULLET = newTexture("rifle_bullet");
	public static final TextureRegion RIFLE_MUZZLE_FLASH = newTexture("rifle_muzzle_flash");
	public static final TextureRegion ENEMY_BULLET_ELONGATED = newTexture("enemy_bullet_elongated_violet");
	public static final TextureRegion ENEMY_BULLET_DIAMOND = newTexture("enemy_bullet_diamond_green");
	public static final TextureRegion ROCKET = newTexture("rocket");
	public static final TextureRegion ROCKET_FAST = newTexture("rocket_fast");
	public static final TextureRegion ROCKET_EXPLOSION = newTexture("rocket_explosion");
	public static final TextureRegion SOUL = newTexture("soul");
	public static final TextureRegion ENEMY_BLOOD = newTexture("enemy_blood");
	public static final TextureRegion UI_BG = newTexture("ui_bg");
	public static final TextureRegion BACKGROUND_LAVA = newTexture("background_lava_pixel");
	public static final TextureRegion FOREGROUND_LAVA = newTexture("foreground_lava_pixel");
	public static final TextureRegion PARALLAX_BG_LVL1_1 = newTexture("parallax_bg_lvl1_1");
	public static final TextureRegion BG_PIXEL_PARTICLE_EFFECT = newTexture("bg_pixel_particle_effect");
	public static final TextureRegion GHOST_1 = newTexture("ghost_1");
	public static final TextureRegion GHOST_1_FIRING = newTexture("ghost_1_firing");
	public static final TextureRegion[] PENTAHORROR = newTextureArrayKrita("pentahorror/pentahorror", 6);
	public static final TextureRegion[] PENTAHORROR_FIRING = newTextureArrayKrita("pentahorror_firing/pentahorror_firing", 6);
	public static final TextureRegion[] SPLITTER = newTextureArrayKrita("splitter/splitter", 8);
	public static final TextureRegion[] BURST_LAVA = newTextureArray("burst_lava", 4);
	public static final TextureRegion[] LAVA_ON_PLAYER = newTextureArray("lava_on_player", 5);
	public static final TextureRegion[] PENTAGRAM_EXPLOSION =
			newTextureArray("pentagram_explosion", 11);
	public static final TextureRegion[] PLAYER_EXPLOSION =
			newTextureArray("player_explosion", 20);
	public static final TextureRegion[] PLAYER_SHIP_EXHAUST = newTextureArray("player_ship_exhaust", 3);
	public static final TextureRegion[] PLAYER_EXHAUST_CROSS = newTextureArray("player_exhaust_cross", 3);

	private static Map<NinePatches, NinePatch> ninePatchMap = new HashMap<>();
	
	static {
		ninePatchMap.put(NinePatches.BUTTON_1, newNinePatch("button1"));
		ninePatchMap.put(NinePatches.CRUCIFORM_WEAPON_BEAM_VERTICAL,
				newNinePatch("cruciform_weapon_beam_vertical"));
		ninePatchMap.put(NinePatches.CRUCIFORM_WEAPON_BEAM_HORIZONTAL,
				newNinePatch("cruciform_weapon_beam_horizontal"));
	}

	/** fileType has dot, e.g. ".png". **/
	private static Texture instantiateTexture(String name, String fileType) {
		Texture texture = new Texture(Gdx.files.internal("images/" + name + fileType), true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		return texture;
	}
	
	/** Instantiates texture using default image fileType, ".png". **/
	private static Texture instantiateTexture(String name) {
		return instantiateTexture(name, ".png");
	}

	private static TextureRegion newTexture(String name) {
		return new TextureRegion(instantiateTexture(name));
	}

	private static TextureRegion newTexture(String name, String fileType) {
		return new TextureRegion(instantiateTexture(name, fileType));
	}
	
	private static TextureRegion[] newTextureArray(String prefix, int numberOfTextures) {
		return newTextureArrayWithOffset(prefix + "_", ".png", numberOfTextures, 1);
	}
	
	/** Krita animations are exported as name0000.tga. **/
	private static TextureRegion[] newTextureArrayKrita(String prefix, int numberOfTextures) {
		if (numberOfTextures < 10) {
			return newTextureArrayWithOffset(prefix + "000", ".tga", numberOfTextures, 0);
		} else if (numberOfTextures < 100) {
			return newTextureArrayWithOffset(prefix + "00", ".tga", numberOfTextures, 0);
		} else if (numberOfTextures < 1000) {
			return newTextureArrayWithOffset(prefix + "0", ".tga", numberOfTextures, 0);
		} else {
			return newTextureArrayWithOffset(prefix, ".tga", numberOfTextures, 0);
		}
	}
	
	private static TextureRegion[] newTextureArrayWithOffset(String prefix, String fileType, int numberOfTextures, int offset) {
		TextureRegion[] textures = new TextureRegion[numberOfTextures];
		for (int i = 0; i < numberOfTextures; i++) {
			textures[i] = newTexture(prefix + (i + offset), fileType);
		}
		return textures;
	}
	
	private static NinePatch newNinePatch(String name) {
		Texture texture = instantiateTexture(name);
		NinePatch patch = new NinePatch(texture, texture.getWidth()/2 - 2, texture.getWidth()/2 - 2,
				texture.getHeight()/2 - 2, texture.getHeight()/2 - 2);
		return patch;
	}

	public static NinePatch getPatch(NinePatches patch) {
		return ninePatchMap.get(patch);
	}

	public static void scalePatches(final float relativeScale) {
		ninePatchMap.forEach((k, patch) -> patch.scale(relativeScale, relativeScale));
	}

}
