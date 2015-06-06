package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hexabeast.sandbox.mobs.AllMobs;

public class AllEntities {
	public AllProjectiles projectiles;
	AllItems items;
	AllTrees trees;
	public AllMobs mobs;
	AllFurnitures furnitures;
	public static Entity[][] entitymap;
	public static List<int[]> changeList = new ArrayList<int[]>();
	public static int treetype = 1;
	public static int furnituretype = 2;
	public static int mobtype = 3;
	
	
	public AllEntities()
	{
		projectiles = new AllProjectiles();
		mobs = new AllMobs();
		items = GameScreen.items;
		trees = new AllTrees();
		furnitures = new AllFurnitures();
		entitymap = new Entity[Map.instance.width][Map.instance.height];
		/*for(int i = 0; i<itemap.length; i++)
		{
			for(int j = 0; j<itemap[0].length; j++)
			{
				itemap[i][j]=-1;
			}
		}*/
	}
	
	public static int getType(int x,int y)
	{
		Entity e = getEntity(x,y);
		if(e == null)return -1;
		else return e.entitype;
	}
	
	public static Entity getEntity(int x,int y)
	{
		if(x<0)x = entitymap.length+x;
		if(x>entitymap.length-1)x = 0+(x-(entitymap.length));
		if(y<0)y=0;
		if(y>entitymap[0].length)y=entitymap[0].length;
		return entitymap[x][y];
	}
	
	public static void setMap(int x, int y, Entity e)
	{
		if(x<0)x = entitymap.length+x;
		if(x>entitymap.length-1)x = 0+(x-(entitymap.length));
		entitymap[x][y] = e;
		int[] change = {x,y};
		changeList.add(change);
	}
	
	public static void rebootMap()
	{
		for(int i = changeList.size()-1; i>=0; i--)
		{
			entitymap[changeList.get(i)[0]][changeList.get(i)[1]] = null;
			changeList.remove(i);
		}
	}
	
	public void DrawAll(SpriteBatch batch)
	{
		projectiles.DrawMagic(batch);
	}
}
