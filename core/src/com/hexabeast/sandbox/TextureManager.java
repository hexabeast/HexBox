package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	
	public static TextureManager instance;
	
	public int PNJNumber = 3;
	public int PNJArmorNumber = 20;

	int sX = 3;
	int sY = 2;
	
	int bX = 9;
	int bY = 6;
	
	public ArrayList<TextureRegion> packedTextures = new ArrayList<TextureRegion>();
	
	PixmapPacker packer = new PixmapPacker(2048,2048,Pixmap.Format.RGBA8888,2,true);
	
	TextureAtlas atlas;
	
	public TextureRegion[][] TextureRegions = new TextureRegion[AllBlocTypes.instance.TileSetNumber][bX*bY];
	public Coordinates[] CoordinatesBlocs = new Coordinates[AllBlocTypes.instance.TileSetNumber];
	public TextureRegion[] ParticleBlocs = new TextureRegion[AllBlocTypes.instance.TileSetNumber];
	
	public TextureRegion empty;
	public TextureRegion rain;
	public TextureRegion[] rains = new TextureRegion[4];
	
	public TextureRegion item;
	public TextureRegion tool;
	public TextureRegion badwolf;
	public TextureRegion hornet;
	public TextureRegion dinorouge;
	public TextureRegion biginsecte;
	public TextureRegion[] veswings = new TextureRegion[2];
	public TextureRegion[] vesbody = new TextureRegion[4];
	
	public TextureRegion[] wolf = new TextureRegion[10];
	public TextureRegion[] hor = new TextureRegion[2];
	
	public TextureRegion bonhommeWeapon;
	public TextureRegion greenSceptre;
	public TextureRegion redSceptre;
	public TextureRegion purpleSceptre;
	public TextureRegion blueSceptre;
	public TextureRegion blackSceptre;
	public TextureRegion woodStick;
	public TextureRegion woodStickAndStone;
	
	public TextureRegion Ttroncs;
	public TextureRegion TbranchesD;
	public TextureRegion TbranchesG;
	public TextureRegion Tcimes;
	public TextureRegion backText;
	public TextureRegion selectDefault;
	public TextureRegion backTrashTexture;
	public TextureRegion invButtonTexture;
	public TextureRegion craftNew;
	public TextureRegion craftButton;
	public TextureRegion backLBText;
	public TextureRegion LBText;
	public TextureRegion redLBText;
	public TextureRegion greenLBText;
	public Texture loadingJ;
	public TextureRegion loadingV;
	public TextureRegion loadingShadow;
	public Texture cad1;
	public Texture cad2;
	public Texture cad3;
	public Texture cad4;
	public Texture cad5;
	public Texture cad6;
	public Texture cad7;
	public Texture cad8;
	public Texture backGround;
	public Texture backGroundChar;
	public Texture roue1;
	public Texture roue2;
	public Texture back;
	public Texture backn;
	public Texture layer1;
	public Texture layer2;
	public Texture layer3;
	public Texture layer4;
	public Texture layer5;
	
	public TextureRegion particle;
	public TextureRegion armTextureOld;
	public TextureRegion selecbacktext;
	public TextureRegion torchText;
	public TextureRegion cadre;
	
	public TextureRegion portalTexture;
	public TextureRegion forgeTexture;
	public TextureRegion armorForgeTexture;
	public TextureRegion fleuristeTexture;
	
	public TextureRegion doorForgeTexture;
	public TextureRegion doorArmorForgeTexture;
	public TextureRegion doorFleuristeTexture;
	
	public TextureRegion chatBehind;
	public TextureRegion playButton;
	public TextureRegion hostButton;
	public TextureRegion joinButton;
	public TextureRegion settingsButton;
	public TextureRegion charEditButton;
	public TextureRegion ipButton;
	public TextureRegion textBoxSurline;
	public TextureRegion textBoxCursor;
	
	public TextureRegion sGraphicsButton;
	public TextureRegion sAudioButton;
	public TextureRegion sKeyboardButton;
	public TextureRegion sDisplayButton;
	public TextureRegion sButtonHolder;
	
	public TextureRegion casque1;
	public TextureRegion armor1;
	public TextureRegion arms1;
	public TextureRegion leggins1;
	public TextureRegion[] legginsArray1;
	
	public TextureRegion[] PNJeye = new TextureRegion[PNJNumber];
	public TextureRegion[] PNJhairs = new TextureRegion[PNJNumber];
	public TextureRegion[] PNJhead = new TextureRegion[PNJNumber];
	public TextureRegion[] PNJlegList = new TextureRegion[PNJNumber];
	public TextureRegion[] PNJbody = new TextureRegion[PNJNumber];
	public TextureRegion[][] PNJlegs = new TextureRegion[PNJNumber][];
	public TextureRegion[][] PNJeyes = new TextureRegion[PNJNumber][];
	
	public TextureRegion[] PNJarmTexture = new TextureRegion[PNJNumber];
	
	
	
	public Texture PNJAtlas;
	
	public TextureRegion[] PNJHelmet = new TextureRegion[PNJArmorNumber];
	public TextureRegion[] PNJArmor = new TextureRegion[PNJArmorNumber];
	public TextureRegion[] PNJGlove = new TextureRegion[PNJArmorNumber];
	public TextureRegion[] PNJLegginsList = new TextureRegion[PNJArmorNumber];
	public TextureRegion[][] PNJLeggins = new TextureRegion[PNJArmorNumber][];
	
	
	
	public TextureRegion miningHelmet;
	public TextureRegion bow;
	public TextureRegion grapple;
	public TextureRegion grappleGreen;
	public TextureRegion arrow;
	public TextureRegion vespic;
	public ArrayList<TextureRegion> swords = new ArrayList<TextureRegion>();
	public TextureRegion bigsword;
	
	public TextureRegion joy;
	
	public TextureRegion blank;
	
	public Texture materials;

	Coordinates DirtCoordinates = new Coordinates(270,0);	
	Coordinates UnbreakCoordinates = new Coordinates(30,0);	
	Coordinates CobbleCoordinates = new Coordinates(0,100);	
	Coordinates GrassCoordinates = new Coordinates(60,0);
	Coordinates IronCoordinates = new Coordinates(90,0);
	Coordinates GoldCoordinates = new Coordinates(120,0);
	Coordinates MachaliumCoordinates = new Coordinates(150,0);
	Coordinates DragoniumCoordinates = new Coordinates(180,0);
	Coordinates CarbaiumCoordinates = new Coordinates(210,0);
	Coordinates EltaliumCoordinates = new Coordinates(240,0);
	Coordinates WoodCoordinates = new Coordinates(180,100);
	Coordinates StoneBricksCoordinates = new Coordinates(90,100);
	Coordinates GlassCoordinates = new Coordinates(90,160);
	Coordinates IronBlockCoordinates = new Coordinates(0,160);
	Coordinates IceCoordinates = new Coordinates(360,100);
	Coordinates LogCoordinates = new Coordinates(270,100);
	Coordinates FossileCoordinates = new Coordinates(420,0);
	Coordinates CrystalPurpleCoordinates = new Coordinates(360,160);
	Coordinates CrystalGreenCoordinates = new Coordinates(270,160);
	Coordinates CrystalRedCoordinates = new Coordinates(180,160);
	Coordinates CrystalBlueCoordinates = new Coordinates(90,220);
	Coordinates CrystalDarkCoordinates = new Coordinates(0,220);
	Coordinates TorchCoordinates = new Coordinates(90,20);
	Coordinates CrystalFireCoordinates1 = new Coordinates(120,20);
	Coordinates CrystalFireCoordinates2 = new Coordinates(150,20);
	Coordinates CrystalFireCoordinates3 = new Coordinates(180,20);
	Coordinates CrystalFireCoordinates4 = new Coordinates(150,20);
	Coordinates TorchCoordinates2 = new Coordinates(210,20);
	Coordinates TorchCoordinates3= new Coordinates(240,20);
	Coordinates TorchCoordinates4 = new Coordinates(270,20);
	Coordinates TorchCoordinates5 = new Coordinates(300,20);
	Coordinates MudCoordinates = new Coordinates(300,0);
	
	TextureRegion occlusion;
	TextureRegion occlusionl;
	TextureRegion occlusionr;
	TextureRegion occlusionu;
	TextureRegion occlusiond;
	TextureRegion occlusionlr;
	TextureRegion occlusionlu;
	TextureRegion occlusionld;
	TextureRegion occlusionru;
	TextureRegion occlusionrd;
	TextureRegion occlusionud;
	TextureRegion occlusionlru;
	TextureRegion occlusionlrd;
	TextureRegion occlusionlud;
	TextureRegion occlusionrud;
	TextureRegion occlusionlrud;
	
	TextureRegion[] grass =  new TextureRegion[4];
	TextureRegion[] dirtBorder =  new TextureRegion[4];
	TextureRegion[] dirtBorderHor =  new TextureRegion[4];
	
	TextureRegion pause;
	TextureRegion pauseexit;
	TextureRegion pauseresume;
	TextureRegion pausesettings;
	TextureRegion pauseback;
	TextureRegion pauseArrowL;
	TextureRegion pauseArrowR;
	TextureRegion pausecheat;
	TextureRegion pauseButton;
	
	TextureRegion chest;
	TextureRegion bed;
	TextureRegion chair;
	TextureRegion anvil;
	
	Texture rope;
	Texture ropeGreen;
	
	public TextureRegion newTexture(FileHandle file)
	{
		TextureRegion r = new TextureRegion(new Texture(file));
		packedTextures.add(r);
		return r;
	}
	public TextureRegion newTexture(String file)
	{
		TextureRegion r = new TextureRegion(new Texture(Gdx.files.internal(file)));
		packedTextures.add(r);
		return r;
	}
	
	public TextureManager() 
	{
		
		Pixmap pm = new Pixmap(Gdx.files.internal("others/mouse.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();
		
		//TEXTURES
		rope = new Texture("weapons/Hooks/rope2.png");
		rope.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		ropeGreen = new Texture("weapons/Hooks/rope.png");
		ropeGreen.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		materials = new Texture("materials/materials.png");
		
		PNJAtlas = new Texture("players/PNJAtlas.png");
		
		empty = newTexture(Gdx.files.internal("others/empty.png"));

		item = newTexture(Gdx.files.internal("icones/items.png"));
		tool = newTexture(Gdx.files.internal("icones/tools.png"));
		
		badwolf = newTexture("mobs/loupmechant.png");
		hornet = newTexture("mobs/insectehor.png");
		dinorouge = newTexture("mobs/dinorouge.png");
		biginsecte = newTexture("mobs/biginsecte.png");
		
		bonhommeWeapon = newTexture("weapons/PickAxes/pickaxe1.png");
		greenSceptre = newTexture("weapons/Magic/sceptreGreen.png");
		redSceptre = newTexture("weapons/Magic/sceptreRed.png");
		purpleSceptre = newTexture("weapons/Magic/sceptrePurple.png");
		blueSceptre = newTexture("weapons/Magic/sceptreBlue.png");
		blackSceptre = newTexture("weapons/Magic/sceptreDark.png");
		bow = newTexture("weapons/Bows/bow.png");
		grapple = newTexture("weapons/Hooks/grapple2.png");
		grappleGreen = newTexture("weapons/Hooks/grapple.png");
		
		bigsword = newTexture("weapons/Swords/greatsword.png");
		arrow = newTexture("weapons/Ammos/arrow.png");
		vespic = newTexture("weapons/Ammos/vespic.png");
		woodStick = newTexture("weapons/Swords/woodstick.png");
		woodStickAndStone = newTexture("weapons/Swords/woodstickandrock.png");
		
		Ttroncs = newTexture("trees/troncs.png");
		TbranchesD = newTexture("trees/branchesgauches.png");
		TbranchesG = newTexture("trees/branchesdroites.png");
		Tcimes = newTexture("trees/cimes.png");
		
		backText = newTexture(Gdx.files.internal("inventory/pancarte-grande.png"));
		selectDefault = newTexture(Gdx.files.internal("inventory/sele.png"));
		backTrashTexture = newTexture(Gdx.files.internal("inventory/TrashPanel.png"));
		invButtonTexture = newTexture(Gdx.files.internal("inventory/OpenInvPanell.png"));
		pauseButton = newTexture(Gdx.files.internal("menu/pausebutton.png"));
		craftNew = newTexture("inventory/craftnew.png");
		craftButton = newTexture("inventory/craftbutton.png");
		
		backLBText = newTexture(Gdx.files.internal("inventory/behindLifePanel.png"));
		LBText = newTexture(Gdx.files.internal("inventory/LifePanel.png"));
		redLBText = newTexture(Gdx.files.internal("inventory/redLifePanel.png"));
		greenLBText = newTexture(Gdx.files.internal("inventory/greenLifePanel.png"));
		
		loadingV = newTexture("loading/loadingvide.png");
		loadingShadow = newTexture("loading/ombreloading.png");
		
		loadingJ = new Texture("loading/loadingjauge.png");
		cad1 = new Texture("loading/cad1.png");
		cad2 = new Texture("loading/cad2.png");
		cad3 = new Texture("loading/cad3.png");
		cad4 = new Texture("loading/cad4.png");
		cad5 = new Texture("loading/cad5.png");
		cad6 = new Texture("loading/cad6.png");
		cad7 = new Texture("loading/cad7.png");
		backGround = new Texture("loading/bgloading.png");
		backGroundChar = new Texture("loading/charloading.png");
		roue1 = new Texture("loading/roue1.png");
		roue2 = new Texture("loading/roue2.png");
		
		backn = new Texture("background/backNight.png");
		back = new Texture("background/back.png");
		layer1 = new Texture("background/1.png");
		layer2 = new Texture("background/layer2.png");
		layer3 = new Texture("background/layer3.png");
		layer4 = new Texture("background/layer4.png");
		layer5 = new Texture("background/layer5.png");
		
		
		
		armTextureOld = newTexture(Gdx.files.internal("players/arm1old.png"));
		
		rain = newTexture(Gdx.files.internal("others/raindrop.png"));
		
		particle = newTexture(Gdx.files.internal("particles/particle1.png"));
		
		selecbacktext = newTexture("inventory/woodSelector.png");
		torchText = newTexture(Gdx.files.internal("inventory/torch.png"));
		cadre = newTexture(Gdx.files.internal("others/cadre.png"));
		
		portalTexture = newTexture(Gdx.files.internal("decors/portail.png"));
		doorForgeTexture = newTexture(Gdx.files.internal("decors/doorArmes.png"));
		doorArmorForgeTexture = newTexture(Gdx.files.internal("decors/doorArmure.png"));
		doorFleuristeTexture = newTexture(Gdx.files.internal("decors/doorFleuriste.png"));
		
		forgeTexture = newTexture(Gdx.files.internal("decors/forge-armes.png"));
		armorForgeTexture = newTexture(Gdx.files.internal("decors/forge-armures.png"));
		fleuristeTexture = newTexture(Gdx.files.internal("decors/fleuriste.png"));
		
		sGraphicsButton = newTexture(Gdx.files.internal("menu/sGraphicsButton.png"));
		sAudioButton = newTexture(Gdx.files.internal("menu/sAudioButton.png"));
		sKeyboardButton = newTexture(Gdx.files.internal("menu/sKeyboardButton.png"));
		sDisplayButton = newTexture(Gdx.files.internal("menu/sDisplayButton.png"));
		sButtonHolder = newTexture(Gdx.files.internal("menu/sButtonHolder.png"));
		
		chatBehind = newTexture(Gdx.files.internal("menu/chatbehind.png"));
		playButton = newTexture(Gdx.files.internal("menu/playButton.png"));
		hostButton = newTexture(Gdx.files.internal("menu/hostButton.png"));
		joinButton = newTexture(Gdx.files.internal("menu/joinButton.png"));
		settingsButton = newTexture(Gdx.files.internal("menu/settingsButton.png"));
		charEditButton = newTexture(Gdx.files.internal("menu/charEditButton.png"));
		ipButton = newTexture(Gdx.files.internal("menu/ipButton.png"));
		textBoxSurline = newTexture(Gdx.files.internal("menu/textBoxSurline.png"));
		textBoxCursor = newTexture(Gdx.files.internal("menu/textBoxCursor.png"));
		casque1 = newTexture(Gdx.files.internal("armors/casque1.png"));
		miningHelmet = newTexture(Gdx.files.internal("armors/casquelight.png"));
		armor1 = newTexture(Gdx.files.internal("armors/armor1.png"));
		arms1 = newTexture(Gdx.files.internal("armors/arms1.png"));
		leggins1 = newTexture(Gdx.files.internal("armors/leggins1.png"));
		
		for(int i = 0; i<PNJNumber; i++)
		{
			PNJeye[i] = new TextureRegion(PNJAtlas,468,i*26,52,26);
			PNJhead[i] = new TextureRegion(PNJAtlas,416,i*26,26,26);
			PNJhairs[i] = new TextureRegion(PNJAtlas,442,i*26,26,26);
			PNJlegList[i] = new TextureRegion(PNJAtlas,520,i*26,312,26);
			PNJbody[i] = new TextureRegion(PNJAtlas,390,i*26,26,26);
			PNJarmTexture[i] = new TextureRegion(PNJAtlas,832,i*26,26,26);
		}
		
		for(int i = 0; i<PNJArmorNumber; i++)
		{
			PNJArmor[i] = new TextureRegion(PNJAtlas,0,i*26,26,26);
			PNJHelmet[i] = new TextureRegion(PNJAtlas,26,i*26,26,26);
			PNJLegginsList[i] = new TextureRegion(PNJAtlas,2*26,i*26,312,26);
			PNJGlove[i] = new TextureRegion(PNJAtlas,14*26,i*26,26,26);
		}
		
		joy = newTexture(Gdx.files.internal("menu/joystick.png"));
		blank = newTexture(Gdx.files.internal("others/blank.png"));
		
		pause = newTexture(Gdx.files.internal("menu/pause.png"));
		
		chest = newTexture(Gdx.files.internal("furnitures/chest.png"));
		bed = newTexture(Gdx.files.internal("furnitures/bed.png"));
		chair = newTexture(Gdx.files.internal("furnitures/chaise1.png"));
		anvil = newTexture(Gdx.files.internal("furnitures/anvil.png"));
		
		swords.add(newTexture("weapons/Swords/sword.png"));
		swords.add(newTexture("weapons/Swords/sword1.png"));
		swords.add(newTexture("weapons/Swords/sword2.png"));
		swords.add(newTexture("weapons/Swords/sword3.png"));
		swords.add(newTexture("weapons/Swords/sword4.png"));
		swords.add(newTexture("weapons/Swords/sword5.png"));
		swords.add(newTexture("weapons/Swords/sword6.png"));
		swords.add(newTexture("weapons/Swords/sword7.png"));
		swords.add(newTexture("weapons/Swords/sword8.png"));
		swords.add(newTexture("weapons/Swords/sword9.png"));
		//TODO
		
		
		
		//PACKING
		for(int i = 0; i<packedTextures.size(); i++)
		{
			packedTextures.get(i).getTexture().getTextureData().prepare();
			packer.pack(String.valueOf(i), packedTextures.get(i).getTexture().getTextureData().consumePixmap());
			packedTextures.get(i).getTexture().getTextureData().disposePixmap();
			packedTextures.get(i).getTexture().dispose();
		}
		
		atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
		
		/*PixmapPackerIO pl = new PixmapPackerIO();
		try {
			pl.save(Gdx.files.local("truc.pack"), packer);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		packer.dispose();
		
		for(int i = packedTextures.size()-1; i>=0; i--)
		{
			TextureRegion reg = atlas.findRegion(String.valueOf(i));
			reg.setRegion(reg.getRegionX()+packedTextures.get(i).getRegionX(), reg.getRegionY()+packedTextures.get(i).getRegionY(), packedTextures.get(i).getRegionWidth(), packedTextures.get(i).getRegionHeight());
			packedTextures.get(i).setRegion(reg);
		}
		
		for(int i = 0; i<PNJNumber; i++)
		{
			PNJlegs[i] = PNJlegList[i].split(PNJlegList[i].getRegionWidth()/12, PNJlegList[i].getRegionHeight())[0];
			PNJeyes[i] = PNJeye[i].split(PNJeye[i].getRegionWidth()/2, PNJeye[i].getRegionHeight())[0];
		}
		
		for(int i = 0; i<PNJArmorNumber; i++)
		{
			PNJLeggins[i] = PNJLegginsList[i].split(PNJLegginsList[i].getRegionWidth()/12, PNJLegginsList[i].getRegionHeight())[0];
		}
		
		
		wolf[0] = new TextureRegion(badwolf, 0, 0, 41, 22);
		wolf[1] = new TextureRegion(badwolf, 41, 0, 41, 22);
		wolf[2] = new TextureRegion(badwolf, 164/2, 0, 41, 22);
		wolf[3] = new TextureRegion(badwolf, 246/2, 0, 41, 22);
		wolf[4] = new TextureRegion(badwolf, 328/2, 0, 41, 22);
		wolf[5] = new TextureRegion(badwolf, 410/2, 0, 41, 22);
		wolf[6] = new TextureRegion(badwolf, 492/2, 0, 41, 22);
		wolf[7] = new TextureRegion(badwolf, 574/2, 0, 41, 22);
		wolf[8] = new TextureRegion(badwolf, 656/2, 0, 41, 22);
		wolf[9] = new TextureRegion(badwolf, 738/2, 0, 41, 22);
		
		hor[0] = new TextureRegion(hornet, 0, 0, 32, 17);
		hor[1] = new TextureRegion(hornet, 32, 0, 32, 17);
		
		veswings[0] = new TextureRegion(biginsecte, 0, 0, 100, 96);
		veswings[1] = new TextureRegion(biginsecte, 102, 0, 100, 96);
		
		vesbody[0] = new TextureRegion(biginsecte, 204, 0, 100, 96);
		vesbody[1] = new TextureRegion(biginsecte, 0, 96, 100, 96);
		vesbody[2] = new TextureRegion(biginsecte, 102, 96, 100, 96);
		vesbody[3] = new TextureRegion(biginsecte, 204, 96, 100, 96);
		
		rains[0] = new TextureRegion(rain,0,0,16,16);
		rains[1] = new TextureRegion(rain,16,0,16,16);
		rains[2] = new TextureRegion(rain,32,0,16,16);
		rains[3] = new TextureRegion(rain,48,0,16,16);
		
		legginsArray1 = leggins1.split(leggins1.getRegionWidth()/12, leggins1.getRegionHeight())[0];
		
		
		pauseresume = new TextureRegion(pause,0,0,122,44);
		pauseexit = new TextureRegion(pause,0,46,122,44);
		pausesettings = new TextureRegion(pause,0,92,122,44);
		pauseArrowL = new TextureRegion(pause,0,138,22,22);
		pauseArrowR = new TextureRegion(pause,22,138,22,22);
		pauseback = new TextureRegion(pause,0,162,122,44);
		pausecheat = new TextureRegion(pause,0,208,122,44);
		
		CoordinatesBlocs[1] = DirtCoordinates;
		CoordinatesBlocs[2] = UnbreakCoordinates;
		CoordinatesBlocs[3] = CobbleCoordinates;
		CoordinatesBlocs[4] = GrassCoordinates;
		CoordinatesBlocs[5] = IronCoordinates;
		CoordinatesBlocs[6] = GoldCoordinates;
		CoordinatesBlocs[7] = MachaliumCoordinates;
		CoordinatesBlocs[8] = DragoniumCoordinates;
		CoordinatesBlocs[9] = CarbaiumCoordinates;
		CoordinatesBlocs[10] = EltaliumCoordinates;
		CoordinatesBlocs[11] = WoodCoordinates;
		CoordinatesBlocs[12] = StoneBricksCoordinates;
		CoordinatesBlocs[13] = GlassCoordinates;
		CoordinatesBlocs[14] = IronBlockCoordinates;
		CoordinatesBlocs[15] = IceCoordinates;
		CoordinatesBlocs[16] = LogCoordinates;
		CoordinatesBlocs[17] = FossileCoordinates;
		CoordinatesBlocs[18] = CrystalPurpleCoordinates;
		CoordinatesBlocs[19] = CrystalGreenCoordinates;
		CoordinatesBlocs[20] = CrystalRedCoordinates;
		CoordinatesBlocs[21] = CrystalBlueCoordinates;
		CoordinatesBlocs[22] = CrystalDarkCoordinates;
		CoordinatesBlocs[23] = TorchCoordinates;
		CoordinatesBlocs[24] = CrystalFireCoordinates1;
		CoordinatesBlocs[25] = CrystalFireCoordinates2;
		CoordinatesBlocs[26] = CrystalFireCoordinates3;
		CoordinatesBlocs[27] = CrystalFireCoordinates4;
		CoordinatesBlocs[28] = TorchCoordinates2;
		CoordinatesBlocs[29] = TorchCoordinates3;
		CoordinatesBlocs[30] = TorchCoordinates4;
		CoordinatesBlocs[31] = TorchCoordinates5;
		CoordinatesBlocs[32] = MudCoordinates;
		
		for(int i = 0; i<ParticleBlocs.length; i++)
		{
			if(CoordinatesBlocs[i]!=null)ParticleBlocs[i] = new TextureRegion(materials, CoordinatesBlocs[i].x+2, CoordinatesBlocs[i].y+2, 4, 4);
		}
		
		TextureRegion[][][] tempAll = new TextureRegion[AllBlocTypes.instance.TileSetNumber][sY][sX];
		TextureRegion[][][] tempAllOld = new TextureRegion[AllBlocTypes.instance.TileSetNumber][bY][bX];
		
		
		occlusion = new TextureRegion(materials,4,4,8,8);
		occlusionl = new TextureRegion(materials,0,4,12,8);
		occlusionr = new TextureRegion(materials,4,4,12,8);
		occlusionu = new TextureRegion(materials,4,0,8,12);
		occlusiond = new TextureRegion(materials,4,4,8,12);
		occlusionlr = new TextureRegion(materials,0,4,16,8);
		occlusionlu = new TextureRegion(materials,0,0,12,12);
		occlusionld = new TextureRegion(materials,0,4,12,12);
		occlusionru = new TextureRegion(materials,4,0,12,12);
		occlusionrd = new TextureRegion(materials,4,4,12,12);
		occlusionud = new TextureRegion(materials,4,0,8,16);
		occlusionlru = new TextureRegion(materials,0,0,16,12);
		occlusionlrd = new TextureRegion(materials,0,4,16,12);
		occlusionlud = new TextureRegion(materials,0,0,12,16);
		occlusionrud = new TextureRegion(materials,4,0,12,16);
		occlusionlrud = new TextureRegion(materials,0,0,16,16);

		grass[0] = new TextureRegion(materials,0,19,8,16);
		grass[1] = new TextureRegion(materials,8,19,8,16);
		grass[2] = new TextureRegion(materials,16,19,8,16);
		grass[3] = new TextureRegion(materials,24,19,8,16);
		
		dirtBorder[0] = new TextureRegion(materials,1,40,2,8);
		dirtBorderHor[0] = new TextureRegion(materials,5,40,8,2);
		dirtBorder[1] = new TextureRegion(materials,15,40,2,8);
		dirtBorderHor[1] = new TextureRegion(materials,19,40,8,2);
		
		
		
		for(int i = 1; i<CoordinatesBlocs.length; i++)
		{
			TextureRegion tempTex;
			if(CoordinatesBlocs[i].y>=100)tempTex = new TextureRegion(materials, CoordinatesBlocs[i].x, CoordinatesBlocs[i].y, 90, 60);
			else tempTex = new TextureRegion(materials, CoordinatesBlocs[i].x, CoordinatesBlocs[i].y, 30, 20);
			
			
			if(tempTex.getRegionWidth()<32)tempAll[i] = tempTex.split(tempTex.getRegionWidth()/sX, tempTex.getRegionHeight()/sY);
			else tempAllOld[i] = tempTex.split(tempTex.getRegionWidth()/bX, tempTex.getRegionHeight()/bY);
		}
		
		for(int i = 1; i<tempAll.length; i++)
		{
			if(tempAll[i][0][0] != null)
			{
				for(int j = 0; j<tempAll[i].length; j++)
				{
					for(int h = 0; h<tempAll[i][j].length; h++)
					{
						tempAll[i][j][h].setRegion(tempAll[i][j][h].getRegionX(), tempAll[i][j][h].getRegionY(), 8, 8);
						TextureRegions[i][j*sX+h] = tempAll[i][j][h];
					}
				}
			}
			else
			{
				for(int j = 0; j<tempAllOld[i].length; j++)
				{
					for(int h = 0; h<tempAllOld[i][j].length; h++)
					{
						tempAllOld[i][j][h].setRegion(tempAllOld[i][j][h].getRegionX(), tempAllOld[i][j][h].getRegionY(), 8, 8);
						TextureRegions[i][j*bX+h] = tempAllOld[i][j][h];
					}
				}
			}
		}
	}
}
