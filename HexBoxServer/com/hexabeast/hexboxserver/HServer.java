package com.hexabeast.hexboxserver;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class HServer {
	
	static HServer instance;
	public Server server;
	
	//public ArrayList<Integer> ids;
	
	public HServer()
	{
		//ids = new ArrayList<Integer>();
		
		server = new Server();
		
		initKryoClasses(server.getKryo());
		
		server.addListener(new Listener() 
		{
	       public void received (Connection c, Object object) 
	       {
	          if (object instanceof NBlockModification) 
	          {
	             //c.sendTCP(object);
	             server.sendToAllTCP(object);
	          }
	          
	          if (object instanceof NPlayer)
	          {
	        	 ((NPlayer)object).id = c.getID();
	        	 server.sendToAllTCP(object);
	          }
	          
	          if (object instanceof NPlayerUpdate)
	          {
	        	  ((NPlayerUpdate)object).id = c.getID();
	        	 server.sendToAllUDP(object);
	          }
	       }
	       public void connected(Connection c)
	       {
	    	   
           }
	       public void disconnected (Connection c) 
	       {
	    	   
	       }
		});
		
		try 
		{
			server.bind(54321, 55321);
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to bind server! (Another server already running?)");
		}
		
		server.start();
		
		System.out.println("Server started");
	}
	
	public void stop()
	{
		server.stop();
	}
	
	public static void initKryoClasses(Kryo kryo)
	{
		kryo.register(NBlockModification.class);
		kryo.register(NEntityPosModification.class);
		kryo.register(Vector2.class);
		kryo.register(Nclick.class);
		kryo.register(NPlayerUpdate.class);
		kryo.register(NPlayer.class);
		kryo.register(NInputUpdate.class);
	}
}
