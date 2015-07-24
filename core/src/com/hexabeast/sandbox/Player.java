package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.sandbox.mobs.BigInsecte;
import com.hexabeast.sandbox.mobs.Hornet;
import com.hexabeast.sandbox.mobs.Mob;
import com.hexabeast.sandbox.mobs.PNJ;
import com.hexabeast.sandbox.mobs.Wolf;

public class Player extends Entity{
	
	
	public ArrayList<Mob> touchedList = new ArrayList<Mob>();
	
	public ArrayList<Mob> transformList = new ArrayList<Mob>();
	public Wolf transformWolf;
	public BigInsecte transformBigInsecte;
	public Hornet transformHornet;
	public PNJ transformPlayer;
	
	boolean grappleFlying = false;
	public Grapple grapple;
	
	public boolean transformed = false;
	
	float oldX;
	float oldY;
	
	public float transformrate = 0.6f;
	public float transformtime = 0;
	public boolean transformingin = false;
	public boolean transformingout = false;
	public int nextTransform = -1;

	public HitBox hitbox;
	public HitRect hitrect;
	public float maxHealth = 100;
	public float health = maxHealth;

	boolean icy = false;
	boolean stuck = false;
	private float shakeWeapon;
	private float shakeWeapon2;
	private boolean shakeWeaponDirection;
	private boolean shakeWeaponDirection2;
	public Vector2 velocity = new Vector2();
	public Vector2 acceleration = new Vector2();
	
	public float originalSpeed = 250;
	public float originalHorspeed = 210;
	
	public float speed = 250, gravity = 1000f,x,y,w,h;

	public float horspeed = 210;
	public float defense = 100;
	public float originalDefense = 100;
	public float currentAngle = 0;
	
	public boolean canJump = false, beginHike = false;
	
	public float animationTime = 0;
	
	public Sprite arm;
	public Sprite arm2;
	public Sprite armHang;
	public Sprite armWeapon;
	
	boolean isTool;
	
	public Sprite Helmet;
	public Sprite Armor;
	public Sprite Leggins;
	public Sprite Arms;
	
	public Stats stats;
	
	float tempangle;
	
	private float armOffsetX = -4;
	private float armOffsetY = -15;
	
	public float transoffy = 0;
	public float transoffx = 0;
	
	private float armHangOffsetX = 0;
	private float armHangOffsetY = 30;
	
	public  Sprite[] bodyParts = new Sprite[5];
	public Vector2 initialPos;
	
	public int currentCellID;
	public int currentCellState = 0;
	
	public boolean hurt;
	public float handvelocity = 0;
	
	public float currentDamage = 0;
	
	Vector2 launcherOffset;
	Vector2 hookOffset;
	public Vector2 hookCoord;
	public Vector2 launcherCoord = new Vector2();
	Vector2 shoulderCoord = new Vector2();
	Vector2 velocityCoord = new Vector2();
	Vector2 velocityCoord2 = new Vector2();
	Vector2 velocityHook = new Vector2();
	public Vector2 middle = new Vector2();
	
	TextureRegion body;
    TextureRegion head;
    TextureRegion hairs;
    TextureRegion[] eyes;
    TextureRegion leg;
    TextureRegion legStill;
    Animation legAnimWalk;
    TextureRegion legJump1;
    TextureRegion legJump2;
    TextureRegion legJump3;
    
    int bodyID = 0;
    int legsID = 1;
    int headID = 2;
    int eyesID = 3;
    int hairsID = 4;
	
