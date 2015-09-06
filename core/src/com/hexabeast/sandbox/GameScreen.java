package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.particles.HParticleEmmiter;

public class GameScreen implements Screen 
{	
	public SpriteBatch batch;
	
	
	public static boolean isLeftClicked = false;
	public static boolean isItemTransit = false;
	public static boolean noLimit = true;
	public static float gameZoom = 1;
	
	public static OrthographicCamera camera;
	public static AllItems items;
	public static HParticleEmmiter emmiter;
	public static Selector select;
	public static AllEntities entities;
	public static Rain rain;
	public static Inventory inventory;
	public static Player player;
	public static Flashes flashes;
	public static ShapeRenderer shapeRenderer;
	public static MiniLoading saver;
	public static LifeBar lifeBar;
	public static Paralax paralax;
	
	public static Vector2 camiddle = new Vector2();
	
	public static Joystick joy;
	
	static Vector3 camvec = new Vector3();
	
	int CurFPS;
	static Vector3 camPos = new Vector3();
	static boolean UI = false;
	
	private void Clear()
	{
		Gdx.gl.glClearColor(0,0,0,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public GameScreen() 
	{
		items = new AllItems();
		entities = new AllEntities();
		rain = new Rain();
	}

	@Override
	public void render(float delta) 
	{	
		NetworkManager.instance.modifications.applyModifications();
		NetworkManager.instance.update();
		
		Clear();
		TeleportPlayerOtherSide();
		if(Parameters.i.HQ >3)
		{
			Parameters.i.fullBright = true;
			Parameters.i.shader = true;
		}
		else
		{
			Parameters.i.fullBright = false;
			Parameters.i.shader = false;
		}
		
		if(Parameters.i.noShadow)
		{
			Parameters.i.fullBright = true;
			Parameters.i.shader = false;
		}
		
		UpdateCamera();
		
		if(!Parameters.i.fullBright || Parameters.i.shader)Map.instance.lights.UpdateLight();
		if(Parameters.i.shader && Parameters.i.fullBright)Shaders.instance.updateShadows();
		
		UpdateAndDrawRenderer();
		
		if(!Main.pause && (!player.transformed || Parameters.i.currentTransform != 4))MouseInputs();
		//TODO
		SwapCam();
		
		if(CurFPS!=Gdx.graphics.getFramesPerSecond())
		{
		Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond())); 
		CurFPS = Gdx.graphics.getFramesPerSecond();
		}
	}
	
	@Override
	public void resize(int width, int height)
	{
		if(!Parameters.i.zoomLock)
		{
			camera.viewportWidth = width;
			camera.viewportHeight = height;
		}
		else
		{
			camera.viewportWidth = 1280;
			camera.viewportHeight = ((float)height/(float)width)*1280;
		}
		
	}
	
