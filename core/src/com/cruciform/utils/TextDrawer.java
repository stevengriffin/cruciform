package com.cruciform.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextDrawer {

	private final SpriteBatch batch;
	private final BitmapFont font;
	private final GlyphLayout layout = new GlyphLayout();
	
	public TextDrawer(SpriteBatch batch, BitmapFont font) {
		this.batch = batch;
		this.font = font;
	}
	
	public void drawCentered(String text, float x, float y) {
		layout.setText(font, text);
		font.draw(batch, text, x - layout.width/2, y - layout.height/2);
	}
}
