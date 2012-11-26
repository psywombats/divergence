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
	private Map<InputButton, InputCommand> downMap;
	private Map<InputButton, InputCommand> upMap;
	
	/**
	 * Creates and initializes the default command map. Should probably only
	 * need to be created once but w/e.
	 */
	public TestCommandMap() {
		downMap = new HashMap<InputButton, InputCommand>();
		upMap = new HashMap<InputButton, InputCommand>();
		
		downMap.put(InputButton.BUTTON_1, 	InputCommand.INTENT_CONFIRM);
		downMap.put(InputButton.BUTTON_2, 	InputCommand.INTENT_CANCEL);
		downMap.put(InputButton.DOWN, 		InputCommand.MOVE_START_DOWN);
		downMap.put(InputButton.LEFT, 		InputCommand.MOVE_START_LEFT);
		downMap.put(InputButton.RIGHT, 		InputCommand.MOVE_START_RIGHT);
		downMap.put(InputButton.UP, 		InputCommand.MOVE_START_UP);
		
		upMap.put(InputButton.DOWN, 		InputCommand.MOVE_STOP_DOWN);
		upMap.put(InputButton.LEFT, 		InputCommand.MOVE_STOP_LEFT);
		upMap.put(InputButton.RIGHT, 		InputCommand.MOVE_STOP_RIGHT);
		upMap.put(InputButton.UP, 			InputCommand.MOVE_STOP_UP);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.rainfall.io.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		if (downMap.containsKey(button)) {
			this.signal(downMap.get(button));
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.rainfall.io.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		if (upMap.containsKey(button)) {
			this.signal(upMap.get(button));
		}
	}

}
