/**
 *  ScreenStack.java
 *  Created on Nov 23, 2012 5:53:16 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.ButtonListener;
import net.wombatrpgs.mgne.io.InputEvent;

/**
 * A bunch of screens stacked on top of each other that make up the game
 * environment. Only one should exist per game, probably in the globals
 * manager. This is the object that should be rendering every frame regardless
 * of whatever the hell else is going on.
 */
public class ScreenStack implements	Disposable,
									ButtonListener,
									Queueable {
	
	private Stack<Screen> screens;
	
	/**
	 * Creates and initializes a new empty stack of screens.
	 */
	public ScreenStack() {
		screens = new Stack<Screen>();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (Screen screen :  screens) {
			screen.dispose();
		}
		screens.clear();
		MGlobal.keymap.unregisterListener(this);
	}

	/**
	 * Pushes a screen to the top of the screen stack. Corrects the z-value of
	 * the screen such that it's on the top.
	 * @param	screen		The screen to put on top
	 */
	public void push(Screen screen) {
		if (screens.size() > 0) {
			screens.peek().onFocusLost();
		}
		screens.push(screen);
		screen.onFocusGained();
	}
	
	/**
	 * Removes the top screen from the stack. Does not alter z-values.
	 * @return				The screen evicted from the top
	 */
	public Screen pop() {
		if (screens.size() == 0) {
			MGlobal.reporter.warn("No screens left in the stack, but popping.");
			return null;
		}
		Screen oldTop = screens.pop();
		oldTop.onFocusLost();
		peek().onFocusGained();
		return oldTop;
	}
	
	/**
	 * Resets like it's a new game.
	 */
	public void reset() {
		for (Screen s : screens) {
			s.dispose();
		}
		screens.clear();
	}
	
	/**
	 * Gets the camera being used by the topmost screen
	 * @return					The in-use camera
	 */
	public TrackerCam getCamera() {
		return screens.get(0).getCamera();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.ButtonListener#onEvent
	 * (net.wombatrpgs.mgne.io.InputEvent)
	 */
	@Override
	public void onEvent(InputEvent event) {
		peek().onEvent(event);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		for (Screen screen : screens) {
			screen.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		for (Screen screen : screens) {
			screen.postProcessing(manager, pass);
		}
	}

	/**
	 * Renders the top screen on the stack.
	 */
	public void render() {
		if (screens.size() == 0) {
			MGlobal.reporter.warn("No screens in stack, but told to render");
		} else {
			screens.peek().render();
			update();
		}
	}
	
	/**
	 * Updates all objects in the screen.
	 */
	public void update() {
		
		try {
			// I shouldn't have to be the one to call this, libgdx...
			AnimatedTiledMapTile.updateAnimationBaseTime();
			
			float elapsed = Gdx.graphics.getDeltaTime();
			MGlobal.keymap.update(elapsed);
			if (screens.size() > 0) {
				// this check is to ensure the stack wasn't removed from under us
				screens.peek().update(elapsed);
			}
			
			MGlobal.audio.update(elapsed);
		} catch (Exception e) {
			MGlobal.reporter.err(e);
		}
	}
	
	/**
	 * Reports how many scenes are on the stack.
	 * @return					The number of screens on the stack
	 */
	public int size() {
		return screens.size();
	}
	
	/**
	 * Gets the first screen on the stack without popping it. This should be
	 * used /very/ sparingly, don't assume the screen you want is the screen
	 * you're getting.
	 * @return					The topmost screen
	 */
	public Screen peek() {
		return screens.peek();
	}

}
