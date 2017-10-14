package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hexabeast.hexboxserver.HMessage;

public class Chat {
	TextField inputField;
	Stage scene;
	
	ArrayList<Message> messages = new ArrayList<Message>();
	
	BitmapFont font;
	Random ran;
	
	public Chat()
	{
		ran = new Random();
		font = FontManager.instance.fontsmall;
		
		this.scene = new Stage();
		TextFieldStyle tfs = new TextFieldStyle();
		tfs.font = font;
		tfs.fontColor = Color.WHITE;
		tfs.selection = new TextureRegionDrawable(TextureManager.instance.textBoxSurline);
		tfs.cursor = new TextureRegionDrawable(TextureManager.instance.textBoxCursor);
		//tfs.background = new TextureRegionDrawable(TextureManager.instance.ipButton);
		
		inputField = new TextField("", tfs);
		inputField.setWidth(400);
		
		inputField.setPosition(10,60);
		
		scene.addActor(inputField);
	}
	
	public void addMessageN(HMessage msg)
	{
		if(msg.str != null && msg.owner != null)
		{
			if(msg.str.length()>0)
			{
				messages.add(new Message(msg.str,msg.owner,generateColor(msg.id)));
			}
		}
	}
	
	public Color generateColor(int id)
	{
		ran.setSeed(id*123);
		float[] col = new float[3];
		col[0] = ran.nextFloat();
		col[1] = ran.nextFloat();
		col[2] = ran.nextFloat();
		int max = 0;
		for(int i= 0; i<3; i++)
		{
			if(col[max]<col[i])max = i;
		}
		for(int i= 0; i<3; i++)
		{
			col[i]*= (1/col[max]);
		}
		return new Color(col[0],col[1], col[2], 1);
	}
	
	public void addConsoleMessage(String str)
	{
		messages.add(new Message(str,"Console", new Color(1f, 0.3f, 0.3f,1)));
	}
	
	public void addMessage(String str)
	{
		if(!str.equals(""))
		{
			if(str.startsWith("/"))
			{
				String s = "Command not found !";
				String[] words = str.split(" ");
				if(words.length == 1)
				{	
					if(words[0].equals("/cheatmode"))
					{
						Main.enableCheats = !Main.enableCheats;
						s = Tools.enabldis(Main.enableCheats) + "cheats";
					}
					else if(words[0].equals("/quit") || words[0].equals("/exit"))
					{
						Main.backToMenu(Main.defaultWelcomeMessage, Color.WHITE);
						
					}
					
					if(Main.enableCheats)
					{
						if(words[0].equals("/superman"))
						{
							Parameters.i.superman = !Parameters.i.superman;
							s = Tools.enabldis(Parameters.i.superman) + "superman mode";
						}
						
						else if(words[0].equals("/drawhitbox"))
						{
							Parameters.i.drawhitbox = !Parameters.i.drawhitbox;
							s = Tools.enabldis(Parameters.i.drawhitbox) + "hitbox display";
						}
						else if(words[0].equals("/supermagic"))
						{
							Parameters.i.cheatMagic = !Parameters.i.cheatMagic;
							s = Tools.enabldis(Parameters.i.cheatMagic) + "super magic";
						}
						else if(words[0].equals("/ultrarate"))
						{
							Parameters.i.ultrarate = !Parameters.i.ultrarate;
							s = Tools.enabldis(Parameters.i.ultrarate) + "ultra rate";
						}
						else if(words[0].equals("/ultrarange"))
						{
							Parameters.i.ultrarange = !Parameters.i.ultrarange;
							s = Tools.enabldis(Parameters.i.ultrarange) + "ultra range";
						}
						else if(words[0].equals("/bigview"))
						{
							Parameters.i.noShadow = true;
							Main.zoom = 40;
						}
						else if(words[0].equals("/noshadow"))
						{
							Parameters.i.noShadow = !Parameters.i.noShadow;
							s = Tools.enabldis(!Parameters.i.noShadow) + "shadows";
						}
						else if(words[0].equals("/godmode"))
						{
							Parameters.i.godmode = !Parameters.i.godmode;
							s = Tools.enabldis(Parameters.i.godmode) + "godmode";
						}
						else if(words[0].equals("/day"))
						{
							Parameters.i.daylight = 1;
							s = "Setting time to day";
						}
						else if(words[0].equals("/night"))
						{
							Parameters.i.daylight = 0.1f;
							s = "Setting time to night";
						}
						else if(words[0].equals("/rain"))
						{
							Parameters.i.rain = !Parameters.i.rain;
							s = Tools.enabldis(Parameters.i.rain) + "rain";
						}
						
						else if(words[0].equals("/untransform"))
						{
							GameScreen.player.transform(0);
							s = "Untransforming...";
						}
						
					}
				}
				else if(words.length == 2)
				{
					if(Main.enableCheats)
					{
						if(words[0].equals("/gamespeed"))
						{
							try
							{
								float b=Float.parseFloat(words[1]);
								if(b>10.001 ||b<0.0099)s = "Enter a value between 0.01 and 10";
								else
								{
									Parameters.i.deltaMultiplier = b;
									s = "Game speed changed!";
								}
							}
							catch(java.lang.NumberFormatException e)
							{
								s = "The game speed must be a number (0.01 to 10)";
							}
						}
						else if(words[0].equals("/transform"))
						{
							if(words[1].equals("wolf"))
							{
								GameScreen.player.transform(1);
								s = "Transforming to wolf...";
							}
							else if(words[1].equals("insectboss"))
							{
								GameScreen.player.transform(2);
								s = "Transforming to Insect Boss...";
							}
							else if(words[1].equals("insect"))
							{
								GameScreen.player.transform(3);
								s = "Transforming to insect...";
							}
							else
							{
								s = "Enter a valid mob name";
							}
						}
					}
					
					
					if(words[0].equals("/name"))
					{
						if(words[1].length()>2 && words[1].length()<16)
						{
							Parameters.i.name = words[1];
							s = "Name changed to " + words[1];
						}
						else s = "Your name must be between 3 and 15 characters!";
					}
				}
				else if(words.length == 3)
				{
					if(words[0].equals("/spawnmob"))
					{
						try
						{
							int b=Integer.parseInt(words[2]);
							if(b>10000 ||b<1)s = "Enter a value between 1 and 10000";
							else
							{
								int c = -1;
								if(words[1].equals("wolf"))
								{
									c = 2;
									s = "Spawning "+words[2]+" wolf...";
								}
								else if(words[1].equals("insectboss"))
								{
									c = 1;
									s = "Spawning "+words[2]+" insect boss...";
								}
								else if(words[1].equals("insect"))
								{
									c = 3;
									s = "Spawning "+words[2]+" insect...";
								}
								if(c != -1)
								{
									for(int i = 0; i<b; i++)GameScreen.entities.mobs.placeMob(GameScreen.player.PNJ.middle.x, GameScreen.player.PNJ.y+50, c);
								}
								else
								{
									s = "Enter a valid mob name";
								}
							}
						}
						catch(java.lang.NumberFormatException e)
						{
							s = "The last value must be a number";
						}
					}
				}
				messages.add(new Message(s,"Console", new Color(1f, 0.3f, 0.3f,1)));
			}
			else
			{
				if(!NetworkManager.instance.online)messages.add(new Message(str,Parameters.i.name, new Color(0.7f, 0.7f, 0.7f,1)));
				else NetworkManager.instance.sendTCP(new HMessage(str, Parameters.i.name));
			}
		}
	}
	
