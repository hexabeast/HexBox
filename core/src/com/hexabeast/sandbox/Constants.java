package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {
	public static String[] qualities = {"None", "Low Quality", "Medium Quality", "Medium Quality+", "Shader (Pixel)", "Shader (Realistic)"};
	public static Vec2int[] resolutions = {new Vec2int(800,450), new Vec2int(1024,576),new Vec2int(1280,720),new Vec2int(1600,900),new Vec2int(1680,1050),new Vec2int(1920,1080)};
	public static float invof = 6;
	public static int treelimit = 10000000;
	public static int chestlimit = 20000000;
	public static int moblimit = 30000000;
	public static Vector2 leftVec = new Vector2(-1,0);
	public static Vector2 rightVec = new Vector2(1,0);
	public static Vector2 upVec = new Vector2(0,1);
	public static float upAngle = upVec.angle();
	public static float leftAngle = leftVec.angle();
	public static float rightAngle = rightVec.angle();
	public static float gravity = 1000f;
	
	public static int[] lightDistances = {2,6,12,20,30};
	public static String[] lightDistancesNames = {"Very Low","Low","Medium","High","Very High"};
	
	public static String[] transformNames = {"Human", "Wolf", "Big Insect", "Hornet", "PNJ", "???", "???", "???", "???", "???", "???"};
	
	public static ColorString emptystring = new ColorString("", Color.WHITE);
	public static ColorString rarity1 = new ColorString("Rarity 1", Color.WHITE);
	public static ColorString rarity2 = new ColorString("Rarity 2", Color.WHITE);
	public static ColorString rarity3 = new ColorString("Rarity 3", Color.WHITE);
	public static ColorString rarity4 = new ColorString("Rarity 4", Color.GREEN);
	public static ColorString rarity5 = new ColorString("Rarity 5", new Color(1,0.3f,0.4f,1));
	public static ColorString rarity6 = new ColorString("Rarity 6", new Color(0.4f,0.6f,1,1));
	public static ColorString rarity7 = new ColorString("Rarity 7", Color.ORANGE);
	public static ColorString rarity8 = new ColorString("Rarity 8", Color.RED);
	public static ColorString rarity9 = new ColorString("Rarity 9", Color.YELLOW);
	public static ColorString rarity10 = new ColorString("Rarity 10", new Color(1,0.1f,1,1));
}
