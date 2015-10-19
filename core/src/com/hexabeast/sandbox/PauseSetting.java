package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PauseSetting {
	float x;
	float y;
	String title = "";
	String value = "";
	String description = "";
	float wArrow = 0;
	float hArrow = 0;
	float hfont = 0;
	
	PauseSetting(float x, float y, String title, String description)
	{
		this.x = x;
		this.y = y;
		this.title = title;
		this.description = description;
		wArrow = TextureManager.instance.pauseArrowL.getRegionWidth()*4;
		hArrow = TextureManager.instance.pauseArrowL.getRegionHeight()*4;
		this.x-=(1900+wArrow*2)/2;
		hfont = FontManager.instance.fontSettings.getXHeight();
	}
	
	public void draw(SpriteBatch batch)
	{
		FontManager.instance.fontSettings.setColor(batch.getColor());
		FontManager.instance.fontSettings.drawWrapped(batch, title, x, y+hArrow/2+hfont/2, 430, BitmapFont.HAlignment.RIGHT);
		batch.draw(TextureManager.instance.pauseArrowL, x+440, y, wArrow , hArrow);
		FontManager.instance.fontSettings.drawWrapped(batch, value, x+450+wArrow, y+hArrow/2+hfont/2, 430, BitmapFont.HAlignment.CENTER);
		batch.draw(TextureManager.instance.pauseArrowR, x+890+wArrow, y, wArrow , hArrow);
		FontManager.instance.fontSettings.drawWrapped(batch, description, x+900+wArrow*2, y+hArrow-12, 1000, BitmapFont.HAlignment.LEFT);
		FontManager.instance.fontSettings.setColor(Color.WHITE);
	}
	
	public boolean isTouchedLeft()
	{
		Vector2 m = Tools.getAbsoluteMouse();
		if(x+440<m.x && x+440+wArrow>m.x && y<m.y && y+hArrow>m.y)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
	public boolean isTouchedLeft(float mx, float my)
	{
		if(x+440<mx && x+440+wArrow>mx && y<my && y+hArrow>my)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
	public boolean isTouchedRight()
	{
		Vector2 m = Tools.getAbsoluteMouse();
		if(x+890+wArrow<m.x && x+890+wArrow*2>m.x && y<m.y && y+hArrow>m.y)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
	public boolean isTouchedRight(float mx, float my)
	{
		if(x+890+wArrow<mx && x+890+wArrow*2>mx && y<my && y+hArrow>my)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
}
