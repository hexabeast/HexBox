package com.hexabeast.sandbox;

public class LightCoord {
	float[] c = new float[3];
	public LightCoord(float x, float y, float z)
	{
		setX(x);
		setY(y);
		setZ(z);
	}
	float getX(){return c[0];}
	float getY(){return c[1];}
	float getZ(){return c[2];}
	
	void setX(float f){c[0] = f;}
	void setY(float f){c[1] = f;}
	void setZ(float f){c[2] = f;}
}
