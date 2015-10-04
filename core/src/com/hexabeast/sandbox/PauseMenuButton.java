package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseMenuButton extends Button {
	public PauseMenuButton(TextureRegion texture, float x, float y)
	{
		this.tex = texture;
		this.x = x-texture.getRegionWidth();
		this.y = y-texture.getRegionHeight();
		this.w = texture.getRegionWidth()*2;
		this.h = texture.getRegionHeight()*2;
	}
}
