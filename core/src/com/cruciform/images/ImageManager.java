package com.cruciform.images;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ImageManager {
	private static Map<Picture, Texture> map = new HashMap<>();
	
	static {
		map.put(Picture.CRUCIFORM_1, newTexture("cruciform_weapon1"));
		map.put(Picture.PLAYER_SHIP_1, newTexture("ship1"));
		map.put(Picture.PLAYER_SHIP_2, newTexture("player_ship2"));
		map.put(Picture.RIFLE_BULLET, newTexture("rifle_bullet"));
		map.put(Picture.ROCKET, newTexture("rocket"));
		map.put(Picture.ROCKET_FAST, newTexture("rocket_fast"));
		map.put(Picture.ROCKET_EXPLOSION, newTexture("rocket_explosion"));
	}
	
	private static Texture newTexture(String name) {
		return new Texture(Gdx.files.internal("images/" + name + ".png"));
	}
	
	public static Texture get(Picture picture) {
		return map.get(picture);
	}
}
