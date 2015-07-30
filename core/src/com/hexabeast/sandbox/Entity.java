package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity {
	
	public float offx = 0;
	public boolean isTurned = false;
	public boolean isDead = false;
	public boolean detach = true;
	public boolean attach = true;
	public float width = 0;
	public float height = 0;
	
	public int entitype = 0;
	
	public float getX()
	{
		return 0;
	}
	
	public float getY()
	{
		return 0;
	}
	
	public void setX(float xii)
	{
	}
	
	public void setY(float xii)
	{
	}
	
	public void draw(SpriteBatch batch)
	{
		tpOtherSide();
	}
	
	public void tpOtherSide()
	{
		if(getX()<1000 && GameScreen.player.PNJ.x>(Map.instance.width)*8)
		{
			float xii = (Map.instance.width)*16+(getX());
			setX(xii);
		}
		if(getX()>=(Map.instance.width)*16-1000 && GameScreen.player.PNJ.x<(Map.instance.width)*8)
		{
			float xii = 0+(getX()-(Map.instance.width)*16);
			setX(xii);
		}
	}
}

