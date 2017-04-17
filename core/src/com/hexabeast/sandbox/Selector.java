package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Selector {

    public int dirt = 1;
    public int unbreak = 2;
    public int cobble = 3;
    
    private float scalePanel = 4f;
    private float scale = 2.6f;
    private float espacement = 3f;
    public float currentPosX = 0;
    public float currentPosY = 0;
    public float yOffset = 0;
    
    public float animationTime = 0;
    public int selected = 0;
    public InvItem[] Selectors;
    public Sprite[] backSelectorSprites;
    public Sprite selectorBackGround;
    public int[] Ids;
    
    TextureRegion torch[] = new TextureRegion[3];
    Animation<TextureRegion> torchAnim;
    
    public Sprite torchSprite;
    
	public Selector() {

		Selectors = new InvItem[10];
		backSelectorSprites = new Sprite[10];
		
		selectorBackGround = new Sprite(TextureManager.instance.selecbacktext);
		selectorBackGround.setOrigin(0, 0);
		selectorBackGround.setScale(4.5f);
		
		torch = new TextureRegion(TextureManager.instance.torchText).split(TextureManager.instance.torchText.getRegionWidth()/6, TextureManager.instance.torchText.getRegionHeight())[0];
		
		torchAnim = new Animation<TextureRegion>(0.12f, torch[0],torch[1],torch[2],torch[3],torch[4],torch[5]);
		torchAnim.setPlayMode(PlayMode.LOOP);
		torchSprite = new Sprite(torch[1]);
		torchSprite.setOrigin(0, 0);
		torchSprite.setScale(4.5f);
		
		Ids = new int[10];
		
		for(int i = 0; i<Selectors.length;i++)
		{
			Selectors[i] = new InvItem(0);
			Ids[i] = 0;
			Selectors[i].image.setOrigin(0, 0);
			Selectors[i].setScaleAll(scale);
		}
		
		for(int i = 0; i<backSelectorSprites.length;i++)
		{
			backSelectorSprites[i] = new Sprite(TextureManager.instance.selectDefault);
			backSelectorSprites[i].setOrigin(0, 0);
			backSelectorSprites[i].setScale(scale);
		}
	}
	
	public void setSelector(int number, InvItem item)
	{
		Selectors[number].id = item.id;
		Selectors[number].number = item.number;
		Selectors[number].refresh();
		Ids[number] = item.id;
	}
	
	public int getSelectorId(int number)
	{
		return Ids[number];
	}
	
	public void setSelected(int number)
	{
		selected = number;
		refresh();
	}
	
	public void refresh()
	{
		for(int i = 0; i<Selectors.length;i++)
		{
			Selectors[i].setScaleAll(scale);
			if(i == selected)Selectors[i].setScaleAll(scale*1.1f);
		}
		for(int i = 0; i<backSelectorSprites.length;i++)
		{
			backSelectorSprites[i].setScale(scalePanel);
			if(i == selected)backSelectorSprites[i].setScale(scalePanel*1.1f);
		}
	}
	
	public void refreshSelector()
	{
		for(int i = 0; i<Selectors.length;i++)
		{
			setSelector(i,GameScreen.inventory.invItemsArray[i][0]);
		}
		GameScreen.player.refreshSelect();
	}
	
	public void setPosition(float x, float y,float delta)
	{
		y+=yOffset;
		y+=-100;
		currentPosX = x;
		currentPosY = y;
		animationTime+=delta;
		torchSprite.setRegion(torchAnim.getKeyFrame(animationTime));
		selectorBackGround.setPosition(x-selectorBackGround.getWidth()/2*selectorBackGround.getScaleX(),y+10);
		torchSprite.setPosition(x-torchSprite.getWidth()/2*torchSprite.getScaleX(),y);
		for(int i = 0; i<Selectors.length;i++)
		{
			
			int offsets = -20;
			
			if(i>4)offsets = 20;
			
			Selectors[i].setPositionAll((offsets+x+i*Selectors[i].image.getWidth()*espacement)-5*Selectors[i].image.getWidth()*espacement, y);
			
			backSelectorSprites[i].setPosition((offsets+x+i*Selectors[i].image.getWidth()*espacement)-5*Selectors[i].image.getWidth()*espacement, y);
		}
	}
	
	public void toggleOffset()
	{
		if(yOffset>-100)
		{
			yOffset = -0;
		}
		else
		{
			yOffset = 0;
		}
	}

}
