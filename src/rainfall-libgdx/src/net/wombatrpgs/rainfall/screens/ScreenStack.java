/**
 *  ScreenStack.java
 *  Created on Nov 23, 2012 5:53:16 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * A bunch of screens stacked on top of each other that make up the game
 * environment. Only one should exist per game, probably in the globals
 * manager. This is the object that should be rendering every frame regardless
 * of whatever the hell else is going on.
 */
public class ScreenStack {
	
	private List<GameScreen> screens;
	private GameScreen levelScreen;
	
	/**
	 * Creates and initializes a new empty stack of screens.
	 */
	public ScreenStack() {
		screens = new ArrayList<GameScreen>();
	}
	
	/**
	 * Pushes a screen to the top of the screen stack. Corrects the z-value of
	 * the screen such that it's on the top.
	 * @param	screen		The screen to put on top
	 */
	public void push(GameScreen screen) {
		if (screens.size() == 0) {
			screens.add(screen);
		} else {
			GameScreen oldTop = screens.get(0);
			screen.setZ(oldTop.z - 1);
			screens.add(0, screen);
			sortStack();
		}
		focus(screen);
	}
	
	/**
	 * Removes the top screen from the stack. Does not alter z-values.
	 * @return				The screen evicted from the top
	 */
	public GameScreen pop() {
		if (screens.size() == 0) {
			RGlobal.reporter.warn("No screens left in the stack, but popping.");
			return null;
		} else {
			GameScreen oldTop = screens.get(0);
			unfocus(oldTop);
			if (screens.size() > 0) {
				focus(screens.get(0));
			}
			return oldTop;
		}
	}
	
//	/**
//	 * Inserts a screen before another on the stack. Adjusts the z-values
//	 * accordingly.
//	 * @param 	toInsert	The screen to insert
//	 * @param 	reference	The screen to insert before
//	 */
//	public void insertBefore(GameScreen toInsert, GameScreen reference) {
//		if (!screens.contains(reference)) {
//			Global.reporter.warn("Stack did not contain " + reference);
//		} else {
//			float beforeZ = reference.getZ();
//			int beforeIndex = screens.indexOf(reference);
//			if (beforeIndex == screens.size() - 1) {
//				// this item is the last on the stack
//			} else {
//				float afterZ = screens.get(beforeIndex + 1).getZ();
//				toInsert.setZ((afterZ+beforeZ) / 2);
//				screens.add(beforeIndex, toInsert);
//				sortStack();
//			}
//		}
//	}
	
	/**
	 * Renders the top screen on the stack.
	 * @param	camera			The camera to render with
	 */
	public void render() {
		if (screens.size() == 0) {
			RGlobal.reporter.warn("No screens in stack, but told to render");
		} else {
			screens.get(0).render();
		}
		update();
	}
	
	// TODO: might want to make this affect /all/ the screens
	/**
	 * Updates all objects in the screen.
	 */
	public void update() {
		float elapsed = Gdx.graphics.getDeltaTime();
		float real = 1.0f / elapsed;
		if (real < RGlobal.constants.rate()) {
			elapsed = (1.0f / RGlobal.constants.rate());
		}
		screens.get(0).update(elapsed);
	}
	
	/**
	 * Set a screen as the screen for levels. This means when teleportation
	 * occurs, this screen will get wiped. The previous screen will be replaced.
	 * @param 	screen			The screen to register
	 */
	public void registerLevelScreen(GameScreen screen) {
		levelScreen = screen;
	}
	
	/**
	 * Gets the screen previously registered as the level carrier.
	 * @return					The screen prevouisly registered
	 */
	public GameScreen getLevelScreen() {
		return levelScreen;
	}
	
	/**
	 * Sorts the stack of game screens by their z-value.
	 */
	private void sortStack() {
		Collections.sort(screens);
	}
	
	/**
	 * Called whenever a screen gains focus.
	 * @param	 screen			The screen that gained focus
	 */
	private void focus(GameScreen screen) {
		screen.onFocusGained();
		RGlobal.keymap.registerListener(screen.getCommandContext());
	}
	
	/**
	 * Called whenever a screen loses focus.
	 * @param 	screen			The screen that lost focus
	 */
	private void unfocus(GameScreen screen) {
		screen.onFocusLost();
		screens.remove(screen);
		RGlobal.keymap.unregisterListener(screen.getCommandContext());
	}

}
