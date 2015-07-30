package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.sandbox.mobs.BigInsecte;
import com.hexabeast.sandbox.mobs.Hornet;
import com.hexabeast.sandbox.mobs.Mob;
import com.hexabeast.sandbox.mobs.PNJ;
import com.hexabeast.sandbox.mobs.Wolf;

public class Player{
	
	
	public ArrayList<Mob> transformList = new ArrayList<Mob>();
	public Wolf transformWolf;
	public BigInsecte transformBigInsecte;
	public Hornet transformHornet;
	public PNJ PNJ;
	
	public boolean transformed = false;
	
	public Mob currentForm;
	
	public float transformrate = 0.6f;
	public float transformtime = 0;
	public boolean transformingin = false;
	public boolean transformingout = false;
	public int nextTransform = 0;
	
	public float transoffy = 0;
	public float transoffx = 0;
	
	public  Sprite[] bodyParts = new Sprite[5];
	public Vector2 initialPos;
	
	public int currentCellID;
	public int currentCellState = 0;
	
	public boolean isDead;
	
	public boolean hurt = false;
	
	
	public Player()
	{
		PNJ = new PNJ();
		PNJ.manual = true;
		PNJ.isMain = true;
		PNJ.speedx = 250;
		PNJ.speedy = 1000;
		transformList.add(PNJ);
		
		transformWolf = new Wolf();
		transformWolf.manual = true;
		transformWolf.speedx*=1.8f;
		transformWolf.power = 20;
		transformWolf.health = 100;
		transformList.add(transformWolf);
		
		transformBigInsecte = new BigInsecte();
		transformBigInsecte.manual = true;
		transformBigInsecte.speedx = 400;
		transformBigInsecte.speedy = 400;
		transformBigInsecte.power = 20;
		transformBigInsecte.health = 100;
		transformList.add(transformBigInsecte);
		
		transformHornet = new Hornet();
		transformHornet.manual = true;
		transformHornet.speedx = 300;
		transformHornet.speedy = 1300;
		transformHornet.power = 20;
		transformHornet.health = 100;
		transformList.add(transformHornet);

		initialPos = new Vector2(Map.instance.width/2, Map.instance.randomHeight-40);
		if(Main.devtest)initialPos = new Vector2(2, Map.instance.randomHeight-40);
		
		while(!Map.instance.mainLayer.blocs[Tools.floor(initialPos.x)][Tools.floor(initialPos.y-5)].collide)
		{
			initialPos.y--;
		}
		initialPos.x*=Map.instance.mainLayer.getTileWidth();
		initialPos.y*=Map.instance.mainLayer.getTileWidth();
		currentCellState = 0;
	}
	
	public void setPosition(float x, float y)
	{
		PNJ.x = x;
		PNJ.y = y;
		for(int i = 0; i<transformList.size(); i++)
		{
			transformList.get(i).x = x+transoffx;
			transformList.get(i).y = y+PNJ.hitbox.min-transformList.get(i).hitbox.min;
			transformList.get(i).vx = PNJ.vx;
			transformList.get(i).vy = PNJ.vy;
		}
	}
	
	public ArrayList<Rectangle> getHitRect()
	{
		return transformList.get(Parameters.i.currentTransform).hitrect.getRects(transformList.get(Parameters.i.currentTransform).isTurned);
	}
	
	public boolean isTouched(float x, float y)
	{
		return (Math.abs(x-PNJ.x-PNJ.width/2)<PNJ.width/3.5f && Math.abs(y-PNJ.y-PNJ.height/2)<PNJ.height/2.2f);
	}
	
