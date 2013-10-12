/**
 *  DefaultCommandMap.java
 *  Created on Nov 23, 2012 3:51:04 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mrogueschema.io.data.InputButton;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

/**
 * The default mapping of virtual keys to commands. This is a test map,
 * basically, and command maps probably need to be swapped in and out depending
 * on context.
 */
public class TestCommandMap extends CommandMap {
	
	// stopgap solution - every key is mapped to one command
	private Map<InputButton, InputCommand> downMap;
	private Map<InputButton, InputCommand> upMap;
	
	private List<InputButton> pressed;
	
	/**
	 * Creates and initializes the default command map. Should probably only
	 * need to be created once but w/e.
	 */
	public TestCommandMap() {
		downMap = new HashMap<InputButton, InputCommand>();
		upMap = new HashMap<InputButton, InputCommand>();
		pressed = new ArrayList<InputButton>();
		
		downMap.put(InputButton.UP, 		InputCommand.MOVE_NORTH);
		downMap.put(InputButton.RIGHT, 		InputCommand.MOVE_EAST);
		downMap.put(InputButton.DOWN, 		InputCommand.MOVE_SOUTH);
		downMap.put(InputButton.LEFT, 		InputCommand.MOVE_WEST);
		
		downMap.put(InputButton.DIR_N, 		InputCommand.MOVE_NORTH);
		downMap.put(InputButton.DIR_NE, 	InputCommand.MOVE_NORTHEAST);
		downMap.put(InputButton.DIR_E, 		InputCommand.MOVE_EAST);
		downMap.put(InputButton.DIR_SE, 	InputCommand.MOVE_SOUTHEAST);
		downMap.put(InputButton.DIR_S, 		InputCommand.MOVE_SOUTH);
		downMap.put(InputButton.DIR_SW, 	InputCommand.MOVE_SOUTHWEST);
		downMap.put(InputButton.DIR_W, 		InputCommand.MOVE_WEST);
		downMap.put(InputButton.DIR_NW, 	InputCommand.MOVE_NORTHWEST);
		downMap.put(InputButton.WAIT,		InputCommand.MOVE_WAIT);
		
		downMap.put(InputButton.MENU,		InputCommand.INTENT_EXIT);
		downMap.put(InputButton.FULLSCREEN,	InputCommand.INTENT_FULLSCREEN);
		
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		if (downMap.containsKey(button)) {
			this.signal(downMap.get(button));
			pressed.add(button);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		if (!pressed.contains(button)) return;
		pressed.remove(button);
		if (upMap.containsKey(button)) {
			this.signal(upMap.get(button));
		}
	}

}
