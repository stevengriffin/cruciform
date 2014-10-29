package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {
	private static final Map<Picture, TextureRegion> map = new HashMap<>();
	
	static {
		// TODO refactor to simple TextureRegion fields
		map.put(Picture.BLANK, newTexture("blank"));
		map.put(Picture.CRUCIFORM_1, newTexture("cruciform_weapon1"));
		map.put(Picture.CRUCIFORM_WEAPON_CROSS, newTexture("cruciform_weapon_cross"));
		map.put(Picture.PLAYER_SHIP_1, newTexture("ship1"));
		map.put(Picture.PLAYER_SHIP_2, newTexture("player_ship2"));
		map.put(Picture.PLAYER_SHIP_GOLD, newTexture("player_ship_gold"));
		map.put(Picture.PLAYER_SHIP_GOLD_BODY, newTexture("player_ship_gold_body"));
		map.put(Picture.PLAYER_SHIP_GOLD_COCKPIT, newTexture("player_ship_gold_cockpit"));
		map.put(Picture.PLAYER_SHIP_EXHAUST_1, newTexture("player_ship_exhaust_1"));
		map.put(Picture.PLAYER_SHIP_EXHAUST_2, newTexture("player_ship_exhaust_2"));
		map.put(Picture.PLAYER_SHIP_EXHAUST_3, newTexture("player_ship_exhaust_3"));
		map.put(Picture.PLAYER_EXHAUST_CROSS, newTexture("player_exhaust_cross"));
		map.put(Picture.PLAYER_EXHAUST_CROSS_2, newTexture("player_exhaust_cross_2"));
		map.put(Picture.PLAYER_EXHAUST_CROSS_3, newTexture("player_exhaust_cross_3"));
		map.put(Picture.PLAYER_EXHAUST_CROSS_4, newTexture("player_exhaust_cross_4"));
		map.put(Picture.PLAYER_EXHAUST_GLOW, newTexture("player_exhaust_glow"));
		map.put(Picture.RIFLE_BULLET, newTexture("rifle_bullet"));
		map.put(Picture.RIFLE_MUZZLE_FLASH, newTexture("rifle_muzzle_flash"));
		map.put(Picture.ENEMY_BULLET_ELONGATED, newTexture("enemy_bullet_elongated_violet"));
		map.put(Picture.ROCKET, newTexture("rocket"));
		map.put(Picture.ROCKET_FAST, newTexture("rocket_fast"));
		map.put(Picture.ROCKET_EXPLOSION, newTexture("rocket_explosion"));
		map.put(Picture.SOUL, newTexture("soul"));
		map.put(Picture.SIDE_PANEL, newTexture("side_panel"));
		map.put(Picture.BOTTOM_PANEL, newTexture("bottom_panel"));
		map.put(Picture.WEAPONS_PANEL, newTexture("weapons_panel"));
		map.put(Picture.BACKGROUND_LAVA, newTexture("background_lava_pixel"));
		map.put(Picture.FOREGROUND_LAVA, newTexture("foreground_lava_pixel"));
		map.put(Picture.BURST_LAVA_1, newTexture("burst_lava_1"));
		map.put(Picture.BURST_LAVA_2, newTexture("burst_lava_2"));
		map.put(Picture.BURST_LAVA_3, newTexture("burst_lava_3"));
		map.put(Picture.BURST_LAVA_4, newTexture("burst_lava_4"));
		map.put(Picture.LAVA_ON_PLAYER_1, newTexture("lava_on_player_1"));
		map.put(Picture.LAVA_ON_PLAYER_2, newTexture("lava_on_player_2"));
		map.put(Picture.LAVA_ON_PLAYER_3, newTexture("lava_on_player_3"));
		map.put(Picture.LAVA_ON_PLAYER_4, newTexture("lava_on_player_4"));
		map.put(Picture.LAVA_ON_PLAYER_5, newTexture("lava_on_player_5"));
		map.put(Picture.PARALLAX_BG_LVL1_1, newTexture("parallax_bg_lvl1_1"));
		map.put(Picture.BG_PIXEL_PARTICLE_EFFECT, newTexture("bg_pixel_particle_effect"));
	}

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
	
	public static TextureRegion get(Picture picture) {
		return map.get(picture);
	}
	
	public static void scalePatches(final float relativeScale) {
		ninePatchMap.forEach((k, patch) -> patch.scale(relativeScale, relativeScale));
	}
	
}
