package com.hexabeast.sandbox.mobs;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.sandbox.AllEntities;
import com.hexabeast.sandbox.AllTools;
import com.hexabeast.sandbox.Constants;
import com.hexabeast.sandbox.GameScreen;
import com.hexabeast.sandbox.Grapple;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.Main;
import com.hexabeast.sandbox.Map;
import com.hexabeast.sandbox.Parameters;
import com.hexabeast.sandbox.TextureManager;
import com.hexabeast.sandbox.ToolType;
import com.hexabeast.sandbox.Tools;

public class PNJ extends Mob{
	
	//TEXTURES PLACEMENT
	
	int currentBody = 0;
	int currentLegs = 0;
	int currentHead = 0;
	int currentEyes = 0;
	int currentHair = 0;
	int currentArm = 0;
	
	ToolType currentArmor = AllTools.instance.defaultType;
	ToolType currentLeggins = AllTools.instance.defaultType;
	ToolType currentHelmet = AllTools.instance.defaultType;
	ToolType currentGlove = AllTools.instance.defaultType;
	ToolType currentHook = AllTools.instance.defaultType;
	
	ToolType[] equipment = new ToolType[4];
	
	int currentLegAnimation = 0; 

	int bodyId = 0;
	int legsId = 1;
	int headId = 2;
	int eyesId = 3;
	int hairId = 4;
	int armId = 5;
	
	int legnumber = 12;
	
	int BodyOffsetY = 14;
	int HelmetOffsetY = 34;
	int LegsOffsetY = -6;
	
	int pieceSize = 52;
	
	public float larmRotation;
	public float rarmRotation;
	
	public float currentAngle;
	
	public float shoulderX = 9;
	public float shoulderY = 13;
	
	public float shoulderOriginX = 10;
	public float shoulderOriginY = 25;
	
	public float shoulderToolX = -31;
	public float shoulderToolY = -89;
	
	public float shoulderToolOriginX = 50;
	public float shoulderToolOriginY = 127;
	
	public float offsetArmLeft = 8;
	
	public float animationTime = 1;
	
	public float baseSpeedx = 250;
	public float baseSpeedy = 210;
	
	public float speedx = 250;
	public float speedy = 210;

	public float horspeed = 210;
	
	public boolean larmRotationDirection;
	
	public Vector2 handOffset = new Vector2(12,32);
	
	//SHAKE
	
	public float shakeAngle;
	public boolean shakeAngleDirection;
	
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
	
	float oldX;
	float oldY;
	
	float currentFriction;
	public boolean icy = false;
	public Vector2 tempVelocity = new Vector2();
	
	public boolean hookFlying = false;
	
	public Grapple hook;
	
	//FIGHT
	
	public float currentMeleeDamage;
	public ArrayList<Mob> touchedMeleeMobList = new ArrayList<Mob>();
	
	//TOOL CARACTERISTICS
	
	public boolean hasTool = false;
	public boolean bigTool = false;
	public boolean hasSword= false;
	
	//INPUTS
	
	public boolean leftPress;
	public boolean rightPress;
	public boolean leftClick;
	public boolean rightClick;
	
	public boolean upPressed;
	public boolean downPressed;
	
	//CARACTERISTICS
	public int currentItem = 0;
	public float baseDefense = 100;
	public float basePower = 10;
	

