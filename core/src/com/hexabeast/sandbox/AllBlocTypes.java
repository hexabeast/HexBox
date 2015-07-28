package com.hexabeast.sandbox;

public class AllBlocTypes {

	public static AllBlocTypes instance;
	
	public int TileSetNumber = 33;

	public BlocType[] CellTypes = new BlocType[TileSetNumber];
	
	public BlocType Empty;
	public BlocType Dirt;
	public BlocType Unbreak;
	public BlocType Cobble;
	public BlocType Grass;
	public BlocType Iron;
	public BlocType Gold;
	public BlocType Machalium;
	public BlocType Dragonium;
	public BlocType Carbalium;
	public BlocType Eltalium;
	public BlocType Wood;
	public BlocType StoneBricks;
	public BlocType Glass;
	public BlocType IronBlock;
	public BlocType Ice;
	public BlocType Log;
	public BlocType Fossile;
	public BlocType CrystalPurple;
	public BlocType CrystalGreen;
	public BlocType CrystalRed;
	public BlocType CrystalBlue;
	public BlocType CrystalDark;
	public BlocType Torch;
	public BlocType[] CrystalFire = new BlocType[4];
	public BlocType TorchPurple;
	public BlocType TorchGreen;
	public BlocType TorchRed;
	public BlocType TorchBlue;
	public BlocType Mud;
	
	public int full = 2;
	
	public int IIIsides = 0;
	
	public int IIsidesOpp = 4;
	
	public int IIsides = 3;
	
	public int Isides = 1;
	
	public int Nosides = 5;
	
	
	//OLD
	
	public int oldFull = 42;
	
	public int iRight = 0;
	public int iLeft = 9;
	public int iDown = 18;
	public int iUp = 27;
	
	public int RightDown = 36;
	public int LeftDown = 39;
	public int RightUp = 45;
	public int LeftUp = 48;
	
	public int RightLeft = 15;
	public int UpDown = 24;
	
	public int Left = 3;
	public int Right = 12;
	public int Up = 21;
	public int Down = 30;
	
