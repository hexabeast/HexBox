package com.hexabeast.hexboxserver;

public class Main {
	
	public static ServerMap map;
 
	public static void main (String args[]) 
	{

		map = new ServerMap("calmap2");	
		
		HServer.instance = new HServer();
		
		
		
	}
	

}
