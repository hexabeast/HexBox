package com.hexabeast.sandbox.mobs;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.sandbox.AllEntities;
import com.hexabeast.sandbox.Constants;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.Main;
import com.hexabeast.sandbox.TextureManager;
import com.hexabeast.sandbox.Timer;
import com.hexabeast.sandbox.Tools;

public class Hornet extends Mob{
	
	float basedecisionrate = 3;
	
	public Timer decisionTimer = new Timer(basedecisionrate);
	public Timer moveTimer = new Timer(0.05f);
	public Timer stillTimer = new Timer(0.3f);
	public Timer attackTimer = new Timer(0.1f);
	public Timer agressTimer = new Timer(1);
	
	
	boolean attack = false;
	boolean attacked = true;
	
	int currentBody = 0;
	
	boolean aggressive = false;
	boolean running = false;
	public Vector2 velo = new Vector2();
	
	float maxspeedx = 300;
	

	public Hornet()
	{
		type = 3;
		
		speedx = 200;
		tex.add(TextureManager.instance.hor[currentBody]);
		calculateSize(0);
		
		offx = 0;
		health = 50;
		
		hitrect = new HitRect(offx+width);
		hitrect.add(new Rectangle(6,5,48,22));

		hitbox = new HitBox(new Rectangle(18,5,28,22), offx+width);
		hitbox.noturn = true;
		
		attach = false;
	}
	
	@Override
	public void damage(float d, float immortality, float x, float y)
	{
		decisionTimer.rate = 0;
		aggressive = true;
		speedx = maxspeedx;
		super.damage(d, immortality,x,y);
	}
	
	@Override
	public void IA()
	{
		if(aggressive)
		{
			
			if(canJump && decisionTimer.check())
			{
				decisionTimer.rate = (float) ((basedecisionrate/8+Math.random())*basedecisionrate/10);
				
				velo.x = GameScreen.player.middle.x-(x+width/2);
				velo.y = GameScreen.player.middle.y-(y+height/2);
				
				if(velo.len()>720)aggressive = false;

				if(canJump && stillTimer.checkNoReboot())
				{
					jump();
					if(velo.x<0)
					{
						vx = -speedx;
						jump();
					}
					else
					{
						vx = speedx;
						jump();
					}
				}
			}
			
			if(agressTimer.checkNoReboot())
			{
				boolean touched = false;
				ArrayList<Rectangle> rects = GameScreen.player.getHitRect();
				
				Rectangle colrec = new Rectangle();
				ArrayList<Rectangle> colrects = hitrect.getRects(isTurned);
				for(int i = 0; i<rects.size(); i++)
				{
					for(int j = 0; j<colrects.size(); j++)
					{
						colrec.x = colrects.get(j).x-(GameScreen.player.x+GameScreen.player.transoffx)+x;
						colrec.y = colrects.get(j).y-(GameScreen.player.y+GameScreen.player.transoffy)+y;
						colrec.width = colrects.get(j).width;
						colrec.height = colrects.get(j).height;
						
						//rects.get(i).contains(x+mach.x+machoff-GameScreen.player.x, y+mach.y-GameScreen.player.y))
						
						if(rects.get(i).overlaps(colrec))
						{
							touched = true;
							break;
						}
					}
				}
				if(touched)
				{
					agressTimer.reboot();
					GameScreen.player.Hurt(power, 0, 0, x+width/2, y+height/2);
				}
			}
		}
		else
		{
			if(running && vx== 0)
			{
				vx = -vx;
			}
			

			if(canJump && decisionTimer.check())
			{
				decisionTimer.rate = (float) ((basedecisionrate+Math.random())*1.6f);
				
				if(running && hitbox.stableFloor() && Math.random()>0.1)
				{
					vx = 0;
				}
				else if(Math.random()>0.5)
				{
					vx = -speedx;
					jump();
				}
				else
				{
					vx = speedx;
					jump();
				}
			}
			
		}
	}
	
	public void jump()
	{
		vy = speedy/3+(Main.random.nextFloat()*speedy)/5;
	}
	
	@Override
	public void premove()
	{
		vy -= Constants.gravity*Main.delta;
		
		if(isTurned && vx<0)isTurned = false;
		else if(!isTurned && vx>0) isTurned = true;
		if(vy<-speedy)vy = -speedy;
		
		if(vx>speedx)vx = speedx;
		else if(vx<-speedx)vx = -speedx;
		
		if(canJump && vy<1)vx = 0;
		else stillTimer.reboot();
		
		if(canJump)running = false;
		else running = true;
	}
	
	@Override
	public void goRight()
	{
		if(canJump && stillTimer.checkNoReboot())
		{
			jump();
			vx = speedx;
		}
	}
	
	@Override
	public void goLeft()
	{
		if(canJump && stillTimer.checkNoReboot())
		{
			jump();
			vx = -speedx;
		}
	}
	
	@Override
	public void goStandX()
	{
	}
	
	@Override
	public void goJump()
	{
		if(canJump && stillTimer.checkNoReboot())
		{
			jump();
			if(!isTurned)vx = -speedx;
			else vx = speedx;
		}
		
	}
	
	@Override
	public void goClickLeftInstant()
	{	
		
		if(AllEntities.getType(Tools.floor((x+width/2)/16), Tools.floor((y+height/2)/16)) == AllEntities.mobtype)
		{
			Mob m = (Mob)AllEntities.getEntity(Tools.floor((x+width/2)/16), Tools.floor((y+height/2)/16));
			boolean touched = false;
			ArrayList<Rectangle> rects = m.hitrect.getRects(m.isTurned);
			
			Rectangle colrec = new Rectangle();
			ArrayList<Rectangle> colrects = hitrect.getRects(isTurned);
			for(int i = 0; i<rects.size(); i++)
			{
				for(int j = 0; j<colrects.size(); j++)
				{
					colrec.x = colrects.get(j).x-(m.x)+x;
					colrec.y = colrects.get(j).y-(m.y)+y;
					colrec.width = colrects.get(j).width;
					colrec.height = colrects.get(j).height;

					if(rects.get(i).overlaps(colrec))
					{
						touched = true;
						break;
					}
				}
			}
			if(touched)
			{
				m.damage(power, 0.5f, x+width/2, y+height/2);
			}
		}
	}
	
	@Override
	public void visual()
	{
		if(running)
		{
			currentBody = 1;
		}
		else
		{
			currentBody = 0;
		}

		tex.set(0, TextureManager.instance.hor[currentBody]);
	}
}
