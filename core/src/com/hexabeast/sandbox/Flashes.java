package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Flashes {
	
	float alpha = 0;
	boolean isUp = false;
	float max = 1f;
	float speed = 0.5f;
	
	boolean regen = false;
	boolean respawn = false;
	boolean teleport = false;
	boolean door = false;
	
	float telex = 0;
	float teley = 0;
	
	Color color = Color.WHITE;
	
	boolean running = false;

	public Flashes() {
	}

	public void FlashTeleport(float x, float y, boolean portal)
	{
		if(!running)
		{
			telex = x;
			teley = y;
			isUp = true;
			
			
			
			if(portal)
			{
				color = new Color(0.05f,0,0.1f,1);
				teleport = true;
			}
			else 
			{
				color = new Color(0,0,0,1);
				door = true;
			}
			max = 1f;
			speed = 2f;
		}
	}
	
	public void FlashDead() 
	{
		isUp = true;
		regen = true;
		respawn = true;
		color = Color.WHITE;
		max = 1f;
		speed =2f;
	}
	
	public void FlashHurt() 
	{
		if(!running)
		{
			isUp = true;
			color = new Color(0.5f,0.1f,0.1f,1);
			max = 0.5f;
			speed = 2.5f;
		}
	}
	
	public void DrawAll()
	{
		if(isUp)
		{
			alpha+=speed*3*Main.delta;
		}
		else
		{
			alpha-=speed/2*Main.delta;
		}
		
		if(alpha<=0)
		{
			alpha = 0;
			running = false;
		}
		else if(alpha>max && isUp)
		{
			alpha = max;
			isUp = false;
			running = true;
			if(respawn)
			{
				GameScreen.player.setPosition(GameScreen.player.initialPos.x,GameScreen.player.initialPos.y);
				GameScreen.player.transformed = false;
				Parameters.i.currentTransform = 0;
				respawn = false;
			}
			if(regen)
			{
				GameScreen.player.PNJ.health = GameScreen.player.PNJ.maxHealth;
				GameScreen.player.PNJ.detach = true;
				GameScreen.player.PNJ.isDead = false;
				regen = false;
			}
			/*if(teleport)
			{
				GameScreen.constructions.Teleport(telex, teley);
				teleport = false;
			}
			if(door)
			{
				GameScreen.constructions.Door(telex, teley);
				door = false;
			}*/
			
		}
		else if(alpha>max)
		{
			alpha = max;
			running = true;
		}
		else 
		{
			running = true;
		}
		
		
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		 Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		 GameScreen.shapeRenderer.setProjectionMatrix(GameScreen.camera.combined);
		 GameScreen.shapeRenderer.begin(ShapeType.Filled);
		 GameScreen.shapeRenderer.setColor(new Color(color.r, color.g, color.b, alpha));
		 GameScreen.shapeRenderer.rect(-200000, -200000, 4000000,4000000);
		 GameScreen.shapeRenderer.end();
	}
}
