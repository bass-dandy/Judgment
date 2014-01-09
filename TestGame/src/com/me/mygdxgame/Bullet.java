package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	private Sprite bullet;
	public Circle bounds;
	
	// calculated values
	private Vector2 dir;
	public float x;
	public float y;
	
	// constants
	private static final int SPEED = 1000;
	private static final int SIZE = 8;
	
	
	public Bullet(float x, float y, Vector2 dir)
	{
		// create sprite
		Texture bulletImage = new Texture( Gdx.files.internal("bullet.png") );
		bullet = new Sprite(bulletImage);
		bullet.setSize(SIZE, SIZE);
		bullet.setPosition(x - SIZE/2, y - SIZE/2);
		bounds = new Circle(x, y, SIZE/2);
		
		// set direction and position
		this.dir = dir.cpy();
		this.x = x;
		this.y = y;
	}
	
	public boolean isOutOfBounds()
	{
		if(x < 0 - bullet.getWidth() || x > 600 || y < 0 - bullet.getHeight() || y > 600)
			return true;
		
		else return false;
	}
	
	public void draw(SpriteBatch batch)
	{
		// draw self
		bullet.draw(batch);
		
		// continue flying in given direction
		x += dir.x * SPEED * Gdx.graphics.getDeltaTime();
		y += dir.y * SPEED * Gdx.graphics.getDeltaTime();
		bullet.setX(x);
		bullet.setY(y);
		bounds.setPosition(x, y);
	}
	
	void dispose() { bullet.getTexture().dispose(); }
}