	public Player()
	{
		launcherOffset = new Vector2(1,0);
		hookOffset = new Vector2(20,0);
		hookCoord = new Vector2(0,0);
		
		grapple = new Grapple(0,0,0,0,0,null,null);
		grapple.playerAttached = false;
		
		transformWolf = new Wolf();
		transformWolf.manual = true;
		transformWolf.speedx*=1.8f;
		transformWolf.power = 20;
		transformList.add(transformWolf);
		
		transformBigInsecte = new BigInsecte();
		transformBigInsecte.manual = true;
		transformBigInsecte.speedx = 400;
		transformBigInsecte.speedy = 400;
		transformBigInsecte.power = 20;
		transformList.add(transformBigInsecte);
		
		transformHornet = new Hornet();
		transformHornet.manual = true;
		transformHornet.speedx = 300;
		transformHornet.speedy = 1300;
		transformHornet.power = 20;
		transformList.add(transformHornet);
		
		transformPlayer = new PNJ();
		transformPlayer.manual = true;
		transformPlayer.speedx = 250;
		transformPlayer.speedy = 1000;
		transformList.add(transformPlayer);
		
		
		body = new TextureRegion(TextureManager.instance.Abody);
		head = new TextureRegion(TextureManager.instance.Ahead);
		hairs = new TextureRegion(TextureManager.instance.Ahairs);
		eyes = new TextureRegion(TextureManager.instance.Aeyes).split(TextureManager.instance.Aeyes.getRegionWidth()/2, TextureManager.instance.Aeyes.getRegionHeight())[0];
		leg = new TextureRegion(TextureManager.instance.Aleg);
		
		TextureRegion[] legs = TextureManager.instance.Alegs;
        
        legAnimWalk = new Animation(0.085f, legs[1],legs[2],legs[3],legs[4],legs[5],legs[6],legs[7],legs[8]);
        legAnimWalk.setPlayMode(PlayMode.LOOP);
        
        legStill = legs[0];
        
        legJump1 = legs[9];
        legJump2 = legs[10];
        legJump3 = legs[11];
        
		Helmet = new Sprite(AllTools.instance.getRegion(9));
		Armor = new Sprite(AllTools.instance.getRegion(10));
		Leggins = new Sprite(AllTools.instance.getLeggins(1)[0]);
		Arms = new Sprite(AllTools.instance.getRegion(11));
		
		armHang = new Sprite(GameScreen.items.getTextureById(1));
		armHang.setSize(32, 32);
		armWeapon = new Sprite(AllTools.instance.getRegion(1));
		arm = new Sprite(TextureManager.instance.armRTexture);
		arm2 = new Sprite(TextureManager.instance.armRTexture);
		
		armWeapon.setScale(0.7f);
		arm.setScale(0.7f);
		arm2.setScale(0.7f);
		armHang.setScale(0.7f);
		
		Helmet.setScale(0.7f);
		Armor.setScale(0.7f);
		Arms.setScale(0.7f);
		Leggins.setScale(0.7f);
		
		armWeapon.setOrigin(75, 195);
		arm.setOrigin(75, 195);
		arm2.setOrigin(75, 195);
		Arms.setOrigin(75, 195);
		armHang.setOrigin(armHangOffsetX+armHang.getWidth()/2, armHangOffsetY+armHang.getHeight()/2);
		
		for(int i = 0; i<5; i++)
		{
			bodyParts[i] = new Sprite(body);
			bodyParts[i].setScale(0.7f);
		}
		
		bodyParts[bodyID].setRegion(body);
		bodyParts[headID].setRegion(head);
		bodyParts[eyesID].setRegion(eyes[0]);
		bodyParts[hairsID].setRegion(hairs);

		w = bodyParts[0].getWidth();
		h = bodyParts[0].getHeight();
		
		width = w;
		height = w;

		initialPos = new Vector2(Map.instance.width/2, Map.instance.randomHeight-40);
		if(Main.devtest)initialPos = new Vector2(2, Map.instance.randomHeight-40);
		
		while(!Map.instance.mainLayer.blocs[Tools.floor(initialPos.x)][Tools.floor(initialPos.y-5)].collide)
		{
			initialPos.y--;
		}
		initialPos.x*=Map.instance.mainLayer.getTileWidth();
		initialPos.y*=Map.instance.mainLayer.getTileWidth();
		currentCellState = 0;
		
		hitrect = new HitRect(w);
		hitrect.add(new Rectangle(32, 24, 14, 58));
		hitbox = new HitBox(hitrect.normal, w);
		hitbox.noturn = true;
		offx = 0;
	}
	
	public ArrayList<Rectangle> getHitRect()
	{
		if(!transformed)return hitrect.normal;
		else return transformList.get(Parameters.i.currentTransform).hitrect.getRects(transformList.get(Parameters.i.currentTransform).isTurned);
	}
	
	public void setPosition(float gx, float gy)
	{
		x = gx;
		y = gy;
		for(int i = 0; i<transformList.size(); i++)
		{
			transformList.get(i).x = x+transoffx;
			transformList.get(i).y = y+hitbox.min-transformList.get(i).hitbox.min;
			transformList.get(i).vx = velocity.x;
			transformList.get(i).vy = velocity.y;
		}
	}
	
	public boolean isTouched(float x, float y)
	{
		return (Math.abs(x-bodyParts[1].getX()-bodyParts[1].getWidth()/2)<bodyParts[1].getWidth()/3.5f && Math.abs(y-bodyParts[1].getY()-6-bodyParts[1].getHeight()/2)<bodyParts[1].getHeight()/3f);
	}
	
	public void Update()
	{
		if(!Main.pause)
		{
			setStats();
			input();
			
			Forces();
			Collision(Main.delta);
			visualThings();
			
			if(GameScreen.inventory.Helmet().helmetLight)
			{
				Vector2 headvec = new Vector2(middle.x, middle.y+34);
				
				Vector2 vec = new Vector2((Tools.getAbsoluteMouse()).sub(headvec));
				
				Map.instance.lights.torche(headvec, vec, GameScreen.inventory.Helmet().torchangle,1.5f,1.5f,1.2f,false,0);
			}
			
			if(grapple.playerAttached && grapple.isPlanted && grapple.getLine().len()>grapple.max)
			{
				grappleFlying = true;
				
			}
			else
			{
				grappleFlying = false;
			}
			
			hookPhysics();
			
			if(AllEntities.getType(Tools.floor(middle.x/16), Tools.floor(middle.y/16)-1) == AllEntities.furnituretype)
			{
				Furniture f = (Furniture)AllEntities.getEntity(Tools.floor(middle.x/16), Tools.floor(middle.y/16)-1);
				if(f.container)
				{
					if(f != GameScreen.inventory.chest || !GameScreen.inventory.chestopen)
					{
						GameScreen.inventory.putinchest(f);
					}
					GameScreen.inventory.chest = f;
					GameScreen.inventory.chestopen = true;
				}
				else
				{
					GameScreen.inventory.chestopen = false;
				}
			}
			else
			{
				GameScreen.inventory.chestopen = false;
			}
		}
		
		
	}
	
