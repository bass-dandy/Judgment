package com.me.mygdxgame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Judgement";
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 600;
		cfg.resizable = false; 
		cfg.addIcon("icon.png", Files.FileType.Internal);
		
		new LwjglApplication(new TestGame(), cfg);
	}
}
