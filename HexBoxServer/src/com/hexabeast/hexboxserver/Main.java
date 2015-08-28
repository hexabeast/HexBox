package com.hexabeast.hexboxserver;

import java.util.Scanner;

public class Main {
	
	public static ServerMap map;
	public static HServer server;
	static boolean terminated = false;
 
	public static void main (String args[]) 
	{
		final Scanner scan = new Scanner(System.in);
		boolean exit = false;

		map = new ServerMap("calmap2");
		
		System.out.println("Enter the port (default = 42245)");
		String s = scan.nextLine();
		HServer.port = Integer.valueOf(s).intValue();
		
		server = new HServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread() 
		{
            public void run() 
            {
            	scan.close();
        		
            	if(!terminated)
            	{
            		System.out.println("Saving map...");
            		map.saveMap();
            		System.out.println("Map saved!...");
            		
            		System.out.println("Stopping server...");
            		server.server.stop();
            		System.out.println("Server stopped!...");
            	}
            }
        });
		
		
		while(!exit)
		{
			String str = scan.nextLine();
			
			if(str.equals("exit") || str.equals("quit") || str.equals("stop"))exit = true;
		}
		
		scan.close();
		
		System.out.println("Saving map...");
		map.saveMap();
		System.out.println("Map saved!...");
		
		System.out.println("Shutting down server...");
		server.stop();
		System.out.println("Server down!");
		
		terminated = true;
		
		System.exit(0);
	}
	

}
