/**
 *  Keymap.java
 *  Created on Nov 19, 2012 2:31:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.global.Global;

import com.badlogic.gdx.InputProcessor;

/**
 * A map from physical keyboard keys to the meta-buttons that the game runs on.
 */
public abstract class Keymap implements InputProcessor {
	
	private List<ButtonListener> listeners;
	
	/**
	 * Creates and intializes a new keymap.
	 */
	public Keymap() {
		listeners = new ArrayList<ButtonListener>();
	}
	
	/**
	 * Registers a new object to listen for meta-button presses.
	 * @param 	listener		The listener to register
	 */
	public final void registerListener(ButtonListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unregisters an existing listener from meta-button presses.
	 * @param 	listener		The listener to unregister
	 */
	public final void unregisterListener(ButtonListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			Global.reporter.warn("The listener " + listener + " is not " +
					"actually listening to " + this);
		}
	}
	
	/**
	 * Signal that a meta-button was pressed to whatever's listening for it.
	 * @param 	button		The meta-button that was pushed
	 * @param	down		True if button was pressed down, false if released
	 */
	protected final void signal(InputButton button, boolean down) {
		for (ButtonListener listener : listeners) {
			if (down) {
				listener.onButtonPressed(button);
			} else {
				listener.onButtonReleased(button);
			}
		}
	}
	
	/**
	 * Signal that a meta-button was pulsed.
	 * @param 	button		The meta-button that pulsed
	 */
	protected final void signal(InputButton button) {
		signal(button, true);
		signal(button, false);
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#touchMoved(int, int)
	 */
	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	/**
	 * Override if needed by the specific keymapping.
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	
}
