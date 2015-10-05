package com.hexabeast.sandbox;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hexabeast.hexboxserver.NCompressedLayer;

public class LoadingScreen implements Screen {

	public OrthographicCamera camera;
	
	Texture loadingJ;
	TextureRegion loadingV;
	Texture backGround;
	TextureRegion loadingShadow;
	boolean passed = false;
	
	Texture[] loadingCadre = new Texture[7];
	
	SpriteBatch batch;
	int state = 0;
	int checkState = 2;
	float progress = 0f;
	float progresspeed = 0.3f;
	float progressSmooth = 0f;
	float width = Gdx.graphics.getWidth();
	float height = Gdx.graphics.getHeight();
	boolean mapLoaded = false;
	
	public boolean NmainLoaded;
	public boolean NbackLoaded;
	public NCompressedLayer NmainLayer;
	public NCompressedLayer NbackLayer;
	
	boolean random;
	public LoadingScreen(boolean Random) {
		random = Random;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
      //  if(load && random)LoadStateRandom();
      //  else if(load && !random)LoadStateFile();
        
        
        if(!passed)
		{
			new Thread
			(
				new Runnable()
				{
					@Override
					public void run()
					{
						if(Main.multiplayer)
						{
							LoadNetwork();
							mapLoaded = true;
							
						}
						else
						{
							if(random)LoadStateRandomAll();
							else if(!random)LoadStateFileAll();
							
							if(Main.host)
							{
								if(!NetworkManager.instance.host())Main.backToMenu("Failed launching the server (client)", Color.RED);
								if(NetworkManager.instance.server.problem)Main.backToMenu("Failed launching the server (port)", Color.RED);
								else Main.multiplayer= true;
							}
							mapLoaded = true;
							
							Gdx.app.postRunnable(new Runnable()
							{
								@Override
								public void run()
								{
									
								}
							});
						}
						
					}
				}
			).start();
			
			passed = true;
		}
        
        if(progress+0.1f>progressSmooth && progress+0.1f<1)
        {
        	progressSmooth+=progresspeed/30*Main.delta;
        }
        
        if(progress>progressSmooth)
        {
        	progressSmooth+=progresspeed/2*Main.delta;
        }
        
        if(progress>progressSmooth+0.1f && progresspeed>=0.09f)
        {
        	progressSmooth+=progresspeed*Main.delta;
        }
        
        
        if(0.8f<progressSmooth && progress<progressSmooth)
        {
        	progressSmooth=progress;
        }
        
        camera.update();
        float Jwidth = progressSmooth*loadingJ.getWidth();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float x = -loadingV.getRegionWidth()/2; 
        float y = -loadingV.getRegionHeight()/2;
        
        float srcX = 0;
		float srcY = 0;
		float srcWidth = Jwidth;
		float srcHeight = loadingJ.getHeight();
		
		float u = srcX / loadingJ.getWidth();
		float v2 = srcY / loadingJ.getHeight();
		float u2 = (srcX + srcWidth) / loadingJ.getWidth();
		float v = (srcY + srcHeight) / loadingJ.getHeight();
		batch.draw(backGround,-backGround.getWidth()/2,-backGround.getHeight()/2);
		batch.draw(loadingShadow,-loadingShadow.getRegionWidth()/2,-loadingShadow.getRegionHeight()/2);
		
		int number = (int) (6.9f*progressSmooth);
		float nuance =  6.9f*progressSmooth-number;
		if(number<0)number = 0;
		batch.draw(loadingCadre[0],-loadingCadre[number].getWidth(),-loadingCadre[number].getHeight(),loadingCadre[number].getWidth()*2,loadingCadre[number].getHeight()*2);
		if(number>0)batch.draw(loadingCadre[number],-loadingCadre[number].getWidth(),-loadingCadre[number].getHeight(),loadingCadre[number].getWidth()*2,loadingCadre[number].getHeight()*2);
		batch.setColor(1,1,1,nuance);
		if(number<6 && progressSmooth>0)batch.draw(loadingCadre[number+1],-loadingCadre[number].getWidth(),-loadingCadre[number].getHeight(),loadingCadre[number].getWidth()*2,loadingCadre[number].getHeight()*2);
		batch.setColor(0,0,0,1);
		batch.draw(loadingJ,x+4,y);
		batch.setColor(1,1,1,1);
		batch.draw(loadingJ, x+4, y, Jwidth, loadingJ.getHeight(), u, v, u2, v2);
        batch.draw(loadingV,x,y);
        batch.end();
	}
	
