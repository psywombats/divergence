/**
 *  ScreenStack.java
 *  Created on Nov 23, 2012 5:53:16 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * A bunch of screens stacked on top of each other that make up the game
 * environment. Only one should exist per game, probably in the globals
 * manager. This is the object that should be rendering every frame regardless
 * of whatever the hell else is going on.
 */
public class ScreenStack implements Disposable {
	
	private List<Screen> screens;
	
	/**
	 * Creates and initializes a new empty stack of screens.
	 */
	public ScreenStack() {
		screens = new ArrayList<Screen>();
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (Screen screen :  screens) {
			screen.dispose();
		}
	}

	/**
	 * Pushes a screen to the top of the screen stack. Corrects the z-value of
	 * the screen such that it's on the top.
	 * @param	screen		The screen to put on top
	 */
	public void push(Screen screen) {
		if (screens.size() == 0) {
			screens.add(screen);
		} else {
			Screen oldTop = screens.get(0);
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
	public Screen pop() {
		if (screens.size() == 0) {
			RGlobal.reporter.warn("No screens left in the stack, but popping.");
			return null;
		} else {
			Screen oldTop = screens.get(0);
			unfocus(oldTop);
			if (screens.size() > 0) {
				focus(screens.get(0));
			}
			return oldTop;
		}
	}
	
	/**
	 * Gets the camera being used by the topmost screen
	 * @return					The in-use camera
	 */
	public TrackerCam getCamera() {
		return screens.get(0).getCamera();
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
	 * Gets the first screen on the stack without popping it.
	 * @return					The topmost screen
	 */
	public Screen peek() {
		return screens.get(0);
	}
	
	/**
	 * Reports how many scenes are on the stack.
	 * @return					The number of screens on the stack
	 */
	public int size() {
		return screens.size();
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
	private void focus(Screen screen) {
		screen.onFocusGained();
		RGlobal.keymap.registerListener(screen.getCommandContext());
	}
	
	/**
	 * Called whenever a screen loses focus.
	 * @param 	screen			The screen that lost focus
	 */
	private void unfocus(Screen screen) {
		screen.onFocusLost();
		screens.remove(screen);
		RGlobal.keymap.unregisterListener(screen.getCommandContext());
	}

}
