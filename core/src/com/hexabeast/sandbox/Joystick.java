package com.hexabeast.sandbox;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Joystick implements InputProcessor {
	
	boolean isTouching = false;
	Vector2 posTouch = new Vector2();
	int touchID = 0;
	float w = 400;
	float h = 400;
	
	float x = -900;
	float y = -450;
	
	boolean left = false;
	boolean right = false;
	
	Vector2 center = new Vector2(x+w/2,y+h/2);
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(TextureManager.instance.joy,x,y,w,h);
		if(isTouching)
		{
			if(posTouch.x>center.x+50)
			{
				if(!right)
				{
					right = true;
					
				}
			}
			else if(right)
			{
				right = false;
				Inputs.instance.D = false;
			}
			
			if(posTouch.x<center.x-50)
			{
				if(!left)
				{
					left = true;
					Inputs.instance.Q = true;
				}
			}
			else if(left)
			{
				left = false;
				Inputs.instance.Q = false;
			}			
			
			if(posTouch.y>center.y+130)Inputs.instance.space = true;
		}
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 pos = Tools.getAbsoluteMouseUI(screenX,screenY);
		if(!isTouching && isClicked(pos))
		{
			touchID = pointer;
			isTouching = true;
			posTouch = new Vector2(pos.x,pos.y);
		}
		return false;
	}
	
	public boolean isClicked(Vector2 pos)
	{
		if(pos.x>x+50 && pos.x<x+w-50 && pos.y>y+50 && pos.y<y+h+50)return true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(isTouching && pointer == touchID)
		{
			isTouching = false;
			if(right)Inputs.instance.D = false;
			if(left)Inputs.instance.Q = false;
			right = false;
			left = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(isTouching && pointer == touchID)
		{
			Vector2 pos = Tools.getAbsoluteMouseUI(screenX,screenY);
			posTouch = new Vector2(pos.x,pos.y);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
