/**
 *  DefaultKeymap.java
 *  Created on Nov 23, 2012 3:33:18 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mrogueschema.io.data.InputButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

// TODO: make this class a database entry
/**
 * The default keyboard bindings for Rainfall.
 */
public class DefaultKeymap extends Keymap {
	
	private Map<Integer, InputButton> map;
	private Map<InputButton, Integer> backmap;
	private Map<InputButton, PressState> state;
	private List<InputButton> constantButtons;
	
	/**
	 * Creates and initializes the default keymap.
	 */
	public DefaultKeymap() {
		map = new HashMap<Integer, InputButton>();
		backmap = new HashMap<InputButton, Integer>();
		constantButtons = new ArrayList<InputButton>();
		
		// movamant
		map.put(Keys.UP, 		InputButton.UP);
		map.put(Keys.DOWN, 		InputButton.DOWN);
		map.put(Keys.LEFT, 		InputButton.LEFT);
		map.put(Keys.RIGHT, 	InputButton.RIGHT);
		
		map.put(Keys.NUM_1,		InputButton.DIR_SW);
		map.put(Keys.NUM_2,		InputButton.DIR_S);
		map.put(Keys.NUM_3,		InputButton.DIR_SE);
		map.put(Keys.NUM_4,		InputButton.DIR_W);
		map.put(Keys.NUM_5,		InputButton.WAIT);
		map.put(Keys.NUM_6,		InputButton.DIR_E);
		map.put(Keys.NUM_7,		InputButton.DIR_NW);
		map.put(Keys.NUM_8,		InputButton.DIR_N);
		map.put(Keys.NUM_9,		InputButton.DIR_NE);
		
		// buttans
		map.put(Keys.Z, 		InputButton.BUTTON_1);
		map.put(Keys.SPACE, 	InputButton.BUTTON_1);
		map.put(Keys.ENTER, 	InputButton.BUTTON_1);
		map.put(Keys.X, 		InputButton.BUTTON_2);
		map.put(Keys.BACKSPACE,	InputButton.BUTTON_2);
		map.put(Keys.C, 		InputButton.BUTTON_3);
		map.put(Keys.V, 		InputButton.BUTTON_4);
		map.put(Keys.S, 		InputButton.BUTTON_5);
		map.put(Keys.D, 		InputButton.BUTTON_6);
		
		// etc
		map.put(Keys.L,			InputButton.LOOK);
		
		// attax
		map.put(Keys.F1,		InputButton.ABIL_1);
		map.put(Keys.F2,		InputButton.ABIL_2);
		map.put(Keys.F3,		InputButton.ABIL_3);
		map.put(Keys.F4,		InputButton.ABIL_4);
		map.put(Keys.F5,		InputButton.ABIL_5);
		map.put(Keys.F6,		InputButton.ABIL_6);
		
		map.put(Keys.ESCAPE, 	InputButton.MENU);
		map.put(Keys.F12,		InputButton.FULLSCREEN);
		map.put(Keys.TAB,		InputButton.TAB);
		
		for (Object key : map.keySet()) {
			backmap.put(map.get(key), (Integer) key);
		}
		
		state = new HashMap<InputButton, PressState>();
		for (InputButton button : InputButton.values()) {
			state.put(button, new PressState(false));
		}
		
		constantButtons.add(InputButton.RIGHT);
		constantButtons.add(InputButton.UP);
		constantButtons.add(InputButton.LEFT);
		constantButtons.add(InputButton.DOWN);
		
		constantButtons.add(InputButton.DIR_N);
		constantButtons.add(InputButton.DIR_NE);
		constantButtons.add(InputButton.DIR_E);
		constantButtons.add(InputButton.DIR_SE);
		constantButtons.add(InputButton.DIR_S);
		constantButtons.add(InputButton.DIR_SW);
		constantButtons.add(InputButton.DIR_W);
		constantButtons.add(InputButton.DIR_NW);
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.Keymap#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		if (map.containsKey(keycode)) {
			if (!state.get(map.get(keycode)).down) {
				state.put(map.get(keycode), new PressState(true));
				this.signal(map.get(keycode), true);
			}
		}
		return super.keyDown(keycode);
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.Keymap#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		if (map.containsKey(keycode)) {
			if (state.get(map.get(keycode)).down) {
				state.put(map.get(keycode), new PressState(false));
				this.signal(map.get(keycode), false);
			}
		}
		return super.keyUp(keycode);
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.Keymap#isButtonDown
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public boolean isButtonDown(InputButton button) {
		if (backmap.containsKey(button)) {
			return Gdx.input.isKeyPressed(backmap.get(button));
		}
		return false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// awful keyrepeat
		for (InputButton button : constantButtons) {
			state.get(button).lastPushed += elapsed;
			if (state.get(button).down && state.get(button).lastPushed > .3f) {
				signal(button, true);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.Keymap#replicateButtonUp
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	protected void replicateButtonUp(InputButton button) {
		keyUp(backmap.get(button));
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.Keymap#replicateButtonDown
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	protected void replicateButtonDown(InputButton button) {
		keyDown(backmap.get(button));
	}

	/**
	 * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	private class PressState {
		public boolean down;
		public float lastPushed;
		public PressState(boolean down) {
			this.down = down;
			this.lastPushed = 0;
		}
	}
	
}
