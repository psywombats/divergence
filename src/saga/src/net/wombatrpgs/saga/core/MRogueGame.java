package net.wombatrpgs.saga.core;

import net.wombatrpgs.saga.io.FocusListener;

import com.badlogic.gdx.ApplicationListener;

public class MRogueGame implements ApplicationListener, FocusListener {
	
	private boolean paused;
	
	/**
	 * Creates a new game. Requires a few setup tools that are platform
	 * dependant. Actually they all got removed, but this is where they should
	 * go.
	 */
	public MRogueGame(Platform platform) {
		super();
		SGlobal.platform = platform;
		//focusReporter.registerListener(this);
		//this.focusReporter = focusReporter;
		paused = false;
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
		if (!paused) {			
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
		paused = true;
		SGlobal.keymap.onPause();
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		paused = false;
		SGlobal.keymap.onResume();
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
