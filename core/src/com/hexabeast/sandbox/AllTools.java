package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AllTools {

	public static AllTools instance;
	
	public TextureRegion[] weaponTextures = new TextureRegion[50];
	public TextureRegion[][] legginsTextures = new TextureRegion[50][];
	public int StoneAxeId = 1001;
	public int StdTreeId = 1002;
	public int GreenSpId = 1003;
	public int RedSpId = 1004;
	public int PurpleSpId = 1005;
	public int BlueSpId = 1006;
	public int BlackSpId = 1007;
	public int WoodStickId = 1008;
	public int WoodStickAndStoneId = 1009;
	public int UltraHelmetId = 1010;
	public int UltraArmorId = 1011;
	public int UltraArmsId = 1012;
	public int UltraLegginsId = 1013;
	public int BowId = 1014;
	public int ArrowId = 1015;
	public int SwordId = 1016;
	public int BigSwordId = 1017;
	public int ChestId = 1018;
	public int BedId = 1019;
	public int ChairId = 1020;
	public int Sword1Id = 1021;
	public int Sword2Id = 1022;
	public int Sword3Id = 1023;
	public int Sword4Id = 1024;
	public int Sword5Id = 1025;
	public int Sword6Id = 1026;
	public int Sword7Id = 1027;
	public int Sword8Id = 1028;
	public int Sword9Id = 1029;
	public int AnvilId = 1030;
	public int HookId = 1031;
	public int NormalHookId = 1032;
	public int MiningHelmetId = 1033;
	
	
	public ToolType[] ToolTypes = new ToolType[50];
	
	
	public ToolType defaultType = new ToolType();
	public int UseLess = 0;
	public int Axe = 1;
	public int Tree = 2;
	public int Sceptre = 3;
	public int Bow = 4;
	public int Sword = 5;
	public int Furniture = 6;
	public int Hook = 7;
	
	
	public AllTools() {
		for(int i = 0; i<ToolTypes.length;i++)
		{
			ToolTypes[i] = new ToolType();
			ToolTypes[i].id = 1000+i;
		}
		
		ToolTypes[StoneAxeId-1000].type = Axe; 
		ToolTypes[StoneAxeId-1000].efficiency = 4;
		ToolTypes[StoneAxeId-1000].damage = 4;
		ToolTypes[StoneAxeId-1000].weaponTexture = 1;
		ToolTypes[StoneAxeId-1000].description = "A standard pickaxe";
		ToolTypes[StoneAxeId-1000].name = "Pickaxe";
		ToolTypes[StoneAxeId-1000].rate = 0.1f;
		ToolTypes[StoneAxeId-1000].canAttack = true;
		ToolTypes[StoneAxeId-1000].canCutTrees = true;
		ToolTypes[StoneAxeId-1000].stackable = false;
		
		ToolTypes[StdTreeId-1000].type = Tree; 
		ToolTypes[StdTreeId-1000].weaponTexture = 0;
		ToolTypes[StdTreeId-1000].description = "It will spawn a normal tree";
		ToolTypes[StdTreeId-1000].name = "Seed:";
		ToolTypes[StdTreeId-1000].rate = 0.2f;
		
		ToolTypes[GreenSpId-1000].type = Sceptre; 
		ToolTypes[GreenSpId-1000].damage = 5;
		ToolTypes[GreenSpId-1000].isShake = false;
		ToolTypes[GreenSpId-1000].weaponTexture = 2;
		ToolTypes[GreenSpId-1000].description = "A green magic wand";
		ToolTypes[GreenSpId-1000].name = "Green Wand";
		ToolTypes[GreenSpId-1000].launcherDistance = 63;
		ToolTypes[GreenSpId-1000].launcherAngle = -18;
		ToolTypes[GreenSpId-1000].sceptreType = 0;
		ToolTypes[GreenSpId-1000].rate = 0.333f;
		ToolTypes[GreenSpId-1000].obstacleProof = true;
		ToolTypes[GreenSpId-1000].stackable = false;
		ToolTypes[GreenSpId-1000].magicprojectile = true;
		ToolTypes[GreenSpId-1000].rarity = Constants.rarity8;
		
		ToolTypes[RedSpId-1000].type = Sceptre; 
		ToolTypes[RedSpId-1000].damage = 5;
		ToolTypes[RedSpId-1000].isShake = false;
		ToolTypes[RedSpId-1000].weaponTexture = 3;
		ToolTypes[RedSpId-1000].description = "A red magic wand";
		ToolTypes[RedSpId-1000].name = "Red Wand";
		ToolTypes[RedSpId-1000].launcherDistance = 63;
		ToolTypes[RedSpId-1000].launcherAngle = -18;
		ToolTypes[RedSpId-1000].sceptreType = 1;
		ToolTypes[RedSpId-1000].rate = 0.2f;
		ToolTypes[RedSpId-1000].obstacleProof = true;
		ToolTypes[RedSpId-1000].stackable = false;
		ToolTypes[RedSpId-1000].magicprojectile = true;
		ToolTypes[RedSpId-1000].rarity = Constants.rarity8;
		
		ToolTypes[PurpleSpId-1000].type = Sceptre; 
		ToolTypes[PurpleSpId-1000].damage = 8;
		ToolTypes[PurpleSpId-1000].isShake = false;
		ToolTypes[PurpleSpId-1000].weaponTexture = 4;
		ToolTypes[PurpleSpId-1000].description = "A purple magic wand";
		ToolTypes[PurpleSpId-1000].name = "Purple Wand";
		ToolTypes[PurpleSpId-1000].launcherDistance = 63;
		ToolTypes[PurpleSpId-1000].launcherAngle = -18;
		ToolTypes[PurpleSpId-1000].sceptreType = 2;
		ToolTypes[PurpleSpId-1000].rate = 0.6f;
		ToolTypes[PurpleSpId-1000].obstacleProof = true;
		ToolTypes[PurpleSpId-1000].stackable = false;
		ToolTypes[PurpleSpId-1000].magicprojectile = true;
		ToolTypes[PurpleSpId-1000].rarity = Constants.rarity8;
		
		ToolTypes[BlueSpId-1000].type = Sceptre; 
		ToolTypes[BlueSpId-1000].damage = 2;
		ToolTypes[BlueSpId-1000].isShake = false;
		ToolTypes[BlueSpId-1000].weaponTexture = 5;
		ToolTypes[BlueSpId-1000].description = "A blue magic wand";
		ToolTypes[BlueSpId-1000].name = "Blue Wand";
		ToolTypes[BlueSpId-1000].launcherDistance = 63;
		ToolTypes[BlueSpId-1000].launcherAngle = -18;
		ToolTypes[BlueSpId-1000].sceptreType = 3;
		ToolTypes[BlueSpId-1000].rate = 0.2f;
		ToolTypes[BlueSpId-1000].obstacleProof = true;
		ToolTypes[BlueSpId-1000].stackable = false;
		ToolTypes[BlueSpId-1000].angle = 0;
		ToolTypes[BlueSpId-1000].magicprojectile = true;
		ToolTypes[BlueSpId-1000].rarity = Constants.rarity8;
		
		ToolTypes[BlackSpId-1000].type = Sceptre; 
		ToolTypes[BlackSpId-1000].damage = 100;
		ToolTypes[BlackSpId-1000].isShake = false;
		ToolTypes[BlackSpId-1000].weaponTexture = 6;
		ToolTypes[BlackSpId-1000].description = "A black magic wand, use it to illuminate your way";
		ToolTypes[BlackSpId-1000].name = "Black Wand";
		ToolTypes[BlackSpId-1000].launcherDistance = 63;
		ToolTypes[BlackSpId-1000].launcherAngle = -18;
		ToolTypes[BlackSpId-1000].sceptreType = 4;
		ToolTypes[BlackSpId-1000].rate = 2f;
		ToolTypes[BlackSpId-1000].obstacleProof = true;
		ToolTypes[BlackSpId-1000].stackable = false;
		ToolTypes[BlackSpId-1000].magicprojectile = true;
		ToolTypes[BlackSpId-1000].magiclight = true;
		ToolTypes[BlackSpId-1000].rarity = Constants.rarity8;
		
		ToolTypes[WoodStickId-1000].type = UseLess; 
		ToolTypes[WoodStickId-1000].damage = 1;
		ToolTypes[WoodStickId-1000].isShake = true;
		ToolTypes[WoodStickId-1000].weaponTexture = 7;
		ToolTypes[WoodStickId-1000].description = "A simple wooden stick";
		ToolTypes[WoodStickId-1000].name = "Stick";
		ToolTypes[WoodStickId-1000].rate = 1f;
		ToolTypes[WoodStickId-1000].obstacleProof = true;
		ToolTypes[WoodStickId-1000].stackable = false;
		
		ToolTypes[UltraHelmetId-1000].type = UseLess; 
		ToolTypes[UltraHelmetId-1000].description = "Increases Jump and Defense";
		ToolTypes[UltraHelmetId-1000].name = "Ultra Helmet";
		ToolTypes[UltraHelmetId-1000].stackable = false;
		ToolTypes[UltraHelmetId-1000].equipment = true;
		ToolTypes[UltraHelmetId-1000].helmet = true;
		ToolTypes[UltraHelmetId-1000].armorJump = 30;
		ToolTypes[UltraHelmetId-1000].armorSpeed = 0;
		ToolTypes[UltraHelmetId-1000].armorDefense = 50;
		ToolTypes[UltraHelmetId-1000].weaponTexture = 9;
		ToolTypes[UltraHelmetId-1000].rarity = Constants.rarity10;
		
		ToolTypes[UltraArmorId-1000].type = UseLess; 
		ToolTypes[UltraArmorId-1000].description = "Increases Jump and Defense";
		ToolTypes[UltraArmorId-1000].name = "Ultra Armor";
		ToolTypes[UltraArmorId-1000].stackable = false;
		ToolTypes[UltraArmorId-1000].equipment = true;
		ToolTypes[UltraArmorId-1000].armor = true;
		ToolTypes[UltraArmorId-1000].armorJump = 50;
		ToolTypes[UltraArmorId-1000].armorSpeed = 0;
		ToolTypes[UltraArmorId-1000].armorDefense = 50;
		ToolTypes[UltraArmorId-1000].weaponTexture = 10;
		ToolTypes[UltraArmorId-1000].rarity = Constants.rarity10;
		
		ToolTypes[UltraArmsId-1000].type = UseLess; 
		ToolTypes[UltraArmsId-1000].description = "Increases Speed and Defense";
		ToolTypes[UltraArmsId-1000].name = "Ultra Arms";
		ToolTypes[UltraArmsId-1000].stackable = false;
		ToolTypes[UltraArmsId-1000].equipment = true;
		ToolTypes[UltraArmsId-1000].arms = true;
		ToolTypes[UltraArmsId-1000].armorJump = 0;
		ToolTypes[UltraArmsId-1000].armorSpeed = 200;
		ToolTypes[UltraArmsId-1000].armorDefense = 50;
		ToolTypes[UltraArmsId-1000].weaponTexture = 11;
		ToolTypes[UltraArmsId-1000].rarity = Constants.rarity10;
		
		ToolTypes[UltraLegginsId-1000].type = UseLess; 
		ToolTypes[UltraLegginsId-1000].description = "Increases Speed and Defense";
		ToolTypes[UltraLegginsId-1000].name = "Ultra Leggins";
		ToolTypes[UltraLegginsId-1000].stackable = false;
		ToolTypes[UltraLegginsId-1000].equipment = true;
		ToolTypes[UltraLegginsId-1000].legs = true;
		ToolTypes[UltraLegginsId-1000].armorJump = 0;
		ToolTypes[UltraLegginsId-1000].armorSpeed = 500;
		ToolTypes[UltraLegginsId-1000].armorDefense = 50;
		ToolTypes[UltraLegginsId-1000].weaponTexture = 1;//legginTexture
		ToolTypes[UltraLegginsId-1000].rarity = Constants.rarity10;
		
		ToolTypes[BowId-1000].type = Bow; 
		ToolTypes[BowId-1000].damage = 10;
		ToolTypes[BowId-1000].isShake = false;
		ToolTypes[BowId-1000].weaponTexture = 12;
		ToolTypes[BowId-1000].description = "A simple crossbow";
		ToolTypes[BowId-1000].name = "Wooden crossbow";
		ToolTypes[BowId-1000].launcherDistance = 50;
		ToolTypes[BowId-1000].launcherAngle = -89;
		ToolTypes[BowId-1000].rate = 0.5f;
		ToolTypes[BowId-1000].obstacleProof = true;
		ToolTypes[BowId-1000].stackable = false;
		ToolTypes[BowId-1000].angle = 20;
		ToolTypes[BowId-1000].arrowType = ArrowId;
		ToolTypes[BowId-1000].rarity = Constants.rarity4;
		
		ToolTypes[ArrowId-1000].type = UseLess; 
		ToolTypes[ArrowId-1000].damage = 1;
		ToolTypes[ArrowId-1000].isShake = true;
		ToolTypes[ArrowId-1000].description = "A simple arrow";
		ToolTypes[ArrowId-1000].name = "Arrow";
		ToolTypes[ArrowId-1000].rate = 1f;
		ToolTypes[ArrowId-1000].obstacleProof = true;
		ToolTypes[ArrowId-1000].stackable = true;
		
		ToolTypes[WoodStickAndStoneId-1000].type = UseLess; 
		ToolTypes[WoodStickAndStoneId-1000].damage = 1;
		ToolTypes[WoodStickAndStoneId-1000].isShake = true;
		ToolTypes[WoodStickAndStoneId-1000].weaponTexture = 8;
		ToolTypes[WoodStickAndStoneId-1000].description = "A wooden stick with a little stone on it";
		ToolTypes[WoodStickAndStoneId-1000].name = "Stick with stone";
		ToolTypes[WoodStickAndStoneId-1000].rate = 1f;
		ToolTypes[WoodStickAndStoneId-1000].obstacleProof = true;
		ToolTypes[WoodStickAndStoneId-1000].stackable = false;
		ToolTypes[WoodStickAndStoneId-1000].rarity = Constants.rarity3;
		
		createSS(SwordId, 20, 1, 13, "Sword", "A simple sword", Constants.rarity1);
		createSS(Sword1Id, 25, 1.1f, 15, "Demon Sword", "Slightly better than normal sword", Constants.rarity3);
		createSS(Sword2Id, 30, 0.9f, 16, "Butcher Sword", "Slices meat very well", Constants.rarity4);
		createSS(Sword3Id, 40, 0.7f, 17, "Wooden Hammer", "Very powerful", Constants.rarity5);
		createSS(Sword4Id, 35, 1.2f, 18, "Liquid Sword", "Fast and efficient", Constants.rarity5);
		createSS(Sword5Id, 12, 1.5f, 19, "Short Sword", "Small but fast", Constants.rarity5);
		createSS(Sword6Id, 50, 1, 20, "Blood Sword", "Crafted in blood and tears", Constants.rarity7);
		createSS(Sword7Id, 30, 0.8f, 21, "Axe", "A simple axe", Constants.rarity1);
		createSS(Sword8Id, 35, 0.8f, 22, "Ehanced Axe", "Slightly better than normal Axe", Constants.rarity3);
		createSS(Sword9Id, 40, 1, 23, "Emerald Axe", "Sharper and faster than other axes", Constants.rarity6);
		
		ToolTypes[BigSwordId-1000].type = Sword; 
		ToolTypes[BigSwordId-1000].isShake = false;
		ToolTypes[BigSwordId-1000].weaponTexture = 14;
		ToolTypes[BigSwordId-1000].description = "A simple great sword";
		ToolTypes[BigSwordId-1000].name = "Great Sword";
		ToolTypes[BigSwordId-1000].launcherDistance = 90;
		ToolTypes[BigSwordId-1000].launcherAngle = -45;
		ToolTypes[BigSwordId-1000].rate = 0.5f;
		ToolTypes[BigSwordId-1000].obstacleProof = true;
		ToolTypes[BigSwordId-1000].stackable = false;
		ToolTypes[BigSwordId-1000].angle = 25;
		ToolTypes[BigSwordId-1000].downtime = 20;
		ToolTypes[BigSwordId-1000].uptime = 10;
		ToolTypes[BigSwordId-1000].damage = 30;
		
		ToolTypes[ChestId-1000].type = Furniture;
		ToolTypes[ChestId-1000].isShake = true;
		ToolTypes[ChestId-1000].weaponTexture = 0;
		ToolTypes[ChestId-1000].description = "A wooden chest";
		ToolTypes[ChestId-1000].name = "Chest";
		ToolTypes[ChestId-1000].stackable = true;
		ToolTypes[ChestId-1000].furnitureType = 0;
		ToolTypes[ChestId-1000].furnitureWidth = 3;
		ToolTypes[ChestId-1000].furnitureHeight = 2;
		ToolTypes[ChestId-1000].rate = 0.1f;
		ToolTypes[ChestId-1000].furnitureContainer = true;
		ToolTypes[ChestId-1000].turnable = true;
		ToolTypes[ChestId-1000].tex = TextureManager.instance.chest;
		
		ToolTypes[BedId-1000].type = Furniture;
		ToolTypes[BedId-1000].isShake = true;
		ToolTypes[BedId-1000].weaponTexture = 0;
		ToolTypes[BedId-1000].description = "A simple bed";
		ToolTypes[BedId-1000].name = "Bed";
		ToolTypes[BedId-1000].stackable = true;
		ToolTypes[BedId-1000].furnitureType = 0;
		ToolTypes[BedId-1000].furnitureWidth = 6;
		ToolTypes[BedId-1000].furnitureHeight = 2;
		ToolTypes[BedId-1000].rate = 0.1f;
		ToolTypes[BedId-1000].turnable = true;
		ToolTypes[BedId-1000].tex = TextureManager.instance.bed;
		
		ToolTypes[ChairId-1000].type = Furniture;
		ToolTypes[ChairId-1000].isShake = true;
		ToolTypes[ChairId-1000].weaponTexture = 0;
		ToolTypes[ChairId-1000].description = "A simple chair";
		ToolTypes[ChairId-1000].name = "Wooden chair";
		ToolTypes[ChairId-1000].stackable = true;
		ToolTypes[ChairId-1000].furnitureType = 0;
		ToolTypes[ChairId-1000].furnitureWidth = 2;
		ToolTypes[ChairId-1000].furnitureHeight = 3;
		ToolTypes[ChairId-1000].rate = 0.1f;
		ToolTypes[ChairId-1000].turnable = true;
		ToolTypes[ChairId-1000].tex = TextureManager.instance.chair;
		
		ToolTypes[AnvilId-1000].type = Furniture;
		ToolTypes[AnvilId-1000].isShake = true;
		ToolTypes[AnvilId-1000].weaponTexture = 0;
		ToolTypes[AnvilId-1000].description = "A simple iron anvil";
		ToolTypes[AnvilId-1000].name = "Iron anvil";
		ToolTypes[AnvilId-1000].stackable = true;
		ToolTypes[AnvilId-1000].furnitureType = 0;
		ToolTypes[AnvilId-1000].furnitureWidth = 4;
		ToolTypes[AnvilId-1000].furnitureHeight = 2;
		ToolTypes[AnvilId-1000].rate = 0.1f;
		ToolTypes[AnvilId-1000].turnable = true;
		ToolTypes[AnvilId-1000].tex = TextureManager.instance.anvil;
		
		ToolTypes[HookId-1000].type = Hook; 
		ToolTypes[HookId-1000].isShake = false;
		ToolTypes[HookId-1000].description = "A magic hook";
		ToolTypes[HookId-1000].name = "Magic hook";
		ToolTypes[HookId-1000].rate = 0.2f;
		ToolTypes[HookId-1000].obstacleProof = true;
		ToolTypes[HookId-1000].stackable = false;
		ToolTypes[HookId-1000].grapple = true;
		ToolTypes[HookId-1000].rarity = Constants.rarity4;
		ToolTypes[HookId-1000].invisible = true;
		ToolTypes[HookId-1000].grappleTex = TextureManager.instance.grappleGreen;
		ToolTypes[HookId-1000].grappleTexRope = TextureManager.instance.ropeGreen;
		
		ToolTypes[NormalHookId-1000].type = Hook; 
		ToolTypes[NormalHookId-1000].isShake = false;
		ToolTypes[NormalHookId-1000].description = "A simple hook";
		ToolTypes[NormalHookId-1000].name = "Hook";
		ToolTypes[NormalHookId-1000].rate = 0.2f;
		ToolTypes[NormalHookId-1000].obstacleProof = true;
		ToolTypes[NormalHookId-1000].stackable = false;
		ToolTypes[NormalHookId-1000].grapple = true;
		ToolTypes[NormalHookId-1000].rarity = Constants.rarity3;
		ToolTypes[NormalHookId-1000].invisible = true;
		ToolTypes[NormalHookId-1000].grappleDistance = 1000;
		ToolTypes[NormalHookId-1000].grappleTex = TextureManager.instance.grapple;
		ToolTypes[NormalHookId-1000].grappleTexRope = TextureManager.instance.rope;
		
		
		ToolTypes[MiningHelmetId-1000].type = UseLess; 
		ToolTypes[MiningHelmetId-1000].description = "Enlight your way";
		ToolTypes[MiningHelmetId-1000].name = "Mining Helmet";
		ToolTypes[MiningHelmetId-1000].stackable = false;
		ToolTypes[MiningHelmetId-1000].equipment = true;
		ToolTypes[MiningHelmetId-1000].helmet = true;
		ToolTypes[MiningHelmetId-1000].helmetLight = true;
		ToolTypes[MiningHelmetId-1000].armorDefense = 3;
		ToolTypes[MiningHelmetId-1000].weaponTexture = 24;
		ToolTypes[MiningHelmetId-1000].rarity = Constants.rarity5;
		

		weaponTextures[0] = new TextureRegion();
		weaponTextures[1] = new TextureRegion(TextureManager.instance.bonhommeWeapon);
		weaponTextures[2] = new TextureRegion(TextureManager.instance.greenSceptre);
		weaponTextures[3] = new TextureRegion(TextureManager.instance.redSceptre);
		weaponTextures[4] = new TextureRegion(TextureManager.instance.purpleSceptre);
		weaponTextures[5] = new TextureRegion(TextureManager.instance.blueSceptre);
		weaponTextures[6] = new TextureRegion(TextureManager.instance.blackSceptre);
		weaponTextures[7] = new TextureRegion(TextureManager.instance.woodStick);
		weaponTextures[8] = new TextureRegion(TextureManager.instance.woodStickAndStone);
		weaponTextures[9] = new TextureRegion(TextureManager.instance.casque1);
		weaponTextures[10] = new TextureRegion(TextureManager.instance.armor1);
		weaponTextures[11] = new TextureRegion(TextureManager.instance.arms1);
		weaponTextures[12] = new TextureRegion(TextureManager.instance.bow);
		weaponTextures[13] = new TextureRegion(TextureManager.instance.swords.get(0));
		weaponTextures[14] = new TextureRegion(TextureManager.instance.bigsword);
		weaponTextures[15] = new TextureRegion(TextureManager.instance.swords.get(1));
		weaponTextures[16] = new TextureRegion(TextureManager.instance.swords.get(2));
		weaponTextures[17] = new TextureRegion(TextureManager.instance.swords.get(3));
		weaponTextures[18] = new TextureRegion(TextureManager.instance.swords.get(4));
		weaponTextures[19] = new TextureRegion(TextureManager.instance.swords.get(5));
		weaponTextures[20] = new TextureRegion(TextureManager.instance.swords.get(6));
		weaponTextures[21] = new TextureRegion(TextureManager.instance.swords.get(7));
		weaponTextures[22] = new TextureRegion(TextureManager.instance.swords.get(8));
		weaponTextures[23] = new TextureRegion(TextureManager.instance.swords.get(9));
		weaponTextures[24] = new TextureRegion(TextureManager.instance.miningHelmet);
		
		
		
		legginsTextures[1] = TextureManager.instance.legginsArray1;
	}
	
	public void createSS(int id, int damage, float speed, int textureid, String name, String description, ColorString rarity)
	{
		ToolTypes[id-1000].type = Sword; 
		ToolTypes[id-1000].isShake = false;
		ToolTypes[id-1000].weaponTexture = textureid;
		ToolTypes[id-1000].description = description;
		ToolTypes[id-1000].name = name;
		ToolTypes[id-1000].launcherDistance = 75;
		ToolTypes[id-1000].launcherAngle = -45;
		ToolTypes[id-1000].obstacleProof = true;
		ToolTypes[id-1000].stackable = false;
		ToolTypes[id-1000].angle = 25;
		ToolTypes[id-1000].downtime = 24*speed;
		ToolTypes[id-1000].uptime = 16*speed;
		ToolTypes[id-1000].damage = damage;
		ToolTypes[id-1000].rarity = rarity;
	}
	
	public TextureRegion[] getLeggins(int Id)
	{
		return legginsTextures[Id];
	}
	public TextureRegion getRegion(int weaponId)
	{
		return weaponTextures[weaponId];
	}
	public ToolType getType(int id)
	{
		if(id<1000)return defaultType;
		return ToolTypes[id-1000];
	}

}
