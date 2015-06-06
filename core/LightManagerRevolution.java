package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManagerRevolution {
	
	public LightCoord[][] lightArray;

	public LightCoord[][] lightChanges;
	
	public List<Vector5> dynaList;
	public List<Vector5> staticdynaList;
	
	float factorDirt = 0.8f;
	float factorAirLow = 0.95f;
	float factorMin = 0.02f;
	float speed = 1f/120;
	float speedTime = 10;
	boolean updated = false;
	public float daySwitch = 0f;
	
	public int chunksize = 100;
	public ArrayList<Vec2> activeChunks;
	
	public int currentChunkX = 0;
	public int currentChunkY = 0;
	
	boolean isLosange = false;
	
	int aj;
	int ai;
	
	int chunkNumberX;
	int chunkNumberY;
	
	public Vector3 lightVec = new Vector3();
	
	public LightManagerRevolution() 
	{
		if(Main.mobile)speed = 1f/30;
		lightArray = new LightCoord[Map.instance.width][Map.instance.height];
		
		chunkNumberX = Map.instance.width/chunksize;
		chunkNumberY = Map.instance.height/chunksize;
		
		lightChanges = new LightCoord[150][150];

		for(int i = 0; i<lightChanges.length; i++)
		{
			for( int j = 0; j<lightChanges[0].length; j++)
			{
				lightChanges[i][j] = new LightCoord(0,0,0);
			}
		}
		
		activeChunks = new ArrayList<Vec2>();
		for(int i = 0; i<9; i++)activeChunks.add(new Vec2(i,0));
		
		/*chunkgrid = new boolean[Map.instance.width/chunksize][Map.instance.height/chunksize];
		
		for(int i = 0; i<chunkgrid.length; i++)
		{
			for(int j = 0; j<chunkgrid[0].length; j++)
			{
				chunkgrid[i][j]
			}
		}*/
		
		for(int i = 0; i<chunksize*9; i++)
		{
			for( int j = 0; j<chunksize; j++)
			{
				lightArray[i][j] = new LightCoord(0,0,0);
			}
		}
		dynaList = new ArrayList<Vector5>();
		staticdynaList = new ArrayList<Vector5>();
	}
	
	public Vector3 getLight(int x, int y)
	{		
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;
			
		if(!Parameters.i.RGB)
		{
				
			lightVec.x = lightArray[x][y].getX();
			lightVec.y = lightArray[x][y].getX();
			lightVec.z = lightArray[x][y].getX();
		}
		else
		{
			lightVec.x = lightArray[x][y].getX();
			lightVec.y = lightArray[x][y].getY();
			lightVec.z = lightArray[x][y].getZ();
		}
	
		return lightVec;
	}
	
	public Vector3 getLightChange(int x, int y)
	{		
		if(!Parameters.i.RGB)
		{
				
			lightVec.x = lightChanges[x][y].getX();
			lightVec.y = lightChanges[x][y].getX();
			lightVec.z = lightChanges[x][y].getX();
		}
		else
		{
			lightVec.x = lightChanges[x][y].getX();
			lightVec.y = lightChanges[x][y].getY();
			lightVec.z = lightChanges[x][y].getZ();
		}
	
		return lightVec;
	}
	
	public float getLight(int x, int y, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;
		return lightArray[x][y].c[i];
	}
	
	public float getLightChange(int x, int y, int i)
	{
		return lightChanges[x][y].c[i];
	}
	
	public float getLightIntern(int x, int y, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;
		return lightArray[x][y].c[i];
	}
	
	public Vector3 getLightIntern(int x, int y)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;

		LightCoord temp = lightArray[x][y];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		
		return lightVec;
	}
	
	public void setOldLight(int x, float z, float w, float v)
	{

		if(dynaList.get(x).x>=Map.instance.width)dynaList.get(x).x = dynaList.get(x).x-Map.instance.width;
		if(dynaList.get(x).x<0)dynaList.get(x).x = dynaList.get(x).x+Map.instance.width;
		
		if(dynaList.get(x).y>=Map.instance.height)dynaList.get(x).y = Map.instance.height-1;
		if(dynaList.get(x).y<0)dynaList.get(x).y = 0;
	
		setLightDyn(dynaList.get(x).x,dynaList.get(x).y,z,w,v);
	}
	
	public void setDynLight(int x, int y, float z, float w, float v)
	{
		
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;

		if(lightArray[x][y].c[0]<z)lightArray[x][y].c[0] = z;
		if(lightArray[x][y].c[1]<w)lightArray[x][y].c[1] = w;
		if(lightArray[x][y].c[2]<v)lightArray[x][y].c[2] = v;
	}
	
	public void setLight(int x, int y, float z, float w, float v)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;

		lightArray[x][y].setX(z);
		lightArray[x][y].setY(w);
		lightArray[x][y].setZ(v);
	}
	
	public void setLight(int x, int y, float z, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;
		

		lightArray[x][y].c[i] = z;
	}
	
	public void setLightIntern(int x, int y, float z, int i)
	{
		lightChanges[x][y].c[i] = z;
	}
	
	
	public void setLightDyn(int x,int y, float z, float w, float v)
	{
		if(lightArray[x][y].getX()<z)lightArray[x][y].setX(z);
		if(lightArray[x][y].getY()<w)lightArray[x][y].setY(w);
		if(lightArray[x][y].getZ()<v)lightArray[x][y].setZ(v);
	}
	/*
	public void setDynLight(int x,int y, float z, float w, float v)
	{
		if(dynlightArray[x-offsetX][y-offsetY].getX()<z)dynlightArray[x-offsetX][y-offsetY].setX(z);
		if(dynlightArray[x-offsetX][y-offsetY].getY()<w)dynlightArray[x-offsetX][y-offsetY].setY(w);
		if(dynlightArray[x-offsetX][y-offsetY].getZ()<v)dynlightArray[x-offsetX][y-offsetY].setZ(v);
	}
	
	public void setDynLightBis(int x,int y, float z, float w, float v)
	{
		dynlightArray[x-offsetX][y-offsetY].setX(z);
		dynlightArray[x-offsetX][y-offsetY].setY(w);
		dynlightArray[x-offsetX][y-offsetY].setZ(v);
	}*/
	
	public void UpdateLight()
	{
		speed = 1f/Parameters.i.lightSpeed;
		updateDay();
		Vector2 cellPos0 = Converter.screenToMapCoords(0,Gdx.graphics.getHeight());
		Vector2 cellPos1 = Converter.screenToMapCoords(Gdx.graphics.getWidth(),0);
		if(cellPos1.x-cellPos0.x<90)Update((int)cellPos0.x-20,(int)cellPos1.x+20,(int)cellPos0.y-20,(int)cellPos1.y+20);
	}
	
	public boolean activeChunksContains(int x, int y)
	{
		for(int i = 0; i<activeChunks.size(); i++)
		{
			if(activeChunks.get(i).x == x && activeChunks.get(i).y == y)return true;
		}
		return false;
	}
	
	public void Update(int minx, int maxx, int miny,int maxy )
	{	
		updated = true;
		speedTime+=Main.delta;

		int cur = 0;
		int max = 3;
		
		if(maxy>Map.instance.height-1)maxy = Map.instance.height-1;
		if(miny<0)miny=0;
		
		int cw = (int)(maxx-minx);
		int ch = (int)(maxy-miny);
		
		currentChunkX = Tools.getBounded((int)((minx+(cw/2))/chunksize),chunkNumberX);
		currentChunkY = Math.min(chunkNumberY-2, Math.max(1, (int)((miny+(ch/2))/chunksize)));
		
		for(int i = 0; i<activeChunks.size(); i++)
		{
			int difx = Math.abs(activeChunks.get(i).x-currentChunkX);
			int dify =  Math.abs(activeChunks.get(i).y-currentChunkY);
			if((difx>1 && difx<19) || (dify>1))
			{
				int ti = -404;
				int tj = -404;
				for(int k = currentChunkX-1; k<=currentChunkX+1; k++)
				{
					for(int j = currentChunkY-1; j<=currentChunkY+1; j++)
					{
						int nk = Tools.getBounded(k,chunkNumberX);
						if(!activeChunksContains(nk,j))
						{
							ti = nk;
							tj = j;
						}
					}
				}
				
				int baseX = activeChunks.get(i).x*100;
				int baseY = activeChunks.get(i).y*100;
				
				int endX = ti*100;
				int endY = tj*100;
				
				for(int j = 0; j<chunksize; j++)
				{
					for(int k = 0; k<chunksize; k++)
					{
						lightArray[endX+j][endY+k] = lightArray[baseX+j][baseY+k];
						lightArray[endX+j][endY+k].reinit();
						lightArray[baseX+j][baseY+k] = null;
					}
				}
				
				activeChunks.get(i).x = ti;
				activeChunks.get(i).y = tj;
			}
		}
		
		
		/*System.out.println("-----------");
		for(int i = 0; i<activeChunks.size(); i++)
		{
			System.out.println("---");
			System.out.println(activeChunks.get(i).x);
			System.out.println(activeChunks.get(i).y);
		}
		*/
		
		while(speedTime>speed && cur<max)
		{
			cur++;
			speedTime-=speed;
			
			int colors = 3;
			if(!Parameters.i.RGB)colors = 1;
			
			for(int n = 0; n<colors;n++)
			{
				for(int i=0;i<cw;i++)
				{
					for(int j=0;j<ch;j++)
					{
						ai = i+minx;
						aj = j+miny;
						
						BlocType cell = Map.instance.mainLayer.getBloc(ai, aj);
						
						if(AllBlocTypes.instance.IsTransparent(cell) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(ai+1,aj)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(ai-1,aj)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(ai,aj-1)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(ai,aj+1)))
						{
							cell = Map.instance.backLayer.getBloc(ai, aj);
							//isLosange = true;
								
							float source = 0;
							
							source = getHighest(getLight(ai,aj+1,n),getLight(ai,aj-1,n),getLight(ai+1,aj,n),getLight(ai-1,aj,n),1);
							
							if(source*factorAirLow>factorMin)
							{
								setLightIntern(i,j,source*factorAirLow,n);
							}
							else
							{
								setLightIntern(i,j,0,n);
							}
							
							if(aj>Map.instance.limit && AllBlocTypes.instance.IsTransparent(cell) && getLightChange(i,j,n)<Parameters.i.daylight)
							{
								setLightIntern(i,j,Parameters.i.daylight,n);
							}
						}
						else 
						{
							float source = getHighest(getLight(ai,aj+1,n),getLight(ai,aj-1,n),getLight(ai+1,aj,n),getLight(ai-1,aj,n),1);
							if(source>factorMin)
							{
								source = Math.max(source,getHighest(getLight(ai+1,aj+1,n),getLight(ai-1,aj-1,n),getLight(ai+1,aj-1,n),getLight(ai-1,aj+1,n),0.88f));
								if(source*(factorAirLow-0.3f)>factorMin)
								{
									setLightIntern(i,j,source*(factorAirLow-0.3f),n);
								}
								else
								{
									setLightIntern(i,j,0,n);
								}						
							}
							else
							{
								setLightIntern(i,j,0,n);
							}
						}
						
						cell = Map.instance.mainLayer.getBloc(ai, aj);
						if(cell.lightFull)
						{
							if(Parameters.i.RGB)
							{
								float coll = cell.lightColor[n];
								setLightIntern(i,j,Math.max((float) (coll+0.1f), getLight(ai,aj,n)),n);
							}
							else
							{
								float coll = Math.max(cell.lightColor[0], Math.max(cell.lightColor[1], cell.lightColor[2]));
								setLightIntern(i,j,Math.max((float) (coll+0.1f), getLightChange(i,j,n)),n);
							}
						}
					}
				}
			}
			
			for(int i=0; i<cw; i++)
			{
				for(int j=0; j<ch; j++)
				{
					Vector3 temp = getLightChange(i,j);
					if(Parameters.i.RGB)
					{
						setLight(i+minx,j+miny,temp.x,temp.y,temp.z);
					}
					else
					{
						setLight(i+minx,j+miny,temp.x,0);
					}
				}
			}
			
			for(int m = 0; m<dynaList.size(); m++)
			{
				setOldLight(m,dynaList.get(m).z,dynaList.get(m).w,dynaList.get(m).v);
			}
		}
		
		
		if(cur>0)
		{
			for(int m = 0; m<staticdynaList.size(); m++)
			{
				setDynLight(staticdynaList.get(m).x,staticdynaList.get(m).y,staticdynaList.get(m).z,staticdynaList.get(m).w,staticdynaList.get(m).v);
			}
		}
		
		dynaList = new ArrayList<Vector5>();
		staticdynaList = new ArrayList<Vector5>();
		if(cur>0)speedTime = 0;
	}
	
	/*private float getHighest(float a, float b, float c, float d,float x, float f, float g, float h, float diag)
	{
		x*=diag;
		f*=diag;
		g*=diag;
		h*=diag;
		
		float e = 0;

		e = a;
				
		if(b>e)
		{
			e = b;
		}
		if(c>e)
		{
			e = c;
		}
		if(d>e)
		{
			e = d;
		}
		
		if(x>e)
		{
			e = x;
		}
		if(f>e)
		{
			e = f;
		}
		if(g>e)
		{
			e = g;
		}
		if(h>e)
		{
			e = h;
		}
		
		//e = (e+(a+b+c+d)/8)/1.5f;
		//if(e>1) e =1;
		return e;
	}*/
	
	private float getHighest(float a, float b, float c, float d, float diag)
	{
		if(diag!=1)
		{
			a*=diag;
			b*=diag;
			c*=diag;
			d*=diag;
		}
		float e = 0;

		e = a;
				
		if(b>e)
		{
			e = b;
		}
		if(c>e)
		{
			e = c;
		}
		if(d>e)
		{
			e = d;
		}
		return e;
	}
	
	public void tempLight(float x, float y, float ix, float iy, float iz)
	{
		int x2 = (int)(x/Map.instance.mainLayer.getTileWidth());
		int y2 = (int)(y/Map.instance.mainLayer.getTileHeight());
		
		if(Parameters.i.RGB)dynaList.add(new Vector5(x2,y2,ix, iy, iz));
		else
		{
			float max = (Math.max(ix, Math.max(iy, iz))+ix+iy+iz)/4;
			dynaList.add(new Vector5(x2,y2,max, max, max));
		}
	}
	
	public void tempLighti(int x, int y, float ix, float iy, float iz)
	{
		
		if(Parameters.i.RGB)dynaList.add(new Vector5(x,y,ix, iy, iz));
		else
		{
			float max = (Math.max(ix, Math.max(iy, iz))+ix+iy+iz)/4;
			dynaList.add(new Vector5(x,y,max, max, max));
		}
	}
	
	
	public void staticTempLight(int x, int y, float ix, float iy, float iz)
	{
		/*for(int i = 0; i<staticdynaList.size(); i++)
		{
			if(staticdynaList.get(i).x == x && staticdynaList.get(i).y == y)
			{
				staticdynaList.get(i).z = Math.max(staticdynaList.get(i).z, ix);
				staticdynaList.get(i).w = Math.max(staticdynaList.get(i).w, iy);
				staticdynaList.get(i).v = Math.max(staticdynaList.get(i).v, iz);
				return;
			}
		}*/
		staticdynaList.add(new Vector5(x,y,ix, iy, iz));

	}
	
	public void updateDay()
	{
		Parameters.i.daylight+=Main.delta*daySwitch;
		if(Parameters.i.daylight<0.1f)Parameters.i.daylight = 0.1f;
		if(Parameters.i.daylight>1)Parameters.i.daylight = 1;
	}
	
	public void switchDayBegin(float change)
	{
		daySwitch = change;
	}
	public void switchDayEnd()
	{
		daySwitch = 0;
	}

}
