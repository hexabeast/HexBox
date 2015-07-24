package com.hexabeast.sandbox.mobs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.sandbox.Entity;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.Main;
import com.hexabeast.sandbox.TextureManager;
import com.hexabeast.sandbox.Tools;

public class BigInsecte extends Mob {
	float wingrate = 0.06f;
	float attackrate = 0.1f;
	float lastwing = 0;
	int currentWing = 0;
	int currentBody = 2;
	float basedecisionrate = 1;
	float decisionrate = basedecisionrate;
	float lastdecision = 0;
	float offvx = 0;
	float offvy = 0;
	boolean nomove = false;
	public Vector2 picpos = new Vector2(180,30);
	
	public Vector2 velo = new Vector2();
	
	float attacktime = 0;
	boolean attack = false;
	boolean attacked = true;

	public BigInsecte()
	{
		manualoffx = 120;
		type = 1;
		tex.add(TextureManager.instance.veswings[currentWing]);
		tex.add(TextureManager.instance.vesbody[currentBody]);
		calculateSize(0);
		
		offx = 320-width;
		health = 500;
		
		hitrect = new HitRect(offx+width);
		hitrect.add(new Rectangle(124, 24, 20, 60));
		hitrect.add(new Rectangle(130, 62, 24, 36));
		hitrect.add(new Rectangle(132, 94, 32, 20));
		hitrect.add(new Rectangle(162, 102, 36, 32));
		hitbox = new HitBox(hitrect.normal, offx+width);
	}
	
	@Override
	public void IA()
	{
		velo.x = GameScreen.player.middle.x-(x+picpos.x)+15;
		velo.y = GameScreen.player.middle.y-(y+picpos.y)+35;
		velo.clamp(100, 300);
		
		if(lastdecision+decisionrate<Main.time)
		{
			if(Math.random()>0.5)
			{
				attack = true;
				attacktime = Main.time;
				attacked = false;
				offvx = 0;
				offvy = 0;
				nomove = true;
				decisionrate = (float) (basedecisionrate+1+Math.random());
				lastdecision = Main.time;
			}
			else
			{
				decisionrate = (float) ((basedecisionrate+Math.random())*0.6f);
				lastdecision = Main.time;
				offvx = (float) ((Math.random()*200-100));
				offvy = (float) ((Math.random()*250));
			}
		}
		
		
		
		if(!isTurned && velo.x<-20 && turnTest())isTurned = true;
		else if(isTurned && velo.x>20 && turnTest()) isTurned = false;
		
		vx = 0;
		vy = 0;
		
			if(velo.y>0)
			{
				vy+=velo.y+5;
				vy+=offvy;
			}
			else if(velo.y<-250)
			{
				vy+=velo.y;
			}
			else if(velo.y>-100)
			{
				vy+=velo.y/5;
				vy+=offvy*2;
			}
			else
			{
				vy += velo.y/3;
				vy+=offvy*2;
			}
			
			if(velo.x<-700 || velo.x>700)
			{
				vx+=velo.x;
			}
			else if(velo.x>-200 && velo.x<200)
			{
				vx+=velo.x/5;
				vx+=offvx*2;
			}
			else
			{
				vx += velo.x/3;
				vx+=offvx*2;
			}
			
			if(velo.len()<20)
			{
				vy/=2;
				vx/=2;
			}
	}
	
	@Override
	public void premove()
	{
		if(manual)
		{
			if(!Main.pause)
			{
				if(x+manualoffx+38>Tools.getAbsoluteMouse().x)
				{
					if(!isTurned && turnTest())isTurned = true;
				}
				else
				{
					if(isTurned && turnTest()) isTurned = false;
				}
			}
			
			
			if(ax == 0)
			{
				if(vx>0)
				{
					vx -= (Main.delta*speedx*3);
					if(vx<0)vx=0;
				}
				else
				{
					vx += (Main.delta*speedx*3);
					if(vx>0)vx=0;
				}
			}
			
			if(ay == 0)
			{
				if(vy>0)
				{
					vy -= (Main.delta*speedy*3);
					if(vy<0)vy=0;
				}
				else
				{
					vy += (Main.delta*speedy*3);
					if(vy>0)vy=0;
				}
			}
			
			vx+=ax*Main.delta;
			vy+=ay*Main.delta;
			
			if(vy<-speedy)vy = -speedy;
			if(vy>speedy)vy = speedy;
			
			if(vx>speedx)vx = speedx;
			else if(vx<-speedx)vx = -speedx;
		}
	}
	
