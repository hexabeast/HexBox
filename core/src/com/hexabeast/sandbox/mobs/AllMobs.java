package com.hexabeast.sandbox.mobs;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hexabeast.sandbox.AllEntities;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.Map;
import com.hexabeast.sandbox.Tools;

public class AllMobs {
	public List<Mob> mobListAll;
	public List<Mob>[][] mobList;
	
	public int chunksize = 100;
	
	@SuppressWarnings("unchecked")
	public AllMobs() 
	{
		mobList = new ArrayList[Map.instance.width/chunksize][Map.instance.height/chunksize];
		for(int i = 0; i< mobList.length; i++)
		{
			for(int j = 0; j< mobList[0].length; j++)
			{
				mobList[i][j] = new ArrayList<Mob>();
			}
		}
		mobListAll = new ArrayList<Mob>();
	}
	
	public void placeMob(float x, float y, int type)
	{
		if(x>=Map.instance.width*16)x = x-Map.instance.width*16;
		if(x<0)x = Map.instance.width*16+x;
		if(y<0)y = 0;
		if(y>=Map.instance.height*16)y = Map.instance.height*16-16;
		
		Mob newMob;
		if(type==1)newMob = new BigInsecte();
		else if(type==3)newMob = new Hornet();
		else newMob = new Wolf();
		newMob.x = x;
		newMob.y = y;
		newMob.entitype = AllEntities.mobtype;
		//newMob.checkPoints();
		mobList[Tools.floor(x/16)/chunksize][Tools.floor(y/16)/chunksize].add(newMob);
		mobListAll.add(newMob);
	}

	public void checkIDs()
	{
		for(int i = 0; i<mobListAll.size(); i++)
		{
			mobListAll.get(i).id = i;
		}
	}
	
	public ArrayList<Mob> mobsToTest()
	{
		ArrayList<Mob> r = new ArrayList<Mob>();
		
		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);
		
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = mobList.length+rj;
			else if(rj>=mobList.length)rj = rj-mobList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = mobList[0].length+rk;
				else if(rk>=mobList[0].length)rk = rk-mobList.length;
				
				for (int i = 0; i < mobList[rj][rk].size(); i++)
				{
					r.add(mobList[rj][rk].get(i));
				}
			}
		}
		return r;
	}
	
	public void checkPoints()
	{
		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);
		
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = mobList.length+rj;
			else if(rj>=mobList.length)rj = rj-mobList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = mobList[0].length+rk;
				else if(rk>=mobList[0].length)rk = rk-mobList.length;
				
				for (int i = mobList[rj][rk].size()-1; i >= 0; i--)
			        if(mobList[rj][rk].get(i).isDead)
			        {
			        	Tools.checkItems();
			        	mobListAll.remove(mobListAll.indexOf(mobList[rj][rk].get(i)));
			        	mobList[rj][rk].remove(i);
			        	checkIDs();
			        }
				
				for (int i = 0; i < mobList[rj][rk].size(); i++)
				{
					if(!mobList[rj][rk].get(i).isDead)mobList[rj][rk].get(i).checkPoints();
				}
			}
		}
	}
	
	public void DrawAll(SpriteBatch batch)
	{

		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);

		//DRAW
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = mobList.length+rj;
			else if(rj>=mobList.length)rj = rj-mobList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = mobList[0].length+rk;
				else if(rk>=mobList[0].length)rk = rk-mobList.length;
				
				for (int i = 0; i < mobList[rj][rk].size(); i++)
				{
					mobList[rj][rk].get(i).update();
					mobList[rj][rk].get(i).draw(batch);
					mobList[rj][rk].get(i).superDraw(batch);
				}
			}
		}
		
		//CHUNK MOVE
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = mobList.length+rj;
			else if(rj>=mobList.length)rj = rj-mobList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = mobList[0].length+rk;
				else if(rk>=mobList[0].length)rk = rk-mobList.length;
				
				for (int i = mobList[rj][rk].size()-1; i >= 0; i--)
				{
					int rj2 = Tools.floor(mobList[rj][rk].get(i).getX()/16)/chunksize;
					if(rj2<0)rj2 = mobList.length+rj2;
					else if(rj2>=mobList.length)rj2 = rj2-mobList.length;
					
					int rk2 = Tools.floor(mobList[rj][rk].get(i).getY()/16)/chunksize;
					if(rk2<0)rk2 = mobList[0].length+rk2;
					else if(rk2>=mobList[0].length)rk2 = rk2-mobList.length;
					
					if(rj2 != rj || rk2 != rk)
					{
						mobList[rj2][rk2].add(mobList[rj][rk].get(i));
						mobList[rj][rk].remove(i);
					}
				}
			}
		}
	}
}
