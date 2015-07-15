package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ToolType {
public boolean isUseLess;
public int id;
public float efficiency;
public float damage;
public boolean isShake;
public boolean obstacleProof;
public boolean canCutTrees;
public boolean canAttack;
public int weaponTexture;
public boolean stackable;
public int type = 1;
public float launcherDistance;
public float launcherAngle;
public String name;
public String description;
public int sceptreType;
public float rate;
public boolean armor;
public boolean helmet;
public boolean arms;
public boolean legs;
public float armorDefense;
public float armorSpeed;
public float armorJump;
public boolean equipment;
public float angle;
public int arrowType = 0;
public float uptime = 30;
public float downtime = 30;
public int furnitureType = 0;
public int furnitureWidth = 1;
public int furnitureHeight = 1;
public boolean turnable = false;
public boolean magicprojectile;
public boolean magiclight;
public float range;
public int torchangle = 40;
ColorString rarity;
TextureRegion tex;
public boolean grapple;
public float grappleDistance;
public TextureRegion grappleTex;
public Texture grappleTexRope;
public boolean invisible;
public boolean furnitureContainer = false;
public boolean helmetLight = false;

	public ToolType() 
	{
		invisible = false;
		grappleDistance = 1500;
		grappleTex = TextureManager.instance.grapple;
		grappleTexRope = TextureManager.instance.rope;
		grapple = false;
		magiclight = false;
		rarity = Constants.rarity1;
		magicprojectile = false;
		equipment = false;
		armorDefense = 0;
		armorSpeed = 0;
		armorJump = 0;
		angle = 0;
		armor = false;
		canCutTrees = false;
		canAttack = false;
		obstacleProof = false;
		stackable = true;
		rate = 0.1f;
		sceptreType = 0;
		launcherDistance = 63;
		launcherAngle = -18;
		name = "No name";
		description ="No description available";
		efficiency = 0;
		damage = 0;
		weaponTexture = 0;
		isShake = true;
		isUseLess = false;
		range = ModifyTerrain.range;
	}

}
