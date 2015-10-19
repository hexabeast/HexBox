package com.hexabeast.sandbox;

public class Parameters {
	public static Parameters i;
	
	public boolean fullBright = false;
	public boolean noShadow = false;
	public boolean shader = true;
	public int HQ = 5;
	public boolean RGB = true;
	public float daylight = 1.0f;
	public boolean cheatMagic = false;
	public int lightSpeed = 120;
	public int resolution = 2;
	public boolean fullscreen = false;
	public boolean superman = false;
	public boolean ultrarate = false;
	public boolean multithread = false;//TODO
	public float deltaMultiplier = 1;
	public boolean vsync = false;
	public boolean details = true;
	public boolean drawhitbox = false;
	public boolean ultrarange = false;
	public boolean godmode = false;
	public boolean rain = false;
	public boolean background = true;
	public boolean oldtransition = false;
	public int currentTransform = 0;
	public boolean goodmagic = true;
	public int lightDistance = 2;
	public boolean FBORender = false;
	public boolean zoomLock = false;
	public boolean ratio = true;
	public String name = "John";
	
	public int head = 0;
	public int body = 0;
	public int arms = 0;
	public int legs = 0;
	public int eyes = 0;
	public int hair = 0;
	
	Parameters()
	{
		
	}
	
	public void disableCheats()
	{
		
	}

	public void SwitchQuality()
	{
		HQ++;
		if(HQ>=Constants.qualities.length)HQ = 1;
	}
	
}
