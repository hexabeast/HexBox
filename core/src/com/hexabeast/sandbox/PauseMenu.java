package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseMenu {
	public static PauseMenu instance;
	
	public PauseMenuButton resume;
	public PauseMenuButton quit;
	public PauseMenuButton settings;
	public PauseMenuButton back;
	public PauseMenuButton cheats;
	
	public PauseSetting resolution;
	public PauseSetting fullscreen;
	public PauseSetting shadowQuality;
	public PauseSetting lightSpeed;
	public PauseSetting lightColor;
	public PauseSetting details;
	public PauseSetting vsync;
	public PauseSetting HQMagic;
	public PauseSetting background;
	public PauseSetting lightDistance;
	public PauseSetting FBO;
	public PauseSetting zoomlock;
	public PauseSetting ratio;
	
	public PauseSetting volume;
	public PauseSetting volumeMusic;
	public PauseSetting volumeFX;
	
	public PauseSetting keyboard;
	
	public PauseSetting time;
	public PauseSetting fullbright;
	public PauseSetting superman;
	public PauseSetting ultramagic;
	public PauseSetting ultrarate;
	public PauseSetting gamespeed;
	public PauseSetting hitbox;
	public PauseSetting ultrarange;
	public PauseSetting godmode;
	public PauseSetting rain;
	public PauseSetting axerange;
	public PauseSetting transform;
	
	Sprite pb;
	
	public boolean clear = false;
	public boolean setting = false;
	public boolean main = false;
	public boolean cheat = false;
	int settingtab = 0;
	
	public Button tabGraphics;
	public Button tabDisplay;
	public Button tabAudio;
	public Button tabKeyboard;
	
	public static final int TAB_GRAPHICS = 1;
	public static final int TAB_DISPLAY = 0;
	public static final int TAB_AUDIO = 2;
	public static final int TAB_KEYBOARD = 3;
	
	PauseMenu()
	{
		pb = new Sprite(TextureManager.instance.pauseButton);
		pb.setOrigin(0, 0);
		pb.setScale(4);
		
		resume = new PauseMenuButton(TextureManager.instance.pauseresume,0,200);
		quit = new PauseMenuButton(TextureManager.instance.pauseexit,0,-400);
		settings = new PauseMenuButton(TextureManager.instance.pausesettings,0,0);
		back = new PauseMenuButton(TextureManager.instance.pauseback,-1080,-560);
		cheats = new PauseMenuButton(TextureManager.instance.pausecheat, 0,-200);
		
		//TABS
		tabGraphics = new Button(-320,600,TextureManager.instance.sGraphicsButton);
		tabGraphics.w*=2;
		tabGraphics.h*=2;
		
		tabDisplay = new Button(-720,600,TextureManager.instance.sDisplayButton);
		tabDisplay.w*=2;
		tabDisplay.h*=2;
		
		tabAudio = new Button(80,600,TextureManager.instance.sAudioButton);
		tabAudio.w*=2;
		tabAudio.h*=2;
		
		tabKeyboard = new Button(480,600,TextureManager.instance.sKeyboardButton);
		tabKeyboard.w*=2;
		tabKeyboard.h*=2;
		
		//DISPLAY
		zoomlock = new PauseSetting(0,-200,"Zoom Lock :","Locks the zoom to HD default");
		resolution = new PauseSetting(0,200,"Resolution :","Changes the resolution of the window");
		ratio = new PauseSetting(0,-100,"Ratio :","Changes the ratio of the window");
		fullscreen = new PauseSetting(0,100,"FullScreen :","Enables / disables fullscreen mode");
		vsync = new PauseSetting(0,0,"VSync :","Enables/Disables VSync");
		
		//GRAPHICS
		shadowQuality = new PauseSetting(0,300,"Shadows Quality :","Shader is often faster than Medium Quality for a better rendering quality");
		lightSpeed = new PauseSetting(0,200,"Light Speed :","Higher number = faster lights but worse performances");
		lightColor = new PauseSetting(0,100,"Colored Lights :","Colored lights require more CPU but looks better than white lights");
		lightDistance = new PauseSetting(0,-400,"Light Prevision :","Reduces light glitches on the sides of the screen, but makes the game slower");
		details = new PauseSetting(0,00,"Details :","Ambient occlusion, better grass and better transitions between blocks");
		HQMagic = new PauseSetting(0,-100,"Spells quality :","Changes the quality of magic things");
		background = new PauseSetting(0,-200,"Background :","Enables/Disables background");
		FBO = new PauseSetting(0,-300,"More Shaders :","Slows down the game, enables magic space deformations and better colors");
		
		//AUDIO
		volume = new PauseSetting(0,200,"Master Volume :","Set the general volume of the game");
		volumeMusic = new PauseSetting(0,100,"Music Volume :","Set the volume of the music");
		volumeFX = new PauseSetting(0,000,"SFX Volume :","Set the volume of the effects");
		
		//KEYBOARD
		keyboard = new PauseSetting(0,200,"Keyboard Layout :","Set keyboard layout (QWERTY or AZERTY only)");
		
		//CHEATS
		time = new PauseSetting(0,500,"Time :","Cheat, Changes the time between night and day");
		fullbright = new PauseSetting(0,400,"Fullbright :","Cheat, Disables all shadows, and increases performances");
		superman = new PauseSetting(0,300,"Superman :","Cheat, increases significantly speed and jump height");
		ultramagic = new PauseSetting(0,200,"Apocalyptic magic :","Cheat, makes magic wands useful for mining");
		ultrarate = new PauseSetting(0,100,"Ultra rate :","Cheat, multiply by 10 the rate of all tools and weapons");
		gamespeed = new PauseSetting(0,0,"Game speed :","Cheat, speeds up or slows down time, can cause bugs");
		hitbox = new PauseSetting(0,-100,"Draw Hitboxes :","Draws hitboxes");
		ultrarange = new PauseSetting(0,-200,"No range :","Cheat, permit to put/break blocks anywhere on the screen");
		godmode = new PauseSetting(0,-300,"God Mode :","Cheat, provides invulnerability");
		rain = new PauseSetting(0,-400,"Rain :","Cheat, enables/disables rain");
		axerange = new PauseSetting(0,-500,"Alt Pickaxe :","enables a different way to dig");
		transform = new PauseSetting(0,600,"Transform :","choose what you want to be");
	}
	
	public void drawPauseButton(SpriteBatch batch)
	{
		pb.setPosition(GameScreen.camera.viewportWidth-pb.getWidth()*2.6f,GameScreen.camera.viewportHeight-pb.getHeight()*2.6f);
		pb.draw(batch);
	}
	
	public void drawMenu(SpriteBatch batch, float x, float y)
	{
		resume.draw(batch);
		quit.draw(batch);
		settings.draw(batch);
		if(Main.enableCheats)cheats.draw(batch);
		
		if(Inputs.instance.mousedown)
		{
			if(resume.isTouched(x,y))Main.pause = false;
			if(quit.isTouched(x,y))Main.backToMenu(Main.defaultWelcomeMessage, Color.WHITE);
			if(settings.isTouched(x,y))
			{
				main = false;
				setting = true;
				cheat = false;
			}
			if(Main.enableCheats && cheats.isTouched(x,y))
			{
				main = false;
				setting = false;
				cheat = true;
			}
		}
	}
	
	public void drawSettings(SpriteBatch batch, float x, float y, boolean pause)
	{
		String resx = String.valueOf(Constants.resolutions[Parameters.i.resolution]);
		float ratiof = 3f/4f;
		if(Parameters.i.ratio)ratiof = 9f/16f;
		
		String resy = String.valueOf((int)(ratiof*Constants.resolutions[Parameters.i.resolution]));
		
		resx = String.valueOf(Gdx.graphics.getWidth());
		resy = String.valueOf(Gdx.graphics.getHeight());
		
		batch.draw(TextureManager.instance.sButtonHolder, -TextureManager.instance.sButtonHolder.getRegionWidth()*2, 470,TextureManager.instance.sButtonHolder.getRegionWidth()*4,TextureManager.instance.sButtonHolder.getRegionHeight()*4);
		
		if(settingtab == TAB_DISPLAY)
		{
			resolution.value = resx+"x"+resy;
			resolution.draw(batch);
			
			fullscreen.value = Tools.booltoyes(Parameters.i.fullscreen);
			fullscreen.draw(batch);
			
			vsync.value = Tools.booltoyes(Parameters.i.vsync);
			vsync.draw(batch);
			
			zoomlock.value = Tools.booltoyes(Parameters.i.zoomLock);
			zoomlock.draw(batch);
			
			if(Parameters.i.ratio)ratio.value = "16:9";
			else ratio.value = "4:3";
			ratio.draw(batch);
			
			if(Inputs.instance.mousedown)
			{
				if(zoomlock.isTouchedLeft(x,y) || zoomlock.isTouchedRight(x,y))
				{
					Parameters.i.zoomLock = !Parameters.i.zoomLock;
					if(Main.ingame)GameScreen.manualResize();
				}
				
				else if(ratio.isTouchedLeft(x,y) || ratio.isTouchedRight(x,y))
				{
					Parameters.i.ratio = !Parameters.i.ratio;
					Main.updateResolution();
				}
				
				else if(resolution.isTouchedRight(x,y))
				{
					for(int i = 0; i<Constants.resolutions.length; i++)
					{
						if(Constants.resolutions[i] > Gdx.graphics.getWidth()+0.1f)
						{
							Parameters.i.resolution = i;
							break;
						}
						else if(i == Constants.resolutions.length-1)Parameters.i.resolution = i;
					}
					
					Main.updateResolution();
				}
				else if(resolution.isTouchedLeft(x,y))
				{
					if(Parameters.i.resolution>0)
					{
						for(int i = Constants.resolutions.length-1; i>=0; i--)
						{
							if(Constants.resolutions[i] < Gdx.graphics.getWidth()-0.1f)
							{
								Parameters.i.resolution = i;
								break;
							}
							else if(i == 0)Parameters.i.resolution = i;
						}
						Main.updateResolution();
					}
				}
				
				
				else if(fullscreen.isTouchedRight(x,y)||fullscreen.isTouchedLeft(x,y))
				{
					Parameters.i.fullscreen = !Parameters.i.fullscreen;
					Main.updateResolution();
				}
				
				else if(vsync.isTouchedRight(x,y)||vsync.isTouchedLeft(x,y))
				{
					Parameters.i.vsync = !Parameters.i.vsync;
					Gdx.graphics.setVSync(Parameters.i.vsync);
				}
			}
		}
		else if(settingtab == TAB_GRAPHICS)
		{
			shadowQuality.value = Constants.qualities[Parameters.i.HQ];
			shadowQuality.draw(batch);
			
			lightSpeed.value = String.valueOf(Parameters.i.lightSpeed)+" m/s";
			lightSpeed.draw(batch);
			
			lightDistance.value = Constants.lightDistancesNames[Parameters.i.lightDistance];
			lightDistance.draw(batch);
			
			lightColor.value = Tools.booltoyes(Parameters.i.RGB);
			lightColor.draw(batch);
			
			details.value = Tools.booltoyes(Parameters.i.details);
			details.draw(batch);
			
			if(Parameters.i.goodmagic)
			{
				HQMagic.value = "High";
			}
			else
			{
				HQMagic.value = "Low";
			}
			HQMagic.draw(batch);
			
			background.value = Tools.booltoyes(Parameters.i.background);
			background.draw(batch);
			
			FBO.value = Tools.booltoyes(Parameters.i.FBORender);
			FBO.draw(batch);
			
			if(Inputs.instance.mousedown)
			{
				if(FBO.isTouchedRight(x,y)||FBO.isTouchedLeft(x,y))Parameters.i.FBORender = !Parameters.i.FBORender;
				
				else if(HQMagic.isTouchedRight(x,y)||HQMagic.isTouchedLeft(x,y))Parameters.i.goodmagic = !Parameters.i.goodmagic;
				
				
				
				else if(background.isTouchedRight(x,y)||background.isTouchedLeft(x,y))
				{
					Parameters.i.background = !Parameters.i.background;
				}
				
				
				else if(shadowQuality.isTouchedLeft(x,y)){if(Parameters.i.HQ>1)Parameters.i.HQ--;}
				else if(shadowQuality.isTouchedRight(x,y)){if(Parameters.i.HQ<Constants.qualities.length-1)Parameters.i.HQ++;}
				
				else if(lightDistance.isTouchedLeft(x,y)){if(Parameters.i.lightDistance>0)Parameters.i.lightDistance--;}
				else if(lightDistance.isTouchedRight(x,y)){if(Parameters.i.lightDistance<Constants.lightDistances.length-1)Parameters.i.lightDistance++;}
				
				else if(lightSpeed.isTouchedLeft(x,y))
					{
						if(Parameters.i.lightSpeed>=60)
						{
							Parameters.i.lightSpeed-=30;
							if(Parameters.i.lightSpeed%30!=0)Parameters.i.lightSpeed=120;
						}
					}
					
				else if(lightSpeed.isTouchedRight(x,y))
					{
						if(Parameters.i.lightSpeed<=270)
						{
							Parameters.i.lightSpeed+=30;
							if(Parameters.i.lightSpeed%30!=0)Parameters.i.lightSpeed=120;
						}	
					}
					
				else if(lightColor.isTouchedRight(x,y)||lightColor.isTouchedLeft(x,y))Parameters.i.RGB = !Parameters.i.RGB;
				else if(details.isTouchedRight(x,y)||details.isTouchedLeft(x,y))Parameters.i.details = !Parameters.i.details;
			}
		}
		
		else if(settingtab == TAB_AUDIO)
		{
			volume.value = String.valueOf(Parameters.i.volume*10);
			volumeMusic.value = String.valueOf(Parameters.i.volumeMusic*10);
			volumeFX.value = String.valueOf(Parameters.i.volumeFX*10);
			
			volume.draw(batch);
			volumeMusic.draw(batch);
			volumeFX.draw(batch);
			
			if(Inputs.instance.mousedown)
			{
				if(volume.isTouchedLeft(x,y)){if(Parameters.i.volume>0)Parameters.i.volume--;}
				else if(volume.isTouchedRight(x,y)){if(Parameters.i.volume<10)Parameters.i.volume++;}
				
				else if(volumeMusic.isTouchedLeft(x,y)){if(Parameters.i.volumeMusic>0)Parameters.i.volumeMusic--;}
				else if(volumeMusic.isTouchedRight(x,y)){if(Parameters.i.volumeMusic<10)Parameters.i.volumeMusic++;}
				
				else if(volumeFX.isTouchedLeft(x,y)){if(Parameters.i.volumeFX>0)Parameters.i.volumeFX--;}
				else if(volumeFX.isTouchedRight(x,y)){if(Parameters.i.volumeFX<10)Parameters.i.volumeFX++;}
				
				SoundManager.instance.updateVolume();
			}
		}
		
		else if(settingtab == TAB_KEYBOARD)
		{
			if(Parameters.i.keyboard == HKeys.AZERTY)keyboard.value = "AZERTY";
			else if(Parameters.i.keyboard == HKeys.QWERTY)keyboard.value = "QWERTY";
			
			keyboard.draw(batch);
			
			if(Inputs.instance.mousedown)
			{
				if(keyboard.isTouchedLeft(x,y) || keyboard.isTouchedRight(x,y)){Parameters.i.keyboard++;}
				if(Parameters.i.keyboard>=HKeys.keyboardNumber)Parameters.i.keyboard=0;
				
				HKeys.setKeyboard(Parameters.i.keyboard);
			}
		}
		
		if(pause)back.draw(batch);
		
		tabGraphics.draw(batch);
		tabDisplay.draw(batch);
		tabAudio.draw(batch);
		tabKeyboard.draw(batch);
		
		if(Inputs.instance.mousedown)
		{
			if(pause && back.isTouched(x,y))
			{
				main = true;
				setting = false;
				cheat = false;
			}
			else if(tabGraphics.isTouched(x,y))
			{
				settingtab = TAB_GRAPHICS;
			}
			else if(tabDisplay.isTouched(x,y))
			{
				settingtab = TAB_DISPLAY;
			}
			else if(tabAudio.isTouched(x,y))
			{
				settingtab = TAB_AUDIO;
			}
			else if(tabKeyboard.isTouched(x,y))
			{
				settingtab = TAB_KEYBOARD;
			}
		}
	}
	
	public void drawCheats(SpriteBatch batch, float x, float y)
	{
		batch.setColor(1,0.1f,0.1f,1);
		
		if(Parameters.i.daylight<0.5f)time.value = "Night";
		else time.value = "Day";
		time.draw(batch);
		
		fullbright.value = Tools.booltoyes(Parameters.i.noShadow);
		fullbright.draw(batch);
		
		superman.value = Tools.booltoyes(Parameters.i.superman);
		superman.draw(batch);
		
		ultramagic.value = Tools.booltoyes(Parameters.i.cheatMagic);
		ultramagic.draw(batch);
		
		ultrarate.value = Tools.booltoyes(Parameters.i.ultrarate);
		ultrarate.draw(batch);
		
		gamespeed.value = "x"+String.valueOf((float)Math.round(Parameters.i.deltaMultiplier*100)/100);
		gamespeed.draw(batch);
		
		hitbox.value = Tools.booltoyes(Parameters.i.drawhitbox);
		hitbox.draw(batch);
		
		ultrarange.value = Tools.booltoyes(Parameters.i.ultrarange);
		ultrarange.draw(batch);
		
		godmode.value = Tools.booltoyes(Parameters.i.godmode);
		godmode.draw(batch);
		
		rain.value = Tools.booltoyes(Parameters.i.rain);
		rain.draw(batch);
		
		transform.value = Constants.transformNames[Parameters.i.currentTransform+1];
		if(GameScreen.player.transformingin || GameScreen.player.transformingout)transform.value = Constants.transformNames[GameScreen.player.nextTransform+1];
		transform.draw(batch);
		
		batch.setColor(Color.WHITE);
		
		back.draw(batch);
		
		if(Inputs.instance.mousedown)
		{
			if(back.isTouched(x,y))
			{
				main = true;
				setting = false;
				cheat = false;
			}
			else if(time.isTouchedRight(x,y)||time.isTouchedLeft(x,y))
			{
				if(Parameters.i.daylight<0.5f)Parameters.i.daylight = 1;
				else Parameters.i.daylight = 0.1f;
			}
			else if(fullbright.isTouchedRight(x,y)||fullbright.isTouchedLeft(x,y))Parameters.i.noShadow = !Parameters.i.noShadow;
			else if(hitbox.isTouchedRight(x,y)||hitbox.isTouchedLeft(x,y))Parameters.i.drawhitbox = !Parameters.i.drawhitbox;
			else if(superman.isTouchedRight(x,y)||superman.isTouchedLeft(x,y))Parameters.i.superman = !Parameters.i.superman;
			else if(ultramagic.isTouchedRight(x,y)||ultramagic.isTouchedLeft(x,y))Parameters.i.cheatMagic = !Parameters.i.cheatMagic;
			else if(ultrarate.isTouchedRight(x,y)||ultrarate.isTouchedLeft(x,y))Parameters.i.ultrarate = !Parameters.i.ultrarate;
			else if(ultrarange.isTouchedRight(x,y)||ultrarange.isTouchedLeft(x,y))Parameters.i.ultrarange = !Parameters.i.ultrarange;
			else if(godmode.isTouchedRight(x,y)||godmode.isTouchedLeft(x,y))Parameters.i.godmode = !Parameters.i.godmode;
			else if(gamespeed.isTouchedLeft(x,y) && Parameters.i.deltaMultiplier>0.01f)
			{
				if(Parameters.i.deltaMultiplier<=0.11f)Parameters.i.deltaMultiplier-=0.01f;
				else if(Parameters.i.deltaMultiplier<=2.5f)Parameters.i.deltaMultiplier-=0.1f;
				else Parameters.i.deltaMultiplier-=1;
			}
			else if(gamespeed.isTouchedRight(x,y) && Parameters.i.deltaMultiplier<4)
			{
				if(Parameters.i.deltaMultiplier<0.095f)Parameters.i.deltaMultiplier+=0.01f;
				else if(Parameters.i.deltaMultiplier<1.95f)Parameters.i.deltaMultiplier+=0.1f;
				else Parameters.i.deltaMultiplier+=1;
			}
			else if(rain.isTouchedLeft(x,y) || rain.isTouchedRight(x,y))Parameters.i.rain = !Parameters.i.rain;
			
			else if(transform.isTouchedLeft(x,y))
			{
				int tempcf = Parameters.i.currentTransform-1;
				if(tempcf<0)tempcf = GameScreen.player.transformList.size()-1;
				GameScreen.player.transform(tempcf);
			}
			else if(transform.isTouchedRight(x,y))
			{
				int tempcf = Parameters.i.currentTransform+1;
				if(tempcf>=GameScreen.player.transformList.size())tempcf = 0;
				GameScreen.player.transform(tempcf);
			}
			
		}
	}
	
	public void draw(SpriteBatch batch, float x, float y)
	{
		clear = false;
		batch.setColor(0,0,0,0.8f);
		batch.draw(TextureManager.instance.blank, -2000, -2000, 4000,4000);
		batch.setColor(Color.WHITE);
		
		if(main)
		{
			drawMenu(batch,x,y);
		}
		else if(setting)
		{
			drawSettings(batch,x,y,true);
		}
		else if(cheat && Main.enableCheats)
		{
			drawCheats(batch,x,y);
		}
		drawPauseButton(batch);
	}
	
	public void rebootMenu()
	{
		clear = true;
		setting = false;
		main = true;
		cheat = false;
		settingtab = 0;
	}
}
