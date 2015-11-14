package com.hexabeast.sandbox;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MapLayer {
	
	Random rand;
	
	int width;
	int height;
	
	int minX;
	int minY;
	int maxX;
	int maxY;
	float torchState;
	float animState;
	
	BlocType[][] blocs; 
	byte[][] state; 
	
	boolean isMain = true;
	
	BlocType curBloc;
	
	boolean[][] isChanged = new boolean[Map.chunkNumberWidth][Map.chunkNumberHeight];
	
	Vector3 color = new Vector3();
	Vector3 color2= new Vector3();
	Vector3 color3= new Vector3();
	Vector3 color4= new Vector3();
	Vector3 color5= new Vector3();

	public MapLayer(boolean isMain) 
	{
		rand = new Random();
		this.isMain = isMain;
		
		this.width = Map.instance.width;
		this.height = Map.instance.height;
		
		blocs = new BlocType[width][height];
		
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
			{
				blocs[i][j] = AllBlocTypes.instance.Empty;
			}
		}
		
		state = new byte[width][height];
	}
	
	public void computeBounds()
	{
		minX = (int)((GameScreen.camera.position.x - GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 - 32)/getTileWidth());
		minY = (int)((GameScreen.camera.position.y - GameScreen.camera.zoom*GameScreen.camera.viewportHeight/2 - 32)/getTileHeight());
		maxX = (int)((GameScreen.camera.position.x + GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 + 32)/getTileWidth());
		maxY = (int)((GameScreen.camera.position.y + GameScreen.camera.zoom*GameScreen.camera.viewportHeight/2 + 32)/getTileHeight());
		
		if(!GameScreen.noLimit)
		{
			if(minX<0)minX = 0;
			if(maxX>width-1)maxX = width-1;
		}
		
		if(minY<1)minY = 1;
		if(maxY>height-2)maxY = height-2;
	}
	
	public int getTileHeight()
	{
		return 16;
	}
	
	public int getTileWidth()
	{
		return 16;
	}
	
	int getHeight()
	{
		return height;
	}
	
	int getWidth()
	{
		return width;
	}
	
	
	public BlocType getBloc(int x, int y)
	{

		
		if(!GameScreen.noLimit)
		{
			if(x<0 || y<2 || x>=width || y>=height-2)
			{
				return AllBlocTypes.instance.Unbreak;
			}
		}
		else
		{
			if(y<2 || y>=height-2)
			{
				return AllBlocTypes.instance.Unbreak;
			}
			
			while(x<0)x = width+x;
			while(x>width-1)x = 0+(x-(width));
		}
		return blocs[x][y];
	}
	
	int getState(int x, int y)
	{
		if(!GameScreen.noLimit)
		{
			if(x<0 || y<2 || x>=width || y>=height-2)
			{
				return AllBlocTypes.instance.full;
			}
		}
		else
		{
			if(y<2 || y>=height-2)
			{
				return AllBlocTypes.instance.full;
			}
			while(x<0)x = width+x;
			while(x>width-1)x = 0+(x-(width));
		}
		
		return state[x][y];
	}
	
	public void setBloc(int x, int y, BlocType cell, int stat, boolean important) {
		
		
		if(x<0 || x>=width)
		{
			if(GameScreen.noLimit)
			{
				while(x<0)x = width+x;
				while(x>width-1)x = 0+(x-(width));
				
				if(blocs[x][y]!= cell && important)
				{
					setChanged(x,y);
				}
				blocs[x][y] =  cell;
				state[x][y] = (byte) stat;
			}
		}
		else
		{
			if(blocs[x][y]!=cell && important)
			{
				setChanged(x,y);
			}
			blocs[x][y] = cell;
			state[x][y] = (byte) stat;	
		}
	}
	
	public void setChanged(int x, int y)
	{
		int x2 = (int)((float)x/(float)width*isChanged.length);
		int y2 = (int)((float)y/(float)height*isChanged[0].length);
		isChanged[x2][y2] = true;
	}
	
	public void Animate(int i, int j)
	{
		if(curBloc.torch && torchState>1)
		{	
			setBloc(i, j, curBloc, (byte) ((int)getState(i,j)+1), false);
			if(getState(i,j)>5)setBloc(i, j, curBloc, 0, false);
		}
		if(curBloc.animated && animState>1)
		{
			setBloc(i, j, curBloc.next, getState(i,j), false);
		}
	}
	
	public void renderBlock(SpriteBatch batch, Color col, int i, int j)
	{
		Animate(i,j);
		int angle = ((int)getState(i,j)/10)*90;
		
		
		if (Parameters.i.fullBright)
		{
			batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)%10], i*16, j*16,8,8, 16, 16,1,1,angle);
		}
		else
		{
			color.set(Tools.getShadowColor(i,j));
			if(Parameters.i.HQ<=1 || (!isMain && Parameters.i.HQ<3) || Map.instance.mainLayer.getBloc(i,j).lightFull)
			{
				batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
				batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)%10], i*16, j*16,8,8, 16, 16,1,1,angle);
			}
			else 
			{
				Vector3 lumF;
				boolean tooDif = false;
				color2.set(Tools.getShadowColor(i+1,j));
				color3.set(Tools.getShadowColor(i,j+1));
				color4.set(Tools.getShadowColor(i-1,j));
				color5.set(Tools.getShadowColor(i,j-1));
				if(Parameters.i.HQ>1) tooDif = Tools.isDifferentEnough(color.x,color4.x,color2.x,color3.x,color5.x);
				if(!tooDif && Parameters.i.RGB)tooDif = Tools.isDifferentEnough(color.y,color4.y,color2.y,color3.y,color5.y);
				if(!tooDif && Parameters.i.RGB)tooDif = Tools.isDifferentEnough(color.z,color4.z,color2.z,color3.z,color5.z);
				
				if(tooDif)
				{
					for(int l = 0; l<2; l++)
						for(int m = 0; m<2; m++)
						{
							
							int m2 = m;
							int l2 = l;
							if(angle!=0)
							{
								if(angle==180)
								{
									if(l==0)l2 = 1;
									else l2 = 0;
									if(m == 0)m2=1;
									else m2=0;
								}
								else if(angle==90)
								{
									if(l==0)
									{
										if(m == 0)m2=1;
										else l2=1;
									}
									else
									{
										if(m == 1)m2=0;
										else l2=0;
									}
								}
								else if(angle==270)
								{
									if(l==0)
									{
										if(m == 0)l2=1;
										else m2=0;
									}
									else
									{
										if(m == 1)l2=0;
										else m2=1;
									}
								}
							}
							Vector3 lum1 = color4;
							if(l2 == 1)lum1 = color2;
							Vector3 lum2 = color3;
							if(m2 == 1)lum2 = color5;
							lumF = new Vector3();
							lumF.add(color);
							lumF.add(color);
							lumF.add(lum1);
							lumF.add(lum2);
							batch.setColor(lumF.x/4*col.r,lumF.y/4*col.g,lumF.z/4*col.b,1);
							

							int srcX = TextureManager.instance.CoordinatesBlocs[curBloc.Id].x+(int)((getState(i,j)%10)%3)*10+4*l;
							int srcY = TextureManager.instance.CoordinatesBlocs[curBloc.Id].y+(int)((int)((getState(i,j)%10)/3))*10+4*m;
							int srcWidth = 4;
							int srcHeight = 4;

							/*float u = srcX / TextureManager.instance.Textures[getBloc(i,j)].getWidth();
							float v2 = srcY / TextureManager.instance.Textures[getBloc(i,j)].getHeight();
							float u2 = (srcX + srcWidth) / TextureManager.instance.Textures[getBloc(i,j)].getWidth();
							float v = (srcY + srcHeight) / TextureManager.instance.Textures[getBloc(i,j)].getHeight();
							*/

							batch.draw(TextureManager.instance.materials, i*16+8*l2, j*16+8*(1-m2), 4, 4, 8, 8, 1, 1, angle, srcX, srcY, srcWidth, srcHeight, false, false);	
							if(Math.abs(lum1.x-color.x)<0.05f || Math.abs(lum2.x-color.x)<0.05f && (!Parameters.i.RGB || ((Math.abs(lum1.y-color.y)<0.05f || Math.abs(lum2.y-color.y)<0.05f) && (Math.abs(lum1.z-color.z)<0.05f || Math.abs(lum2.z-color.z)<0.05f))))
							tooDif = false;
						}
				}
				else
				{
					batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
					batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)%10], i*16, j*16, 8, 8, 16, 16, 1, 1, angle);
				}
			}
		}
			
			
	}
	
	public void renderOldBlock(SpriteBatch batch, Color col, int i, int j)
	{

			Animate(i,j);
			
			if (Parameters.i.fullBright)
			{
				batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)], i*16, j*16, 16, 16);
			}
			else
			{
				color.set(Tools.getShadowColor(i,j));
				if (Parameters.i.HQ<=1 || (!isMain && Parameters.i.HQ<3) || Map.instance.mainLayer.getBloc(i,j).lightFull)
				{
					batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
					batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)], i*16, j*16, 16, 16);
				}
				else
				{
					Vector3 lumF;
					boolean tooDif = false;
					color2.set(Tools.getShadowColor(i+1,j));
					color3.set(Tools.getShadowColor(i,j+1));
					color4.set(Tools.getShadowColor(i-1,j));
					color5.set(Tools.getShadowColor(i,j-1));
					if(Parameters.i.HQ>1) tooDif = Tools.isDifferentEnough(color.x,color4.x,color2.x,color3.x,color5.x);
					if(!tooDif && Parameters.i.RGB)tooDif = Tools.isDifferentEnough(color.y,color4.y,color2.y,color3.y,color5.y);
					if(!tooDif && Parameters.i.RGB)tooDif = Tools.isDifferentEnough(color.z,color4.z,color2.z,color3.z,color5.z);
					
					int angle = 0;
					if(tooDif)
					{
						for(int l = 0; l<2; l++)
							for(int m = 0; m<2; m++)
							{
								
								int m2 = m;
								int l2 = l;
							
								Vector3 lum1 = color4;
								if(l2 == 1)lum1 = color2;
								Vector3 lum2 = color3;
								if(m2 == 1)lum2 = color5;
								lumF = new Vector3();
								lumF.add(color);
								lumF.add(color);
								lumF.add(lum1);
								lumF.add(lum2);
								batch.setColor(lumF.x/4*col.r,lumF.y/4*col.g,lumF.z/4*col.b,1);
									
								int srcX = TextureManager.instance.CoordinatesBlocs[curBloc.Id].x+(int)(getState(i,j)%9)*10+4*l;
								int srcY = TextureManager.instance.CoordinatesBlocs[curBloc.Id].y+(int)((int)(getState(i,j)/9))*10+4*m;
								int srcWidth = 4;
								int srcHeight =  4;
	
								batch.draw(TextureManager.instance.materials, i*16+8*l2, j*16+8*(1-m2), 4, 4, 8, 8, 1, 1, angle, srcX, srcY, srcWidth, srcHeight, false, false);	
								if(Math.abs(lum1.x-color.x)<0.05f || Math.abs(lum2.x-color.x)<0.05f && (!Parameters.i.RGB || ((Math.abs(lum1.y-color.y)<0.05f || Math.abs(lum2.y-color.y)<0.05f) && (Math.abs(lum1.z-color.z)<0.05f || Math.abs(lum2.z-color.z)<0.05f))))
									tooDif = false;
							}
					}
					else
					{
						batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
						batch.draw(TextureManager.instance.TextureRegions[curBloc.Id][getState(i,j)], i*16, j*16, 8, 8, 16, 16, 1, 1, angle);
					}
				}
			}
	}
	
	public void draw(SpriteBatch batch)
	{
		computeBounds();
		
		Color col = batch.getColor();
		if(!isMain)
		{
			torchState+=Main.delta*12;
			animState+=Main.delta*12;
			Map.instance.mainLayer.torchState+=Main.delta*12;
			Map.instance.mainLayer.animState+=Main.delta*12;
		}
		
		for(int i = minX; i<=maxX; i++)
		{
			for(int j = minY; j<=maxY; j++)
			{
				curBloc = getBloc(i,j);
				if(curBloc != AllBlocTypes.instance.Empty)
				{
					if(isMain || !Tools.isFull(Map.instance.mainLayer.getBloc(i,j),Map.instance.mainLayer.getState(i,j)) || Map.instance.mainLayer.getBloc(i,j).transparent)
					{
						if(!isMain || !curBloc.needBack)
						{
							if(curBloc.oldTexture)renderOldBlock(batch,col, i, j);
							else renderBlock(batch,col, i, j);
						}
					}
				}
				if(!isMain)
				{
					if(Map.instance.mainLayer.getBloc(i,j).needBack)
					{
						Map.instance.mainLayer.curBloc = Map.instance.mainLayer.getBloc(i,j);
						batch.setColor(Color.WHITE);
						if(Map.instance.mainLayer.curBloc.oldTexture)Map.instance.mainLayer.renderOldBlock(batch,Color.WHITE, i, j);
						else Map.instance.mainLayer.renderBlock(batch,Color.WHITE, i, j);
						batch.setColor(col);
					}
				}
			}
		}
		
		
		if(torchState>1)torchState = 0;
		if(animState>1)animState = 0;
		
		if(Parameters.i.details)
		{
			for(int i = minX; i<=maxX; i++)
			{
				for(int j = minY; j<=maxY; j++)
				{
					if(!isMain)
					{
						setOcclusion(batch,i,j);
						if(!Tools.isFull(Map.instance.mainLayer.getBloc(i,j),Map.instance.mainLayer.getState(i,j)) || Map.instance.mainLayer.getBloc(i,j).transparent)drawTransition(batch,col, i, j);
					}
					else
					{
						if(j>Map.instance.limit-10)setGrass(batch,col, i, j);
						drawTransition(batch,col, i, j);
					}
				}
			}
		}
	}
	
	public void setGrass( SpriteBatch batch, Color col, int i, int j)
	{
		if(AllBlocTypes.instance.Grass == Map.instance.mainLayer.getBloc(i,j))
		{
			if(!Map.instance.mainLayer.getBloc(i,j+1).collide)
			{
				
				rand.setSeed(Tools.getMapBounded(i)*2+j);
				if(!Parameters.i.fullBright)batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
				rand.nextInt(4);
				batch.draw(TextureManager.instance.grass[rand.nextInt(4)], i*16, j*16, 16,32);
				batch.setColor(col);
			}
		}
	}
	
	public void drawTransition( SpriteBatch batch, Color col, int i, int j)
	{
		BlocType b = getBloc(i,j).merge;
		if(b.dirtish)
		{
			BlocType b1 = getBloc(i+1,j).merge;
			BlocType b2 = getBloc(i-1,j).merge;
			BlocType b3 = getBloc(i,j+1).merge;
			BlocType b4 = getBloc(i,j-1).merge;
			
			boolean c1 = (b1 != b && !b1.artificial && !b1.nonblock && b.dirtishpriority<b1.dirtishpriority);
			boolean c2 = ( b2 != b && !b2.artificial&& !b2.nonblock &&b.dirtishpriority<b2.dirtishpriority);
			boolean c3 = (b3 != b && !b3.artificial&& !b3.nonblock && b.dirtishpriority<b3.dirtishpriority);
			boolean c4 = (b4 != b && !b4.artificial&& !b4.nonblock && b.dirtishpriority<b4.dirtishpriority);
			
			if(!Parameters.i.fullBright && (c1 || c2 || c3 || c4))
			{
				color.set(Tools.getShadowColor(i,j));
				batch.setColor(color.x*col.r,color.y*col.g,color.z*col.b,1);
			}
			
			if(c1)
			{
				batch.draw(TextureManager.instance.dirtBorder[b.dirtishtype], i*16+14, j*16, 4,16);
			}
			if(c2)
			{
				batch.draw(TextureManager.instance.dirtBorder[b.dirtishtype], i*16+2, j*16, -4,16);
			}
			if(c3)
			{
				batch.draw(TextureManager.instance.dirtBorderHor[b.dirtishtype], i*16, j*16+14, 16,4);
			}
			if(c4)
			{
				batch.draw(TextureManager.instance.dirtBorderHor[b.dirtishtype], i*16, j*16+2, 16,-4);
			}
			
			if(!Parameters.i.fullBright && (c1 || c2 || c3 || c4))
			{
				batch.setColor(col);
			}
		}
	}
	
	public void setOcclusion(SpriteBatch batch, int i, int j)
	{
		BlocType bloc = Map.instance.mainLayer.getBloc(i,j);
		if(bloc.Id != 0 && !bloc.lightFull && !bloc.transparent && !Map.instance.backLayer.getBloc(i,j).transparent)
		{
			boolean lefttransparent = Map.instance.mainLayer.getBloc(i-1,j).transparent || Map.instance.mainLayer.getBloc(i-1,j).lightFull;
			boolean righttransparent = Map.instance.mainLayer.getBloc(i+1,j).transparent || Map.instance.mainLayer.getBloc(i+1,j).lightFull;
			boolean uptransparent = Map.instance.mainLayer.getBloc(i,j+1).transparent || Map.instance.mainLayer.getBloc(i,j+1).lightFull;
			boolean downtransparent = Map.instance.mainLayer.getBloc(i,j-1).transparent || Map.instance.mainLayer.getBloc(i,j-1).lightFull;
			
			if(j<Map.instance.limit)
			{
				if(lefttransparent)
				{
					if(righttransparent)
					{
						if(uptransparent)
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlrud, i*16-8, j*16-8,TextureManager.instance.occlusionlrud.getRegionWidth()*2,TextureManager.instance.occlusionlrud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlru, i*16-8, j*16,TextureManager.instance.occlusionlru.getRegionWidth()*2,TextureManager.instance.occlusionlru.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlrd, i*16-8, j*16-8,TextureManager.instance.occlusionlrd.getRegionWidth()*2,TextureManager.instance.occlusionlrd.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlr, i*16-8, j*16,TextureManager.instance.occlusionlr.getRegionWidth()*2,TextureManager.instance.occlusionlr.getRegionHeight()*2);
							}
						}
					}
					else
					{
						if(uptransparent)
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlud, i*16-8, j*16-8,TextureManager.instance.occlusionlud.getRegionWidth()*2,TextureManager.instance.occlusionlud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlu, i*16-8, j*16,TextureManager.instance.occlusionlu.getRegionWidth()*2,TextureManager.instance.occlusionlu.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionld, i*16-8, j*16-8,TextureManager.instance.occlusionld.getRegionWidth()*2,TextureManager.instance.occlusionld.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionl, i*16-8, j*16,TextureManager.instance.occlusionl.getRegionWidth()*2,TextureManager.instance.occlusionl.getRegionHeight()*2);
							}
						}
					}
				}
				else
				{
					if(righttransparent)
					{
						if(uptransparent)
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionrud, i*16, j*16-8,TextureManager.instance.occlusionrud.getRegionWidth()*2,TextureManager.instance.occlusionrud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionru, i*16, j*16,TextureManager.instance.occlusionru.getRegionWidth()*2,TextureManager.instance.occlusionru.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionrd, i*16, j*16-8,TextureManager.instance.occlusionrd.getRegionWidth()*2,TextureManager.instance.occlusionrd.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionr, i*16, j*16,TextureManager.instance.occlusionr.getRegionWidth()*2,TextureManager.instance.occlusionr.getRegionHeight()*2);
							}
						}
					}
					else
					{
						if(uptransparent)
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusionud, i*16, j*16-8,TextureManager.instance.occlusionu.getRegionWidth()*2,TextureManager.instance.occlusionud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionu, i*16, j*16,TextureManager.instance.occlusionu.getRegionWidth()*2,TextureManager.instance.occlusionu.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent)
							{
								batch.draw(TextureManager.instance.occlusiond, i*16, j*16-8,TextureManager.instance.occlusiond.getRegionWidth()*2,TextureManager.instance.occlusiond.getRegionHeight()*2);
							}
							else
							{
								
							}
						}
					}
				}
			}
			else
			{
				boolean leftBtransparent = Map.instance.backLayer.getBloc(i-1,j).transparent || Map.instance.backLayer.getBloc(i-1,j).lightFull;
				boolean rightBtransparent = Map.instance.backLayer.getBloc(i+1,j).transparent || Map.instance.backLayer.getBloc(i+1,j).lightFull;
				boolean upBtransparent = Map.instance.backLayer.getBloc(i,j+1).transparent || Map.instance.backLayer.getBloc(i,j+1).lightFull;
				boolean downBtransparent = Map.instance.backLayer.getBloc(i,j-1).transparent || Map.instance.backLayer.getBloc(i,j-1).lightFull;
				if(lefttransparent && !leftBtransparent)
				{
					if(righttransparent && !rightBtransparent)
					{
						if(uptransparent && !upBtransparent)
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlrud, i*16-8, j*16-8,TextureManager.instance.occlusionlrud.getRegionWidth()*2,TextureManager.instance.occlusionlrud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlru, i*16-8, j*16,TextureManager.instance.occlusionlru.getRegionWidth()*2,TextureManager.instance.occlusionlru.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlrd, i*16-8, j*16-8,TextureManager.instance.occlusionlrd.getRegionWidth()*2,TextureManager.instance.occlusionlrd.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlr, i*16-8, j*16,TextureManager.instance.occlusionlr.getRegionWidth()*2,TextureManager.instance.occlusionlr.getRegionHeight()*2);
							}
						}
					}
					else
					{
						if(uptransparent && !upBtransparent)
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionlud, i*16-8, j*16-8,TextureManager.instance.occlusionlud.getRegionWidth()*2,TextureManager.instance.occlusionlud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionlu, i*16-8, j*16,TextureManager.instance.occlusionlu.getRegionWidth()*2,TextureManager.instance.occlusionlu.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionld, i*16-8, j*16-8,TextureManager.instance.occlusionld.getRegionWidth()*2,TextureManager.instance.occlusionld.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionl, i*16-8, j*16,TextureManager.instance.occlusionl.getRegionWidth()*2,TextureManager.instance.occlusionl.getRegionHeight()*2);
							}
						}
					}
				}
				else
				{
					if(righttransparent && !rightBtransparent)
					{
						if(uptransparent && !upBtransparent)
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionrud, i*16, j*16-8,TextureManager.instance.occlusionrud.getRegionWidth()*2,TextureManager.instance.occlusionrud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionru, i*16, j*16,TextureManager.instance.occlusionru.getRegionWidth()*2,TextureManager.instance.occlusionru.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionrd, i*16, j*16-8,TextureManager.instance.occlusionrd.getRegionWidth()*2,TextureManager.instance.occlusionrd.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionr, i*16, j*16,TextureManager.instance.occlusionr.getRegionWidth()*2,TextureManager.instance.occlusionr.getRegionHeight()*2);
							}
						}
					}
					else
					{
						if(uptransparent && !upBtransparent)
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusionud, i*16, j*16-8,TextureManager.instance.occlusionu.getRegionWidth()*2,TextureManager.instance.occlusionud.getRegionHeight()*2);
							}
							else
							{
								batch.draw(TextureManager.instance.occlusionu, i*16, j*16,TextureManager.instance.occlusionu.getRegionWidth()*2,TextureManager.instance.occlusionu.getRegionHeight()*2);
							}
						}
						else
						{
							if(downtransparent && !downBtransparent)
							{
								batch.draw(TextureManager.instance.occlusiond, i*16, j*16-8,TextureManager.instance.occlusiond.getRegionWidth()*2,TextureManager.instance.occlusiond.getRegionHeight()*2);
							}
							else
							{
								
							}
						}
					}
				}
			}
			
		}
	}
}
