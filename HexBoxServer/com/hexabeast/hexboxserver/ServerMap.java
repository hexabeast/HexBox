package com.hexabeast.hexboxserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StreamUtils;
import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.kryonet.Connection;

public class ServerMap {
	
	public byte[][][] layers;
	
	public int width;
	public int height;
	
	public int chunkWidth = 500;
	public int chunkHeight = 500;
	
	public int chunkNumberWidth;
	public int chunkNumberHeight;
	
	public FileHandle[][][] mapFiles;
	
	public boolean changedChunks[][][];
	
	Json json;
	
	String fileName;	
	
	public ServerMap(String fileName)
	{
		this.fileName = fileName;
		json = new Json();
		
		
		//LOAD PROPERTIES
		
		File propertiesFile = new File("data/maps/"+fileName+"/properties.json");
		
		if(!propertiesFile.exists()) 
		{
			System.out.println("Error: No map");
		}
		else {
			
			MapProperties properties;

			properties = json.fromJson(MapProperties.class, propertiesFile);
			
			if(properties != null)
			{
				width = properties.width;
				height = properties.height;
				System.out.println("Map size : "+String.valueOf(width)+" x " +String.valueOf(height));
			}
			
			System.out.println("Map loading...");
			
			chunkNumberWidth = (int)(width/chunkWidth);
			chunkNumberHeight = (int)(height/chunkHeight);
			
			mapFiles = new  FileHandle[2][chunkNumberWidth][chunkNumberHeight];
			
			for(int i=0; i<chunkNumberWidth; i++)
			{
				for(int j=0; j<chunkNumberHeight; j++)
				{
					mapFiles[0][i][j] = new FileHandle("data/maps/"+fileName+"/"+fileName+"1-"+String.valueOf(i)+"-"+String.valueOf(j)+".tmhm");
					mapFiles[1][i][j] = new FileHandle("data/maps/"+fileName+"/"+fileName+"2-"+String.valueOf(i)+"-"+String.valueOf(j)+".tmhm");
				}
			}
			
			layers = new byte[2][width][height];
			
			try {
				loadMap(layers[0], mapFiles[0]);
				loadMap(layers[1], mapFiles[1]);
			} catch (IOException e) {
				System.out.println("Error loading map");
			}
			
			System.out.println("Map loaded!");
		}
		
		//STOPLOAD PROPERTIES
		
		
		changedChunks = new boolean[2][chunkNumberWidth][chunkNumberHeight];
	}
	
	public synchronized void setBlock(NBlockModification conf)
	{
		int x = conf.x;
		int y = conf.y;
		int layer = 0;
		if(!conf.layer)layer = 1;
		int id = conf.id;
		if(x<0 || x>=width)
		{
			while(x<0)x = width+x;
			while(x>width-1)x = 0+(x-(width));
			
			if(id != layers[layer][x][y])
			{
				setChanged(x,y,layer);
				Main.server.sendBlock(conf);
			}
			layers[layer][x][y] = (byte) id;
		}
		else
		{
			if(id != layers[layer][x][y])
			{
				setChanged(x,y,layer);
				Main.server.sendBlock(conf);
			}
			layers[layer][x][y] = (byte) id;
		}
	}
		
	public void setChanged(int x, int y, int layer)
	{
		int x2 = (int)((float)x/(float)width*changedChunks[0].length);
		int y2 = (int)((float)y/(float)height*changedChunks[0][0].length);
		changedChunks[layer][x2][y2] = true;
	}
	
	public synchronized void sendCompressedMap(boolean isMain, Connection c)
	{
		try {
			sendLayer(isMain,c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLayer(boolean isMain, Connection c) throws IOException
	{
		int m = 0;
		if(!isMain)m = 1;
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DeflaterOutputStream dos;

		baos2 = new ByteArrayOutputStream();
		
        dos = new DeflaterOutputStream(baos2);
		
        for(int k = 0; k<width; k++)
		{
        	for(int l = 0; l<height; l++)
			{
				dos.write(layers[m][k][l] & 0x000000FF);
			}
		}
		if(dos != null) {
            dos.finish();
        }
		NCompressedLayer l = new NCompressedLayer();
		l.layer = new String(Base64Coder.encode(baos2.toByteArray()));
		l.isMain = isMain;
		
		Main.server.sendLayer(l,c);
		 System.out.println("Map sent!");
		
		baos2.close();
	}
	
	public void loadMap(byte[][] layer, FileHandle[][] mapFile) throws IOException
	{	
		InputStream is = null;
		byte[] bytes;
		for(int i=0; i<chunkNumberWidth; i++)
		{
			for(int j=0; j<chunkNumberHeight; j++)
			{
				bytes = Base64Coder.decode(mapFile[i][j].readString());
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				is = new InflaterInputStream(bais);
					
				byte[] temp = new byte[1];
					
				for(int k = i*chunkWidth; k<(i+1)*chunkWidth; k++)
				{
					for(int l = j*chunkHeight; l<(j+1)*chunkHeight; l++)
					{
							is.read(temp);
							layer[k][l] = (temp[0]);
							
					}
				}
				
				StreamUtils.closeQuietly(is);
				StreamUtils.closeQuietly(bais);
			}
		}
	}
	
	public void saveMap()
	{
		for(int i = 0; i<2; i++)
		{
			try {
				SaveLayers(i,false);
			} catch (IOException e) {
				System.out.println("Problem saving the map");
			}
		}
			
	}
	
	public void SaveLayers(int m, boolean saveAll) throws IOException
	{
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DeflaterOutputStream dos;
		for(int i=0; i<chunkNumberWidth; i++)
		{
			for(int j=0; j<chunkNumberHeight; j++)
			{
				if(changedChunks[m][i][j] || saveAll)
				{
					changedChunks[m][i][j] = false;
					
					baos2 = new ByteArrayOutputStream();
					
			        dos = new DeflaterOutputStream(baos2);
					
			        for(int k = i*chunkWidth; k<(i+1)*chunkWidth; k++)
					{
			        	for(int l = j*chunkHeight; l<(j+1)*chunkHeight; l++)
						{
							dos.write(layers[m][k][l] & 0x000000FF);
						}
					}
					if(dos != null) {
			            dos.finish();
			        }
					mapFiles[m][i][j].writeString(new String(Base64Coder.encode(baos2.toByteArray())), false);
					baos2.close();	
				}	
			}
		}
	}

}
