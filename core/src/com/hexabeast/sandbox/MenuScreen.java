package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {

	public SpriteBatch batch;
	public boolean play = false;
	public boolean begin = false;
	public OrthographicCamera camera;
	
	boolean pressedmulti = false;
	boolean pressedhost = false;
	
	public Button playButton;
	public Button hostButton;
	public Button joinButton;
	public Button settingsButton;
	public Button charEditButton;
	public Button exitButton;
	
	public DisplayText helpText;
	
	public float x1;
	public float x2;
	public float x3;
	public float x4;
	public float x5;
	public float alpha = 1;
	
	public boolean alphaMinus = false;
	
	TextField txtfld;
	Stage scene;
	
	public MenuScreen()
	{
		helpText = new DisplayText(640,190,Main.welcomeMessage,FontManager.instance.font,true);
		helpText.color = Main.welcomeColor;
		
		playButton = new Button(640-TextureManager.instance.playButton.getRegionWidth()/2, 500-TextureManager.instance.playButton.getRegionHeight()/2, TextureManager.instance.playButton);
		hostButton = new Button(640-TextureManager.instance.hostButton.getRegionWidth()-10, 350-TextureManager.instance.hostButton.getRegionHeight()/2, TextureManager.instance.hostButton);
		joinButton = new Button(640+10, 350-TextureManager.instance.joinButton.getRegionHeight()/2, TextureManager.instance.joinButton);
		charEditButton = new Button(640+200, 500-TextureManager.instance.charEditButton.getRegionHeight()/2, TextureManager.instance.charEditButton);
		settingsButton = new Button(640-TextureManager.instance.settingsButton.getRegionWidth()-200, 500-TextureManager.instance.settingsButton.getRegionHeight()/2, TextureManager.instance.settingsButton);
		exitButton = new Button(50, 30, TextureManager.instance.pauseexit);
		
		scene = new Stage();
		
		TextFieldStyle tfs = new TextFieldStyle();
		tfs.font = FontManager.instance.font;
		tfs.fontColor = Color.WHITE;
		tfs.selection = new TextureRegionDrawable(TextureManager.instance.textBoxSurline);
		tfs.cursor = new TextureRegionDrawable(TextureManager.instance.textBoxCursor);
		//tfs.background = new TextureRegionDrawable(TextureManager.instance.ipButton);
		
		txtfld = new TextField("", tfs);
		txtfld.setWidth(TextureManager.instance.ipButton.getRegionWidth()-70);
		
		txtfld.setPosition(660-txtfld.getWidth()/2, 250);
		
		scene.addActor(txtfld);
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        Vector3 cor = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);	
		camera.unproject(cor);
        
        if(Inputs.instance.mousedown && !pressedmulti && !pressedhost)
        {
        	if(exitButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		Gdx.app.exit();
        	}
        	else if(charEditButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		Main.charedit = new CharEditorScreen();
        		SoundManager.instance.playSound(SoundManager.instance.click,1,1.2f);
        		Main.instance.setScreen(Main.charedit);
        	}
        	else if(settingsButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		Main.settings = new SettingsScreen();
        		SoundManager.instance.playSound(SoundManager.instance.click,1,1.2f);
        		Main.instance.setScreen(Main.settings);
        	}
        	else if(playButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		SoundManager.instance.playSound(SoundManager.instance.click,1,1.2f);
        		alphaMinus = true;
        	}
        	else if(joinButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		SoundManager.instance.playSound(SoundManager.instance.click,1,1.2f);
        		if(txtfld.getText()!="")
        		{
            		
            		if(NetworkManager.instance.connect(txtfld.getText()))pressedmulti = true;
            		else
            		{
            			pressedmulti = false;
            			helpText.color = Color.RED;
            			helpText.text = "Connection failed";
            		}
        		}
        		else
        		{
        			helpText.color = Color.RED;
        			helpText.text = "Enter a valid IP adress";
        		}
        	}
        	else if(hostButton.isTouched(cor.x,cor.y) && !alphaMinus)
        	{
        		SoundManager.instance.playSound(SoundManager.instance.click,1,1.2f);
            	pressedhost = true;
        	}
        }
        
        if(pressedmulti)
        {
        	helpText.text = "Connecting...";
        	helpText.color = Color.WHITE;
        	if(NetworkManager.instance.online)
        	{
        		alphaMinus = true;
        		helpText.text = "Connected!";
        	}
        }
        
        if(pressedhost)
        {
        	alphaMinus = true;
        	Main.host = true;
        	Main.enableCheats = false;
        }
        
        if(alphaMinus)alpha-=delta*2;
        
        if(alpha<=-0.2f)
        {
        	play = true;
        	if(pressedmulti)
        	{
        		Main.multiplayer=true;
        		Main.enableCheats = false;
        	}
        	else
        	{
        		Main.multiplayer=false;
        	}
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
		playButton.draw(batch);
		hostButton.draw(batch);
		joinButton.draw(batch);
		settingsButton.draw(batch);
		charEditButton.draw(batch);
		exitButton.draw(batch);
		
		batch.setColor(Color.WHITE);
		
		TextureRegion ttex = TextureManager.instance.ipButton;
		
		helpText.draw(batch);
		
		batch.draw(ttex, (txtfld.getX()+txtfld.getWidth()/2)-ttex.getRegionWidth()/2-20, (txtfld.getY()+txtfld.getHeight()/2)-ttex.getRegionHeight()/2);
		batch.end();
		
	    scene.draw();
	}

	@Override
	public void resize(int width, int height) {
		scene.getViewport().update(width, height);
	}

	@Override
	public void show() {
		batch = Main.batch;
		camera = new OrthographicCamera(1280,720);
		camera.position.set(640,360,0);
		camera.update();
		begin = true;
		Main.inputMultiplexer.addProcessor(scene);
	}

	@Override
	public void hide() {
		Main.inputMultiplexer.removeProcessor(scene);
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
