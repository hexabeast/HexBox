package com.hexabeast.sandbox;

import com.badlogic.gdx.Input;

public class HKeys {
	public static final int keyboardNumber = 2;
	
	public static final int QWERTY = 0;
	public static final int AZERTY = 1;
	
	public static int A;
	public static int W;
	public static int Z;
	public static int Q;
	public static int M;
	
	public static void setKeyboard(int k)
	{
		if(k==QWERTY)
		{
			A = Input.Keys.A;
			W = Input.Keys.W;
			Z = Input.Keys.Z;
			Q = Input.Keys.Q;
			M = Input.Keys.M;
		}
		else if(k==AZERTY)
		{
			A = Input.Keys.Q;
			W = Input.Keys.Z;
			Z = Input.Keys.W;
			Q = Input.Keys.A;
			M = Input.Keys.SEMICOLON;
		}
	}
	
}
