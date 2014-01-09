package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private Sprite player;
	
	// movement vectors
	private Vector2 acc;
	private Vector2 vel;
	private Vector2 dir;
	
	// constants
	private static final int SPEED = 5;
	private static final int WIDTH = 16;
	private static final int HEIGHT = 32;
	private static final int TURN_SPEED = 250;

	public Player()
	{
		// load image that player will use
		Texture ArrowImage = new Texture(Gdx.files.internal("arrow.png"));
		TextureRegion playerImage = new TextureRegion(ArrowImage, 203, 413);

		// Create and size player sprite
		player = new Sprite(playerImage, 0, 0, 203, 413);
		player.setSize(WIDTH, HEIGHT);
		player.setOrigin(WIDTH/2, HEIGHT/2);

		// start player in center of screen, facing up, stationary
		player.setPosition(600/2 - WIDTH/2, 600/2 - HEIGHT/2);
		dir = new Vector2(0, 1);
		vel = new Vector2(0, 0);
		acc = new Vector2(0, 0);
	}

	public void draw(SpriteBatch batch) { player.draw(batch); }

	public Bullet shoot()
	{
		float x = player.getX() + WIDTH/2;
		float y = player.getY() + HEIGHT/2;
		return new Bullet(x, y, dir);
	}

	public void move(float delta) 
	{
		// rotate sprite and set dir
		if(Gdx.input.isKeyPressed(Keys.LEFT)) 
		{
			player.rotate(TURN_SPEED * delta);
			dir.rotate(TURN_SPEED * delta).nor();
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) 
		{
			player.rotate(-TURN_SPEED * delta);
			dir.rotate(-TURN_SPEED * delta).nor();
		}
		
		// update acceleration vector
		acc = dir.cpy().scl(30f * delta);
		
		// accelerate forward or backward
		if( Gdx.input.isKeyPressed(Keys.DOWN) ) 
		{
			if(vel.len() < SPEED)
				vel.sub(acc);
			vel.setAngle(acc.cpy().rotate(180).angle());
		}
		else if(Gdx.input.isKeyPressed(Keys.UP)) 
		{
			if(vel.len() < SPEED)
				vel.add(acc);
			vel.setAngle(acc.angle());
		}
		
		// slow down if nothing is pressed
		else if (vel.len() > 0) 
		{
			if(vel.x > 0) vel.x -= Math.abs(acc.x/2);
			else if(vel.x < 0) vel.x += Math.abs(acc.x/2);
			if(vel.y > 0) vel.y -= Math.abs(acc.y/2);
			else if(vel.y < 0) vel.y += Math.abs(acc.y/2);
		}
		
		// keep player within screen bounds
		if(player.getX() < 0) player.setX(0);
		if( player.getX() > 600 - WIDTH ) player.setX( 600 - WIDTH );
		if(player.getY() < 0) player.setY(0);
		if( player.getY() > 600 - HEIGHT ) player.setY( 600 - HEIGHT );
		
		// finally change player position based on above calculations
		player.setX( player.getX() + vel.x );
		player.setY( player.getY() + vel.y );
	}
	
	void dispose() { player.getTexture().dispose();	}
}
