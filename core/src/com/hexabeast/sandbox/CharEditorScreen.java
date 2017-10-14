package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hexabeast.sandbox.mobs.PNJ;

public class CharEditorScreen implements Screen {
	
	public OrthographicCamera camera;
	public OrthographicCamera camera2;
	public Button back;
	
	public PauseSetting head;
	public PauseSetting body;
	public PauseSetting arms;
	public PauseSetting legs;
	public PauseSetting eyes;
	public PauseSetting hair;
	
	TextField txtfld;
	Stage scene;
	
	public CharEditorScreen()
	{
		scene = new Stage();
		
		TextFieldStyle tfs = new TextFieldStyle();
		tfs.font = FontManager.instance.fontNameChar;
		tfs.fontColor = Color.WHITE;
		tfs.selection = new TextureRegionDrawable(TextureManager.instance.textBoxSurline);
		tfs.cursor = new TextureRegionDrawable(TextureManager.instance.textBoxCursor);
		//tfs.background = new TextureRegionDrawable(TextureManager.instance.ipButton);
		
		txtfld = new TextField("", tfs);
		txtfld.setWidth(300);
		
		txtfld.setAlignment(1);
		
		txtfld.setText(Parameters.i.name);
		
		txtfld.setPosition(630-txtfld.getWidth()/2, 65);
		
		scene.addActor(txtfld);
		
		camera = new OrthographicCamera(2560,1440);
		camera.position.set(1280,720,0);
		
		camera2 = new OrthographicCamera(1280,720);
		camera2.position.set(640,360,0);
		
		hair = new PauseSetting(1550,1100,"Hair :","");
		head = new PauseSetting(1550,950,"Head :","");
		eyes = new PauseSetting(1550,800,"Eyes :","");
		body = new PauseSetting(1550,650,"Body :","");
		arms = new PauseSetting(1550,500,"Arms :","");
		legs = new PauseSetting(1550,350,"Legs :","");
		
		back = new Button(40,60,TextureManager.instance.pauseback.getRegionWidth()*2,TextureManager.instance.pauseback.getRegionHeight()*2,TextureManager.instance.pauseback);
	}
	
