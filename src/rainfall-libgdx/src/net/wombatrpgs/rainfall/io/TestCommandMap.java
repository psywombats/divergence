/**
 *  DefaultCommandMap.java
 *  Created on Nov 23, 2012 3:51:04 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.HashMap;
import java.util.Map;

/**
 * The default mapping of virtual keys to commands. This is a test map,
 * basically, and command maps probably need to be swapped in and out depending
 * on context.
 */
public class TestCommandMap extends CommandMap {
	
	// stopgap solution - every key is mapped to one command
	private Map<InputButton, InputCommand> map;
	
	/**
	 * Creates and initializes the default command map. Should probably only
	 * need to be created once but w/e.
	 */
	public TestCommandMap() {
		map = new HashMap<InputButton, InputCommand>();
		
		map.put(InputButton.BUTTON_1, 	InputCommand.INTENT_CONFIRM);
		map.put(InputButton.BUTTON_2, 	InputCommand.INTENT_CANCEL);
		map.put(InputButton.DOWN, 		InputCommand.MOVE_DOWN);
		map.put(InputButton.LEFT, 		InputCommand.MOVE_LEFT);
		map.put(InputButton.RIGHT, 		InputCommand.MOVE_RIGHT);
		map.put(InputButton.UP, 		InputCommand.MOVE_UP);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.rainfall.io.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		if (map.containsKey(button)) {
			this.signal(map.get(button));
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.rainfall.io.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		// derpdederr nothing for now
	}

}
