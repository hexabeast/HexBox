package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.mobs.PNJ;
import com.hexabeast.sandbox.smallclasses.HookBreakPoint;

public class Grapple extends Entity{
	
	public float currentTime = 0;
	public float lifeTime = 5;
	
	public boolean isStarted = false;
	public boolean isPlanted = false;
	public boolean playerAttached = false;
	public boolean playerAttachedOnce = false;
	
	public float maxSpeed = 800;
	public float currentSpeed = 0;

	public float verymin = 8;
	public float verymax = 1500;
	public float max = 0;
	public Vector2 grapfront = new Vector2(16,0);
	
	public ArrayList<HookBreakPoint> breakpoints = new ArrayList<HookBreakPoint>();

	public float gravity = 400;
	
	public float vmultiplier = 4;
	public boolean todetach = false;
	
	public Vector2 liaison = new Vector2();
	
	public float x;
	public float y;
	
	public Vector2 linkPlayer = new Vector2();
	
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
		
		breakpoints.add(new HookBreakPoint(x,y));
		
		this.x = x;
		this.y = y;
		
		linkPlayer.x = x;
		linkPlayer.y = y;
	}
	
	public void updateLink(float m)
	{
		//LINK TO THE PLAYER
		if(playerAttached)
		{
			linkPlayer.x = GameScreen.player.PNJ.hookAnchorCoord.x;
			linkPlayer.y = GameScreen.player.PNJ.hookAnchorCoord.y;
		}
		else
		{
			
			//TELEPORTOTHERSIDE:
			if(linkPlayer.x<2000 && GameScreen.player.PNJ.x>(Map.instance.width)*8)
			{
				float xii = (Map.instance.width)*16+(linkPlayer.x);
				linkPlayer.x = (xii);
			}
			if(linkPlayer.x>=(Map.instance.width)*16-2000 && GameScreen.player.PNJ.x<(Map.instance.width)*8)
			{
				float xii = 0+(linkPlayer.x-(Map.instance.width)*16);
				linkPlayer.x = (xii);
			}
			
			//IF DETACHED RETRACTATION
			if(breakpoints.size()>0)
			{
				Vector2 corde = new Vector2(breakpoints.get(breakpoints.size()-1).x-linkPlayer.x,breakpoints.get(breakpoints.size()-1).y-linkPlayer.y);
				corde.clamp(0, Math.max(0, corde.len()-m*1500*Main.delta));
				linkPlayer.x = breakpoints.get(breakpoints.size()-1).x-corde.x;
				linkPlayer.y = breakpoints.get(breakpoints.size()-1).y-corde.y;
				if(corde.len()<0.1f)breakpoints.remove(breakpoints.size()-1);
			}
		}
	}
	
	
	@Override
	public void draw(SpriteBatch batch)
	{
		super.draw(batch);
		
		for(int j = 0; j<breakpoints.size(); j++)
		{
			breakpoints.get(j).tpOtherSide();
		}
		
		updateLink(1);
		
		liaison.x = linkPlayer.x-x;
		liaison.y = linkPlayer.y-y;
		
		if(Math.abs(velocity.x)>0.01f || Math.abs(velocity.y)>0.01f)rot = Tools.fLerpAngle(rot, velocity.angle(), 10);
		grapfront.setAngle(rot);
		
		if(!isPlanted && verymax<liaison.len())playerAttached = false;
		
		if(!playerAttached)currentTime+=Main.delta;
		if(currentTime>lifeTime && !isDead)
		{
			isDead = true;
		}
		
		int tests = (int) Math.max(1, Main.delta*500);
		
		//MOVING
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
			
			//PLANTING
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
			
			//MOVING IF NOT PLANTED
			if(!isPlanted)
			{
				x+=velocity.x*Main.delta/tests;			
				y+=velocity.y*Main.delta/tests;
				
				if(breakpoints.size()>0)
				{
					breakpoints.get(0).x = x;
					breakpoints.get(0).y = y;
				}
			
				velocity.y-= gravity*Main.delta/tests;
			
				velocity.clamp(0, 1500);
				max = liaison.len()+32;
			}
		}
		
		//TRYING TO BREAKPOINT
		if(playerAttached)
		{
			boolean added = false;
			for(int j = 0; j<breakpoints.size(); j++)
			{
				int k = j+1;
				if(k>=breakpoints.size())k=breakpoints.size()-1;
				if(breakpoints.size()-1 == j || breakpoints.get(j).floating || breakpoints.get(k).floating)
				{
					float linkx = linkPlayer.x;
					float linky = linkPlayer.y;
					if(j+1<breakpoints.size())
					{
						linkx = breakpoints.get(j+1).x;
						linky = breakpoints.get(j+1).y;
					}
					
					Vector2 tempLiaison = new Vector2(breakpoints.get(j).x-linkx, breakpoints.get(j).y-linky);
					int l = (int) tempLiaison.len();
					float i = 0;
					
					//RAYCAST
					while(i<l-1)
					{
						float tx = linkx+(tempLiaison.x*i)/l;
						float ty = linky+(tempLiaison.y*i)/l;
						
						//COLLIDE = BREAKPOINT
						if(Map.instance.mainLayer.getBloc( Tools.floor(tx/16), Tools.floor(ty/16)).collide)
						{
							if(j<breakpoints.size()-1)
							{
								breakpoints.add(j+1,new HookBreakPoint(tx,ty));
							}
							else
							breakpoints.add(new HookBreakPoint(tx,ty));
							
							added = true;
							break;
						}
						i+=0.5f;
					}
				}
			}
			
			//REMOVE BREAKPOINTS
			if(breakpoints.size()>1 && !added)
			{
				for(int j = 1; j<breakpoints.size(); j++)
				{
					float linkx = linkPlayer.x;
					float linky = linkPlayer.y;
					if(j+1<breakpoints.size())
					{
						linkx = breakpoints.get(j+1).x;
						linky = breakpoints.get(j+1).y;
					}
					
					Vector2 tempLiaisonLink = new Vector2(breakpoints.get(j).x-linkx, breakpoints.get(j).y-linky);
					Vector2 tempLiaisonBreak = new Vector2(breakpoints.get(j-1).x-breakpoints.get(j).x, breakpoints.get(j-1).y-breakpoints.get(j).y);
					float angle = tempLiaisonBreak.angle(tempLiaisonLink);

					//SEGMENT TEST FOR ORIENTATION
					Vector2 orthogonalVec = new Vector2(tempLiaisonLink);
					orthogonalVec.rotate(90);
					orthogonalVec.setLength(6);
					
					boolean ccwCollide = Map.instance.mainLayer.getBloc( Tools.floor((breakpoints.get(j).x+orthogonalVec.x)/16),Tools.floor((breakpoints.get(j).y+orthogonalVec.y)/16)).collide;
					boolean cwCollide = Map.instance.mainLayer.getBloc( Tools.floor((breakpoints.get(j).x-orthogonalVec.x)/16), Tools.floor((breakpoints.get(j).y-orthogonalVec.y)/16)).collide;
					
					//REMOVING
					if((!cwCollide && angle>=0 )|| (!ccwCollide && angle<=0))
					{
						if(Math.abs(angle)<4)breakpoints.remove(j);
						else breakpoints.get(j).floating = true;
					}
				}
			}
			
			//MOVE FLOATING POINTS
			for(int j = 1; j<breakpoints.size(); j++)
			{
				if(breakpoints.get(j).floating)
				{
					float linkx = linkPlayer.x;
					float linky = linkPlayer.y;
					if(j+1<breakpoints.size())
					{
						linkx = breakpoints.get(j+1).x;
						linky = breakpoints.get(j+1).y;
					}
					Vector2 tempLiaisonFar = new Vector2(breakpoints.get(j-1).x-linkx, breakpoints.get(j-1).y-linky);
					
					Vector2 direction = new Vector2(10000000,0);
					
					for(int k = 0; k<20; k++)
					{
						Vector2 middleFar = new Vector2(tempLiaisonFar.x*(float)(k)/19f+linkx, tempLiaisonFar.y*(float)(k)/19f+linky);
						Vector2 tdirection = new Vector2(middleFar.x-breakpoints.get(j).x, middleFar.y-breakpoints.get(j).y);
						if(tdirection.len()<direction.len())direction = tdirection;
					}
					
					float le = direction.len();
					
					direction.clamp(150, 150);
					
					breakpoints.get(j).x+=direction.x*Main.delta;
					breakpoints.get(j).y+=direction.y*Main.delta;
					
					if(le<2)breakpoints.remove(j);
				}
			}
		}
		
		if(playerAttached)playerAttachedOnce = true;
		
		
		if(todetach)playerAttached = false;
		
		if(((owner.rightPress && AllTools.instance.getType(GameScreen.player.currentCellID).grapple) || owner.upPressed)&& isPlanted && playerAttached)
		{
			if(max>verymin)max-=(3f/4f)*currentSpeed*Main.delta;
			else max = verymin;
			currentSpeed+=maxSpeed*Main.delta*3;
			if(currentSpeed>maxSpeed)currentSpeed=maxSpeed;
		}
		
		
		//HOOK LENGTH
		else if((owner.downPressed)&& isPlanted && playerAttached)
		{
			if(max<verymax-10)max+=(new Vector2(owner.vx,owner.vy).len()+100)*Main.delta;
			else max = verymax-10;
		}
		else
		{
			currentSpeed-=maxSpeed*Main.delta*5;
			if(currentSpeed<maxSpeed/3)currentSpeed=maxSpeed/3;
		}
		
		float tlen = getLen();
		if(max>tlen+20)max=tlen+20;
		
		///if(Inputs.instance.leftmousedown && AllTools.instance.getType(GameScreen.player.currentCellID).grapple && !justspawned && playerAttached)todetach=true;
		//if(Inputs.instance.middleOrAPressed && !justspawned && playerAttached)todetach=true;
	
		if(!Parameters.i.fullBright)
		{
			Vector3 color = Tools.getShadowColor(Tools.floor (x/16),Tools.floor (y/16));
			batch.setColor(color.x,color.y,color.z,1);
		}
		
		//DISPLAY
		if(playerAttachedOnce && breakpoints.size()>0)
		{
			for(int i = 0; i<breakpoints.size()-1; i++)
			{
				Tools.drawLine(ropeTex, breakpoints.get(i+1).x, breakpoints.get(i+1).y, breakpoints.get(i).x, breakpoints.get(i).y, 2);
			}
			Tools.drawLine(ropeTex, linkPlayer.x, linkPlayer.y,breakpoints.get(breakpoints.size()-1).x, breakpoints.get(breakpoints.size()-1).y);
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
	
	public float getLen()
	{
		float l = 0;
		if(breakpoints.size()>0)
		{
			l+=getUselessLen();
			l+=new Vector2(owner.hookAnchorCoord.x-breakpoints.get(breakpoints.size()-1).x, owner.hookAnchorCoord.y-breakpoints.get(breakpoints.size()-1).y).len();
		}
		return l;
	}
	
	public float getUselessLen()
	{
		float l = 0;
		for(int i = 0; i<breakpoints.size()-1; i++)
		{
			l+= new Vector2(breakpoints.get(i).x-breakpoints.get(i+1).x, breakpoints.get(i).y - breakpoints.get(i+1).y).len();
		}
		return l;
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
	 
	 public float gx()
	 {
		 return  breakpoints.get(breakpoints.size()-1).x;
	 }
	 
	 public float gy()
	 {
		 return  breakpoints.get(breakpoints.size()-1).y;
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
