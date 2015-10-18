package com.hexabeast.hexboxserver;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class HServer {
	
	static HServer instance;
	public Server server;
	
	public boolean problem = false;
	
	public boolean running = false;
	
	public VirtualMap servermap;
	
	public LinkedBlockingQueue<NBlockModification> bqueue;
	
	public static int port= 25565;
	
	//public ArrayList<Integer> ids;
	
	class RunnableSendMap implements Runnable {
		Connection c;
		boolean l;
        RunnableSendMap(boolean l, Connection c) 
		{ 
			this.l = l;
			this.c = c; 
        }
        public void run() {
        	servermap.sendCompressedMap(l, c);
        }
    }
	
	class BlockModifs implements Runnable {
		LinkedBlockingQueue<NBlockModification> b;
		
		BlockModifs(LinkedBlockingQueue<NBlockModification> b) 
		{ 
			this.b = b;
        }
        public void run() 
        {
        	while(running)
        	{
        		try {
					servermap.setBlock(bqueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        }
    }
	
	public HServer(final VirtualMap servermap)
	{
		this.servermap = servermap;
		
		bqueue = new LinkedBlockingQueue<NBlockModification>();
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
		             
		             if(str.equals("GetMainLayer") || str.equals("GetBackLayer"))
		             {
		            	 boolean l = str.equals("GetMainLayer");
		            	 new Thread(new RunnableSendMap(l,c)).start();
		            	 
		             }
		          }
	    	   
	          if (object instanceof NBlockModification) 
	          {
	             NBlockModification nnn = (NBlockModification)object;
	             try {
					bqueue.put(nnn);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	          }
	          
	          if (object instanceof NPlayer)
	          {
	        	 ((NPlayer)object).id = c.getID();
	        	 server.sendToAllExceptTCP(c.getID(),object);
	          }
	          
	          if (object instanceof HMessage)
	          {
	        	 ((HMessage)object).id = c.getID();
	        	 server.sendToAllTCP(object);
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
	          
	          if (object instanceof NInputUpDown)
	          {
	        	  ((NInputUpDown)object).id = c.getID();
	        	 server.sendToAllExceptTCP(c.getID(),object);
	          }
	          
	          if (object instanceof NInputRightLeft)
	          {
	        	  ((NInputRightLeft)object).id = c.getID();
	        	 server.sendToAllExceptTCP(c.getID(),object);
	          }
	          
	          if (object instanceof Nclick)
	          {
	        	  ((Nclick)object).id = c.getID();
	        	 server.sendToAllExceptUDP(c.getID(),object);
	          }
	          
	          if (object instanceof ConsoleMessage)
	          {
	        	  ((ConsoleMessage)object).id = c.getID();
	        	 server.sendToAllExceptTCP(c.getID(),object);
	          }
	       }
		   @Override
	       public void connected(Connection c)
	       {
	    	   System.out.println("Player connected!");
			   ConsoleMessage cn = new ConsoleMessage(ConsoleMessage.CONNECTED, "id = "+String.valueOf(c.getID()));
			   cn.id = c.getID();
	    	   server.sendToAllExceptTCP(c.getID(),cn);
	    	   c.sendTCP("Connected");
           }
		   @Override
	       public void disconnected (Connection c) 
	       {
			   System.out.println("Player disconnected");
			   Ndead n = new Ndead();
			   ConsoleMessage cn = new ConsoleMessage(ConsoleMessage.DISCONNECTED, "id = "+String.valueOf(c.getID()));
			   n.id = c.getID();
			   cn.id = c.getID();
	    	   server.sendToAllExceptTCP(c.getID(),n);
	    	   server.sendToAllExceptTCP(c.getID(),cn);
	       }
		});
		
		try 
		{
			server.bind(port, port);
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to bind server! (Another server already running?)");
			problem = true;
		}
		
		server.start();
		
		if(!problem)
		{
			runServer();
			System.out.println("Server started");
		}
		
	}
	
	public void runServer()
	{
		running = true;
		new Thread(new BlockModifs(bqueue)).start();
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
		running = false;
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
		kryo.register(NInputRightLeft.class);
		kryo.register(NInputUpDown.class);
		kryo.register(HMessage.class);
		kryo.register(ConsoleMessage.class);
	}
}
