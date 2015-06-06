package com.hexabeast.sandbox;

public class BlocType {
public int Id;
public boolean unbreakable;
public boolean collide;
public boolean air;
public boolean transparent;
public float lightIntensity;
public float friction;
public int durability;
public float pitch;
public String name;
public boolean fertile;
public String description;
public int soundType;
public boolean shadow;
public boolean needBack;
public boolean lightFull;
public boolean stdCheck;
public boolean fluid;
public boolean waterOccupy;
public boolean animated;
public boolean oldTexture;
public boolean artificial;
public BlocType merge;
public BlocType next;
public float[] lightColor;
public boolean torch = false;
public boolean dirtish = false;
public int dirtishpriority = 10000;
public int dirtishtype = 0;
public boolean nonblock = false;
ColorString rarity;

	public BlocType() {
		rarity = Constants.rarity1;
		shadow = false;
		animated = false;
		waterOccupy = false;
		fluid = false;
		stdCheck = true;
		needBack = false;
		lightFull = false;
		soundType = 0;
		fertile = false;
		friction = 1;
		name = "No name";
		description ="No description available";
		pitch = 1;
		durability = 1;
		unbreakable = false;
		collide = true;
		air = false;
		transparent = false;
		oldTexture = false;
		artificial = false;
		merge = this;
	}
}
