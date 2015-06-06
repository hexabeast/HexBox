package com.hexabeast.sandbox;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Tree extends Entity{

	public Sprite[] Stroncs,  SbranchesD, SbranchesG;
	public Sprite Scime, Spousse;
	public int[] troncType;
	public int brancheDNumber = 0;
	public int brancheGNumber = 0;
	public int x;
	public int y;
	public float scale = 1;
	public int seed;
	public float durability = 100;
	public float miniDurability = 1;
	public boolean rotateSens = true;
	
	public float ecloseTime = 1;
	public boolean isEclosed = false;
	
	public int sizecimex = 8;
	public int sizecimey = 8;
	public int sizebranchex = 2;
	public int sizebranchey = 2;
	public int sizetroncx = 2;
	public int sizetroncy = 2;
	public float ydeadoffset = 0;
	public float ydeadvelocity;
	public int id = 0;
	
	Random rand;
	
	public Tree(int x,int y, TextureRegion pousse, TextureRegion[] troncs, TextureRegion[] branchesD,TextureRegion[] branchesG,TextureRegion[] cimes, int seed, float ectime) {
		if(ectime<=0)
		{
			isEclosed = true;
			ecloseTime = -1;
		}
		else
		{
			ecloseTime = (float) (ectime+Math.random()*10);
		}
		
		if(seed == 0) seed = Tools.floor((float) (10000*Math.random()));
		
		rand = new Random(seed);
		
		this.seed = seed;

		this.Spousse = new Sprite(pousse);
		this.Spousse.setSize(32, 32);
		
		this.x = x;
		this.y = y;
		
		Stroncs = new Sprite[Tools.floor(rand.nextFloat()*3+6.9999f)];
		troncType = new int[Stroncs.length];
		
		boolean isRightBranch = false;
		if(rand.nextFloat()>0.5)isRightBranch = true;
		for (int i = 0;i<Stroncs.length; i++)
		{
			if(i>0)
			{
				double randi = rand.nextFloat();
				if(randi>0.6 || i<3 || i>Stroncs.length-2)
				{
					troncType[i] = 2;
				}
				else if(!isRightBranch)
				{
					isRightBranch = !isRightBranch;
					troncType[i] = 1;
					brancheDNumber++;
				}
				else
				{
					isRightBranch = !isRightBranch;
					troncType[i] = 0;
					brancheGNumber++;
				}
			}
			else
			{
				troncType[0] = 3;
			}
			Stroncs[i] = new Sprite(troncs[troncType[i]]);
			Stroncs[i].setScale(scale);
		}
		
		SbranchesD = new Sprite[brancheDNumber];
		SbranchesG = new Sprite[brancheGNumber];
		
		for (int i = 0;i<SbranchesD.length; i++)
		{
			SbranchesD[i] = new Sprite(branchesD[Tools.floor(rand.nextFloat()*branchesD.length)]);
			SbranchesD[i].setScale(scale);
		}
		for (int i = 0;i<SbranchesG.length; i++)
		{
			SbranchesG[i] = new Sprite(branchesG[Tools.floor(rand.nextFloat()*branchesG.length)]);
			SbranchesG[i].setScale(scale);
		}
		Scime = new Sprite(cimes[Tools.floor(rand.nextFloat()*cimes.length)]);
		Scime.setScale(scale);
	}
	
	public void SetPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		int BGCount = 0;
		int BDCount = 0;
		Spousse.setPosition(x*16, y*16-6);
		for (int i = 0;i<Stroncs.length; i++)
		{
			float by = y*16-2+ydeadoffset;
			float xBis = x*16+Stroncs[0].getWidth()/2;
			Stroncs[i].setPosition(x*16, by+i*sizetroncy*16);
			Stroncs[i].setOrigin(-Stroncs[i].getX()+xBis, -Stroncs[i].getY()+by);
			if(troncType[i] == 0)
			{
				SbranchesG[BGCount].setPosition(x*16+2-SbranchesG[BGCount].getWidth(), by+(i)*sizetroncy*16);
				SbranchesG[BGCount].setOrigin(-SbranchesG[BGCount].getX()+xBis, -SbranchesG[BGCount].getY()+by);
				BGCount++;
			}
			else if(troncType[i] == 1)
			{
				SbranchesD[BDCount].setPosition(x*16-2+sizetroncx*16, by+(i)*sizetroncy*16);
				SbranchesD[BDCount].setOrigin(-SbranchesD[BDCount].getX()+xBis, -SbranchesD[BDCount].getY()+by);
				BDCount++;
			}
			if(i == Stroncs.length-1)
			{
				Scime.setPosition(x*16-(sizecimex-sizetroncx)*16/2, by+(1+i)*sizetroncy*16);
				Scime.setOrigin(-Scime.getX()+xBis, -Scime.getY()+by+50);
			}
		}
	}
	
	public void checkPoints()
	{
		for(int j = 0; j<sizetroncx; j++)
		{
			AllEntities.setMap(x+j,y-1,this);
			AllEntities.setMap(x+j,y,this);
			AllEntities.setMap(x+j,y+1,this);
		}
		if(isEclosed)
		{
			for (int i = 0;i<Stroncs.length; i++)
			{
				for(int j = 0; j<sizetroncx; j++)
				{
					for(int k = 0; k<sizetroncy; k++)
					{
						AllEntities.setMap(x+j,y+i*sizetroncy+k,this);
					}
				}
				
				if(troncType[i] == 0)
				{
					for(int j = 0; j<sizebranchex; j++)
					{
						for(int k = 0; k<sizebranchey; k++)
						{
							AllEntities.setMap(x-j-1,y+i*sizetroncy+k,this);
						}
					}
				}
				else if(troncType[i] == 1)
				{
					for(int j = 0; j<sizebranchex; j++)
					{
						for(int k = 0; k<sizebranchey; k++)
						{
							AllEntities.setMap(x+j+sizetroncx,y+i*sizetroncy+k,this);
						}
					}
				}
				if(i == Stroncs.length-1)
				{
					for(int j = 0; j<sizecimex; j++)
					{
						for(int k = 0; k<sizecimey; k++)
						{
							AllEntities.setMap(x+j-(sizecimex-sizetroncx)/2,y+(i+1)*sizetroncy+k,this);
						}
					}
				}
			}
		}
	}
	
	public void BreakTree(float efficiency)
	{
		if(!isEclosed && miniDurability>=0)
		{
			miniDurability-=efficiency/4*0.08f*300*Main.delta;
		}
		else if(isEclosed && durability>=0)
		{
			if(Scime.getRotation()>((100-durability)/25+2) && rotateSens)
			{
				rotateSens = false;
			}
			
			if(Scime.getRotation()<-((100-durability)/25+2) && !rotateSens)
			{
				rotateSens = true;
			}
			
			if(rotateSens)
			{
				RotateAll(efficiency/4*2500/(durability+40));
			}
			else
			{
				RotateAll(efficiency/4*-2500/(durability+40));
			}
			durability-=efficiency/4*0.08f*300*Main.delta;
		}
	}
	
	public void DestroyTree()
	{
		ydeadvelocity+=0*Main.delta;
		if(ydeadvelocity>10)ydeadvelocity=10;
		ydeadoffset-=ydeadvelocity*16*Main.delta;
		SetPosition(x,y);
		Scime.setOrigin(-Scime.getX()+x*16+sizetroncx/2*16, -Scime.getY()+y*16+ydeadoffset);
		
		RotateAll(-(durability+2));
		
		
		durability-=1.2f*60*Main.delta;
		
		Vector2 position;
		
		for(int i = 0;i<SbranchesG.length; i++)
			{
				position = getRelativePos(SbranchesG[i].getRotation()+4,SbranchesG[i].getX(),SbranchesG[i].getY(),SbranchesG[i].getOriginX(),SbranchesG[i].getOriginY());
				if(isCollide(position.x, position.y)&& SbranchesG[i].getColor().a>0.5f)
				{
					SbranchesG[i].setAlpha(0f);
					Item nit = GameScreen.items.CreateItem(AllBlocTypes.instance.Log.Id,position.x,position.y);
					if(rand.nextFloat()>0.9f)nit = GameScreen.items.CreateItem(AllTools.instance.StdTreeId,position.x,position.y);
					GameScreen.items.placeItem(nit);
				}
			}
		for(int i = 0;i<SbranchesD.length; i++)
			{
				position = getRelativePos(SbranchesD[i].getRotation()+4,SbranchesD[i].getX(),SbranchesD[i].getY(),SbranchesD[i].getOriginX(),SbranchesD[i].getOriginY());
				if(isCollide(position.x, position.y) && SbranchesD[i].getColor().a>0.5f)
				{
					SbranchesD[i].setAlpha(0f);
					Item nit = GameScreen.items.CreateItem(AllBlocTypes.instance.Log.Id,position.x,position.y);
					if(rand.nextFloat()>0.9f)nit = GameScreen.items.CreateItem(AllTools.instance.StdTreeId,position.x,position.y);
					GameScreen.items.placeItem(nit);
				}
			}
		for(int i = 0;i<Stroncs.length; i++)
			{
				position = getRelativePos(Stroncs[i].getRotation()+4,Stroncs[i].getX(),Stroncs[i].getY(),Stroncs[i].getOriginX(),Stroncs[i].getOriginY());
				if(isCollide(position.x, position.y)&& Stroncs[i].getColor().a>0.5f)
				{
					Stroncs[i].setAlpha(0f);
					Item nit = GameScreen.items.CreateItem(AllBlocTypes.instance.Log.Id,position.x,position.y);
					GameScreen.items.placeItem(nit);
				}
			}
		position = getRelativePos(Scime.getRotation()+5,Scime.getX(),Scime.getY(),Scime.getOriginX(),Scime.getOriginY());
		if(isCollide(position.x, position.y)&& Scime.getColor().a>0.5f)
		{
			Scime.setAlpha(0f);
			Item nit = GameScreen.items.CreateItem(AllBlocTypes.instance.Log.Id,position.x,position.y);
			if(rand.nextFloat()>0.3f)nit = GameScreen.items.CreateItem(AllTools.instance.StdTreeId,position.x,position.y);
			GameScreen.items.placeItem(nit);
		}
		
		if(Stroncs[0].getRotation()>180)
		isDead = true;
	}
	
	public void superDraw(SpriteBatch batch)
	{
		super.draw(batch);
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		//superDraw(batch);
		//BreakTree();
		
		Vector3 shadow;
		if(isEclosed)
		{
			shadow = getShadow(x+10,y+25);
					
			for(int i = 0;i<SbranchesG.length; i++)SbranchesG[i].setColor(shadow.x,shadow.y,shadow.z,SbranchesG[i].getColor().a);
			for(int i = 0;i<SbranchesD.length; i++)SbranchesD[i].setColor(shadow.x,shadow.y,shadow.z,SbranchesD[i].getColor().a);
			for(int i = 0;i<Stroncs.length; i++)Stroncs[i].setColor(shadow.x,shadow.y,shadow.z,Stroncs[i].getColor().a);
			Scime.setColor(shadow.x,shadow.y,shadow.z,Scime.getColor().a);
			
			if(durability<=0)DestroyTree();
			
			
			for(int i = 0;i<SbranchesG.length; i++)SbranchesG[i].draw(batch);
			for(int i = 0;i<SbranchesD.length; i++)SbranchesD[i].draw(batch);
			for(int i = 0;i<Stroncs.length; i++)Stroncs[i].draw(batch);
			Scime.draw(batch);
			
			for(int i = 0; i<sizetroncx; i++)
			{
				if(!Map.instance.mainLayer.getBloc(x+i, y-1).collide && durability>0)durability = -1;
			}
		}
		else
		{
			checkEclosion();
			if(miniDurability<=0 && !isEclosed)
			{
				GameScreen.items.placeItem(GameScreen.items.CreateItem(AllTools.instance.StdTreeId,x*16+8,y*16+8));
				isDead = true;
			}
			
			shadow = getShadow(x+8,y+8);
			Spousse.setColor(shadow.x,shadow.y,shadow.z,Spousse.getColor().a);
			Spousse.draw(batch);
			
			for(int i = 0; i<sizetroncx; i++)
			{
				if(!Map.instance.mainLayer.getBloc(x+i, y-1).collide)miniDurability = -1;
			}
		}
		
		
	}
	private Vector3 getShadow(float x, float y)
	{
		//return 1;
		return Tools.getShadowColor(Tools.floor(x/16),Tools.floor(y/16));
	}
	
	public void RotateAll(float x)
	{
		x *= Main.delta;
		for(int i = 0;i<SbranchesG.length; i++)SbranchesG[i].rotate(x);
		for(int i = 0;i<SbranchesD.length; i++)SbranchesD[i].rotate(x);
		for(int i = 0;i<Stroncs.length; i++)Stroncs[i].rotate(x);
		Scime.rotate(x);
	}
	
	private boolean isCollide(float x, float y)
	{
		if(Stroncs[0].getRotation()>140)return true;
		BlocType cell = Map.instance.mainLayer.getBloc(Tools.floor(x/Map.instance.mainLayer.getTileWidth()), Tools.floor(y/Map.instance.mainLayer.getTileHeight()));
		return cell.collide;
	}
	
	public boolean isCollidingTree(float x, float y)
	{
		if(Math.abs(GameScreen.player.x-x)<3000)
		{
			if(isEclosed)
			{
				for(int i = 0;i<SbranchesG.length; i++)if(isClicked(SbranchesG[i],x,y,0,0))return true;
				for(int i = 0;i<SbranchesD.length; i++)if(isClicked(SbranchesD[i],x,y,0,0))return true;
				for(int i = 0;i<Stroncs.length; i++)if(isClicked(Stroncs[i],x,y,5,0))return true;
				if(isClicked(Scime,x,y,0,0))return true;
				if(isClicked(Stroncs[0],x,y+Stroncs[0].getHeight()/2,6,0))return true;
			}
			else
			{
				if(isClicked(Spousse,x,y,2,16))return true;
			}
		}
		return false;
	}
	
	public boolean isBreakingTree(float x, float y)
	{
		if(Math.abs(GameScreen.player.x-x)<3000)
		{
			if(isClicked(Stroncs[0],x,y,0,0))return true;
			if(isClicked(Stroncs[1],x,y,0,0))return true;
		}
		return false;
	}
	
	public boolean isClicked(Sprite spr,float x,float y, float margX, float margY)
	{
		if((x > spr.getX()-margX && x < spr.getX()+spr.getWidth()+margX) && (y > spr.getY()-margY && y < spr.getY()+spr.getHeight()+margY/3))return true;
		else return false;
	}
	
	private Vector2 getRelativePos(float rotation, float x, float y,float Ox, float Oy)
	{
		float xy = (float) Math.sqrt(Ox*Ox+Oy*Oy);
		return new Vector2(x+Ox-(float)Math.sin(Math.toRadians(rotation)) * xy, y+Oy+(float)Math.cos(Math.toRadians(rotation)) * xy);
	}
	
	public void checkEclosion()
	{
		ecloseTime-=Main.delta;
		if(!isEclosed && ecloseTime <= 0)
		{
			boolean Ok = true;
			Entity curent = AllEntities.getEntity(x, y);
			for(int i = 0; i<9; i++)
			{
				for(int j = 3; j<25; j++)
				{
					if(!Map.instance.mainLayer.getBloc((i+x-4),j+y).air || (AllEntities.getEntity(x+i-4, y+j)!=null && AllEntities.getEntity(x+i-4, y+j)!=curent))Ok = false;
				}
			}
			for(int i = 0; i<3; i++)
			{
				for(int j = 0; j<3; j++)
				{
					if(!Map.instance.mainLayer.getBloc((i+x-1),j+y).air || (AllEntities.getEntity(x+i-1, y+j)!=null && AllEntities.getEntity(x+i-1, y+j)!=curent))Ok = false;
				}
			}

			if(Ok)
			{
				isEclosed = true;
			}
			else
			{
				ecloseTime = 10;
			}
		}
	}
	
	@Override
	 public float getX()
		{
			return x*16;
		}
	 @Override
	 public float getY()
		{
			return y*16;
		}
	 @Override
	 public void setX(float xii)
		{
		 SetPosition((int)(xii/16), y);
		}
	 @Override
	 public void setY(float xii)
		{
		 SetPosition(x, (int)(xii/16));
		}
}
