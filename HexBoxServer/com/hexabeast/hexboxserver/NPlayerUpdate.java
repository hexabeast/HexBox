package com.hexabeast.hexboxserver;

public class NPlayerUpdate {
	
	public NPlayerUpdate(float x, float y, float vx, float vy, float armRotation,int currentItem)
	{
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.armRotation = armRotation;
		this.currentItem = currentItem;
				
	}
	
	public int id = -1;
	public float x;
	public float y;
	public float vx;
	public float vy;
	public float armRotation;
	public int currentItem;
}
