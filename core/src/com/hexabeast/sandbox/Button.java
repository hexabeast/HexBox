package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Button {

	public float x;
	public float y;
	public float w;
	public float h;
	public TextureRegion tex;
	
	public Button()
	{
		
	}
	
	public Button(float x, float y, TextureRegion tex)
	{
		this.x = x;
		this.y = y;
		this.w = tex.getRegionWidth();
		this.h = tex.getRegionHeight();
		this.tex = tex;
	}
	public Button(float x, float y, float w, float h, TextureRegion tex)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tex = tex;
	}
	
	public boolean isTouched()
	{
		Vector2 m = Tools.getAbsoluteMouse();
		if(x<m.x && x+w>m.x && y<m.y && y+h>m.y)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
	public boolean isTouched(float mx, float my)
	{
		if(x<mx && x+w>mx && y<my && y+h>my)
		{
			SoundManager.instance.click.play(1,(float) (1.1f+Math.random()/20), 0);
			return true;
		}
			
		else return false;
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(tex,x,y,w,h);
	}
}
