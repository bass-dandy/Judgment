package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {

	private SpriteBatch batch;
	public boolean ready = false;

	// Assets
	private Texture earth;
	private BitmapFont title;
	private BitmapFont subtitle;
	private BitmapFont prompt;

	// things that affect text
	private float fadeOpacity = 0f;
	private float titleOpacity = 0f;
	private float subtitleOpacity = 0f;
	private float lastChange = 0f;
	public boolean showPrompt = false;

	public MenuScreen(SpriteBatch batch) 
	{
		this.batch = batch;

		earth = new Texture(Gdx.files.internal("earth.png"));

		title = new BitmapFont(Gdx.files.internal("data/block.fnt"));
		title.setColor(1f, 1f, 1f, titleOpacity);
		title.setScale(3.2f);

		subtitle = new BitmapFont(Gdx.files.internal("data/cayetano.fnt"));
		subtitle.setColor(1f, 1f, 1f, subtitleOpacity);
		subtitle.setScale(1.5f);

		prompt = new BitmapFont(Gdx.files.internal("data/homespun.fnt"));
		prompt.setColor(1f, 1f, 1f, 1f);
		prompt.setScale(0.7f);
	}

	private void update(float delta)
	{
		// update opacity values for fade ins
		if(titleOpacity < 0.99)
			titleOpacity += 0.3 * delta;
		else if(subtitleOpacity < 0.99)
			subtitleOpacity += 0.6 * delta;
		else if(fadeOpacity < 0.99)
			fadeOpacity += 0.40 * delta;
		
		// if fade ins are complete, flash button prompt
		else {
			lastChange += delta;
			ready = true;
			if(lastChange > 0.7) {
				showPrompt = !showPrompt;
				lastChange -= lastChange;
			}
		}
		title.setColor(1f, 1f, 1f, titleOpacity);
		subtitle.setColor(1f, 1f, 1f, subtitleOpacity);
		batch.setColor(1, 1, 1, fadeOpacity);
	}

	@Override
	public void render(float delta) 
	{
		// clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		batch.begin();
		
		batch.draw(earth, 0, 0, 600, 600);
		batch.setColor(Color.WHITE);
		title.draw(batch, "JUDGEMENT", 12, 460);
		subtitle.draw(batch, "... Our Final Hours", 325, 380);

		if(showPrompt)
			prompt.draw(batch, "[Press Space to Begin]", 190, 190);

		batch.end();
	}

	public void reset()
	{
		fadeOpacity = 0f;
		titleOpacity = 0f;
		subtitleOpacity = 0f;
		lastChange = 0f;
		showPrompt = false;
		ready = false;
	}

	@Override
	public void show() { render(Gdx.graphics.getDeltaTime()); }

	@Override
	public void dispose() 
	{ 
		earth.dispose();
		title.dispose();
		subtitle.dispose();
		prompt.dispose();
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
