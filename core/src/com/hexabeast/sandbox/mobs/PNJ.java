package com.hexabeast.sandbox.mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.sandbox.AllTools;
import com.hexabeast.sandbox.Constants;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.Main;
import com.hexabeast.sandbox.Map;
import com.hexabeast.sandbox.TextureManager;
import com.hexabeast.sandbox.Tools;

public class PNJ extends Mob{
	
	//TEXTURES PLACEMENT
	
	int currentBody = 0;
	int currentLegs = 0;
	int currentHead = 0;
	int currentEyes = 0;
	int currentHair = 0;
	int currentArm = 0;

	int bodyId = 0;
	int legsId = 1;
	int headId = 2;
	int eyesId = 3;
	int hairId = 4;
	int armId = 5;
	
	int legnumber = 12;
	
	public float larmRotation;
	public float rarmRotation;
	
	public float currentAngle;
	
	public float shoulderX = -31;
	public float shoulderY = -89;
	
	public float shoulderOriginX = 50;
	public float shoulderOriginY = 127;
	
	public float offsetArmLeft = 8;
	
	public float animationTime = 1;
	
	public float baseSpeedx = 250;
	public float baseSpeedy = 210;
	
	public float speedx = 250;
	public float speedy = 210;

	public float horspeed = 210;
	
	public boolean larmRotationDirection;
	
	public Vector2 handOffset = new Vector2(12,32);
	
	//VECTORS
	
	public Vector2 shoulderPos = new Vector2();
	public Vector2 middle = new Vector2();
	public Vector2 hookAnchorCoord = new Vector2();
	public Vector2 cannonCoord = new Vector2();
	public Vector2 cannonToVisorCoord = new Vector2();
	public Vector2 shoulderToVisorCoord = new Vector2();
	public Vector2 shoulderToCannonCoord = new Vector2();//TODO
	public Vector2 hookToVisorCoord = new Vector2();
	
	//PHYSICS
	
	float currentFriction;
	public boolean icy = false;
	public Vector2 tempVelocity = new Vector2();
	
	//FIGHT
	
	public float currentMeleeDamage;
	
	//CARACTERISTICS
	
	public boolean hasTool = false;
	public boolean bigTool = false;
	public boolean hasSword= false;
	
	public boolean mouseLeftPressed = false;
	public boolean mouseRightPressed = false;
	
	public int currentItem = 0;
	
	public boolean leftPress;
	public boolean rightPress;
	public boolean leftClick;
	public boolean rightClick;
	

	public PNJ()
	{
		tex.add(TextureManager.instance.PNJbody[currentBody]);
		tex.add(TextureManager.instance.PNJlegs[currentLegs][0]);
		tex.add(TextureManager.instance.PNJhead[currentHead]);
		tex.add(TextureManager.instance.PNJeyes[currentEyes][0]);
		tex.add(TextureManager.instance.PNJhairs[currentHair]);
		tex.add(TextureManager.instance.PNJarmTexture[currentArm]);
		
		legnumber = TextureManager.instance.PNJlegs[currentLegs].length;
		
		hitrect = new HitRect(0);
		hitrect.add(new Rectangle(16,2,20,58));
		hitrect.noturn = true;
		calculateSize(1);

		hitbox = new HitBox(new Rectangle(16,2,20,58), 0);
		hitbox.noturn = true;
		
		larmRotation = 25;
		rarmRotation = 25;
		currentAngle = 25;
	}
	
	
	
