package com.hexabeast.sandbox.mobs;

import com.badlogic.gdx.math.Rectangle;
import com.hexabeast.sandbox.HitBox;
import com.hexabeast.sandbox.HitRect;
import com.hexabeast.sandbox.TextureManager;

public class PNJ extends Mob{
	
	int larmId = 0;
	int bodyId = 1;
	int legsId = 2;
	int headId = 3;
	int hairId = 4;
	int rarmId = 5;
	

	public PNJ()
	{
		tex.add(TextureManager.instance.armLTexture);
		tex.add(TextureManager.instance.Bbody);
		tex.add(TextureManager.instance.Blegs[0]);
		tex.add(TextureManager.instance.Bhead);
		tex.add(TextureManager.instance.Bhairs);
		tex.add(TextureManager.instance.armLTexture);
		hitrect = new HitRect(offx+width);
		hitrect.add(new Rectangle(0,0,50,50));
		calculateSize(1);

		hitbox = new HitBox(new Rectangle(0, 0, 50, 50), offx+width);
		hitbox.noturn = true;
	}
	@Override
	public void IA()
	{
		
	}
	@Override
	public void premove()
	{
		
	}
	@Override
	public void visual()
	{
		
	}
}
