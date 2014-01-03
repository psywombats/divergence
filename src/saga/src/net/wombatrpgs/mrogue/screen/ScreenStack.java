/**
 *  ScreenStack.java
 *  Created on Nov 23, 2012 5:53:16 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen;

import java.util.Stack;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Disposable;
import net.wombatrpgs.mrogue.io.ButtonListener;
import net.wombatrpgs.mrogue.io.audio.MusicObject;
import net.wombatrpgs.mrogueschema.io.data.InputButton;

/**
 * A bunch of screens stacked on top of each other that make up the game
 * environment. Only one should exist per game, probably in the globals
 * manager. This is the object that should be rendering every frame regardless
 * of whatever the hell else is going on.
 */
public class ScreenStack implements	Disposable,
									ButtonListener {
	
	private Stack<Screen> screens;
	protected MusicObject current, fadeOut;
	
	/**
	 * Creates and initializes a new empty stack of screens.
	 */
	public ScreenStack() {
		screens = new Stack<Screen>();
		MGlobal.keymap.registerListener(this);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (Screen screen :  screens) {
			screen.dispose();
		}
		screens.clear();
	}

	/**
	 * Pushes a screen to the top of the screen stack. Corrects the z-value of
	 * the screen such that it's on the top.
	 * @param	screen		The screen to put on top
	 */
	public void push(Screen screen) {
		screens.push(screen);
		if (screens.size() > 0) {
			screens.peek().onFocusLost();
		}
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
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		peek().onButtonPressed(button);
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		peek().onButtonReleased(button);
	}

	/**
	 * Renders the top screen on the stack.
	 * @param	camera			The camera to render with
	 */
	public void render() {
		if (screens.size() == 0) {
			MGlobal.reporter.warn("No screens in stack, but told to render");
		} else {
			try {
				screens.get(0).render();
			} catch (Exception e) {
				MGlobal.reporter.err("Exception during render: ", e);
				Gdx.app.exit();
			}
		}
		try {
			update();
		} catch (Exception e) {
			MGlobal.reporter.err("Exception during render: ", e);
			Gdx.app.exit();
		}
	}
	
	// TODO: might want to make this affect /all/ the screens
	/**
	 * Updates all objects in the screen.
	 */
	public void update() {
		float elapsed = Gdx.graphics.getDeltaTime();
//		float real = 1.0f / elapsed;
//		if (real < RGlobal.constants.rate()) {
//			elapsed = (1.0f / RGlobal.constants.rate());
//		}
		screens.get(0).update(elapsed);
		if (current != null) current.update(elapsed);
		if (fadeOut != null) fadeOut.update(elapsed);
	}
	
	// TODO: delete this, it's allowing for ugly shit that'll suck w/ 2+ screens
	/**
	 * Gets the first screen on the stack without popping it.
	 * @return					The topmost screen
	 */
	public Screen peek() {
		return screens.peek();
	}
	
	/**
	 * Reports how many scenes are on the stack.
	 * @return					The number of screens on the stack
	 */
	public int size() {
		return screens.size();
	}
	
	/**
	 * Will update music to play! Because before there were way too many bugs.
	 * @param	music			The music to play
	 * @param	immediate		True to instaplay, false otherwise
	 */
	public void playMusic(MusicObject music, boolean immediate) {
		if (fadeOut != null) {
			fadeOut.stop();
		}
		fadeOut = current;
		if (current != null) {
			if (immediate) current.stop();
			else current.fadeOut(.5f);
		}
		current = music;
		if (music != null) {
			if (immediate) music.play();
			else music.fadeIn(.5f);
		}
	}

}
