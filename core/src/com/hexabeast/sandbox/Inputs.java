package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.hexabeast.hexboxserver.NInputRightLeft;
import com.hexabeast.hexboxserver.NInputUpDown;
import com.hexabeast.hexboxserver.NInputUpdate;
import com.hexabeast.hexboxserver.Nclick;

public class Inputs implements InputProcessor{
	public static Inputs instance;
	public boolean mouseup;
	private boolean tomouseup;
	
	public boolean mousedown;
	private boolean tomousedown;
	
	public boolean leftmousedown;
	private boolean toleftmousedown;
	
	public boolean rightmousedown;
	private boolean torightmousedown;
	
	public boolean leftmouseup;
	private boolean toleftmouseup;
	
	public boolean rightmouseup;
	private boolean torightmouseup;
	
	public boolean middleOrAPressed;
	public boolean tomiddleOrAPressed;
	
	public boolean Q = false;
	public boolean Z = false;
	public boolean S = false;
	public boolean D = false;
	public boolean shift = false;
	public boolean space = false;
	public boolean spacePressed = false;
	
	public NInputUpdate Ninput = new NInputUpdate();
	
	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tomousedown = true;
		
		if (button == Input.Buttons.RIGHT)torightmousedown = true;
		if (button == Input.Buttons.LEFT)toleftmousedown = true;
		
		if(button == Input.Buttons.MIDDLE)tomiddleOrAPressed = true;
		
