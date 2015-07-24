package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Rain {
	
	public ArrayList<RainDrop> rainList;
	public Timer rainRate;
	public float factor;
	
	public Rain()
	{
		rainRate = new Timer(0.0015f);
		rainList = new ArrayList<RainDrop>();
	}

	public void draw(SpriteBatch batch)
	{
		 factor = (float)GameScreen.camera.viewportWidth/1280f;
		if(Parameters.i.zoomLock)factor = 1;
		
		System.out.println(GameScreen.camera.viewportWidth);
		
		if(Parameters.i.rain)
		{
			int ch = rainRate.multicheck();
			ch = Math.min(ch, 20);
			for(int i = 0; i<ch; i++)
			{
				if(GameScreen.camera.position.y>Map.instance.limit*16-2000)
				rainList.add(new RainDrop((float) (GameScreen.camera.position.x+Math.random()*2000*factor-1300*factor), GameScreen.camera.position.y+800+(float)(Math.random()*500)));
			}
		}
		
		for(int i = rainList.size()-1; i>-1; i--)
		{
			rainList.get(i).move();
			batch.draw(TextureManager.instance.rains[rainList.get(i).current], rainList.get(i).x-9, rainList.get(i).y-6);
			
			if(rainList.get(i).isDead)
			{
				rainList.remove(i);
			}
			
		}
	}
}
