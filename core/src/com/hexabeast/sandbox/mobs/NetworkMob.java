package com.hexabeast.sandbox.mobs;

import com.hexabeast.hexboxserver.NInputUpdate;

public class NetworkMob {
	public Mob mob;
	public NInputUpdate n;
	
	public NetworkMob(Mob m)
	{
		mob = m;
		n = new NInputUpdate();
	}
	
	public void update()
	{
		
	}
}
