package net.wombatrpgs.rainfall.core;

import net.wombatrpgs.rainfall.io.FocusListener;
import net.wombatrpgs.rainfall.io.FocusReporter;

import com.badlogic.gdx.ApplicationListener;

public class RainfallGame implements ApplicationListener, FocusListener {
	
	private FocusReporter focusReporter;
	private boolean paused;
	
	/**
	 * Creates a new game. Requires a few setup tools that are platform
	 * dependant.
	 * @param 	focusReporter		The thing that tells if focus is lost
	 */
	public RainfallGame(FocusReporter focusReporter) {
		super();
		focusReporter.registerListener(this);
		this.focusReporter = focusReporter;
		paused = false;
		// Don't you dare do anything fancy in here
	}
	
	@Override
	public void create() {
		RGlobal.globalInit();
	}

	@Override
	public void dispose() {
		RGlobal.assetManager.dispose();
		RGlobal.screens.dispose();
	}

	@Override
	public void render() {		
		focusReporter.update();
		if (!paused) {			
			RGlobal.screens.render();
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
		RGlobal.keymap.onPause();
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		paused = false;
		RGlobal.keymap.onResume();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.FocusListener#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		pause();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.FocusListener#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		resume();
	}

}
