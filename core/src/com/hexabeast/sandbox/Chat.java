package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Chat {
	TextField inputField;
	Stage scene;
	
	ArrayList<Message> messages = new ArrayList<Message>();
	
	BitmapFont font;
	
	public Chat()
	{
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
	
	public void addMessage(String str)
	{
		if(!str.equals(""))messages.add(new Message(str,Main.name));
	}
	
	public void drawMessages(float x, float y)
	{
		Main.batch.begin();
		
		if(Main.game.chatEnabled)Main.batch.draw(TextureManager.instance.chatBehind, x+5,y+55,410,28);
		
		float offsety = 100;
		
		for(int i = messages.size()-1; i>=0; i--)
		{
			
			TextBounds boundsOwner = font.getBounds(messages.get(i).owner + " : ");
			TextBounds bounds = font.getWrappedBounds(messages.get(i).str, 400);
			float bh = bounds.height;
			
			font.setColor(0.7f, 0.7f, 0.7f,1);
			font.draw(Main.batch,  messages.get(i).owner + " : ", x+10, y+offsety+boundsOwner.height);
			font.setColor(Color.WHITE);
			font.drawWrapped(Main.batch, messages.get(i).str, x+10+boundsOwner.width, y+offsety+bh, 400);
			
			offsety+=bounds.height+20;
		}
		Main.batch.end();
	}
	
	public void draw()
	{
		Main.batch.begin();
		scene.draw();
		Main.batch.end();
	}
}
