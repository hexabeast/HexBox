package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.hexabeast.hexboxserver.NBlockModification;
import com.hexabeast.hexboxserver.NInputUpdate;
import com.hexabeast.hexboxserver.NPlayer;
import com.hexabeast.hexboxserver.NPlayerUpdate;
import com.hexabeast.hexboxserver.Nclick;
import com.hexabeast.hexboxserver.Ndead;

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
				
				if(modif.id != 0)ModifyTerrain.instance.setBlockFinal(modif.x, modif.y,modif.id, layer);
				else ModifyTerrain.instance.breakBlockFinal(modif.x, modif.y, layer);
			}
			else if (object instanceof NPlayer && Main.ingame)
	        {
				GameScreen.entities.mobs.NetworkPlayer(((NPlayer)object));
	        }
	          
			else if (object instanceof NPlayerUpdate && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkPlayerUpdate(((NPlayerUpdate)object));
	        }
			else if (object instanceof NInputUpdate && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkInputUpdate(((NInputUpdate)object));
	        }
			else if (object instanceof Nclick && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkClickUpdate(((Nclick)object));
	        }
			else if (object instanceof Ndead && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkDead(((Ndead)object));
	        }
		}
		modifications.clear();
	}
	
	public synchronized void add(Object object)
	{
		modifications.add(object);
	}

}
