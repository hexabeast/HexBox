package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AllFurnitures {
	
	public List<Furniture>[][] furnitureList;
	public List<Furniture> furnitureListAll;
	
	public int chunksize = 100; 
	
	@SuppressWarnings("unchecked")
	public AllFurnitures() 
	{
		furnitureList = new ArrayList[Map.instance.width/chunksize][Map.instance.height/chunksize];
		for(int i = 0; i< furnitureList.length; i++)
		{
			for(int j = 0; j< furnitureList[0].length; j++)
			{
				furnitureList[i][j] = new ArrayList<Furniture>();
			}
		}
		furnitureListAll = new ArrayList<Furniture>();
	}
	
	public void addFurniture(int x, int y, int type)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = Map.instance.width+x;
		if(y<0)y = 0;
		if(y>=Map.instance.height)y = Map.instance.height-1;
		
		Furniture fur = new Furniture(x,y,type);
		fur.entitype = AllEntities.furnituretype;
		if(AllTools.instance.getType(type).turnable)
		{
			fur.isTurned = GameScreen.player.PNJ.isTurned;
		}
		fur.checkPoints();
		furnitureList[x/chunksize][y/chunksize].add(fur);
		furnitureListAll.add(fur);
	}
	
	public void checkPoints()
	{
		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);
		
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = furnitureList.length+rj;
			else if(rj>=furnitureList.length)rj = rj-furnitureList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = furnitureList[0].length+rk;
				else if(rk>=furnitureList[0].length)rk = rk-furnitureList[0].length;
				
				for (int i = furnitureList[rj][rk].size()-1; i >=0; i--)
				{
					if(!furnitureList[rj][rk].get(i).isDead)furnitureList[rj][rk].get(i).checkPoints();
				}
			}
		}
	}
	
	public void addFurniture(int x, int y, int type, int[] ids, int[] numbers, boolean turned)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = Map.instance.width+x;
		if(y<0)y = 0;
		if(y>=Map.instance.height)y = Map.instance.height-1;
		
		Furniture fur = new Furniture(x,y,type);
		
		fur.entitype = AllEntities.furnituretype;
		if(fur.container)
		{
			fur.itemsids = ids;
			fur.itemsnumbers = numbers;
		}
		fur.isTurned = turned;
		fur.checkPoints();
		furnitureList[x/chunksize][y/chunksize].add(fur);
		furnitureListAll.add(fur);
	}

	
	public void DrawAll(SpriteBatch batch)
	{
		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);
		
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = furnitureList.length+rj;
			else if(rj>=furnitureList.length)rj = rj-furnitureList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = furnitureList[0].length+rk;
				else if(rk>=furnitureList[0].length)rk = rk-furnitureList[0].length;
				
				for (int i = furnitureList[rj][rk].size()-1; i >=0; i--)
				{
					furnitureList[rj][rk].get(i).superDraw(batch);
					if(Math.abs(furnitureList[rj][rk].get(i).x*16-GameScreen.camera.position.x)<GameScreen.camera.viewportWidth*0.8f && Math.abs(furnitureList[rj][rk].get(i).y*16-GameScreen.camera.position.y)<GameScreen.camera.viewportHeight)furnitureList[rj][rk].get(i).draw(batch);
					if(furnitureList[rj][rk].get(i).isDead)
			        {
						if(!furnitureList[rj][rk].get(i).isEmpty())
						{
							for(int w = 0; w<furnitureList[rj][rk].get(i).itemsnumbers.length; w++)
							{
								Item it = GameScreen.items.CreateItem(furnitureList[rj][rk].get(i).itemsids[w],furnitureList[rj][rk].get(i).x*16+furnitureList[rj][rk].get(i).casesX*8,furnitureList[rj][rk].get(i).y*16+furnitureList[rj][rk].get(i).casesY*8);
								it.number = furnitureList[rj][rk].get(i).itemsnumbers[w];
								GameScreen.items.placeItem(it);
							}
						}
						
			        	Tools.checkItems();
			        	furnitureListAll.remove(furnitureList[rj][rk].get(i));
			        	furnitureList[rj][rk].remove(i);
			        }
				}
			}
		}
		
		
	}
}