	@Override
	public void IA()
	{
		
	}
	
	
	
	
	@Override
	public void graphicDraw(SpriteBatch batch)
	{
		if(!isTurned)batch.draw(tex.get(armId), x+shoulderX+offsetArmLeft, y+shoulderY+1, shoulderOriginX, shoulderOriginY, tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1, larmRotation);
		else 		 batch.draw(tex.get(armId), x+width-shoulderX-offsetArmLeft, y+shoulderY+1, -shoulderOriginX, shoulderOriginY, -tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1,-larmRotation);
		
		for(int i = 0; i<5; i++)
		{
			if(!isTurned)batch.draw(tex.get(i), x, y, width, height);
			else batch.draw(tex.get(i), x+width+offx, y, -width, height);
		}
		
		if(currentItem != 0)
		{
			if(bigTool)
			{
				if(!isTurned)batch.draw(AllTools.instance.getRegion(AllTools.instance.getType(currentItem).weaponTexture), x+shoulderX, y+shoulderY, shoulderOriginX, shoulderOriginY, tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1, rarmRotation);
				else batch.draw(AllTools.instance.getRegion(AllTools.instance.getType(currentItem).weaponTexture), x+width-shoulderX, y+shoulderY, -shoulderOriginX, shoulderOriginY, -tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1, rarmRotation);
			}
			else
			{
				if(!isTurned)batch.draw(GameScreen.items.getTextureById(currentItem), shoulderPos.x-handOffset.x, shoulderPos.y-handOffset.y, handOffset.x, handOffset.y, GameScreen.items.getTextureById(currentItem).getRegionWidth()*1.5f, GameScreen.items.getTextureById(currentItem).getRegionHeight()*1.5f, 1, 1, rarmRotation);
				else 		 batch.draw(GameScreen.items.getTextureById(currentItem), shoulderPos.x+handOffset.x, shoulderPos.y-handOffset.y, -handOffset.x, handOffset.y, -GameScreen.items.getTextureById(currentItem).getRegionWidth()*1.5f, GameScreen.items.getTextureById(currentItem).getRegionHeight()*1.5f, 1, 1, rarmRotation);
			}
		}
		
		if(!isTurned)batch.draw(tex.get(armId), x+shoulderX, y+shoulderY, shoulderOriginX, shoulderOriginY, tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1, rarmRotation);
		else         batch.draw(tex.get(armId), x+width-shoulderX, y+shoulderY, -shoulderOriginX, shoulderOriginY, -tex.get(armId).getRegionWidth(), tex.get(armId).getRegionHeight(), 1, 1, rarmRotation);
	}
	
	
	
	
	
	
	@Override
	public void move()
	{
		float oldX = x;
		float oldY = y;
		
		y += vy * Main.delta;
		hitbox.update(x,y);
		
		canJump = false;
		if(hitbox.TestCollisions(true) || (hitbox.TestCollisions(false) && vy<=0))
		{
			y = oldY;
			vy = 0;
			
		}
		if(hitbox.TestCollisionsDown() || hitbox.TestCollisions(false))
		{
			canJump = true;
		}

		hitbox.update(x,y);
		
		boolean hiking = false;
		if(hitbox.TestCollisions(false))hiking = true; 

		x+=vx*Main.delta;
		
		
		hitbox.update(x,y);
		
		if(hitbox.TestCollisions(true) || (hitbox.TestCollisionsHigh() && !hiking && hitbox.TestCollisions(false)))
		{
			x = oldX;
			vx = 0;
		}
		else if(hitbox.TestCollisions(false))
		{
			float oldY2 = y;
			y+=Math.min(Math.abs(16-((y+hitbox.min)-(int)((y+hitbox.min)/16)*16)),Math.abs(vx)*1.6f*Main.delta);
			hitbox.update(x,y);	
			if(hitbox.TestCollisions(true))
			{
				y = oldY2;
				y+=Math.abs(vx)*0.2f*Main.delta;
				hitbox.update(x,y);	
				if(hitbox.TestCollisions(true))
				{
					y = oldY2;
				}
			}
		}
		hitbox.update(x,y);
		
		
		calculateShoulder();
		calculateVectors();
	}
	
	
	
	
	
	
	@Override
	public void premove()
	{
		if(Math.abs(vx)>speedx && canJump)
		{
			if(vx>0)
			{
				vx -= (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
				if(vx<0)vx=0;
			}
			else
			{
				vx += (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
				if(vx>0)vx=0;
			}
		}
		
		
		float oldlen = Math.abs(vx);
		float oldvx = vx;
			
		float multiplier = 1;
		if(Math.abs(vx)>baseSpeedx*2/3f)
		{
			multiplier = 1/20f;
		}
		if(Math.abs(vx)>baseSpeedx*4/3f)multiplier = 2/10f;
		vx+=ax*Main.delta*multiplier;
		if(Math.abs(vx)>oldlen && Math.abs(vx)>speedx)
		{
			vx = oldvx;
		}
		
		
		if(vy>-1000)vy -= Constants.gravity*Main.delta;


		tempVelocity.set(vx, vy);
		tempVelocity.clamp(0, 2000);
		vx = tempVelocity.x;
		vy = tempVelocity.y;
		
		float animFactor = vx/21.28f;
		if(isTurned)animFactor = -animFactor;
		animationTime += Main.delta*animFactor;
		if(animationTime>=legnumber-3)animationTime = 1;
		if(animationTime<1)animationTime = legnumber-3-0.1f;
	}
	
	
	
	
	
	
	@Override
	public void visual()
	{
		
		if(VisorPos.x>x+width/2)
		{
			if(isTurned)
			{
				currentAngle = -(currentAngle);
			}
			isTurned = false;
		}
		else
		{
			if(!isTurned)
			{
				currentAngle = -(currentAngle);
			}
			isTurned = true;
		}
		
		if(VisorPos.y>y+50)tex.set(eyesId, TextureManager.instance.PNJeyes[currentEyes][0]);
		else tex.set(eyesId, TextureManager.instance.PNJeyes[currentEyes][1]);
		
		calculateShoulder();
		animationTools();
		calculateAngle();
		
		calculateVectors();
		
		animationLegs();
		animationBackArm();
		
	}
	
	
	
	
	public void calculateShoulder()
	{
		if(!isTurned)shoulderPos.x = (x+shoulderX+shoulderOriginX);
		else shoulderPos.x = (x+width-shoulderX-shoulderOriginX);
		
		shoulderPos.y = (y+shoulderY+shoulderOriginY);
	}
	
	
	
	
	public void calculateAngle()
	{
		if(!isTurned)rarmRotation = currentAngle+80+AllTools.instance.getType(currentItem).angle;
		else 		rarmRotation = currentAngle-80-AllTools.instance.getType(currentItem).angle;
			
	}
	
	
	
	public void calculateVectors()
	{

		Vector2 launcherOffset = new Vector2(AllTools.instance.getType(currentItem).launcherDistance,0);
		
		launcherOffset.setAngle(rarmRotation+AllTools.instance.getType(currentItem).launcherAngle);
		
		
		middle.x = x+width/2;
		middle.y = y+height/2;
		
		hookAnchorCoord.x = middle.x;
		hookAnchorCoord.y = middle.y+6;
		
		cannonCoord.x = shoulderPos.x+launcherOffset.x;
		cannonCoord.y = shoulderPos.y+launcherOffset.y;
		
		cannonToVisorCoord.x = VisorPos.x-cannonCoord.x;
		cannonToVisorCoord.y = VisorPos.y-cannonCoord.y;
		cannonToVisorCoord.setLength(400);
		
		shoulderToVisorCoord.x = VisorPos.x-shoulderPos.x;
		shoulderToVisorCoord.y = VisorPos.y-shoulderPos.y;
		shoulderToVisorCoord.setLength(100);
		
		shoulderToCannonCoord.x = cannonCoord.x-shoulderPos.x;
		shoulderToCannonCoord.y = cannonCoord.y-shoulderPos.y;
		shoulderToCannonCoord.setLength(100);
		
		hookToVisorCoord.x = VisorPos.x-hookAnchorCoord.x;
		hookToVisorCoord.y = VisorPos.y-hookAnchorCoord.y;
		hookToVisorCoord.setLength(400);
	}
	
	
	
	public void animationTools()
	{
		hasTool = false;
		if(currentItem>999)hasTool = true;
		
		bigTool = false;
		hasSword = false;
		
		if((hasTool && AllTools.instance.getType(currentItem).weaponTexture!=0))bigTool = true;
		
		if(hasTool && AllTools.instance.getType(currentItem).type == AllTools.instance.Sword)hasSword = true;
		
		float absAngle;
		
		if(!isTurned)
		{
			absAngle = new Vector2(VisorPos.x - shoulderPos.x, VisorPos.y - shoulderPos.y).angle();
		}
		else
		{
			absAngle = new Vector2(VisorPos.x - shoulderPos.x, VisorPos.y - shoulderPos.y).angle()+180;
		}
		
		if(currentAngle==0)currentAngle = absAngle;
		else if(hasSword)
		{
			if(!leftPress && currentMeleeDamage<=AllTools.instance.getType(currentItem).damage/2)currentAngle = Tools.fLerpAngle(currentAngle, absAngle, AllTools.instance.getType(currentItem).uptime);
		}
		else
		{
			currentAngle = Tools.fLerpAngle(currentAngle, absAngle, AllTools.instance.getType(currentItem).uptime);
		}
	}
	
	
	
	
	
	public void animationBackArm()
	{
		if(vy<-150)
		{
			if(Math.abs(larmRotation-150)>0.01f)larmRotation = larmRotation+(150-larmRotation)*Main.delta*20*Math.abs(vy)/500;
		}
		else if(vy>0.1f /*&& !grappleFlying*/)
		{
			if(Math.abs(larmRotation-20)>0.5f)
			larmRotation = larmRotation+(20-larmRotation)*Main.delta*20;
		}
		else if(Math.abs(vx)>0.1f && canJump)
		{
			if(larmRotationDirection)
			{
				larmRotation += Math.abs(vx)*1.2f*Main.delta; 
				if(larmRotation>75)
					larmRotationDirection = false;
			}
			else
			{
				larmRotation -= Math.abs(vx)*1.1f*Main.delta; 
				if(larmRotation<-15)
					larmRotationDirection = true;
			}
			
			if(larmRotation>75)larmRotation -= Math.abs(vx)*1.1f*Main.delta*5; 
			if(larmRotation<-15)larmRotation += Math.abs(vx)*1.3f*Main.delta*5; 
		}
		else/* if(!grappleFlying)*/
		{
			if(Math.abs(larmRotation-27)>0.1f)larmRotation = larmRotation+(27-larmRotation)*Main.delta*20;
		}
		/*else
		{
			if(Math.abs(larmRotation-70)>0.1f)larmRotation = larmRotation+(70-larmRotation)*Main.delta*20;
		}*/
	}
	
	
	
	
	
	
	public void animationLegs()
	{
		if(!canJump)
		{
			if(vy<-speedy*0.1f)
			{
				tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][legnumber-1]);
			}
			else if(vy>speedy*0.1f)
			{
				tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][legnumber-3]);
			}
			else
			{
				tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][legnumber-2]);
			}
		}
		else
		{
			if(vx == 0)
			{
				tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][0]);
			}
			else
			{
				if(icy)
				{
					tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][0]);
				}
				else
				{
					tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][(int)animationTime]);
				}
			}
		}
	}
	
	
	
	
	
	
	@Override
	public void goJump()
	{
		if(canJump)
		{
			vy = horspeed*2.4f;
			canJump = false;
		}
	}
	
	@Override
	public void goRight()
	{
		ax = 3000;
		icy = false;
		if(vx<0)
		{
			vx += (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
		}
	}
	
	@Override
	public void goLeft()
	{
		icy = false;
		ax = -3000;
		if(vx>0)
		{
			vx -= (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
		}
	}
	
	@Override
	public void goStandX()
	{
		ax = 0;
		if (vx!=0)
		{
			
			if(!canJump)currentFriction/=6;
			if(vx>0)
			{
				vx -= (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
				if(vx<0)vx=0;
			}
			else
			{
				vx += (currentFriction*Main.delta*1000)+(Math.abs(vx)*Main.delta*10);
				if(vx>0)vx=0;
			}
			if(currentFriction<1)
			{
				animationTime = 1;
				icy = true;
			}
			
			if(Math.abs(vx)<0.5f)vx=0;
		}
		else
		{
			animationTime = 1;
		}
	}
	
	@Override
	public void goClickLeftInstant()
	{
		leftClick = true;
	}
	
	@Override
	public void goClickRightInstant()
	{
		rightClick = true;
	}
	
	@Override
	public void goClickLeftPressed()
	{
		leftPress = true;
	}
	
	@Override
	public void goClickRightPressed()
	{
		rightPress = true;
	}
	
	@Override
	public void setItemId(int item)
	{
		currentItem = item;
	}
	
	@Override
	public void preinput()
	{
		currentFriction = Map.instance.mainLayer.getBloc(Tools.floor((x+width/2)/Map.instance.mainLayer.getTileWidth()), Tools.floor((y)/Map.instance.mainLayer.getTileHeight())).friction; 
		leftPress = false;
		rightPress = false;
	}
	
}
