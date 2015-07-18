package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ModifyTerrain {

	public static ModifyTerrain instance;
	
	private float lastTime;
	private float lastTimePose;
	private float ratePose = 0.1f;
	public static float range = 160;
	
	public boolean normalWay = true;
	
	int decalaX;
	int decalaY;
	int newCell;
	int selectorId;
	int id;
	
	Vector2 distToMiddleVec = new Vector2();
	float distToMiddle = 0;
	
	boolean handLapse;
	boolean isLeft = false;
	
	int currentDurability = 1;
	
	int oldnewCell;
	int oldclicCell;
	Vector2 oldclicPos;
	Vector2 oldBreakPos;
	Vector2 cellPos;
	
	float time;
	
	boolean oldLayer;
	BlocType newCellType;


	public ModifyTerrain() 
	{
		oldclicPos = new Vector2(0,0);
		oldBreakPos = new Vector2(0,0);
	}
	
	public boolean PutBlock(MapLayer layer)
	{
		boolean success = false;
		
		lastTime = time;
		handLapse = true;
		if(!isThereObstacle() || (layer == Map.instance.backLayer))// && !isThereObstacleHouse()) )
		{
			newCellType = AllBlocTypes.instance.getType(newCell);

			//IS OBSIDIAN = NO ACTION
			if(!getCell(layer,decalaX+0,decalaY+0).unbreakable && (!AllBlocTypes.instance.getType(newCell).needBack || getCell(Map.instance.backLayer,decalaX+0,decalaY+0).collide))
			{
				float ratePose2 = ratePose;
				if(Parameters.i.ultrarate)ratePose2/=100;
				//IS PLACING TILE
				if(time>lastTimePose+ratePose2 && getCell(layer,decalaX+0,decalaY+0).air && !newCellType.air)// && (true || layer != oldLayer || decalaX != oldclicPos.x || decalaY != oldclicPos.y))
				{
					lastTimePose = time;
					SetCell(layer,AllBlocTypes.instance.getType(newCell),AllBlocTypes.instance.full,decalaX,decalaY);
					MapChecker.instance.CheckCell(layer,decalaX,decalaY);
					MapChecker.instance.CheckCell(layer,decalaX+1,decalaY);
					MapChecker.instance.CheckCell(layer,decalaX,decalaY+1);
					MapChecker.instance.CheckCell(layer,decalaX-1,decalaY);
					MapChecker.instance.CheckCell(layer,decalaX,decalaY-1);
					if(normalWay)GameScreen.inventory.invItemsArray[selectorId][0].number--;
					Tools.checkItems();
					success = true;
				}
			}
			oldnewCell = newCell;
		}
		return success;
	}
	
	
	
	public boolean isThereObstacle()
	{
		return (isThereObstaclePlayer() || AllEntities.getEntity(decalaX,decalaY)!=null);//  || isThereObstacleMob()>=0 ||  isThereObstacleHouse());
	}
	
	public Furniture isThereObstacleFurniture(int x,int y)
	{
		if(AllEntities.getType(x, y) == AllEntities.furnituretype)return (Furniture)AllEntities.getEntity(x, y);
		return null;
	}
	
	public boolean isThereObstaclePlayer()
	{
		if(GameScreen.player.isTouched(decalaX*16+8, decalaY*16+8) && AllBlocTypes.instance.getType(newCell).collide)return true;
		return false;
	}
	
	public Tree isThereObstacleTree(int x,int y)
	{
		if(AllEntities.getType(x, y) == AllEntities.treetype)return (Tree)AllEntities.getEntity(x, y);
		return null;
	}
	
	
	public void breakBlock(int decalaX, int decalaY, MapLayer layer)
	{
		GameScreen.emmiter.addParticles(5, TextureManager.instance.ParticleBlocs[getCell(layer,decalaX,decalaY).Id], decalaX*16+8, decalaY*16+8);
		SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].setPitch(SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].play(),getCell(layer,decalaX,decalaY).pitch);
		Item nit = GameScreen.items.CreateItem(getCell(layer,decalaX,decalaY).dropId,decalaX*16+8+(float)(Math.random()*5),decalaY*16+8);
		GameScreen.items.placeItem(nit);
		SetCell(layer,AllBlocTypes.instance.Empty, AllBlocTypes.instance.full,decalaX,decalaY);
		MapChecker.instance.CheckCell(layer,decalaX,decalaY);
		MapChecker.instance.CheckCell(layer,decalaX+1,decalaY);
		MapChecker.instance.CheckCell(layer,decalaX,decalaY+1);
		MapChecker.instance.CheckCell(layer,decalaX-1,decalaY);
		MapChecker.instance.CheckCell(layer,decalaX,decalaY-1);	
	}
	
	public void hurtBlock(int decalaX, int decalaY, MapLayer layer)
	{
		GameScreen.emmiter.addParticles(5, TextureManager.instance.ParticleBlocs[getCell(layer,decalaX,decalaY).Id], decalaX*16+8, decalaY*16+8);
		SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].setPitch(SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].play(),getCell(layer,decalaX,decalaY).pitch);
		MapChecker.instance.CheckCell(layer,decalaX,decalaY);
	}
	
	public boolean furnitureCollision(int id, int decalaxX, int decalaxY)
	{
		boolean ok = true;
		for(int i = 0; i<AllTools.instance.getType(id).furnitureWidth; i++)
		{
			for(int j = 0; j<AllTools.instance.getType(id).furnitureHeight; j++)
			{
				if(!getCell(Map.instance.mainLayer,decalaxX+i,decalaxY+j).air || AllEntities.getEntity(decalaxX+i, decalaxY+j)!=null)ok = false;
			}
			if(!getCell(Map.instance.mainLayer,decalaxX+i,decalaxY-1).collide)ok = false;
		}
		return ok;
	}
	
	public boolean treeCollision(int decalaX, int decalaY)
	{
		if(getCell(Map.instance.mainLayer,decalaX,decalaY).air && getCell(Map.instance.mainLayer,decalaX+1,decalaY).air && AllEntities.getEntity(decalaX, decalaY)==null  && AllEntities.getEntity(decalaX+1, decalaY)==null
			&&getCell(Map.instance.mainLayer,decalaX,decalaY+1).air && getCell(Map.instance.mainLayer,decalaX+1,decalaY+1).air && AllEntities.getEntity(decalaX, decalaY+1)==null  && AllEntities.getEntity(decalaX+1, decalaY+1)==null)
		{
			if(getCell(Map.instance.mainLayer,decalaX,decalaY-1).fertile && getCell(Map.instance.mainLayer,decalaX+1,decalaY-1).fertile)
			{
				return false;
			}
		}
		return true;
	}
	
	public void UseItem(MapLayer layer, boolean hand)
	{
		boolean obstacleProof = false;
		boolean canCutTrees = true;
		float efficiency = 1;
		float rate = 0.3f;
		int type =  AllTools.instance.Axe;
		float range = ModifyTerrain.range;
		
		if(!hand)
		{
			obstacleProof = AllTools.instance.getType(id).obstacleProof;
			canCutTrees = AllTools.instance.getType(id).canCutTrees;
			efficiency = AllTools.instance.getType(id).efficiency;
			rate = AllTools.instance.getType(id).rate;
			type = AllTools.instance.getType(id).type;
			range = AllTools.instance.getType(id).range;
		}
		
		if(type == AllTools.instance.Sceptre && !isLeft && AllTools.instance.getType(id).magiclight)
		{	
			Map.instance.lights.torche(GameScreen.player.launcherCoord, GameScreen.player.velocityCoord, AllTools.instance.getType(id).torchangle,2,2,2,false,0);
		}
		
		if(Parameters.i.ultrarange)range = 100000;
		if(Parameters.i.ultrarate)rate/=10;
		
		//IS OBSIDIAN = NO ACTION
		if(!getCell(layer,decalaX,decalaY).unbreakable)
		{
			boolean pass = true;
			if(isThereObstacle() && !obstacleProof)
			{
				Tree tree = isThereObstacleTree(decalaX,decalaY);
				if(pass && tree!=null && canCutTrees && isLeft)
				{
					tree.BreakTree(efficiency);
					pass = false;
					lastTime = time;
				}
				Furniture furn = isThereObstacleFurniture(decalaX,decalaY);
				if(pass && furn!=null && canCutTrees && isLeft && range>=distToMiddle)
				{
					if(furn.isEmpty())
					{
						furn.isDead = true;
						SoundManager.instance.Break[1].setPitch(SoundManager.instance.Break[1].play(),1);
						Item nit = GameScreen.items.CreateItem(furn.type,decalaX*16+8+(float)(Math.random()*5),furn.y*16+furn.casesY*8);
						GameScreen.items.placeItem(nit);
						lastTime = time;
					}
					pass = false;
				}
			}
			
			if(pass && time>lastTime+rate)
			{
				lastTime = time;
				
				//PICKAXE
				if(type == AllTools.instance.Axe && range>=distToMiddle)// && !isThereObstacleHouse())
				{
					if(!getCell(layer,decalaX,decalaY).air)
					{
						currentDurability-=efficiency;
						if(currentDurability<=0)
						{
							breakBlock(decalaX,decalaY,layer);
						}
						else
						{
							hurtBlock(decalaX,decalaY,layer);
						}
					}
				}
				else if(type == AllTools.instance.Furniture && range>=distToMiddle)
				{
					if(furnitureCollision(id,decalaX,decalaY))
					{
						GameScreen.entities.furnitures.addFurniture(decalaX, decalaY, id);
						GameScreen.inventory.invItemsArray[selectorId][0].number--;
						Tools.checkItems();
					}
				}
				else if(type == AllTools.instance.Tree && range>=distToMiddle)
				{
					 if(!treeCollision(decalaX, decalaY))
					 {
						 boolean Ok = true;

						if(Ok)
						{
							GameScreen.entities.trees.PlaceTree(decalaX, decalaY, 0,20);
							GameScreen.inventory.invItemsArray[selectorId][0].number--;
							Tools.checkItems();
						}
					 }
				}
				else if(type == AllTools.instance.Sceptre)
				{
					if(AllTools.instance.getType(id).magicprojectile)
					{
						if(isLeft && Tools.raycast(GameScreen.player.shoulderCoord.x, GameScreen.player.shoulderCoord.y, GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, 4).x<0)
						{
							GameScreen.entities.projectiles.AddMagicProjectile(GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, GameScreen.player.velocityCoord.x, GameScreen.player.velocityCoord.y, AllTools.instance.getType(id).sceptreType,  AllTools.instance.getType(id).damage, GameScreen.player);
						}
					
					}
				}
				else if(type == AllTools.instance.Bow)
				{
					if(isLeft && Tools.raycast(GameScreen.player.shoulderCoord.x, GameScreen.player.shoulderCoord.y, GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, 4).x<0 && GameScreen.inventory.remove(AllTools.instance.getType(id).arrowType, 1))
						GameScreen.entities.projectiles.AddProjectile(GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, GameScreen.player.velocityCoord2.x, GameScreen.player.velocityCoord2.y, AllTools.instance.getType(id).arrowType, GameScreen.player, AllTools.instance.getType(id).damage);
				}
				else if(type == AllTools.instance.Hook && Inputs.instance.leftmousedown)
				{
					if(isLeft && !GameScreen.player.grapple.playerAttached)
					{
						if(AllTools.instance.getType(id).grapple)
						GameScreen.entities.projectiles.AddGrapple(GameScreen.player.hookCoord.x, GameScreen.player.hookCoord.y, GameScreen.player.velocityCoord2.x, GameScreen.player.velocityCoord2.y, AllTools.instance.getType(id).grappleDistance, AllTools.instance.getType(id).grappleTex, AllTools.instance.getType(id).grappleTexRope);
					}
					else
					{
						lastTime-=rate;
					}
				}
			}
		}	
	}
	
	public void PutCell(int id, int selectorId, MapLayer layer,float time, boolean left)
	{
		normalWay = true;
		Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);		
		GameScreen.camera.unproject(worldCoordinates);
		decalaX = Tools.floor (worldCoordinates.x/layer.getTileWidth());
		decalaY = Tools.floor (worldCoordinates.y/layer.getTileHeight());
		UseInit(id, selectorId, layer, time, left);
	}
	
	public boolean UseAbsolute(float x, float y, int id, int selectorId, MapLayer layer,float time, boolean left)
	{
		normalWay = false;
		decalaX = Tools.floor (x/layer.getTileWidth());
		decalaY = Tools.floor (y/layer.getTileHeight());
		return UseInit(id, selectorId, layer, time, left);
	}
	
	public boolean UseInit(int id, int selectorId, MapLayer layer,float time, boolean left)
	{
		boolean success = false;
		
		isLeft = left;
		this.selectorId = selectorId;
		this.time = time;
		this.id = id;
		
		newCell = 0;
		
		
		int limit = 0;
		if(GameScreen.noLimit)limit =1000; 
		
		if(decalaX>=-limit && decalaY>1 && decalaX<layer.getWidth()+limit && decalaY<Map.instance.randomHeight && !GameScreen.isVillage())
		{		
			if(id == 0 || (id>999 && AllTools.instance.getType(id).isUseLess) || (id>999 && AllTools.instance.getType(id).type == AllTools.instance.Axe))
			{
				
				if(Inputs.instance.shift)
				{
					Vector2 mous = Tools.getAbsoluteMouse();
					
					Vector2 defaultVec = new Vector2(mous).sub(GameScreen.player.shoulderCoord);
					
					float range = AllTools.instance.getType(id).range+8;
					if(Parameters.i.ultrarange)range = 1000000;
					
					Vector2 faster = new Vector2(1000000,1000000);
					Vector2 bestaimed = new Vector2(-1,-1);
					float limitlen = Math.min(range, new Vector2(decalaX*16+8 - GameScreen.player.middle.x,decalaY*16+8 - GameScreen.player.middle.y).len());
					
					for(int i = 0; i<7; i++)
					{
						Vector2 aimed = new Vector2(Tools.raycastVec(GameScreen.player.middle.x, GameScreen.player.middle.y-22+9*(i), defaultVec.x, defaultVec.y, 4));
						
						
						if(aimed.y>=0)
						{
							Vector2 testvec = new Vector2(aimed.x*16+8 - GameScreen.player.middle.x,aimed.y*16+8 - GameScreen.player.middle.y);

							if(faster.len()>testvec.len() && limitlen>=testvec.len())
							{
								faster = testvec;
								bestaimed = aimed;
							}
						}
					}
					
					if(bestaimed.y>=0)
					{
						decalaX = Tools.floor(bestaimed.x+0.1f);
						decalaY = Tools.floor(bestaimed.y+0.1f);
					}
							
				}
				
				if(decalaX != oldBreakPos.x || decalaY != oldBreakPos.y || oldLayer != layer.isMain)
				{
					currentDurability = getCell(layer,decalaX,decalaY).durability;
					oldBreakPos.x = decalaX;
					oldBreakPos.y = decalaY;
				}
				
				
				distToMiddleVec.x = decalaX*16+8-GameScreen.player.middle.x;
				distToMiddleVec.y = decalaY*16+8-GameScreen.player.middle.y;
				distToMiddle = distToMiddleVec.len();
				if(id == 0 || (id>999 && AllTools.instance.getType(id).isUseLess))UseItem(layer, true);
				if(id>999 && AllTools.instance.getType(id).type == AllTools.instance.Axe)UseItem(layer, false);
			}
			else if(id>999)
			{
				
				distToMiddleVec.x = decalaX*16+8-GameScreen.player.middle.x;
				distToMiddleVec.y = decalaY*16+8-GameScreen.player.middle.y;
				distToMiddle = distToMiddleVec.len();
				UseItem(layer,false);
			}
			else
			{
				distToMiddleVec.x = decalaX*16+8-GameScreen.player.middle.x;
				distToMiddleVec.y = decalaY*16+8-GameScreen.player.middle.y;
				distToMiddle = distToMiddleVec.len();
				if((range>=distToMiddle && justone(layer, decalaX, decalaY, id)) || Parameters.i.ultrarange)
				{
					newCell = id;
					success = PutBlock(layer);
				}
			}
		}
		oldLayer = layer.isMain;
		oldclicPos = new Vector2(decalaX,decalaY);
		
		return success;
	}
	
	public boolean justone(MapLayer layer, int x, int y, int id)
	{
		if(!AllBlocTypes.instance.getType(id).collide)return true;
		if(layer == Map.instance.mainLayer && Map.instance.backLayer.getBloc(x, y).collide)return true;
		if(layer == Map.instance.backLayer && Map.instance.mainLayer.getBloc(x, y).collide)return true;
		if(layer.getBloc(x+1, y).collide)return true;
		if(layer.getBloc(x, y+1).collide)return true;
		if(layer.getBloc(x-1, y).collide)return true;
		if(layer.getBloc(x, y-1).collide)return true;
		return false;
	}
	
	private BlocType getCell(MapLayer layer,int decalX,int decalY)
	{
		return layer.getBloc(decalX, decalY);
	}
	
	private void SetCell(MapLayer layer,BlocType cell, int state, int decalX,int decalY){
		layer.setBloc(decalX, decalY, cell, state,true);
	}
	
	public static boolean isClicked(Sprite spr,float x,float y, float margX)
	{
		if((x > spr.getX()-margX && x < spr.getX()+spr.getWidth()+margX) && (y > spr.getY() && y < spr.getY()+spr.getHeight()))return true;
		else return false;
	}
}
