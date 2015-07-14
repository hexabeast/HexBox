package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AllProjectiles {
	public List<MagicProjectile> projList = new ArrayList<MagicProjectile>();
	public List<Projectile> projList2 = new ArrayList<Projectile>();
	public List<Grapple> projList3 = new ArrayList<Grapple>();
	
	public int number = 5;

	public FileHandle[] particlepath1 = new FileHandle[number];
	public FileHandle[] particlepath2 = new FileHandle[number];
	public FileHandle particlefolder;
	
	ParticleEffectPool[] effectPools = new ParticleEffectPool[number];
	ParticleEffectPool[] effectPools2 = new ParticleEffectPool[number];
	
	Array<PooledEffect> effects = new Array<PooledEffect>();
	
	public AllProjectiles() 
	{		
		particlefolder = Gdx.files.internal("particles");
		
		particlepath1[0] = Gdx.files.internal("particles/greenWand");
		particlepath2[0] = Gdx.files.internal("particles/greenWand2");
		
		particlepath1[1] = Gdx.files.internal("particles/redWand");
		particlepath2[1] = Gdx.files.internal("particles/redWand2");
		
		particlepath1[2] = Gdx.files.internal("particles/purpleWand");
		particlepath2[2] = Gdx.files.internal("particles/purpleWand2");
		
		particlepath1[3] = Gdx.files.internal("particles/blueWand");
		particlepath2[3] = Gdx.files.internal("particles/blueWand2");
		
		particlepath1[4] = Gdx.files.internal("particles/blackWand");
		particlepath2[4] = Gdx.files.internal("particles/blackWand2");
		
		for(int i = 0; i<number; i++)
		{
			ParticleEffect effect = new ParticleEffect();
			effect.load(particlepath1[i], particlefolder);
			effectPools[i] = new ParticleEffectPool(effect, 1, 2000);
			ParticleEffect effect2 = new ParticleEffect();
			effect2.load(particlepath2[i], particlefolder);
			effectPools2[i] = new ParticleEffectPool(effect2, 1, 2000);
		}
		
	}
	
	public void AddMagicProjectile(float x, float y, float vx, float vy, int type, float damage, Entity owner)
	{
		MagicProjectile m = new MagicProjectile(x,y,vx,vy,type, damage,!Parameters.i.goodmagic, owner);
		projList.add(m);
	}
	
	public void AddProjectile(float x, float y, float vx, float vy, int type, Entity owner, float damage)
	{
		projList2.add(new Projectile(x,y,vx,vy,type, owner, damage));
	}
	
	public void AddGrapple(float x, float y, float vx, float vy, float distance, TextureRegion tex, Texture ropeTex)
	{
		GameScreen.player.grapple.playerAttached = false;
		GameScreen.player.grapple = new Grapple(x,y,vx,vy, distance, tex, ropeTex);
		projList3.add(GameScreen.player.grapple);
	}
	
	public void DrawMagic(SpriteBatch batch)
	{
		for (int i = 0; i < projList.size(); i++)
		{
			projList.get(i).draw(batch);
		}
		for (int i = 0; i < projList.size(); i++)
		{
			if(projList.get(i).isDead)
			{
				if(!projList.get(i).simple)
				{
					projList.get(i).particle.dispose();
					projList.get(i).particle2.dispose();
				}
				projList.remove(i);
			}
		}
	}
	
	public void DrawProjectiles(SpriteBatch batch)
	{
		for (int i = 0; i < projList2.size(); i++)
		{
			projList2.get(i).update();
			projList2.get(i).draw(batch);
		}
		for (int i = projList2.size()-1; i >=0; i--)
		{
			if(projList2.get(i).isDead)
			{
				projList2.remove(i);
			}
		}
	}
	
	public void DrawGrapples(SpriteBatch batch)
	{
		for (int i = 0; i < projList3.size(); i++)
		{
			projList3.get(i).draw(batch);
		}
		for (int i = projList3.size()-1; i >=0; i--)
		{
			if(projList3.get(i).isDead)
			{
				projList3.remove(i);
			}
		}
	}

}
