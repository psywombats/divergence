/**
 *  Keymap.java
 *  Created on Nov 19, 2012 2:31:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.SGlobal;
import net.wombatrpgs.mgne.core.Updateable;
import net.wombatrpgs.mgne.io.InputEvent.EventType;
import net.wombatrpgs.mgne.screen.ScreenStack;
import net.wombatrpgs.mgneschema.io.KeymapMDO;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.KeyButtonPairMDO;
import net.wombatrpgs.mgneschema.ui.InputSettingsMDO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * A map from physical keyboard keys to the meta-buttons that the game runs on.
 * 
 * As of 2013-01-31, constantly sends the down event as long as the key is
 * held down. This may lead to some weird issues, but it's much better than the
 * mandatory 1:1 mapping alternative.
 * 
 * Changed on 2014-01-21, contains a lot more functionality for key repeat.
 */
public class Keymap implements	InputProcessor,
								Updateable {
	
	protected KeymapMDO mdo;
	
	protected List<ButtonListener> listeners;
	protected List<InputEvent> queue;
	protected Map<InputButton, KeyState> states;
	protected Map<Integer, InputButton> keyToButton;
	protected Map<InputButton, Integer> buttonToKey;
	
	/**
	 * Creates and intializes a new keymap.
	 */
	public Keymap(KeymapMDO mdo) {
		this.mdo = mdo;
		queue = new ArrayList<InputEvent>();
		listeners = new ArrayList<ButtonListener>();
		states = new HashMap<InputButton, KeyState>();
		keyToButton = new HashMap<Integer, InputButton>();
		buttonToKey = new HashMap<InputButton, Integer>();
		
		for (KeyButtonPairMDO pairMDO : mdo.bindings) {
			keyToButton.put(pairMDO.keyCode.keycode, pairMDO.button);
			buttonToKey.put(pairMDO.button, pairMDO.keyCode.keycode);
		}
		for (InputButton button : InputButton.values()) {
			states.put(button, KeyState.UP);
		}
	}
	
	/**
	 * Creates the default keymap by checking the database for the MDO as
	 * defined in Constants. Should be called from SGlobal.
	 * @return					The created keymap
	 */
	public static Keymap initDefaultKeymap() {
		InputSettingsMDO inputMDO = SGlobal.data.getEntryFor(Constants.KEY_INPUT, InputSettingsMDO.class);
		KeymapMDO keyMDO = SGlobal.data.getEntryFor(inputMDO.keymap, KeymapMDO.class);
		Keymap map = new Keymap(keyMDO);
		map.registerListener(SGlobal.screens);
		Gdx.input.setInputProcessor(map);
		return map;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		for (InputButton button : InputButton.values()) {
			if (states.get(button) == KeyState.DOWN) {
				if (!Gdx.input.isKeyPressed(buttonToKey.get(button))) {
					// this is to prevent loss-of-focus getting state unsync'd
					states.put(button, KeyState.UP);
					queue.add(new InputEvent(button, EventType.RELEASE));
				} else {
					queue.add(new InputEvent(button, EventType.HOLD));
				}
			}
		}
		for (InputEvent event : queue) {
			signal(event);
		}
		queue.clear();
	}

	/**
	 * Registers a new object to listen for meta-button presses.
	 * @param 	listener		The listener to register
	 */
	public  void registerListener(ScreenStack listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unregisters an existing listener from meta-button presses.
	 * @param 	listener		The listener to unregister
	 */
	public void unregisterListener(ScreenStack listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			SGlobal.reporter.warn("The listener " + listener + " is not " +
					"actually listening to " + this);
		}
	}
	
	/**
	 * Gets the state of a specific input button. This should really only be
	 * called by the command map as part of a reverse-mapping for very specific
	 * polling situations.
	 * @param	button			The button to fetch state
	 * @return					The current state of that button's key
	 */
	public KeyState getButtonState(InputButton button) {
		return states.get(button);
	}

	/**
	 * We'll handle the keybindings here.
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		InputButton button = keyToButton.get(keycode);
		if (button == null) return false;
		if (states.get(button) == KeyState.UP) {
			queue.add(new InputEvent(button, EventType.PRESS));
			queue.add(new InputEvent(button, EventType.HOLD));
		}
		states.put(button, KeyState.DOWN);
		return true;
	}

	/**
	 * We'll handle the keybindings here.
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		// this will be handled in the update
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
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	/**
	 * Override if needed etc etc
	 * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	/**
	 * Signal that a meta-button event (press etc) occurred. Construct the event
	 * yourself in the raw input handling.
	 * @param 	event			The event that occurred
	 */
	protected final void signal(InputEvent event) {
		for (ButtonListener listener : listeners) {
			listener.onEvent(event);
		}
	}
	
	/**
	 * Short thing to keep track of buttons.
	 */
	public enum KeyState {
		DOWN,
		UP,
	}
	
}