	public void LoadNetwork()
	{
		System.out.println(1);
		while(!NetworkManager.instance.online)
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(2);
		progress =0.2f;
		NetworkManager.instance.sendTCP("GetBackLayer");
		
		while(!NbackLoaded)
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		progress =0.3f;
		System.out.println(3);
		try {
			Map.instance.backLayer = MapGenerator.instance.NLoad(NbackLayer.layer, false);
		} catch (Exception e1) {
			e1.printStackTrace();
			Main.backToMenu("Error loading map (layer 2)", Color.RED);
		}
		
		progress =0.4f;
		System.out.println(4);
		
		while(!NmainLoaded)
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		progress =0.5f;
		System.out.println(5);
		
		try {
			Map.instance.mainLayer = MapGenerator.instance.NLoad(NmainLayer.layer, true);
		} catch (Exception e1) {
			e1.printStackTrace();
			Main.backToMenu("Error loading map (layer 1)", Color.RED);
		}
		
		checkMap();
	}
	
	
	public void LoadStateFileAll()
	{	

		try {
			MapGenerator.instance.LoadTrees(Map.instance.mapFileTrees);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			MapGenerator.instance.LoadFurs(Map.instance.mapFileFur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			MapGenerator.instance.LoadMobs(Map.instance.mapFileMob);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Map.instance.mainLayer = MapGenerator.instance.Load(Map.instance.mapFile1, true);

		} catch (Exception e) {
			e.printStackTrace();
			Main.backToMenu("Error loading map (layer 1)", Color.RED);
		}
		progress = 0.2f;
		try {
			Map.instance.backLayer = MapGenerator.instance.Load(Map.instance.mapFile2, false);
		} catch (Exception e) {
			e.printStackTrace();
			Main.backToMenu("Error loading map (layer 2)", Color.RED);
		}
		progress = 0.4f;
		
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.backLayer, i);
				progress = 0.4f+((float)i/(float)(Map.instance.height-1))*0.3f;
			}
		}
		progress = 0.7f;
		
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.mainLayer, i);
				progress = 0.7f+((float)i/(float)(Map.instance.height-1))*0.3f;
			}
		}
		
		progress = 1f;
		
	}
	
	public void checkMap()
	{
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.backLayer, i);
				progress = 0.4f+((float)i/(float)(Map.instance.height-1))*0.3f;
			}
		}
		
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.mainLayer, i);
				progress = 0.7f+((float)i/(float)(Map.instance.height-1))*0.3f;
			}
		}
	}
	
	public void LoadStateRandomAll()
	{
		progress = 0.1f;
		
		
		MapGenerator.instance.GenerateMap();
		progress = 0.25f;
		
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.backLayer, i);
				progress = 0.25f+((float)i/(float)(Map.instance.height-1))*0.2f;
			}
		}
		progress = 0.65f;
		
		for(int i = 2; i<Map.instance.height; i++)
		{
			if(i<Map.instance.height-1)
			{
				MapChecker.instance.CheckLine(Map.instance.mainLayer, i);
				progress = 0.45f+((float)i/(float)(Map.instance.height-1))*0.2f;
			}
		}
		progress = 0.7f;
		
		try {
			Map.instance.SaveLayer(Map.instance.mapFile1, Map.instance.mainLayer,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		progresspeed = 0.07f;
		progress = 1f;
		
		try {
			Map.instance.SaveLayer(Map.instance.mapFile2, Map.instance.backLayer,true);
			Map.instance.SaveEntities();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		progress = 1;
		
	}
	
	
/*	
	public void LoadStateFile()
	{	
		
		if(state == 4)
		{
			for(int i = checkState; i<checkState+50; i++)
			{
				if(i<TerMH.map.height-1)
				{
					MapChecker.instance.CheckLine(TerMH.map.mainLayer, i);
				}
			}
			
			if(checkState>=TerMH.map.height-1)
			{
				checkState = 2;
				mapLoaded = true;
				state++;
			}
			else
			{
				checkState+=50;
				progress = 0.65f + 0.35f*((float)checkState/(float)TerMH.map.height);
			}
		}
		
		if(state == 3)
		{
			for(int i = checkState; i<checkState+50; i++)
			{
				if(i<TerMH.map.height-1)
				{
					MapChecker.instance.CheckLine(TerMH.map.backLayer, i);
				}
			}
			
			if(checkState>=TerMH.map.height-1)
			{
				checkState = 2;
				state++;
			}
			else
			{
				checkState+=50;
				progress = 0.3f + 0.35f*((float)checkState/(float)TerMH.map.height);
			}
		}
		
		if(state == 2)
		{
			try {
				TerMH.map.backLayer = TerMH.map.Load(TerMH.map.mapFile2);
				//TerMH.map.villageBack = TerMH.map.LoadVillage(TerMH.map.mapFileVillage2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			state++;
			progress = 0.3f;
		}
		if(state == 1)
		{
			try {
				TerMH.map.mainLayer = TerMH.map.Load(TerMH.map.mapFile1);
				//TerMH.map.villageMain = TerMH.map.LoadVillage(TerMH.map.mapFileVillage1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			state++;
			progress = 0.2f;
		}
		if(state == 0)
		{
			try {
				TerMH.map.LoadTrees(TerMH.map.mapFileTrees);
			} catch (Exception e) {
				e.printStackTrace();
			}
			state++;
			progress = 0.1f;
		}
	}
	
	public void LoadStateRandom()
	{
		
		if(state == 9)
		{
			for(int i = checkState; i<checkState+50; i++)
			{
				if(i<TerMH.map.height-1)
				{
					MapChecker.instance.CheckLine(TerMH.map.mainLayer, i);
				}
			}
			
			if(checkState>=TerMH.map.height-1)
			{
				checkState = 2;
				mapLoaded = true;
				state++;
			}
			else
			{
				checkState+=50;
				progress = 0.65f + 0.35f*((float)checkState/(float)TerMH.map.height);
			}
		}
		
		if(state == 8)
		{
			for(int i = checkState; i<checkState+50; i++)
			{
				if(i<TerMH.map.height-1)
				{
					MapChecker.instance.CheckLine(TerMH.map.backLayer, i);
				}
			}
			
			if(checkState>=TerMH.map.height-1)
			{
				checkState = 2;
				state++;
			}
			else
			{
				checkState+=50;
				progress = 0.3f + 0.35f*((float)checkState/(float)TerMH.map.height);
			}
		}
		
		if(state == 7)
		{
			TerMH.map.backLayer.Load2Back();
			state++;
			progress = 0.3f;
		}
		
		
		if(state == 6)
		{
			TerMH.map.backLayer.Load1Back();
			state++;
			progress = 0.28f;
		}
		
		if(state == 5)
		{
			TerMH.map.mainLayer.Load6();
			state++;
			progress = 0.24f;
		}
		
		if(state == 4)
		{
			TerMH.map.mainLayer.Load5();
			state++;
			progress = 0.20f;
		}
		
		if(state == 3)
		{
			TerMH.map.mainLayer.Load4();
			state++;
			progress = 0.16f;
		}
		
		if(state == 2)
		{
			TerMH.map.mainLayer.Load3();
			state++;
			progress = 0.12f;
		}
		
		if(state == 1)
		{
			try {
				TerMH.map.villageMain = TerMH.map.LoadVillage(TerMH.map.mapFileVillage1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			TerMH.map.mainLayer.Load2();
			state++;
			progress = 0.08f;
		}
		
		if(state == 0)
		{
			try {
				TerMH.map.villageBack = TerMH.map.LoadVillage(TerMH.map.mapFileVillage2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			TerMH.map.mainLayer.Load1();
			state++;
			progress = 0.04f;
		}
	}
*/
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		camera.viewportWidth = 1280;
		camera.viewportHeight = 720;//((float)height/(float)width)*1280;
		camera.update();
	}

	@Override
	public void show() {
		Main.pause = false;
		batch = Main.batch;
		loadingJ = TextureManager.instance.loadingJ;
		loadingV = TextureManager.instance.loadingV;
		loadingShadow = TextureManager.instance.loadingShadow;
		
		loadingCadre[0] = TextureManager.instance.cad1;
		loadingCadre[1] = TextureManager.instance.cad2;
		loadingCadre[2] = TextureManager.instance.cad3;
		loadingCadre[3] = TextureManager.instance.cad4;
		loadingCadre[4] = TextureManager.instance.cad5;
		loadingCadre[5] = TextureManager.instance.cad6;
		loadingCadre[6] = TextureManager.instance.cad7;
		
		backGround = TextureManager.instance.backGround;
		camera = new OrthographicCamera();
	}

	@Override
	public void hide() {
		dispose();
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
