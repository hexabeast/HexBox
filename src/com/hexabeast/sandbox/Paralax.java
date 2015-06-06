package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Paralax {

	TextureRegion layer1;
	TextureRegion layer2;
	TextureRegion layer3;
	TextureRegion layer4;
	TextureRegion layer5;
	TextureRegion back;
	TextureRegion backn;
	float baseX;
	float baseX1;
	float baseX2;
	float baseX3;
	float baseX4;
	float baseX5;
	float cloudDecay;
	float x;
	float y;
	float w;
	float h;
	
	float DECAY = 1118*16;
	
	float num1;
	float num2;
	float num3;
	float num4;
	float num5;
	
	public Paralax() 
	{
		back = new TextureRegion(TextureManager.instance.back);
		backn = new TextureRegion(TextureManager.instance.backn);
		layer1 = new TextureRegion(TextureManager.instance.layer1);
		layer2 = new TextureRegion(TextureManager.instance.layer2);
		layer3 = new TextureRegion(TextureManager.instance.layer3);
		layer4 = new TextureRegion(TextureManager.instance.layer4);
		layer5 = new TextureRegion(TextureManager.instance.layer5);
		
		baseX = 0;
		cloudDecay = 0;
		
		num5 = 0.27f;		
		float r = 2;
		
		while(!(r>-1 && r<1))
		{
			num5+=0.00001f;
			r = (num5*Map.instance.width*16)%(layer5.getRegionWidth()*2);
		}
		
		num4 = 0.14f;		
		r = 2;
		
		while(!(r>-1 && r<1))
		{
			num4+=0.00001f;
			r = (num4*Map.instance.width*16)%(layer4.getRegionWidth()*2);
		}
		
		num3 = 0.47f;		
		r = 2;
		
		while(!(r>-1 && r<1))
		{
			num3+=0.00001f;
			r = (num3*Map.instance.width*16)%(layer3.getRegionWidth()*2);
		}
		
		num2 = 0.57f;		
		r = 2;
		
		while(!(r>-1 && r<1))
		{
			num2+=0.00001f;
			r = (num2*Map.instance.width*16)%(layer2.getRegionWidth()*2);
		}
		
		num1 = 0.67f;		
		r = 2;
		
		while(!(r>-1 && r<1))
		{
			num1+=0.00001f;
			r = (num1*Map.instance.width*16)%(layer1.getRegionWidth()*2);
		}
	}
	
	public void DrawBack(SpriteBatch batch)
	{
		if(GameScreen.isVillage() && !Main.mobile)
		{
			DECAY = (Map.instance.height-362)*16;
		}
		else
		{
			DECAY = (Map.instance.limit-2)*16;
		}
		x = GameScreen.camera.position.x;
		y = GameScreen.camera.position.y;
		w = GameScreen.camera.viewportWidth;
		h = GameScreen.camera.viewportHeight;
		cloudDecay-=30*Main.delta;
		
		//BACK
		if(Parameters.i.daylight<1)batch.draw(backn, x-2, y, back.getRegionWidth(), back.getRegionHeight(), back.getRegionWidth()+4, back.getRegionHeight(), 2, 2,0);
		batch.setColor(1, 1, 1, Math.max(Parameters.i.daylight*1.15f-0.15f,0));
		if(Parameters.i.daylight>0.11f)batch.draw(back, x-2, y, back.getRegionWidth(), back.getRegionHeight(), back.getRegionWidth()+4, back.getRegionHeight(), 2, 2,0);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void DrawAll(SpriteBatch batch)
	{
		
		
		//LAYER 4
		float xOffset4 = (x-baseX-cloudDecay)*num4;
		float yOffset4 = 0.5f;
		float yOffsetHard4 = 0;
								
		while(baseX4-xOffset4<layer4.getRegionWidth()*2)
		{
			baseX4+=layer4.getRegionWidth()*2;
		}
						
		while(baseX4-xOffset4>=-layer4.getRegionWidth()*2)
		{
			baseX4-=layer4.getRegionWidth()*2;
		}
								
		batch.draw(layer4, x-xOffset4+baseX4, y+layer4.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset4, layer4.getRegionWidth(), layer4.getRegionHeight(), layer4.getRegionWidth(), layer4.getRegionHeight(), 2, 2,0);
		batch.draw(layer4, x+layer4.getRegionWidth()*2-xOffset4+baseX4, y+yOffsetHard4+layer4.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset4, layer4.getRegionWidth(), layer4.getRegionHeight(), layer4.getRegionWidth(), layer4.getRegionHeight(), 2, 2,0);
		batch.draw(layer4, x+layer4.getRegionWidth()*4-xOffset4+baseX4, y+yOffsetHard4+layer4.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset4, layer4.getRegionWidth(), layer4.getRegionHeight(), layer4.getRegionWidth(), layer4.getRegionHeight(), 2, 2,0);
		
		//LAYER 5
		
		
		
		float xOffset5 = (x-baseX)*num5;
		float yOffset5 = 0.7f;
		float yOffsetHard5 = -80;
						
		while(baseX5-xOffset5<layer5.getRegionWidth()*2)
		{
			baseX5+=layer5.getRegionWidth()*2;
		}
								
		while(baseX5-xOffset5>=-layer5.getRegionWidth()*2)
		{
			baseX5-=layer5.getRegionWidth()*2;
		}
								
		batch.draw(layer5, x-xOffset5+baseX5, y+layer5.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset5, layer5.getRegionWidth(), layer5.getRegionHeight(), layer5.getRegionWidth(), layer5.getRegionHeight(), 2, 2,0);
		batch.draw(layer5, x+layer5.getRegionWidth()*2-xOffset5+baseX5, y+yOffsetHard5+layer5.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset5, layer5.getRegionWidth(), layer5.getRegionHeight(), layer5.getRegionWidth(), layer5.getRegionHeight(), 2, 2,0);
		batch.draw(layer5, x+layer5.getRegionWidth()*4-xOffset5+baseX5, y+yOffsetHard5+layer5.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset5, layer5.getRegionWidth(), layer5.getRegionHeight(), layer5.getRegionWidth(), layer5.getRegionHeight(), 2, 2,0);
		
		
		
		
		//LAYER 3
		float xOffset3 = (x-baseX)*num3;
		float yOffset3 = 0.8f;
		float yOffsetHard3 = -50;
				
		while(baseX3-xOffset3<layer3.getRegionWidth()*2)
		{
			baseX3+=layer3.getRegionWidth()*2;
		}
				
		while(baseX3-xOffset3>=-layer3.getRegionWidth()*2)
		{
			baseX3-=layer3.getRegionWidth()*2;
		}
				
		batch.draw(layer3, x-xOffset3+baseX3, y+layer3.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset3, layer3.getRegionWidth(), layer3.getRegionHeight(), layer3.getRegionWidth(), layer3.getRegionHeight(), 2, 2,0);
		batch.draw(layer3, x+layer3.getRegionWidth()*2-xOffset3+baseX3, y+yOffsetHard3+layer3.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset3, layer3.getRegionWidth(), layer3.getRegionHeight(), layer3.getRegionWidth(), layer3.getRegionHeight(), 2, 2,0);
		batch.draw(layer3, x+layer3.getRegionWidth()*4-xOffset3+baseX3, y+yOffsetHard3+layer3.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset3, layer3.getRegionWidth(), layer3.getRegionHeight(), layer3.getRegionWidth(), layer3.getRegionHeight(), 2, 2,0);
		
		
		//LAYER 2
		float xOffset2 = (x-baseX-cloudDecay)*num2;
		float yOffset2 = 0.88f;
		float yOffsetHard2 = 30;
		
		while(baseX2-xOffset2<layer2.getRegionWidth()*2)
		{
			baseX2+=layer2.getRegionWidth()*2;
		}
		
		while(baseX2-xOffset2>=-layer2.getRegionWidth()*2)
		{
			baseX2-=layer2.getRegionWidth()*2;
		}
		
		batch.draw(layer2, x-xOffset2+baseX2, y+layer2.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset2, layer2.getRegionWidth(), layer2.getRegionHeight(), layer2.getRegionWidth(), layer2.getRegionHeight(), 2, 2,0);
		batch.draw(layer2, x+layer2.getRegionWidth()*2-xOffset2+baseX2, y+yOffsetHard2+layer2.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset2, layer2.getRegionWidth(), layer2.getRegionHeight(), layer2.getRegionWidth(), layer2.getRegionHeight(), 2, 2,0);
		batch.draw(layer2, x+layer2.getRegionWidth()*4-xOffset2+baseX2, y+yOffsetHard2+layer2.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset2, layer2.getRegionWidth(), layer2.getRegionHeight(), layer2.getRegionWidth(), layer2.getRegionHeight(), 2, 2,0);
		
		//LAYER 1
		float xOffset1 = (x-baseX)*num1;
		//float yOffset1 = 0.9f;
		//float yOffsetHard1 = -55;
				
		while(baseX1-xOffset1<layer1.getRegionWidth()*2)
		{
			baseX1+=layer1.getRegionWidth()*2;
		}
				
		while(baseX1-xOffset1>=-layer1.getRegionWidth()*2)
		{
			baseX1-=layer1.getRegionWidth()*2;
		}
		/*if(GamePlay.player.isVillage && !GamePlay.player.isVillage)
		{
			batch.draw(layer1, x-xOffset1+baseX1, y+layer1.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset1, layer1.getRegionWidth(), layer1.getRegionHeight(), layer1.getRegionWidth(), layer1.getRegionHeight(), 2, 2,0);
			batch.draw(layer1, x+layer1.getRegionWidth()*2-xOffset1+baseX1, y+yOffsetHard1+layer1.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset1, layer1.getRegionWidth(), layer1.getRegionHeight(), layer1.getRegionWidth(), layer1.getRegionHeight(), 2, 2,0);
			batch.draw(layer1, x+layer1.getRegionWidth()*4-xOffset1+baseX1, y+yOffsetHard1+layer1.getRegionHeight()-h/2     +h/2-(y-DECAY)*yOffset1, layer1.getRegionWidth(), layer1.getRegionHeight(), layer1.getRegionWidth(), layer1.getRegionHeight(), 2, 2,0);
		}*/
		
		
		
	}
}
