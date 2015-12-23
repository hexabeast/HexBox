package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AllItems {

	TextureRegion itemRegion;    
	TextureRegion toolRegion;  
	TextureRegion[][] itemTextureList;
	TextureRegion[][] toolList;
	public List<Item> itemListAll;
	public List<Item>[][] itemList;
	public int chunksize = 100;
	
	@SuppressWarnings("unchecked")
	public AllItems() {
		itemRegion  = new TextureRegion(TextureManager.instance.item);
		toolRegion  = new TextureRegion(TextureManager.instance.tool);
		
		itemTextureList = itemRegion.split(itemRegion.getRegionWidth()/32, itemRegion.getRegionHeight()/2);
		toolList = toolRegion.split(toolRegion.getRegionWidth()/32, toolRegion.getRegionHeight()/2);
		
		itemList = new ArrayList[Map.instance.width/chunksize][Map.instance.height/chunksize];
		for(int i = 0; i< itemList.length; i++)
		{
			for(int j = 0; j< itemList[0].length; j++)
			{
				itemList[i][j] = new ArrayList<Item>();
			}
		}
		itemListAll = new ArrayList<Item>();
	}
	
	public Item CreateItem(int id, float x, float y)
	{
		return new Item(getTextureById(id),id,x,y);
	}
	
	public void placeItem(Item item)
	{
		float x = item.getX();
		float y = item.getY();
		if(x>=Map.instance.width*16)x = x-Map.instance.width*16;
		if(x<0)x = Map.instance.width*16+x;
		if(y<0)y = 0;
		if(y>=Map.instance.height*16)y = Map.instance.height*16-16;
		
		item.setX(x);
		item.setY(y);
		//newMob.checkPoints();
		itemList[Tools.floor(x/16)/chunksize][Tools.floor(y/16)/chunksize].add(item);
		itemListAll.add(item);
	}
	
	public TextureRegion getTextureById(int id)
	{
		
		if(id<1000)
		{
			int line = (int)(id/32);
			return itemTextureList[line][id-line*32];
		}
		else
		{
			id-=1000;
			int line = (int)(id/32);
			return toolList[line][id-line*32];
		}
	}
	
	public void DrawAll(SpriteBatch batch)
	{
		
		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);

		//DEAD & DRAW
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = itemList.length+rj;
			else if(rj>=itemList.length)rj = rj-itemList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = itemList[0].length+rk;
				else if(rk>=itemList[0].length)rk = rk-itemList[0].length;
				
				for (int i = 0; i < itemList[rj][rk].size(); i++)
				{
					itemList[rj][rk].get(i).draw(batch);
				}
				
				for (int i = itemList[rj][rk].size()-1; i >= 0; i--)
			        if(itemList[rj][rk].get(i).isDead)
			        {
			        	Tools.checkItems();
			        	//TODO FAIL
			        	itemListAll.remove(itemListAll.indexOf(itemList[rj][rk].get(i)));
			        	itemList[rj][rk].remove(i);
			        }
			}
		}
		
		//CHUNK MOVE
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = itemList.length+rj;
			else if(rj>=itemList.length)rj = rj-itemList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = itemList[0].length+rk;
				else if(rk>=itemList[0].length)rk = rk-itemList[0].length;
				
				for (int i = itemList[rj][rk].size()-1; i >= 0; i--)
				{
					int rj2 = Tools.floor(itemList[rj][rk].get(i).getX()/16)/chunksize;
					if(rj2<0)rj2 = itemList.length+rj2;
					else if(rj2>=itemList.length)rj2 = rj2-itemList.length;
					
					int rk2 = Tools.floor(itemList[rj][rk].get(i).getY()/16)/chunksize;
					if(rk2<0)rk2 = itemList[0].length+rk2;
					else if(rk2>=itemList[0].length)rk2 = rk2-itemList.length;
					
					if(rj2 != rj || rk2 != rk)
					{
						itemList[rj2][rk2].add(itemList[rj][rk].get(i));
						itemList[rj][rk].remove(i);
					}
				}
			}
		}
	       
	}
	
}
