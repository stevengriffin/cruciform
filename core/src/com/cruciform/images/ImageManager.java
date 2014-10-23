package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {
	private static Map<Picture, TextureRegion> map = new HashMap<>();
	
	static {
		map.put(Picture.CRUCIFORM_1, newTexture("cruciform_weapon1"));
		map.put(Picture.PLAYER_SHIP_1, newTexture("ship1"));
		map.put(Picture.PLAYER_SHIP_2, newTexture("player_ship2"));
		map.put(Picture.PLAYER_SHIP_GOLD, newTexture("player_ship_gold"));
		map.put(Picture.RIFLE_BULLET, newTexture("rifle_bullet"));
		map.put(Picture.ENEMY_BULLET_ELONGATED, newTexture("enemy_bullet_elongated_1"));
		map.put(Picture.ROCKET, newTexture("rocket"));
		map.put(Picture.ROCKET_FAST, newTexture("rocket_fast"));
		map.put(Picture.ROCKET_EXPLOSION, newTexture("rocket_explosion"));
		map.put(Picture.SIDE_PANEL, newTexture("side_panel"));
		map.put(Picture.BOTTOM_PANEL, newTexture("bottom_panel"));
		map.put(Picture.WEAPONS_PANEL, newTexture("weapons_panel"));
	}

	private static Map<NinePatches, NinePatch> ninePatchMap = new HashMap<>();
	
	static {
		ninePatchMap.put(NinePatches.BUTTON_1, newNinePatch("button1"));
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
}
