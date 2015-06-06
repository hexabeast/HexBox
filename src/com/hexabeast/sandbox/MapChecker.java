package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MapChecker {

	
	public static MapChecker instance;
	
	BlocType clicCellType;
	BlocType upCellType;
	BlocType downCellType;
	BlocType rightCellType;
	BlocType leftCellType;
	
	int currentDecalX = 0;
	int currentDecalY = 0;
	
	BlocType NewId;
	
	public MapChecker() {
		
	}
	
	
	public void CheckMap(MapLayer layer,OrthographicCamera camera, AllBlocTypes cells)
	{
		Vector3 worldCoordinates = new Vector3(0, 0, 0);		
		camera.unproject(worldCoordinates);
		
		for(int i = 0; i<layer.getWidth();i++)
		{
			for(int j = 2; j<layer.getHeight()-1;j++)
			{			
				CheckCell(layer,i,j);
			}
		}
	}
	
	public void CheckLine(MapLayer layer, int line)
	{		
		for(int i = 0; i<layer.getWidth();i++)
		{		
			if(layer.getBloc(i,line) != AllBlocTypes.instance.Empty)CheckCell(layer,i,line);
		}
	}
	
	
	public void CheckCell(MapLayer layer,int decalX,int decalY)
	{
		currentDecalX = decalX;
		currentDecalY = decalY;
		
		
		if(!Map.instance.backLayer.getBloc(0+decalX,0+decalY).collide)
		{
			if(Map.instance.mainLayer.getBloc(0+decalX,0+decalY).needBack)
			{
				Item nit = GameScreen.items.CreateItem(Map.instance.mainLayer.getBloc(decalX,decalY).Id,decalX*layer.getTileWidth(),decalY*layer.getTileHeight());
				GameScreen.items.placeItem(nit);
				SetCell(Map.instance.mainLayer,AllBlocTypes.instance.getType(0), AllBlocTypes.instance.full,decalX,decalY,true);
				CheckCell(Map.instance.mainLayer,decalX,decalY);
				CheckCell(Map.instance.mainLayer,decalX+1,decalY);
				CheckCell(Map.instance.mainLayer,decalX,decalY+1);
				CheckCell(Map.instance.mainLayer,decalX-1,decalY);
				CheckCell(Map.instance.mainLayer,decalX,decalY-1);
			}
		}

		clicCellType = layer.getBloc(0+decalX,0+decalY);
		upCellType = layer.getBloc(0+decalX,1+decalY);
		downCellType = layer.getBloc(0+decalX,-1+decalY);
		rightCellType = layer.getBloc(1+decalX,0+decalY);
		leftCellType = layer.getBloc(-1+decalX,0+decalY);
		
		
		if(clicCellType.stdCheck)
		{
			if(!clicCellType.oldTexture)newCheck(layer,decalX,decalY);
			else oldCheck(layer,decalX,decalY);
		}
	}
	
	
	
	
	private void oldCheck(MapLayer layer, int decalX, int decalY)
	{
		if(clicCellType == AllBlocTypes.instance.Dirt && layer == Map.instance.mainLayer)
		{
			int i = (int)decalX;
			int j = (int)decalY;
			if
			((AllBlocTypes.instance.IsTransparent(layer.getBloc(i+1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i+1, j))) ||
					 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i-1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i-1, j))) ||		
					 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i, j+1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j+1))) ||
					 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i, j-1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j-1))) )
			{
				if(j>Map.instance.limit)
				{
					SetCell(layer,AllBlocTypes.instance.Grass,RanTile(AllBlocTypes.instance.oldFull),decalX,decalY,false);
					clicCellType = layer.getBloc(0+decalX,0+decalY);	
				}
			}	
		}
		
		if(clicCellType == AllBlocTypes.instance.Grass)
		{
			int i = (int)decalX;
			int j = (int)decalY;
			if
			(!(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i+1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i+1, j))) &&
					 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i-1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i-1, j))) &&		
					 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i, j+1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j+1))) &&
					 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i, j-1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j-1))) )
					{
				SetCell(layer,AllBlocTypes.instance.Dirt,RanTile(AllBlocTypes.instance.oldFull),decalX,decalY,false);
				clicCellType = layer.getBloc(0+decalX,0+decalY);
			}	
		}
		
		NewId = clicCellType;
		
		///////////////////ALL SIDES SAME
		if(sameRight() && sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.oldFull),decalX,decalY,true);
		}
		
		//////////////////THREE SIDES SAME
		else if(sameRight() && sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.iDown),decalX,decalY,true);
		}
		else if(sameRight() && sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.iUp),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.iLeft),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.iRight),decalX,decalY,true);
		}
		/////////////////TWO SIDES SAME
		else if(sameRight() && sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.RightLeft),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.LeftUp),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.UpDown),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.RightDown),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.LeftDown),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.RightUp),decalX,decalY,true);
		}
		
		///////////////////ONE SIDE SAME
		else if(sameRight() && !sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Right),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Left),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Up),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Down),decalX,decalY,true);
		}
		///////////////ALONE
		else if(!sameRight() && !sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Alone),decalX,decalY,true);
		}
	}
	
	
	
	
	
	private void newCheck(MapLayer layer, int decalX, int decalY)
	{
		if(clicCellType == AllBlocTypes.instance.Dirt && layer == Map.instance.mainLayer)
		{
			int i = (int)decalX;
			int j = (int)decalY;
			if
			((AllBlocTypes.instance.IsTransparent(layer.getBloc(i+1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i+1, j))) ||
			 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i-1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i-1, j))) ||		
			 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i, j+1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j+1))) ||
			 (AllBlocTypes.instance.IsTransparent(layer.getBloc(i, j-1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j-1))) )
			{
				if(j>Map.instance.limit)
				{
					SetCell(layer,AllBlocTypes.instance.Grass,RanTile(AllBlocTypes.instance.full,0),decalX,decalY,false);
					clicCellType = layer.getBloc(0+decalX,0+decalY);	
				}
			}	
		}
		
		if(clicCellType == AllBlocTypes.instance.Grass)
		{
			int i = (int)decalX;
			int j = (int)decalY;
			if
			(!(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i+1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i+1, j))) &&
			 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i-1, j)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i-1, j))) &&		
			 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i, j+1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j+1))) &&
			 !(AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i, j-1)) && AllBlocTypes.instance.IsTransparent(Map.instance.backLayer.getBloc(i, j-1))) )
			{
				SetCell(layer,AllBlocTypes.instance.Dirt,RanTile(AllBlocTypes.instance.full,0),decalX,decalY,false);
				clicCellType = layer.getBloc(0+decalX,0+decalY);
			}	
		}
		
		NewId = clicCellType;
		
		///////////////////ALL SIDES SAME
		if(sameRight() && sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.full,0),decalX,decalY,true);
		}
		
		//////////////////THREE SIDES SAME
		else if(sameRight() && sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIIsides,2),decalX,decalY,true);
		}
		else if(sameRight() && sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIIsides,0),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIIsides,1),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIIsides,3),decalX,decalY,true);
		}
		/////////////////TWO SIDES SAME
		else if(sameRight() && sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsidesOpp,1),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsides,2),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsidesOpp,0),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsides,0),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsides,3),decalX,decalY,true);
		}
		else if(sameRight() && !sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.IIsides,1),decalX,decalY,true);
		}
		
		///////////////////ONE SIDE SAME
		else if(sameRight() && !sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Isides,1),decalX,decalY,true);
		}
		else if(!sameRight() && sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Isides,3),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Isides,2),decalX,decalY,true);
		}
		else if(!sameRight() && !sameLeft() && !sameUp() && sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Isides,0),decalX,decalY,true);
		}
		///////////////ALONE
		else if(!sameRight() && !sameLeft() && !sameUp() && !sameDown())
		{
			SetCell(layer,NewId,RanTile(AllBlocTypes.instance.Nosides,0),decalX,decalY,true);
		}
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean sameRight()
	{
		return isSame(rightCellType);
	}
	private boolean sameLeft()
	{
		return isSame(leftCellType);
	}
	private boolean sameUp()
	{
		return isSame(upCellType);
	}
	private boolean sameDown()
	{
		return isSame(downCellType);
	}
	
	private boolean isSame(BlocType b)
	{
		BlocType b2 = clicCellType;
		
		if(!b2.artificial && b.artificial)return true;
		if(!Parameters.i.oldtransition && (!b2.nonblock && !b.nonblock) && (!b2.artificial && !b.artificial) && (b2.merge.dirtish || b.merge.dirtish))return true;
		return ConvertBlock(b) == ConvertBlock(b2);
	}
	
	private BlocType ConvertBlock(BlocType bloc)
	{
		//if(id==AllBlocTypes.instance.GrassId || id==AllBlocTypes.instance.FossileId)id = AllBlocTypes.instance.DirtId;
		//if(id==AllBlocTypes.instance.CrystalFireId || id==AllBlocTypes.instance.CrystalBlueId || id==AllBlocTypes.instance.CrystalDarkId || id==AllBlocTypes.instance.CrystalGreenId || id==AllBlocTypes.instance.CrystalPurpleId || id==AllBlocTypes.instance.CrystalRedId)id = AllBlocTypes.instance.CobbleId;
		return bloc.merge;
	}
	
	private void SetCell(MapLayer layer,BlocType cell, int state, int decalX,int decalY, boolean important){
		layer.setBloc((int)decalX, (int)decalY, cell, state,important);
	}
	
	public int RanTile(int Id, int angle, int x, int y)
	{
		if(Id == AllBlocTypes.instance.full)return Id+10*Main.random.nextInt(4);
		return Id+10*angle;
	}	
	
	private int RanTile(int Id, int angle)
	{
		return RanTile(Id,angle,currentDecalX, currentDecalY);
	}
	
	public int RanTile(int Id, int x, int y)
	{
		return Id+Main.random.nextInt(3);
	}
	
	private int RanTile(int Id)
	{
		return RanTile(Id, currentDecalX, currentDecalY);
	}

}
