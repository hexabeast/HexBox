package com.hexabeast.sandbox.particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.FontManager;
import com.hexabeast.sandbox.Parameters;
import com.hexabeast.sandbox.Shaders;
import com.hexabeast.sandbox.Tools;

public class HParticleEmmiter {
	TextureRegion texture;
	ArrayList<HParticle> particles;
	int maxSize = 200;
	float rate = 1;
	float current = 0;
	float x = 0;
	float y = 0;
	float w = 0;
	float h = 0;
	public float sizevar = 0;
	public boolean randomRot = false;
	public boolean additive = true;
	public boolean automatic = true;
	public float rotvar = 0;
	public float baselife = 2;
	public float gravity = 0;
	public float baseSize = 1;
	
	public Vector2 veloBase;
			
	public HParticleEmmiter()
	{
		particles = new ArrayList<HParticle>();
		w = 8;
		h = 8;
	}
	
	public void setTexture(TextureRegion texture)
	{
		this.texture = texture;
		w = texture.getRegionWidth()*2;
		h = texture.getRegionHeight()*2;
	}
	
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void addParticles(int n, TextureRegion texture, float x, float y)
	{
		for(int i = 0; i<n; i++)
		{
			if(particles.size()<maxSize)
			{
				addnew(texture,x,y);
			}
			
		}
	}
	
	public void addnew(TextureRegion texture, float x, float y)
	{
		HParticle p = new HParticle(x,y);
		p.scale = (float) (baseSize+Math.random()*sizevar);
		p.texture = texture;
		if(randomRot)p.r = (float) (Math.random()*1000);
		p.velor = (float) (rotvar*Math.random());
		p.velo = new Vector2(veloBase.rotate((float) (Math.random()*360)));
		p.life = baselife;
		p.gravity = gravity;
		particles.add(p);
	}
	
	public void addnewText(int txt, float x, float y)
	{
		HParticle p = new HParticle(x,y);
		p.scale = (float) (0.35f+((float)Math.min(txt,25)/150f));
		p.isText = true;
		p.str = String.valueOf(txt);
		p.velo = new Vector2(0,40);
		p.life = baselife;
		p.cr = 1;
		p.cg = 0;
		p.cb = 0;
		p.gravity = 0;
		particles.add(p);
	}
	
	public void addParticles(int n)
	{
		for(int i = 0; i<n; i++)
		{
			addnew(texture,x,y);
		}
	}
	
	public void updateParticles(float delta)
	{
		for(int i = particles.size()-1; i>=0; i--)
		{
			particles.get(i).update(delta);
			if(particles.get(i).life<0)particles.remove(i);
		}
	}
	
	public void update(float delta)
	{
		updateParticles(delta);
		
		if(automatic)
		{
			current+=delta;
			if(current>rate && particles.size()<maxSize)
			{
				current = 0;
				addnew(texture,x,y);
			}
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		//if (additive)batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		for(int i = 0; i<particles.size(); i++)
		{
			if(!particles.get(i).isText )
			{
				if(!Parameters.i.fullBright)
				{
					Vector3 color = Tools.getShadowColor((int) (particles.get(i).x/16),(int) (particles.get(i).y/16));
					batch.setColor(color.x*particles.get(i).cr,color.y*particles.get(i).cg,color.z*particles.get(i).cb,particles.get(i).a);
				}
				else batch.setColor(particles.get(i).cr,particles.get(i).cg,particles.get(i).cb,particles.get(i).a);
				batch.draw(particles.get(i).texture, particles.get(i).x, particles.get(i).y, w/2, h/2, w, h, particles.get(i).scale, particles.get(i).scale, particles.get(i).r);	
				batch.setColor(1,1,1,1);
			}
			else
			{
				Shaders.instance.setOutlineShader();
				FontManager.instance.font1.getData().setScale(particles.get(i).scale);
				FontManager.instance.font1.setColor(1,1,1,particles.get(i).a);
				
				//TextBounds bounds = FontManager.instance.font1.getBounds(particles.get(i).str);
				Shaders.instance.updateOutline(1, 1, particles.get(i).cr,particles.get(i).cg,particles.get(i).cb);
				
				FontManager.instance.font1.draw(batch, particles.get(i).str, particles.get(i).x, particles.get(i).y);
				FontManager.instance.initializeFont1();
				Shaders.instance.setShadowShader();
			}
		}
		
		//if (additive) batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
	}

}
