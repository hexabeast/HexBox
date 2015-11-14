package com.hexabeast.sandbox;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Tools {
	
	public static Vector3 color = new Vector3();
	
	public static Vector2 raycastvec = new Vector2();
	public static Vector2 raycastvec2 = new Vector2();
	
	public static Vector2 abpos = new Vector2();
	public static Vector3 worldCoord = new Vector3();
	
	public static Vector2 absMouse = new Vector2();

	public static final int LAST_BYTE = 0x000000FF;
	
	static boolean isDifferentEnough(float a, float b, float c, float d, float e)
	{
		if(Math.abs(a-b)>0.02f)return true;
		if(Math.abs(a-c)>0.02f)return true;
		if(Math.abs(a-d)>0.02f)return true;
		if(Math.abs(a-e)>0.02f)return true;
		return false;
	}
	
	static boolean isFull(BlocType b, int x)
	{
		if(b.artificial)return true;
		if(b.oldTexture)return (x >= AllBlocTypes.instance.oldFull && x < AllBlocTypes.instance.oldFull+3);
		else return (x%10 == AllBlocTypes.instance.full);
	}
	
	public static final <T> void swap (T[] a, int i, int j) 
	{	
		  T t = a[i];
		  a[i] = a[j];
		  a[j] = t;
	}
	
	public static int getIndex(int i, int max)
	{
		if(i<0)i=max+i;
		else if(i>=max)i=i-max;
		return i;
	}
	
	public static final <T> void swap (T[][] a, int i, int j, int k) 
	{
		  
		  T t = a[i][k];
		  a[i][k] = a[j][k];
		  a[j][k] = t;
	}
	
	public static void drawLine(Color col, float w, float x, float y, float x2, float y2)
	{
		Vector2 vec = new Vector2(x2-x,y2-y);
		Color col2 = Main.batch.getColor();
		
		Main.batch.setColor(col);
		Main.batch.draw(TextureManager.instance.blank, x-w/2, y, w/2, 0, w, vec.len(), 1, 1, vec.angle()-90);
		Main.batch.setColor(col2);
	}
	
	public static void drawLine(Texture tex,float x, float y, float x2, float y2,int bonus)
	{
		Vector2 vec = new Vector2(x2-x,y2-y);
		
		 Sprite sprite = new Sprite(tex, 0, 0, tex.getWidth(), (int) vec.len()+bonus);
		 sprite.setColor(Main.batch.getColor());
	     sprite.setOrigin(tex.getWidth()/2, 0);
	     sprite.setPosition(x-tex.getWidth()/2, y);
	     sprite.setRotation( vec.angle()-90);
	     sprite.draw(Main.batch);
		
		//Main.batch.draw(tex, x-w/2, y, w/2, 0, w, vec.len(), 1, 1, vec.angle()-90, 0, 0, tex.getWidth(), (int) vec.len(), false, false);
	}
	
	public static void drawLine(Texture tex,float x, float y, float x2, float y2)
	{
		drawLine(tex,x,y,x2,y2,0);
	}
	
	public static String enabldis(boolean b)
	{
		if(!b) return "Disabling ";
		else return "Enabling ";
	}
	
	public static boolean isIPAdress(String str)
	{
		String[] numbers = str.split("[.]");
		
		if(numbers.length!=4)return false;
		
		for(int i = 0; i<numbers.length; i++)
		{
			try
			{
				int b=Integer.parseInt(numbers[i]);
				if(b>256 ||b<0)return false;
			}
			catch(java.lang.NumberFormatException e)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static void computeAbsoluteMouse()
	{
		absMouse.set(getAbsolutePos(Gdx.input.getX(),Gdx.input.getY()));	
	}
	
	public static float angleCrop(float angle) 
	{
	    while(angle<0)angle+=360;
	    while(angle>360)angle-=360;

	    return angle;
	}
	
	public static Vector2 getAbsoluteMouse()
	{
		return absMouse;	
	}
	
	public static Vector2 getAbsolutePos(float x, float y)
	{
		worldCoord.x = x;
		worldCoord.y = y;
		worldCoord.z = 0;
		GameScreen.camera.unproject(worldCoord);
		abpos.x = worldCoord.x;
		abpos.y = worldCoord.y;
		return abpos;
	}
	
	public static Vector2 getScreenPos(float x, float y)
	{
		Vector3 worldCoord = new Vector3(x,y, 0);	
		GameScreen.camera.project(worldCoord);
		return new Vector2(worldCoord.x, worldCoord.y);
	}
	
	public static Vector2 getAbsoluteMouseUI(float x, float y)
	{
		GameScreen.SwapUI();
		Vector3 worldCoord = new Vector3(x,y, 0);	
		GameScreen.camera.unproject(worldCoord);
		GameScreen.SwapCam();
		return new Vector2(worldCoord.x, worldCoord.y);
	}
	
	public static boolean isClicked(Sprite spr, float degre)
	{
		Vector2 coord = getAbsoluteMouse();
		if((coord.x > spr.getX()-degre && coord.x < spr.getX()+spr.getScaleX()*spr.getWidth()+degre) && (coord.y > spr.getY()-degre && coord.y < spr.getY()+spr.getHeight()*spr.getScaleY()+degre))return true;
		else return false;
	}
	
	public static Vector2 raycast(float x, float y, float xf, float yf, float step)
	{
		boolean end = false;
		raycastvec.x = xf-x;
		raycastvec.y = yf-y;
		
		raycastvec2.x = raycastvec.x;
		raycastvec2.y = raycastvec.y;
		
		for(float i = step; !end; i+=step)
		{
			if(i>=raycastvec.len())
			{
				i = raycastvec.len();
				end = true;
			}
			raycastvec2.clamp(i, i);
			if(Map.instance.mainLayer.getBloc((int)((raycastvec2.x+x)/16), (int)((raycastvec2.y+y)/16)).collide)
			{
				raycastvec.x = (int)((raycastvec2.x+x)/16);
				raycastvec.y = (int)((raycastvec2.y+y)/16);
				return raycastvec;
			}
		}
		raycastvec.x = -1;
		raycastvec.y = -1;
		return raycastvec;
	}
	
	public static Vector2 raycastVec(float x, float y, float xf, float yf, float step)
	{

		
		boolean end = false;
		raycastvec.x = xf;
		raycastvec.y = yf;
		
		raycastvec2.x = raycastvec.x;
		raycastvec2.y = raycastvec.y;
		
		for(float i = step; !end; i+=step)
		{
			if(i>=raycastvec.len())
			{
				i = raycastvec.len();
				end = true;
			}
			raycastvec2.clamp(i, i);
			if(Map.instance.mainLayer.getBloc((int)((raycastvec2.x+x)/16), (int)((raycastvec2.y+y)/16)).collide)
			{
				raycastvec.x = (int)((raycastvec2.x+x)/16);
				raycastvec.y = (int)((raycastvec2.y+y)/16);
				
				//drawLine(Color.WHITE, 3, x, y, raycastvec.x*16+8, raycastvec.y*16+8);
				
				return raycastvec;
			}
		}
		raycastvec.x = -1;
		raycastvec.y = -1;
		
		return raycastvec;
	}
	
	public static Vector2 raycastAngle(float x, float y, float l, float angle, float step)
	{
		raycastvec.x = l;
		raycastvec.y = 0;
		
		raycastvec.setAngle(angle);
		
		return raycast(x,y,x+raycastvec.x,y+raycastvec.y,step);
	}
	
	public static int floor(float f)
	{
		if(f>=0)return (int)f;
		else return (int)f-1;
	}
	
	public static int getMapBounded(int x)
	{
		if(x<0)x = Map.instance.width+x;
		else if(x>Map.instance.width-1)x = 0+(x-(Map.instance.width));
		return x;
	}
	
	public static int getBounded(int x, int mmm)
	{
		if(x<0)x = mmm+x;
		else if(x>mmm-1)x = 0+(x-mmm);
		return x;
	}
	
	public static void checkItems()
	{
		GameScreen.inventory.refreshItemsinv();
		if(GameScreen.inventory.chestopen)
		{
			Furniture chest = GameScreen.inventory.chest;
			for(int i = 0; i<chest.itemsids.length; i++)
			{
				int xs = (int)(i/6);
				int ys = i%6+7;
				chest.itemsids[i] = GameScreen.inventory.invItemsArray[xs][ys].id;
				chest.itemsnumbers[i] = GameScreen.inventory.invItemsArray[xs][ys].number;
			}
		}
		GameScreen.select.refreshSelector();
		GameScreen.player.refreshSelect();
	}
	
	public static Vector3 Lerp(Vector3 vecFrom, Vector3 vecTo)
	{
		if(vecFrom.x-vecTo.x>1000 || vecFrom.x-vecTo.x<-1000 || vecFrom.y-vecTo.y>1000 || vecFrom.y-vecTo.y<-1000)
			return vecTo;
		float speedfactorx = GameScreen.player.PNJ.baseSpeedx/    Math.max(GameScreen.player.PNJ.baseSpeedx, Math.abs(GameScreen.player.PNJ.vx));
		float speedfactory = GameScreen.player.PNJ.baseSpeedy/ Math.max(GameScreen.player.PNJ.baseSpeedy, Math.abs(GameScreen.player.PNJ.vy));
		return new Vector3(vecFrom.x+((vecTo.x-vecFrom.x)/((speedfactorx)*7+2))*Main.delta*60,vecFrom.y+((vecTo.y-vecFrom.y)/((speedfactory)*7+2))*Main.delta*60,0);
	}
	
	public static void drawRect(float x, float y, float w, float h)
	{
		Main.batch.draw(TextureManager.instance.blank, x, y, w, h);
	}
	
	public static Vector3 getShadowColor(int x, int y)
	{
		
		if(!Parameters.i.fullBright)
		{
			if(Parameters.i.RGB)color = Map.instance.lights.getLight(x,y);
			else
			{
				float lum = Map.instance.lights.getLight(x,y,0);
				color.x = lum; color.y = lum; color.z = lum;
			}
			color.x = Math.min(color.x, 1);
			color.y = Math.min(color.y, 1);
			color.z = Math.min(color.z, 1);
		}
		else
		{
			color.x = 1;
			color.y = 1;
			color.z = 1;
		}
		
		return color;
	}
	
	public static String booltoyes(boolean b)
	{
		if(b)return "Yes";
		else return "No";
	}
	
	public static float fLerpAngle(float x, float y, float speed)
	{
		if(y-x>180)y-=360;
		if(x-y>180)x-=360;
		float t = (y-x)*speed*Main.delta;
		if(t<-40*speed*Main.delta)t=-40*speed*Main.delta;
		if(t>40*speed*Main.delta)t=40*speed*Main.delta;
		float result = x+t;
		while(result>360)result-=360;
		while(result<0)result+=360;
		return result;
	}
	
	public static float fLerpAngle(float x, float y, float speed, boolean sens)
	{
		while(sens && y<x)
		{
			y+=360;
		}
		while(!sens && y>x)
		{
			y-=360;
		}
		
		if(Math.abs(x-y)<0.3f)return y;
		
		float result;
		
		float t = (y-x)*speed*Main.delta;
		if(t<-40*speed*Main.delta)
		{
			t=-40*speed*Main.delta;
		}
		else if(t>40*speed*Main.delta)
		{
			t=40*speed*Main.delta;
		}

		result = x+t;
		
		while(result>360)result-=360;
		while(result<0)result+=360;
		return result;
	}
	
	 static void shuffleArray(int[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
}

