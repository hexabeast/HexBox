package com.hexabeast.sandbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.StreamUtils;
import com.hexabeast.sandbox.mobs.MobSave;

public class Map {
	
	public static int villageWidth = 2000;
	public static int villageHeight = 400;
	
	public byte[][] villageMain;
	public byte[][] villageBack;
	
	public int randomHeight = 1570;
	
	public FileHandle mapFileVillage1;
	public FileHandle mapFileVillage2;
	
	public int width;
	public int height;
	
	boolean random = false;
	
	public int limit = 0;
	
	public static Map instance;
	
	public MapLayer mainLayer;
	public MapLayer backLayer;
	
	public MapLayer mainLayerVillage;
	public MapLayer backLayerVillage;
	
	public FileHandle[][] mapFile1;
	public FileHandle[][] mapFile2;
	public FileHandle mapFileTrees;
	public FileHandle mapFileFur;
	public FileHandle mapFileMob;
	
	public static int chunkWidth = 500;
	public static int chunkHeight = 500;
	
	public static int chunkNumberWidth;
	public static int chunkNumberHeight;
	
	public LightManager lights;

	public Map(String fileName, int width, int height) 
	{
		
		Gdx.files.local("data/maps/"+fileName).mkdirs();
		
		if(Main.mobile)
		{
			randomHeight = 999;
		}
		
		randomHeight = height-villageHeight-30;
				
		MapGenerator.limit = height-villageHeight-400;
		
		if(Main.mobile)
		{
			MapGenerator.limit = 700;
		}
		
		limit = MapGenerator.limit;
		
		instance = this;
		
		chunkNumberWidth = (int)(width/chunkWidth);
		chunkNumberHeight = (int)(height/chunkHeight);
		
		mapFile1 = new  FileHandle[chunkNumberWidth][chunkNumberHeight];
		mapFile2 = new  FileHandle[chunkNumberWidth][chunkNumberHeight];
		
		this.width = width;
		this.height = height;
		
		for(int i=0; i<chunkNumberWidth; i++)
		{
			for(int j=0; j<chunkNumberHeight; j++)
			{
				mapFile1[i][j] = Gdx.files.local("data/maps/"+fileName+"/"+fileName+"1-"+String.valueOf(i)+"-"+String.valueOf(j)+".tmhm");
				mapFile2[i][j] = Gdx.files.local("data/maps/"+fileName+"/"+fileName+"2-"+String.valueOf(i)+"-"+String.valueOf(j)+".tmhm");
			}
		}
		
		mapFileTrees = Gdx.files.local("data/maps/"+fileName+"/"+fileName+"Trees"+".tmht");
		mapFileFur = Gdx.files.local("data/maps/"+fileName+"/"+fileName+"Fur"+".tmht");
		mapFileMob = Gdx.files.local("data/maps/"+fileName+"/"+fileName+"Mob"+".tmht");
		
		mapFileVillage1 = Gdx.files.local("data/village1.tmhm");
		mapFileVillage2 = Gdx.files.local("data/village2.tmhm");
		
		if(!mapFile1[0][0].file().exists() || Main.mobile) 
		{
			for(int i=0; i<chunkNumberWidth; i++)
			{
				for(int j=0; j<chunkNumberHeight; j++)
				{
					try {
						mapFile1[i][j].file().createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			mainLayer = new MapLayer(true);
			random = true;
		} 

		if(!mapFile2[0][0].file().exists() || Main.mobile) 
		{
			for(int i=0; i<chunkNumberWidth; i++)
			{
				for(int j=0; j<chunkNumberHeight; j++)
				{
					try {
						mapFile2[i][j].file().createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			backLayer = new MapLayer(false);
			random = true;
		}
		
		lights = new LightManager();
	}
	
	
	public void Save() throws IOException 
	{
		SaveLayer(mapFile1, mainLayer,false);
		SaveLayer(mapFile2, backLayer,false);
		
		SaveVillage(mapFileVillage1,mainLayer);
		SaveVillage(mapFileVillage2,backLayer);
		
		SaveEntities();
	}
	
	public void SaveEntities()
	{
		TreeSave treesav = new TreeSave();
		FurnitureSave fursav = new FurnitureSave();
		MobSave mobsav = new MobSave(); 
		
		fursav.x = new int[GameScreen.entities.furnitures.furnitureListAll.size()];
		fursav.y = new int[GameScreen.entities.furnitures.furnitureListAll.size()];
		fursav.type = new int[GameScreen.entities.furnitures.furnitureListAll.size()];
		fursav.chestids = new int[GameScreen.entities.furnitures.furnitureListAll.size()][];
		fursav.chestnumbers = new int[GameScreen.entities.furnitures.furnitureListAll.size()][];
		fursav.isTurned = new boolean[GameScreen.entities.furnitures.furnitureListAll.size()];
		
		mobsav.x = new float[GameScreen.entities.mobs.mobListAll.size()];
		mobsav.y = new float[GameScreen.entities.mobs.mobListAll.size()];
		mobsav.type = new int[GameScreen.entities.mobs.mobListAll.size()];
		
		treesav.x = new int[GameScreen.entities.trees.treeListAll.size()];
		treesav.y = new int[GameScreen.entities.trees.treeListAll.size()];
		treesav.seed = new int[GameScreen.entities.trees.treeListAll.size()];
		treesav.ecloseTime = new float[GameScreen.entities.trees.treeListAll.size()];
		//treesav.portalPos = MapLayer.portalInitialPos;
				
		for (int i = 0; i < GameScreen.entities.trees.treeListAll.size(); i++)
		{
			treesav.x[i] = GameScreen.entities.trees.treeListAll.get(i).x;
			treesav.y[i] = GameScreen.entities.trees.treeListAll.get(i).y;
			treesav.seed[i] = GameScreen.entities.trees.treeListAll.get(i).seed;
			treesav.ecloseTime[i] = GameScreen.entities.trees.treeListAll.get(i).ecloseTime;
		}
		
		for (int i = 0; i < GameScreen.entities.mobs.mobListAll.size(); i++)
		{
			mobsav.x[i] = GameScreen.entities.mobs.mobListAll.get(i).x;
			mobsav.y[i] = GameScreen.entities.mobs.mobListAll.get(i).y;
			mobsav.type[i] = GameScreen.entities.mobs.mobListAll.get(i).type;
		}
		
		for (int i = 0; i < GameScreen.entities.furnitures.furnitureListAll.size(); i++)
		{
			fursav.x[i] = GameScreen.entities.furnitures.furnitureListAll.get(i).x;
			fursav.y[i] = GameScreen.entities.furnitures.furnitureListAll.get(i).y;
			fursav.type[i] = GameScreen.entities.furnitures.furnitureListAll.get(i).type;
			fursav.isTurned[i] = GameScreen.entities.furnitures.furnitureListAll.get(i).isTurned;
			if(GameScreen.entities.furnitures.furnitureListAll.get(i).container)
			{
				fursav.chestids[i] = new int[GameScreen.entities.furnitures.furnitureListAll.get(i).itemsids.length];
				fursav.chestnumbers[i] = new int[GameScreen.entities.furnitures.furnitureListAll.get(i).itemsids.length];
				for(int j = 0; j<GameScreen.entities.furnitures.furnitureListAll.get(i).itemsids.length; j++)
				{
					fursav.chestids[i][j] = GameScreen.entities.furnitures.furnitureListAll.get(i).itemsids[j];
					fursav.chestnumbers[i][j] = GameScreen.entities.furnitures.furnitureListAll.get(i).itemsnumbers[j];
				}
			}
		}
		
		String saveTrees = new Json().toJson(treesav);	
		String saveFur = new Json().toJson(fursav);
		String saveMob = new Json().toJson(mobsav);
		mapFileTrees.writeString(saveTrees, false);
		mapFileFur.writeString(saveFur,false);
		mapFileMob.writeString(saveMob, false);
	}
	
	public void SaveVillage(FileHandle mapFile, MapLayer layer) throws IOException
	{ 	
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		DeflaterOutputStream dos;
        dos = new DeflaterOutputStream(baos2);
		
		for(int i = 0; i<villageWidth; i++)
		{
			for(int j = layer.height-villageHeight; j<layer.blocs[i].length; j++)
			{
				dos.write(layer.blocs[i][j].Id & Tools.LAST_BYTE);
			}
		}
		if(dos != null) {
            dos.finish();
        }
		mapFile.writeString(new String(Base64Coder.encode(baos2.toByteArray())), false);
		baos2.close();
	}
	
	public byte[][] LoadVillage(FileHandle mapFile) throws IOException
	{
		InputStream is = null;
		byte[] bytes = Base64Coder.decode(mapFile.readString());
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		is = new InflaterInputStream(bais);
		
		byte [][] vil = new byte [villageWidth][villageHeight];
		
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
	}
	
	public void SaveLayer(FileHandle[][] mapFile, MapLayer layer, boolean saveAll) throws IOException
	{
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DeflaterOutputStream dos;
		for(int i=0; i<chunkNumberWidth; i++)
		{
			for(int j=0; j<chunkNumberHeight; j++)
			{
				if(layer.isChanged[i][j] || saveAll)
				{
					layer.isChanged[i][j] = false;
					
					baos2 = new ByteArrayOutputStream();
					
			        dos = new DeflaterOutputStream(baos2);
					
			        for(int k = i*chunkWidth; k<(i+1)*chunkWidth; k++)
					{
			        	for(int l = j*chunkHeight; l<(j+1)*chunkHeight; l++)
						{
							dos.write(layer.blocs[k][l].Id & Tools.LAST_BYTE);
						}
					}
					if(dos != null) {
			            dos.finish();
			        }
					mapFile[i][j].writeString(new String(Base64Coder.encode(baos2.toByteArray())), false);
					baos2.close();	
				}	
			}
		}
	}
	
	public void DrawBack(SpriteBatch batch)
	{
		int minX = (int)((GameScreen.camera.position.x - GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 - 32)/16);
		int minY = (int)((GameScreen.camera.position.y - GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 - 32)/16);
		int maxX = (int)((GameScreen.camera.position.x + GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 + 32)/16);
		int maxY = (int)((GameScreen.camera.position.y + GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 + 32)/16);
		
		if(minX<-width+2)minX = -width+2;
		if(minY<0)minY = 0;
		if(maxX>width*2-2)maxX = width*2-2;
		if(maxY>limit)maxY = limit;
		Color col = batch.getColor();

		for(int i = minX; i<maxX; i++)
		{
			for(int j = minY; j<maxY; j++)
			{
				if((!Tools.isFull(mainLayer.getBloc(i, j).Id,mainLayer.getState(i, j)) || mainLayer.getBloc(i, j).transparent) && (!Tools.isFull(backLayer.getBloc(i, j).Id,backLayer.getState(i, j)) || backLayer.getBloc(i, j).transparent))
				{
				Vector3 color = Tools.getShadowColor(i, j);
				batch.setColor(color.x*col.r*0.65f,color.y*col.g*0.65f,color.z*col.b*0.65f,1);
				batch.draw(TextureManager.instance.TextureRegions[AllBlocTypes.instance.Dirt.Id][AllBlocTypes.instance.full], i*16, j*16, 16, 16);
				}
			}
			if(maxY == limit)
			{
				if((!Tools.isFull(mainLayer.getBloc(i, limit).Id,mainLayer.getState(i, limit)) || mainLayer.getBloc(i, limit).transparent) && (!Tools.isFull(backLayer.getBloc(i, limit).Id,backLayer.getState(i, limit)) || backLayer.getBloc(i, limit).transparent))
				{
				Vector3 color = Tools.getShadowColor(i, limit);
				batch.setColor(color.x*col.r*0.65f,color.y*col.g*0.65f,color.z*col.b*0.65f,1);
				batch.draw(TextureManager.instance.TextureRegions[AllBlocTypes.instance.Dirt.Id][AllBlocTypes.instance.IIIsides], i*16, limit*16, 16, 16);
				}
			}
			
		}
		
		batch.setColor(col);
	}

}
