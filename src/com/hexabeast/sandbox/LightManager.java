package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManager {
	
	public LightCoord[][] lightArray;
	public LightCoord[][] dynlightArray;
	public LightCoord[][] oldLightArray;
	public List<Vector5> dynaList;
	public List<Vector5> staticdynaList;
	
	public boolean dynalight = true;
	
	float factorDirt = 0.8f;
	float factorAirLow = 0.95f;
	float factorMin = 0.02f;
	float speed = 1f/120;
	float speedTime = 10;
	int offsetX = 0;
	int offsetY = 0;
	int oldoffX = 0;
	int oldoffY = 0;
	boolean updated = false;
	public float daySwitch = 0f;
	int size = 240;
	int portey = 20;
	
	boolean isLosange = false;
	
	public Vector3 lightVec = new Vector3();
	
	public LightManager() {
		if(Main.mobile)speed = 1f/30;
		lightArray = new LightCoord[size][size];
		oldLightArray = new LightCoord[size][size];
		dynlightArray = new LightCoord[size][size];
		for(int i = 0; i<size; i++)
		{
			for( int j = 0; j<size; j++)
			{
				lightArray[i][j] = new LightCoord(0,0,0);
				oldLightArray[i][j] = new LightCoord(0,0,0);
				dynlightArray[i][j] = new LightCoord(0,0,0);
			}
		}
		dynaList = new ArrayList<Vector5>();
		staticdynaList = new ArrayList<Vector5>();
		
	}
	
	public Vector3 getLight(int x, int y)
	{
		x = x-offsetX;
		y = y-offsetY;
		if(x<size && y<size&& x>=0 && y>=0)
		{
			if(!Parameters.i.RGB)
			{
				if(!dynalight)
				{
					lightVec.x = lightArray[x][y].getX();
					lightVec.y = lightArray[x][y].getX();
					lightVec.z = lightArray[x][y].getX();
				}
				else
				{
					lightVec.x = dynlightArray[x][y].getX();
					lightVec.y = dynlightArray[x][y].getX();
					lightVec.z = dynlightArray[x][y].getX();
				}
			}
			else
			{
				if(!dynalight)
				{
					lightVec.x = lightArray[x][y].getX();
					lightVec.y = lightArray[x][y].getY();
					lightVec.z = lightArray[x][y].getZ();
				}
				else
				{
					lightVec.x = dynlightArray[x][y].getX();
					lightVec.y = dynlightArray[x][y].getY();
					lightVec.z = dynlightArray[x][y].getZ();
				}
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
		x = x-offsetX;
		y = y-offsetY;
		if(x<size && y<size&& x>=0 && y>=1)
		{
			return lightArray[x][y].c[i];
		}
		else return 0;
	}
	
	public Vector3 getLightIntern(int x, int y)
	{
		x = x-offsetX;
		y = y-offsetY;
		lightVec.x = lightArray[x][y].getX();
		lightVec.y = lightArray[x][y].getY();
		lightVec.z = lightArray[x][y].getZ();
		return lightVec;
	}
	
	public float getLightIntern(int x, int y, int i)
	{
		return lightArray[x-offsetX][y-offsetY].c[i];
	}
	
	public Vector3 getOldLight(int x, int y)
	{
		LightCoord temp = oldLightArray[x-offsetX][y-offsetY];
		lightVec.x = temp.getX();
		lightVec.y = temp.getY();
		lightVec.z = temp.getZ();
		return lightVec;
	}
	
	public float getOldLight(int x, int y, int i)
	{
		return oldLightArray[x-offsetX][y-offsetY].c[i];
	}
	
	public void setOldLight(int x, float z, float w, float v)
	{
		if(dynaList.get(x).x-offsetX<size && dynaList.get(x).y-offsetY<size&& dynaList.get(x).x-offsetX>=0 && dynaList.get(x).y-offsetY>=0)
		setOldLightDyn((int) dynaList.get(x).x,(int) dynaList.get(x).y,z,w,v);
	}
	
	public void setDynLight(int x, float z, float w, float v)
	{
		if(staticdynaList.get(x).x-offsetX<size && staticdynaList.get(x).y-offsetY<size&& staticdynaList.get(x).x-offsetX>=0 && staticdynaList.get(x).y-offsetY>=0)
		{
			if(Parameters.i.RGB)
			{
				setDynLight((int) staticdynaList.get(x).x,(int) staticdynaList.get(x).y,z,w,v);
			}
			else
			{
				setDynLight((int) staticdynaList.get(x).x,(int) staticdynaList.get(x).y,z,z,z);
			}
		}
		
	}
	
	public void setLight(int x, int y, float z, float w, float v)
	{
		lightArray[x-offsetX][y-offsetY].setX(z);
		lightArray[x-offsetX][y-offsetY].setY(w);
		lightArray[x-offsetX][y-offsetY].setZ(v);
	}
	
	public void setLight(int x, int y, float z, int i)
	{
		lightArray[x-offsetX][y-offsetY].c[i] = z;
	}
	
	public void setOldLight(int x,int y, float z, float w, float v)
	{
		oldLightArray[x-offsetX][y-offsetY].setX(z);
		oldLightArray[x-offsetX][y-offsetY].setY(w);
		oldLightArray[x-offsetX][y-offsetY].setZ(v);
	}
	
	public void setOldLightDyn(int x,int y, float z, float w, float v)
	{
		if(oldLightArray[x-offsetX][y-offsetY].getX()<z)oldLightArray[x-offsetX][y-offsetY].setX(z);
		if(oldLightArray[x-offsetX][y-offsetY].getY()<w)oldLightArray[x-offsetX][y-offsetY].setY(w);
		if(oldLightArray[x-offsetX][y-offsetY].getZ()<v)oldLightArray[x-offsetX][y-offsetY].setZ(v);
	}
	
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
	}
	
	public void setOldLight(int x,int y, float z, int i)
	{
		oldLightArray[x-offsetX][y-offsetY].c[i] = z;
	}
	
	public void UpdateLight()
	{
		speed = 1f/Parameters.i.lightSpeed;
		updateDay();
		Vector2 cellPos0 = Converter.screenToMapCoords(0,Gdx.graphics.getHeight());
		Vector2 cellPos1 = Converter.screenToMapCoords(Gdx.graphics.getWidth(),0);
		if(cellPos1.x-cellPos0.x<100)Update(cellPos0,cellPos1);
	}
	
	public void Update(Vector2 cellPos0,Vector2 cellPos1 )
	{	
		updated = true;
		speedTime+=Main.delta;

		int cur = 0;
		int max = 3;
		int limit = 0;
		if(GameScreen.noLimit)limit =1000; 
		
		while(speedTime>speed && cur<max)
		{
			cur++;
			
			offsetX = (int) (cellPos0.x-(size-(cellPos1.x-cellPos0.x))/2);
			offsetY = (int) (cellPos0.y-(size-(cellPos1.y-cellPos0.y))/2);

			if((cellPos1.x)-offsetX+25>size)offsetX = 0;
			if((cellPos1.y)-offsetY+25>size)offsetY = 0;
			int decalX = offsetX-oldoffX;
			int decalY = offsetY-oldoffY;
			if(Math.abs(decalX - Map.instance.width)<5)decalX-=Map.instance.width;
			if(Math.abs(decalX + Map.instance.width)<5)decalX+=Map.instance.width;
			
			speedTime-=speed;
			int offset = portey+2;
			if(decalX!= 0 || decalY!=0)offset = 30;
			for(int i=(int)cellPos0.x-offset;i<(int)cellPos1.x+offset;i++)
			{
				for(int j=(int)cellPos0.y-offset;j<(int)cellPos1.y+offset;j++)
				{
					if(i>=0-limit && j>=1 && i<Map.instance.mainLayer.width+limit && j<Map.instance.mainLayer.height-2)
					{
						
						if(Parameters.i.RGB)
						{
							if(i+decalX<offsetX+size && i+decalX>offsetX && j+decalY<offsetY+size && j+decalY>offsetY)
							{
								Vector3 temp = getLightIntern(i+decalX,j+decalY);
								setOldLight(i,j,temp.x,temp.y,temp.z);
							}
							else setOldLight(i,j,0,0,0);
						}
						else
						{
							if(i+decalX<offsetX+size && i+decalX>offsetX && j+decalY<offsetY+size && j+decalY>offsetY)
							{
								float temp = getLightIntern(i+decalX,j+decalY,0);
								setOldLight(i,j,temp,0);
							}
							else setOldLight(i,j,0,0);
						}
						
					}
				}
			}
			
			for(int m = 0; m<dynaList.size(); m++)
			{
				setOldLight(m,dynaList.get(m).z,dynaList.get(m).w,dynaList.get(m).v);
			}
			
			if(decalX!= 0 || decalY!=0)
			{
				for(int i=(int)cellPos0.x-30;i<(int)cellPos1.x+30;i++)
				{
					for(int j=(int)cellPos0.y-30;j<(int)cellPos1.y+30;j++)
					{
						if(i>=0-limit && j>=1 && i<Map.instance.mainLayer.width+limit && j<Map.instance.mainLayer.height-2)
						{
							if(i+decalX<offsetX+size && i+decalX>offsetX && j+decalY<offsetY+size && j+decalY>offsetY)
							{
								if(Parameters.i.RGB)
								{
									Vector3 temp = getOldLight(i,j);
									setLight(i,j,temp.x,temp.y,temp.z);
								}
								else
								{
									float temp = getOldLight(i,j,0);
									setLight(i,j,temp,0);
								}
							}
							
							else setLight(i,j,0,0,0);
						}
					}
				}
			}
			
			
			
			int colors = 3;
			if(!Parameters.i.RGB)colors = 1;
			for(int n = 0; n<colors;n++)
			{
				for(int i=(int)cellPos0.x-portey;i<(int)cellPos1.x+portey;i++)
				{
					for(int j=(int)cellPos0.y-portey;j<(int)cellPos1.y+portey;j++)
					{
						if(i>=-limit && j>1 && i<Map.instance.mainLayer.width+limit && j<Map.instance.mainLayer.height-2)
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
									setLight(i,j,source*factorAirLow,n);
								}
								else
								{
									setLight(i,j,0,n);
								}
								
								if(j>Map.instance.limit && AllBlocTypes.instance.IsTransparent(cell) && getLightIntern(i,j,n)<Parameters.i.daylight)
								{
									setLight(i,j,Parameters.i.daylight,n);
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
			
			
			oldoffX = offsetX;
			oldoffY = offsetY;
		}
		
		
		if(staticdynaList.size()>0)dynalight = true;
		else dynalight = false;
		
		if(dynalight && cur>0)
		{
			for(int i = 0; i<lightArray.length; i++)
			{
				for(int j = 0; j<lightArray[0].length; j++)
				{
					dynlightArray[i][j].setX(lightArray[i][j].getX());
					dynlightArray[i][j].setY(lightArray[i][j].getY());
					dynlightArray[i][j].setZ(lightArray[i][j].getZ());
				}
			}
			
			for(int m = 0; m<staticdynaList.size(); m++)
			{
				setDynLight(m,staticdynaList.get(m).z,staticdynaList.get(m).w,staticdynaList.get(m).v);
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
