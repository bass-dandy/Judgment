package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private BitmapFont font;

	// game objects
	private Player player;
	private Earth earth;
	private ArrayList<Bullet> bullets;
	private ArrayList<Rock> rocks;
	private Music music;
	private Texture fade;

	// player and game state trackers
	private float lastSpawnTime = 0f;
	private int score = 0;
	private int level = 0;
	private float fadeOpacity = 0f;
	private boolean canShoot = true;
	private boolean canScale = true;
	public boolean gameOver = false;
	public boolean ready = false;

	// constants
	private static float BASE_SPAWN_RATE = 1000000000f;
	private static float SPAWN_CHANGE_RATE = 30000000f;
	private static int KILLS_TO_NEXT_LEVEL = 10;


	public GameScreen(SpriteBatch batch)
	{
		// init structures
		this.batch = batch;
		bullets = new ArrayList<Bullet>();
		rocks = new ArrayList<Rock>();

		// init assets
		player = new Player();
		earth = new Earth();
		fade = new Texture(Gdx.files.internal("black.png"));
		font = new BitmapFont(Gdx.files.internal("data/homespun.fnt"));
		font.setScale(0.5f);

		// start music, set to loop
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3") );
		music.setLooping(true);
	}

	private void update(float delta)
	{
		// check if player has lost the game
		if(earth.population <= 0) {
			earth.population = 0;
			gameOver = true;
		}

		// fade out if the player has lost the game
		if(gameOver)
			fadeOpacity += 0.3f * delta;
		if(fadeOpacity > 0.99f)
			ready = true;

		// get player input if the round has started, otherwise zoom out from earth until it is ready
		if(earth.zoomComplete())
			getPlayerInput();
		else
			earth.zoom();

		// spawn rocks
		if(TimeUtils.nanoTime() - lastSpawnTime > (BASE_SPAWN_RATE - SPAWN_CHANGE_RATE * level) 
				&& earth.zoomComplete()) 
		{
			rocks.add(new Rock());
			lastSpawnTime = TimeUtils.nanoTime();
		}

		// calculate collisions
		doRockCollisions();
		doBulletCollisions();

		// scale difficulty (time between rock spawns)
		if(score % KILLS_TO_NEXT_LEVEL == 0 && score > 0 && canScale) {
			level++;
			canScale = false;
		}
		else if(score % KILLS_TO_NEXT_LEVEL != 0)
			canScale = true;
	}

	@Override
	public void render(float delta) 
	{
		update(delta);

		// clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Draw everything but bullets
		batch.enableBlending();
		batch.begin();
		earth.draw(batch);
		for(Rock r: rocks) { r.draw(batch); }
		player.draw(batch);
		if(earth.zoomComplete()) {
			font.draw(batch, "Destroyed: " + score, 5, 578);
			font.draw(batch, "Population: " + earth.population, 5, 595);
		}
		batch.setColor(1, 1, 1, fadeOpacity);
		batch.draw(fade, 0, 0, 600, 600);
		batch.end();

		// draw bullets
		batch.disableBlending();
		batch.begin();
		for(Bullet b: bullets) { b.draw(batch); }
		batch.end();
	}

	private void doRockCollisions()
	{
		Iterator<Rock> it = rocks.iterator();
		Rock r;

		// check for rocks out of bounds
		while(it.hasNext()) {
			r = it.next();
			if(r.isOutOfBounds()) {
				r.dispose();
				it.remove();
			}
			// check collision between rock and earth
			else if(Intersector.overlaps(earth.bounds, r.bounds)) {
				earth.population -= r.damage;
				r.dispose();
				it.remove();
			}
			else {
				// check collision between rocks and... rocks
				for(Rock r2: rocks) {
					for(Rock r3: rocks) {
						if(!r2.equals(r3) && Intersector.overlaps(r2.bounds, r3.bounds) && ! r3.collided) {
							r2.collide(r3);
							r2.collided = true;
						}
					}
				}
			}	
		}
		// reset rocks for next collision check
		for(Rock rock: rocks) { rock.collided = false; }
	}

	private void doBulletCollisions()
	{
		Iterator<Bullet> it = bullets.iterator();
		Iterator<Rock> it2 = rocks.iterator();
		Bullet b;
		Rock r;

		while(it.hasNext()) 
		{
			b = it.next();
			// first check if bullet is out of bounds
			if(b.isOutOfBounds()) {
				b.dispose();
				it.remove();
			}
			// if in bounds, check collisions
			else {
				boolean deleted = false;
				it2 = rocks.iterator();
				while(it2.hasNext() && !deleted) {
					r = it2.next();
					// check collisions between bullet and rocks
					if(Intersector.overlaps(b.bounds, r.bounds)) {
						r.dispose();
						it2.remove();
						b.dispose();
						it.remove();
						deleted = true;
						if(!gameOver)
							score++;
					}
				}
			}
		}
	}

	private void getPlayerInput()
	{
		// move player
		player.move( Gdx.graphics.getDeltaTime() );

		// shoot one bullet each time z is pressed
		if(Gdx.input.isKeyPressed(Keys.Z) && canShoot) {
			bullets.add(player.shoot());
			canShoot = false;
		}
		else if( !Gdx.input.isKeyPressed(Keys.Z) )
			canShoot = true;
	}

	public void reset()
	{
		bullets.clear();
		rocks.clear();
		player.dispose();
		player = new Player();
		earth.dispose();
		earth = new Earth();
		score = 0;
		level = 0;
		gameOver = false;
		canShoot = true;
		canScale = true;
		lastSpawnTime = 0f;
		fadeOpacity = 0f;
		ready = false;
	}

	@Override
	public void dispose() 
	{
		player.dispose();
		music.dispose();
		earth.dispose();
		font.dispose();
		fade.dispose();
		for(Bullet b: bullets) { b.dispose(); }
		for(Rock r: rocks) { r.dispose(); }
	}

	public void show() 
	{
		if(!music.isPlaying())
			music.play();
		render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }
}