	public PNJ()
	{
		hook = new Grapple(this,0,0,0,0,0,null,null);
		hook.playerAttached = false;
		
		for(int i = 0; i<4; i++)equipment[i] = AllTools.instance.defaultType;
		
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
		//LEFT ARM
		if(!isTurned)batch.draw(tex.get(armId), x+shoulderX+offsetArmLeft, y+shoulderY+1, shoulderOriginX, shoulderOriginY, pieceSize, pieceSize, 1, 1, larmRotation);
		else 		 batch.draw(tex.get(armId), x+width-shoulderX-offsetArmLeft, y+shoulderY+1, -shoulderOriginX, shoulderOriginY, -pieceSize, pieceSize, 1, 1,-larmRotation);
		
		if(currentGlove.armsId>=0)
		{
			if(!isTurned)batch.draw(TextureManager.instance.PNJGlove[currentGlove.armsId], x+shoulderX+offsetArmLeft, y+shoulderY+1, shoulderOriginX, shoulderOriginY, 52, 52, 1, 1, larmRotation);
			else         batch.draw(TextureManager.instance.PNJGlove[currentGlove.armsId], x+width-shoulderX-offsetArmLeft, y+shoulderY+1, -shoulderOriginX, shoulderOriginY, -52, 52, 1, 1, -larmRotation);
		}
		
		//BODY
		if(!isTurned)batch.draw(tex.get(bodyId), x, y+BodyOffsetY, pieceSize, pieceSize);
		else batch.draw(tex.get(bodyId), x+width+offx, y+BodyOffsetY, -pieceSize, pieceSize);
		
		if(currentArmor.armorId>=0)
		{
			if(!isTurned)batch.draw(TextureManager.instance.PNJArmor[currentArmor.armorId], x, y+BodyOffsetY, pieceSize, pieceSize);
			else batch.draw(TextureManager.instance.PNJArmor[currentArmor.armorId], x+width+offx, y+BodyOffsetY, -pieceSize, pieceSize);
		}
		
		//LEGS
		if(!isTurned)batch.draw(tex.get(legsId), x, y+LegsOffsetY, pieceSize, pieceSize);
		else batch.draw(tex.get(legsId), x+width+offx, y+LegsOffsetY, -pieceSize, pieceSize);
		
		if(currentLeggins.legsId>=0)
		{
			if(!isTurned)batch.draw(TextureManager.instance.PNJLeggins[currentLeggins.legsId][currentLegAnimation], x, y+LegsOffsetY, pieceSize, pieceSize+1);
			else batch.draw(TextureManager.instance.PNJLeggins[currentLeggins.legsId][currentLegAnimation], x+width+offx, y+LegsOffsetY, -pieceSize, pieceSize+1);
		}
		
		//HEAD
		if(!isTurned)batch.draw(tex.get(headId), x, y+HelmetOffsetY, pieceSize, pieceSize);
		else batch.draw(tex.get(headId), x+width+offx, y+HelmetOffsetY, -pieceSize, pieceSize);
		
		if(!isTurned)batch.draw(tex.get(eyesId), x, y+HelmetOffsetY, pieceSize, pieceSize);
		else batch.draw(tex.get(eyesId), x+width+offx, y+HelmetOffsetY, -pieceSize, pieceSize);
		
		if(currentHelmet.helmetId<0 || currentHelmet.helmetHair)
		{
			if(!isTurned)batch.draw(tex.get(hairId), x, y+HelmetOffsetY, pieceSize, pieceSize);
			else batch.draw(tex.get(hairId), x+width+offx, y+HelmetOffsetY, -pieceSize, pieceSize);
		}
		else if(currentHelmet.helmetId>=0)
		{
			if(!isTurned)batch.draw(TextureManager.instance.PNJHelmet[currentHelmet.helmetId], x, y+HelmetOffsetY, pieceSize, pieceSize);
			else batch.draw(TextureManager.instance.PNJHelmet[currentHelmet.helmetId], x+width+offx, y+HelmetOffsetY, -pieceSize, pieceSize);
		}
		
		
		//WEAPON
		if(currentItem != 0)
		{
			if(bigTool)
			{
				if(!isTurned)batch.draw(AllTools.instance.getRegion(AllTools.instance.getType(currentItem).weaponTexture), x+shoulderToolX, y+shoulderToolY, shoulderToolOriginX, shoulderToolOriginY, 200, 130, 1, 1, rarmRotation);
				else batch.draw(AllTools.instance.getRegion(AllTools.instance.getType(currentItem).weaponTexture), x+width-shoulderToolX, y+shoulderToolY, -shoulderToolOriginX, shoulderToolOriginY, -200, 130, 1, 1, rarmRotation);
			}
			else
			{
				if(!isTurned)batch.draw(GameScreen.items.getTextureById(currentItem), shoulderPos.x-handOffset.x, shoulderPos.y-handOffset.y, handOffset.x, handOffset.y, 24, 24, 1, 1, rarmRotation);
				else 		 batch.draw(GameScreen.items.getTextureById(currentItem), shoulderPos.x+handOffset.x, shoulderPos.y-handOffset.y, -handOffset.x, handOffset.y, -24, 24, 1, 1, rarmRotation);
			}
		}
		
		//RIGHT ARM
		if(!isTurned)batch.draw(tex.get(armId), x+shoulderX, y+shoulderY, shoulderOriginX, shoulderOriginY, pieceSize, pieceSize, 1, 1, rarmRotation);
		else         batch.draw(tex.get(armId), x+width-shoulderX, y+shoulderY, -shoulderOriginX, shoulderOriginY, -pieceSize, pieceSize, 1, 1, rarmRotation);
		
		if(currentGlove.armsId>=0)
		{
			if(!isTurned)batch.draw(TextureManager.instance.PNJGlove[currentGlove.armsId], x+shoulderX, y+shoulderY, shoulderOriginX, shoulderOriginY, pieceSize, pieceSize, 1, 1, rarmRotation);
			else         batch.draw(TextureManager.instance.PNJGlove[currentGlove.armsId], x+width-shoulderX, y+shoulderY, -shoulderOriginX, shoulderOriginY, -pieceSize, pieceSize, 1, 1, rarmRotation);
		}
	}
	
	
	
	
	
	
	@Override
	public void move()
	{
		oldX = x;
		oldY = y;
		
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
		
		helmetLight();
		
		hookPhysics();
	}
	
	
	public void helmetLight()
	{
		if(currentHelmet.helmetLight)
		{
			Vector2 headvec = new Vector2(middle.x, middle.y+34);
			
			Vector2 vec = (new Vector2(VisorPos)).sub(headvec);
			
			Map.instance.lights.torche(headvec, vec, currentHelmet.torchangle,1.5f,1.5f,1.2f,false,0);
		}
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
		
		if(hookFlying)
		{
			tempVelocity.x = vx;
			tempVelocity.y = vy;
			
			float oldlen = tempVelocity.len();
			float oldvx = tempVelocity.x;
			
			tempVelocity.x+=ax*Main.delta;
			if(tempVelocity.len()>oldlen && tempVelocity.len()>baseSpeedx)
			{
				tempVelocity.x = oldvx;
			}
			
			oldlen = tempVelocity.len();
			float oldvy = tempVelocity.y;
			
			tempVelocity.y+=ay*Main.delta;
			if(tempVelocity.len()>oldlen && tempVelocity.len()>baseSpeedx)
			{
				tempVelocity.y = oldvy;
			}
			vx = tempVelocity.x;
			vy = tempVelocity.y;
		}
		else
		{
		
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
		
		speedx = baseSpeedx;
		speedy = baseSpeedy;
		power = basePower;
		defense = baseDefense;
		
		if(Parameters.i.superman && isMain && manual)
		{
			speedx+=1000;
			speedy+=200;
			defense+=1000;
			power+=1000;
		}
		
		for(int i = 0; i<4; i++)
		{
			speedx+=equipment[i].armorSpeed;
			speedy+=equipment[i].armorJump;
			defense+=equipment[i].armorDefense;
			power+=equipment[i].armorPower;
		}
		
		if(!Main.pause)
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
		}
		
		
		currentAngle = Tools.angleCrop(currentAngle);
		
		if(VisorPos.y>y+50)tex.set(eyesId, TextureManager.instance.PNJeyes[currentEyes][0]);
		else tex.set(eyesId, TextureManager.instance.PNJeyes[currentEyes][1]);
		
		calculateShoulder();
		animationTools();
		animationSword();
		calculateShake();
		calculateAngle();
		
		calculateVectors();
		
		animationLegs();
		animationBackArm();
		
	}
	
	
	
	
	public void calculateShoulder()
	{
		if(!isTurned)shoulderPos.x = (x+shoulderToolX+shoulderToolOriginX);
		else shoulderPos.x = (x+width-shoulderToolX-shoulderToolOriginX);
		
		shoulderPos.y = (y+shoulderToolY+shoulderToolOriginY);
	}
	
	
	
	
	public void calculateAngle()
	{
		if(!isTurned)rarmRotation = currentAngle+80+AllTools.instance.getType(currentItem).angle+shakeAngle;
		else 		 rarmRotation = currentAngle-80-AllTools.instance.getType(currentItem).angle+shakeAngle;
			
	}
	
	
	
