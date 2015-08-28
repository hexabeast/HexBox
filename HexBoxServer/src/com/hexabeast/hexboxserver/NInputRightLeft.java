package com.hexabeast.hexboxserver;

public class NInputRightLeft {
	public boolean right;
	public boolean pressed;
	public int id;
	
	public NInputRightLeft(){}
	
	public NInputRightLeft(boolean direction, boolean press)
	{
		right = direction;
		pressed = press;
	}
}
