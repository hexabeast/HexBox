package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class HitRect {
	public ArrayList<Rectangle> normal;
	public ArrayList<Rectangle> flipped;
	public float turnoffx = 0;
	
	public HitRect(float offx)
	{
		turnoffx = offx;
		normal = new ArrayList<Rectangle>();
		flipped = new ArrayList<Rectangle>();
	}
	
	public void add(Rectangle rec)
	{
		Rectangle rec2 = new Rectangle(turnoffx-rec.x-rec.width, rec.y, rec.width, rec.height);
		normal.add(rec);
		flipped.add(rec2);
	}
	
	public ArrayList<Rectangle> getRects(boolean turned)
	{
		if(!turned)
		{
			return normal;
		}
		else
		{
			return flipped;
		}
	}
}
