/**
 *  DefaultKeymap.java
 *  Created on Nov 23, 2012 3:33:18 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.rainfallschema.io.data.InputButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * The default keyboard bindings for Rainfall.
 */
public class DefaultKeymap extends Keymap {
	
	private Map<Integer, InputButton> map;
	private Map<InputButton, Integer> backmap;
	private Map<InputButton, Boolean> state;
	
	/**
	 * Creates and initializes the default keymap.
	 */
	public DefaultKeymap() {
		map = new HashMap<Integer, InputButton>();
		backmap = new HashMap<InputButton, Integer>();
		
		// movamant
		map.put(Keys.UP, 		InputButton.UP);
		map.put(Keys.DOWN, 		InputButton.DOWN);
		map.put(Keys.LEFT, 		InputButton.LEFT);
		map.put(Keys.RIGHT, 	InputButton.RIGHT);
		
		// buttans
		map.put(Keys.Z, 		InputButton.BUTTON_1);
		map.put(Keys.X, 		InputButton.BUTTON_2);
		map.put(Keys.C, 		InputButton.BUTTON_3);
		map.put(Keys.V, 		InputButton.BUTTON_4);
		map.put(Keys.S, 		InputButton.BUTTON_5);
		map.put(Keys.D, 		InputButton.BUTTON_6);
		
		map.put(Keys.ESCAPE, 	InputButton.MENU);
		
		for (Object key : map.keySet()) {
			backmap.put(map.get(key), (Integer) key);
		}
		
		state = new HashMap<InputButton, Boolean>();
		for (InputButton button : InputButton.values()) {
			state.put(button, false);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Keymap#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		if (map.containsKey(keycode)) {
			if (!state.get(map.get(keycode))) {
				state.put(map.get(keycode), true);
				this.signal(map.get(keycode), true);
			}
		}
		return super.keyDown(keycode);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Keymap#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		if (map.containsKey(keycode)) {
			if (state.get(map.get(keycode))) {
				state.put(map.get(keycode), false);
				this.signal(map.get(keycode), false);
			}
		}
		return super.keyUp(keycode);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Keymap#isButtonDown(net.wombatrpgs.rainfallschema.io.data.InputButton)
	 */
	@Override
	public boolean isButtonDown(InputButton button) {
		return Gdx.input.isKeyPressed(backmap.get(button));
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Keymap#replicateButtonUp
	 * (net.wombatrpgs.rainfallschema.io.data.InputButton)
	 */
	@Override
	protected void replicateButtonUp(InputButton button) {
		keyUp(backmap.get(button));
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Keymap#replicateButtonDown
	 * (net.wombatrpgs.rainfallschema.io.data.InputButton)
	 */
	@Override
	protected void replicateButtonDown(InputButton button) {
		keyDown(backmap.get(button));
	}
	
}
