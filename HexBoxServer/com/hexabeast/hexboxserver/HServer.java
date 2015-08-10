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
		
		server = new Server(1048576, 1048576);
		
		initKryoClasses(server.getKryo());
		
		server.addListener(new Listener() 
		{
		   @Override
	       public void received (Connection c, Object object) 
	       {
	    	   if (object instanceof String) 
		          {
		             String str = (String)object;
		             
		             if(str.equals("GetMainLayer"))
		             {
		            	 Main.map.sendCompressedMap(true, c);
		             }
		             
		             if(str.equals("GetBackLayer"))
		             {
		            	 Main.map.sendCompressedMap(false, c);
		             }
		          }
	    	   
	          if (object instanceof NBlockModification) 
	          {
	             NBlockModification nnn = (NBlockModification)object;
	             Main.map.setBlock(nnn);
	          }
	          
	          if (object instanceof NPlayer)
	          {
	        	 ((NPlayer)object).id = c.getID();
	        	 server.sendToAllExceptTCP(c.getID(),object);
	          }
	          
	          if (object instanceof NPlayerUpdate)
	          {
	        	  ((NPlayerUpdate)object).id = c.getID();
	        	 server.sendToAllExceptUDP(c.getID(),object);
	          }
	          
	          if (object instanceof NInputUpdate)
	          {
	        	  ((NInputUpdate)object).id = c.getID();
	        	 server.sendToAllExceptUDP(c.getID(),object);
	          }
	          
	          if (object instanceof Nclick)
	          {
	        	  ((Nclick)object).id = c.getID();
	        	 server.sendToAllExceptUDP(c.getID(),object);
	          }
	       }
		   @Override
	       public void connected(Connection c)
	       {
	    	   System.out.println("Player connected!");
	    	   c.sendTCP("Connected");
           }
		   @Override
	       public void disconnected (Connection c) 
	       {
			   System.out.println("Player disconnected");
			   Ndead n = new Ndead();
			   n.id = c.getID();
	    	   server.sendToAllExceptTCP(c.getID(),n);
	       }
		});
		
		try 
		{
			server.bind(43321, 45322);
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to bind server! (Another server already running?)");
		}
		
		server.start();
		
		System.out.println("Server started");
	}
	
	public void sendBlock(NBlockModification n)
	{
		server.sendToAllTCP(n);
	}
	
	public void sendLayer(NCompressedLayer n, Connection c)
	{
		c.sendTCP(n);
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
		kryo.register(String.class);
		kryo.register(NCompressedLayer.class);
		kryo.register(Ndead.class);
	}
}
