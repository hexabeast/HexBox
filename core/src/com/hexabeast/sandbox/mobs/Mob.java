package com.hexabeast.sandbox.mobs;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.AllEntities;
import com.hexabeast.sandbox.Entity;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.Main;
import com.hexabeast.sandbox.Parameters;
import com.hexabeast.sandbox.Shaders;
import com.hexabeast.sandbox.TextureManager;
import com.hexabeast.sandbox.ToolType;
import com.hexabeast.sandbox.Tools;

public class Mob extends Entity 
{
	public int id = 0;
	
	//TEXTURES
	public ArrayList<TextureRegion> tex = new ArrayList<TextureRegion>();
	
	
	//POSITION / VELOCITY / ACCELERATION / SIZE
	
	public float x = 0;
	public float y = 0;
	public float vx = 0;
	public float vy = 0;
	public float ax = 0;
	public float ay = 0;
	
	public int casesX = 1;
	public int casesY = 1;
	
	public float manualoffx = 0;
	
	Vector2 VisorPos = new Vector2();
	
	//PHYSICS
	
	public float speedx = 250;
	public float speedy = 1000;
	
	public HitBox hitbox;
	public HitRect hitrect;
	
	public boolean canJump = false;
	
	//TIMERS
	
	public boolean attacked = false;
	public float redrate = 0.05f;
	public float lastred = 0;
	
	public boolean damaged = false;
	public float lastdamaged = 0;
	public float damagerate = 0.1f;
	
	//CARACTERISTICS
	
	public float maxHealth = 100;
	public int type = 0;
	public float health = 100;
	public float defense = 100;
	public float power = 10;
	
	public boolean manual = false;
	public boolean isMain = false;
	
	
	
	public void superDraw(SpriteBatch batch)
	{
		super.draw(batch);
	}
	
	public void calculateSize(int tid)
	{
		width = tex.get(tid).getRegionWidth()*2;
		height = tex.get(tid).getRegionHeight()*2;
		casesX = Tools.floor(width/16)+1;
		casesY = Tools.floor(height/16)+1;
	}

	
	public void checkPoints()
	{
		ArrayList<Rectangle> rects = hitrect.getRects(isTurned);
		for(int i = 0; i<rects.size(); i++)
		{
			checkRect(rects.get(i));
		}
	}
	
	public void Hurt(float d, float immortality, float x, float y)
	{
		if(!damaged)
		{
			d*=(100f/defense);
			
			d = Math.max(d,1);
			
			if(immortality>0)
			{
				damagerate = immortality;
				damaged = true;
				lastdamaged = Main.time;
			}
			
			health-=d;
			if(health<=0)isDead = true;
			lastred = Main.time;
			attacked = true;
			
			GameScreen.emmiter.addnewText((int)d, x, y);
		}
	}
	
	public void checkRect(Rectangle rec)
	{
		for(int i = Tools.floor((x+rec.x)/16); i<=Tools.floor((x+rec.x+rec.width)/16); i++)
		{
			for(int j = Tools.floor((y+rec.y)/16); j<=Tools.floor((y+rec.y+rec.height)/16); j++)
			{
				AllEntities.setMap(i,j,this);
			}
		}
	}
	
	public boolean turnTest()
	{
		hitbox.isTurned = !isTurned;
		if(hitbox.TestCollisions(true) || (hitbox.TestCollisions(false)))
		{
			hitbox.isTurned = isTurned;
			return false;
		}
		return true;
	}
	
	public void move()
	{
		float oldX= x;
		float oldY = y;
		
		y+=vy*Main.delta;

		hitbox.update(x,y);
		
		canJump = false;
		if(hitbox.TestCollisions(true) || (hitbox.TestCollisions(false) && vy<=0))
		{
			y = oldY;
			vy = 0;
		}		
		if(hitbox.TestCollisionsDown() || hitbox.TestCollisions(false))
		{
			canJump = true;
		}
		
		hitbox.update(x,y);
		
		boolean hiking = false;
		if(hitbox.TestCollisions(false))hiking = true;
		
		x+=vx*Main.delta;
		hitbox.update(x,y);
		
		if(hitbox.TestCollisions(true) || (hitbox.TestCollisionsHigh() && !hiking && hitbox.TestCollisions(false)))
		{
			x = oldX;
			vx = 0;
		}
		else if(hitbox.TestCollisions(false))
		{
			oldY = y;
			y+=Math.abs(speedx)*2.4f*Main.delta;
			hitbox.update(x,y);	
			if(hitbox.TestCollisions(true))
			{
				y = oldY;
				y+=speedx*0.2f*Main.delta;
				hitbox.update(x,y);	
				if(hitbox.TestCollisions(true))
				{
					y = oldY;
				}
			}
			
		}
		hitbox.update(x,y);
	}
	
	public void visual(){}
	
	public void premove(){}
	public void preinput(){}
	
	public void graphicDraw(SpriteBatch batch)
	{
		for(int i = 0; i<tex.size(); i++)
		{
			if(!isTurned)batch.draw(tex.get(i), x, y, width, height);
			else batch.draw(tex.get(i), x+width+offx, y, -width, height);
		}
	}
	
	public void update()
	{
		if(!manual)IA();
		visual();
		
		hitbox.isTurned = isTurned;
		
		premove();
		move();
		
		if(damaged && Main.time>lastdamaged+damagerate)
		{
			damaged = false;
		}
		
		if(attacked && Main.time>lastred+redrate)attacked = false;
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		
		Vector3 color = Tools.getShadowColor(Tools.floor(x/16)+Tools.floor(casesX/2),Tools.floor(y/16)+Tools.floor(casesY/2));
		if(attacked)
		{
			Shaders.instance.setRedShader();
		}
		else
		{
			batch.setColor(color.x,color.y,color.z,1);
		}
		
		graphicDraw(batch);
		
		if(attacked)
		{
			Shaders.instance.setShadowShader();
		}
		else
		{
			batch.setColor(1,1,1,1);
		}
		
		
		if(Parameters.i.drawhitbox)
		{
			Color old = batch.getColor();
			hitbox.drawHitBox();
			batch.setColor(1, 0, 0, 0.4f);
			for(int i = 0; i<hitrect.normal.size(); i++)
			{
				batch.draw(TextureManager.instance.blank, x+hitrect.getRects(isTurned).get(i).x, y+hitrect.getRects(isTurned).get(i).y, hitrect.getRects(isTurned).get(i).width, hitrect.getRects(isTurned).get(i).height);
			}
			batch.setColor(old);
		}
	}
	
	public void IA()
	{
		
	}
	
	public void goRight(){}
	public void goLeft(){}
	public void goUp(){}
	public void goDown(){}
	public void goJump(){}
	public void goStandX(){}
	public void goStandY(){}
	public void goClickLeftInstant(){}
	public void goClickRightInstant(){}
	public void goClickLeftPressed(){}
	public void goClickRightPressed(){}
	public void setItemId(int item){}
	public void setVisorPos(Vector2 v)
	{
		VisorPos.x = v.x;
		VisorPos.y = v.y;
	}
	public void setEquipment(ToolType equip){}
	public void setHelmet(ToolType equip){}
	public void setArmor(ToolType equip){}
	public void setGlove(ToolType equip){}
	public void setLeggins(ToolType equip){}
	public void setHook(ToolType equip){}
	public void goHook(){}
	
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
