package com.hexabeast.sandbox;
import com.hexabeast.hexboxserver.NBlockModification;
import com.hexabeast.hexboxserver.NCompressedLayer;
import com.hexabeast.hexboxserver.VirtualMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StreamUtils;
import com.esotericsoftware.kryonet.Connection;


public class IntegratedServerMap extends VirtualMap{

	String fileName;	
	
	public IntegratedServerMap() 
	{
	}
	@Override
	public synchronized void setBlock(NBlockModification conf)
	{
		int x = conf.x;
		//int y = conf.y;
		//MapLayer layer = Map.instance.mainLayer;
		//if(!conf.layer)layer = Map.instance.backLayer;
		//int id = conf.id;

		while(x<0)x = Map.instance.width+x;
		while(x>Map.instance.width-1)x = 0+(x-(Map.instance.width));
		
		//if(id != layer.getBloc(x, y).Id)
		//{
			NetworkManager.instance.server.sendBlock(conf);
		//}
		
		NetworkManager.instance.modifications.add(conf);
		//if(id!=0)ModifyTerrain.instance.setBlockFinal(x, y, id, layer);
		//else ModifyTerrain.instance.breakBlockFinal(x, y, layer);

	}
	
	@Override
	public synchronized void sendCompressedMap(boolean isMain, Connection c)
	{
		try {
			sendLayer(isMain,c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendLayer(boolean isMain, Connection c) throws IOException
	{
		MapLayer m = Map.instance.mainLayer;
		if(!isMain)m = Map.instance.backLayer;
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DeflaterOutputStream dos;

		baos2 = new ByteArrayOutputStream();
		
        dos = new DeflaterOutputStream(baos2);
		
        for(int k = 0; k<Map.instance.width; k++)
		{
        	for(int l = 0; l<Map.instance.height; l++)
			{
				dos.write(m.getBloc(k, l).Id & 0x000000FF);
			}
		}
		if(dos != null) {
            dos.finish();
        }
		NCompressedLayer l = new NCompressedLayer();
		l.layer = new String(Base64Coder.encode(baos2.toByteArray()));
		l.isMain = isMain;
		
		NetworkManager.instance.server.sendLayer(l,c);
		 System.out.println("Map sent!");
		
		baos2.close();
	}
	@Override
	public void loadMap(byte[][] layer, FileHandle[][] mapFile) throws IOException
	{	
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
							layer[k][l] = (temp[0]);
							
					}
				}
				
				StreamUtils.closeQuietly(is);
				StreamUtils.closeQuietly(bais);
			}
		}
	}
}

