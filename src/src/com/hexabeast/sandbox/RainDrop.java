package com.hexabeast.sandbox;

public class RainDrop extends Entity{
	public Timer life;
	public Timer anim;
	public float x;
	public float y;
	public boolean isDead = false;
	public boolean collide = false;
	int current;
	
	public RainDrop(float x, float y)
	{
		life = new Timer(6);
		anim = new Timer(0.05f);
		this.x = x;
		this.y = y;
		current = 0;
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
	
	public void move()
	{
		
		if(collide)
		{
			if(anim.check())
			{
				if(current<3)
				{
					current++;
				}
				
				else
				isDead = true;
			}
		}
		else
		{
			float maximu = 1;
			
			if(Main.delta>1/35f)maximu = 6;
			else if(Main.delta>1/65f)maximu = 4;
			else if(Main.delta>1/125f)maximu = 2;
			for(int i = 0; i<maximu; i++)
			{
				x+=280/maximu*Main.delta;
				y-=800/maximu*Main.delta;
				if(Map.instance.mainLayer.getBloc(Tools.floor(x/16), Tools.floor(y/16)).collide)
				{
					collide = true;
					break;
				}
			}
		}
		
		if(life.check())isDead = true;
	}
}
