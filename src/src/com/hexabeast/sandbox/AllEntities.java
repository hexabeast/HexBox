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
	public static int[][] itemap;
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
		itemap = new int[Map.instance.width][Map.instance.height];
		for(int i = 0; i<itemap.length; i++)
		{
			for(int j = 0; j<itemap[0].length; j++)
			{
				itemap[i][j]=-1;
			}
		}
	}
	
	public static int getType(int x,int y)
	{
		int l = getItem(x, y);
		if(l<Constants.treelimit)return treetype;
		else if(l<Constants.chestlimit)return furnituretype;
		else if(l<Constants.moblimit)return mobtype;
		return 0;
	}
	
	public static int getItem(int x,int y)
	{
		if(x<0)x = itemap.length+x;
		if(x>itemap.length-1)x = 0+(x-(itemap.length));
		if(y<0)y=0;
		if(y>itemap[0].length)y=itemap[0].length;
		return itemap[x][y];
	}
	
	public static void setMap(int x, int y, int id)
	{
		if(x<0)x = itemap.length+x;
		if(x>itemap.length-1)x = 0+(x-(itemap.length));
		itemap[x][y] = id;
		int[] change = {x,y,id};
		changeList.add(change);
	}
	
	public static void rebootMap()
	{
		for(int i = changeList.size()-1; i>=0; i--)
		{
			itemap[changeList.get(i)[0]][changeList.get(i)[1]] = -1;
			changeList.remove(i);
		}
	}
	
	public void DrawAll(SpriteBatch batch)
	{
		projectiles.DrawMagic(batch);
	}
}
