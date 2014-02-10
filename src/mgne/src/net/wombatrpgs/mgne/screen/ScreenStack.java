/**
 *  ScreenStack.java
 *  Created on Nov 23, 2012 5:53:16 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import java.util.Stack;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.mgne.core.SGlobal;
import net.wombatrpgs.mgne.graphics.Disposable;
import net.wombatrpgs.mgne.io.ButtonListener;
import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.audio.MusicObject;

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
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Disposable#dispose()
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
			SGlobal.reporter.warn("No screens left in the stack, but popping.");
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

	/**
	 * @see net.wombatrpgs.mgne.io.ButtonListener#onEvent
	 * (net.wombatrpgs.mgne.io.InputEvent)
	 */
	@Override
	public void onEvent(InputEvent event) {
		peek().onEvent(event);
	}

	/**
	 * Renders the top screen on the stack.
	 * @param	camera			The camera to render with
	 */
	public void render() {
		if (screens.size() == 0) {
			SGlobal.reporter.warn("No screens in stack, but told to render");
		} else {
			try {
				screens.get(0).render();
			} catch (Exception e) {
				SGlobal.reporter.err("Exception during render: ", e);
				Gdx.app.exit();
			}
		}
		try {
			update();
		} catch (Exception e) {
			SGlobal.reporter.err("Exception during update: ", e);
			Gdx.app.exit();
		}
	}
	
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