	public void drawMessages(float x, float y)
	{
		Main.batch.begin();
		
		if(Main.game.chatEnabled)Main.batch.draw(TextureManager.instance.chatBehind, x+5,y+55,410,28);
		
		float offsety = 100;
		
		for(int i = messages.size()-1; i>=0; i--)
		{
			float alpha;
			if(Main.game.chatEnabled)
			{
				alpha = 1;
				messages.get(i).alpha-=Main.delta/2;
			}
			else
			{
				alpha = messages.get(i).alpha;
				messages.get(i).alpha-=Main.delta;
			}
			if(messages.get(i).alpha<0)messages.get(i).alpha = 0;
			
			if(offsety>600)
			{
				messages.remove(i);
			}
			else if(alpha>0.001)
			{
				//TextBounds boundsOwner = font.getBounds(messages.get(i).owner + " : ");
				//TextBounds bounds = font.getWrappedBounds(messages.get(i).str, 400);
				GlyphLayout boundsOwner=new GlyphLayout(font, messages.get(i).owner + " : ");
				GlyphLayout bounds=new GlyphLayout(font, messages.get(i).str, font.getColor(), 400, Align.left, true);
				float bh = bounds.height;
				
				
				font.setColor(messages.get(i).col.r, messages.get(i).col.g, messages.get(i).col.b,Math.min(alpha, 1));
				
				font.draw(Main.batch,  messages.get(i).owner + " : ", x+10, y+offsety+boundsOwner.height);
				
				font.setColor(1,1,1,Math.min(alpha, 1));
				
				//font.drawWrapped(Main.batch, messages.get(i).str, x+10+boundsOwner.width, y+offsety+bh, 400);
				font.draw(Main.batch, messages.get(i).str, x+10+boundsOwner.width, y+offsety+bh, 400,Align.left,true);
				
				font.setColor(Color.WHITE);
				
				offsety+=bounds.height+20;
			}
		}
		Main.batch.end();
	}
	
	public void draw()
	{
		scene.draw();
	}
}
