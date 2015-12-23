package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AllTrees {

	public List<Tree> treeListAll;
	public List<Tree>[][] treeList;
	public TextureRegion[] troncs,  branchesD, branchesG, cimes;
	public TextureRegion pousse;
	
	public int chunksize = 100; 
	
	@SuppressWarnings("unchecked")
	public AllTrees() 
	{
		treeList = new ArrayList[Map.instance.width/chunksize][Map.instance.height/chunksize];
		for(int i = 0; i< treeList.length; i++)
		{
			for(int j = 0; j< treeList[0].length; j++)
			{
				treeList[i][j] = new ArrayList<Tree>();
			}
		}
		treeListAll = new ArrayList<Tree>();
		
		troncs = new TextureRegion(TextureManager.instance.Ttroncs).split(TextureManager.instance.Ttroncs.getRegionWidth()/4, TextureManager.instance.Ttroncs.getRegionHeight())[0];
		branchesD = new TextureRegion(TextureManager.instance.TbranchesD).split(TextureManager.instance.TbranchesD.getRegionWidth()/4, TextureManager.instance.TbranchesD.getRegionHeight())[0];
		branchesG = new TextureRegion(TextureManager.instance.TbranchesG).split(TextureManager.instance.TbranchesG.getRegionWidth()/4, TextureManager.instance.TbranchesG.getRegionHeight())[0];
		cimes = new TextureRegion(TextureManager.instance.Tcimes).split(TextureManager.instance.Tcimes.getRegionWidth(), TextureManager.instance.Tcimes.getRegionHeight())[0];
		pousse = GameScreen.items.getTextureById(AllTools.instance.StdTreeId);
	}
	
	public void PlaceTree(int x, int y, int seed, float ectime)
	{
		
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = Map.instance.width+x;
		if(y<0)y = 0;
		if(y>=Map.instance.height)y = Map.instance.height-1;
		
		Tree newTree = new Tree(x,y,pousse,troncs,  branchesD, branchesG, cimes,seed,ectime);
		newTree.SetPosition(x, y);
		newTree.id = treeListAll.size();
		newTree.entitype = AllEntities.treetype;
		newTree.checkPoints();
		treeList[x/chunksize][y/chunksize].add(newTree);
		treeListAll.add(newTree);
	}
	
	public void checkIDs()
	{
		for(int i = 0; i<treeListAll.size(); i++)
		{
			treeListAll.get(i).id = i;
		}
	}
	
	public void DrawAll(SpriteBatch batch)
	{

		int currentchunkX = Tools.floor(GameScreen.camera.position.x/16/chunksize);
		int currentchunkY = Tools.floor(GameScreen.camera.position.y/16/chunksize);
		
		for(int j = currentchunkX-1; j<=currentchunkX+1; j++)
		{
			int rj = j;
			if(rj<0)rj = treeList.length+rj;
			else if(rj>=treeList.length)rj = rj-treeList.length;
			
			for(int k = currentchunkY-1; k<=currentchunkY+1; k++)
			{
				int rk = k;
				if(rk<0)rk = treeList[0].length+rk;
				else if(rk>=treeList[0].length)rk = rk-treeList[0].length;
				
				for (int i = 0; i < treeList[rj][rk].size(); i++)
			        if(treeList[rj][rk].get(i).isDead)
			        {
			        	Tools.checkItems();
			        	treeListAll.remove(treeListAll.indexOf(treeList[rj][rk].get(i)));
			        	treeList[rj][rk].remove(i);
			        	checkIDs();
			        }
				
				for (int i = 0; i < treeList[rj][rk].size(); i++)
				{
					treeList[rj][rk].get(i).checkPoints();
				}
				
				for (int i = 0; i < treeList[rj][rk].size(); i++)
				{
					treeList[rj][rk].get(i).superDraw(batch);
					if(Math.abs(treeList[rj][rk].get(i).x*16-GameScreen.camera.position.x)<GameScreen.camera.viewportWidth && Math.abs(treeList[rj][rk].get(i).y*16-GameScreen.camera.position.y)<GameScreen.camera.viewportHeight*2)treeList[rj][rk].get(i).draw(batch);
				}
				
			}
		}
		
	}
}
