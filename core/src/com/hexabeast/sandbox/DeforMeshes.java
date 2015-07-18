package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DeforMeshes {
	
	static DeforMeshes instance;
	
	Mesh lastmesh;
	
	public int patternCols = 10;
	public int patternRows = 10;
	
	float impuwidth = 100;
	float impuspeed = 600;
	
	float diffusetime = 3/4f; 
	float diffusetime2;
	
	public ArrayList<Vec7> implist;
	
	int col;
	int rows;
	
	float coef;
	
	float[][] pattern = new float[patternCols][patternRows];
	
	float[] texCoordx;
	float[] texCoordy;
	float[][] pCoordx;
	float[][] pCoordy;
	
	float[][] ppCoordx;
	float[][] ppCoordy;
	
	float[] verts;
	
	boolean ready;
	boolean lastcheck = false;
	
	public DeforMeshes(int col, int rows)
	{
		diffusetime2 = (1/diffusetime)*(1-diffusetime); 
		
		implist = new ArrayList<Vec7>();
		this.col = col;
		this.rows = rows;
		
		texCoordx = new float[col+1];
		texCoordy = new float[rows+1];
		
		ppCoordx = new float[col+1][rows+1];
		ppCoordy = new float[col+1][rows+1];
		
		pCoordx = new float[col+1][rows+1];
		pCoordy = new float[col+1][rows+1];
		
		ready = false;
		
		verts = new float[30*rows*col];
		
		ComputeFloats();
	}
	
	public void ComputeFloats()
	{
		int totwidth = Main.windowWidth;
		int totheight = Main.windowHeight;
		
		coef = (float)totwidth/1280;
		if(!Parameters.i.zoomLock)coef = 1;
		  
		float width = (float)Main.windowWidth/col;
		float height = (float)Main.windowHeight/rows;
		
		
		for(int j = 0; j<col+1; j++)
		{
			for(int k = 0; k<rows+1; k++)
			{
				pCoordx[j][k] =  width*j;
			}
			texCoordx[j] = (width*j)/totwidth;
		}
		  
		for(int k = 0; k<rows+1; k++)
		{
			for(int j = 0; j<col+1; j++)
			{
				pCoordy[j][k] =  height*k;
			}
			texCoordy[k] = (height*k)/totheight;
		}
		  
		for(int j = 0; j<col+1; j++)
		{
			  pCoordy[j][0] = 0;
			  pCoordy[j][rows] = totheight;
		}
		  
		for(int k = 0; k<rows+1; k++)
		{
			  pCoordx[0][k] = 0;
			  pCoordx[col][k] = totwidth;
		}
		ready = false;
	}
	
	public void addImpulsion(float x, float y, float max, float speed, float intensity)
	{
		implist.add(new Vec7(x,y,0,impuwidth,speed*impuspeed,max, intensity));
	}
	
	public void createFullScreenQuad() 
	{
		
		if(implist.size() > 0 || !ready || !lastcheck)
		{
			if(implist.size() == 0)lastcheck = true;
			else lastcheck = false;
			
			ready = true;
			
			int i = 0;
			
			float[][] ppCoordx = new float[col+1][rows+1];
			float[][] ppCoordy = new float[col+1][rows+1];
			
			for(int j = 0; j<col+1; j++)
			{
				for(int k = 0; k<rows+1; k++)
				{
					ppCoordx[j][k] = this.pCoordx[j][k];
					ppCoordy[j][k] = this.pCoordy[j][k];
				}
			}
			
			Vector2 vec = new Vector2();
			for(int l = implist.size()-1; l>=0; l--)
			{
				float intensity = implist.get(l).e;
				implist.get(l).a += implist.get(l).c*Main.delta;
				float dist;
				
				Vector3 vect = GameScreen.camera.project(new Vector3(implist.get(l).x,implist.get(l).y,0));
				
				for(int j = 1; j<col; j++)
				{
					for(int k = 1; k<rows; k++)
					{
						vec.x =(ppCoordx[j][k]-vect.x);
						vec.y =(ppCoordy[j][k]-vect.y);
						
						dist = Math.max(0, coef*implist.get(l).b-Math.abs(coef*implist.get(l).a-vec.len()))/(coef*implist.get(l).b);
						
						float tier = coef*implist.get(l).d*diffusetime;
						
						if(coef*implist.get(l).a>tier*diffusetime2)
						{
							dist *= 1-(coef*implist.get(l).a-tier*diffusetime2)/tier;
						}
						if(coef*implist.get(l).a<40)dist *= coef*implist.get(l).a/40;
						
						
						vec.setLength(coef*implist.get(l).a);
						
						ppCoordx[j][k] = ppCoordx[j][k]*2-(ppCoordx[j][k]+(vect.x+vec.x)*dist*intensity)/(1+dist*intensity);
						ppCoordy[j][k] = ppCoordy[j][k]*2-(ppCoordy[j][k]+(vect.y+vec.y)*dist*intensity)/(1+dist*intensity);
					}
				}
				if(coef*implist.get(l).a>coef*implist.get(l).d)implist.remove(l);
			}
			
			
			for(int j = 0; j<col; j++)
			{
				for(int k = 0; k<rows; k++)
				{
					verts[i++] = ppCoordx[j][k]; // x1
					verts[i++] = ppCoordy[j][k]; // y1
					verts[i++] = 0;
					verts[i++] = texCoordx[j]; // u1
					verts[i++] = texCoordy[k]; // v1
					
					verts[i++] = ppCoordx[j+1][k]; // x2
					verts[i++] = ppCoordy[j+1][k]; // y2
					verts[i++] = 0;
					verts[i++] = texCoordx[j+1]; // u2
					verts[i++] = texCoordy[k]; // v2
					
					verts[i++] = ppCoordx[j+1][k+1]; // x3
					verts[i++] = ppCoordy[j+1][k+1]; // y2
					verts[i++] = 0;
					verts[i++] = texCoordx[j+1]; // u3
					verts[i++] = texCoordy[k+1]; // v3
					
					verts[i++] = ppCoordx[j][k+1]; // x4
					verts[i++] = ppCoordy[j][k+1]; // y4
					verts[i++] = 0;
					verts[i++] = texCoordx[j]; // u4
					verts[i++] = texCoordy[k+1]; // v4
					  
					verts[i++] = ppCoordx[j][k]; // x2
					verts[i++] = ppCoordy[j][k]; // y2
					verts[i++] = 0;
					verts[i++] = texCoordx[j]; // u2
					verts[i++] = texCoordy[k]; // v2
					
					verts[i++] = ppCoordx[j+1][k+1]; // x3
					verts[i++] = ppCoordy[j+1][k+1]; // y2
					verts[i++] = 0;
					verts[i++] = texCoordx[j+1]; // u3
					verts[i++] = texCoordy[k+1]; // v3
				  }
			}
			  
			
			Mesh mesh = new Mesh( true, 6*rows*col, 0,
					    new VertexAttribute( Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
					    new VertexAttribute( Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
			
			mesh.setVertices( verts );
			
			if(Main.mesh!=null)Main.mesh.dispose();
			
			Main.mesh = mesh;
		}
	}
}