	/*public void computeVillage()
	{
		if(y>(Map.instance.height+30)*16)isVillage = true;
		else isVillage = false;
	}*/
	
public void inputTransform()
{	
	if(Inputs.instance.Q)transformList.get(Parameters.i.currentTransform).goLeft();
	else if(Inputs.instance.D)transformList.get(Parameters.i.currentTransform).goRight();
	else transformList.get(Parameters.i.currentTransform).goStandX();
	
	if(Inputs.instance.S)transformList.get(Parameters.i.currentTransform).goDown();
	else if(Inputs.instance.Z)transformList.get(Parameters.i.currentTransform).goUp();
	else transformList.get(Parameters.i.currentTransform).goStandY();

	if(Inputs.instance.mousedown && !Main.pause)transformList.get(Parameters.i.currentTransform).goAttack();
	if(Inputs.instance.space)transformList.get(Parameters.i.currentTransform).goJump();
}
	
public void input()
{
	if(Inputs.instance.space && canJump)
	{
		velocity.y = horspeed*2.4f;
		canJump = false;
	}
	if(velocity.y<=0.05f && canJump)
	{
		stuck = false;
	}
	if(!stuck)
	{
		float friction = Map.instance.mainLayer.getBloc(Tools.floor((x+w/2)/Map.instance.mainLayer.getTileWidth()), Tools.floor((y+15)/Map.instance.mainLayer.getTileHeight())).friction; 
		
		if(Inputs.instance.D)
		{
			acceleration.x = 3000;
			icy = false;
			if(velocity.x<0)
			{
				velocity.x += (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
			}
		}
		else if(Inputs.instance.Q)
		{
			icy = false;
			acceleration.x = -3000;
			if(velocity.x>0)
			{
				velocity.x -= (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
			}
		}
		else
		{
			acceleration.x = 0;
			if (velocity.x!=0 && !grappleFlying)
			{
				
				if(!canJump)friction/=6;
				if(velocity.x>0)
				{
					velocity.x -= (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
					if(velocity.x<0)velocity.x=0;
				}
				else
				{
					velocity.x += (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
					if(velocity.x>0)velocity.x=0;
				}
				if(friction<1)
				{
					animationTime = 0;
					icy = true;
				}
				
				if(Math.abs(velocity.x)<0.5f)velocity.x=0;
			}
			else
			{
				animationTime = 0;
			}
		}
		
		
		if(Math.abs(velocity.x)>speed && canJump)
		{
			if(velocity.x>0)
			{
				velocity.x -= (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
				if(velocity.x<0)velocity.x=0;
			}
			else
			{
				velocity.x += (friction*Main.delta*1000)+(Math.abs(velocity.x)*Main.delta*10);
				if(velocity.x>0)velocity.x=0;
			}
		}
	}
}

public void setStats()
{
	stats = GameScreen.inventory.getStats();
	
	speed = originalSpeed+stats.speed;
	gravity = Constants.gravity;
	horspeed = originalHorspeed+stats.jump;
	defense = originalDefense+stats.defense;
	
	if(Parameters.i.superman)
	{
		speed = 2100;
		gravity = Constants.gravity;
		horspeed = 420;
	}
}

public void Forces()
{
	if(grappleFlying)
	{
		float oldlen = velocity.len();
		float oldvx = velocity.x;
		
		velocity.x+=acceleration.x*Main.delta;
		if(velocity.len()>oldlen && velocity.len()>originalSpeed)
		{
			velocity.x = oldvx;
		}
		
		oldlen = velocity.len();
		float oldvy = velocity.y;
		
		velocity.y+=acceleration.y*Main.delta;
		if(velocity.len()>oldlen && velocity.len()>originalSpeed)
		{
			velocity.y = oldvy;
		}
	}
	else
	{
		
		float oldlen = Math.abs(velocity.x);
		float oldvx = velocity.x;
		
		float multiplier = 1;
		if(Math.abs(velocity.x)>originalSpeed*2/3f)
		{
			multiplier = 1/20f;
		}
		if(Math.abs(velocity.x)>originalSpeed*4/3f)multiplier = 2/10f;
		velocity.x+=acceleration.x*Main.delta*multiplier;
		if(Math.abs(velocity.x)>oldlen && Math.abs(velocity.x)>speed)
		{
			velocity.x = oldvx;
		}
	}
	
	
	if(velocity.y>-1000)velocity.y -= gravity*Main.delta;

	velocity.clamp(0, 2000);

	oldX = x;
	oldY = y;
	
	animationTime += Main.delta*Math.abs(velocity.x)/250;
}
	
private void Collision(float delta)
{
	y += velocity.y * delta;
	hitbox.update(x,y);
	
	canJump = false;
	if(hitbox.TestCollisions(true) || (hitbox.TestCollisions(false) && velocity.y<=0))
	{
		y = oldY;
		velocity.y = 0;
		
	}
	if(hitbox.TestCollisionsDown() || hitbox.TestCollisions(false))
	{
		canJump = true;
	}

	hitbox.update(x,y);
	
	boolean hiking = false;
	if(hitbox.TestCollisions(false))hiking = true; 

	x+=velocity.x*delta;
	
	
	hitbox.update(x,y);
	
	if(hitbox.TestCollisions(true) || (hitbox.TestCollisionsHigh() && !hiking && hitbox.TestCollisions(false)))
	{
		x = oldX;
		velocity.x = 0;
	}
	else if(hitbox.TestCollisions(false))
	{
		float oldY2 = y;
		y+=Math.min(Math.abs(16-((y+hitbox.min)-(int)((y+hitbox.min)/16)*16)),Math.abs(velocity.x)*1.6f*delta);
		hitbox.update(x,y);	
		if(hitbox.TestCollisions(true))
		{
			y = oldY2;
			y+=Math.abs(velocity.x)*0.2f*delta;
			hitbox.update(x,y);	
			if(hitbox.TestCollisions(true))
			{
				y = oldY2;
			}
		}
	}
	hitbox.update(x,y);
}

public void hookPhysics()
{
	if(grappleFlying)
	{	
		float oldlaunchx = oldX-x+hookCoord.x;
		float oldlaunchy = oldY-y+hookCoord.y;
		float nx = hookCoord.x-grapple.x;
		float ny = hookCoord.y-grapple.y;
		
		Vector2 d2 = new Vector2(nx,ny);
		d2.setLength(grapple.max);
		
		float nx2 = (grapple.x+d2.x)-oldlaunchx;
		float ny2 = (grapple.y+d2.y)-oldlaunchy;
		Vector2 d3 = new Vector2(nx2,ny2);
		
		d3.nor();
		
		d3.setLength(d3.dot(velocity));
		
		velocity = d3;
		
		x += (grapple.x+d2.x)-hookCoord.x;
		hitbox.update(x,y);
		if(hitbox.TestCollisionsAll())
		{
			x = oldX;
			velocity.x = 0;
		}
		
		y += (grapple.y+d2.y)-hookCoord.y;
		hitbox.update(x,y);
		if(hitbox.TestCollisionsAll())
		{
			y = oldY;
			velocity.y = 0;
		}
		hitbox.update(x,y);
		
		grapple.max = Math.max(grapple.max, grapple.getLine().len()-8);
	}
}

public void visualThings()
{
	isTool = false;
	if(currentCellID>999)isTool = true;
	
	boolean press = false;
	boolean bigitem = false;
	boolean sword = false;
	
	if((isTool && AllTools.instance.getType(currentCellID).weaponTexture!=0))bigitem = true;
	
	if(isTool && AllTools.instance.getType(currentCellID).type == AllTools.instance.Sword)sword = true;
	
	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT))press = true;
	
	float teoangle = getAngle(new Vector2(arm.getX()+arm.getOriginX(), arm.getY()+arm.getOriginY()),Tools.getAbsoluteMouse());
	
	float cur = currentAngle;
	if(currentAngle==0)currentAngle = teoangle;
	else if(( !sword ))currentAngle = Tools.fLerpAngle(currentAngle, teoangle, AllTools.instance.getType(currentCellID).uptime);
	else if(sword)
	{
		if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && currentDamage<=AllTools.instance.getType(currentCellID).damage/2)currentAngle = Tools.fLerpAngle(currentAngle, teoangle, AllTools.instance.getType(currentCellID).uptime);
	}
	
	
	
	if(handvelocity>1200)handvelocity = 1200;
	
	if(teoangle>270 || teoangle<90)
	{
		if(!isTurned)
		{
			currentAngle = -(currentAngle-180);
		}
		isTurned = true;
	}
	else
	{
		if(isTurned)
		{
			currentAngle = -(currentAngle-180);
		}
		isTurned = false;
	}
	
	//SWORD ->
	if(sword)
	{
		if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && currentDamage>AllTools.instance.getType(currentCellID).damage/2)
		{
			float awx = armWeapon.getX()+armWeapon.getOriginX();
			float awy = armWeapon.getY()+armWeapon.getOriginY();
			
			Vector2 launcherCoord2 = new Vector2(launcherCoord.x-awx, launcherCoord.y-awy);
			Vector2 launcherCoordBis = new Vector2(launcherCoord2).clamp(1, 1);
			
			float l = 0;
			boolean finished = false;
			boolean canmove = true;
			
			currentDamage -= Main.delta*AllTools.instance.getType(currentCellID).damage*AllTools.instance.getType(currentCellID).downtime/30;
			
			if(canmove)
			{
				
				if(isTurned)
				{
					currentAngle = Tools.fLerpAngle(currentAngle, 270, AllTools.instance.getType(currentCellID).downtime, false);
				}
				else
				{
					currentAngle = Tools.fLerpAngle(currentAngle, -90, AllTools.instance.getType(currentCellID).downtime, true);
				}
			}
			
			while(!finished)
			{
				l+=5;
				if(l>=launcherCoord2.len())
				{
					l = launcherCoord2.len();
					finished = true;
				}
				launcherCoordBis.clamp(l, l);
				
				if(Parameters.i.drawhitbox)Tools.drawRect(launcherCoordBis.x+awx, launcherCoordBis.y+awy, 2, 2);
				
				currentDamage = Math.max(Math.min(currentDamage, AllTools.instance.getType(currentCellID).damage), 0);
				
				if(currentDamage>=AllTools.instance.getType(currentCellID).damage/4 && AllEntities.getType(Tools.floor((launcherCoordBis.x+awx)/16), Tools.floor((launcherCoordBis.y+awy)/16)) == AllEntities.mobtype)
				{
					Mob m = (Mob)AllEntities.getEntity(Tools.floor((launcherCoordBis.x+awx)/16), Tools.floor((launcherCoordBis.y+awy)/16));
					
					if(!touchedList.contains(m))
					{
						boolean touched = false;
						ArrayList<Rectangle> rects = m.hitrect.getRects(m.isTurned);
						
						for(int i = 0; i<rects.size(); i++)
						{
							if(rects.get(i).contains((launcherCoordBis.x+awx)-m.x, (launcherCoordBis.y+awy)-m.y))
							{
								touched = true;
								break;
							}
						}
						if(touched)
						{
							m.damage((float) (currentDamage+Math.random()*currentDamage/5 - currentDamage/20),0.5f,launcherCoordBis.x+awx, launcherCoordBis.y+awy);
							touchedList.add(m);
						}
					}
				}
				if(Map.instance.mainLayer.getBloc(Tools.floor((launcherCoordBis.x+awx)/16), Tools.floor((launcherCoordBis.y+awy)/16)).collide)
				{
					finished = true;
				}
			}
		}
		else
		{
			touchedList.clear();
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
			{
				if(isTurned)currentAngle = Tools.fLerpAngle(currentAngle, 110, AllTools.instance.getType(currentCellID).uptime, true);
				else currentAngle = Tools.fLerpAngle(currentAngle, 70, AllTools.instance.getType(currentCellID).uptime, false);
				currentDamage+=AllTools.instance.getType(currentCellID).uptime*Main.delta*AllTools.instance.getType(currentCellID).damage/10;
			}
			else
			{
				currentDamage -= Main.delta*AllTools.instance.getType(currentCellID).damage;
			}
		}
		
		if(currentDamage>AllTools.instance.getType(currentCellID).damage*1.5f)currentDamage=AllTools.instance.getType(currentCellID).damage*1.5f;
	}
	else currentDamage = 0;
	
	if(currentDamage<0)currentDamage=0;
	

	// <- SWORD
	
	handvelocity = Math.abs(cur-currentAngle)*(1/Main.delta);
	
	if((velocity.x>0 && isTurned) || (velocity.x<0 && !isTurned))
		legAnimWalk.setPlayMode(PlayMode.LOOP);
	else
		legAnimWalk.setPlayMode(PlayMode.LOOP_REVERSED);
	
	
	for(int i = 0; i<5;i++)
	{
		bodyParts[i].setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):-Math.abs(bodyParts[0].getScaleX()), bodyParts[0].getScaleY());
		bodyParts[i].setPosition(x, y);
	}
	
