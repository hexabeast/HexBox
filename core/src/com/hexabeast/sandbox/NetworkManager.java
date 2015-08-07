package com.hexabeast.sandbox;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.hexabeast.hexboxserver.HServer;

public class NetworkManager {
	
	public static NetworkManager instance;

	public Client client;
	
	public NetworkRequestList modifications;
	
	String name = "John";
	
	boolean online = false;
	
	public Timer playerTimer;
	public Timer playerUpdateTimer;
	
	public NetworkManager()
	{
		instance = this;
		modifications = new NetworkRequestList();
		client = new Client();
		HServer.initKryoClasses(client.getKryo());
	    
	}
	
	public void addListener()
	{
		client.addListener(new Listener() 
		{
	       public void received (Connection c, Object object) 
	       {
	    	   modifications.add(object);
	       }
	       
	       public void connected(Connection c)
	       {
	    	   online = true;
	    	   System.out.println("online");
           }
           public void disconnected(Connection c)
           {
        	   online = false;
           }
           
		});
	}
	
	public void connectLocal()
	{
		connect("127.0.0.1");
	}
	
	public void connect(String IP)
	{
		client.start();
	    try {
			client.connect(5000, IP, 54321, 55321);
			
			addListener();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendTCP(Object object)
	{
		client.sendTCP(object);
	}
	
	public void sendUDP(Object object)
	{
		client.sendUDP(object);
	}
	
	public void update()
	{
		
	}
	
}
