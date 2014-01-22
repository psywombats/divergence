package net.wombatrpgs.saga.core;

import net.wombatrpgs.saga.io.FocusListener;

import com.badlogic.gdx.ApplicationListener;

public class SagaGame implements ApplicationListener, FocusListener {
	
	private boolean unfocused;
	
	/**
	 * Creates a new game. Requires a few setup tools that are platform
	 * dependant. Actually they all got removed, but this is where they should
	 * go.
	 */
	public SagaGame(Platform platform) {
		super();
		SGlobal.platform = platform;
		//focusReporter.registerListener(this);
		//this.focusReporter = focusReporter;
		unfocused = false;
		// Don't you dare do anything fancy in here
	}
	
	@Override
	public void create() {
		SGlobal.globalInit();
	}

	@Override
	public void dispose() {
		SGlobal.assetManager.dispose();
		SGlobal.screens.dispose();
	}

	@Override
	public void render() {		
		//focusReporter.update();
		if (!unfocused) {			
			SGlobal.screens.render();
		}
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO: handle game resizing
		
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#pause()
	 */
	@Override
	public void pause() {
		unfocused = true;
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		unfocused = false;
	}

	/**
	 * @see net.wombatrpgs.saga.io.FocusListener#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		pause();
	}

	/**
	 * @see net.wombatrpgs.saga.io.FocusListener#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		resume();
	}

}