	public void Update()
	{
		if(!Main.pause)
		{
			if(AllEntities.getType(Tools.floor(PNJ.middle.x/16), Tools.floor(PNJ.middle.y/16)-1) == AllEntities.furnituretype)
			{
				Furniture f = (Furniture)AllEntities.getEntity(Tools.floor(PNJ.middle.x/16), Tools.floor(PNJ.middle.y/16)-1);
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
	transformList.get(Parameters.i.currentTransform).preinput();
	
	if(GameScreen.inventory.Helmet().helmet)transformList.get(Parameters.i.currentTransform).setEquipment(GameScreen.inventory.Helmet());
	else transformList.get(Parameters.i.currentTransform).setHelmet(AllTools.instance.defaultType);
	
	if(GameScreen.inventory.Armor().armor)transformList.get(Parameters.i.currentTransform).setEquipment(GameScreen.inventory.Armor());
	else transformList.get(Parameters.i.currentTransform).setArmor(AllTools.instance.defaultType);
	
	if(GameScreen.inventory.Legs().legs)transformList.get(Parameters.i.currentTransform).setEquipment(GameScreen.inventory.Legs());
	else transformList.get(Parameters.i.currentTransform).setLeggins(AllTools.instance.defaultType);
	
	if(GameScreen.inventory.Arms().arms)transformList.get(Parameters.i.currentTransform).setEquipment(GameScreen.inventory.Arms());
	else transformList.get(Parameters.i.currentTransform).setGlove(AllTools.instance.defaultType);

	
	if(Inputs.instance.Q)transformList.get(Parameters.i.currentTransform).goLeft();
	else if(Inputs.instance.D)transformList.get(Parameters.i.currentTransform).goRight();
	else transformList.get(Parameters.i.currentTransform).goStandX();
	
	if(Inputs.instance.S)transformList.get(Parameters.i.currentTransform).goDown();
	else if(Inputs.instance.Z)transformList.get(Parameters.i.currentTransform).goUp();
	else transformList.get(Parameters.i.currentTransform).goStandY();

	if(Inputs.instance.leftmousedown && !Main.pause)transformList.get(Parameters.i.currentTransform).goClickLeftInstant();
	if(Inputs.instance.rightmousedown && !Main.pause)transformList.get(Parameters.i.currentTransform).goClickRightInstant();
	
	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Main.pause)transformList.get(Parameters.i.currentTransform).goClickLeftPressed();
	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Main.pause)transformList.get(Parameters.i.currentTransform).goClickRightPressed();
	
	if(Inputs.instance.space)transformList.get(Parameters.i.currentTransform).goJump();
	
	if(GameScreen.inventory.Grapple().grapple)transformList.get(Parameters.i.currentTransform).setHook(GameScreen.inventory.Grapple());
	else transformList.get(Parameters.i.currentTransform).setHook(AllTools.instance.defaultType);
	
	if(Inputs.instance.middleOrAPressed)transformList.get(Parameters.i.currentTransform).goHook();
	
	transformList.get(Parameters.i.currentTransform).setItemId(currentCellID);
	transformList.get(Parameters.i.currentTransform).setVisorPos(Tools.getAbsoluteMouse());
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

public float getAngle(Vector2 base, Vector2 target) {
    float angle = (float) Math.toDegrees(Math.atan2(target.y - base.y, target.x - base.x));

    if(angle < 0){
        angle += 360;
    }

    return angle;
}

public boolean transform(int id)
{
	transformList.get(id).hitbox.update(transformList.get(id).x,transformList.get(id).y);
	if( (id!=Parameters.i.currentTransform) && !transformList.get(id).hitbox.TestCollisionsAll())
	{
		nextTransform = id;
		transformingin = true;
		transformingout = false;
		transformtime = Main.time;
	}
	return false;
}

public void Hurt(float d, float immortality, float x, float y)
{
	hurt = true;
	PNJ.Hurt(d, immortality, x, y);
}

 public void draw(SpriteBatch batch)
 { 
	if(transformList.get(Parameters.i.currentTransform).health<=0 || transformList.get(Parameters.i.currentTransform).isDead)isDead = true;
	
	if(transformed || isDead)PNJ.hook.playerAttached = false;
	
	transoffy = PNJ.hitbox.min-transformList.get(Parameters.i.currentTransform).hitbox.min;
	transoffx = -transformList.get(Parameters.i.currentTransform).manualoffx;
	//detach = true;
	
	inputTransform();
	transformList.get(Parameters.i.currentTransform).update();
	transformList.get(Parameters.i.currentTransform).draw(batch);
	
	if(Parameters.i.currentTransform!=0)
	{
		PNJ.x = transformList.get(Parameters.i.currentTransform).x-transoffx;
		PNJ.y = transformList.get(Parameters.i.currentTransform).y-PNJ.hitbox.min+transformList.get(Parameters.i.currentTransform).hitbox.min;
		PNJ.vx = transformList.get(Parameters.i.currentTransform).vx;
		PNJ.vy = transformList.get(Parameters.i.currentTransform).vy;
		PNJ.health = transformList.get(Parameters.i.currentTransform).health;
	}
	
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
				transformList.get(nextTransform).hitbox.update(transformList.get(nextTransform).x,transformList.get(nextTransform).y);
				if(!transformList.get(nextTransform).hitbox.TestCollisionsAll())
				{
					if(nextTransform!=0)
					transformed = true;
					else transformed = false;
				}
				else
				{
					nextTransform = Parameters.i.currentTransform;
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
	
	for(int i = 1; i<transformList.size(); i++)
	{
		transformList.get(i).x = PNJ.x-transformList.get(i).manualoffx;
		transformList.get(i).y = PNJ.y+PNJ.hitbox.min-transformList.get(i).hitbox.min;
		transformList.get(i).vx = PNJ.vx;
		transformList.get(i).vy = PNJ.vy;
	}
	
	Map.instance.lights.tempLight(Tools.floor(PNJ.middle.x), Tools.floor(PNJ.middle.y), 0.1f,  0.1f,  0.1f);
	
	
	if(Parameters.i.currentTransform!=0)transformed = true;
	else transformed = false;
	currentForm = transformList.get(Parameters.i.currentTransform);
 }

}
