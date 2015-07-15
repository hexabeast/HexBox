package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManager {
	
	public LightCoord[][] lightArray;

	public LightCoord[][] lightChanges;
	
	public List<Vector5Light> dynaList;
	public List<Vector5Light> staticdynaList;
	public List<Vector5Light> staticremoveList;
	
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
	boolean STRANGELIGHTS = false;
	
	int aj;
	int ai;
	
	int chunkNumberX;
	int chunkNumberY;
	
	int minx;
	int miny;
	int maxx;
	int maxy;
	
	public Vector3 lightVec = new Vector3();
	
	public LightManager() 
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
		dynaList = new ArrayList<Vector5Light>();
		staticdynaList = new ArrayList<Vector5Light>();
		staticremoveList = new ArrayList<Vector5Light>();
	}
	
	public Vector3 getLight(int x, int y)
	{		
		if(x>=minx && x<maxx && y>=miny && y<maxy)
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
		}
		else
		{
			lightVec.x = 0;
			lightVec.y = 0;
			lightVec.z = 0;
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
	
	public void setLightDyn(int m)
	{
		int x = dynaList.get(m).x;
		int y = dynaList.get(m).y;
		
		if(x>=minx && x<maxx && y>=miny && y<maxy)
		{
			
	
			if(x>=Map.instance.width)x = x-Map.instance.width;
			if(x<0)x = x+Map.instance.width;
			
			if(y>=Map.instance.height)y = Map.instance.height-1;
			if(y<0)y = 0;
			
			if(lightArray[x][y].getX()<dynaList.get(m).z)lightArray[x][y].setX(dynaList.get(m).z);
			if(lightArray[x][y].getY()<dynaList.get(m).w)lightArray[x][y].setY(dynaList.get(m).w);
			if(lightArray[x][y].getZ()<dynaList.get(m).v)lightArray[x][y].setZ(dynaList.get(m).v);		
		}
	}
	
	public void setStaticLight(int x, int y, float z, float w, float v, int m)
	{
		if(lightArray[x][y].c[0]<z)lightArray[x][y].c[0] = z;
		if(lightArray[x][y].c[1]<w)lightArray[x][y].c[1] = w;
		if(lightArray[x][y].c[2]<v)lightArray[x][y].c[2] = v;
	}
	
	
	public void setStaticRemove(int x, int y)
	{
		staticremoveList.add(new Vector5Light(x,y,lightArray[x][y].c[0],lightArray[x][y].c[1],lightArray[x][y].c[2]));
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
	
	public void setLightNoSafe(int x, int y, float z, float w, float v)
	{
		lightArray[x][y].setX(z);
		lightArray[x][y].setY(w);
		lightArray[x][y].setZ(v);
	}
	
	public void setLightIntern(int x, int y, float z, int i)
	{
		lightChanges[x][y].c[i] = z;
	}
	
	
	
	public void UpdateLight()
	{
		speed = 1f/Parameters.i.lightSpeed;
		updateDay();
		Vector2 cellPos0 = Converter.screenToMapCoords(0,Gdx.graphics.getHeight());
		Vector2 cellPos1 = Converter.screenToMapCoords(Gdx.graphics.getWidth(),0);
		
		minx = (int)cellPos0.x-Constants.lightDistances[Parameters.i.lightDistance];
		miny = (int)cellPos0.y-Constants.lightDistances[Parameters.i.lightDistance];
				
		maxx = (int)cellPos1.x+Constants.lightDistances[Parameters.i.lightDistance]+1;
		maxy = (int)cellPos1.y+Constants.lightDistances[Parameters.i.lightDistance]+1;
		
		
		if(cellPos1.x-cellPos0.x<90)Update();
	}
	
	public boolean activeChunksContains(int x, int y)
	{
		for(int i = 0; i<activeChunks.size(); i++)
		{
			if(activeChunks.get(i).x == x && activeChunks.get(i).y == y)return true;
		}
		return false;
	}
	
	public void Update()
	{	
		updated = true;
		speedTime+=Main.delta;

		int cur = 0;
		int max = 3;
		
		if(maxy>Map.instance.height-1)maxy = Map.instance.height-1;
		if(miny<0)miny=0;
		
		int cw = (int)(maxx-minx);
		int ch = (int)(maxy-miny);
		
		if(speedTime>speed)
		{
			for(int i = 0; i<staticremoveList.size(); i++)
			{
				setLightNoSafe(staticremoveList.get(i).x,staticremoveList.get(i).y,staticremoveList.get(i).z,staticremoveList.get(i).w,staticremoveList.get(i).v);
			}
			staticremoveList = new ArrayList<Vector5Light>();
		}
		
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
								setLightIntern(i,j,Math.max((float) (coll+0.1f), getLightChange(i,j,n)),n);
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
				setLightDyn(m);
			}
		}
		
		if(cur>0)
		{
			
			for(int m = 0; m<staticdynaList.size(); m++)setStaticRemove(staticdynaList.get(m).x,staticdynaList.get(m).y);
			for(int m = 0; m<staticdynaList.size(); m++)setStaticLight(staticdynaList.get(m).x,staticdynaList.get(m).y,staticdynaList.get(m).z,staticdynaList.get(m).w,staticdynaList.get(m).v, m);
			staticdynaList = new ArrayList<Vector5Light>();
			dynaList = new ArrayList<Vector5Light>();
			speedTime = 0;
		}
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
		
		if(Parameters.i.RGB)dynaList.add(new Vector5Light(x2,y2,ix, iy, iz));
		else
		{
			float max = (Math.max(ix, Math.max(iy, iz))+ix+iy+iz)/4;
			dynaList.add(new Vector5Light(x2,y2,max, max, max));
		}
	}
	
	public void tempLighti(int x, int y, float ix, float iy, float iz)
	{
		
		if(Parameters.i.RGB)dynaList.add(new Vector5Light(x,y,ix, iy, iz));
		else
		{
			float max = (Math.max(ix, Math.max(iy, iz))+ix+iy+iz)/4;
			dynaList.add(new Vector5Light(x,y,max, max, max));
		}
	}
	
	
	public void staticTempLight(int x, int y, float ix, float iy, float iz)
	{
		if(x>=Map.instance.width)x = x-Map.instance.width;
		if(x<0)x = x+Map.instance.width;
		
		if(y>=Map.instance.height)y = Map.instance.height-1;
		if(y<0)y = 0;
		
		staticdynaList.add(new Vector5Light(x,y,ix, iy, iz));
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
	
	
	public void torche(Vector2 basePos, Vector2 velo, int angle, float r, float g, float b, boolean base, float basecolor)
	{
		int precision = 1;
		float light = 1;
		velo.setLength(800);
		velo.rotate(-angle);
		angle*=precision;
		Vector2 velo2 = new Vector2(velo);
		
		if(!base)tempLight(basePos.x, basePos.y, light/4*r, light/4*g, light/4*b);
		else tempLight(basePos.x, basePos.y, light/4*basecolor, light/4*basecolor, light/4*basecolor);
		
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
						staticTempLight(x, y, light*r, light*g, light*b);
						if(i == 0 || i == angle/4 || i == -angle/4 || i == angle/2 || i == -angle/2 || i == angle/3*2 || i == -angle/3*2)
						{
							if(!base)tempLighti(x, y, light/3*r, light/3*g, light/3*b);
							else tempLighti(x, y, light/3*basecolor, light/3*basecolor, light/3*basecolor);
						}
							
						

						if(!Map.instance.mainLayer.getBloc(x, y).transparent)
						{
							light*=0.7f;
							if(oldx!=x && oldy!=y)
							{
								light*=0.7f;
							}
							if(!base)tempLighti(x, y, light/2*r, light/2*g, light/2*b);
							else tempLighti(x, y, light/2*basecolor, light/2*basecolor, light/2*basecolor);
							
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
		staticTempLight(x, y, light*r, light*g, light*b);
		if(i == 0 || i == angle/4 || i == -angle/4 || i == angle/2 || i == -angle/2 || i == angle/3*2 || i == -angle/3*2)
		{
			if(!base)tempLighti(x, y, light/3*r, light/3*g, light/3*b);
			else tempLighti(x, y, light/4*basecolor, light/4*basecolor, light/4*basecolor);
		}
		

		if(!Map.instance.mainLayer.getBloc(x, y).transparent)
		{
			light*=0.7f;
			
		}

		light-=1f/100f;
		
		return light;
	}

}
