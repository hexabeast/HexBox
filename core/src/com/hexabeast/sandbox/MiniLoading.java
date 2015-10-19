package com.hexabeast.sandbox;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MiniLoading {
	Sprite loadImage;
	Sprite loadImage2;
	float saveCoolDown = 120;
	float saveCounter = 0;
	boolean isLoading = false;
	float alpha = 0;

	public MiniLoading() {
		saveCounter = 0;
		loadImage = new Sprite(TextureManager.instance.roue1);
		loadImage2 = new Sprite(TextureManager.instance.roue2);
		
		loadImage.setOriginCenter();
		loadImage2.setOriginCenter();
		
		loadImage.setScale(5);
		loadImage2.setScale(5);
		
		loadImage.setPosition(1100, -580);
		loadImage2.setPosition(1100, -580);
	}
	
	public void DrawAll(SpriteBatch batch)
	{
		saveCounter+= Main.delta;
		//loadImage.rotate(180*Main.delta);
		loadImage2.rotate(-720*Main.delta);
		
		if(isLoading)
		{
			alpha+=3*Main.delta;
			if(alpha>1)alpha = 1;
		}
		else
		{
			alpha-=2*Main.delta;
			if(alpha<0)alpha = 0;
		}
		
		loadImage2.setColor(1,1,1,alpha);
		loadImage.setColor(1,1,1,alpha);
		
		if(alpha>0)
		{
			loadImage2.draw(batch);
			loadImage.draw(batch);
		}
		
		
		if(saveCounter>saveCoolDown-1)
		{
			isLoading = true;
		}
		
		if(saveCounter>saveCoolDown)
		{
			new Thread
			(
				new Runnable()
				{
					@Override
					public void run()
					{
						miniSave();
						isLoading = false;
						Gdx.app.postRunnable(new Runnable()
						{
							@Override
							public void run()
							{
								
							}
						});
					}
				}
			).start();
			
			saveCounter = 0;
		}
	}
	
	public void miniSave()
	{
		isLoading = true;
		SaveManager.SaveParam();
		try {
			SaveManager.Save();
			Map.instance.Save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isLoading = false;
		
	}

}
