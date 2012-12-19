/**
 *  Keymap.java
 *  Created on Nov 19, 2012 2:31:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfallschema.io.data.InputButton;

import com.badlogic.gdx.InputProcessor;

/**
 * A map from physical keyboard keys to the meta-buttons that the game runs on.
 */
public abstract class Keymap implements InputProcessor {
	
	private List<ButtonListener> listeners;
	@SuppressWarnings("unused")
	private Map<InputButton, Boolean> snapshot;
	
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
	 * Called by the game when focus is lost. Fires events that should've
	 * happened in the meantime.
	 */
	public final void onPause() {
		snapshot = takeSnapshot();
	}
	
	/**
	 * Called by the game when focus is regained. Fires events that should've
	 * happened in the meantime.
	 */
	public final void onResume() {
//		Map<InputButton, Boolean> newState = takeSnapshot();
//		for (InputButton button : InputButton.values()) {
//			if (newState.get(button) && !snapshot.get(button)) {
//				replicateButtonDown(button);
//			}
//			if (!newState.get(button) && snapshot.get(button)) {
//				replicateButtonUp(button);
//			}
//		}
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
	 * Takes a snapshot of the current button state. Button's state is on or
	 * off. This is useful when focus is lost.
	 * @return					A mapping from buttons to true if pressed
	 */
	protected final Map<InputButton, Boolean> takeSnapshot() {
		Map<InputButton, Boolean> snapshot = new HashMap<InputButton, Boolean>();
		for (InputButton button : InputButton.values()) {
			snapshot.put(button, isButtonDown(button));
			if (isButtonDown(button)) {
				System.out.println(button + " was down");
			}
		}
		return snapshot;
	}
	
	/**
	 * Polling input method. Returns true if the input is currently down. In the
	 * case of buttons that fire in pulses, it's safe to always return false.
	 * @param 	button			The button to check if down
	 * @return					True if that button is down, false otherwise
	 */
	public abstract boolean isButtonDown(InputButton button);
	
	/**
	 * Fire whatever would've happened to cause this button to come up. It
	 * probably happened while focus was lost.
	 * @param 	button			The button whose state changed while paused
	 */
	protected abstract void replicateButtonUp(InputButton button);
	
	/**
	 * Fire whatever would've happened to cause this button to come down. It
	 * probably happened while focus was lost.
	 * @param 	button			The button whose state changed while paused
	 */
	protected abstract void replicateButtonDown(InputButton button);

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
