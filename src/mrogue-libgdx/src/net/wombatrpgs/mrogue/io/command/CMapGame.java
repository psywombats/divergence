/**
 *  DefaultCommandMap.java
 *  Created on Nov 23, 2012 3:51:04 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io.command;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mrogue.io.CommandMap;
import net.wombatrpgs.mrogueschema.io.data.InputButton;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

/**
 * The default mapping of virtual keys to commands. This is a test map,
 * basically, and command maps probably need to be swapped in and out depending
 * on context.
 */
public class CMapGame extends CommandMap {
	
	// stopgap solution - every key is mapped to one command
	private Map<InputButton, InputCommand> downMap;
	private Map<InputButton, InputCommand> upMap;
	
	/**
	 * Creates and initializes the default command map. Should probably only
	 * need to be created once but w/e.
	 */
	public CMapGame() {
		downMap = new HashMap<InputButton, InputCommand>();
		upMap = new HashMap<InputButton, InputCommand>();
		
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
		
		downMap.put(InputButton.ABIL_1,		InputCommand.ABIL_1);
		downMap.put(InputButton.ABIL_2,		InputCommand.ABIL_2);
		downMap.put(InputButton.ABIL_3,		InputCommand.ABIL_3);
		downMap.put(InputButton.ABIL_4,		InputCommand.ABIL_4);
		downMap.put(InputButton.ABIL_5,		InputCommand.ABIL_5);
		downMap.put(InputButton.ABIL_6,		InputCommand.ABIL_6);
		
		downMap.put(InputButton.MENU,		InputCommand.INTENT_QUIT);
		downMap.put(InputButton.FULLSCREEN,	InputCommand.INTENT_FULLSCREEN);
		downMap.put(InputButton.TAB,		InputCommand.INTENT_INVENTORY);
		downMap.put(InputButton.LOOK,		InputCommand.INTENT_LOOK);
		
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandMap#get
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton, boolean)
	 */
	@Override
	public InputCommand get(InputButton button, boolean wasRelease) {
		if (wasRelease) {
			if (upMap.containsKey(button)) {
				return upMap.get(button);
			} else {
				return null;
			}
		} else {
			if (downMap.containsKey(button)) {
				return downMap.get(button);
			} else {
				return null;
			}
		}
	}
	
}
