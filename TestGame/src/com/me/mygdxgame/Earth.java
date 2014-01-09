package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Earth 
{
	public Circle bounds;
	private Sprite earth;
	
	// sizing attributes
	private static final int START_SIZE = 600;
	private static final int STOP_SIZE = 200;
	private static final int ZOOM_SPEED = 2;
	private int currentSize;
	
	// health
	public long population = 8000000000L;
	
	public Earth()
	{
		// create sprite
		Texture earthTexture = new Texture(Gdx.files.internal("earth.png"));
		earth = new Sprite(earthTexture, 0, 0, 1024, 1024);
		earth.setSize(START_SIZE, START_SIZE);
		currentSize = START_SIZE;
		earth.setPosition((600 - currentSize)/2, (600 - currentSize)/2);
		
		bounds = new Circle(300, 300, STOP_SIZE/2 - 10);
	}
	
	public void zoom()
	{
		if(currentSize > STOP_SIZE) {
			currentSize -= ZOOM_SPEED;
			earth.setSize(currentSize, currentSize);
			earth.setPosition((600 - currentSize)/2, (600 - currentSize)/2);
		}
	}
	
	public boolean zoomComplete()
	{
		if(currentSize == STOP_SIZE)
			return true;
		else return false;
	}
	
	public void draw(SpriteBatch batch) { earth.draw(batch); }
	
	public void dispose() { earth.getTexture().dispose(); }
}
