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
	public boolean cheat = true;
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
	public boolean wayaxe = false;
	public boolean background = true;
	public boolean oldtransition = false;
	public int currentTransform = -1;
	public boolean goodmagic = true;
	public int lightDistance = 2;
	public boolean FBORender = false;
	
	Parameters()
	{
		
	}
	
	

	public void SwitchQuality()
	{
		HQ++;
		if(HQ>=Constants.qualities.length)HQ = 1;
	}
	
}
