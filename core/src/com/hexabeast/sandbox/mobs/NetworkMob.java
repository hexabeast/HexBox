package com.hexabeast.sandbox.mobs;

import com.badlogic.gdx.math.Vector2;
import com.hexabeast.hexboxserver.NInputUpdate;
import com.hexabeast.sandbox.Main;

public class NetworkMob {
	public Mob mob;
	public NInputUpdate n;
	
	public NetworkMob(Mob m)
	{
		mob = m;
		n = new NInputUpdate();
		n.mousePos = new Vector2();
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
		
		if(n.Left && !Main.pause)mob.goClickLeftPressed();
		if(n.Right && !Main.pause)mob.goClickRightPressed();
		
		if(n.A)mob.goHook();

		mob.setVisorPos(new Vector2(n.mousePos.x + mob.x, n.mousePos.y + mob.y));
		
		mob.update();
	}
	
	public void draw()
	{
		mob.draw(Main.batch);
	}
}
