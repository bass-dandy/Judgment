package com.me.mygdxgame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Rock
{
	
	private Sprite rock;
	public Circle bounds;
	public boolean collided = false;
	
	// attributes
	private int rockWidth;
	private int rockHeight;
	private float speed;
	private int rotationSpeed;
	private int spin = 1;
	public long damage;
	
	// position and movement
	public Vector2 vel;
	public float x;
	public float y;
	
	// min and max values
	private static final int MAX_SPEED = 80;
	private static final int MIN_ROCK_SIZE = 20;
	private static final int MAX_ROCK_SIZE = 60;
	private static final int MAX_ROTATION_SPEED = 500;
	
	
	public Rock()
	{
		Random r = new Random();
		
		// randomize size and speed
		rockWidth = r.nextInt(MAX_ROCK_SIZE - MIN_ROCK_SIZE) + MIN_ROCK_SIZE;
		rockHeight = rockWidth * 3/2;
		speed = r.nextInt(MAX_SPEED);
		
		// calculate damage
		damage = (long) (speed * rockHeight * 500000L);
		
		// randomize rotation speed and direction
		rotationSpeed = r.nextInt(MAX_ROTATION_SPEED);
		if(speed % 2 == 0)
			spin = -1;
		
		// create sprite
		Texture rockTexture = new Texture("kyle.png");
		TextureRegion rockImage = new TextureRegion(rockTexture, 221, 332);
		rock = new Sprite(rockImage, 0, 0, 221, 332);
		rock.setSize(rockWidth, rockHeight);
		rock.setOrigin(rockWidth/2, rockHeight/2);
		
		// set bounding box
		bounds = new Circle(0, 0, rockHeight/2);
		
		// spawn rock from one of 4 sides of the screen
		int side = r.nextInt(4);
		spawn(side, r);
	}
	
	public boolean isOutOfBounds()
	{
		if( rock.getX() < 0 - 2 * rockWidth || rock.getX() > 600 + 2 * rockWidth || 
				rock.getY() < 0 - 2 * rockHeight || rock.getY() > 600 + 2 * rockHeight )
			return true;
		
		else return false;
	}
	
	public void collide(Rock other)
	{
		Vector2 v1final;
		Vector2 v2final;
		
		// phun with physics!
		Vector2 unitNorm = new Vector2(other.x - this.x, other.y - this.y).nor();
		Vector2 unitTan = new Vector2(-unitNorm.y, unitNorm.x);
		
		// project vector components onto the normal and tangent unit vectors of the collision
		float v1norm_start = unitNorm.dot(this.vel);
		float v1tan = unitTan.dot(this.vel);
		float v2norm_start = unitNorm.dot(other.vel);
		float v2tan = unitTan.dot(other.vel);
		
		// base masses on size (squared so differences are more pronounced)
		float m1 = this.rockHeight * this.rockHeight;
		float m2 = other.rockHeight * other.rockHeight;
		
		// plug into fancy equations
		float v1norm_end = v1norm_start * (m1 - m2);
		v1norm_end += 2 * m2 * v2norm_start;
		v1norm_end /= (m1 + m2);
		
		float v2norm_end = v2norm_start * (m2 - m1);
		v2norm_end += 2 * m1 * v1norm_start;
		v2norm_end /= (m1 + m2);
		
		// normal and tangent by result, add to unit normal and tangent
		v1final = unitNorm.cpy().scl(v1norm_end);
		v1final = v1final.add(unitTan.cpy().scl(v1tan));
		
		v2final = unitNorm.cpy().scl(v2norm_end);
		v2final = v2final.add(unitTan.cpy().scl(v2tan));
		
		// we're done!
		this.vel = v1final;
		other.vel = v2final;
	}
	
	private void spawn(int s, Random r)
	{
		int posX;
		int posY;
		
		// spawn at given side of screen, set velocity inwards
		switch(s)
		{
		case 0:
			posX = r.nextInt(600) + 1;
			posY = 600 + rockHeight;
			vel = new Vector2(spin * r.nextFloat(), -1).scl(speed);
			break;
		case 1:
			posX = 600;
			posY = r.nextInt(600) + 1;
			vel = new Vector2(-1, spin * r.nextFloat()).scl(speed);
			break;
		case 2:
			posX = r.nextInt(600) + 1;
			posY = 0 - rockHeight;
			vel = new Vector2(spin * r.nextFloat(), 1).scl(speed);
			break;
		default:
			posX = 0 - rockWidth;
			posY = r.nextInt(600) + 1;
			vel = new Vector2(1, spin * r.nextFloat()).scl(speed);
			break;
		}
		rock.setX(posX);
		rock.setY(posY);
		bounds.setPosition(posX + rockWidth/2, posY + rockHeight/2);
		x = posX + rockWidth/2;
		y = posY + rockHeight/2;
	}
	
	private void move()
	{
		// move in calculated direction
		rock.setX(rock.getX() + vel.x * Gdx.graphics.getDeltaTime() );
		rock.setY(rock.getY() + vel.y * Gdx.graphics.getDeltaTime() );
		bounds.setPosition(rock.getX() + rockWidth/2, rock.getY() + rockHeight/2);
		x = rock.getX() + rockWidth/2;
		y = rock.getY() + rockHeight/2;
		// rotate at calculated speed in calculated direction
		rock.rotate(spin * rotationSpeed * Gdx.graphics.getDeltaTime());
	}
	
	public void draw(SpriteBatch batch) {
		move();
		rock.draw(batch);
	}
	
	public void dispose() { rock.getTexture().dispose(); }
}