	bodyParts[0].setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):-Math.abs(bodyParts[0].getScaleX()), bodyParts[0].getScaleY());
	
	SetArmors(isTurned);

	arm.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):!isTurned ?-Math.abs(bodyParts[0].getScaleX()):arm.getScaleX(), bodyParts[0].getScaleY());
	arm2.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):!isTurned ?-Math.abs(bodyParts[0].getScaleX()):arm2.getScaleX(), bodyParts[0].getScaleY());
	
	
	armWeapon.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):!isTurned ?-Math.abs(bodyParts[0].getScaleX()):arm.getScaleX(), bodyParts[0].getScaleY());
	float tempOffsetX;
	if(bodyParts[0].getScaleX()<0)tempOffsetX = -armOffsetX+7;
	else tempOffsetX = armOffsetX;
	arm.setPosition(x-w/2+tempOffsetX, y+armOffsetY-119);
	arm2.setPosition(x-w/2+tempOffsetX-9, y+armOffsetY-119);
	if(isTurned)arm2.setPosition(x-w/2+tempOffsetX+9, y+armOffsetY-119);
	armWeapon.setPosition(x-w/2+tempOffsetX, y+armOffsetY-119);
	armHang.setPosition(arm.getX()+arm.getWidth()/2-armHang.getWidth()/2-armHangOffsetX-75, arm.getY()+arm.getHeight()+armOffsetY-5-armHangOffsetY);
	armHang.setRegion(GameScreen.items.getTextureById(currentCellID));
	
	
	if(press)
	{
		if(shakeWeaponDirection)
		{
			shakeWeapon += 300*Main.delta; 
			if(shakeWeapon>20)
				shakeWeaponDirection = false;
		}
		else
		{
			shakeWeapon -= 300*Main.delta; 
			if(shakeWeapon<-20)
				shakeWeaponDirection = true;
		}
		if(isTool)if(!AllTools.instance.getType(currentCellID).isShake)shakeWeapon = 0;
	}
	else if(bigitem)shakeWeapon = 0;
	
	
	if(velocity.y<-150)
	{
		if(Math.abs(shakeWeapon2-150)>0.01f)shakeWeapon2 = shakeWeapon2+(150-shakeWeapon2)*Main.delta*20*Math.abs(velocity.y)/500;
	}
	else if(velocity.y>0.1f && !grappleFlying)
	{
		if(Math.abs(shakeWeapon2-20)>0.5f)
		shakeWeapon2 = shakeWeapon2+(20-shakeWeapon2)*Main.delta*20;
	}
	else if(Math.abs(velocity.x)>0.1f && canJump)
	{
		if(shakeWeaponDirection2)
		{
			shakeWeapon2 += Math.abs(velocity.x)*1.2f*Main.delta; 
			if(shakeWeapon2>75)
				shakeWeaponDirection2 = false;
		}
		else
		{
			shakeWeapon2 -= Math.abs(velocity.x)*1.1f*Main.delta; 
			if(shakeWeapon2<-15)
				shakeWeaponDirection2 = true;
		}
		
		if(shakeWeapon2>75)shakeWeapon2 -= Math.abs(velocity.x)*1.1f*Main.delta*5; 
		if(shakeWeapon2<-15)shakeWeapon2 += Math.abs(velocity.x)*1.3f*Main.delta*5; 
	}
	else if(!grappleFlying)
	{
		if(Math.abs(shakeWeapon2-27)>0.1f)shakeWeapon2 = shakeWeapon2+(27-shakeWeapon2)*Main.delta*20;
	}
	else
	{
		if(Math.abs(shakeWeapon2-70)>0.1f)shakeWeapon2 = shakeWeapon2+(70-shakeWeapon2)*Main.delta*20;
	}
	
	/*if(!press && !bigitem)
	{
		if(shakeWeapon2>90)shakeWeapon = -(90-30)+5;
		else shakeWeapon = -(shakeWeapon2-30)+5;
		if(arm2static)shakeWeapon-=7;
		if(!isTurned)shakeWeapon = -shakeWeapon;
	}*/
	
	//if(!press && !bigitem && isTurned)currentAngle = Tools.fLerpAngle(currentAngle, -75, 30);
	//else if(!press && !bigitem)currentAngle = Tools.fLerpAngle(currentAngle, -105, 30);;
	
	tempangle = currentAngle;
		//shoulderCoord = new Vector2(armWeapon.getX()+armWeapon.getOriginX(), armWeapon.getY()+armWeapon.getOriginY());
		
	calculateVectors();
	
	if(isTurned)
	{
		arm2.setRotation(shakeWeapon2);
	}
	else
	{
		arm2.setRotation(-shakeWeapon2);
	}
	
	if(isTurned)
	{
		arm.setRotation(75+tempangle+shakeWeapon);
		armHang.setRotation(75+tempangle+shakeWeapon);
		armWeapon.setRotation(75+tempangle+shakeWeapon);
	}
	else
	{
		arm.setRotation(105+tempangle+shakeWeapon);
		armHang.setRotation(105+tempangle+shakeWeapon);
		armWeapon.setRotation(105+tempangle+shakeWeapon);
	}
	
	
	if(isTool)
	{
		if(AllTools.instance.getType(currentCellID).weaponTexture!=0 && !AllTools.instance.getType(currentCellID).equipment)
		{
			armWeapon.setRegion(AllTools.instance.getRegion(AllTools.instance.getType(currentCellID).weaponTexture));
		}
	}
	
	if(!canJump)
	{
		bodyParts[legsID].setRegion(velocity.y < -horspeed*0.2f ? legJump3:velocity.y > horspeed*0.2f ? legJump1 : legJump2);
	}
	else 
	{
		bodyParts[legsID].setRegion(velocity.x != 0 && !icy ? legAnimWalk.getKeyFrame(animationTime) : legStill);
	}
	
	float MousePosY = Tools.getAbsoluteMouse().y;
	
	if(MousePosY-(y+h/1.4f)>0)
	{
		bodyParts[eyesID].setRegion(eyes[0]);
	}
	else
	{
		bodyParts[eyesID].setRegion(eyes[1]);
	}
}

