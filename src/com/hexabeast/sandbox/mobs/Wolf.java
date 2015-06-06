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

public class Wolf extends Mob{
	
	float basedecisionrate = 3;
	
	public Timer decisionTimer = new Timer(basedecisionrate);
	public Timer stuckTimer = new Timer(0.5f);
	public Timer moveTimer = new Timer(0.05f);
	public Timer stillTimer = new Timer(0.6f);
	public Timer attackTimer = new Timer(0.1f);
	public Timer agressTimer = new Timer(1);
	public Timer jumpTimer = new Timer(0.15f);
	
	
	boolean attack = false;
	boolean attacked = true;
	
	int currentBody = 0;
	
	boolean aggressive = false;
	boolean running = false;
	public Vector2 machpos = new Vector2();
	public Vector2 invmachpos = new Vector2();
	public Vector2 velo = new Vector2();
	
	float maxspeedx = 300;
	

	public Wolf()
	{
		type = 2;
		
		speedx = 200;
		tex.add(TextureManager.instance.wolf[currentBody]);
		calculateSize(0);
		
		machpos = new Vector2(74,28);
		invmachpos = new Vector2(width-machpos.x,machpos.y);
		
		offx = 0;
		health = 50;
		
		hitrect = new HitRect(offx+width);
		hitrect.add(new Rectangle(18,8,46,24));
		hitrect.add(new Rectangle(width-6-18,24,20,12));

		hitbox = new HitBox(new Rectangle(20, 1, 40, 39), new Rectangle(10, 34, 60, 6), offx+width);
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
			Vector2 mach = machpos;
			
			if(isTurned)
			{
				mach = invmachpos;
			}
			
			if(canJump && decisionTimer.check())
			{
				decisionTimer.rate = (float) ((basedecisionrate/8+Math.random())*basedecisionrate/10);
				
				velo.x = GameScreen.player.middle.x-(x+mach.x);
				velo.y = GameScreen.player.middle.y-(y+mach.y);
				
				if(velo.len()>720)aggressive = false;

				if(canJump && velo.y>64)
				{
					velo.y = speedy/2.6f;
				}
				
				if(!isTurned)
				{
					if(velo.x<-width*2/3)
					{
						ax = -speedx*10;
					}
					else if(velo.x>4 || velo.x<-4 || Math.abs(velo.y)>32)
					{
						ax = speedx*10;
					}
					
					else
					{
						ax = 0;
						vx = 0;
					}
				}
				else
				{
					if(velo.x>width*2/3)
					{
						ax = speedx*10;
					}
					else if(velo.x<-4 || velo.x>4 || Math.abs(velo.y)>32)
					{
						ax = -speedx*10;
					}
					
					else
					{
						ax = 0;
						vx = 0;
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
					GameScreen.player.Hurt(power, 0, 0, x+mach.x, y+mach.y);
				}
			}
		
			if(canJump && running && hitbox.upHitOffset(vx/5, 0))
			{
				vy = speedy/2.6f;
			}
			
			if(vx!= 0 || ax == 0)
			{
				stuckTimer.reboot();
			}
			
			if(stuckTimer.check())
			{
				ax = -ax;
			}
			
			if(vx == 0)running = false;
			else running = true;
		}
		else
		{
			if(running && vx== 0)
			{
				ax = -ax;
			}
			
			if(canJump && running && hitbox.upHitOffset(vx/5, 0))
			{
				vy = speedy/2.6f;
			}
			else if(canJump && decisionTimer.check())
			{
				decisionTimer.rate = (float) ((basedecisionrate+Math.random())*basedecisionrate/2f);
				
				if(running && hitbox.stableFloor() && Math.random()>0.1)
				{
					ax = 0;
					running = false;
				}
				else if(Math.random()>0.5)
				{
					ax = -speedx*10;
					running = true;
				}
				else
				{
					ax = speedx*10;
					running = true;
				}
			}
			
		}
	}
	
	@Override
	public void premove()
	{
		vy -= Constants.gravity*Main.delta;
		if(ax == 0)
		{
			if(vx>0)
			{
				vx -= (Main.delta*speedx*10);
				if(vx<0)vx=0;
			}
			else
			{
				vx += (Main.delta*speedx*10);
				if(vx>0)vx=0;
			}
		}
		
		vx+=ax*Main.delta;
		vy+=ay*Main.delta;
		
		if(!isTurned && ax<0)isTurned = true;
		else if(isTurned && ax>0) isTurned = false;
		if(vy<-speedy)vy = -speedy;
		
		if(vx>speedx)vx = speedx;
		else if(vx<-speedx)vx = -speedx;
	}
	
	@Override
	public void goRight()
	{
		ax = speedx*10;
		running = true;
	}
	
	@Override
	public void goLeft()
	{
		ax = -speedx*10;
		running = true;
	}
	
	@Override
	public void goStandX()
	{
		if(canJump)currentBody = 0;
		ax = 0;
		running = false;
	}
	
	@Override
	public void goJump()
	{
		if(canJump)vy = speedy/2.6f;
	}
	
	@Override
	public void goAttack()
	{	
		Vector2 mach = machpos;
		
		if(isTurned)
		{
			mach = invmachpos;
		}
		
		if(AllEntities.getType(Tools.floor((x+mach.x)/16), Tools.floor((y+mach.y)/16)) == AllEntities.mobtype)
		{
			int id = AllEntities.getItem(Tools.floor((x+mach.x)/16), Tools.floor((y+mach.y)/16))-Constants.chestlimit;
			
			boolean touched = false;
			ArrayList<Rectangle> rects = GameScreen.entities.mobs.mobListAll.get(id).hitrect.getRects(GameScreen.entities.mobs.mobListAll.get(id).isTurned);
			
			Rectangle colrec = new Rectangle();
			ArrayList<Rectangle> colrects = hitrect.getRects(isTurned);
			for(int i = 0; i<rects.size(); i++)
			{
				for(int j = 0; j<colrects.size(); j++)
				{
					colrec.x = colrects.get(j).x-(GameScreen.entities.mobs.mobListAll.get(id).x)+x;
					colrec.y = colrects.get(j).y-(GameScreen.entities.mobs.mobListAll.get(id).y)+y;
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
				GameScreen.entities.mobs.mobListAll.get(id).damage(power, 0.5f, x+mach.x, y+mach.y);
			}
		}
	}
	
	@Override
	public void visual()
	{
		float rate = moveTimer.rate;
		if(vx!=0)rate *= maxspeedx/Math.abs(vx);
		if(canJump && running && moveTimer.check(rate))
		{
			currentBody++;
			if(currentBody>=8 || currentBody<2)
			{
				currentBody = 2;
			}
		}
		
		if(canJump && !running && stillTimer.check())
		{
			currentBody++;
			if(currentBody>1)
			{
				currentBody = 0;
			}
		}
		
		if(!canJump && jumpTimer.check())
		{
			currentBody++;
			if(currentBody>=10 || currentBody<8)
			{
				currentBody = 8;
			}
		}
		
		tex.set(0, TextureManager.instance.wolf[currentBody]);
	}
}
