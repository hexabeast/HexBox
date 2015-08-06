package com.hexabeast.hexboxserver;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class HServer {
	
	static HServer instance;
	public Server server;
	
	public HServer()
	{
		server = new Server();
		
		
		initKryoClasses(server.getKryo());
		
		server.addListener(new Listener() 
		{
	       public void received (Connection connection, Object object) 
	       {
	          if (object instanceof NBlockModification) 
	          {
	             connection.sendTCP(object);
	          }
	          
	       }
	       public void connected(Connection con)
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
			System.out.println("Failed to bind server!");
		}
		
		server.start();
		
		System.out.println("Server started");
	}
	
	public static void initKryoClasses(Kryo kryo)
	{
		kryo.register(NBlockModification.class);
		kryo.register(NEntityPosModification.class);
	}
}