	@Override
	public void goRight()
	{
		ax = speedx*3;
	}
	
	@Override
	public void goLeft()
	{
		ax = -speedx*3;
	}
	
	@Override
	public void goStandX()
	{
		ax = 0;
	}
	
	@Override
	public void goUp()
	{
		ay = speedy*3;
	}
	
	@Override
	public void goDown()
	{
		ay = -speedy*3;
	}
	
	@Override
	public void goStandY()
	{
		ay = 0;
	}
	
	@Override
	public void goAttack()
	{
		attack = true;
		attacktime = Main.time;
		attacked = false;
	}
	
	@Override
	public void visual()
	{
		if(attack)
		{
			if(attacktime+attackrate*10<Main.time)
			{
				if(!attacked)
				{
					attacked = true;
					Vector2 velo2;
					if(!manual)velo2 = new Vector2(GameScreen.player.middle.x-(x+picpos.x), GameScreen.player.middle.y-(y+picpos.y)).setLength(800);
					else
					{
						Vector2 mo = Tools.getAbsoluteMouse();
						velo2 = new Vector2(mo.x-(x+picpos.x), mo.y-(y+picpos.y)).setLength(800);
					}
					velo2.y+=50;
					float ang = velo2.angle();
					if(!isTurned)
					{
						if(ang>180)
						{
							if(ang<280)ang = 280;
						}
						else
						{
							if(ang>40)ang = 40;
						}
						velo2.setAngle(ang);
					}
					else
					{
						if(ang>180)
						{
							if(ang>260)ang = 260;
						}
						else
						{
							if(ang<140)ang = 140;
						}
						velo2.setAngle(ang);
					}
					
					Entity owner = this;
					if(manual)owner = GameScreen.player;
					
					GameScreen.entities.projectiles.AddProjectile(x+picpos.x, y+picpos.y, velo2.x, velo2.y, 1, owner,power);
					velo2.rotate(15);
					GameScreen.entities.projectiles.AddProjectile(x+picpos.x, y+picpos.y, velo2.x, velo2.y, 1, owner,power);
					velo2.rotate(-30);
					GameScreen.entities.projectiles.AddProjectile(x+picpos.x, y+picpos.y, velo2.x, velo2.y, 1, owner,power);
				}
			}
			
			if(attacktime+attackrate*13<Main.time)
			{
				currentBody = 2;
				attack = false;
			}
			else if(attacktime+attackrate*12<Main.time)
			{
				currentBody = 1;
			}
			else if(attacktime+attackrate*11<Main.time)
			{
				currentBody = 2;
			}
			else if(attacktime+attackrate*10<Main.time)
			{
				currentBody = 3;
			}
			else if(attacktime+attackrate*9<Main.time)
			{
				currentBody = 2;
			}
			else if(attacktime+attackrate*8<Main.time)
			{
				currentBody = 1;
				offvx = velo.x;
				offvy = velo.y;
				nomove = false;
			}
			else
			{
				currentBody = 0;
			}

		}
		
		if(wingrate+lastwing<Main.time)
		{
			lastwing = Main.time;
			currentWing++;
			if(currentWing>=TextureManager.instance.veswings.length)
			{
				currentWing = 0;
			}
		}
		tex.set(0, TextureManager.instance.veswings[currentWing]);
		tex.set(1, TextureManager.instance.vesbody[currentBody]);
	}
}
