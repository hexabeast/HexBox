
package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.Color;

public class Message {
	public String str;
	public String owner;
	public float alpha = 15;
	public Color col;
	
	public Message()
	{
		
	}
	
	public Message(String str, String owner, Color col)
	{
		this.str = str;
		this.owner = owner;
		this.col = col;
	}

}
