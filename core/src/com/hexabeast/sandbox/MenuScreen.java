package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen implements Screen {

	public SpriteBatch batch;
	public boolean play = false;
	public boolean begin = false;
	public OrthographicCamera camera;
	
	public float buttonX = 640;
	public float buttonY = 500;
	
	boolean pressed = false;
	
	public float x1;
	public float x2;
	public float x3;
	public float x4;
	public float x5;
	public float alpha = 1;
	
	public boolean alphaMinus = false;
	
	float w = TextureManager.instance.playButton.getRegionWidth();
	float h = TextureManager.instance.playButton.getRegionHeight();
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !pressed)
        {
        	
        	Vector3 cor = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);	
    		camera.unproject(cor);
        	if(cor.x>buttonX-w/2 && cor.x<buttonX+w/2 && cor.y>buttonY-h/2 && cor.y<buttonY+h/2)
        	{
        		SoundManager.instance.click.play(1,1.2f, 0);
        		alphaMinus = true;
        	}
        	pressed = true;
        }
        else if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
        	pressed = false;
        }
        
        if(alphaMinus)alpha-=delta*2;
        
        if(alpha<=-0.2f)
        {
        	play = true;
        }
        
        x1-=delta*160;
        x2-=delta*120;
        x3-=delta*80;
        x5-=delta*40;
        x4-=delta*20;
        
        if(x1<=-1280)
        {
        	x1 = 0;
        }
        if(x2<=-1280)
        {
        	x2 = 0;
        }
        if(x3<=-1280)
        {
        	x3 = 0;
        }
        if(x4<=-1280)
        {
        	x4 = 0;
        }
        if(x5<=-1280)
        {
        	x5 = 0;
        }
        
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(TextureManager.instance.back,-360,-240,2000,2000/TextureManager.instance.back.getWidth()*TextureManager.instance.back.getHeight());
		
		batch.draw(TextureManager.instance.layer4,x4,0,1280,1280/TextureManager.instance.layer4.getWidth()*TextureManager.instance.layer4.getHeight());
		batch.draw(TextureManager.instance.layer4,x4+1280,0,1280,1280/TextureManager.instance.layer4.getWidth()*TextureManager.instance.layer4.getHeight());
		
		batch.draw(TextureManager.instance.layer5,x5+1280,60,1280,1280/TextureManager.instance.layer5.getWidth()*TextureManager.instance.layer5.getHeight());
		batch.draw(TextureManager.instance.layer5,x5,60,1280,1280/TextureManager.instance.layer5.getWidth()*TextureManager.instance.layer5.getHeight());

		batch.draw(TextureManager.instance.layer3,x3+1280,40,1280,1280/TextureManager.instance.layer3.getWidth()*TextureManager.instance.layer3.getHeight());
		batch.draw(TextureManager.instance.layer3,x3,40,1280,1280/TextureManager.instance.layer3.getWidth()*TextureManager.instance.layer3.getHeight());

		batch.draw(TextureManager.instance.layer2,x2+1280,20,1280,1280/TextureManager.instance.layer2.getWidth()*TextureManager.instance.layer2.getHeight());
		batch.draw(TextureManager.instance.layer2,x2,20,1280,1280/TextureManager.instance.layer2.getWidth()*TextureManager.instance.layer2.getHeight());
		
		batch.draw(TextureManager.instance.layer1,x1,-5,1280,1280/TextureManager.instance.layer1.getWidth()*TextureManager.instance.layer1.getHeight());
		batch.draw(TextureManager.instance.layer1,x1+1280,-5,1280,1280/TextureManager.instance.layer1.getWidth()*TextureManager.instance.layer1.getHeight());
		
		if(alpha>0)
		{
			batch.setColor(1,1,1,alpha);
		}
		else
		{
			batch.setColor(1,1,1,0);
		}
		batch.draw(TextureManager.instance.playButton,buttonX-w/2,buttonY-h/2);
		batch.setColor(Color.WHITE);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		batch = Main.batch;
		camera = new OrthographicCamera(1280,720);
		camera.position.set(640,360,0);
		camera.update();
		begin = true;
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
