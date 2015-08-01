package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.mobs.PNJ;

public class Grapple extends Entity{
	public float currentTime = 0;
	public float lifeTime = 5;
	
	public boolean isStarted = false;
	public boolean isPlanted = false;
	public boolean playerAttached = false;
	public boolean playerAttachedOnce = false;

	public float verymin = 8;
	public float verymax = 1500;
	public float max = 0;
	public Vector2 grapfront = new Vector2(16,0);
	public Vector2 corde = new Vector2(1,0);

	public float gravity = 400;
	
	public float vmultiplier = 4;
	public boolean todetach = false;
	
	public Vector2 liaison = new Vector2();
	
	public float x;
	public float y;
	public Vector2 velocity = new Vector2();
	public Vector2 lastangle = new Vector2(1,1);
	public TextureRegion tex;
	public Texture ropeTex;
	public float rot;
	
	public boolean justspawned = true;
	
	public PNJ owner;
	
	public Grapple(PNJ owner, float x, float y, float vx, float vy, float distance, TextureRegion tex, Texture ropeTex) 
	{
		
		this.owner = owner;
		justspawned = true;
		this.tex = tex;
		this.ropeTex = ropeTex;
		verymax = distance;
		
		currentTime = 0;
		
		playerAttached = true;
		
		velocity.x = vx*vmultiplier;
		velocity.y = vy*vmultiplier;
		
		rot = velocity.angle();
		
		this.x = x;
		this.y = y;
	}
	
	
	@Override
	public void draw(SpriteBatch batch)
	{
		super.draw(batch);
		
		liaison.x = GameScreen.player.PNJ.hookAnchorCoord.x-x;
		liaison.y = GameScreen.player.PNJ.hookAnchorCoord.y-y;
		
		if(Math.abs(velocity.x)>0.01f || Math.abs(velocity.y)>0.01f)rot = Tools.fLerpAngle(rot, velocity.angle(), 10);
		grapfront.setAngle(rot);
		
		if(!isPlanted && verymax<liaison.len())playerAttached = false;
		
		if(!playerAttached)currentTime+=Main.delta;
		if(currentTime>lifeTime && !isDead)
		{
			isDead = true;
		}
		
		
		int tests = (int) Math.max(1, Main.delta*500);
		
		for(int k = 0; k<tests; k++)
		{
			
			int grapx = Tools.floor((x+grapfront.x)/16);
			int grapy = Tools.floor((y+grapfront.y)/16);
			
			int minx = Tools.floor((x)/16);
			int miny = Tools.floor((y)/16);
			int maxx = grapx;
			int maxy = grapy;
			
			if(minx>maxx)
			{
				int t = minx;
				minx = maxx;
				maxx = t;
			}
			
			if(miny>maxy)
			{
				int t = miny;
				miny = maxy;
				maxy = t;
			}
			
			boolean wasPlanted = isPlanted;
			isPlanted = false;
			for(int i = minx; i<=maxx; i++)
			{
				for(int j = miny; j<=maxy; j++)
				{
					if(Map.instance.mainLayer.getBloc(i, j).collide)
					{
						if(!wasPlanted)
						{
							GameScreen.emmiter.addParticles(8, TextureManager.instance.ParticleBlocs[Map.instance.mainLayer.getBloc(i, j).Id], x+grapfront.x, y+grapfront.y);
							wasPlanted = true;
						}
						isPlanted = true;
						velocity.x = 0;
						velocity.y = 0;
					}
				}
			}
			
			if(!isPlanted)
			{
				x+=velocity.x*Main.delta/tests;			
				y+=velocity.y*Main.delta/tests;
			
				velocity.y-= gravity*Main.delta/tests;
			
				velocity.clamp(0, 1500);
				max = liaison.len()+32;
			}
		}
		
		if(playerAttached)playerAttachedOnce = true;
		
		corde.clamp(0, Math.max(0, corde.len()-1000*Main.delta));
		if(playerAttached)
		{
			corde.x = owner.hookAnchorCoord.x-x;
			corde.y = owner.hookAnchorCoord.y-2-y;
		}
		
		if(todetach)playerAttached = false;
		
		if(((owner.rightPress && AllTools.instance.getType(GameScreen.player.currentCellID).grapple) || owner.upPressed)&& isPlanted && playerAttached)
		{
			if(max>verymin)max-=500*Main.delta;
			else max = verymin;
		}
		
		if((owner.downPressed)&& isPlanted && playerAttached)
		{
			if(max<verymax-10)max+=500*Main.delta;
			else max = verymax-10;
		}
		///if(Inputs.instance.leftmousedown && AllTools.instance.getType(GameScreen.player.currentCellID).grapple && !justspawned && playerAttached)todetach=true;
		//if(Inputs.instance.middleOrAPressed && !justspawned && playerAttached)todetach=true;
	
		if(!Parameters.i.fullBright)
		{
			Vector3 color = Tools.getShadowColor(Tools.floor (x/16),Tools.floor (y/16));
			batch.setColor(color.x,color.y,color.z,1);
		}
		
		if(playerAttachedOnce && corde.len()>1)
		{
			Tools.drawLine(ropeTex, x, y, corde.x+x, corde.y+y);
		}
		
		grapfront.setAngle(rot);
		
		batch.draw(tex, x-tex.getRegionWidth()+4+grapfront.x*0.8f, y-tex.getRegionHeight()/2+grapfront.y*0.8f, tex.getRegionWidth()-4, tex.getRegionHeight()/2, tex.getRegionWidth(), tex.getRegionHeight(), 1, 1, rot);
		if(!Parameters.i.fullBright)batch.setColor(Color.WHITE);
		
		justspawned = false;
	}
	
	public Vector2 getLine()
	{
		return new Vector2(owner.hookAnchorCoord.x-x, owner.hookAnchorCoord.y-y);
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
