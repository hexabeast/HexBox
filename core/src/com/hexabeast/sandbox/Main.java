package com.hexabeast.sandbox;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.hexabeast.sandbox.Parameters;

public class Main extends Game {
	
	public static boolean noUI = false;
	
	public static boolean mobile = false;
	public static float delta;
	public static float time = 0;
	public static boolean pause = false;
	public static boolean devtest = true;
	
	public static int windowWidth = 1280;
	public static int windowHeight = 720;
	
	public static float zoom = 1;
	
	public static GameScreen game;
	public static MenuScreen menu;
	public LoadingScreen loading;
	public boolean loaded = false;
	public static SpriteBatch batch;
	public static SpriteBatch secondBatch;
	public static SaveManager saveMan;
	public static boolean ingame = false;
	public static Thread secondThread;
	
	public static Matrix4 defaultMatrix;
	public static OrthographicCamera matrixCam;
	
	public static FrameBuffer fbo;
	public static Random random;
	
	public static Mesh mesh;

	InputMultiplexer inputMultiplexer = new InputMultiplexer();
	Joystick joy;
	
	public boolean allLoaded = false;
	
	@Override
	public void create () {
		
		DeforMeshes.instance = new DeforMeshes(32, 18);
		
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		matrixCam = new OrthographicCamera();
		defaultMatrix = new Matrix4();
		
		new HNoise(0).generateGradient(200, 200, 10, 10);
		random = new Random();
		Shaders.instance = new Shaders();
		batch = new SpriteBatch();
		batch.setShader(Shaders.instance.basic);
		
		secondBatch = new SpriteBatch();
		secondBatch.setShader(Shaders.instance.basic);
		
		SaveManager.instance = new SaveManager();
		SoundManager.instance = new SoundManager();
		FontManager.instance = new FontManager();
		AllBlocTypes.instance = new AllBlocTypes();
		TextureManager.instance = new TextureManager();
		AllTools.instance = new AllTools();
		AllCrafts.instance = new AllCrafts();
		Parameters.i = new Parameters();
		PauseMenu.instance = new PauseMenu();
		Inputs.instance = new Inputs();
		
		saveMan = new SaveManager();
		menu = new MenuScreen();
		
		SaveManager.instance.LoadParams();
		updateResolution();
		Gdx.graphics.setVSync(Parameters.i.vsync);
		
		inputMultiplexer.addProcessor(Inputs.instance);
		
		if(Main.mobile)
		{
			joy = new Joystick();
			inputMultiplexer.addProcessor(joy);
		}
		
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public static void updateMatrix()
	{
		matrixCam.viewportWidth = windowWidth;
		matrixCam.viewportHeight =  windowHeight;
		matrixCam.position.set(windowWidth/2, windowHeight/2,0);
		matrixCam.update();
		defaultMatrix.set(matrixCam.combined);
		
		fbo = new FrameBuffer(Format.RGBA8888 ,windowWidth, windowHeight,false);
	}
	
	public static void updateResolution()
	{
		updateResolution(Constants.resolutions[Parameters.i.resolution].x, Constants.resolutions[Parameters.i.resolution].y);
	}
	
	public static void updateResolution(int wx, int hy)
	{
		Gdx.graphics.setDisplayMode(wx, hy, Parameters.i.fullscreen);
	}
	
	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		if((float)width/(float)height>18f/9f)updateResolution(width, (int) (width*9f/16f)-1);
		else if((float)width/(float)height<16f/10f)updateResolution((int) (height*16f/9f)+1, height);
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		updateMatrix();
		DeforMeshes.instance.ComputeFloats();
	}
	
	@Override
	public void pause () {
		super.pause();
	}
	
	@Override
	public void resume () {
		super.resume();
	}

	@Override
	public void dispose () 
	{
		if(loaded && allLoaded)
		{
			try {
				SaveManager.Save();
				Map.instance.Save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		super.dispose();
	}
	
	
	@Override
	public void render () {
		delta = Gdx.graphics.getDeltaTime();
		//if(delta>1f/400)System.out.println(1/delta);
		if(ingame)
		{
			if(!pause && !SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].isPlaying())SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].play();
			if(pause && SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].isPlaying())SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].pause();
			delta *= Parameters.i.deltaMultiplier;
		}
		
		Inputs.instance.update();
		
		if(pause)delta = Float.MIN_VALUE;
		if(!PauseMenu.instance.clear && !pause)PauseMenu.instance.rebootMenu();
		if(delta>0.03f)delta = 0.03f;
		
		time+=delta;
		
		Shaders.instance.update();
		
		if(devtest)menu.play = true;
		
		if(!menu.play)
		{
			if(!menu.begin)
			{
				setScreen(menu);
				SoundManager.instance.menuTheme.play();
			}
			
		}
		else
		{
			if(!loaded)
			{
				loaded = true;
				MapChecker.instance = new MapChecker();
				if(!mobile)
				{
					if(!devtest)Map.instance = new Map("calmap",7000,2500);
					else Map.instance = new Map("calmap2",2000,1500);
				}
				else
				{
					//MOBILE
					Map.instance = new Map("calmap",2000,1000);
				}
				MapGenerator.instance = new MapGenerator((int)(Math.random()*5000000));
				ModifyTerrain.instance = new ModifyTerrain();
				game = new GameScreen();
				if(Map.instance.random)loading = new LoadingScreen(true);
				else loading = new LoadingScreen(false);
				setScreen(loading);
				
			}
			if(loading.mapLoaded && !allLoaded)
			{
				setScreen(game);
				ingame = true;
				if(SoundManager.instance.menuTheme.isPlaying())SoundManager.instance.menuTheme.coolStop();
				SoundManager.instance.playAmbiance();
				if(!mobile)SaveManager.instance.Load();
				allLoaded = true;
			}
		}
		
		super.render();
		Inputs.instance.updateLate();
	}
}
