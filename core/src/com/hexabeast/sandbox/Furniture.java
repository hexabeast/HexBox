package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Furniture extends Entity{
	public int x;
	public int y;
	public int casesX;
	public int casesY;
	public int type = 0;
	public TextureRegion tex;
	public boolean container = false;
	public int[] itemsids;
	public int[] itemsnumbers;
	public int id;
	
	public Furniture(int x,int y, int type)
	{
		this.x = x;
		this.y = y;
		this.casesX = AllTools.instance.getType(type).furnitureWidth;
		this.casesY = AllTools.instance.getType(type).furnitureHeight;
		
		this.type = type;
		
		container = AllTools.instance.getType(type).furnitureContainer;
		
		tex = AllTools.instance.getType(type).tex;
		
		if(container)
		{
			itemsids = new int[60];
			itemsnumbers = new int[60];
		}
	}
	
	public boolean isEmpty()
	{
		if(!container)return true;
		else
		{
			for(int i = 0; i<itemsnumbers.length; i++)if(itemsnumbers[i]>0)return false;
				
			return true;
		}
		
	}
	
	public void checkPoints()
	{
		for(int i = 0; i<casesX; i++)
		{
			for(int j = -1; j<casesY; j++)
			{
				AllEntities.setMap(x+i,y+j,this);
			}
		}
	}
	
	@Override
	public float getX()
	{
		return x*16;
	}
	@Override
	public float getY()
	{
		 return y*16;
	}
	@Override
	public void setX(float xii)
	{
		x = (int)(xii/16);
	}
	@Override
	public void setY(float xii)
	{
		y = (int)(xii/16);
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		//superDraw(batch);
		
		for(int i = 0; i<casesX; i++)
		{
			if(!Map.instance.mainLayer.getBloc(x+i, y-1).collide && !isDead)
			{
				GameScreen.items.placeItem(GameScreen.items.CreateItem(type,x*16+casesX*8,y*16+casesY*8));
				isDead = true;
			}
		}
		Vector3 color = Tools.getShadowColor(x+(int)(casesX/2),y+(int)(casesY/2));
		batch.setColor(color.x,color.y,color.z,1);
		if(!isTurned)batch.draw(tex, x*16, y*16-2, casesX * 16, casesY*16);
		else batch.draw(tex, x*16+casesX * 16, y*16-2, -casesX * 16, casesY*16);
		batch.setColor(1,1,1,1);
	}
	
	public boolean isClicked(float x,float y)
	{
		if(((int)(x/16) >= this.x && (int)(x/16) < this.x+casesX) && ((int)(y/16) >= this.y && (int)(y/16) < this.y+casesY))return true;
		else return false;
	}
	
	public void superDraw(SpriteBatch batch)
	{
		super.draw(batch);
	}

}
