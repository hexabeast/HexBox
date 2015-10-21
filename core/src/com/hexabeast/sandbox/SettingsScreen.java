package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SettingsScreen  implements Screen 
{
	
	public OrthographicCamera camera;
	public Button back;
	public int f = 2;
	
	public SettingsScreen()
	{
		camera = new OrthographicCamera(1280*f,720*f);
		//camera.position.set(640,360,0);
		
		back = new Button(-620*f,-320*f,TextureManager.instance.pauseback.getRegionWidth()*f,TextureManager.instance.pauseback.getRegionHeight()*f,TextureManager.instance.pauseback);
	}
	
	@Override
	public void show() 
	{
		PauseMenu.instance.rebootMenu();
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        
        Vector3 cor = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);	
		camera.unproject(cor);
        
        if(Inputs.instance.mousedown)
		{
			if(back.isTouched(cor.x,cor.y))
			{
				Main.menu = new MenuScreen();
				Main.instance.setScreen(Main.menu);
			}
		}
        
        Main.batch.setProjectionMatrix(camera.combined);
        
        Main.batch.begin();
        
        Main.batch.draw(TextureManager.instance.backGround, -640*f, -360*f, 1280*f, 720*f);
        
        PauseMenu.instance.drawSettings(Main.batch, cor.x, cor.y,false);
        
        back.draw(Main.batch);
        
        Main.batch.end();
	}

	@Override
	public void resize(int width, int height) 
	{
		
	}

	@Override
	public void pause() 
	{
		
	}

	@Override
	public void resume() 
	{
		
	}

	@Override
	public void hide() 
	{
		PauseMenu.instance.rebootMenu();
	}

	@Override
	public void dispose() 
	{
		
	}
	
	public Vector2 getAbsoluteMouse()
	{
		return getAbsolutePos(Gdx.input.getX(), Gdx.input.getY());
	}
	
	public Vector2 getAbsolutePos(float x, float y)
	{
		Vector3 worldCoord = new Vector3(x,y,0);
		camera.unproject(worldCoord);
		return new Vector2(worldCoord.x,worldCoord.y);
	}
}
