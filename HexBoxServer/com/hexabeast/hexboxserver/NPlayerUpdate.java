package com.hexabeast.hexboxserver;

public class NPlayerUpdate {
	
	public NPlayerUpdate(float x, float y, float vx, float vy,int currentItem,float mousex, float mousey)
	{
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.currentItem = currentItem;
		this.mousex = mousex;
		this.mousey = mousey;
	}
	
	public NPlayerUpdate(){}
	
	public int id = -1;
	public float x;
	public float y;
	public float vx;
	public float vy;
	public int currentItem;
	public float mousex;
	public float mousey;
}
