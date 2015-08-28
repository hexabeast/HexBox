package com.hexabeast.hexboxserver;

public class NPlayer {
	
	public NPlayer(float x, float y, byte armId, byte legId, byte bodyId, byte headId, byte gloveId, byte legginsId, byte armorId, byte helmetId, int currentItem, byte hookId)
	{
		this.x = x;
		this.y = y;
		this.armId = armId;
		this.legId = legId;
		this.bodyId = bodyId;
		this.headId = headId;
		this.gloveId = gloveId;
		this.legginsId = legginsId;
		this.armorId = armorId;
		this.helmetId = helmetId;
		this.currentItem = currentItem;
		this.hookId = hookId;
				
	}
	
	
	public NPlayer()
	{
		
	}
	
	public int id = -1;
	public float x;
	public float y;
	public byte armId;
	public byte legId;
	public byte bodyId;
	public byte headId;
	public byte gloveId;
	public byte legginsId;
	public byte armorId;
	public byte helmetId;
	public byte hookId;
	
	public int currentItem;
}
