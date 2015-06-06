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
	
	PauseMenu()
	{
		pb = new Sprite(TextureManager.instance.pauseButton);
		pb.setOrigin(0, 0);
		pb.setScale(4);
		
		resume = new PauseMenuButton(TextureManager.instance.pauseresume,0,200);
		quit = new PauseMenuButton(TextureManager.instance.pauseexit,0,-400);
		settings = new PauseMenuButton(TextureManager.instance.pausesettings,0,0);
		back = new PauseMenuButton(TextureManager.instance.pauseback,-1000,-560);
		cheats = new PauseMenuButton(TextureManager.instance.pausecheat, 0,-200);
		
		resolution = new PauseSetting(0,500,"Resolution :","Changes the resolution of the window (16/9 ratio only)");
		fullscreen = new PauseSetting(0,400,"FullScreen :","Enables / disables fullscreen mode");
		shadowQuality = new PauseSetting(0,300,"Shadows Quality :","Shader is often faster than Medium Quality for a better rendering quality");
		lightSpeed = new PauseSetting(0,200,"Light Speed :","Higher number = faster lights but worse performances");
		lightColor = new PauseSetting(0,100,"Colored Lights :","Colored lights require more CPU but looks better than white lights");
		details = new PauseSetting(0,0,"Details :","Ambient occlusion, better grass and better transitions between blocks");
		vsync = new PauseSetting(0,-100,"VSync :","Enables/Disables VSync");
		HQMagic = new PauseSetting(0,-200,"Spells quality :","Changes the quality of magic things");
		background = new PauseSetting(0,-300,"Background :","Enables/Disables background");
		
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
	
	public void draw(SpriteBatch batch)
	{
		clear = false;
		batch.setColor(0,0,0,0.8f);
		batch.draw(TextureManager.instance.blank, -2000, -2000, 4000,4000);
		batch.setColor(Color.WHITE);
		
		if(main)
		{
			resume.draw(batch);
			quit.draw(batch);
			settings.draw(batch);
			if(Parameters.i.cheat)cheats.draw(batch);
			
			if(Inputs.instance.mouseup)
			{
				if(resume.isTouched())Main.pause = false;
				if(quit.isTouched())Gdx.app.exit();
				if(settings.isTouched())
				{
					main = false;
					setting = true;
					cheat = false;
				}
				if(Parameters.i.cheat && cheats.isTouched())
				{
					main = false;
					setting = false;
					cheat = true;
				}
			}
		}
		else if(setting)
		{
			String resx = String.valueOf(Constants.resolutions[Parameters.i.resolution].x);
			String resy = String.valueOf(Constants.resolutions[Parameters.i.resolution].y);
			
			resx = String.valueOf(Gdx.graphics.getWidth());
			resy = String.valueOf(Gdx.graphics.getHeight());
			
			resolution.value = resx+"x"+resy;
			resolution.draw(batch);
			
			fullscreen.value = Tools.booltoyes(Parameters.i.fullscreen);
			fullscreen.draw(batch);
			
			shadowQuality.value = Constants.qualities[Parameters.i.HQ];
			shadowQuality.draw(batch);
			
			lightSpeed.value = String.valueOf(Parameters.i.lightSpeed);
			lightSpeed.draw(batch);
			
			lightColor.value = Tools.booltoyes(Parameters.i.RGB);
			lightColor.draw(batch);
			
			details.value = Tools.booltoyes(Parameters.i.details);
			details.draw(batch);
			
			vsync.value = Tools.booltoyes(Parameters.i.vsync);
			vsync.draw(batch);
			
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
			
			back.draw(batch);
			
			if(Inputs.instance.mouseup)
			{
				if(back.isTouched())
				{
					main = true;
					setting = false;
					cheat = false;
				}
				
				if(resolution.isTouchedRight())
				{
					for(int i = 0; i<Constants.resolutions.length; i++)
					{
						if(Constants.resolutions[i].x > Gdx.graphics.getWidth()+0.1f)
						{
							Parameters.i.resolution = i;
							break;
						}
						else if(i == Constants.resolutions.length-1)Parameters.i.resolution = i;
					}
					
					Main.updateResolution();
				}
				if(resolution.isTouchedLeft())if(Parameters.i.resolution>0)
				{
					for(int i = Constants.resolutions.length-1; i>=0; i--)
					{
						if(Constants.resolutions[i].x < Gdx.graphics.getWidth()-0.1f)
						{
							Parameters.i.resolution = i;
							break;
						}
						else if(i == 0)Parameters.i.resolution = i;
					}
					Main.updateResolution();
				}
				if(fullscreen.isTouchedRight()||fullscreen.isTouchedLeft())
				{
					Parameters.i.fullscreen = !Parameters.i.fullscreen;
					Main.updateResolution();
				}
				
				if(HQMagic.isTouchedRight()||HQMagic.isTouchedLeft())Parameters.i.goodmagic = !Parameters.i.goodmagic;
				
				if(vsync.isTouchedRight()||vsync.isTouchedLeft())
				{
					Parameters.i.vsync = !Parameters.i.vsync;
					Gdx.graphics.setVSync(Parameters.i.vsync);
				}
				
				if(background.isTouchedRight()||background.isTouchedLeft())
				{
					Parameters.i.background = !Parameters.i.background;
				}
				
				
				if(shadowQuality.isTouchedLeft())if(Parameters.i.HQ>1)Parameters.i.HQ--;
				if(shadowQuality.isTouchedRight())if(Parameters.i.HQ<Constants.qualities.length)Parameters.i.HQ++;
				
				if(lightSpeed.isTouchedLeft())if(Parameters.i.lightSpeed>=60)
					{
						Parameters.i.lightSpeed-=30;
						if(Parameters.i.lightSpeed%30!=0)Parameters.i.lightSpeed=120;
					}
				if(lightSpeed.isTouchedRight())if(Parameters.i.lightSpeed<=270)
					{
						Parameters.i.lightSpeed+=30;
						if(Parameters.i.lightSpeed%30!=0)Parameters.i.lightSpeed=120;
					}
				if(lightColor.isTouchedRight()||lightColor.isTouchedLeft())Parameters.i.RGB = !Parameters.i.RGB;
				if(details.isTouchedRight()||details.isTouchedLeft())Parameters.i.details = !Parameters.i.details;
			}
		}
		else if(cheat)
		{
			if(Parameters.i.cheat)
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
				
				axerange.value = Tools.booltoyes(Parameters.i.wayaxe);
				axerange.draw(batch);
				
				transform.value = Constants.transformNames[Parameters.i.currentTransform+1];
				if(GameScreen.player.transformingin || GameScreen.player.transformingout)transform.value = Constants.transformNames[GameScreen.player.nextTransform+1];
				transform.draw(batch);
				
				batch.setColor(Color.WHITE);
				
				back.draw(batch);
				
				if(Inputs.instance.mouseup)
				{
					if(back.isTouched())
					{
						main = true;
						setting = false;
						cheat = false;
					}
					if(time.isTouchedRight()||time.isTouchedLeft())
					{
						if(Parameters.i.daylight<0.5f)Parameters.i.daylight = 1;
						else Parameters.i.daylight = 0.1f;
					}
					if(fullbright.isTouchedRight()||fullbright.isTouchedLeft())Parameters.i.noShadow = !Parameters.i.noShadow;
					if(hitbox.isTouchedRight()||hitbox.isTouchedLeft())Parameters.i.drawhitbox = !Parameters.i.drawhitbox;
					if(superman.isTouchedRight()||superman.isTouchedLeft())Parameters.i.superman = !Parameters.i.superman;
					if(ultramagic.isTouchedRight()||ultramagic.isTouchedLeft())Parameters.i.cheatMagic = !Parameters.i.cheatMagic;
					if(ultrarate.isTouchedRight()||ultrarate.isTouchedLeft())Parameters.i.ultrarate = !Parameters.i.ultrarate;
					if(ultrarange.isTouchedRight()||ultrarange.isTouchedLeft())Parameters.i.ultrarange = !Parameters.i.ultrarange;
					if(godmode.isTouchedRight()||godmode.isTouchedLeft())Parameters.i.godmode = !Parameters.i.godmode;
					if(gamespeed.isTouchedLeft() && Parameters.i.deltaMultiplier>0.01f)
					{
						if(Parameters.i.deltaMultiplier<=0.11f)Parameters.i.deltaMultiplier-=0.01f;
						else if(Parameters.i.deltaMultiplier<=2.5f)Parameters.i.deltaMultiplier-=0.1f;
						else Parameters.i.deltaMultiplier-=1;
					}
					if(gamespeed.isTouchedRight() && Parameters.i.deltaMultiplier<4)
					{
						if(Parameters.i.deltaMultiplier<0.095f)Parameters.i.deltaMultiplier+=0.01f;
						else if(Parameters.i.deltaMultiplier<1.95f)Parameters.i.deltaMultiplier+=0.1f;
						else Parameters.i.deltaMultiplier+=1;
					}
					if(rain.isTouchedLeft() || rain.isTouchedRight())Parameters.i.rain = !Parameters.i.rain;
					if(axerange.isTouchedLeft() || axerange.isTouchedRight())Parameters.i.wayaxe = !Parameters.i.wayaxe;
					
					if(transform.isTouchedLeft())
					{
						int tempcf = Parameters.i.currentTransform-1;
						if(tempcf<-1)tempcf = GameScreen.player.transformList.size()-1;
						GameScreen.player.transform(tempcf);
					}
					else if(transform.isTouchedRight())
					{
						int tempcf = Parameters.i.currentTransform+1;
						if(tempcf>=GameScreen.player.transformList.size())tempcf = -1;
						GameScreen.player.transform(tempcf);
					}
					
				}
			}
		}
		drawPauseButton(batch);
	}
	
	public void rebootMenu()
	{
		clear = true;
		setting = false;
		main = true;
	}
}
