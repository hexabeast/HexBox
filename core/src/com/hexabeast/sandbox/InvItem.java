package com.hexabeast.sandbox;


import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InvItem {
public int id;
public Sprite image;
public int number;


	public InvItem(int idd) {
		id = idd;
		image = new Sprite(GameScreen.items.getTextureById(id));
		image.setSize(32, 32);
	}
	
	public void refresh(){
		image.setRegion(GameScreen.items.getTextureById(id));
		image.setSize(32, 32);
	}
	
	public void drawAll(SpriteBatch batch)
	{
		image.draw(batch);
		if(number>1)
		{
			FontManager.instance.font1.draw(batch, String.valueOf(number), image.getX()+image.getWidth()*image.getScaleX()*2f/3-4-
					/*FontManager.instance.font1.getBounds(String.valueOf(number))*/
					new GlyphLayout(FontManager.instance.font1,String.valueOf(number) ).width/2, image.getY()+6+
					new GlyphLayout(FontManager.instance.font1,String.valueOf(number) ).height);
		}
	}
	
	public void setScaleAll(float scale)
	{
		image.setScale(scale);
	}
	
	public void setPositionAll(float x, float y)
	{
		image.setPosition(x, y);
	}
}
