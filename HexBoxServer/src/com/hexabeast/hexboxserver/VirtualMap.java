package com.hexabeast.hexboxserver;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryonet.Connection;

public class VirtualMap {
	public VirtualMap()
	{
	}
	
	public synchronized void setBlock(NBlockModification conf)
	{
		
	}
		
	public void setChanged(int x, int y, int layer)
	{
		
	}
	
	public synchronized void sendCompressedMap(boolean isMain, Connection c)
	{
		
	}
	
	public void sendLayer(boolean isMain, Connection c) throws IOException
	{
		
	}
	
	public void loadMap(byte[][] layer, FileHandle[][] mapFile) throws IOException
	{	
		
	}
	
	public void saveMap()
	{
			
	}
	
	public void SaveLayers(int m, boolean saveAll) throws IOException
	{
		
	}
}
