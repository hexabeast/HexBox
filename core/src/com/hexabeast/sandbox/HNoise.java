package com.hexabeast.sandbox;

import java.util.Random;

public class HNoise {
public int seed = 0;
public Random rand;

public HNoise(int seed)
{
	rand = new Random(seed);
}

public void setSeed(int seed)
{
	rand.setSeed(seed);
}

public float[][] generateBasic2(int w, int h, int dx, int dy)
{
	float[][] gen = new float[w][h];
	
	float[][] smallgen = new float[w/dx+1][h/dy+1];
	
	for(int i = 0; i<smallgen.length; i++)
	{
		for(int j = 0; j<smallgen[0].length; j++)
		{
			smallgen[i][j] = rand.nextFloat()*2-1;
		}
	}
	
	for(int i = 0; i<gen.length; i++)
	{
		for(int j = 0; j<gen[0].length; j++)
		{
			int ci = (int)(i/dx);
			int ni = (int)(i/dx)+1;
			
			int cj = (int)(j/dy);
			int nj = (int)(j/dy)+1;
			
			float xforce = 1-((float)(i%dx)/(float)dx);
			float yforce = 1-((float)(j%dy)/(float)dy);
			
			xforce = curve(xforce);
			yforce = curve(yforce);
			
			gen[i][j] =  smallgen[ci][cj] * ((xforce*yforce))
			+ smallgen[ni][cj] * (((1-xforce)*yforce))
			+ smallgen[ci][nj] * ((xforce*(1-yforce)))
			+ smallgen[ni][nj] * (((1-xforce)*(1-yforce)));
			//System.out.printf(String.valueOf(gen[i][j]).substring(0, 4)+"/");
		}
		//System.out.println();
	}
	
	return gen;
}

public float[][] generateBasic(int w, int h, int dx, int dy)
{
	float[][] gen = new float[w][h];
	
	float[][] smallgen = new float[w/dx+1][h/dy+1];
	
	for(int i = 0; i<smallgen.length; i++)
	{
		for(int j = 0; j<smallgen[0].length; j++)
		{
			smallgen[i][j] = rand.nextFloat()*2-1;
		}
	}
	
	for(int i = 0; i<gen.length; i++)
	{
		for(int j = 0; j<gen[0].length; j++)
		{
			int ci = (int)(i/dx);
			int ni = (int)(i/dx)+1;
			
			int cj = (int)(j/dy);
			int nj = (int)(j/dy)+1;
			
			float xforce = ((float)(i%dx)/(float)dx);
			float yforce = ((float)(j%dy)/(float)dy);
			
			xforce = curve(xforce);
			yforce = curve(yforce);
			
			float ax = smallgen[ci][cj]*(1-xforce) + smallgen[ni][cj]*(xforce);
			float bx = smallgen[ci][nj]*(1-xforce) + smallgen[ni][nj]*(xforce);
			
			gen[i][j] =  ax*(1-yforce)+bx*(yforce);
			//System.out.printf(String.valueOf(gen[i][j]).substring(0, 4)+"/");
		}
		//System.out.println();
	}
	
	return gen;
}

public float[][] generateGradient(int w, int h, int dx, int dy)
{
	return generateGradient(w, h, dx, dy, 0.5f, 1);
}

public float[][] generateGradient(int w, int h, int dx, int dy, float amp, int octaves)
{
	float[][] gen = new float[w][h];
	
	
	
	float amplitude = 1;
	
	for(int k = 0; k<octaves; k++)
	{
		float[][] smallgenx = new float[w/dx+2][h/dy+2];
		float[][] smallgeny = new float[w/dx+2][h/dy+2];
		for(int i = 0; i<smallgenx.length-2; i++)
		{
			for(int j = 0; j<smallgenx[0].length; j++)
			{
				smallgenx[i][j] = rand.nextFloat();
				smallgeny[i][j] = /*rand.nextFloat();*/(float) Math.sqrt(1-smallgenx[i][j]*smallgenx[i][j]);
				if(rand.nextBoolean())smallgeny[i][j] = -smallgeny[i][j];
				if(rand.nextBoolean())smallgenx[i][j] = -smallgenx[i][j];
				
			}
		}
		for(int i = smallgenx.length-2; i<smallgenx.length; i++)
		{
			for(int j = 0; j<smallgenx[0].length; j++)
			{
				smallgeny[i][j] = smallgeny[0][j];
				smallgenx[i][j] = smallgeny[0][j];
			}
		}
		
		for(int i = 0; i<gen.length; i++)
		{
			for(int j = 0; j<gen[0].length; j++)
			{		
				int ci = (int)(i/dx);
				int ni = (int)(i/dx)+1;
				
				int cj = (int)(j/dy);
				int nj = (int)(j/dy)+1;
				
				float s = smallgenx[ci][cj] * ((i) - (ci*dx))/dx + smallgeny[ci][cj] * ((j) - (cj*dy))/dy;
				
				float t = smallgenx[ni][cj] * ((i) - (ni*dx))/dx + smallgeny[ni][cj] * ((j) - (cj*dy))/dy;

				float u = smallgenx[ci][nj] * ((i) - (ci*dx))/dx + smallgeny[ci][nj] * ((j) - (nj*dy))/dy;
				
				float v = smallgenx[ni][nj] * ((i) - (ni*dx))/dx + smallgeny[ni][nj] * ((j) - (nj*dy))/dy;
				
				float xforce = ((float)(i%dx)/(float)dx);
				float yforce = ((float)(j%dy)/(float)dy);
				
				xforce = curve(xforce);
				yforce = curve(yforce);
				
				float ax = s*(1-xforce) + t*(xforce);
				float bx = u*(1-xforce) + v*(xforce);
				
				gen[i][j] +=  (ax*(1-yforce)+bx*(yforce))*amplitude;
				
				//else gen[i][j] =  (((ax*(1-yforce)+bx*(yforce))*amplitude/(i))+gen[i][j])/(1+(1f/(i)));
				//System.out.printf(String.valueOf(gen[i][j]+0.0000000011).substring(0, 4)+"/");
			}
			//System.out.println();
		}
		dx/=2;
		dy/=2;
		amplitude*=amp;
	}
	
	
	return gen;
}

public float[] generate1DGradient(int w, int dx)
{
float[] gen = new float[w];
	
	float[] smallgenx = new float[w/dx+2];
	
	for(int i = 0; i<smallgenx.length-2; i++)
	{
		smallgenx[i] = rand.nextFloat();
		if(rand.nextBoolean())smallgenx[i] = -smallgenx[i];
	}
	smallgenx[smallgenx.length-2] = smallgenx[0];
	smallgenx[smallgenx.length-1] = smallgenx[0];
	
	for(int i = 0; i<gen.length; i++)
	{
		int ci = (int)(i/dx);
		int ni = (int)(i/dx)+1;
		
		float s = smallgenx[ci] * ((i) - (ci*dx))/dx;
		
		float t = smallgenx[ni] * ((i) - (ni*dx))/dx;

		float xforce = ((float)(i%dx)/(float)dx);
		
		xforce = curve(xforce);

		gen[i] = s*(1-xforce) + t*(xforce);
		
		//System.out.printf(String.valueOf(gen[i][j]+0.0000000011).substring(0, 4)+"/");
	}
	
	return gen;
}

public float curve(float x)
{
	return -2*x*x*x+3*x*x;
}

}
