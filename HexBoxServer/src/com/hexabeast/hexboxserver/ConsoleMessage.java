package com.hexabeast.hexboxserver;

public class ConsoleMessage {

	public String str;
	public int id;
	public int type;
	
	public static final int DISCONNECTED = 0;
	public static final int CONNECTED = 0;
	
	public ConsoleMessage()
	{
		
	}
	
	public ConsoleMessage(int t, String str)
	{
		this.type = t;
		this.str = str;
	}
}