public void calculateVectors()
{
	tempangle = currentAngle;
	
	launcherOffset.clamp(AllTools.instance.getType(currentCellID).launcherDistance, AllTools.instance.getType(currentCellID).launcherDistance);
	
	if(isTurned)
	{
		tempangle+=AllTools.instance.getType(currentCellID).angle;
		launcherOffset.setAngle(tempangle+ AllTools.instance.getType(currentCellID).launcherAngle+75+shakeWeapon);
		//hookOffset.setAngle(tempangle+75-100+shakeWeapon);
	}
	else
	{
		tempangle-=AllTools.instance.getType(currentCellID).angle;
		launcherOffset.setAngle(tempangle+ -AllTools.instance.getType(currentCellID).launcherAngle+285+shakeWeapon);
		//hookOffset.setAngle(tempangle+285+100+shakeWeapon);
	}
	
	if(isTurned)
	{
		shoulderCoord = new Vector2(x+32, y+61);
	}
	else
	{
		shoulderCoord = new Vector2(x+47, y+61);
	}
	
	middle = new Vector2(x+bodyParts[0].getOriginX(), y+bodyParts[0].getOriginY());
	hookCoord = new Vector2(middle.x, middle.y+6);
	launcherCoord = new Vector2(shoulderCoord.x+launcherOffset.x, shoulderCoord.y+launcherOffset.y);
	velocityCoord = new Vector2(Tools.getAbsoluteMouse().x-launcherCoord.x,Tools.getAbsoluteMouse().y-launcherCoord.y).setLength(400);
	velocityCoord2 = new Vector2(launcherCoord.x-shoulderCoord.x, launcherCoord.y-shoulderCoord.y).setLength(100);
	velocityHook = new Vector2(Tools.getAbsoluteMouse().x-hookCoord.x,Tools.getAbsoluteMouse().y-hookCoord.y).setLength(400);
}

