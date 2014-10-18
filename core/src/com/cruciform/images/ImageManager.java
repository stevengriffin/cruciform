package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {
	private static Map<Picture, TextureRegion> map = new HashMap<>();
	
	static {
		map.put(Picture.CRUCIFORM_1, newTexture("cruciform_weapon1"));
		map.put(Picture.PLAYER_SHIP_1, newTexture("ship1"));
		map.put(Picture.PLAYER_SHIP_2, newTexture("player_ship2"));
		map.put(Picture.RIFLE_BULLET, newTexture("rifle_bullet"));
		map.put(Picture.ROCKET, newTexture("rocket"));
		map.put(Picture.ROCKET_FAST, newTexture("rocket_fast"));
		map.put(Picture.ROCKET_EXPLOSION, newTexture("rocket_explosion"));
		map.put(Picture.SIDE_PANEL, newTexture("side_panel"));
	}
	
	private static TextureRegion newTexture(String name) {
		Texture texture =  new Texture(Gdx.files.internal("images/" + name + ".png"), true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture);
		return region;
	}
	
	public static TextureRegion get(Picture picture) {
		return map.get(picture);
	}
}
