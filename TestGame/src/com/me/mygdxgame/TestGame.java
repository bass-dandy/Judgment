package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestGame implements ApplicationListener {

	private SpriteBatch batch;
	
	// screens
	private ControlScreen intro;
	private MenuScreen menu;
	private GameScreen game;
	private GameOverScreen gameOver;
	private int state = 0;


	@Override
	public void create() 
	{	
		batch = new SpriteBatch();
		intro = new ControlScreen(batch);
		game = new GameScreen(batch);
		menu = new MenuScreen(batch);
		gameOver = new GameOverScreen(batch);
	}

	@Override
	public void render() 
	{
		switch(state) 
		{
		case 0:
			intro.show();
			if(intro.ready)
				state++;
			break;
		case 1:
			menu.show();
			if(Gdx.input.isKeyPressed(Keys.SPACE) && menu.ready)
				state++;
			break;
		case 2:
			game.show();
			if(game.ready)
				state++;
			break;
		case 3:
			gameOver.show();
			if(gameOver.ready)
				state++;
			break;
		default:
			menu.reset();
			game.reset();
			gameOver.reset();
			state = 1;
			break;
		}
	}
	
	@Override
	public void dispose() 
	{
		batch.dispose();
		menu.dispose();
		game.dispose();
		gameOver.dispose();
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }
}
