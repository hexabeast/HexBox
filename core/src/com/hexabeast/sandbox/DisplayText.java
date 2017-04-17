package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class DisplayText {
	BitmapFont fnt;
	float x;
	float y;
	String text;
	Color color;
	float width = 800;
	boolean middle = false;

	public DisplayText(float x, float y, String text, BitmapFont fnt, boolean middled)
	{
		middle = middled;
		this.x = x;
		this.y = y;
		this.text = text;
		this.fnt = fnt;
		color = Color.WHITE;
	}
	
	public void draw(SpriteBatch batch)
	{
		fnt.setColor(color);
		if(!middle)fnt.draw(batch, text, x, y, width, Align.center,true);
		else fnt.draw(batch, text, x-width/2, y, width, Align.center,true);
		fnt.setColor(Color.WHITE);
	}
}
