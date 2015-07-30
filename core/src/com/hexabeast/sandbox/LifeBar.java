package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LifeBar {
	
	public Sprite LB;
	public Sprite backLB;
	public Sprite redLB;
	public Sprite greenLB;
	
	public float scale = 4;
	
	public LifeBar() {
		
		LB = new Sprite(TextureManager.instance.LBText);
		LB.setScale(scale);
		
		backLB = new Sprite(TextureManager.instance.backLBText);
		backLB.setScale(scale);
		
		redLB = new Sprite(TextureManager.instance.redLBText);
		redLB.setScale(scale);
		
		greenLB = new Sprite(TextureManager.instance.greenLBText);
		greenLB.setScale(scale);
	}
	
	public void DrawAll(float x, float y, SpriteBatch batch)
	{
		LB.setPosition(x, y);
		backLB.setPosition(x, y);
		greenLB.setPosition(LB.getX()-(GameScreen.player.PNJ.maxHealth-GameScreen.player.PNJ.health)/GameScreen.player.PNJ.maxHealth*greenLB.getWidth()*scale+12, y);
		redLB.setPosition(floatLerp(redLB.getX(), greenLB.getX()),y);
		
		backLB.draw(batch);
		redLB.draw(batch);
		greenLB.draw(batch);
		LB.draw(batch);
	}
	
	private float floatLerp(float fFrom, float fTo)
	{
		if(Math.abs(fFrom-fTo)>greenLB.getWidth()*scale*2/3)return fTo;
		return fFrom+((fTo-fFrom)/10)*Main.delta*60;
	}

}
