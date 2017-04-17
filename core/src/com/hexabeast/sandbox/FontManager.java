package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontManager {
	public static FontManager instance;
	public BitmapFont font;
	public BitmapFont fontNameChar;
	public BitmapFont font1;
	public BitmapFont fontsmall;
	public BitmapFont fontSettings;
	
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	FreeTypeFontGenerator generator2;
	
	FontManager()
	{
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arma.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.kerning = true;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		parameter.size = 26;
		font = generator.generateFont(parameter);
		generator.dispose();
		
		generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ProFontWindows.ttf"));
		
		parameter.size = 60;
		fontNameChar = generator2.generateFont(parameter);
		fontNameChar.getData().setScale(0.5f);
		
		parameter.size = 46;
		fontSettings = generator2.generateFont(parameter);
		
		fontsmall = generator2.generateFont(parameter);
		fontsmall.getData().setScale(0.5f);
		
		generator2.dispose();
		
		//font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		/*
		font = new BitmapFont(Gdx.files.internal("fonts/pixelFont.fnt"),Gdx.files.internal("fonts/pixelFont_0.png"), false);
		font.setScale(1.4f);
		font.setColor(1f, 1f,1f, 1);
		font.setUseIntegerPositions(true);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		*/
		/*fontSettings = new BitmapFont(Gdx.files.internal("fonts/pixelFont.fnt"),Gdx.files.internal("fonts/pixelFont_0.png"), false);
		fontSettings.setScale(2);
		fontSettings.setColor(1f, 1f,1f, 1);
		fontSettings.setUseIntegerPositions(true);
		fontSettings.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		fontsmall = new BitmapFont(Gdx.files.internal("fonts/pixelFont.fnt"),Gdx.files.internal("fonts/pixelFont_0.png"), false);
		fontsmall.setScale(1);
		fontsmall.setColor(1f, 1f,1f, 1);
		fontsmall.setUseIntegerPositions(true);
		fontsmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);*/
		
		font1 = new BitmapFont(Gdx.files.internal("fonts/pixelFon3.fnt"),Gdx.files.internal("fonts/pixelFon3_0.png"), false);
		initializeFont1();
		font1.setUseIntegerPositions(true);
		font1.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void initializeFont1()
	{
		font1.getData().setScale(0.6f);
		font1.setColor(1f, 1f,1f, 1);
	}

}