public void SetArmors(boolean isTurned)
{
	Helmet.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):-Math.abs(bodyParts[0].getScaleX()), bodyParts[0].getScaleY());
	Armor.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):-Math.abs(bodyParts[0].getScaleX()), bodyParts[0].getScaleY());
	Leggins.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):-Math.abs(bodyParts[0].getScaleX()), bodyParts[0].getScaleY());
	Arms.setScale(isTurned ? Math.abs(bodyParts[0].getScaleX()):!isTurned ?-Math.abs(bodyParts[0].getScaleX()):Arms.getScaleX(), bodyParts[0].getScaleY());
	
	Helmet.setPosition(x, y-3);
	Armor.setPosition(x, y);
	Leggins.setPosition(x, y);
	Arms.setPosition(x, y);

	if(GameScreen.inventory.Helmet().helmet)
	{
		Helmet.setRegion(AllTools.instance.getRegion(GameScreen.inventory.Helmet().weaponTexture));
	}
	
	
	if(GameScreen.inventory.Armor().armor)
	{
		Armor.setRegion(AllTools.instance.getRegion(GameScreen.inventory.Armor().weaponTexture));
	}
	
	
	if(GameScreen.inventory.Arms().arms)
	{
		Arms.setRegion(AllTools.instance.getRegion(GameScreen.inventory.Arms().weaponTexture));
	}
	
	
	if(GameScreen.inventory.Legs().legs)
	{
		//Leggins.setRegion(AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture));
		if(!canJump)
		{
			Leggins.setRegion(velocity.y < -horspeed*0.2f ? AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture)[11]:velocity.y > horspeed*0.2f ? AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture)[9] : AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture)[10]);
		}
		else 
		{
			Leggins.setRegion(velocity.x != 0 && !icy ? AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture)[Tools.floor((animationTime/0.085f)%8+1)] : AllTools.instance.getLeggins(GameScreen.inventory.Legs().weaponTexture)[0]);
		}
	}
}


