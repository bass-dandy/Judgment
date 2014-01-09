package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ControlScreen implements Screen {

	private SpriteBatch batch;
	private Texture controls;
	private BitmapFont text;
	private float alpha = 0f;
	public boolean ready = false;
	
	
	public ControlScreen(SpriteBatch batch)
	{
		this.batch = batch;
		controls = new Texture(Gdx.files.internal("keys.png"));
		text = new BitmapFont(Gdx.files.internal("data/homespun.fnt"));
		
	}
	
	private void update(float delta)
	{
		if(alpha < 0.99)
			alpha += 0.3 * delta;
		
		batch.setColor(1, 1, 1, alpha);
		text.setColor(1, 1, 1, alpha);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		batch.begin();
		batch.draw(controls, 60, 130, 512, 256);
		text.draw(batch, "Shoot", 140, 380);
		text.draw(batch, "Move", 382, 380);
		text.draw(batch, "Objective: Defend Earth", (600 - text.getBounds("Objective: Defend Earth").width)/2, 480);
		
		if(alpha >= 0.99)
		{
			text.draw(batch, "Continue [SPACE]", (600 - text.getBounds("Continue [SPACE]").width)/2, 145);
			if(Gdx.input.isKeyPressed(Keys.SPACE))
				ready = true;
		}
		
		batch.end();
	}
	
	public void reset()
	{
		alpha = 0f;
		ready = false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() { render(Gdx.graphics.getDeltaTime()); }

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
