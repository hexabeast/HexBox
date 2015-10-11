package com.hexabeast.hexboxserver;

public class HMessage {
	
	public int id;
	public String str;
	public String owner;

	public HMessage()
	{
		
	}
	
	public HMessage(String str, String owner)
	{
		this.str = str;
		this.owner = owner;
	}
}
