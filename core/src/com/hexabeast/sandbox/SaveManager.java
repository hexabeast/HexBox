package com.hexabeast.sandbox;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class SaveManager {

	static Save sav;
	static SaveManager instance;
	
	public static FileHandle file1;
	public static FileHandle file2;
	public static FileHandle file3;
	public static StringWriter writer;
	public static Json json;
	
	public SaveManager() {
		Gdx.files.local("data").mkdirs();
		
		writer = new StringWriter();
		sav = new Save();
		json = new Json();
		
		file1 = Gdx.files.local("data/saveFile.json");
		file2 = Gdx.files.local("data/saveFile2.json");
		file3 = Gdx.files.local("data/parameters.json");
		
		if(!file1.file().exists()) {
			try {
				file1.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!file2.file().exists()) {
			try {
				file2.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		if(!file3.file().exists()) {
			try {
				file3.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void Save() throws IOException
	{
		if(sav == null)sav = new Save();
		sav.playerPos = new Vector2(GameScreen.player.PNJ.x,GameScreen.player.PNJ.y);
		sav.health = GameScreen.player.currentForm.health;
		//SAVE INV
		if(GameScreen.inventory != null)
		{
			SimpleInventory inven = new SimpleInventory();
			for(int j = 0; j<GameScreen.inventory.invItemsArray[0].length;j++)
			{
				for(int i = 0; i<GameScreen.inventory.invItemsArray.length;i++)
				{
					if(j<7)inven.SimpleinvItemsArray[i][j] = new SimpleInvItem(GameScreen.inventory.invItemsArray[i][j].id,GameScreen.inventory.invItemsArray[i][j].number);
					else inven.SimpleinvItemsArray[i][j] = new SimpleInvItem(0,0);
						
				}
			}	
			String saveItems = json.toJson(inven);	
			file2.writeString(saveItems, false);	
		}
		//SAVE PLAYER
		String savedata = json.toJson(sav);
		file1.writeString(savedata, false);
	}
	
	public static void SaveParam()
	{
		String param = json.toJson(Parameters.i);
		file3.writeString(param, false);
	}
	
	public void Load()
	{
		String loaddata = file1.readString();
		String loadItems = file2.readString();

		sav = json.fromJson(Save.class, loaddata);
		SimpleInventory inv = json.fromJson(SimpleInventory.class, loadItems);
		
		
		if(inv != null)
		{
			for(int j = 0; j<GameScreen.inventory.invItemsArray[0].length;j++)
			{
				for(int i = 0; i<GameScreen.inventory.invItemsArray.length;i++)
				{
					GameScreen.inventory.invItemsArray[i][j].id = inv.SimpleinvItemsArray[i][j].id;
					GameScreen.inventory.invItemsArray[i][j].number = inv.SimpleinvItemsArray[i][j].number;
					Tools.checkItems();
				}
			}	
		}
		
		if(sav != null)
		{
		GameScreen.player.PNJ.x = (sav.playerPos.x);
		GameScreen.player.PNJ.y = (sav.playerPos.y);
		GameScreen.player.PNJ.health = sav.health;
		}
		
		
	}
	
	public void LoadParams()
	{
		String loadParams = file3.readString();
		Parameters param = json.fromJson(Parameters.class, loadParams);
		if(param!= null)
		{
			Parameters.i = param;
		}
	}

}