	public int Alone = 6;
	
	
	public AllBlocTypes() 
	{
		for(int i = 0; i<CellTypes.length;i++)
		{
			CellTypes[i] = new BlocType();
			CellTypes[i].Id = i;
			CellTypes[i].dropId = i;
		}

		Empty = CellTypes[0];
		Empty.collide = false;
		Empty.air = true;
		Empty.transparent = true;
		Empty.waterOccupy = true;
		Empty.nonblock = true;
		
		//ShadowId].shadow = true;
		
		Dirt = CellTypes[1];
		Dirt.collide = true; 
		Dirt.pitch = 1f;
		Dirt.description = "Probably the cheapest material";
		Dirt.name = "Dirt";
		Dirt.fertile = true;
		Dirt.dirtish = true;
		Dirt.soundType = SoundManager.instance.dirtSound;
		Dirt.dirtishpriority = 0;
		
		Grass = CellTypes[4];
		Grass.collide = true;
		Grass.pitch = 1f;
		Grass.fertile = true;
		Grass.soundType = SoundManager.instance.dirtSound;
		Grass.merge = Dirt;
		Grass.dropId = Dirt.Id;
		
		Mud = CellTypes[32];
		Mud.collide = true;
		Mud.pitch = 1f;
		Mud.fertile = true;
		Mud.description = "Muddy";
		Mud.name = "Mud";
		Mud.soundType = SoundManager.instance.dirtSound;
		Mud.dirtishpriority = 1;
		Mud.dirtish = true;
		Mud.dirtishtype = 1;
		//Mud.mergeId = DirtId;

		Iron = CellTypes[5];
		Iron.collide = true; 
		Iron.durability = 6; 
		Iron.pitch = 1f; 
		Iron.description = "Strong enough to make weapons and armors with it";
		Iron.name = "Iron";
		Iron.soundType = SoundManager.instance.metalSound;
		Iron.rarity = Constants.rarity3;
		Iron.dirtishpriority = 3;
		Iron.dirtish = true;
		Iron.dirtishtype = 1;
		
		Gold = CellTypes[6];
		Gold.collide = true; 
		Gold.durability = 6; 
		Gold.pitch = 1f;
		Gold.description = "More expensive, and more resistant than iron";
		Gold.name = "Gold";
		Gold.soundType = SoundManager.instance.metalSound;
		Gold.rarity = Constants.rarity4;
		Iron.dirtishpriority = 4;
		Iron.dirtish = true;
		Iron.dirtishtype = 1;
		
		Machalium = CellTypes[7];
		Machalium.collide = true; 
		Machalium.durability = 10; 
		Machalium.pitch = 1f;
		Machalium.description = "Better than gold, great for forging armors";
		Machalium.name = "Machalium";
		Machalium.soundType = SoundManager.instance.metalSound;
		Machalium.rarity = Constants.rarity5;
		Machalium.dirtishpriority = 5;
		Machalium.dirtish = true;
		Machalium.dirtishtype = 1;
		
		Dragonium = CellTypes[8];
		Dragonium.collide = true; 
		Dragonium.durability = 12; 
		Dragonium.pitch = 1f;
		Dragonium.description = "Better than gold, great for forging weapons";
		Dragonium.name = "Dragonium";
		Dragonium.soundType = SoundManager.instance.metalSound;
		Dragonium.rarity = Constants.rarity5;
		Dragonium.dirtishpriority = 6;
		Dragonium.dirtish = true;
		Dragonium.dirtishtype = 1;
		
		Carbalium = CellTypes[9];
		Carbalium.collide = true; 
		Carbalium.durability = 14; 
		Carbalium.pitch = 1f;
		Carbalium.description = "A pure concentrate of power, better than Machalium or Dragonium";
		Carbalium.name = "Carbalium";
		Carbalium.soundType = SoundManager.instance.metalSound;
		Carbalium.rarity = Constants.rarity7;
		Carbalium.dirtishpriority = 7;
		Carbalium.dirtish = true;
		Carbalium.dirtishtype = 1;
		
		Eltalium = CellTypes[10];
		Eltalium.collide = true; 
		Eltalium.durability = 18; 
		Eltalium.pitch = 1f;
		Eltalium.description = "According to a legend, gods used Eltalium to forge their weapons";
		Eltalium.name = "Eltalium:";
		Eltalium.soundType = SoundManager.instance.metalSound;
		Eltalium.rarity = Constants.rarity9;
		Eltalium.dirtishpriority = 8;
		Eltalium.dirtish = true;
		Eltalium.dirtishtype = 1;
		
		Wood = CellTypes[11];
		Wood.collide = true; 
		Wood.durability = 2; 
		Wood.pitch = 1f;
		Wood.description = "Useful to build houses or some parts of weapons";
		Wood.name = "Wood";
		Wood.soundType = SoundManager.instance.woodSound;
		Wood.artificial = true;
		Wood.oldTexture = true;
		
		StoneBricks = CellTypes[12];
		StoneBricks.collide = true; 
		StoneBricks.durability = 4; 
		StoneBricks.pitch = 1f;
		StoneBricks.description = "Useful to build resistant houses";
		StoneBricks.name = "Stone Bricks";
		StoneBricks.soundType = SoundManager.instance.stoneSound;
		StoneBricks.oldTexture = true;
		StoneBricks.artificial = true;
		
		Glass = CellTypes[13];
		Glass.collide = true; 
		Glass.durability = 1; 
		Glass.pitch = 1f;
		Glass.description = "Easy to break, the light passes through it";
		Glass.name = "Glass";
		Glass.transparent = true;
		Glass.soundType = SoundManager.instance.metalSound;
		Glass.artificial = true;
		Glass.oldTexture = true;
		Glass.rarity = Constants.rarity3;
		
		Cobble = CellTypes[3];
		Cobble.collide = true; 
		Cobble.durability = 4; 
		Cobble.pitch = 1f;
		Cobble.description = "Harder than dirt, but not rarer";
		Cobble.name = "Cobblestone";
		Cobble.soundType = SoundManager.instance.stoneSound;
		Cobble.oldTexture = true;
		Cobble.dirtishpriority = 2;
		Cobble.dirtish = true;
		Cobble.dirtishtype = 1;
		
		IronBlock = CellTypes[14];
		IronBlock.collide = true; 
		IronBlock.durability = 12; 
		IronBlock.pitch = 1f;
		IronBlock.description = "Very resistant material, used for building";
		IronBlock.name = "Iron Block";
		IronBlock.soundType = SoundManager.instance.metalSound;
		IronBlock.artificial = true;
		IronBlock.oldTexture = true;
		IronBlock.rarity = Constants.rarity4;
		
		Ice = CellTypes[15];
		Ice.collide = true; 
		Ice.durability = 1; 
		Ice.pitch = 1f;
		Ice.description = "Very weak material, used for building";
		Ice.name = "Ice";
		Ice.friction = 0.00005f;
		Ice.soundType = SoundManager.instance.metalSound;
		Ice.artificial = true;
		Ice.oldTexture = true;
		Ice.rarity = Constants.rarity2;
		
		Log = CellTypes[16];
		Log.collide = true; 
		Log.durability = 3; 
		Log.pitch = 1f;
		Log.description = "Raw wood, used for building";
		Log.name = "Log";
		Log.soundType = SoundManager.instance.woodSound;
		Log.artificial = true;
		Log.oldTexture = true;
		
		Fossile = CellTypes[17];
		Fossile.collide = true; 
		Fossile.durability = 5; 
		Fossile.pitch = 1f;
		Fossile.description = "Dirt with bones";
		Fossile.name = "Fossiles";
		Fossile.soundType = SoundManager.instance.stoneSound;
		Fossile.merge = Dirt;
		Fossile.rarity = Constants.rarity4;
		
		CrystalPurple = CellTypes[18];
		CrystalPurple.collide = true; 
		CrystalPurple.durability = 10; 
		CrystalPurple.pitch = 1f;
		CrystalPurple.description = "A purple crystal";
		CrystalPurple.name = "Purple Crystal";
		CrystalPurple.soundType = SoundManager.instance.metalSound;
		CrystalPurple.merge = Cobble;
		CrystalPurple.oldTexture = true;
		CrystalPurple.rarity = Constants.rarity6;
		
		CrystalRed = CellTypes[20];
		CrystalRed.collide = true; 
		CrystalRed.durability = 10; 
		CrystalRed.pitch = 1f;
		CrystalRed.description = "A red crystal";
		CrystalRed.name = "Red Crystal";
		CrystalRed.soundType = SoundManager.instance.metalSound;
		CrystalRed.merge = Cobble;
		CrystalRed.oldTexture = true;
		CrystalRed.rarity = Constants.rarity6;
		
		CrystalGreen = CellTypes[19];
		CrystalGreen.collide = true; 
		CrystalGreen.durability = 10; 
		CrystalGreen.pitch = 1f;
		CrystalGreen.description = "A green crystal";
		CrystalGreen.name = "Green Crystal";
		CrystalGreen.soundType = SoundManager.instance.metalSound;
		CrystalGreen.merge = Cobble;
		CrystalGreen.oldTexture = true;
		CrystalGreen.rarity = Constants.rarity6;
		
		CrystalBlue = CellTypes[21];
		CrystalBlue.collide = true; 
		CrystalBlue.durability = 10; 
		CrystalBlue.pitch = 1f;
		CrystalBlue.description = "A blue crystal";
		CrystalBlue.name = "Blue Crystal";
		CrystalBlue.soundType = SoundManager.instance.metalSound;
		CrystalBlue.merge = Cobble;
		CrystalBlue.oldTexture = true;
		CrystalBlue.rarity = Constants.rarity6;
		
		CrystalDark = CellTypes[22];
		CrystalDark.collide = true; 
		CrystalDark.durability = 10; 
		CrystalDark.pitch = 1f;
		CrystalDark.description = "A dark crystal";
		CrystalDark.name = "Dark Crystal";
		CrystalDark.soundType = SoundManager.instance.metalSound;
		CrystalDark.merge = Cobble;
		CrystalDark.oldTexture = true;
		CrystalDark.rarity = Constants.rarity10;
		
		Torch = CellTypes[23];
		Torch.transparent = true; 
		Torch.stdCheck = false; 
		Torch.needBack = true; 
		Torch.lightFull = true; 
		Torch.collide = false; 
		Torch.durability = 1; 
		Torch.pitch = 1f;
		Torch.description = "emmit light";
		Torch.name = "Torch";
		Torch.soundType = SoundManager.instance.woodSound;
		Torch.lightColor = new float[]{1.3f,1.3f,0.9f};
		Torch.torch = true;
		Torch.nonblock = true;
		
		TorchRed = CellTypes[30];
		TorchRed.transparent = true; 
		TorchRed.stdCheck = false; 
		TorchRed.needBack = true; 
		TorchRed.lightFull = true; 
		TorchRed.collide = false; 
		TorchRed.durability = 1; 
		TorchRed.pitch = 1f;
		TorchRed.description = "emmit red light";
		TorchRed.name = "Red torch";
		TorchRed.soundType = SoundManager.instance.woodSound;
		TorchRed.lightColor = new float[]{2.0f,0.4f,0.4f};
		TorchRed.torch = true;
		TorchRed.nonblock = true;
		
		TorchPurple = CellTypes[28];
		TorchPurple.transparent = true; 
		TorchPurple.stdCheck = false; 
		TorchPurple.needBack = true; 
		TorchPurple.lightFull = true; 
		TorchPurple.collide = false; 
		TorchPurple.durability = 1; 
		TorchPurple.pitch = 1f;
		TorchPurple.description = "emmit purple light";
		TorchPurple.name = "Purple torch";
		TorchPurple.soundType = SoundManager.instance.woodSound;
		TorchPurple.lightColor = new float[]{1.6f,0.4f,1.6f};
		TorchPurple.torch = true;
		TorchPurple.nonblock = true;
		
		TorchGreen = CellTypes[29];
		TorchGreen.transparent = true; 
		TorchGreen.stdCheck = false; 
		TorchGreen.needBack = true; 
		TorchGreen.lightFull = true; 
		TorchGreen.collide = false; 
		TorchGreen.durability = 1; 
		TorchGreen.pitch = 1f;
		TorchGreen.description = "emmit green light";
		TorchGreen.name = "Green torch";
		TorchGreen.soundType = SoundManager.instance.woodSound;
		TorchGreen.lightColor = new float[]{0.4f,2.0f,0.4f};
		TorchGreen.torch = true;
		TorchGreen.nonblock = true;
		
		TorchBlue = CellTypes[31];
		TorchBlue.transparent = true; 
		TorchBlue.stdCheck = false; 
		TorchBlue.needBack = true; 
		TorchBlue.lightFull = true; 
		TorchBlue.collide = false; 
		TorchBlue.durability = 1; 
		TorchBlue.pitch = 1f;
		TorchBlue.description = "emmit blue light";
		TorchBlue.name = "Blue torch";
		TorchBlue.soundType = SoundManager.instance.woodSound;
		TorchBlue.lightColor = new float[]{0.4f,0.4f,2.0f};
		TorchBlue.torch = true;
		TorchBlue.nonblock = true;
		
		
		/*Water.transparent = true; 
		Water.waterOccupy = true; 
		Water.fluid = true; 
		Water.stdCheck = false; 
		Water.collide = false; 
		Water.unbreakable = true; 
		Water.durability = 1; 
		Water.pitch = 1f;
		Water.description = "WTF";
		Water.name = "Water";
		Water.soundType = SoundManager.instance.metalSound;*/
		for(int i = 0; i<4; i++)
		{
			CrystalFire[i] = CellTypes[24+i];
		}
		for(int i = 0; i<4; i++)
		{
			CrystalFire[i].animated = true; 
			if(i<3)CrystalFire[i].next = CrystalFire[i+1]; 
			else CrystalFire[i].next = CrystalFire[0]; 
			CrystalFire[i].lightFull = true;
			CrystalFire[i].dropId = 24;
			CrystalFire[i].collide = true; 
			CrystalFire[i].durability = 30; 
			CrystalFire[i].pitch = 1f;
			CrystalFire[i].description = "An extremely rare crystal";
			CrystalFire[i].name = "Fire Crystal";
			CrystalFire[i].soundType = SoundManager.instance.metalSound;
			CrystalFire[i].merge = Cobble;
			CrystalFire[i].lightColor = new float[]{1.8f,0.5f,0.3f};
			CrystalFire[i].rarity = Constants.rarity9;
		}
		
		Unbreak = CellTypes[2];
		Unbreak.collide = true; 
		Unbreak.unbreakable = true;
		Unbreak.description = "If you look at this description, you're a cheater";
		Unbreak.name = "Unbreakable:";
		Unbreak.dirtishpriority = 9;
		Unbreak.dirtish = true;
		Unbreak.dirtishtype = 1;
	}
	
	public BlocType getType(int id)
	{
		return CellTypes[id];
	}
	
	public boolean IsTransparent(int cell)
	{
		BlocType cType = getType(cell);
		return cType.transparent;
	}
	
	public boolean IsTransparent(BlocType cType)
	{
		return cType.transparent;
	}
}
