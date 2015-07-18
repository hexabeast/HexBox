package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.mobs.Mob;

public class MagicProjectile extends Entity{
	PooledEffect particle;
	PooledEffect particle2;
	
	TextureRegion tex;
	
	public boolean simple = true;
	
	float currentTime = 0;
	float lifeTime = 5;
	float damage = 1;
	
	boolean isStarted = false;
	boolean isExploded = false;
	boolean isExplodedEnd = false;
	
	public Entity owner;
	
	int type = 0;
	
	float x;
	float y;
	Vector2 velocity = new Vector2();
	Vector3 color = new Vector3(0.2f,1,0.2f);
	float colorMultiplier = 2.5f;
	public MagicProjectile(float x, float y, float vx, float vy, int type, float damage, boolean simple, Entity owner) {
		
		this.damage = damage;
		this.owner = owner;
		
		currentTime = 0;
		
		velocity.x = vx;
		velocity.y = vy;
		
		if(type == 0)
		{
			velocity.y/=8;
			velocity.x/=8;
		}
		
		if(type == 1)
		{
			velocity.y = vy*2+150;
			color = new Vector3(1,0.4f,0.24f);
		}
		else if(type == 2)
		{
			velocity.y = vy*2+100;
			color = new Vector3(0.8f,0.2f,0.8f);
		}
		else if(type == 3)
		{
			lifeTime=10;
			color = new Vector3(0.4f,0.4f,1f);
		}
		else if(type == 4)
		{
			velocity.y*=2;
			velocity.x*=2;
			color = Vector3.Zero;
		}
		
		this.x = x;
		this.y = y;
		this.type = type;
		this.simple = simple;
		if(simple)
		{
			tex = TextureManager.instance.particle;
		}
		else
		{
			particle = GameScreen.entities.projectiles.effectPools[type].obtain();
			particle2 = GameScreen.entities.projectiles.effectPools2[type].obtain();
		}
		
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		super.draw(batch);
		currentTime+=Main.delta;
		if(currentTime>lifeTime && !isExploded)
		{
			Explode();
		}
		
		if(currentTime>lifeTime+1f)isDead = true;
		
		if(currentTime>lifeTime+0.2f)isExplodedEnd = true;
		
		
		
		if(!isExploded)
		{	
			
			boolean touched = false;
			ArrayList<Rectangle> rects = GameScreen.player.getHitRect();
			
			if(GameScreen.player!=owner)
			{
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
					GameScreen.player.Hurt(damage, 0, 0,x,y);
					Explode();
				}
			}
			
	
			if(AllEntities.getType((int)(x/16), (int)(y/16)) == AllEntities.mobtype)
			{
				Mob m = (Mob)AllEntities.getEntity((int)(x/16), (int)(y/16));
				if(m!=owner)
				{
					touched = false;
					rects = m.hitrect.getRects(m.isTurned);
					
					for(int i = 0; i<rects.size(); i++)
					{
						if(rects.get(i).contains(x-m.x, y-m.y))
						{
							touched = true;
							break;
						}
					}
					if(touched)
					{
						Explode();
						
						m.damage(damage,0,x,y);
					}
				}
			}
			
			if(type!= 2)
			{
				x+=velocity.x*Main.delta;
				y+=velocity.y*Main.delta;
				
				if(Map.instance.mainLayer.getBloc(Tools.floor(x/16), Tools.floor(y/16)).collide)
				{
					if(!Parameters.i.cheatMagic)Explode();
				}
			}
			else
			{
				float oldX = x;
				float oldY = y;
				x+=velocity.x*Main.delta;
				if(Map.instance.mainLayer.getBloc(Tools.floor(x/16), Tools.floor(y/16)).collide)
				{
					velocity.x = -velocity.x*0.8f;
					x = oldX;
				}
				
				y+=velocity.y*Main.delta;
				if(Map.instance.mainLayer.getBloc(Tools.floor(x/16), Tools.floor(y/16)).collide)
				{
					velocity.y = -velocity.y*0.8f;
					y = oldY;
				}
			}
			
			if(color!=Vector3.Zero)Map.instance.lights.tempLight(x, y,color.x*colorMultiplier, color.y*colorMultiplier, color.z*colorMultiplier);
		}
		else if(!isExplodedEnd)
		{
			if(color!=Vector3.Zero)Map.instance.lights.tempLight(x, y,color.x*colorMultiplier*1.2f, color.y*colorMultiplier*1.2f, color.z*colorMultiplier*1.2f);
		}
		
		if(type == 0)
		{
			float l = velocity.len();
			velocity.setLength(l+700*Main.delta);
		}
		
		if(type == 1)
		{
			//velocity.x*= 1-0.1f*Main.delta;
			velocity.y-= 500*Main.delta;
		}
		
		if(type == 2)
		{
			velocity.x*= 1-0.1f*Main.delta;
			velocity.y-= 700*Main.delta;
		}
		
		if(type == 3 && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
			Vector2 mouse = Tools.getAbsoluteMouse();
			Vector2 mouse2 = new Vector2(mouse.x-x, mouse.y-y).setLength(3000);
			velocity.x+= mouse2.x*Main.delta;
			velocity.y+= mouse2.y*Main.delta;
		}
		
		if(type != 3)	
		{
			velocity.clamp(0, 1000);
		}
		else
		{
			velocity.x = Math.min(Math.max(velocity.x, -600), 600);
			velocity.y = Math.min(Math.max(velocity.y, -600), 600);
		}
		
		if(Parameters.i.cheatMagic)
		{
			int tx = Tools.floor(x/16);
			int ty = Tools.floor(y/16);
			if(ty>1 && ty<Map.instance.height-2 && Map.instance.mainLayer.getBloc(tx, ty).Id != 0)
			{
				ModifyTerrain.instance.breakBlock(tx, ty, Map.instance.mainLayer);
			}
		}
		
		if(!simple)
		{
			particle.setPosition(x, y);
			particle2.setPosition(x,y);
			if(!isStarted)
			{
				isStarted = true;
				particle.start();
			}
			particle.update(Main.delta);
			if(isExploded)particle2.update(Main.delta);
			
			particle.draw(batch);
			particle2.draw(batch);
		}
		else
		{
			if(color!=Vector3.Zero)batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			batch.setColor(color.x, color.y, color.z, 1);
			if(!isExploded)batch.draw(tex, x-4, y-4,8,8);
			batch.setColor(Color.WHITE);
			if(color!=Vector3.Zero)batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		
	}
	
	public void Explode()
	{
		if(!simple)
		{
			particle.getEmitters().get(0).setContinuous(false);
			particle2.start();
		}
		
		if(Parameters.i.FBORender)
		{
			if(type == 4)
			{
				DeforMeshes.instance.addImpulsion(x, y,500,0.7f,1.5f);
			}
			else
			{
				DeforMeshes.instance.addImpulsion(x, y,100,0.6f,0.4f);
			}
		}
		
		
		isExploded = true;
		currentTime = lifeTime;
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
