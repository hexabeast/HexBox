package com.hexabeast.sandbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.StreamUtils;
import com.hexabeast.sandbox.mobs.MobSave;

public class MapGenerator {
	
	public int seed;
	
	public static MapGenerator instance;
	
	
	public static int limit = 1700;

	public int width;
	public int height;
	
	public HNoise noise;
	
	public int mudbiomepos;
	public int mudbiomepower = 300;
	
	float[] perlinMudBiomeY;
	
	float[][] perlinFirst;
	float[][] perlinSecond;
	float[][] perlinThird;

	public MapGenerator(int seed) 
	{
		noise = new HNoise(seed);
		
		instance = this;
		
		this.seed = seed;
		
		width = Map.instance.width;
		height = Map.instance.height;
		
		Main.random.setSeed(seed);
		
		mudbiomepos = Main.random.nextInt(width);
		
		perlinMudBiomeY = noise.generate1DGradient(height, 20);
	}
	
	public int difference(int a, int b)
	{
		return Math.min(Math.abs(a-b), Math.abs(Math.abs(a-b)-width));
	}
	
	public void GenerateMap()
	{
		noise.setSeed(seed);
		int h = limit+2;
		
		//BIOME
		float[] perlinBiome = noise.generate1DGradient(width, 650);
		for(int i = 0; i<width; i++)
		{
			perlinBiome[i] += 0.6;
			if(perlinBiome[i]<0)perlinBiome[i] = 0;
		}
		
		//MONTAGNE
		float[] perlinMontagne = noise.generate1DGradient(width, 130);
		for(int i = 0; i<width; i++)
		{
			perlinMontagne[i] *= perlinBiome[i]*12;
			if(perlinMontagne[i]<0)perlinMontagne[i] = 0;
		}
		
		//TROUBLE
		float[] perlinTrouble = noise.generate1DGradient(width, 11);
		for(int i = 0; i<width; i++)
		{
			perlinTrouble[i] /=20;
			perlinTrouble[i]*=perlinMontagne[i]*4;
		}
		
		//TROUBLE2
		float[] perlinTrouble2 = noise.generate1DGradient(width, 7);
		for(int i = 0; i<width; i++)
		{
			perlinTrouble2[i] *= perlinMontagne[i]/12 ;
		}
		
		//HILLS
		float[] perlinHills = noise.generate1DGradient(width, 32);
		for(int i = 0; i<width; i++)
		{
			perlinHills[i] /= 4;
		}
		
		for(int i = 0; i<width; i++)
		{
			int y = (int)((perlinMontagne[i]+perlinTrouble[i]+perlinTrouble2[i]+perlinHills[i])*30)+h;
			
			BlocType b = AllBlocTypes.instance.Dirt;
			if(difference(i,mudbiomepos)+(int)(perlinMudBiomeY[y]*20)<mudbiomepower)
			{
				b = AllBlocTypes.instance.Mud;
			}
			
			Map.instance.mainLayer.blocs[i][y] = b;
		}
		
		
		//PLACE DIRT
		for(int i = 0; i<width; i++)
		{
			int dif = difference(i,mudbiomepos);
			int k = 0;
			while(Map.instance.mainLayer.blocs[i][k]==AllBlocTypes.instance.Empty)
			{
				BlocType b = AllBlocTypes.instance.Dirt;
				if(dif+perlinMudBiomeY[k]*20<mudbiomepower)
				{
					b = AllBlocTypes.instance.Mud;
				}
				
				Map.instance.mainLayer.blocs[i][k] = b;
				Map.instance.backLayer.blocs[i][k] = b;
				k++;
			}
		}
		
		h = limit-35;
		
		float[] perlinCobble = noise.generate1DGradient(width, 100);
		for(int i = 0; i<width; i++)
		{
			int y = (int)(perlinCobble[i]*30+h);
					
			Map.instance.mainLayer.blocs[i][y] = AllBlocTypes.instance.Cobble;
						
			int k = 0;
			while(Map.instance.mainLayer.blocs[i][k]!=AllBlocTypes.instance.Cobble)
			{
				Map.instance.mainLayer.blocs[i][k] = AllBlocTypes.instance.Cobble;	
				k++;
			}
		}
		
		//IRON
		perlinFirst = noise.generateGradient(width, height, 8, 8);
		for(int i = 0; i<width; i++)
		{
			for(int j = limit-30; j<height; j++)
			{
				perlinFirst[i][j] = 0;
			}
		}
		
		//IRON2
		perlinSecond = noise.generateGradient(width, height, 16, 16);
		for(int i = 0; i<width; i++)
		{
			for(int j = limit-30; j<height; j++)
			{
				perlinSecond[i][j] = 0;
			}
		}
		
		//RAREMINERALS
		//PLACE IRON AND GOLD AND RARE
		perlinThird = noise.generateGradient(width, height, 33, 33);
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
			{
				if(perlinFirst[i][j]+perlinSecond[i][j]>0.78)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Iron;
				}
				if(perlinFirst[i][j]+perlinSecond[i][j]<-0.85)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Gold;
				}
				if(perlinFirst[i][j]*2*perlinSecond[i][j]+perlinThird[i][j]<-0.72)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Machalium;
				}
				if(perlinFirst[i][j]*2*perlinSecond[i][j]+perlinThird[i][j]>0.72)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Dragonium;
				}
			}
		}
		
		perlinFirst= null;
		perlinSecond = null;
		perlinThird = null;
		
		perlinFirst = noise.generateGradient(width, height, 25, 25);
		perlinSecond = noise.generateGradient(width, height, 15, 15);
		perlinThird = noise.generateGradient(width, height, 10, 10);
		
		for(int i = 0; i<width; i++)
		{
			for(int j = limit-20; j<height; j++)
			{
				perlinFirst[i][j] = 0;
			}
		}
		
		//FOSSILE
		for(int i = 0; i<width; i++)
		{
			for(int j = limit-20; j<height; j++)
			{
				perlinSecond[i][j] = 0;
			}
		}
		
		//CRYSTALS		
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
			{					
				if(perlinFirst[i][j]>0.25)
				{
					BlocType b = AllBlocTypes.instance.Dirt;
					if(difference(i,mudbiomepos)+(int)(perlinMudBiomeY[j]*20)<mudbiomepower)
					{
						b = AllBlocTypes.instance.Mud;
					}
					Map.instance.mainLayer.blocs[i][j] = b;
				}
				if(perlinFirst[i][j]>0.3 && perlinSecond[i][j]>0.4f)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Fossile;
				}
						
				if(perlinThird[i][j] > 0.45 && perlinSecond[i][j]>0.45f && Map.instance.mainLayer.blocs[i][j] == AllBlocTypes.instance.Cobble)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.CrystalBlue;
				}
				if(perlinThird[i][j] > 0.45 && perlinSecond[i][j]<-0.45f && Map.instance.mainLayer.blocs[i][j] == AllBlocTypes.instance.Cobble)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.CrystalRed;
				}
				if(perlinThird[i][j] < -0.45 && perlinSecond[i][j]>0.45f && Map.instance.mainLayer.blocs[i][j] == AllBlocTypes.instance.Cobble)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.CrystalGreen;
				}
				if(perlinThird[i][j] <- 0.45 && perlinSecond[i][j]<-0.45f && Map.instance.mainLayer.blocs[i][j] == AllBlocTypes.instance.Cobble)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.CrystalPurple;
				}
						
				if(perlinThird[i][j] > 0.48 && perlinFirst[i][j]<-0.45f && Map.instance.mainLayer.blocs[i][j] == AllBlocTypes.instance.Cobble)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.CrystalFire[0];
				}			
			}
		}
		perlinFirst= null;
		perlinSecond = null;
		perlinThird = null;
		
		perlinFirst = noise.generateGradient(width, height, 100, 60,0.5f,3);
		perlinSecond = noise.generateGradient(width, height, 300, 150);
			
		// PLACE HOLES
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
			{
				double minimum = -0.15;
				if(j>limit-120)minimum = minimum+(((double)(j-(limit-120)))/200);

				float p1 = perlinFirst[i][j];
				float p2 = perlinSecond[i][j];
				
				boolean test = false;
				
				if(!test)
				{
					if(p1<0.15 && p1>-0.05 && -Math.abs(p1-0.05)*1.5+p2>minimum)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Empty;	
					}
				}
				else
				{
					if(p1<-0.80)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Empty;	
					}
					else if(p1<-0.60)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Carbalium;	
					}
					else if(p1<-0.40)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Machalium;	
					}
					else if(p1<-0.20)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Dragonium;	
					}
					else if(p1<-0.06)
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.Eltalium;	
					}
					else if(p1<0.06)
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.Empty;	
					}
					else if(p1<0.20)
					{
						Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Gold;	
					}
					else if(p1<0.40)
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.Iron;	
					}
					else if(p1<0.60)
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.Ice;	
					}
					else if(p1<0.80)
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.IronBlock;	
					}
					else
					{
						Map.instance.mainLayer.blocs[i][j] =  AllBlocTypes.instance.CrystalFire[0];	
					}
				}
			}
		}
		perlinFirst= null;
		perlinSecond = null;
		perlinThird = null;
		
		if(!Main.mobile)
		{
			try {
				Map.instance.villageBack = Map.instance.LoadVillage(Map.instance.mapFileVillage2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Map.instance.villageMain = Map.instance.LoadVillage(Map.instance.mapFileVillage1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i<Map.instance.villageMain.length; i++)
			{
				for(int j = height-Map.instance.villageMain[i].length; j<height; j++)
				{
					Map.instance.backLayer.blocs[i][j] = AllBlocTypes.instance.getType(Map.instance.villageBack[i][j-(height-Map.instance.villageBack[i].length)]);
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.getType(Map.instance.villageMain[i][j-(height-Map.instance.villageMain[i].length)]);	
				}
			}
			
			for(int i = 0; i<width; i++)
			{
				Map.instance.mainLayer.blocs[i][0] = AllBlocTypes.instance.Unbreak;
				Map.instance.mainLayer.blocs[i][1] = AllBlocTypes.instance.Unbreak;
					
				Map.instance.mainLayer.blocs[i][height-1] = AllBlocTypes.instance.Unbreak;
				Map.instance.mainLayer.blocs[i][height-2] = AllBlocTypes.instance.Unbreak;
			}
		
		
			for(int i = 0; i<width; i++)
			{
				for(int j = Map.instance.randomHeight+15; j<Map.instance.randomHeight+35; j++)
				{
					Map.instance.mainLayer.blocs[i][j] = AllBlocTypes.instance.Unbreak;	
				}
			}
			
			for(int i = Map.instance.randomHeight+20; i<height; i++)
			{
				Map.instance.mainLayer.blocs[Map.villageWidth-1][i] = AllBlocTypes.instance.Unbreak;
				Map.instance.mainLayer.blocs[Map.villageWidth-2][i] = AllBlocTypes.instance.Unbreak;	
			}
		}
		
		noise.setSeed(seed);
				
	}
	

	
	public MapLayer Load(FileHandle[][] mapFile, boolean isMain) throws IOException, IllegalArgumentException
	{
		MapLayer layer = new MapLayer(isMain);
		InputStream is = null;
		byte[] bytes;
		for(int i=0; i<Map.chunkNumberWidth; i++)
		{
			for(int j=0; j<Map.chunkNumberHeight; j++)
			{
				bytes = Base64Coder.decode(mapFile[i][j].readString());
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				is = new InflaterInputStream(bais);
					
				byte[] temp = new byte[1];
					
				for(int k = i*Map.chunkWidth; k<(i+1)*Map.chunkWidth; k++)
				{
					for(int l = j*Map.chunkHeight; l<(j+1)*Map.chunkHeight; l++)
					{
							is.read(temp);
							layer.blocs[k][l] = AllBlocTypes.instance.getType((temp[0]));
					}
				}
				StreamUtils.closeQuietly(is);
				StreamUtils.closeQuietly(bais);
			}
		}
		return layer;
	}
	
	public MapLayer NLoad(String mapString, boolean isMain) throws IOException
	{
		MapLayer layer = new MapLayer(isMain);
		InputStream is = null;
		byte[] bytes;

		bytes = Base64Coder.decode(mapString);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		is = new InflaterInputStream(bais);
			
		byte[] temp = new byte[1];
			
		for(int k = 0; k<Map.instance.width; k++)
		{
			for(int l = 0; l<Map.instance.height; l++)
			{
					is.read(temp);
					layer.blocs[k][l] = AllBlocTypes.instance.getType((temp[0]));
			}
		}
		StreamUtils.closeQuietly(is);
		StreamUtils.closeQuietly(bais);

		return layer;
	}
	
	
	/*
	public byte[][] LoadVillage(FileHandle mapFile) throws IOException
	{
		InputStream is = null;
		byte[] bytes = Base64Coder.decode(mapFile.readString());
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		is = new InflaterInputStream(bais);
		
		byte [][] vil = new byte [Map.villageWidth][Map.villageHeight];
		
		byte[] temp = new byte[1];
		
		for(int i = 0; i<vil.length; i++)
		{
			for(int j = 0; j<vil[i].length; j++)
			{
				is.read(temp);
				vil[i][j] = (temp[0]);
			}
		}
		StreamUtils.closeQuietly(is);
		StreamUtils.closeQuietly(bais);
		return vil;
	}*/
	
	public void LoadTrees(FileHandle mapFile) throws IOException
	{
		if(mapFile.exists())
		{
			String loadTrees = mapFile.readString();
			TreeSave treeList = new Json().fromJson(TreeSave.class, loadTrees);
			
			if(treeList!= null)
			{
				for(int i = 0; i<treeList.x.length; i++)
				{
					GameScreen.entities.trees.PlaceTree(treeList.x[i], treeList.y[i], treeList.seed[i],treeList.ecloseTime[i]);
				}
			}
		}
	}
	public void LoadFurs(FileHandle mapFile) throws IOException
	{
		if(mapFile.exists())
		{
			String loadFurs = mapFile.readString();
			FurnitureSave furList = new Json().fromJson(FurnitureSave.class, loadFurs);
			
			if(furList!= null)
			{
				for(int i = 0; i<furList.x.length; i++)
				{
					GameScreen.entities.furnitures.addFurniture(furList.x[i], furList.y[i], furList.type[i],furList.chestids[i],furList.chestnumbers[i], furList.isTurned[i]);
				}
			}
		}
	}
	public void LoadMobs(FileHandle mapFile) throws IOException
	{
		if(mapFile.exists())
		{
			String loadMobs = mapFile.readString();
			MobSave mobList = new Json().fromJson(MobSave.class, loadMobs);
			
			if(mobList!= null)
			{
				for(int i = 0; i<mobList.x.length; i++)
				{
					GameScreen.entities.mobs.placeMob(mobList.x[i], mobList.y[i], mobList.type[i]);
				}
			}
		}
		
	}
}
