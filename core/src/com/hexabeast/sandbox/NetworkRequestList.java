package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.hexabeast.hexboxserver.NBlockModification;
import com.hexabeast.hexboxserver.NPlayer;
import com.hexabeast.hexboxserver.NPlayerUpdate;

public class NetworkRequestList {
	
	private ArrayList<Object> modifications;
	
	public NetworkRequestList()
	{
		modifications = new ArrayList<Object>();
	}
	
	public synchronized void applyModifications()
	{
		for(int i = 0; i<modifications.size(); i++)
		{
			Object object = modifications.get(i);
			if(object instanceof NBlockModification)
			{
				NBlockModification modif = (NBlockModification)object;
				MapLayer layer = Map.instance.mainLayer;
				if(!modif.layer)layer = Map.instance.backLayer;
				ModifyTerrain.instance.breakBlock(modif.x, modif.y, layer);
			}
			
			if (object instanceof NPlayer)
	          {
	        	 GameScreen.entities.mobs.NetworkPlayer(((NPlayer)object));
	          }
	          
	          if (object instanceof NPlayerUpdate)
	          {
	        	  GameScreen.entities.mobs.NetworkPlayerUpdate(((NPlayerUpdate)object));
	          }
		}
	}
	
	public synchronized void add(Object object)
	{
		modifications.add(object);
	}

}