	public void calculateVectors()
	{

		Vector2 launcherOffset = new Vector2(AllTools.instance.getType(currentItem).launcherDistance,0);
		
		if(!isTurned)launcherOffset.setAngle(rarmRotation+AllTools.instance.getType(currentItem).launcherAngle);
		else launcherOffset.setAngle(rarmRotation-AllTools.instance.getType(currentItem).launcherAngle+180);
		
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
	
	
	
	public void calculateShake()
	{
		if(leftPress || rightPress)
		{
			if(shakeAngleDirection)
			{
				shakeAngle += 300*Main.delta; 
				if(shakeAngle>20)
					shakeAngleDirection = false;
			}
			else
			{
				shakeAngle -= 300*Main.delta; 
				if(shakeAngle<-20)
					shakeAngleDirection = true;
			}
			if(hasTool)if(!AllTools.instance.getType(currentItem).isShake)shakeAngle = 0;
		}
		else if(bigTool)shakeAngle = 0;
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
	
	
	
	public void animationSword()
	{
		if(hasSword)
		{
			if(!leftPress && currentMeleeDamage>AllTools.instance.getType(currentItem).damage/2)
			{
				
				Vector2 cannonCoord2 = new Vector2(cannonCoord.x-shoulderPos.x, cannonCoord.y-shoulderPos.y);
				Vector2 cannonCoord2clamped = new Vector2(cannonCoord2).clamp(1, 1);
				
				float l = 0;
				boolean finished = false;
				boolean canmove = true;
				
				currentMeleeDamage -= Main.delta*AllTools.instance.getType(currentItem).damage*AllTools.instance.getType(currentItem).downtime/30;
				
				if(canmove)
				{
					if(isTurned)
					{
						currentAngle = Tools.fLerpAngle(currentAngle, 90, AllTools.instance.getType(currentItem).downtime, true);
					}
					else
					{
						currentAngle = Tools.fLerpAngle(currentAngle, 270, AllTools.instance.getType(currentItem).downtime, false);
					}
				}
				
				while(!finished)
				{
					l+=5;
					if(l>=cannonCoord2.len())
					{
						l = cannonCoord2.len();
						finished = true;
					}
					cannonCoord2clamped.clamp(l, l);
					
					if(Parameters.i.drawhitbox)Tools.drawRect(cannonCoord2clamped.x+shoulderPos.x, cannonCoord2clamped.y+shoulderPos.y, 2, 2);
					
					currentMeleeDamage = Math.max(Math.min(currentMeleeDamage, AllTools.instance.getType(currentItem).damage), 0);
					
					if(currentMeleeDamage>=AllTools.instance.getType(currentItem).damage/4 && AllEntities.getType(Tools.floor((cannonCoord2clamped.x+shoulderPos.x)/16), Tools.floor((cannonCoord2clamped.y+shoulderPos.y)/16)) == AllEntities.mobtype)
					{
						Mob m = (Mob)AllEntities.getEntity(Tools.floor((cannonCoord2clamped.x+shoulderPos.x)/16), Tools.floor((cannonCoord2clamped.y+shoulderPos.y)/16));
						
						if(!touchedMeleeMobList.contains(m))
						{
							boolean touched = false;
							ArrayList<Rectangle> rects = m.hitrect.getRects(m.isTurned);
							
							for(int i = 0; i<rects.size(); i++)
							{
								if(rects.get(i).contains((cannonCoord2clamped.x+shoulderPos.x)-m.x, (cannonCoord2clamped.y+shoulderPos.y)-m.y))
								{
									touched = true;
									break;
								}
							}
							if(touched)
							{
								float damage = (float) (currentMeleeDamage+Math.random()*currentMeleeDamage/5 - currentMeleeDamage/20);
								damage*=power/10f;
								m.Hurt(damage,0.1f,cannonCoord2clamped.x+shoulderPos.x, cannonCoord2clamped.y+shoulderPos.y);
								touchedMeleeMobList.add(m);
							}
						}
					}
					if(Map.instance.mainLayer.getBloc(Tools.floor((cannonCoord2clamped.x+shoulderPos.x)/16), Tools.floor((cannonCoord2clamped.y+shoulderPos.y)/16)).collide)
					{
						finished = true;
					}
				}
			}
			else
			{
				touchedMeleeMobList.clear();
				if(leftPress)
				{
					if(isTurned)currentAngle = Tools.fLerpAngle(currentAngle, 250, AllTools.instance.getType(currentItem).uptime, false);
					else currentAngle = Tools.fLerpAngle(currentAngle, 110, AllTools.instance.getType(currentItem).uptime, true);
					currentMeleeDamage+=AllTools.instance.getType(currentItem).uptime*Main.delta*AllTools.instance.getType(currentItem).damage/10;
				}
				else
				{
					currentMeleeDamage -= Main.delta*AllTools.instance.getType(currentItem).damage;
				}
			}
			
			if(currentMeleeDamage>AllTools.instance.getType(currentItem).damage*1.5f)currentMeleeDamage=AllTools.instance.getType(currentItem).damage*1.5f;
		}
		else currentMeleeDamage = 0;
		
		if(currentMeleeDamage<0)currentMeleeDamage=0;
	}
	
	
	public void animationBackArm()
	{
		if(vy<-150)
		{
			if(Math.abs(larmRotation-150)>0.01f)larmRotation = larmRotation+(150-larmRotation)*Main.delta*20*Math.abs(vy)/500;
		}
		else if(vy>0.1f && !hookFlying)
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
		else if(!hookFlying)
		{
			if(Math.abs(larmRotation-27)>0.1f)larmRotation = larmRotation+(27-larmRotation)*Main.delta*20;
			
		}
		else
		{
			if(Math.abs(larmRotation-70)>0.1f)larmRotation = larmRotation+(70-larmRotation)*Main.delta*20;
		}
	}
	
	
	
	
	
	
	public void animationLegs()
	{
		if(!canJump)
		{
			if(vy<-speedy*0.1f)
			{
				currentLegAnimation = legnumber-1;
			}
			else if(vy>speedy*0.1f)
			{
				currentLegAnimation = legnumber-3;
			}
			else
			{
				currentLegAnimation = legnumber-2;
			}
		}
		else
		{
			if(vx == 0)
			{
				currentLegAnimation = 0;
			}
			else
			{
				if(icy)
				{
					currentLegAnimation = 0;
				}
				else
				{
					currentLegAnimation = (int)animationTime;
				}
			}
		}
		
		tex.set(legsId, TextureManager.instance.PNJlegs[currentLegs][currentLegAnimation]);
	}
	
	
	
	public void hookPhysics()
	{
		if(hook.playerAttached && hook.isPlanted && hook.getLine().len()>hook.max)
		{
			hookFlying = true;
		}
		else
		{
			hookFlying = false;
		}
		
		if(hookFlying)
		{
			float oldlaunchx = oldX-x+hookAnchorCoord.x;
			float oldlaunchy = oldY-y+hookAnchorCoord.y;
			float nx = hookAnchorCoord.x-hook.x;
			float ny = hookAnchorCoord.y-hook.y;
			
			Vector2 d2 = new Vector2(nx,ny);
			d2.setLength(hook.max);
			
			float nx2 = (hook.x+d2.x)-oldlaunchx;
			float ny2 = (hook.y+d2.y)-oldlaunchy;
			Vector2 d3 = new Vector2(nx2,ny2);
			
			d3.nor();
			
			tempVelocity.x = vx;
			tempVelocity.y = vy;
			
			d3.setLength(d3.dot(tempVelocity));
			
			tempVelocity = d3;
			
			x += (hook.x+d2.x)-hookAnchorCoord.x;
			hitbox.update(x,y);
			if(hitbox.TestCollisionsAll())
			{
				x = oldX;
				tempVelocity.x = 0;
			}
			
			y += (hook.y+d2.y)-hookAnchorCoord.y;
			hitbox.update(x,y);
			if(hitbox.TestCollisionsAll())
			{
				y = oldY;
				tempVelocity.y = 0;
			}
			hitbox.update(x,y);
			
			hook.max = Math.max(hook.max, hook.getLine().len()-8);
			
			vx = tempVelocity.x;
			vy = tempVelocity.y;
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
		if (vx!=0 && !hookFlying)
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
	public void goUp()
	{
		upPressed = true;
	}
	
	@Override
	public void goDown()
	{
		downPressed = true;
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
	public void setEquipment(ToolType equip)
	{
		if(equip.helmet)
		{
			currentHelmet = equip;
			equipment[0] = equip;
		}
		if(equip.armor)
		{
			currentArmor = equip;
			equipment[1] = equip;
		}
		if(equip.arms)
		{
			currentGlove = equip;
			equipment[2] = equip;
		}
		if(equip.legs)
		{
			currentLeggins = equip;
			equipment[3] = equip;
		}
	}
	
	@Override
	public void setHelmet(ToolType equip)
	{
		currentHelmet = equip;
		equipment[0] = equip;
	}
	
	@Override
	public void setArmor(ToolType equip)
	{
		currentArmor = equip;
		equipment[1] = equip;
	}
	
	@Override
	public void setGlove(ToolType equip)
	{
		currentGlove = equip;
		equipment[2] = equip;
	}
	
	@Override
	public void setLeggins(ToolType equip)
	{
		currentLeggins = equip;
		equipment[3] = equip;
	}
	
	@Override
	public void setHook(ToolType equip)
	{
		currentHook = equip;
	}
	
	@Override
	public void goHook()
	{
		if(hook.playerAttached)
		{
			hook.playerAttached = false;
		}
		else
		{
			if(currentHook.grapple)
			{
				GameScreen.entities.projectiles.AddGrapple(this, hookAnchorCoord.x, hookAnchorCoord.y, hookToVisorCoord.x, hookToVisorCoord.y, currentHook.grappleDistance, currentHook.grappleTex, currentHook.grappleTexRope);
			}
		}
	}
	
	@Override
	public void preinput()
	{
		currentFriction = Map.instance.mainLayer.getBloc(Tools.floor((x+width/2)/Map.instance.mainLayer.getTileWidth()), Tools.floor((y)/Map.instance.mainLayer.getTileHeight())).friction; 
		leftPress = false;
		rightPress = false;
		downPressed = false;
		upPressed = false;
	}
	
}