	public static void manualResize()
	{
		if(!Parameters.i.zoomLock)
		{
			camera.viewportWidth = Gdx.graphics.getWidth();
			camera.viewportHeight = Gdx.graphics.getHeight();
		}
		else
		{
			camera.viewportWidth = 1280;
			camera.viewportHeight = ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth())*1280;
		}
		resetCamera();
	}

	@Override
	public void show() 
	{
		if(!Shaders.instance.shadow.isCompiled())Parameters.i.HQ = 3;
		if(Main.mobile)Parameters.i.HQ = 1;
		if(Main.mobile)Parameters.i.fullBright = false;
		paralax = new Paralax();
		saver = new MiniLoading();
		camera = new OrthographicCamera(1280,720);
		batch = Main.batch;
		items = entities.items;
		emmiter = new HParticleEmmiter();
		emmiter.veloBase = new Vector2(64,64);
		emmiter.automatic = false;
		emmiter.additive = false;
		emmiter.randomRot = true;
		emmiter.baselife = 0.5f;
		emmiter.rotvar = 90;
		emmiter.baseSize = 0.7f;
		emmiter.sizevar = 0.2f;
		emmiter.gravity = 400;

		select = new Selector();
		inventory = new Inventory();
		flashes = new Flashes();
		shapeRenderer = new ShapeRenderer();
		lifeBar = new LifeBar();
		
		InitializePlayerAndCamera();
		Map.instance.lights.UpdateLight();
	}

	@Override
	public void hide() 
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
	public void dispose() 
	{
		shapeRenderer.dispose();
	}
	
	public static void updatePauseClick()
	{
		SwapUI();
		if(Tools.isClicked(PauseMenu.instance.pb, 10))
		{
			Main.pause = !Main.pause;
		}
		SwapCam();
	}
	
	public static void PutOneItem(boolean isLeft)
	{
		SwapUI();
		if(Tools.isClicked(inventory.invButtonSprite, 10))
		{
		inventory.ToggleHide();
		}
		if(!inventory.hidden && isLeft == isLeftClicked)
		{
			if(isItemTransit) 
			{
				boolean isFinished = false;
				for(int i = 0; i<inventory.invItemsArray.length;i++)
				{
					if(!isFinished && Tools.isClicked(select.Selectors[i].image, Constants.invof))
					{
						isFinished = true;
						inventory.layOneItem(i,0);
						select.refreshSelector();
					}
					for(int j = 1; j<inventory.invItemsArray[i].length;j++)
					{	
						if(!isFinished && Tools.isClicked(inventory.invItemsArray[i][j].image, Constants.invof))
						{
							isFinished = true;
							inventory.layOneItem(i,j);
							select.refreshSelector();
						}
					}
				}
			}
		}
		SwapCam();
	}
	
	private void InitializePlayerAndCamera()
	{
		//TODO
		inventory.PutItem(1001);
		inventory.PutItem(1003);
		inventory.PutItem(1004);
		inventory.PutItem(1005);
		inventory.PutItem(1006);
		inventory.PutItem(1007);
		inventory.PutItem(1010);
		inventory.PutItem(1011);
		inventory.PutItem(1012);
		inventory.PutItem(1013);
		inventory.PutItem(1014);
		inventory.PutItem(1016);
		inventory.PutItem(1017);
		inventory.PutItem(1021);
		inventory.PutItem(1022);
		inventory.PutItem(1023);
		inventory.PutItem(1024);
		inventory.PutItem(1025);
		inventory.PutItem(1026);
		inventory.PutItem(1027);
		inventory.PutItem(1028);
		inventory.PutItem(1029);
		inventory.PutItem(1031);
		inventory.PutItem(1032);
		inventory.PutItem(1033);
		
		for(int i = 0; i<999; i++)
		{
			inventory.PutItem(1002);
			inventory.PutItem(1015);
			inventory.PutItem(1018);
			inventory.PutItem(1019);
			inventory.PutItem(1020);
			inventory.PutItem(1030);
			for(int j = 0; j<AllBlocTypes.instance.TileSetNumber; j++)inventory.PutItem(j);
		}
		
		
		/*for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.IronBlockId);
		
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.StoneBricksId);
		for(int i = 0; i<4000; i++)
		inventory.PutItem(AllBlocTypes.instance.TorchId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.CobbleId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.WoodId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.DirtId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.IronId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.GlassId);
		for(int i = 0; i<999; i++)
		inventory.PutItem(AllBlocTypes.instance.IceId);*/
		/*
		constructions.addPortal(TiledMap2Layer.portalInitialPos.x-17, TiledMap2Layer.portalInitialPos.y+10);
		
		constructions.addPortal(AllConstructions.exteriorPos[0].x, AllConstructions.exteriorPos[0].y);
		constructions.addHouse(AllConstructions.exteriorPos[1].x, AllConstructions.exteriorPos[1].y, 1);
		constructions.addHouse(AllConstructions.exteriorPos[2].x, AllConstructions.exteriorPos[2].y, 2);
		constructions.addHouse(AllConstructions.exteriorPos[3].x, AllConstructions.exteriorPos[3].y, 3);
		
		constructions.addDoor(AllConstructions.interiorPos[1].x, AllConstructions.interiorPos[1].y, 1);
		constructions.addDoor(AllConstructions.interiorPos[2].x, AllConstructions.interiorPos[2].y, 2);
		constructions.addDoor(AllConstructions.interiorPos[3].x, AllConstructions.interiorPos[3].y, 3);
		
		
		selectBatch = new SpriteBatch();*/
		
		player = new Player();
		player.setPosition(player.initialPos.x, player.initialPos.y);
		//TODO
		
		
		
		Tools.checkItems();
		
		camera.zoom = gameZoom;
		camera.position.set(player.PNJ.x+player.PNJ.middle.x/2, player.PNJ.y+player.PNJ.middle.y/2, camera.position.z);	
	}

	static void SwapCam()
	{
		manualResize();
		camera.zoom = gameZoom;
		if(UI)camera.position.set(camPos);
		resetCamera();
		UI = false;
	}
	
	static void SwapUI()
	{
		camera.viewportWidth = Math.max(1280,Main.windowWidth);
		camera.viewportHeight = ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth())*camera.viewportWidth;
		UI = true;
		camera.zoom = 2f;
		camPos.x = camera.position.x;
		camPos.y = camera.position.y;
		camera.position.set(0,0,camera.position.z);
		resetCamera();
	}
	
	private void UpdateCamera()
	{
		if(Main.mobile)gameZoom = Main.zoom/2;
		else gameZoom = Main.zoom;
		
		float demiCamy = camera.viewportHeight*camera.zoom/2;
		float demiCamx = camera.viewportWidth*camera.zoom/2;
		
		if(isVillage())noLimit = false;
		else noLimit = true;
		
		camera.position.set(Tools.Lerp(camera.position,new Vector3((player.PNJ.x+player.PNJ.width/2), (player.PNJ.y+player.PNJ.height/2), (int)10)));
		
		if(!noLimit)
		{
			if(camera.position.x<demiCamx+2+16*2)camera.position.x = demiCamx+2+16*2;
			if(camera.position.x>Map.instance.width*16 - demiCamx-2)camera.position.x = Map.instance.width*16 - demiCamx-2;
		}
		
		if(camera.position.y<demiCamy+2+16*2)camera.position.y = demiCamy+2+16*2;
		if(camera.position.y>Map.instance.height*16 - demiCamy-2-16*2)camera.position.y = Map.instance.height*16 - demiCamy-2-16*2;
		
		
		if(!noLimit)
		{
			if(camera.position.y<(Map.instance.randomHeight+31)*16 + demiCamy)camera.position.y = (Map.instance.randomHeight+30)*16 + demiCamy;
			
			if(camera.position.x>Map.villageWidth*16 - demiCamx-2-32)camera.position.x = Map.villageWidth*16 - demiCamx-2-32;
		}
		else if(!Main.devtest)
		{
			if(camera.position.y>Map.instance.randomHeight*16 - demiCamy && !isVillage())camera.position.y = Map.instance.randomHeight*16 - demiCamy;
		}
		resetCamera();
		camiddle.x = camera.position.x;
		camiddle.y = camera.position.y;
	}

	public static void resetCamera()
	{
		camvec.set(camera.position);
		camera.position.set((int)camera.position.x, (int)camera.position.y, (int)camera.position.z);
		camera.update();
		camera.position.set(camvec);
		Tools.computeAbsoluteMouse();
	}

	private void TeleportPlayerOtherSide()
	{
		if(noLimit)
		{
			if(camiddle.x<0)
			{
				
				float x = (Map.instance.width)*16+(player.PNJ.x);
				player.setPosition(x,player.PNJ.y);
				camera.position.set(camera.position.x+(Map.instance.width)*16, camera.position.y, camera.position.z);
				player.PNJ.hook.tpOtherSide();
				for(int l = DeforMeshes.instance.implist.size()-1; l>=0; l--)
				{
					DeforMeshes.instance.implist.get(l).x+=(Map.instance.width)*16;
				}
				
				player.PNJ.calculateShoulder();
				player.PNJ.calculateVectors();
				
			}
			if(camiddle.x>=(Map.instance.width)*16)
			{
				
				float x = 0+(player.PNJ.x-(Map.instance.width)*16);
				player.setPosition(x,player.PNJ.y);
				camera.position.set(camera.position.x-(Map.instance.width)*16, camera.position.y, camera.position.z);
				player.PNJ.hook.tpOtherSide();
				
				for(int l = DeforMeshes.instance.implist.size()-1; l>=0; l--)
				{
					DeforMeshes.instance.implist.get(l).x-=(Map.instance.width)*16;
				}
			}
			
			player.PNJ.calculateShoulder();
			player.PNJ.calculateVectors();
			
			resetCamera();
		}
	}
	
	private void UpdateAndDrawRenderer(){
		
		
		batch.setColor(Color.WHITE);
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		if(Parameters.i.FBORender)
		{
		Main.fbo.begin();
		Clear();
		batch.setBlendFunction(-1, -1);
		Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE,GL20.GL_ONE);
		}
		
		if(Parameters.i.background)
		{
			paralax.DrawBack(batch);
			batch.setColor( Parameters.i.daylight, Parameters.i.daylight, Parameters.i.daylight, 1);
			paralax.DrawAll(batch);
			batch.setColor( 1, 1, 1, 1);
		}
		batch.end();
		
		Shaders.instance.setShadowShader();
		
			batch.begin();
			
				if(Parameters.i.fullBright && Parameters.i.shader)batch.setColor( 0.25f, 0.25f, 0.25f, 1);
				else batch.setColor( 0.45f, 0.45f, 0.45f, 1);
				Map.instance.DrawBack((SpriteBatch) batch);
				batch.setColor( 0.45f, 0.45f, 0.45f, 1);
				Map.instance.backLayer.draw(batch);
				
				batch.setColor(1,1,1,1);
				AllEntities.rebootMap();
				
				entities.furnitures.checkPoints();
				entities.mobs.checkPoints();
				
				entities.trees.DrawAll(batch);
				entities.furnitures.DrawAll(batch);
				
				player.Update();
				entities.projectiles.DrawProjectiles(batch);
				
				entities.projectiles.DrawGrapples(batch);
				if(!Main.noUI)player.draw(batch);
				
				entities.mobs.DrawAll(batch);
				entities.DrawAll(batch);
				
				items.DrawAll(batch);
				
				rain.draw(batch);
				Map.instance.mainLayer.draw(batch);
				
				drawBlockShadow(batch);
				
				drawCursorShadow(batch);
				
				emmiter.update(Main.delta);
				emmiter.draw(batch);
				
				//MouseInputs();
			
			batch.end();
			
		if(Parameters.i.FBORender)
		{
			batch.setBlendFunction(-1, -1);
			Gdx.gl20.glBlendFuncSeparate(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA,GL20.GL_ONE, GL20.GL_DST_ALPHA);
			
			Main.fbo.end();
			
			Shaders.instance.setShadowShaderFBO();
			batch.setProjectionMatrix(Main.defaultMatrix);
			
			Texture rtex = Main.fbo.getColorBufferTexture();
			rtex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
	        Gdx.gl20.glEnable(GL20.GL_BLEND);
	        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
	        rtex.bind();
			
			Shaders.instance.meshader.begin();
			Shaders.instance.meshader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
			Shaders.instance.meshader.setUniformi("u_texture", 0);

			DeforMeshes.instance.createFullScreenQuad();
			
			Main.mesh.render(Shaders.instance.meshader, GL20.GL_TRIANGLES);
			
			Shaders.instance.meshader.end();
		}

		Shaders.instance.setDefaultShader();
		
		SwapUI();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
			if(!Main.noUI)
			{
				select.setPosition(camera.position.x,camera.position.y+camera.viewportHeight, Main.delta);
				select.selectorBackGround.draw(batch);
				for(int i = 0; i<select.backSelectorSprites.length;i++)
				{
					select.backSelectorSprites[i].draw(batch);
				}
				for(int i = 0; i<select.Selectors.length;i++)
				{
					select.Selectors[i].drawAll(batch);
				}
				select.torchSprite.draw(batch);
				
				inventory.invButtonSprite.setPosition(camera.viewportWidth-inventory.invButtonSprite.getWidth()*4,camera.viewportHeight-inventory.invButtonSprite.getHeight()-200);
				inventory.invButtonSprite.draw(batch);
				
				PauseMenu.instance.drawPauseButton(batch);
		
				inventory.setPosition(camera.position.x,camera.position.y+camera.viewportHeight);
				inventory.drawAll(batch);
		
				lifeBar.DrawAll(-camera.viewportWidth+200,camera.viewportHeight-70,batch);
				saver.DrawAll(batch);
				
				if(Parameters.i.fullBright && !Parameters.i.shader)FontManager.instance.font.draw(batch, "Shadows: None", camera.viewportWidth-500, camera.viewportHeight-25);
				else FontManager.instance.font.draw(batch, "Shadows: "+Constants.qualities[Parameters.i.HQ], camera.viewportWidth-500, camera.viewportHeight-25);
				
				if(Main.mobile)
				{
					Color coltemp = batch.getColor();
					batch.setColor(Color.WHITE);
					joy.draw(batch);
					batch.setColor(coltemp);
				}
				
				if(Main.pause)PauseMenu.instance.draw(batch);
				
			}
			
		batch.end();
		
		SwapCam();
		
		flashes.DrawAll();
		
		if(player.isDead)
		{
			flashes.FlashDead();
			player.isDead = false;
		}
		else if(player.hurt)
		{
			flashes.FlashHurt();
			player.hurt = false;
		}
		
	}
	
	public void drawCursorShadow(SpriteBatch batch)
	{
		if(inventory.hidden && !Main.pause)
		{
			boolean axe = (GameScreen.player.currentCellID>999 && AllTools.instance.getType(GameScreen.player.currentCellID).type == AllTools.instance.Axe);
			boolean hand = (GameScreen.player.currentCellID==0);
			
			if(axe || hand)
			{
				Color col = batch.getColor();
				float range;
				
				Vector2 mous = Tools.getAbsoluteMouse();
				int px = Tools.floor(mous.x/16);
				int py = Tools.floor(mous.y/16);
				
				if(Inputs.instance.shift)
				{

					Vector2 defaultVec = new Vector2(mous).sub(GameScreen.player.PNJ.shoulderPos);
					
					Vector2 faster = new Vector2(1000000,1000000);
					Vector2 bestaimed = new Vector2(-1,-1);
					float limitlen = new Vector2(px*16+8 - GameScreen.player.PNJ.middle.x,py*16+8 - GameScreen.player.PNJ.middle.y).len();
					
					for(int i = 0; i<7; i++)
					{
						Vector2 aimed = new Vector2(Tools.raycastVec(GameScreen.player.PNJ.middle.x, GameScreen.player.PNJ.middle.y-22+9*(i), defaultVec.x, defaultVec.y, 4));
						
						
						if(aimed.y>=0)
						{
							Vector2 testvec = new Vector2(aimed.x*16+8 - GameScreen.player.PNJ.middle.x,aimed.y*16+8 - GameScreen.player.PNJ.middle.y);

							if(faster.len()>testvec.len() && limitlen>testvec.len())
							{
								faster = testvec;
								bestaimed = aimed;
							}
						}
					}
					
					if(bestaimed.y>=0)
					{
						px = Tools.floor(bestaimed.x+0.1f);
						py = Tools.floor(bestaimed.y+0.1f);
					}
				}
				
				if(axe)range = AllTools.instance.getType(GameScreen.player.currentCellID).range;
				else range = ModifyTerrain.range;
				
				ModifyTerrain.instance.distToMiddleVec.x = px*16+8-GameScreen.player.PNJ.shoulderPos.x;
				ModifyTerrain.instance.distToMiddleVec.y = py*16+8-GameScreen.player.PNJ.shoulderPos.y;
				float distToMiddle = ModifyTerrain.instance.distToMiddleVec.len();
					
				if(distToMiddle>range)
				{
					batch.setColor(1,1/4, 1/4, 0.4f);
				}
				else
				{
					batch.setColor(1,0.85f, 0.6f, 0.4f);
				}
			
				
				batch.draw(TextureManager.instance.cadre, px*16-3, py*16-3, 22, 22);

				batch.setColor(col);
				
			}
		}
	}
	
	public void drawBlockShadow(SpriteBatch batch)
	{
		if(inventory.hidden && !Main.pause)
		{
			boolean furniture = (GameScreen.player.currentCellID>999 && AllTools.instance.getType(GameScreen.player.currentCellID).type == AllTools.instance.Furniture);
			boolean block = (GameScreen.player.currentCellID<999 && GameScreen.player.currentCellID>0);
			boolean tree = (GameScreen.player.currentCellID>999 && AllTools.instance.getType(GameScreen.player.currentCellID).type == AllTools.instance.Tree);
			
			
			if(tree || furniture || block)
			{
				TextureRegion tex;
				Color col = batch.getColor();
				float range;
				
				Vector2 mous = Tools.getAbsoluteMouse();
				int px = Tools.floor(mous.x/16);
				int py = Tools.floor(mous.y/16);
				
				int w = 16;
				int h = 16;
				
				ModifyTerrain.instance.distToMiddleVec.x = px*16+8-GameScreen.player.PNJ.middle.x;
				ModifyTerrain.instance.distToMiddleVec.y = py*16+8-GameScreen.player.PNJ.middle.y;
				float distToMiddle = ModifyTerrain.instance.distToMiddleVec.len();
				
				boolean ok = true;
				
				
				if(furniture)
				{	
					range = AllTools.instance.getType(GameScreen.player.currentCellID).range;
					if(!ModifyTerrain.instance.furnitureCollision(GameScreen.player.currentCellID,px,py))ok = false;
					tex = AllTools.instance.getType(GameScreen.player.currentCellID).tex;
					w = AllTools.instance.getType(GameScreen.player.currentCellID).furnitureWidth*16;
					h = AllTools.instance.getType(GameScreen.player.currentCellID).furnitureHeight*16;
				}
				else if(block)
				{
					range = ModifyTerrain.range;
					if(AllBlocTypes.instance.getType(GameScreen.player.currentCellID).oldTexture)
					{
						tex = TextureManager.instance.TextureRegions[GameScreen.player.currentCellID][AllBlocTypes.instance.Alone];
					}
					else
					{
						tex = TextureManager.instance.TextureRegions[GameScreen.player.currentCellID][AllBlocTypes.instance.Nosides%10];
					}
					
					if(!ModifyTerrain.instance.justone(Map.instance.mainLayer, px, py, GameScreen.player.currentCellID))ok = false;
					else if(Map.instance.mainLayer.getBloc(px, py).Id != 0)ok = false;
					else if(AllEntities.getEntity(px, py) != null)ok = false;
					else if(AllBlocTypes.instance.getType(GameScreen.player.currentCellID).needBack &&  !Map.instance.backLayer.getBloc(px, py).collide)ok = false;
				}
				else
				{
					range = AllTools.instance.getType(GameScreen.player.currentCellID).range;
					tex = GameScreen.items.getTextureById(GameScreen.player.currentCellID);
					w = 32;
					h = 32;
					if(ModifyTerrain.instance.treeCollision(px,py))ok = false;
				}
				
				
				
				if(!ok)
				{
					batch.setColor(1,1/4, 1/4, 0.7f);
				}
				else if(distToMiddle>range)
				{
					batch.setColor(1,1/1.5f, 1/4, 0.7f);
				}
				else
				{
					batch.setColor(1/4,1, 1/4, 0.7f);
				}
							
				
				int offy = -2;
				if(block)offy = 0;
				if(tree)offy = -6;
				if(!player.currentForm.isTurned || (!furniture))batch.draw(tex, px*16, py*16+offy, w, h);
				else batch.draw(tex, px*16+w, py*16+offy, -w, h);

				batch.setColor(col);
			}
			
		}
	}

	public void MouseInputs()
	{
		
		SwapUI();
		boolean abort = false;
		for(int i = 0; i<select.backSelectorSprites.length; i++)
		{
			if(Tools.isClicked(select.backSelectorSprites[i], Constants.invof))abort = true;
		}
		SwapCam();
		
		if(Main.mobile)
		{
			if(joy.isTouching || joy.isClicked(Tools.getAbsoluteMouseUI(Gdx.input.getX(),Gdx.input.getY())))abort = true;
		}
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			//PUTCELL
			if(!abort && inventory.hidden && !player.transformed)ModifyTerrain.instance.PutCell( player.currentCellID,player.currentCellState, Map.instance.mainLayer,Main.time,true);
		}
		else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
			//PUTCELL
			boolean backPlaced;
			if(player.currentCellID>999)
				backPlaced = false;
			else
				backPlaced = AllBlocTypes.instance.getType(player.currentCellID).needBack;
			
			if(!abort && inventory.hidden && !backPlaced && !player.transformed)ModifyTerrain.instance.PutCell(player.currentCellID,player.currentCellState, Map.instance.backLayer,Main.time,false);
		}
	}
	
	
	
	public static boolean isVillage()
	{
		if(Main.mobile)return false;
		if(player.PNJ.x<Map.villageWidth*16 && player.PNJ.y>Map.instance.height*16-Map.villageHeight*16)
		{
			return true;
		}
		return false;
	}
}
