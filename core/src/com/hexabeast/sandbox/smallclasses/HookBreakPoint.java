package com.hexabeast.sandbox.smallclasses;

import com.hexabeast.sandbox.Entity;

public class HookBreakPoint extends Entity{
	public float x;
	public float y;
	public boolean floating = false;
	
	public HookBreakPoint() 
	{
		
	}
	
	public HookBreakPoint(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public float getX()
	{
		return x;
	}
	
	@Override
	public float getY()
	{
		return y;
	}
	
	@Override
	public void setX(float xii)
	{
		x = xii;
	}
	
	@Override
	public void setY(float xii)
	{
		y = xii;
	}
}
