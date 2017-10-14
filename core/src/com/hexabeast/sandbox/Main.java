package com.hexabeast.sandbox;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.hexabeast.sandbox.Parameters;

public class Main extends Game {
	
	public static Main instance;
	
	public static boolean devtest = true;
	public static boolean mobile = false;
	
	public static int windowWidth = 1280;
	public static int windowHeight = 720;
	
	public static boolean noUI = false;
	
	public static boolean enableCheats = true;
	public static boolean backtom;
	
	public static boolean multiplayer = false;
	public static boolean host = false;
	
	public static String defaultWelcomeMessage = "This is a development version - Multiplayer port: 25565";
	public static String welcomeMessage = "This is a development version - Multiplayer port: 25565";
	public static Color welcomeColor = Color.WHITE;
	
	public static float delta;
	public static float time;
	public static boolean pause;
	
	public static NetworkManager network;
	
	public static float zoom;
	
	public static GameScreen game;
	public static CharEditorScreen charedit;
	public static SettingsScreen settings;
	public static MenuScreen menu;
	public static LoadingScreen loading;
	public static boolean loaded;
	public static SpriteBatch batch;
	public static SpriteBatch secondBatch;
	public static SaveManager saveMan;
	public static boolean ingame;
	public static Thread secondThread;
	
	public static Matrix4 defaultMatrix;
	public static OrthographicCamera matrixCam;
	
	public static FrameBuffer fbo;
	public static Random random;
	
	public static Mesh mesh;

	public static InputMultiplexer inputMultiplexer;
	static Joystick joy;
	
	public static boolean allLoaded;
	
	public static void sCreate()
	{
		allLoaded = false;
		noUI = false;
		multiplayer = false;
		host = false;
		ingame = false;
		time = 0;
		pause = false;
		loaded = false;
		zoom = 1;
		backtom = false;
		
		inputMultiplexer = new InputMultiplexer();
		
		if(NetworkManager.instance != null && NetworkManager.instance.server != null)NetworkManager.instance.server.stop();
		network= new NetworkManager();
		//if(multiplayer)network.connectLocal();
		
		Parameters.i = new Parameters();
		DeforMeshes.instance = new DeforMeshes(32, 18);
		
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		matrixCam = new OrthographicCamera();
		defaultMatrix = new Matrix4();
		
		//new HNoise(0).generateGradient(200, 200, 10, 10);
		random = new Random();
		
		saveMan = new SaveManager();
		menu = new MenuScreen();
		
		charedit = new CharEditorScreen();
		settings = new SettingsScreen();
		
		SaveManager.instance.LoadParams();
		updateResolution();
		sResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.graphics.setVSync(Parameters.i.vsync);
		
		Parameters.i.disableCheats();
		
		inputMultiplexer.addProcessor(Inputs.instance);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		SoundManager.instance.updateVolume();
		HKeys.setKeyboard(Parameters.i.keyboard);
	}
	
	@Override
	public void create () 
	{
		instance = this;
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
		PauseMenu.instance = new PauseMenu();
		
		if(Main.mobile)
		{
			joy = new Joystick();
			inputMultiplexer.addProcessor(joy);
		}
		
		Inputs.instance = new Inputs();
		
		sCreate();
		
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
		float ratio = 3f/4f;
		if(Parameters.i.ratio)ratio = 9f/16f;
		updateResolution(Constants.resolutions[Parameters.i.resolution], (int)(Constants.resolutions[Parameters.i.resolution]*ratio));
	}
	
	public static void updateResolution(int wx, int hy)
	{
		//Gdx.graphics.setDisplayMode(wx, hy, Parameters.i.fullscreen);
		Gdx.graphics.setWindowedMode(wx, hy);
	}
	
	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		sResize(width,height);
	}
	
	public static void sResize(int width, int height)
	{
		if((float)width/(float)height>18f/9f)updateResolution(width, (int) (width*9f/16f)-1);
		else if((float)width/(float)height<16f/13f)updateResolution((int) (height*16f/9f)+1, height);
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
		SaveManager.SaveParam();

		if(loaded && allLoaded)
		{
			try {
				SaveManager.Save();
				Map.instance.Save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(host)
		{
			NetworkManager.instance.server.server.stop();
		}
		
		super.dispose();
	}
	
	
	@Override
	public void render () {

		if(backtom)sCreate();
		
		//if(NetworkManager.instance.server == null)System.out.println("koj");
		
		delta = Gdx.graphics.getDeltaTime();
		if(ingame)
		{
			if(!pause && !SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].isPlaying())SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].play();
			if(pause && SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].isPlaying())SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].pause();
			delta *= Parameters.i.deltaMultiplier;
		}
		
		Inputs.instance.update();
		
		if(GameScreen.camera != null)Tools.computeAbsoluteMouse();
		
		if(pause && !NetworkManager.instance.online)delta = Float.MIN_VALUE;
		if(!PauseMenu.instance.clear && !pause && ingame)PauseMenu.instance.rebootMenu();
		if(delta>0.03f)delta = 0.03f;
		
		time+=delta;
		
		Shaders.instance.update();
		
		//if(devtest)menu.play = true;
		
		if(!menu.play)
		{
			if(!menu.begin)
			{
				setScreen(menu);
				if(!SoundManager.instance.menuTheme.isPlaying())SoundManager.instance.menuTheme.play();
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
					Map.instance = null;
					System.gc();
					if(!devtest)Map.instance = new Map("calmap2",5000,2000);
					else Map.instance = new Map("calmap2",2000,1500);
				}
				else
				{
					//MOBILE
					Map.instance = new Map("calmap",2000,1000);
				}
				MapGenerator.instance = new MapGenerator((int)(Math.random()*5000000));
				ModifyTerrain.instance = new ModifyTerrain();
				if(game != null)Main.inputMultiplexer.removeProcessor(game.chat.scene);
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
				Main.inputMultiplexer.addProcessor(game.chat.scene);
			}
		}
		
		super.render();
		Inputs.instance.updateLate();
	}
	
	public static void backToMenu(String str, Color c)
	{
		
		if(!backtom)
		{
			SaveManager.SaveParam();
			if(ingame)
			{
				if(SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].isPlaying())SoundManager.instance.ambiance[SoundManager.instance.playOrder[SoundManager.instance.oldPlay]].pause();
				try {
					SaveManager.Save();
					Map.instance.Save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			welcomeMessage = str;
			welcomeColor = c;
			backtom = true;
		}
		if(NetworkManager.instance.online)
		{
			NetworkManager.instance.client.close();
			NetworkManager.instance.online = false;
		} 
	}
}