	@Override
	public void show() 
	{
		Main.inputMultiplexer.addProcessor(scene);
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
				
				String str = txtfld.getText();
				str = str.replaceAll("\\s+","");
				if(str.length() <=15 && str.length()>=3)Parameters.i.name = str;
				
				Main.instance.setScreen(Main.menu);
			}
			else
			{
				if(head.isTouchedRight(cor.x,cor.y))Parameters.i.head++;
				else if(head.isTouchedLeft(cor.x,cor.y))Parameters.i.head--;
				
				else if(body.isTouchedRight(cor.x,cor.y))Parameters.i.body++;
				else if(body.isTouchedLeft(cor.x,cor.y))Parameters.i.body--;
				
				else if(arms.isTouchedRight(cor.x,cor.y))Parameters.i.arms++;
				else if(arms.isTouchedLeft(cor.x,cor.y))Parameters.i.arms--;
				
				else if(legs.isTouchedRight(cor.x,cor.y))Parameters.i.legs++;
				else if(legs.isTouchedLeft(cor.x,cor.y))Parameters.i.legs--;

				else if(eyes.isTouchedRight(cor.x,cor.y))Parameters.i.eyes++;
				else if(eyes.isTouchedLeft(cor.x,cor.y))Parameters.i.eyes--;

				else if(hair.isTouchedRight(cor.x,cor.y))Parameters.i.hair++;
				else if(hair.isTouchedLeft(cor.x,cor.y))Parameters.i.hair--;
				
				if(Parameters.i.head >= TextureManager.instance.PNJNumber)Parameters.i.head=0;
				if(Parameters.i.head <0)Parameters.i.head = TextureManager.instance.PNJNumber-1;
				
				if(Parameters.i.body >= TextureManager.instance.PNJNumber)Parameters.i.body=0;
				if(Parameters.i.body <0)Parameters.i.body = TextureManager.instance.PNJNumber-1;
				
				if(Parameters.i.arms >= TextureManager.instance.PNJNumber)Parameters.i.arms=0;
				if(Parameters.i.arms <0)Parameters.i.arms = TextureManager.instance.PNJNumber-1;
				
				if(Parameters.i.legs >= TextureManager.instance.PNJNumber)Parameters.i.legs=0;
				if(Parameters.i.legs <0)Parameters.i.legs = TextureManager.instance.PNJNumber-1;
				
				if(Parameters.i.eyes >= TextureManager.instance.PNJNumber)Parameters.i.eyes=0;
				if(Parameters.i.eyes <0)Parameters.i.eyes = TextureManager.instance.PNJNumber-1;
				
				if(Parameters.i.hair >= TextureManager.instance.PNJNumber)Parameters.i.hair=0;
				if(Parameters.i.hair <0)Parameters.i.hair = TextureManager.instance.PNJNumber-1;
			}
		}
        
        Main.batch.setProjectionMatrix(camera.combined);
        
        Main.batch.begin();
        
        Main.batch.draw(TextureManager.instance.backGroundChar, 0, 0, 2560, 1440);
        
        float factor = 4;
        
        float shoulderX = 10;
    	float shoulderY = 14;
    	
    	int BodyOffsetY = PNJ.BodyOffsetY;
    	int HelmetOffsetY = PNJ.HelmetOffsetY;
    	int LegsOffsetY = PNJ.LegsOffsetY;
    	float offsetArmLeft = 8;
    	
    	float shoulderOriginX = 10;
    	float shoulderOriginY = 25;
    	
    	int pieceSize = 52;
    	
    	float x = 1170;
    	float y = 640;
        
  		Main.batch.draw(TextureManager.instance.PNJarmTexture[Parameters.i.arms], x+shoulderX*factor+offsetArmLeft*factor, y+shoulderY*factor+1, shoulderOriginX*factor, shoulderOriginY*factor, pieceSize*factor, pieceSize*factor, 1, 1, 30);

  		Main.batch.draw(TextureManager.instance.PNJbody[Parameters.i.body], x, y+BodyOffsetY*factor, pieceSize*factor, pieceSize*factor);

  		Main.batch.draw(TextureManager.instance.PNJlegs[Parameters.i.legs][0], x, y+LegsOffsetY*factor, pieceSize*factor, pieceSize*factor);
  		
  		Main.batch.draw(TextureManager.instance.PNJhead[Parameters.i.head], x, y+HelmetOffsetY*factor, pieceSize*factor, pieceSize*factor);
  		
  		Main.batch.draw(TextureManager.instance.PNJeyes[Parameters.i.eyes][0], x, y+HelmetOffsetY*factor, pieceSize*factor, pieceSize*factor);

  		Main.batch.draw(TextureManager.instance.PNJhairs[Parameters.i.hair], x, y+HelmetOffsetY*factor, pieceSize*factor, pieceSize*factor);
  		
  		Main.batch.draw(TextureManager.instance.PNJarmTexture[Parameters.i.arms], x+shoulderX*factor, y+shoulderY*factor, shoulderOriginX*factor, shoulderOriginY*factor, pieceSize*factor, pieceSize*factor, 1, 1, 0);
  		
        
        head.draw(Main.batch);
        body.draw(Main.batch);
        arms.draw(Main.batch);
        legs.draw(Main.batch);
        eyes.draw(Main.batch);
        hair.draw(Main.batch);
        
        back.draw(Main.batch);
        
        Main.batch.end();
        
        scene.draw();
	}

	@Override
	public void resize(int width, int height) 
	{
		scene.getViewport().update(width, height);
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
		Main.inputMultiplexer.removeProcessor(scene);
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
