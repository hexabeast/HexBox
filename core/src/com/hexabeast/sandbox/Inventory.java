package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class Inventory {

	public InvItem[][] invItemsArray = new InvItem[10][13];
	//public InvItem[] craftItem = new InvItem[4];
	//public Sprite[] backCraftSprites = new Sprite[4];
	public Sprite[][] backSprites = new Sprite[10][13];
	public Sprite backTrashSprite;
	public Sprite invButtonSprite;
	public Sprite backCraftSprite;
	public Sprite buttonCraftSprite;
	public Sprite backTextSprite;
	public InvItem tempItem;
	public InvItem craftResultItem;
	public boolean hidden = true;
	public boolean isHalfing = false;
	public boolean isTransit = false;
	public boolean isLeftClicked = false;
	public boolean isRightClicked = false;
	public boolean alreadyClick = false;
	public boolean isLeft = false;
	
	public boolean chestopen = false;
	public Furniture chest;
	
	public boolean testCraft = false;
	
	public boolean rightCraftPressed = false;
	public boolean leftCraftPressed = false;
	public float scale = 2.6f;
	public float scalePanel = 4;
	public float space = 3f;
	public float yOffset = 0f;
	public int maxItems = 999;
	private int precedentItemPosX;
	private int precedentItemPosY;
	private Furniture precedentChest;
	public Vector2 offsetTemp = new Vector2();
	
	Stats stats = new Stats();

	
	String name = "";
	String description = "";
	ColorString rarity;
	String speed = "";
	String bdamages = "";
	String damages = "";
	
	
	public Inventory() 
	{
		
		tempItem = new InvItem(0);
		tempItem.image.setOrigin(0, 0);
		tempItem.image.setScale(scale);
		
		craftResultItem = new InvItem(0);
		craftResultItem.image.setOrigin(0, 0);
		craftResultItem.image.setScale(scale);
		
		buttonCraftSprite = new Sprite(TextureManager.instance.craftButton);
		buttonCraftSprite.setOrigin(0, 0);
		buttonCraftSprite.setScale(scalePanel);
		backCraftSprite = new Sprite(TextureManager.instance.craftNew);
		backCraftSprite.setOrigin(0, 0);
		backCraftSprite.setScale(scalePanel);
		backTextSprite = new Sprite(TextureManager.instance.backText);
		backTextSprite.setOrigin(0, 0);
		backTextSprite.setScale(4);
		backTrashSprite = new Sprite(TextureManager.instance.backTrashTexture);
		backTrashSprite.setOrigin(0, 0);
		backTrashSprite.setScale(scalePanel);
		invButtonSprite = new Sprite(TextureManager.instance.invButtonTexture);
		invButtonSprite.setOrigin(0, 0);
		invButtonSprite.setScale(scalePanel);

		for(int i = 0; i<invItemsArray.length;i++)
		{
			for(int j = 0; j<invItemsArray[i].length;j++)
			{
				invItemsArray[i][j] = new InvItem(0);
				invItemsArray[i][j].image.setOrigin(0, 0);
				invItemsArray[i][j].setScaleAll(scale);
				backSprites[i][j] = new Sprite(TextureManager.instance.selectDefault);
				backSprites[i][j].setOrigin(0, 0);
				backSprites[i][j].setScale(scalePanel);
			}
		}
	}
	
	public boolean beginTransition(Vector2 coord,int k, int l)
	{
		isHalfing = false;
		if(l>=7 && !chestopen)return false;
		if(invItemsArray[k][l].id != 0)
		{
			
			offsetTemp = new Vector2(backSprites[k][l].getX()-coord.x, backSprites[k][l].getY()-coord.y);
			refreshTempItemPos(coord);
			precedentItemPosX = k;
			precedentItemPosY = l;
			precedentChest = chest;
			tempItem.id = invItemsArray[k][l].id;
			tempItem.number = invItemsArray[k][l].number;
			
			invItemsArray[k][l].id = 0;
			invItemsArray[k][l].number = 0;
			
			tempItem.refresh();
			refreshItems();
			return true;
		}
		return false;
	}
	
	public boolean beginHalfTransition(Vector2 coord,int k, int l)
	{
		isHalfing = true;
		if(l>=7 && !chestopen)return false;
		if(invItemsArray[k][l].id != 0)
		{
			
			offsetTemp = new Vector2(backSprites[k][l].getX()-coord.x, backSprites[k][l].getY()-coord.y);
			refreshTempItemPos(coord);
			precedentItemPosX = k;
			precedentItemPosY = l;
			if(invItemsArray[k][l].number >1)tempItem.id = invItemsArray[k][l].id;
			else tempItem.id = 0;
			tempItem.number = (int)(invItemsArray[k][l].number/2);
			
			invItemsArray[k][l].number -= tempItem.number;
			
			tempItem.refresh();
			refreshItems();
			return true;
		}
		return false;
	}
	
	public void abortTransition()
	{

		if(precedentItemPosY >= 7 && (!chestopen || chest!=precedentChest))
		{
			for(int i = 0; i<tempItem.number; i++)
			{
				GameScreen.items.placeItem(GameScreen.items.CreateItem(tempItem.id,GameScreen.player.PNJ.middle.x,GameScreen.player.PNJ.middle.y));
			}
		}
		else
		{
			invItemsArray[precedentItemPosX][precedentItemPosY].id = tempItem.id;
			invItemsArray[precedentItemPosX][precedentItemPosY].number += tempItem.number;
		}
		
		tempItem.id = 0;
		tempItem.number = 0;
		
		refreshItems();
		tempItem.refresh();
	}
	public void endTransition(int k, int l)
	{
		if(l>=7 && !chestopen)abortTransition();
		if(!isHalfing || isHalfing)
		{
			if(tempItem.id != 0)
			{
				if(TestCompatibility(k,l, tempItem.id))
				{
					if(invItemsArray[k][l].id == 0)
					{
						invItemsArray[k][l].id = tempItem.id;
						invItemsArray[k][l].number = tempItem.number;
					}
					else if(invItemsArray[k][l].number+1 < maxItems && invItemsArray[k][l].id == tempItem.id && AllTools.instance.getType(invItemsArray[k][l].id).stackable)
					{
						if(tempItem.number+invItemsArray[k][l].number > maxItems)
						{
							tempItem.number -= maxItems-invItemsArray[k][l].number;
							invItemsArray[k][l].number = maxItems;
							abortTransition();
						}
						else
						{
							invItemsArray[k][l].number += tempItem.number;
						}
					}
					else
					{
						if(!isHalfing && TestCompatibility(precedentItemPosX,precedentItemPosY,invItemsArray[k][l].id))
						{
							int tempID = tempItem.id;
							int tempNumber = tempItem.number;
							tempItem.id = invItemsArray[k][l].id;
							tempItem.number = invItemsArray[k][l].number;
							invItemsArray[k][l].id = tempID;
							invItemsArray[k][l].number = tempNumber;
						}
						abortTransition();
					}
				
				
				tempItem.id = 0;
				tempItem.number = 0;
				}
				else
				{
					abortTransition();
				}
				refreshItems();
				tempItem.refresh();
			}	
		}
	}
	
	public void removeTemp()
	{
		tempItem.id = 0;
		tempItem.number = 0;
		
		refreshItems();
		tempItem.refresh();
	}
	
	public void layOneItem(int k, int l)
	{
		if(l>=7 && !chestopen)abortTransition();
			if(tempItem.id != 0)
			{
				if(TestCompatibility(k,l, tempItem.id))
				{
					if(invItemsArray[k][l].id == 0)
					{
						invItemsArray[k][l].id = tempItem.id;
						invItemsArray[k][l].number = 1;
						tempItem.number--;
					}
					else if(invItemsArray[k][l].number+1 < maxItems && invItemsArray[k][l].id == tempItem.id && AllTools.instance.getType(invItemsArray[k][l].id).stackable)
					{
						invItemsArray[k][l].number += 1;
						tempItem.number--;
					}		
					
					if(tempItem.number<1)
					{
						tempItem.id = 0;
						tempItem.number = 0;
					}
				}
				else
				{
					abortTransition();
				}
			}
			refreshItems();
			tempItem.refresh();
	}
	
	public boolean TestCompatibility(int k, int l, int id)
	{
		if(l == 6)
		{
			if(k == 4)return false;
			if(k == 5 && !AllTools.instance.getType(id).helmet)return false;
			if(k == 6 && !AllTools.instance.getType(id).armor)return false;
			if(k == 7 && !AllTools.instance.getType(id).arms)return false;
			if(k == 8 && !AllTools.instance.getType(id).legs)return false;
			if(k == 9 && !AllTools.instance.getType(id).grapple)return false;
		}
		
		return true;
	}
	
	public void refreshTempItemPos(Vector2 coord)
	{
		setTempItemPos(new Vector2(coord.x+offsetTemp.x, coord.y+offsetTemp.y));
		tempItem.refresh();
	}
	
	public void setTempItemPos(Vector2 coord)
	{
		tempItem.image.setX(coord.x); 
		tempItem.image.setY(coord.y); 
	}
	
	public void PutItem(int id)
	{
		outerloop:
		if(id!=0)
		{
			for(int j = 0; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == id && invItemsArray[i][j].number<maxItems && AllTools.instance.getType(invItemsArray[i][j].id).stackable)
					{
						invItemsArray[i][j].number++;
						break outerloop;
					}
				}
			}
			
			
			for(int j = 1; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == 0)
					{
						invItemsArray[i][j].id = id;
						invItemsArray[i][j].number = 1;
						break outerloop;
					}
				}
			}
			for(int i = 0; i<invItemsArray.length;i++)
			{
				if(invItemsArray[i][0].id == 0)
				{
					invItemsArray[i][0].id = id;
					invItemsArray[i][0].number = 1;
					break outerloop;
				}
			}
		}
	}
	
	public void putinchest(Furniture chest)
	{
		for(int i = 0; i<chest.itemsids.length; i++)
		{
			int xs = (int)(i/6);
			int ys = i%6+7;
			invItemsArray[xs][ys].id = chest.itemsids[i];
			invItemsArray[xs][ys].number = chest.itemsnumbers[i];
		}
		this.chest = chest;
		refreshItems();
	}
	
	public boolean isFull(int id)
	{
		if(id!=0)
		{
			for(int j = 0; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == id && invItemsArray[i][j].number<maxItems && AllTools.instance.getType(invItemsArray[i][j].id).stackable)
					{
						return false;
					}
				}
			}
			
			for(int j = 0; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == 0)
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean remove(int id, int number)
	{
		if(id!=0)
		{
			for(int j = 0; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == id && invItemsArray[i][j].number>=number)
					{
						invItemsArray[i][j].number-=number;
						Tools.checkItems();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean present(int id, int number)
	{
		if(id!=0)
		{
			for(int j = 0; j<6;j++)
			{
				for(int i = 0; i<invItemsArray.length;i++)
				{
					if(invItemsArray[i][j].id == id && invItemsArray[i][j].number>=number)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int presentorch(int number)
	{
		for(int j = 0; j<6;j++)
		{
			for(int i = 0; i<invItemsArray.length;i++)
			{
				if(invItemsArray[i][j].id<1000 && AllBlocTypes.instance.getType(invItemsArray[i][j].id).torch && invItemsArray[i][j].number>=number)
				{
					return invItemsArray[i][j].id;
				}
			}
		}
		return -1;
	}
	
	public void setPosition(float x, float y)
	{
		x+=0;
		y-=110;
		float imWidth = invItemsArray[2][2].image.getWidth();
		float imHeight = invItemsArray[2][2].image.getHeight();
		
		for(int i = 0; i<backSprites.length;i++)
		{
			backSprites[i][0].setPosition(GameScreen.select.backSelectorSprites[i].getX(), GameScreen.select.backSelectorSprites[i].getY());
			invItemsArray[i][0].image.setPosition(GameScreen.select.backSelectorSprites[i].getX(), GameScreen.select.backSelectorSprites[i].getY());
			for(int j = 1; j<6;j++)
			{
				float posBackX = x+i*imWidth*space - (backSprites.length)*imWidth*space/2;
				float posBackY = y+yOffset-j*imHeight*space;
				
				float posX = x+(i*imWidth*space) - (backSprites.length)*imWidth*space/2;
				float posY = y+yOffset-j*imHeight*space;
				
				backSprites[i][j].setPosition(posBackX, posBackY);
				invItemsArray[i][j].setPositionAll(posX, posY);
			}
			for(int j = 7; j<13;j++)
			{
				float posBackX = x+i*imWidth*space - (backSprites.length)*imWidth*space/2;
				float posBackY = y+yOffset-j*imHeight*space-70;
				
				float posX = x+(i*imWidth*space) - (backSprites.length)*imWidth*space/2;
				float posY = y+yOffset-j*imHeight*space-70;
				
				backSprites[i][j].setPosition(posBackX, posBackY);
				invItemsArray[i][j].setPositionAll(posX, posY);
			}
		}
		

		float posX = x-GameScreen.camera.viewportWidth+100;
		float posY = y-210;
		
		float posBackX = posX;
		float posBackY = posY;
		
		
		float spaceFac = 0.92f;
		
		invItemsArray[0][6].setPositionAll(posX, posY);
		invItemsArray[1][6].setPositionAll(posX+imWidth*space*spaceFac, posY);
		invItemsArray[2][6].setPositionAll(posX, posY+imHeight*space*spaceFac);
		invItemsArray[3][6].setPositionAll(posX+imWidth*space*spaceFac, posY+imHeight*space*spaceFac);
		invItemsArray[4][6].setPositionAll(posX+imWidth*space*spaceFac*4-21, posY+4);
		craftResultItem.setPositionAll(posX+imWidth*space*spaceFac*4-21, posY+4);
		
		backCraftSprite.setPosition(posX, posY-42);
		buttonCraftSprite.setPosition(posX+255, posY-115);
		
		backSprites[0][6].setPosition(posBackX, posBackY);
		backSprites[1][6].setPosition(posBackX+imWidth*space*spaceFac, posBackY);
		backSprites[2][6].setPosition(posBackX, posBackY+imHeight*space*spaceFac);
		backSprites[3][6].setPosition(posBackX+imWidth*space*spaceFac, posBackY+imHeight*space*spaceFac);
		backSprites[4][6].setPosition(posBackX+imWidth*space*spaceFac*4-21, posBackY+4);
		
		
		//posBackX = selectX+7*imWidth*space-backWidth/2-408;
		posBackY = posY-3*imHeight*space;
		
		//posX = selectX+7*imWidth*space-imWidth/2-408;
		posY = posY-3*imHeight*space;
		
		backSprites[5][6].setPosition(posBackX, posBackY);
		backSprites[6][6].setPosition(posBackX, posBackY-imHeight*space*spaceFac);
		backSprites[7][6].setPosition(posBackX, posBackY-imHeight*space*spaceFac*2);
		backSprites[8][6].setPosition(posBackX, posBackY-imHeight*space*spaceFac*3);
		
		backSprites[9][6].setPosition(posBackX+imHeight*space*spaceFac, posBackY-imHeight*space*spaceFac*2);
		
		invItemsArray[5][6].setPositionAll(posX, posY);
		invItemsArray[6][6].setPositionAll(posX, posY-imHeight*space*spaceFac);
		invItemsArray[7][6].setPositionAll(posX, posY-imHeight*space*spaceFac*2);
		invItemsArray[8][6].setPositionAll(posX, posY-imHeight*space*spaceFac*3);
	
		invItemsArray[9][6].setPositionAll(posX+imHeight*space*spaceFac, posY-imHeight*space*spaceFac*2);
		
		
		for(int i = 10; i<10; i++)
		{
			invItemsArray[i][6].setPositionAll(posBackY-100000, posBackY-100000);
			backSprites[i][6].setPosition(posBackX-1000000, posBackY-100000);
		}
		
		backTrashSprite.setPosition(12f*invItemsArray[1][1].image.getWidth()*space,backSprites[5][5].getY()-2);
		backTextSprite.setPosition(x+500, y-backTextSprite.getHeight()-370);
	}
	
	public void refreshItems()
	{
		Tools.checkItems();
	}
	
	
	public void refreshItemsinv()
	{
		tempItem.refresh();
		TestCraft();
		craftResultItem.refresh();
		for(int j = 0; j<invItemsArray[0].length;j++)
		{
			for(int i = 0; i<invItemsArray.length;i++)
			{
				if(invItemsArray[i][j].id != 0 && invItemsArray[i][j].number <= 0)
				{
					invItemsArray[i][j].id = 0;
					invItemsArray[i][j].number = 0;
				}
				invItemsArray[i][j].refresh();
			}
		}
	}
	
	public void drawAll(SpriteBatch selectBatch)
	{
		
		Input();
		if(!hidden)
		{
			
			backCraftSprite.draw(selectBatch);
			buttonCraftSprite.draw(selectBatch);
			
			for(int i = 0; i<backSprites.length;i++)
			{
				for(int j = 1; j<7;j++)
				{
					backSprites[i][j].draw(selectBatch);
				}
				if(chestopen)
				{
					for(int j = 7; j<13;j++)
					{
						backSprites[i][j].draw(selectBatch);
					}
				}
			}
			for(int i = 0; i<backSprites.length;i++)
			{
				for(int j = 1; j<7;j++)
				{
					invItemsArray[i][j].drawAll(selectBatch);
				}
				if(chestopen)
				{
					for(int j = 7; j<13;j++)
					{
						invItemsArray[i][j].drawAll(selectBatch);
					}
				}
			}
			
			if(testCraft)
			{
				craftResultItem.number = 1;
				craftResultItem.image.setColor(0.2f,0.2f,0.2f,0.7f);
				craftResultItem.drawAll(selectBatch);
			}
			
			
			backTrashSprite.draw(selectBatch);
			tempItem.drawAll(selectBatch);
			
			if(tempItem.id != 0)
			{
				if(tempItem.id > 999)
				{
					name = AllTools.instance.getType(tempItem.id).name;
					description = AllTools.instance.getType(tempItem.id).description;
					rarity = AllTools.instance.getType(tempItem.id).rarity;
					int type = AllTools.instance.getType(tempItem.id).type;
					if(type == AllTools.instance.Sword)
					{
						bdamages = "";
						damages = "Power: "+String.valueOf((int)(AllTools.instance.getType(tempItem.id).damage));
						speed = "Speed: "+String.valueOf((int)(AllTools.instance.getType(tempItem.id).downtime*100f/24f));
					}
					else if(type == AllTools.instance.Bow)
					{
						bdamages = "";
						damages = "Power: "+String.valueOf((int)(AllTools.instance.getType(tempItem.id).damage));
						speed = "Rate of fire: "+String.valueOf((int)(10/AllTools.instance.getType(tempItem.id).rate));
					}
					else if(type == AllTools.instance.Sceptre && AllTools.instance.getType(tempItem.id).magicprojectile)
					{
						bdamages = "";
						damages = "Power: "+String.valueOf((int)(AllTools.instance.getType(tempItem.id).damage));
						speed = "Rate of fire: "+String.valueOf((int)(10/AllTools.instance.getType(tempItem.id).rate));
					}
					else if(type == AllTools.instance.Axe)
					{
						bdamages = "";
						damages = "Efficiency: "+String.valueOf((int)(AllTools.instance.getType(tempItem.id).efficiency*25));
						speed = "Speed: "+String.valueOf((int)(10/AllTools.instance.getType(tempItem.id).rate));
					}
					else if(AllTools.instance.getType(tempItem.id).equipment)
					{
						if(AllTools.instance.getType(tempItem.id).armorSpeed>0)speed = "Speed: +"+String.valueOf((int)(AllTools.instance.getType(tempItem.id).armorSpeed));
						else speed = "";
						if(AllTools.instance.getType(tempItem.id).armorJump>0)damages = "Jump: +"+String.valueOf((int)(AllTools.instance.getType(tempItem.id).armorJump));
						else damages="";
						if(AllTools.instance.getType(tempItem.id).armorDefense>0)bdamages = "Defense: +"+String.valueOf((int)(AllTools.instance.getType(tempItem.id).armorDefense));
						else bdamages="";
					}
				}
				else
				{
					bdamages = "";
					damages = "";
					speed = "";
					name = AllBlocTypes.instance.getType(tempItem.id).name;
					description = AllBlocTypes.instance.getType(tempItem.id).description;
					rarity = AllBlocTypes.instance.getType(tempItem.id).rarity;
				}
			}
			else if(name == "" && description == "")
			{
				bdamages = "";
				damages = "";
				speed = "";
				rarity = Constants.emptystring;
				name = "Inventory";
				description = "Select an item to see its description.";
			}
			
			backTextSprite.draw(selectBatch);
			
			
			FontManager.instance.font.draw(selectBatch, bdamages, backTextSprite.getX()+60, backTextSprite.getY()+200);
			FontManager.instance.font.draw(selectBatch, damages, backTextSprite.getX()+60, backTextSprite.getY()+160);
			FontManager.instance.font.draw(selectBatch, speed, backTextSprite.getX()+60, backTextSprite.getY()+120);
			
			FontManager.instance.font.setColor(rarity.col);
			FontManager.instance.font.draw(selectBatch, name, backTextSprite.getX()+35, backTextSprite.getY()+backTextSprite.getHeight()*4-50);
			FontManager.instance.font.draw(selectBatch, rarity.str, backTextSprite.getX()+60, backTextSprite.getY()+80);
			FontManager.instance.font.setColor(Color.WHITE);
			
			FontManager.instance.font.draw(selectBatch, description, backTextSprite.getX()+35, backTextSprite.getY()+backTextSprite.getHeight()*4-100, 440,Align.center,true);
			
		}
		
	}
	
	public void ToggleHide()
	{
		if(hidden)hidden = false;
		else hidden = true;
	}
	
	public void Input()
	{
		if(Inputs.instance.rightpress && Tools.isClicked(buttonCraftSprite,Constants.invof) && !rightCraftPressed && Inputs.instance.mousedown)
			{
			for(int i = 0; i<10; i++)Craft();
			rightCraftPressed = true;
			}
		if(!Inputs.instance.rightpress)rightCraftPressed = false;
		
		if(Inputs.instance.leftpress && Tools.isClicked(buttonCraftSprite,Constants.invof) && !leftCraftPressed && Inputs.instance.mousedown)	
			{
			Craft();
			leftCraftPressed = true;
			}
		if(!Inputs.instance.leftpress)leftCraftPressed = false;
		
		if(Inputs.instance.leftpress || Inputs.instance.rightpress)
		{	
			boolean toolate = false;
			if(isTransit) 
			{
				refreshTempItemPos(Tools.getAbsoluteMouse());
				toolate = true;
				
				if(!alreadyClick && ((Inputs.instance.leftpress && !isLeft) || (Inputs.instance.rightpress && isLeft)) )
				{
					for(int i = 0; i<invItemsArray.length;i++)
					{
						if(isTransit && Tools.isClicked(GameScreen.select.Selectors[i].image,Constants.invof))
						{
							layOneItem(i,0);
							GameScreen.select.refreshSelector();
						}
						for(int j = 1; j<invItemsArray[i].length;j++)
						{	
							if(isTransit && Tools.isClicked(invItemsArray[i][j].image,Constants.invof))
							{
								layOneItem(i,j);
								GameScreen.select.refreshSelector();
							}
						}
					}
					alreadyClick = true;
				}
			}
			
			if(Tools.isClicked(invButtonSprite,Constants.invof))
			{
			toolate = true;
			}
			
			if(hidden)
			{
				if(!toolate)
					for(int i = 0; i<GameScreen.select.Selectors.length;i++)
					{
						if(Tools.isClicked(GameScreen.select.Selectors[i].image,Constants.invof) && Inputs.instance.mousedown)
						{
						toolate = true;
						GameScreen.player.currentCellState=i;
						GameScreen.player.refreshSelect();
						}
					}
			}
			
			else
			{
				if(!toolate)
					for(int i = 0; i<invItemsArray.length;i++)
					{
						if(!toolate && Tools.isClicked(GameScreen.select.Selectors[i].image,Constants.invof) && Inputs.instance.mousedown)
						{
							toolate = true;
							isLeftClicked = true;
							if(Inputs.instance.leftpress)
							{
								if(beginTransition(Tools.getAbsoluteMouse(),i,0))
									{
									isTransit = true;
									isLeft = true;
									}
							}
							else 
							{
								if(beginHalfTransition(Tools.getAbsoluteMouse(),i,0))
									{
									isTransit = true;
									isLeft = false;
									}
							}
							GameScreen.select.refreshSelector();
						}
						
						
						for(int j = 1; j<invItemsArray[i].length;j++)
						{
							if(!toolate && Tools.isClicked(invItemsArray[i][j].image,Constants.invof) && Inputs.instance.mousedown)
							{
								toolate = true;
								isLeftClicked = true;
								if(Inputs.instance.leftpress)
								{
									if(beginTransition(Tools.getAbsoluteMouse(),i,j))
									{
										isTransit = true;
										isLeft = true;
									}
								}
								else 
								{
									if(beginHalfTransition(Tools.getAbsoluteMouse(),i,j))
									{
										isTransit = true;
										isLeft = false;
									}
								}
							}
						}
					}
			}
		}
		
		if(((!isLeft && !Inputs.instance.leftpress) || (isLeft && !Inputs.instance.rightpress)))
		{
			alreadyClick = false;
		}
		
		if(isTransit && ((isLeft && !Inputs.instance.leftpress) || (!isLeft && !Inputs.instance.rightpress)) )
		{
			for(int i = 0; i<invItemsArray.length;i++)
			{
				if(isTransit && Tools.isClicked(GameScreen.select.Selectors[i].image,Constants.invof))
				{
					endTransition(i,0);
					isTransit = false;
					GameScreen.select.refreshSelector();
				}
				for(int j = 1; j<invItemsArray[i].length;j++)
				{	
					if(isTransit && Tools.isClicked(invItemsArray[i][j].image,Constants.invof))
					{
						endTransition(i,j);
						isTransit = false;
						GameScreen.select.refreshSelector();
					}
				}
			}
			if(isTransit && Tools.isClicked(backTrashSprite,Constants.invof))
			{
				GameScreen.inventory.removeTemp();
				isTransit = false;
				GameScreen.select.refreshSelector();
			}
			if(isTransit)
			{
				GameScreen.inventory.abortTransition();
				isTransit = false;
				GameScreen.select.refreshSelector();
			}
		}
	}
	
	public void Craft()
	{
		int[] craftResult = AllCrafts.instance.getCraft(invItemsArray[0][6], invItemsArray[1][6], invItemsArray[2][6], invItemsArray[3][6]);
		if(craftResult[0] != 0 && (invItemsArray[4][6].id == 0 || (invItemsArray[4][6].number+craftResult[1]<=maxItems && invItemsArray[4][6].id == craftResult[0] && AllTools.instance.getType(invItemsArray[4][6].id).stackable)))
		{
			for(int i = 0; i<4; i++)
			{
				if(invItemsArray[i][6].id!=0)
				{
					if(invItemsArray[i][6].number>1)
					{
						invItemsArray[i][6].number--;
					}
					else
					{
						invItemsArray[i][6].id = 0;
						invItemsArray[i][6].number = 0;
					}
				}
			}
			

			if(invItemsArray[4][6].id == craftResult[0])
			{
				invItemsArray[4][6].number +=craftResult[1];
			}
			else
			{
				invItemsArray[4][6].id = craftResult[0];
				invItemsArray[4][6].number = craftResult[1];
			}
				
			
			refreshItems();
			GameScreen.select.refreshSelector();
			
		}
	}
	
	public void TestCraft()
	{
		int[] craftResult = AllCrafts.instance.getCraft(invItemsArray[0][6], invItemsArray[1][6], invItemsArray[2][6], invItemsArray[3][6]);
		if(craftResult[0] != 0 && invItemsArray[4][6].id == 0)
		{
			craftResultItem.id = craftResult[0]; 
			testCraft = true;
		}
		else
		{
			craftResultItem.id = 0;
			testCraft = false;
		}
	}
	
	public Stats getStats()
	{
		stats.reboot();
		ToolType helmetType = Helmet();
		ToolType armorType = Armor();
		ToolType armsType = Arms();
		ToolType legsType = Legs();
		
		if(helmetType.helmet)
		{
			addStats(stats, helmetType);
		}
		if(armorType.armor)
		{
			addStats(stats, armorType);
		}
		if(armsType.arms)
		{
			addStats(stats, armsType);
		}
		if(legsType.legs)
		{
			addStats(stats, legsType);
		}
		
		return stats;
	}
	
	public void addStats(Stats stats, ToolType type)
	{
		stats.defense+=type.armorDefense;
		stats.speed+=type.armorSpeed;
		stats.jump+=type.armorJump;
	}
	
	public ToolType Grapple()
	{
		return AllTools.instance.getType(invItemsArray[9][6].id);
	}
	
	public ToolType Armor()
	{
		return AllTools.instance.getType(invItemsArray[6][6].id);
	}
	
	public ToolType Helmet()
	{
		return AllTools.instance.getType(invItemsArray[5][6].id);
	}
	
	public ToolType Arms()
	{
		return AllTools.instance.getType(invItemsArray[7][6].id);
	}
	
	public ToolType Legs()
	{
		return AllTools.instance.getType(invItemsArray[8][6].id);
	}

	
}
