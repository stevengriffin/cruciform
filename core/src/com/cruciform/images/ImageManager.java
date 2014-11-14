package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {
	public static final TextureRegion BLANK = newTexture("blank");
	public static final TextureRegion CRUCIFORM_1 = newTexture("cruciform_weapon1");
	public static final TextureRegion CRUCIFORM_WEAPON_CROSS = newTexture("cruciform_weapon_cross");
	public static final TextureRegion PLAYER_SHIP_1 = newTexture("ship1");
	public static final TextureRegion PLAYER_SHIP_2 = newTexture("player_ship2");
	public static final TextureRegion PLAYER_SHIP_GOLD = newTexture("player_ship_gold");
	public static final TextureRegion PLAYER_SHIP_GOLD_BODY = newTexture("player_ship_gold_body");
	public static final TextureRegion PLAYER_SHIP_GOLD_COCKPIT = newTexture("player_ship_gold_cockpit");
	public static final TextureRegion PLAYER_SHIP_EXHAUST_1 = newTexture("player_ship_exhaust_1");
	public static final TextureRegion PLAYER_SHIP_EXHAUST_2 = newTexture("player_ship_exhaust_2");
	public static final TextureRegion PLAYER_SHIP_EXHAUST_3 = newTexture("player_ship_exhaust_3");
	public static final TextureRegion PLAYER_EXHAUST_CROSS = newTexture("player_exhaust_cross");
	public static final TextureRegion PLAYER_EXHAUST_CROSS_2 = newTexture("player_exhaust_cross_2");
	public static final TextureRegion PLAYER_EXHAUST_CROSS_3 = newTexture("player_exhaust_cross_3");
	public static final TextureRegion PLAYER_EXHAUST_CROSS_4 = newTexture("player_exhaust_cross_4");
	public static final TextureRegion PLAYER_EXHAUST_GLOW = newTexture("player_exhaust_glow");
	public static final TextureRegion RIFLE_BULLET = newTexture("rifle_bullet");
	public static final TextureRegion RIFLE_MUZZLE_FLASH = newTexture("rifle_muzzle_flash");
	public static final TextureRegion ENEMY_BULLET_ELONGATED = newTexture("enemy_bullet_elongated_violet");
	public static final TextureRegion ROCKET = newTexture("rocket");
	public static final TextureRegion ROCKET_FAST = newTexture("rocket_fast");
	public static final TextureRegion ROCKET_EXPLOSION = newTexture("rocket_explosion");
	public static final TextureRegion SOUL = newTexture("soul");
	public static final TextureRegion SIDE_PANEL = newTexture("side_panel");
	public static final TextureRegion BOTTOM_PANEL = newTexture("bottom_panel");
	public static final TextureRegion WEAPONS_PANEL = newTexture("weapons_panel");
	public static final TextureRegion BACKGROUND_LAVA = newTexture("background_lava_pixel");
	public static final TextureRegion FOREGROUND_LAVA = newTexture("foreground_lava_pixel");
	public static final TextureRegion BURST_LAVA_1 = newTexture("burst_lava_1");
	public static final TextureRegion BURST_LAVA_2 = newTexture("burst_lava_2");
	public static final TextureRegion BURST_LAVA_3 = newTexture("burst_lava_3");
	public static final TextureRegion BURST_LAVA_4 = newTexture("burst_lava_4");
	public static final TextureRegion LAVA_ON_PLAYER_1 = newTexture("lava_on_player_1");
	public static final TextureRegion LAVA_ON_PLAYER_2 = newTexture("lava_on_player_2");
	public static final TextureRegion LAVA_ON_PLAYER_3 = newTexture("lava_on_player_3");
	public static final TextureRegion LAVA_ON_PLAYER_4 = newTexture("lava_on_player_4");
	public static final TextureRegion LAVA_ON_PLAYER_5 = newTexture("lava_on_player_5");
	public static final TextureRegion PARALLAX_BG_LVL1_1 = newTexture("parallax_bg_lvl1_1");
	public static final TextureRegion BG_PIXEL_PARTICLE_EFFECT = newTexture("bg_pixel_particle_effect");
	public static final TextureRegion GHOST_1 = newTexture("ghost_1");

	private static Map<NinePatches, NinePatch> ninePatchMap = new HashMap<>();

	static {
		ninePatchMap.put(NinePatches.BUTTON_1, newNinePatch("button1"));
		ninePatchMap.put(NinePatches.CRUCIFORM_WEAPON_BEAM_VERTICAL,
				newNinePatch("cruciform_weapon_beam_vertical"));
		ninePatchMap.put(NinePatches.CRUCIFORM_WEAPON_BEAM_HORIZONTAL,
				newNinePatch("cruciform_weapon_beam_horizontal"));
	}

	private static Texture instantiateTexture(String name) {
		Texture texture = new Texture(Gdx.files.internal("images/" + name + ".png"), true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		return texture;
	}

	private static TextureRegion newTexture(String name) {
		return new TextureRegion(instantiateTexture(name));
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
