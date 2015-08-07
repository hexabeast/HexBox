package com.hexabeast.sandbox.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hexabeast.hexboxserver.NInputUpdate;
import com.hexabeast.sandbox.Inputs;
import com.hexabeast.sandbox.Main;

public class NetworkMob {
	public Mob mob;
	public NInputUpdate n;
	
	public NetworkMob(Mob m)
	{
		mob = m;
		n = new NInputUpdate();
	}
	
	public void update()
	{
		mob.preinput();
		
		if(n.Q)mob.goLeft();
		else if(n.D)mob.goRight();
		else mob.goStandX();
		
		if(n.S)mob.goDown();
		else if(n.Z)mob.goUp();
		else mob.goStandY();

		//if(Inputs.instance.leftmousedown && !Main.pause)mob.goClickLeftInstant();
		//if(Inputs.instance.rightmousedown && !Main.pause)mob.goClickRightInstant();
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Main.pause)mob.goClickLeftPressed();
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Main.pause)mob.goClickRightPressed();
		
		if(Inputs.instance.space)mob.goJump();
		
		
		
		if(Inputs.instance.middleOrAPressed)mob.goHook();

		mob.setVisorPos(n.mousePos);
		
		mob.update();
	}
	
	public void draw()
	{
		mob.draw(Main.batch);
	}
}
