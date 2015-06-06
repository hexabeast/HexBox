package com.hexabeast.sandbox.particles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class HParticle {
	float x;
	float y;
	float scale;
	float r;
	float basea;
	float a;
	Vector2 velo;
	float velor;
	float veloAlpha;
	float veloScale;
	float cr = 1;
	float cg = 1;
	float cb = 1;
	float life;
	float gravity = 0;
	TextureRegion texture;
	boolean isText;
	String str;
	
	public HParticle(float x, float y)
	{
		this.x = x;
		this.y = y;
		this.r = 0;
		this.scale = 1;
		this.a = 1;
		this.basea = 1;
		velo = new Vector2();
		velor = 0;
		veloScale = 0;
		veloAlpha = 0;
		life = 2;
	}
	
	public void update(float delta)
	{
		velo.y-=gravity*delta;
		x+=velo.x*delta;
		y+=velo.y*delta;
		r+=velor*delta;
		a+=veloAlpha;
		scale+=veloScale;
		life-=delta;
		if(life<0.2f)a = basea*life*5;
		if(a<0)a=0;
	}
}
