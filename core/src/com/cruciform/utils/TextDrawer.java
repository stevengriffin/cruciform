package com.cruciform.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextDrawer {

	private final SpriteBatch batch;
	private final BitmapFont font;
	
	public TextDrawer(SpriteBatch batch, BitmapFont font) {
		this.batch = batch;
		this.font = font;
	}
	
	public void drawCentered(String text, float x, float y) {
		TextBounds bounds = font.getBounds(text);
		font.draw(batch, text, x - bounds.width/2, y - bounds.height/2);
	}
}