public void refreshSelect()
{
	if(currentCellState > 9)
	{
		currentCellState = 0;
		int id = GameScreen.select.getSelectorId(currentCellState);
		GameScreen.select.setSelected(currentCellState);
		currentCellID = id;
	}
	else if(currentCellState < 0)
	{
		currentCellState = 9;
		int id = GameScreen.select.getSelectorId(currentCellState);
		GameScreen.select.setSelected(currentCellState);
		currentCellID = id;
	}
	else
	{
		int id = GameScreen.select.getSelectorId(currentCellState);
		GameScreen.select.setSelected(currentCellState);
		currentCellID = id;
	}
}

public void Hurt(float damage, int x, int y, float x2, float y2)
{
	if(!Parameters.i.godmode)
	{
		damage*=(100/defense);
		health-=damage;
		velocity.x = -x*80;
		velocity.y = y*80;
		if(!transformed)stuck = true;
		if(health>0)hurt = true;
		GameScreen.emmiter.addnewText(Math.max(Tools.floor(damage),1), x2, y2);
	}
}

public float getAngle(Vector2 base, Vector2 target) {
    float angle = (float) Math.toDegrees(Math.atan2(target.y - base.y, target.x - base.x));

    if(angle < 0){
        angle += 360;
    }

    return angle;
}

public void graphicDraw(SpriteBatch batch, float alphamultiply)
{
	Vector3 color = Tools.getShadowColor(Tools.floor ((x+w/2)/16),Tools.floor ((y+h/2)/16));
	
	bodyParts[legsID].setColor( color.x, color.y, color.z, alphamultiply);
	bodyParts[bodyID].setColor( color.x, color.y, color.z, alphamultiply);
	bodyParts[headID].setColor( color.x, color.y, color.z, alphamultiply);
	bodyParts[hairsID].setColor( color.x, color.y, color.z, alphamultiply);
	bodyParts[eyesID].setColor( color.x, color.y, color.z,  alphamultiply);
	Helmet.setColor( color.x, color.y, color.z,  alphamultiply);
	Armor.setColor( color.x, color.y, color.z, alphamultiply);
	Arms.setColor( color.x, color.y, color.z, alphamultiply);
	Leggins.setColor( color.x, color.y, color.z, alphamultiply);
	
	armHang.setColor( color.x, color.y, color.z, alphamultiply);
	armWeapon.setColor( color.x, color.y, color.z,  alphamultiply);
	arm.setColor( color.x, color.y, color.z,  alphamultiply);
	arm2.setColor( color.x, color.y, color.z,  alphamultiply);
		
	//bodyParts[2].draw(batch);

	arm2.draw(batch);
	Arms.setPosition(arm2.getX(), arm2.getY());
	Arms.setRotation(arm2.getRotation());
	if(GameScreen.inventory.Arms().arms)Arms.draw(batch);
	
	bodyParts[bodyID].draw(batch);
	bodyParts[legsID].draw(batch);
	if(GameScreen.inventory.Armor().armor)Armor.draw(batch);
	
	if(GameScreen.inventory.Legs().legs)Leggins.draw(batch);
	bodyParts[headID].draw(batch);
	bodyParts[eyesID].draw(batch);
	
	
	if(!GameScreen.inventory.Helmet().helmet)bodyParts[hairsID].draw(batch);
	
	if(GameScreen.inventory.Helmet().helmet)Helmet.draw(batch);
	
	if(isTool && AllTools.instance.getType(currentCellID).weaponTexture!=0 && !AllTools.instance.getType(currentCellID).equipment)
		armWeapon.draw(batch);
	else if(!isTool || !AllTools.instance.getType(currentCellID).invisible)
		armHang.draw(batch);
	
	arm.draw(batch);
	Arms.setPosition(arm.getX(), arm.getY());
	Arms.setRotation(arm.getRotation());
	if(GameScreen.inventory.Arms().arms)Arms.draw(batch);
}

public boolean transform(int id)
{
	if(id<0)
	{
		return untransform();
	}
	transformList.get(id).hitbox.update(transformList.get(id).x,transformList.get(id).y);
	if( (!transformed || id!=Parameters.i.currentTransform) && !transformList.get(id).hitbox.TestCollisionsAll())
	{
		nextTransform = id;
		transformingin = true;
		transformingout = false;
		transformtime = Main.time;
	}
	return false;
}

