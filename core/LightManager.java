package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManager {
	
	public LightCoord[][][][] lightArray;
	public LightCoord[][][][] oldLightArray;
	public List<Vector5> dynaList;
	public List<Vector5> staticdynaList;
	
	float factorDirt = 0.8f;
	float factorAirLow = 0.95f;
	float factorMin = 0.02f;
	float speed = 1f/120;
	float speedTime = 10;
	boolean updated = false;
	public float daySwitch = 0f;
	int chunkSize = 100;
	int currentchunkx = 1;
	int currentchunky = 1;
	
	int chunksx = 0;
	int chunksy = 0;

	int[][][] used = new int[3][3][2];
	
	boolean isLosange = false;
	
	public Vector3 lightVec = new Vector3();
	
	public LightManager() 
	{
		
		chunksx = (int)(Map.instance.width/chunkSize);
		chunksy = (int)(Map.instance.height/chunkSize);
		
		if(Main.mobile)speed = 1f/30;
		lightArray = new LightCoord[chunksx][chunksy][chunkSize][chunkSize];
		oldLightArray = new LightCoord[chunksx][chunksy][chunkSize][chunkSize];
		for(int k = 0; k<3; k++)
		{
			for(int l = 0; l<3; l++)
			{
				used[k][l][0] = 0;
				used[k][l][0] = 1;
				for(int i = 0; i<chunkSize; i++)
				{
					for( int j = 0; j<chunkSize; j++)
					{
						lightArray[k][l][i][j] = new LightCoord(0,0,0);
						oldLightArray[k][l][i][j] = new LightCoord(0,0,0);
					}
				}
			}
		}
		dynaList = new ArrayList<Vector5>();
		staticdynaList = new ArrayList<Vector5>();
	}
	
	public void emptyArray(LightCoord[][] tab)
	{
		for(int i = 0; i<chunkSize; i++)
		{
			for( int j = 0; j<chunkSize; j++)
			{
				tab[i][j].setX(0);
				tab[i][j].setY(0);
				tab[i][j].setZ(0);
			}
		}
	}
	
	public Vector3 getLight(int x, int y)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			
			if(!Parameters.i.RGB)
			{
					
				lightVec.x = lightArray[cx][cy][x][y].getX();
				lightVec.y = lightArray[cx][cy][x][y].getX();
				lightVec.z = lightArray[cx][cy][x][y].getX();
			}
			else
			{
				lightVec.x = lightArray[cx][cy][x][y].getX();
				lightVec.y = lightArray[cx][cy][x][y].getY();
				lightVec.z = lightArray[cx][cy][x][y].getZ();
			}
		}
		
		else
		{
			lightVec.x = 0;
			lightVec.y = 0;
			lightVec.z = 0;
		}
		
		return lightVec;
	}
	
	
	public float getLight(int x, int y, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			return lightArray[cx][cy][x][y].c[i];
		}
		else return 0;
	}
	
	public float getLightIntern(int x, int y, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	

		return lightArray[Tools.getIndex(cx, chunksx)][Tools.getIndex(cy, chunksx)][x-cx*chunkSize][y-cy*chunkSize].c[i];
	}
	
	public Vector3 getLightIntern(int x, int y)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	

		LightCoord temp = lightArray[Tools.getIndex(cx, chunksx)][Tools.getIndex(cy, chunksx)][x-cx*chunkSize][y-cy*chunkSize];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		
		return lightVec;
	}
	
	public Vector3 getOldLight(int x, int y)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	

		LightCoord temp = oldLightArray[Tools.getIndex(cx, chunksx)][Tools.getIndex(cy, chunksx)][x-cx*chunkSize][y-cy*chunkSize];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		
		return lightVec;
	}
	
	public float getOldLight(int x, int y, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);

		return oldLightArray[Tools.getIndex(cx, chunksx)][Tools.getIndex(cy, chunksx)][x-cx*chunkSize][y-cy*chunkSize].c[i];
	}
	
	public void setOldLight(int x, float z, float w, float v)
	{

		int cx = Tools.floor((float)dynaList.get(x).x/chunkSize);
		int cy = Tools.floor((float)dynaList.get(x).y/chunkSize);
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;
		
		int px=dynaList.get(x).x-cx*chunkSize;
		int py=dynaList.get(x).y-cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
	
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{	
			setOldLightDyn(px,py,z,w,v,cx,cy);
		}
	}
	
	public void setDynLight(int x, int y, float z, float w, float v)
	{
		
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			if(lightArray[cx][cy][x][y].c[0]<z)lightArray[cx][cy][x][y].c[0] = z;
			if(lightArray[cx][cy][x][y].c[1]<w)lightArray[cx][cy][x][y].c[1] = w;
			if(lightArray[cx][cy][x][y].c[2]<v)lightArray[cx][cy][x][y].c[2] = v;
		}
	}
	
	public void setLight(int x, int y, float z, float w, float v)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			lightArray[cx][cy][x][y].setX(z);
			lightArray[cx][cy][x][y].setY(w);
			lightArray[cx][cy][x][y].setZ(v);
		}
	}
	
	public void setLight(int x, int y, float z, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			lightArray[cx][cy][x][y].c[i] = z;
		}
	}
	
	public void setLightIntern(int x, int y, float z, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);

		lightArray[Tools.getIndex(cx, chunksx)][Tools.getIndex(cy, chunksx)][x-cx*chunkSize][y-cy*chunkSize].c[i] = z;
	}
	
	public void setOldLight(int x,int y, float z, float w, float v)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		oldLightArray[cx][cy][x][y].setX(z);
		oldLightArray[cx][cy][x][y].setY(w);
		oldLightArray[cx][cy][x][y].setZ(v);
	}
	
	public void setOldLightDyn(int x,int y, float z, float w, float v, int cx, int cy)
	{
		if(oldLightArray[cx][cy][x][y].getX()<z)oldLightArray[cx][cy][x][y].setX(z);
		if(oldLightArray[cx][cy][x][y].getY()<w)oldLightArray[cx][cy][x][y].setY(w);
		if(oldLightArray[cx][cy][x][y].getZ()<v)oldLightArray[cx][cy][x][y].setZ(v);
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
	
	public void setOldLight(int x,int y, float z, int i)
	{
		int cx = Tools.floor((float)x/chunkSize);
		int cy = Tools.floor((float)y/chunkSize);	
		
		int difx = cx-currentchunkx;
		int dify = cy-currentchunky;

		x-=cx*chunkSize;
		y-=cy*chunkSize;
		
		cx = Tools.getIndex(cx, chunksx);
		cy = Tools.getIndex(cy, chunksx);
		
		if(Math.abs(difx)<2 && Math.abs(dify)<2)
		{
			oldLightArray[cx][cy][x][y].c[i] = z;
		}
	}
	
	public void UpdateLight()
	{
		speed = 1f/Parameters.i.lightSpeed;
		updateDay();
		Vector2 cellPos0 = Converter.screenToMapCoords(0,Gdx.graphics.getHeight());
		Vector2 cellPos1 = Converter.screenToMapCoords(Gdx.graphics.getWidth(),0);
		if(cellPos1.x-cellPos0.x<88)Update((int)cellPos0.x-6,(int)cellPos1.x+6,(int)cellPos0.y-6,(int)cellPos1.y+6);
	}
	
	
	
	public void Update(int minx, int maxx, int miny,int maxy )
	{	
		updated = true;
		speedTime+=Main.delta;

		int cur = 0;
		int max = 3;
		
		int chunkx = Tools.floor(GameScreen.camiddle.x/16/chunkSize);
		int chunky = Tools.floor(GameScreen.camiddle.y/16/chunkSize);
		
		int differencex = chunkx-currentchunkx;
		int differencey = chunky-currentchunky;
		
		if(Math.abs(differencex)>1 || Math.abs(differencey)>1)
		{
			System.out.println("0z");
			for(int i = -1; i<2; i++)
			{
				for(int j = -1; j<2; j++)
				{
					int ax = Tools.getIndex(chunkx+i,chunksx);
					int ay = Tools.getIndex(chunky+j,chunksy);
					int bx = Tools.getIndex(currentchunkx+i,chunksx);
					int by = Tools.getIndex(currentchunky+j,chunksy);
					
					oldLightArray[ax][ay] = oldLightArray[bx][by];
					oldLightArray[bx][by] = null;
					
					lightArray[ax][ay] = lightArray[bx][by];
					emptyArray(lightArray[ax][ay]);
					lightArray[bx][by] = null;
				}
			}
			currentchunkx = Tools.getIndex(chunkx, chunksx);
			currentchunky = Tools.getIndex(chunky, chunksy);
		}
		else
		{
			if(differencex==1)
			{
				System.out.println("1z");
				for(int j = -1; j<2; j++)
				{
					int ax = Tools.getIndex(currentchunkx-1,chunksx);
					int bx = Tools.getIndex(currentchunkx+2,chunksx);
					int by = Tools.getIndex(currentchunky+j,chunksy);
					
					Tools.swap(oldLightArray, ax,bx,by);
					oldLightArray[ax][by] = null;
					emptyArray(oldLightArray[bx][by]);
					
					Tools.swap(lightArray, ax,bx,by);
					lightArray[ax][by] = null;
					emptyArray(lightArray[bx][by]);
				}
				currentchunkx = Tools.getIndex(chunkx, chunksx);
			}
			else if(differencex==-1)
			{
				System.out.println("2z");
				for(int j = -1; j<2; j++)
				{
					int ax = Tools.getIndex(currentchunkx+1,chunksx);
					int bx = Tools.getIndex(currentchunkx-2,chunksx);
					int by = Tools.getIndex(currentchunky+j,chunksy);
					
					Tools.swap(oldLightArray, ax,bx,by);
					oldLightArray[ax][by] = null;
					emptyArray(oldLightArray[bx][by]);
					
					Tools.swap(lightArray, ax,bx,by);
					lightArray[ax][by] = null;
					emptyArray(lightArray[bx][by]);
				}
				currentchunkx = Tools.getIndex(chunkx, chunksx);
			}
			
			if(differencey==1)
			{
				System.out.println("3z");
				for(int i = -1; i<2; i++)
				{
					int ay = Tools.getIndex(currentchunky-1,chunksy);
					int by = Tools.getIndex(currentchunky+2,chunksy);
					int bx = Tools.getIndex(currentchunkx+i,chunksx);
					
					Tools.swap(oldLightArray[bx], ay,by);
					oldLightArray[bx][ay] = null;
					emptyArray(oldLightArray[bx][by]);
					
					Tools.swap(lightArray[bx], ay,by);
					lightArray[bx][ay] = null;
					emptyArray(lightArray[bx][by]);
				}
				currentchunky = Tools.getIndex(chunky, chunksy);
			}
			else if(differencey==-1)
			{
				System.out.println("4z");
				for(int i = -1; i<2; i++)
				{
					int ay = Tools.getIndex(currentchunky+1,chunksy);
					int by = Tools.getIndex(currentchunky-2,chunksy);
					int bx = Tools.getIndex(currentchunkx+i,chunksx);
					
					Tools.swap(oldLightArray[bx], ay,by);
					oldLightArray[bx][ay] = null;
					emptyArray(oldLightArray[bx][by]);
					
					Tools.swap(lightArray[bx], ay,by);
					lightArray[bx][ay] = null;
					emptyArray(lightArray[bx][by]);
				}
				currentchunky = Tools.getIndex(chunky, chunksy);
			}
		}
		
		if(maxy>Map.instance.height-1)maxy = Map.instance.height-1;
		if(miny<0)miny=0;
		
		while(speedTime>speed && cur<max)
		{
			
			cur++;
			
			speedTime-=speed;		
			
			
			int colors = 3;
			if(!Parameters.i.RGB)colors = 1;
			for(int n = 0; n<colors;n++)
			{
				for(int i=minx;i<(int)maxx;i++)
				{
					for(int j=miny;j<(int)maxy;j++)
					{
						if(j>1 && j<Map.instance.mainLayer.height-2)
						{
							int cell = Map.instance.mainLayer.getBloc(i, j);
							
							/*if(AllBlocTypes.instance.getType(cell).shadow)
							{
								setLight(i,j,0,n);
							}
							else */if(AllBlocTypes.instance.IsTransparent(cell) || 
							  AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i+1,j)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i-1,j)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i,j-1)) || AllBlocTypes.instance.IsTransparent(Map.instance.mainLayer.getBloc(i,j+1)))
							{
								cell = Map.instance.backLayer.getBloc(i, j);
								//isLosange = true;
									
								float source = 0;
								
								//else source  =  getHighest(oldLightArray[i][j+1],oldLightArray[i][j-1],oldLightArray[i+1][j],oldLightArray[i-1][j],oldLightArray[i+1][j+1],oldLightArray[i-1][j-1],oldLightArray[i+1][j-1],oldLightArray[i-1][j+1],0.88f);
								//if(i>TiledMap2.villageWidth && j>1600 && i<TiledMap2.villageWidth+1000 && source>0.1f)source*=(1+(1-factorAirLow));
									
								source = getHighest(getOldLight(i,j+1,n),getOldLight(i,j-1,n),getOldLight(i+1,j,n),getOldLight(i-1,j,n),1);
								
								if(source*factorAirLow>factorMin)
								{
									setLightIntern(i,j,source*factorAirLow,n);
								}
								else
								{
									setLightIntern(i,j,0,n);
								}
								
								if(j>Map.instance.limit && AllBlocTypes.instance.IsTransparent(cell) && getLight(i,j,n)<Parameters.i.daylight)
								{
									setLightIntern(i,j,Parameters.i.daylight,n);
								}
							}
							else 
							{
								float source = getHighest(getOldLight(i,j+1,n),getOldLight(i,j-1,n),getOldLight(i+1,j,n),getOldLight(i-1,j,n),1);
								if(source>factorMin)
								{
									source = Math.max(source,getHighest(getOldLight(i+1,j+1,n),getOldLight(i-1,j-1,n),getOldLight(i+1,j-1,n),getOldLight(i-1,j+1,n),0.88f));
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
							cell = Map.instance.mainLayer.getBloc(i, j);
							if(AllBlocTypes.instance.getType(cell).lightFull)
							{
								
								if(Parameters.i.RGB)
								{
									float coll = AllBlocTypes.instance.getType(cell).lightColor[n];
									setLightIntern(i,j,Math.max((float) (coll+0.1f), getLight(i,j,n)),n);
								}
								else
								{
									float coll = Math.max(AllBlocTypes.instance.getType(cell).lightColor[0], Math.max(AllBlocTypes.instance.getType(cell).lightColor[1], AllBlocTypes.instance.getType(cell).lightColor[2]));
									setLightIntern(i,j,Math.max((float) (coll+0.1f), getLight(i,j,n)),n);
								}
							}
							//if(!Parameters.i.shader)setLight(i,j,getLightIntern(i,j,n),n);
							//setLight(i,j,getLightIntern(i,j,n),n);
						}
					}
				}
			}
			
			for(int i=minx+4;i<(int)maxx-4;i++)
			{
				for(int j=miny+4;j<(int)maxy-4;j++)
				{
					if(Parameters.i.RGB)
					{
						Vector3 temp = getLight(i,j);
						setOldLight(i,j,temp.x,temp.y,temp.z);
					}
					else
					{
						float temp = getLight(i,j,0);
						setOldLight(i,j,temp,0);
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
