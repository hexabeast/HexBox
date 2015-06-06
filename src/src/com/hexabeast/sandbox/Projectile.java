package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Projectile extends Entity{
	
	float currentTime = 0;
	float lifeTime = 30;
	
	boolean isStarted = false;
	boolean isPlanted = false;
	public float damage = 10;
	public float gravity = 750;
	
	public boolean inoffensive = false;
	
	public Entity target;
	public Entity owner;
	public boolean targetAttached;
	public Vector2 relativetarget;
	
	int type = 0;
	
	float x;
	float y;
	Vector2 velocity = new Vector2();
	public Vector2 lastangle = new Vector2(1,1);
	TextureRegion tex;
	float rot;
	
	public Projectile(float x, float y, float vx, float vy, int type, Entity owner, float damage) 
	{
		this.owner = owner;
		currentTime = 0;
		
		velocity.x = vx*3;// + GameScreen.player.velocity.x;
		velocity.y = vy*3;// + GameScreen.player.velocity.y;
		
		if(type == AllTools.instance.ArrowId)tex = TextureManager.instance.arrow;
		else if(type == 1)
		{
			gravity = 200;
			lifeTime = 8;
			tex = TextureManager.instance.vespic;
		}
		
		this.damage = damage;
		
		rot = velocity.angle();
		
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public void attach(Entity e)
	{
		
			target = e;
			target.detach = false;
			targetAttached = true;
			if(!e.isTurned)
			{
				relativetarget = new Vector2(x-e.getX(), y-e.getY());
				lastangle.setAngle(rot);
			}
			else
			{
				relativetarget = new Vector2(-(x-e.getX())+e.offx+e.width, y-e.getY());
				lastangle.setAngle(rot);
				lastangle.x = -lastangle.x;
			}
			inoffensive = true;	
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		super.draw(batch);
		currentTime+=Main.delta;
		if(currentTime>lifeTime && !isDead)
		{
			isDead = true;
		}
		
		if(targetAttached && target!=null && !target.isDead && !target.detach)
		{
			isPlanted = true;
			velocity.x = 0;
			velocity.y = 0;
			if(target.isTurned)
			{
				x = target.getX()-(relativetarget.x-target.offx-target.width);
				y = target.getY()+relativetarget.y;
				lastangle.x = -lastangle.x;
				rot = lastangle.angle();
				lastangle.x = -lastangle.x;
			}
			else
			{
				x = target.getX()+relativetarget.x;
				y = target.getY()+relativetarget.y;
				rot = lastangle.angle();
			}
		}
		else
		{
			targetAttached = false;
			int tx = Tools.floor((x)/16);
			int ty = Tools.floor((y)/16);
			if(Map.instance.mainLayer.getBloc(tx, ty).collide)
			{
				if(!isPlanted)GameScreen.emmiter.addParticles(5, TextureManager.instance.ParticleBlocs[Map.instance.mainLayer.getBloc(tx, ty).Id], x, y);
				isPlanted = true;
				velocity.x = 0;
				velocity.y = 0;
			}
			else
			{
				isPlanted = false;
			}
		}
		
		
		
		if(!isPlanted)
		{
			int tests = (int) Math.max(1, Main.delta*400);
			
			for(int k = 0; k<tests; k++)
			{
				x+=velocity.x*Main.delta/tests;			
				y+=velocity.y*Main.delta/tests;
			
				velocity.y-= gravity*Main.delta/tests;
			
				velocity.x = Math.min(velocity.x, 800);
				velocity.x = Math.max(velocity.x, -800);
				velocity.y = Math.min(velocity.y, 800);
				velocity.y = Math.max(velocity.y, -800);
				
				
				int tx = Tools.floor((x)/16);
				int ty = Tools.floor((y)/16);
				if(Map.instance.mainLayer.getBloc(tx, ty).collide)
				{
					if(!isPlanted)GameScreen.emmiter.addParticles(5, TextureManager.instance.ParticleBlocs[Map.instance.mainLayer.getBloc(tx, ty).Id], x, y);
					isPlanted = true;
					velocity.x = 0;
					velocity.y = 0;
				}
				
				if(!inoffensive && GameScreen.player!=owner)
				{
					boolean touched = false;
					ArrayList<Rectangle> rects = GameScreen.player.getHitRect();
					
					for(int i = 0; i<rects.size(); i++)
					{
						if(rects.get(i).contains(x-(GameScreen.player.x+GameScreen.player.transoffx), y-(GameScreen.player.y+GameScreen.player.transoffy)))
						{
							touched = true;
							break;
						}
					}
					if(touched)
					{
						GameScreen.player.Hurt(damage, 0, 0, x, y);
						attach(GameScreen.player);
					}
				}
				
				if(!inoffensive && AllEntities.getType(Tools.floor(x/16), Tools.floor(y/16)) == AllEntities.mobtype)
				{
					int id = AllEntities.getItem(Tools.floor(x/16), Tools.floor(y/16))-Constants.chestlimit;
					
					if( owner !=GameScreen.entities.mobs.mobListAll.get(id))
					{
						boolean touched = false;
						ArrayList<Rectangle> rects = GameScreen.entities.mobs.mobListAll.get(id).hitrect.getRects(GameScreen.entities.mobs.mobListAll.get(id).isTurned);
						
						for(int i = 0; i<rects.size(); i++)
						{
							if(rects.get(i).contains(x-GameScreen.entities.mobs.mobListAll.get(id).x, y-GameScreen.entities.mobs.mobListAll.get(id).y))
							{
								touched = true;
								break;
							}
						}
						if(touched)
						{
							if(GameScreen.entities.mobs.mobListAll.get(id).attach)attach(GameScreen.entities.mobs.mobListAll.get(id));
							else inoffensive = true;
							GameScreen.entities.mobs.mobListAll.get(id).damage(damage,0,x,y);
						}
					}
				}			
			}	
		}
		else
		{
			if(type>999 && !targetAttached && Math.abs(x-GameScreen.player.middle.x)<GameScreen.player.width/3 && Math.abs(y-GameScreen.player.middle.y)<GameScreen.player.height/3+16)
			{
				if(!GameScreen.inventory.isFull(type))
				{
					GameScreen.inventory.PutItem(type);
				    SoundManager.instance.PickUp.play();
				    Tools.checkItems();
				    isDead = true;
				}
			}
		}
		
		if(Math.abs(velocity.x)>0.01f || Math.abs(velocity.y)>0.01f)rot = Tools.fLerpAngle(rot, velocity.angle(), 10);
		
		if(!Parameters.i.fullBright)
		{
			Vector3 color = Tools.getShadowColor(Tools.floor (x/16),Tools.floor (y/16));
			batch.setColor(color.x,color.y,color.z,1);
		}
	
		batch.draw(tex, x-tex.getRegionWidth()+4, y-tex.getRegionHeight()/2, tex.getRegionWidth()-4, tex.getRegionHeight()/2, tex.getRegionWidth(), tex.getRegionHeight(), 1, 1, rot);
		if(!Parameters.i.fullBright)batch.setColor(Color.WHITE);
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
