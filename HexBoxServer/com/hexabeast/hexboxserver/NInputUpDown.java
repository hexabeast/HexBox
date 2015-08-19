package com.hexabeast.hexboxserver;

public class NInputUpDown {
	public boolean up;
	public boolean pressed;
	public int id;
	
	public NInputUpDown(){}
	
	public NInputUpDown(boolean direction, boolean press)
	{
		up = direction;
		pressed = press;
	}
}