public boolean untransform()
{
	hitbox.update(x,y);
	if(transformed && !hitbox.TestCollisionsAll())
	{
		nextTransform = -1;
		transformingin = true;
		transformingout = false;
		transformtime = Main.time;
		return true;
	}
	return false;
}

 @Override
 public void draw(SpriteBatch batch)
 {
	if(health<=0)isDead = true;
	
	if(transformed || isDead)grapple.playerAttached = false;
	
	if(!transformed)
	{
		if(!grapple.playerAttached)
		{
			if(GameScreen.inventory.Grapple().grapple && Inputs.instance.middleOrAPressed)
			GameScreen.entities.projectiles.AddGrapple(GameScreen.player.hookCoord.x, GameScreen.player.hookCoord.y, GameScreen.player.velocityHook.x, GameScreen.player.velocityHook.y, GameScreen.inventory.Grapple().grappleDistance, GameScreen.inventory.Grapple().grappleTex, GameScreen.inventory.Grapple().grappleTexRope);
		}
		
		Update();
		
		
		detach = false;
		transoffy = 0;
		transoffx = 0;
		
		if(transformingin)
		{
			graphicDraw(batch,1);
			Shaders.instance.setWhiteShader();
			graphicDraw(batch,Math.min(1, (Main.time-transformtime)/(transformrate/4)));
			Shaders.instance.setShadowShader();
			
			if(Main.time-transformtime>(transformrate/2))
			{
				transformingin = false;
				transformingout = true;
				Parameters.i.currentTransform = nextTransform;
				if(Parameters.i.currentTransform>=0)
				{
					transformList.get(Parameters.i.currentTransform).hitbox.update(transformList.get(Parameters.i.currentTransform).x,transformList.get(Parameters.i.currentTransform).y);
					if(transformList.get(Parameters.i.currentTransform).hitbox.TestCollisionsAll())
					{
						transformed = false;
						nextTransform = -1;
					}
					else
					{
						transformed = true;
					}
				}
				Parameters.i.currentTransform = nextTransform;
			}
		}
		else if(transformingout)
		{
			graphicDraw(batch,1);
			Shaders.instance.setWhiteShader();
			graphicDraw(batch,Math.min(1,Math.max(0, (-Main.time+transformtime+transformrate)/(transformrate/4))));
			Shaders.instance.setShadowShader();
			
			if(Main.time-transformtime>(transformrate))
			{
				transformingout = false;
			}
		}
	}
	else
	{
		transoffy = hitbox.min-transformList.get(Parameters.i.currentTransform).hitbox.min;
		transoffx = -transformList.get(Parameters.i.currentTransform).manualoffx;
		detach = true;
		
		inputTransform();
		transformList.get(Parameters.i.currentTransform).update();
		transformList.get(Parameters.i.currentTransform).draw(batch);
		x = transformList.get(Parameters.i.currentTransform).x-transoffx;
		y = transformList.get(Parameters.i.currentTransform).y-hitbox.min+transformList.get(Parameters.i.currentTransform).hitbox.min;
		velocity.x = transformList.get(Parameters.i.currentTransform).vx;
		velocity.y = transformList.get(Parameters.i.currentTransform).vy;
		
		
		if(transformingin)
		{
			Shaders.instance.setWhiteShader();
			batch.setColor(1, 1, 1,Math.min(1,(Main.time-transformtime)/(transformrate/4)));
			transformList.get(Parameters.i.currentTransform).graphicDraw(batch);
			batch.setColor(Color.WHITE);
			Shaders.instance.setShadowShader();
			
			if(Main.time-transformtime>(transformrate/2))
			{
				transformingin = false;
				transformingout = true;
				
				if(Parameters.i.currentTransform!=nextTransform)
				{
					if(nextTransform<0)
					{
						hitbox.update(x,y);
						if(!hitbox.TestCollisionsAll())
						{
							transformed = false;
						}
						else
						{
							nextTransform = Parameters.i.currentTransform;
						}
					}
					else
					{
						transformList.get(nextTransform).hitbox.update(transformList.get(nextTransform).x,transformList.get(nextTransform).y);
						if(!transformList.get(nextTransform).hitbox.TestCollisionsAll())
						{
							transformed = true;
						}
						else
						{
							nextTransform = Parameters.i.currentTransform;
						}
					}
					
				}
				Parameters.i.currentTransform = nextTransform;
			}
		}
		else if(transformingout)
		{
			Shaders.instance.setWhiteShader();
			batch.setColor(1, 1, 1, Math.min(1,Math.max(0, (-Main.time+transformtime+transformrate)/(transformrate/4))));
			transformList.get(Parameters.i.currentTransform).graphicDraw(batch);
			batch.setColor(Color.WHITE);
			Shaders.instance.setShadowShader();
			
			if(Main.time-transformtime>(transformrate))
			{
				transformingout = false;
			}
		}
	}
	
	for(int i = 0; i<transformList.size(); i++)
	{
		transformList.get(i).x = x-transformList.get(i).manualoffx;
		transformList.get(i).y = y+hitbox.min-transformList.get(i).hitbox.min;
		transformList.get(i).vx = velocity.x;
		transformList.get(i).vy = velocity.y;
	}
	
	
	
	if(Parameters.i.drawhitbox)
	{
		Color old = batch.getColor();
		batch.setColor(1, 1, 1, 0.6f);
		ArrayList<Rectangle>recs = getHitRect();
		for(int i = 0; i<recs.size(); i++)
		{
			batch.draw(TextureManager.instance.blank, (x+transoffx)+recs.get(i).x, (y+transoffy)+recs.get(i).y, recs.get(i).width, recs.get(i).height);
		}
		batch.setColor(old);
	}
	
	Map.instance.lights.tempLight(Tools.floor(middle.x), Tools.floor(middle.y), 0.1f,  0.1f,  0.1f);
	
	//super.draw(batch);
 }
 
 @Override
 public float getX()
	{
		return x;
	}
 @Override
 public float getY()
	{
		return y;
	}
 @Override
 public void setX(float xii)
	{
	 x = xii;
	}
 @Override
 public void setY(float xii)
	{
	 y = xii;
	}

}
