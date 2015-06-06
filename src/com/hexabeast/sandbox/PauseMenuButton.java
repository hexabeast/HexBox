package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PauseMenuButton {
	public TextureRegion texture;
	public float x = 0;
	public float y = 0;
	public float w = 0;
	public float h = 0;
	PauseMenuButton(TextureRegion texture, float x, float y)
	{
		this.texture = texture;
		this.x = x-texture.getRegionWidth();
		this.y = y-texture.getRegionHeight();
		this.w = texture.getRegionWidth()*2;
		this.h = texture.getRegionHeight()*2;
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(texture,x,y,w,h);
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
}
