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
	public Vector2 corde = new Vector2(1,0);
	
	public ArrayList<HookBreakPoint> breakpoints = new ArrayList<HookBreakPoint>();
	public float breakLastAngle;

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
		
		breakpoints.add(new HookBreakPoint(x,y,true));
		
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
				
				breakpoints.get(0).x = x;
				breakpoints.get(0).y = y;
			
				velocity.y-= gravity*Main.delta/tests;
			
				velocity.clamp(0, 1500);
				max = liaison.len()+32;
			}
		}
		
		//TRYING TO BREAKPOINT
		if(isPlanted)
		{
			Vector2 tempLiaison = new Vector2(breakpoints.get(breakpoints.size()-1).x-GameScreen.player.PNJ.hookAnchorCoord.x, breakpoints.get(breakpoints.size()-1).y-GameScreen.player.PNJ.hookAnchorCoord.y);
			float currentAngle = breakLastAngle;
			breakLastAngle = tempLiaison.angle();
			int l = (int) tempLiaison.len();
			float i = 0;
			
			boolean added = false;
			
			//RAYCAST
			while(i<l-1)
			{
				float tx = GameScreen.player.PNJ.hookAnchorCoord.x+(tempLiaison.x*i)/l;
				float ty = GameScreen.player.PNJ.hookAnchorCoord.y+(tempLiaison.y*i)/l;
				
				//COLLIDE = BREAKPOINT
				if(Map.instance.mainLayer.getBloc( (int)(tx/16), (int)(ty/16)).collide)
				{
					float fangle = breakLastAngle-currentAngle;
					while(fangle>180)fangle-=360;
					while(fangle<-180)fangle+=360;
					//TODO fangle useless
					breakpoints.add(new HookBreakPoint(tx,ty,fangle>0));
					added = true;
					break;
				}
				
				i+=0.5f;
			}
			
			//REMOVE BREAKPOINTS
			if(breakpoints.size()>1 && !added)
			{
				boolean pass = true;
				Vector2 tempLiaisonFar = new Vector2(breakpoints.get(breakpoints.size()-2).x-GameScreen.player.PNJ.hookAnchorCoord.x, breakpoints.get(breakpoints.size()-2).y-GameScreen.player.PNJ.hookAnchorCoord.y);
				int l2 = (int) tempLiaisonFar.len();
				
				Vector2 tempLiaisonBreak = new Vector2(breakpoints.get(breakpoints.size()-2).x-breakpoints.get(breakpoints.size()-1).x, breakpoints.get(breakpoints.size()-2).y-breakpoints.get(breakpoints.size()-1).y);
				float angle = tempLiaisonBreak.angle(tempLiaison);
				
				
				//CHECK COLLISIONS
				i = 0;
				//l = (int) tempLiaisonFar.len();
				while(i<l+1)
				{
					float tx = GameScreen.player.PNJ.hookAnchorCoord.x+(tempLiaisonFar.x*i)/l2;
					float ty = GameScreen.player.PNJ.hookAnchorCoord.y+(tempLiaisonFar.y*i)/l2;
					
					if(Map.instance.mainLayer.getBloc( (int)(tx/16), (int)(ty/16)).collide)
					{
						pass = false;
						break;
					}
					
					i+=2;
				}
			
				if(pass || true)
				{
					//SEGMENT TEST FOR ORIENTATION
					Vector2 orthogonalVec = new Vector2(tempLiaison);
					orthogonalVec.rotate(90);
					orthogonalVec.setLength(8);
					
					boolean ccwCollide = Map.instance.mainLayer.getBloc( (int)((breakpoints.get(breakpoints.size()-1).x+orthogonalVec.x)/16), (int)((breakpoints.get(breakpoints.size()-1).y+orthogonalVec.y)/16)).collide;
					boolean cwCollide = Map.instance.mainLayer.getBloc( (int)((breakpoints.get(breakpoints.size()-1).x-orthogonalVec.x)/16), (int)((breakpoints.get(breakpoints.size()-1).y-orthogonalVec.y)/16)).collide;
					
					//System.out.println(tempLiaisonBreak.angle(tempLiaison)<0);
					//if(angle>0 != breakpoints.get(breakpoints.size()-1).clockwise)
					
					//REMOVING
					if((!cwCollide && angle>=0 )|| (!ccwCollide && angle<=0))
					{
						breakpoints.remove(breakpoints.size()-1);
					}
				}
			}
		}
		
		if(playerAttached)playerAttachedOnce = true;
		
		//REDUCING ROPE IF DETACHED
		corde.clamp(0, Math.max(0, corde.len()-1000*Main.delta));
		if(playerAttached)
		{
			corde.x = owner.hookAnchorCoord.x-x;
			corde.y = owner.hookAnchorCoord.y-2-y;
		}
		
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
			if(max<verymax-10)max+=(new Vector2(GameScreen.player.PNJ.vx,GameScreen.player.PNJ.vy).len()+100)*Main.delta;
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
		if(playerAttachedOnce && corde.len()>1)
		{
			for(int i = 0; i<breakpoints.size()-1; i++)
			{
				Tools.drawLine(ropeTex, breakpoints.get(i+1).x, breakpoints.get(i+1).y, breakpoints.get(i).x, breakpoints.get(i).y, 2);
			}
			Tools.drawLine(ropeTex, corde.x+x, corde.y+y,breakpoints.get(breakpoints.size()-1).x, breakpoints.get(breakpoints.size()-1).y);
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
		l+=getUselessLen();
		l+=new Vector2(owner.hookAnchorCoord.x-breakpoints.get(breakpoints.size()-1).x, owner.hookAnchorCoord.y-breakpoints.get(breakpoints.size()-1).y).len();
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
