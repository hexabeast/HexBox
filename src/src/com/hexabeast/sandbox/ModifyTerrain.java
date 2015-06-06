package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ModifyTerrain {

	public static ModifyTerrain instance;
	
	private float lastTime;
	private float lastTimePose;
	private float ratePose = 0.1f;
	public static float range = 160;
	
	int decalaX;
	int decalaY;
	int newCell;
	int selectorId;
	int id;
	
	boolean STRANGELIGHTS = false;
	
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
	
	public void PutBlock(MapLayer layer)
	{
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
					GameScreen.inventory.invItemsArray[selectorId][0].number--;
					Tools.checkItems();
				}
			}
			oldnewCell = newCell;
		}
	}
	
	
	
	public boolean isThereObstacle()
	{
		return (isThereObstaclePlayer() || AllEntities.getItem(decalaX,decalaY)>=0);//  || isThereObstacleMob()>=0 ||  isThereObstacleHouse());
	}
	
	public int isThereObstacleFurniture(int x,int y)
	{
		/*for (int i = 0; i < GameScreen.entities.furnitures.furnitureList.size(); i++)
		{
			if(GameScreen.entities.furnitures.furnitureList.get(i).isClicked(vworldCoordinates.x, vworldCoordinates.y))
			{
				return i;
			}
		}*/
		int r = AllEntities.getItem(x, y);
		if(r>=Constants.treelimit && r<Constants.chestlimit)return r-Constants.treelimit;
		return -1;
	}
	
	public boolean isThereObstaclePlayer()
	{
		if(GameScreen.player.isTouched(decalaX*16+8, decalaY*16+8))return true;
		return false;
	}
	
	/*public boolean isThereObstacleHouse()
	{
		if(GameScreen.constructions.isTouched(vworldCoordinates.x, vworldCoordinates.y))return true;
		return false;
	}
	
	public int isThereObstacleMob()
	{
		float x = GameScreen.getAbsoluteMouseCam1().x;
		float y = GameScreen.getAbsoluteMouseCam1().y;
		for (int i = 0; i < GameScreen.mobs.mobList.size(); i++)
		{
			if(isClicked(GameScreen.mobs.mobList.get(i).mobSprite,x,y,0))
			{
				return i;
			}
		}
		return -1;
	}*/
	
	
	public int isThereObstacleTree(int x,int y)
	{
		int r = AllEntities.getItem(x, y);
		if(r<10000000)return r;
		else return -1;
		/*for (int i = 0; i < GameScreen.entities.trees.treeList.size(); i++)
		{
			if(GameScreen.entities.trees.treeList.get(i).isCollidingTree(vworldCoordinates.x, vworldCoordinates.y))
			{
				return i;
			}
		}*/
	}
	
	
	public void breakBlock(int decalaX, int decalaY, MapLayer layer)
	{
		GameScreen.emmiter.addParticles(5, TextureManager.instance.ParticleBlocs[getCell(layer,decalaX,decalaY).Id], decalaX*16+8, decalaY*16+8);
		SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].setPitch(SoundManager.instance.Break[getCell(layer,decalaX,decalaY).soundType].play(),getCell(layer,decalaX,decalaY).pitch);
		Item nit = GameScreen.items.CreateItem(getCell(layer,decalaX,decalaY).Id,decalaX*16+8+(float)(Math.random()*5),decalaY*16+8);
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
				if(!getCell(Map.instance.mainLayer,decalaxX+i,decalaxY+j).air || AllEntities.getItem(decalaxX+i, decalaxY+j)!=-1)ok = false;
			}
			if(!getCell(Map.instance.mainLayer,decalaxX+i,decalaxY-1).collide)ok = false;
		}
		return ok;
	}
	
	public boolean treeCollision(int decalaX, int decalaY)
	{
		if(getCell(Map.instance.mainLayer,decalaX,decalaY).air && getCell(Map.instance.mainLayer,decalaX+1,decalaY).air && AllEntities.getItem(decalaX, decalaY)==-1  && AllEntities.getItem(decalaX+1, decalaY)==-1
			&&getCell(Map.instance.mainLayer,decalaX,decalaY+1).air && getCell(Map.instance.mainLayer,decalaX+1,decalaY+1).air && AllEntities.getItem(decalaX, decalaY+1)==-1  && AllEntities.getItem(decalaX+1, decalaY+1)==-1)
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
		if(Parameters.i.ultrarange)range = 100000;
		if(Parameters.i.ultrarate)rate/=10;
		
		//IS OBSIDIAN = NO ACTION
		if(!getCell(layer,decalaX,decalaY).unbreakable)
		{
			boolean pass = true;
			int tr=0;
			if(isThereObstacle() && !obstacleProof)
			{
				tr = isThereObstacleTree(decalaX,decalaY);
				if(pass && tr>=0 && canCutTrees && isLeft)
				{
					GameScreen.entities.trees.treeListAll.get(tr).BreakTree(efficiency);
					pass = false;
					lastTime = time;
				}
				tr = isThereObstacleFurniture(decalaX,decalaY);
				if(pass && tr>=0 && canCutTrees && isLeft && range>=distToMiddle)
				{
					if(GameScreen.entities.furnitures.furnitureListAll.get(tr).isEmpty())
					{
						GameScreen.entities.furnitures.furnitureListAll.get(tr).isDead = true;
						SoundManager.instance.Break[1].setPitch(SoundManager.instance.Break[1].play(),1);
						Item nit = GameScreen.items.CreateItem(GameScreen.entities.furnitures.furnitureListAll.get(tr).type,decalaX*16+8+(float)(Math.random()*5),GameScreen.entities.furnitures.furnitureListAll.get(tr).y*16+GameScreen.entities.furnitures.furnitureListAll.get(tr).casesY*8);
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
				else if(type == AllTools.instance.Tree && tr<=0 && range>=distToMiddle)
				{
					 if(!treeCollision(decalaX, decalaY))
					 {
						 boolean Ok = true;
							/*for (int i = 0; i < GameScreen.entities.trees.treeList.size(); i++)
							{
								if(Math.abs(GameScreen.entities.trees.treeList.get(i).x-decalaX*layer.getTileWidth())<31 && Math.abs(GameScreen.entities.trees.treeList.get(i).y-decalaY*layer.getTileWidth())<31) Ok = false;
							}*/

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
							GameScreen.entities.projectiles.AddMagicProjectile(GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, GameScreen.player.velocityCoord.x, GameScreen.player.velocityCoord.y, AllTools.instance.getType(id).sceptreType,  AllTools.instance.getType(id).damage);
						}
					
					}
					else
					{
						torche(GameScreen.player.launcherCoord, GameScreen.player.velocityCoord, AllTools.instance.getType(id).torchangle,2,2,2,false,0);
					}
				}
				else if(type == AllTools.instance.Bow)
				{
					if(isLeft && Tools.raycast(GameScreen.player.shoulderCoord.x, GameScreen.player.shoulderCoord.y, GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, 4).x<0 && GameScreen.inventory.remove(AllTools.instance.getType(id).arrowType, 1))
						GameScreen.entities.projectiles.AddProjectile(GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, GameScreen.player.velocityCoord2.x, GameScreen.player.velocityCoord2.y, AllTools.instance.getType(id).arrowType, GameScreen.player, AllTools.instance.getType(id).damage);
				}
				else if(type == AllTools.instance.Hook && Inputs.instance.leftmousedown)
				{
					if(isLeft && !GameScreen.player.currentGrapple.playerAttached)
					{
						GameScreen.entities.projectiles.AddGrapple(GameScreen.player.launcherCoord.x, GameScreen.player.launcherCoord.y, GameScreen.player.velocityCoord2.x, GameScreen.player.velocityCoord2.y);
					}
					else
					{
						lastTime-=rate;
					}
				}
			}
		}	
	}
	
	public void torche(Vector2 basePos, Vector2 velo, int angle, float r, float g, float b, boolean base, float basecolor)
	{
		int precision = 1;
		float light = 1;
		velo.clamp(800, 800);
		velo.rotate(-angle);
		angle*=precision;
		Vector2 velo2 = new Vector2(velo);
		
		if(!base)Map.instance.lights.tempLight(basePos.x, basePos.y, light/4*r, light/4*g, light/4*b);
		else Map.instance.lights.tempLight(basePos.x, basePos.y, light/4*basecolor, light/4*basecolor, light/4*basecolor);
		
		for(int i = -angle*2; i<angle*2; i++)
		{
			light = 0.2f+(0.8f*(angle-Math.abs(i))/angle);
			velo2.rotate(0.5f/precision);
			boolean finished = false;
			float l = 0;
			int oldx = 0;
			int oldy = 0;
			
			if(STRANGELIGHTS)
			{
				int x0 = Tools.floor((basePos.x)/16);
				int y0 = Tools.floor((basePos.y)/16);
				int x1 = Tools.floor((basePos.x+velo2.x)/16);
				int y1 = Tools.floor((basePos.y+velo2.y)/16);
				
				float deltax = x1 - x0;
				float deltay = y1 - y0;
				float error = 0;
				float deltaerr = Math.abs (deltay / deltax);    //deltaX!=0 (vertical)
			     int y = y0;
			     int x = x0;
			     boolean neg = true;
			     if(x<x1)neg = false;
			     while ((x<x1 && !neg) || (neg && x>x1))
			     {
			    	 if(!neg)x++;
			    	 else x--;
			    	 light = enlight(x,y,light,angle,i,r,g,b,base,basecolor);
			         error = error + deltaerr;
			         while(error >= 0.5)
			         {
			        	 light = enlight(x,y,light,angle,i,r,g,b,base,basecolor);
			             y =  y + (int)Math.signum(y1 - y0);
			             error = error - 1.0f;
			         }
			             
			     }
			}
			else
			{
				while(!finished && light>0.03f)
				{
					l+=4;
					if(l>=velo.len())
					{
						l = velo.len();
						finished = true;
					}
					
					velo2.clamp(l, l);
					
					
					
					int x = Tools.floor((basePos.x+velo2.x)/16);
					int y = Tools.floor((basePos.y+velo2.y)/16);
					
					if(oldx!=x || oldy!=y)
					{
						Map.instance.lights.staticTempLight(x, y, light*r, light*g, light*b);
						if(i == 0 || i == angle/4 || i == -angle/4 || i == angle/2 || i == -angle/2 || i == angle/3*2 || i == -angle/3*2)
						{
							if(!base)Map.instance.lights.tempLighti(x, y, light/3*r, light/3*g, light/3*b);
							else Map.instance.lights.tempLighti(x, y, light/3*basecolor, light/3*basecolor, light/3*basecolor);
						}
							
						

						if(!Map.instance.mainLayer.getBloc(x, y).transparent)
						{
							light*=0.7f;
							if(oldx!=x && oldy!=y)
							{
								light*=0.7f;
							}
							if(!base)Map.instance.lights.tempLighti(x, y, light/2*r, light/2*g, light/2*b);
							else Map.instance.lights.tempLighti(x, y, light/2*basecolor, light/2*basecolor, light/2*basecolor);
							
						}

						light-=1f/100f;
						if(oldx!=x && oldy!=y)light-=1f/100f;
						
						
						oldx = x;
						oldy = y;
					}
				}
			}
		}
	}
	
	public float enlight(int x, int y, float light, float angle,  float i, float r, float g, float b, boolean base, float basecolor)
	{
		Map.instance.lights.staticTempLight(x, y, light*r, light*g, light*b);
		if(i == 0 || i == angle/4 || i == -angle/4 || i == angle/2 || i == -angle/2 || i == angle/3*2 || i == -angle/3*2)
		{
			if(!base)Map.instance.lights.tempLighti(x, y, light/3*r, light/3*g, light/3*b);
			else Map.instance.lights.tempLighti(x, y, light/4*basecolor, light/4*basecolor, light/4*basecolor);
		}
		

		if(!Map.instance.mainLayer.getBloc(x, y).transparent)
		{
			light*=0.7f;
			
		}

		light-=1f/100f;
		
		return light;
	}
	
	public void PutCell(float x, float y, int id, int selectorId, MapLayer layer,float time, boolean left)
	{
		isLeft = left;
		this.selectorId = selectorId;
		this.time = time;
		this.id = id;
		Vector3 worldCoordinates = new Vector3(x, y, 0);		
		GameScreen.camera.unproject(worldCoordinates);
		decalaX = Tools.floor (worldCoordinates.x/layer.getTileWidth());
		decalaY = Tools.floor (worldCoordinates.y/layer.getTileHeight());
		newCell = 0;
		
		
		if(decalaX != oldBreakPos.x || decalaY != oldBreakPos.y || oldLayer != layer.isMain)
		{
			currentDurability = getCell(layer,decalaX,decalaY).durability;
			oldBreakPos.x = decalaX;
			oldBreakPos.y = decalaY;
		}
		
		
		int limit = 0;
		if(GameScreen.noLimit)limit =1000; 
		
		if(decalaX>=-limit && decalaY>1 && decalaX<layer.getWidth()+limit && decalaY<Map.instance.randomHeight && !GameScreen.isVillage())
		{		
			if(id == 0 || (id>999 && AllTools.instance.getType(id).isUseLess))
			{
				
				if(Parameters.i.wayaxe)
				{
					Vector2 aimed = Tools.raycast(GameScreen.player.shoulderCoord.x, GameScreen.player.shoulderCoord.y, decalaX*16+8, decalaY*16+8, 4);
					if(aimed.x>=0)
					{
						decalaX = Tools.floor(aimed.x+0.1f);
						decalaY = Tools.floor(aimed.y+0.1f);
					}
				}
				distToMiddleVec.x = decalaX*16+8-GameScreen.player.middle.x;
				distToMiddleVec.y = decalaY*16+8-GameScreen.player.middle.y;
				distToMiddle = distToMiddleVec.len();
				UseItem(layer, true);
			}
			else if(id>999)
			{
				
				if(Parameters.i.wayaxe && (layer!=Map.instance.backLayer || AllTools.instance.getType(id).type != AllTools.instance.Axe ))
				{
					Vector2 aimed = Tools.raycast(GameScreen.player.shoulderCoord.x, GameScreen.player.shoulderCoord.y, decalaX*16+8, decalaY*16+8, 4);
					if(aimed.x>=0)
					{
						decalaX = Tools.floor(aimed.x+0.1f);
						decalaY = Tools.floor(aimed.y+0.1f);
					}
				}
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
					PutBlock(layer);
				}
			}
		}
		oldLayer = layer.isMain;
		oldclicPos = new Vector2(decalaX,decalaY);
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
