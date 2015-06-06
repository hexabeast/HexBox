package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class HitBox {
	Vector2[] col;
	ArrayList<Vector2> colDown = new ArrayList<Vector2>();
	ArrayList<Vector2> colUp = new ArrayList<Vector2>();

	ArrayList<Vector2> colOrigin = new ArrayList<Vector2>();
	ArrayList<Vector2> colEnd = new ArrayList<Vector2>();
	
	int size = 0;
	
	public float x = 0;
	public float y = 0;
	public float offx = 0;
	public float offy = 0;
	public float min = 0;
	public int offh = 20;
	public float turnoffx = 0;
	public boolean isTurned = false;
	public Vector2 maxFloor;
	public Vector2 minFloor;
	public boolean noturn = false;
	
	public HitBox(Rectangle rec,Rectangle rec2, float offfx)
	{
		ArrayList<Rectangle> list = new ArrayList<Rectangle>();
		list.add(rec);
		list.add(rec2);
		init(list,offfx);
	}
	
	public HitBox(Rectangle rec, float offfx)
	{
		ArrayList<Rectangle> list = new ArrayList<Rectangle>();
		list.add(rec);
		init(list,offfx);
	}
	
	public HitBox(ArrayList<Rectangle> rec, float offfx)
	{
		init(rec,offfx);
	}
	
	public void init(ArrayList<Rectangle> rec, float offfx)
	{
		turnoffx = offfx;
		
		min = rec.get(0).y;
		for(int j = 0; j<rec.size(); j++)
		{
			if(rec.get(j).y<min)
			{
				min = rec.get(j).y;
			}
		}
		for(int j = 0; j<rec.size(); j++)
		{
			if(Math.abs(rec.get(j).y - min)<0.1f)
			{
				if(maxFloor!= null && minFloor!= null)
				{
					if(maxFloor.x<rec.get(j).x+rec.get(j).width)
					{
						maxFloor.x = rec.get(j).x+rec.get(j).width;
						maxFloor.y = rec.get(j).y;
					}
					else if(minFloor.x>rec.get(j).x)
					{
						maxFloor.x = rec.get(j).x;
						maxFloor.y = rec.get(j).y;
					}
				}
				else
				{
					minFloor = new Vector2(rec.get(j).x, rec.get(j).y);
					maxFloor = new Vector2(rec.get(j).x+rec.get(j).width, rec.get(j).y);
				}
			}
		}
		
		size = rec.size(); 
		
		for(int i = 0; i<rec.size(); i++)
		{
			colOrigin.add(new Vector2(rec.get(i).x, rec.get(i).y));
			colEnd.add(new Vector2(rec.get(i).x+rec.get(i).width, rec.get(i).y+rec.get(i).height));
		}
		
	}
	
	public void update(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean stableFloor()
	{
		if(tileCollide(maxFloor.x, maxFloor.y-8) && tileCollide(minFloor.x, minFloor.y-8))return true;
		return false;
	}
	
	public boolean upHitOffset(float x, float y)
	{
		for(int i = 0; i<size; i++)
		{
			float originy = Math.max(min+offh, colOrigin.get(i).y);
			float endy = Math.max(min+offh, colEnd.get(i).y);
			
			if(originy!=endy)
			{
				if(isCollide(colOrigin.get(i).x+x, originy+y, colEnd.get(i).x+x, endy+y))return true;
			}
		}
		return false;
	}
	
	public boolean downHitOffset(float x, float y)
	{
		for(int i = 0; i<size; i++)
		{
			float originy = Math.min(min+offh, colOrigin.get(i).y);
			float endy = Math.min(min+offh, colEnd.get(i).y);
			
			if(originy!=endy)
			{
				if(isCollide(colOrigin.get(i).x+x, originy+y, colEnd.get(i).x+x, endy+y))return true;
			}
		}
		return false;
	}
	
	public boolean hitOffset(float x, float y)
	{
		for(int i = 0; i<size; i++)
		{
			if(isCollide(colOrigin.get(i).x+x, colOrigin.get(i).y+y, colEnd.get(i).x+x, colEnd.get(i).y+y))return true;
		}
		return false;
	}
	
	public boolean TestCollisionsAll()
	{
		for(int i = 0; i<size; i++)
		{
			if(isCollide(colOrigin.get(i).x, colOrigin.get(i).y, colEnd.get(i).x, colEnd.get(i).y))return true;
		}
		return false;
	}
	
	public boolean TestCollisions(boolean up)
	{
		if(up)
		{
			if(upHitOffset(0, 0))return true;
		}
		else
		{
			if(downHitOffset(0, 0))return true;
		}
		return false;
	}

	public boolean TestCollisionsHigh()
	{
		return hitOffset(0,16);
	}

	public boolean TestCollisionsDown()
	{
		return hitOffset(0,-4);
	}
	
	private boolean isCollide(float px, float py, float epx, float epy)
	{
		if(isTurned && !noturn)
		{
			float tepx = epx;
			epx = -px+turnoffx;
			px = -tepx+turnoffx;
		}
		
		int minx = Tools.floor((px+x)/16);
		int miny = Tools.floor((py+y)/16);
		int maxx = Tools.floor((epx+x)/16);
		int maxy = Tools.floor((epy+y)/16);
		for(int i = minx; i<=maxx; i++)
		{
			for(int j = miny; j<=maxy; j++)
			{
				if(Map.instance.mainLayer.getBloc(i,j).collide)return true;
			}
		}
		return false;
	}
	
	private boolean tileCollide(float x, float y)
	{
		return Map.instance.mainLayer.getBloc(Tools.floor((x+this.x)/16),Tools.floor((y+this.y)/16)).collide;
	}
	
	public void drawHitBox()
	{
		Main.batch.setColor(0, 1, 0, 0.6f);
		for(int i = 0; i<size; i++)
		{
			if(isTurned && !noturn)Tools.drawRect(x-colOrigin.get(i).x+turnoffx, colOrigin.get(i).y+y, colOrigin.get(i).x-colEnd.get(i).x, colEnd.get(i).y-colOrigin.get(i).y);
			else Tools.drawRect(colOrigin.get(i).x+x, colOrigin.get(i).y+y, colEnd.get(i).x-colOrigin.get(i).x, colEnd.get(i).y-colOrigin.get(i).y);
		}
	}
}
