package com.hexabeast.sandbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hexabeast.sandbox.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = true;
		config.fullscreen = false;
		config.vSyncEnabled = false;
		//config.samples = 16;
		//config.useGL30 = true;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.title = "Game";
		new LwjglApplication(new Main(), config);
	}
}
