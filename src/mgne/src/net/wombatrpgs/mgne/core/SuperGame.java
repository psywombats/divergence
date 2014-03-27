package net.wombatrpgs.mgne.core;

import net.wombatrpgs.mgne.io.FocusListener;

import com.badlogic.gdx.ApplicationListener;

/**
 * The workhorse of the engine! Actually a runt. This is a super lightweight
 * application listener that calls global init and then listens for events.
 */
public class SuperGame implements ApplicationListener, FocusListener {
	
	/**
	 * Creates a new game. Requires a few setup tools that are platform
	 * dependant. Actually they all got removed, but this is where they should
	 * go.
	 * @param	game			Uh, the game we're gonna play?
	 * @param	platform		The platform we're running on (eg, Desktop)
	 */
	public SuperGame(MgnGame game, Platform platform) {
		super();
		MGlobal.platform = platform;
		MGlobal.game = game;
		// Don't you dare do anything fancy in here
	}
	
	@Override
	public void create() {
		MGlobal.globalInit();
		MGlobal.game.onCreate();
	}

	@Override
	public void dispose() {
		MGlobal.assets.dispose();
		MGlobal.screens.dispose();
	}

	@Override
	public void render() {
		MGlobal.screens.render();
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// I am like 100% sure this never happens
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#pause()
	 */
	@Override
	public void pause() {
		
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		
	}

	/**
	 * @see net.wombatrpgs.mgne.io.FocusListener#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		pause();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.FocusListener#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		resume();
	}

}