		if(Main.ingame)GameScreen.updatePauseClick();
		if(Main.ingame && !Main.pause)
		{
			Nclick iu = new Nclick();
			if (button == Input.Buttons.RIGHT) 
			{
				GameScreen.PutOneItem(true);
				iu.left = true;
			}
			else if(button == Input.Buttons.LEFT)
			{
				GameScreen.PutOneItem(false);
				iu.right = true;
			}
			
			if(NetworkManager.instance.online)
			{
				NetworkManager.instance.sendTCP(iu);
			}
		}
		return false;
	}


	public void update()
	{
		if(NetworkManager.instance.online)
		{
			Ninput.Z = Z;
			Ninput.Q = Q;
			Ninput.S = S;
			Ninput.D = D;
			//Ninput.A = middleOrAPressed;
			Ninput.Left = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
			Ninput.Right = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
			
			Ninput.Space = spacePressed;
			Ninput.mousePos = Tools.getAbsoluteMouse();
		}
		
		if(tomouseup)
		{
			mouseup = true;
			tomouseup = false;
		}
		else
		{
			mouseup = false;
		}
		
		if(toleftmouseup)
		{
			leftmouseup = true;
			toleftmouseup = false;
		}
		else
		{
			leftmouseup = false;
		}
		
		if(tomiddleOrAPressed)
		{
			middleOrAPressed = true;
			tomiddleOrAPressed = false;
		}
		else
		{
			middleOrAPressed = false;
		}
		
		if(torightmouseup)
		{
			rightmouseup = true;
			torightmouseup = false;
		}
		else
		{
			rightmouseup = false;
		}
		
		if(tomousedown)
		{
			mousedown = true;
			tomousedown = false;
		}
		else
		{
			mousedown = false;
		}

		
		if(toleftmousedown)
		{
			leftmousedown = true;
			toleftmousedown = false;
		}
		else
		{
			leftmousedown = false;
		}
		
		if(torightmousedown)
		{
			rightmousedown = true;
			torightmousedown = false;
		}
		else
		{
			rightmousedown = false;
		}
		
	}
	
	public void updateLate()
	{
		if(space)space = false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		tomouseup = true;
		if (button == Input.Buttons.RIGHT)torightmouseup = true;
		if (button == Input.Buttons.LEFT)toleftmouseup = true;
		return false;}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) 
	{
		if(Main.ingame && !Main.pause)
		{
			GameScreen.player.currentCellState+=amount;
			GameScreen.player.refreshSelect();
		}
		return false;
	}
	
	public void placeTorch()
	{
		if(Main.ingame && !Main.pause)
		{
			int tid = GameScreen.inventory.presentorch(1);
			if(tid>=0 && ModifyTerrain.instance.UseAbsolute(GameScreen.player.PNJ.middle.x, GameScreen.player.PNJ.middle.y,  tid, GameScreen.player.currentCellState, Map.instance.mainLayer,Main.time,false))
			GameScreen.inventory.remove(tid, 1);
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		if(Main.ingame && !Main.pause)
		{
			switch(keycode){
			case Keys.V:
				if(!NetworkManager.instance.online)Map.instance.lights.switchDayBegin(-0.5f);
				break;
			case Keys.B:
				if(!NetworkManager.instance.online)Map.instance.lights.switchDayBegin(0.5f);
				break;
				
			case Keys.SPACE:
				space = true;
				spacePressed = true;
				break;
			case Keys.SHIFT_LEFT:
				shift = true;
				break;	
				
			case Keys.C:
				GameScreen.inventory.Craft();
				break;
			case Keys.A:
				tomiddleOrAPressed = true;
				break;
			case Keys.D:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputRightLeft(true,true));
				D = true;
				break;
			case Keys.Q:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputRightLeft(false,true));
				Q = true;
				break;
			case Keys.Z:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(true,true));
				Z = true;
				break;
			case Keys.W:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(true,true));
				Z = true;
				break;	
			case Keys.T:
				placeTorch();
				
				break;
			case Keys.S:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(false,true));
				S = true;
				break;	
			case Keys.NUM_0:
				GameScreen.player.currentCellState = 9;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_1:
				GameScreen.player.currentCellState = 0;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_2:
				GameScreen.player.currentCellState = 1;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_3:
				GameScreen.player.currentCellState = 2;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_4:
				GameScreen.player.currentCellState = 3;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_5:
				GameScreen.player.currentCellState = 4;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_6:
				GameScreen.player.currentCellState = 5;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_7:
				GameScreen.player.currentCellState = 6;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_8:
				GameScreen.player.currentCellState = 7;
				GameScreen.player.refreshSelect();
				break;
			case Keys.NUM_9:
				GameScreen.player.currentCellState = 8;
				GameScreen.player.refreshSelect();
				break;
			}
		}
		
		
		
		return false;
	}
	

	@Override
	public boolean keyUp(int keycode) {
		if(Main.ingame && keycode == Keys.ESCAPE)Main.pause = !Main.pause;
		if(Main.ingame && keycode == Keys.P)Main.pause = !Main.pause;
		if(Main.ingame && !Main.pause)
		{
			switch(keycode){
			
			case Keys.V:
				Map.instance.lights.switchDayEnd();
				break;
			case Keys.I:
				GameScreen.inventory.ToggleHide();
				GameScreen.select.toggleOffset();
				break;
			case Keys.L:
				if(!NetworkManager.instance.online)Parameters.i.cheatMagic = !Parameters.i.cheatMagic;
				break;
			case Keys.K:
				if(!NetworkManager.instance.online)
				{
					if(Parameters.i.deltaMultiplier >0.5f) Parameters.i.deltaMultiplier = 0.2f;
					else Parameters.i.deltaMultiplier = 1f;
				}
				break;
			case Keys.B:
				Map.instance.lights.switchDayEnd();
				break;
			case Keys.SHIFT_LEFT:
				shift = false;
				break;
			case Keys.D:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputRightLeft(true,false));
				D = false;
				break;
			case Keys.Q:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputRightLeft(false,false));
				Q = false;
				break;
			case Keys.Z:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(true,false));
				Z = false;
				break;
			case Keys.W:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(true,false));
				Z = false;
				break;	
			case Keys.SPACE:	
				spacePressed = false;
				break;
			case Keys.S:
				if(NetworkManager.instance.online)NetworkManager.instance.sendTCP(new NInputUpDown(false,false));
				S = false;
				break;
			case Keys.O:
				Parameters.i.FBORender = !Parameters.i.FBORender;
			case Keys.H:
				Main.zoom = 1;
				break;
				
			case Keys.ALT_RIGHT:
				Main.noUI = !Main.noUI;
				break;
				
			case Keys.J:
				Parameters.i.noShadow = true;
				Main.zoom = 40;
				break;
			case Keys.M:
				if(!NetworkManager.instance.online)Parameters.i.superman = !Parameters.i.superman;
				break;
			case Keys.R:
				Parameters.i.SwitchQuality();
				//GamePlay.mobs.SpawnRedDino(x, y);
				break;
			case Keys.NUMPAD_9:
				if(!NetworkManager.instance.online)GameScreen.entities.mobs.placeMob(GameScreen.player.PNJ.x, GameScreen.player.PNJ.y+50, 3);
				break;
			case Keys.NUMPAD_8:
				if(!NetworkManager.instance.online)GameScreen.entities.mobs.placeMob(GameScreen.player.PNJ.x, GameScreen.player.PNJ.y+50, 1);
				break;
			case Keys.NUMPAD_7:
				if(!NetworkManager.instance.online)for(int i = 0; i<1; i++)GameScreen.entities.mobs.placeMob(GameScreen.player.PNJ.x, GameScreen.player.PNJ.y+50, 2);
				break;
			case Keys.NUMPAD_1:
				if(!NetworkManager.instance.online)GameScreen.player.transform(1);
				break;
			case Keys.NUMPAD_2:
				if(!NetworkManager.instance.online)GameScreen.player.transform(2);
				break;
			case Keys.NUMPAD_3:
				if(!NetworkManager.instance.online)GameScreen.player.transform(3);
				break;
			case Keys.NUMPAD_0:
				if(!NetworkManager.instance.online)GameScreen.player.transform(0);
				break;
			case Keys.F:
				Parameters.i.noShadow = !Parameters.i.noShadow;
				break;
			case Keys.G:
				GameScreen.player.Hurt(20,0,GameScreen.player.PNJ.middle.x, GameScreen.player.PNJ.middle.y);
				break;
			case Keys.E:
				GameScreen.inventory.ToggleHide();
				GameScreen.select.toggleOffset();
				break;
			}
		}
		return false;
	}
}
