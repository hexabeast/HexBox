package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManagerOld {
	
	public LightCoord[][] lightArray;
	public LightCoord[][] oldLightArray;
	public List<Vector5> dynaList;
	public List<Vector5> staticdynaList;
	
	float factorDirt = 0.8f;
	float factorAirLow = 0.95f;
	float factorMin = 0.02f;
	float speed = 1f/120;
	float speedTime = 10;
	boolean updated = false;
	public float daySwitch = 0f;
	
	boolean isLosange = false;
	
	public Vector3 lightVec = new Vector3();
	
	public LightManagerOld() 
	{
		if(Main.mobile)speed = 1f/30;
		lightArray = new LightCoord[Map.instance.width][Map.instance.height];
		oldLightArray = new LightCoord[Map.instance.width][Map.instance.height];

		for(int i = 0; i<Map.instance.width; i++)
		{
			for( int j = 0; j<Map.instance.height; j++)
			{
						lightArray[i][j] = new LightCoord(0,0,0);
						oldLightArray[i][j] = new LightCoord(0,0,0);
			}
		}
		dynaList = new ArrayList<Vector5>();
		staticdynaList = new ArrayList<Vector5>();
	}
	
	public Vector3 getLight(int x, int y)
	{		
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
			
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
	
	
	public float getLight(int x, int y, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
		return lightArray[x][y].c[i];
	}
	
	public float getLightIntern(int x, int y, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
		return lightArray[x][y].c[i];
	}
	
	public Vector3 getLightIntern(int x, int y)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		LightCoord temp = lightArray[x][y];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		
		return lightVec;
	}
	
	public Vector3 getOldLight(int x, int y)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		LightCoord temp = oldLightArray[x][y];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		
		return lightVec;
	}
	
	public float getOldLight(int x, int y, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		return oldLightArray[x][y].c[i];
	}
	
	public void setOldLight(int x, float z, float w, float v)
	{

		if(dynaList.get(x).x>=Map.instance.width)dynaList.get(x).x = dynaList.get(x).x-Map.instance.width;
		if(dynaList.get(x).x<0)dynaList.get(x).x = dynaList.get(x).x+Map.instance.width;
		
		if(dynaList.get(x).y>=Map.instance.width)dynaList.get(x).y = dynaList.get(x).y-Map.instance.height;
		if(dynaList.get(x).y<0)dynaList.get(x).y = dynaList.get(x).y+Map.instance.height;
	
		setOldLightDyn(dynaList.get(x).x,dynaList.get(x).y,z,w,v);
	}
	
	public void setDynLight(int x, int y, float z, float w, float v)
	{
		
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		if(lightArray[x][y].c[0]<z)lightArray[x][y].c[0] = z;
		if(lightArray[x][y].c[1]<w)lightArray[x][y].c[1] = w;
		if(lightArray[x][y].c[2]<v)lightArray[x][y].c[2] = v;
	}
	
	public void setLight(int x, int y, float z, float w, float v)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		lightArray[x][y].setX(z);
		lightArray[x][y].setY(w);
		lightArray[x][y].setZ(v);
	}
	
	public void setLight(int x, int y, float z, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
		

		lightArray[x][y].c[i] = z;
	}
	
	public void setLightIntern(int x, int y, float z, int i)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;

		lightArray[x][y].c[i] = z;
	}
	
	public void setOldLight(int x,int y, float z, float w, float v)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
		
		oldLightArray[x][y].setX(z);
		oldLightArray[x][y].setY(w);
		oldLightArray[x][y].setZ(v);
	}
	
	public void setOldLightDyn(int x,int y, float z, float w, float v)
	{
		if(oldLightArray[x][y].getX()<z)oldLightArray[x][y].setX(z);
		if(oldLightArray[x][y].getY()<w)oldLightArray[x][y].setY(w);
		if(oldLightArray[x][y].getZ()<v)oldLightArray[x][y].setZ(v);
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
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.width)y = y-Map.instance.height;
		if(y<0)y = y+Map.instance.height;
		
		oldLightArray[x][y].c[i] = z;
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
							BlocType cell = Map.instance.mainLayer.getBloc(i, j);
							
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
										setLight(i,j,source*(factorAirLow-0.3f),n);
									}
									else
									{
										setLight(i,j,0,n);
									}						
								}
								else
								{
									setLight(i,j,0,n);
								}
							}
							cell = Map.instance.mainLayer.getBloc(i, j);
							if(cell.lightFull)
							{
								
								if(Parameters.i.RGB)
								{
									float coll = cell.lightColor[n];
									setLight(i,j,Math.max((float) (coll+0.1f), getLightIntern(i,j,n)),n);
								}
								else
								{
									float coll = Math.max(cell.lightColor[0], Math.max(cell.lightColor[1], cell.lightColor[2]));
									setLight(i,j,Math.max((float) (coll+0.1f), getLightIntern(i,j,n)),n);
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
