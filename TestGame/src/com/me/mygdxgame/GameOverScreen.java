package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {

	private SpriteBatch batch;
	private BitmapFont text;
	public boolean ready = false;
	
	// these all affect text
	private float alpha = 0f;
	private int pCompleted = 0;
	private boolean canSkip = true;

	// actual text
	private static String p1 = "   As fire continues to rain down from the heavens, Earth finally\n" +
			"succumbs to the heat. Miles above her atmosphere, a final\n" +
			"transmission echoes through your cockpit: \"Thank you.\" Then\n" +
			"silence. Any tears you might have shed had been dried long ago.\n" +
			"No one expected a victory here, not even you. No, all that was\n" +
			"expected was time - time to say goodbyes, time to make\n" +
			"amends.\n";
	private static String p2 = "   It is amazing how quickly our differences fade away in the\n" +
			"face of utter extinction. In the weeks prior to Judgement, entire\n" +
			"nations ceased fighting to work together. As the petty barriers\n" +
			"of race, religion, and government faded away, the truth\n" +
			"became clear: we are all here to help each other. It was a\n" +
			"truth that came far too late, but a truth that will be... must\n" +
			"be... remembered.\n";
	private static String p3 = "   You press a button to begin video playback of humanity's last\n" +
			"stand, letting it loop indefinitely on what remains of your\n" +
			"ship's power. One by one you shut down all systems not needed\n" +
			"to operate the monitor, until only life support remains. You\n" +
			"gaze one last time at the scorched planet that used to be your\n" +
			"home, then reach up to flip the final switch. You close your\n" +
			"eyes and sigh as you slowly drift into the aether. It is a\n" +
			"peaceful death, not unlike falling asleep after a long, weary day.\n" +
			"The sterile cockpit preserves your smile for eternity.\n";
	private static String p4 = "   Powered by the sun's rays, your monitor continues its mission\n" +
			"for millions of years, relaying the single heritage of a long\n" +
			"dead race. Its message echoes through space, searching for\n" +
			"anyone who will listen. If it ever finds itself among willing ears,\n" +
			"they will know us. Not as a people divided by war and history, but\n" +
			"as a people who stood as one in their darkest hour. And, united\n" +
			"in our resolve, a people who stood proud against the very\n" +
			"universe and died with sword in hand. No, the human race did not\n" +
			"die today - the stars will know our history.";


	public GameOverScreen(SpriteBatch batch)
	{
		this.batch = batch;
		text = new BitmapFont(Gdx.files.internal("data/homespun.fnt"));
		text.setColor(1f, 1f, 1f, 1f);
		text.setScale(0.6f);
	}

	private void update(float delta)
	{
		if(alpha < 0.99f)
			alpha += 0.05f * delta;
		else {
			alpha = 0f;
			pCompleted++;
		}
	}
	
	@Override
	public void render(float delta) 
	{
		update(delta);
		
		// clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.enableBlending();
		batch.begin();
		switch(pCompleted) // this seems like a lot of work to render text dramatically...
		{
		case 0: // fade in p1
			text.setColor(1f, 1f, 1f, alpha);
			text.drawMultiLine(batch, p1, 5, 545);
			checkSkip();
			break;
		case 1: // keep p1, fade in p2
			text.setColor(1f, 1f, 1f, 1f);
			text.drawMultiLine(batch, p1, 5, 545);
			text.setColor(1f, 1f, 1f, alpha);
			text.drawMultiLine(batch, p2, 5, 395);
			checkSkip();
			break;
		case 2: // keep p1 and p2, fade in p3
			text.setColor(1f, 1f, 1f, 1f);
			text.drawMultiLine(batch, p1, 5, 545);
			text.drawMultiLine(batch, p2, 5, 395);
			text.setColor(1f, 1f, 1f, alpha);
			text.drawMultiLine(batch, p3, 5, 245);
			checkSkip();
			break;
		case 3: // keep p1, p2, and p3
			text.setColor(1f, 1f, 1f, 1f);
			text.drawMultiLine(batch, p1, 5, 545);
			text.drawMultiLine(batch, p2, 5, 395);
			text.drawMultiLine(batch, p3, 5, 245);
			checkSkip();
			break;
		case 4: // fade out p1, p2, and p3
			alpha += 0.05f * delta; // this fade should be faster than the others
			text.setColor(1f, 1f, 1f, 1f - alpha);
			text.drawMultiLine(batch, p1, 5, 545);
			text.drawMultiLine(batch, p2, 5, 395);
			text.drawMultiLine(batch, p3, 5, 245);
			checkSkip();
			break;
		case 5: // fade in p4
			text.setColor(1f, 1f, 1f, alpha);
			text.drawMultiLine(batch, p4, 5, 395);
			checkSkip();
			break;
		case 6: // keep p4
			text.setColor(1f, 1f, 1f, 1f);
			text.drawMultiLine(batch, p4, 5, 395);
			checkSkip();
			break;
		case 7: // fade out p4
			text.setColor(1f, 1f, 1f, 1f - alpha);
			text.drawMultiLine(batch, p4, 5, 395);
			break;
		default: // back to menu
			text.setColor(1f, 1f, 1f, 0);
			text.drawMultiLine(batch, p4, 5, 395);
			ready = true;
			break;
		}
		batch.end();
	}

	// check if player is trying to skip text (even though I worked really hard on it Q.Q)
	private void checkSkip()
	{
		if(Gdx.input.isKeyPressed(Keys.SPACE) && canSkip && alpha > 0.02f) {
			pCompleted++;
			alpha = 0f;
			canSkip = false;
		}
		else if(!Gdx.input.isKeyPressed(Keys.SPACE))
			canSkip = true;
	}

	public void reset()
	{
		ready = false;
		canSkip = false;
		pCompleted = 0;
		alpha = 0f;
	}

	@Override
	public void show() { render(Gdx.graphics.getDeltaTime()); }

	@Override
	public void dispose() { text.dispose(); }
	
	@Override
	public void resize(int width, int height) { }

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }
}
