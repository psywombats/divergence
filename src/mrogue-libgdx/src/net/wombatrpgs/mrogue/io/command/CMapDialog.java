/**
 *  InventoryCommandMap.java
 *  Created on Oct 21, 2013 2:11:16 AM for project mrogue-libgdx
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
 * The command map for the inventory menu.
 */
public class CMapDialog extends CommandMap {
	
	protected Map<InputButton, InputCommand> downMap;
	
	/**
	 * Creates a new command map and sets some buttans.
	 */
	public CMapDialog() {
		downMap = new HashMap<InputButton, InputCommand>();
		
		downMap.put(InputButton.RIGHT,		InputCommand.CURSOR_RIGHT);
		downMap.put(InputButton.LEFT,		InputCommand.CURSOR_LEFT);
		downMap.put(InputButton.UP,			InputCommand.CURSOR_UP);
		downMap.put(InputButton.DOWN,		InputCommand.CURSOR_DOWN);
		
		downMap.put(InputButton.DIR_E,		InputCommand.CURSOR_RIGHT);
		downMap.put(InputButton.DIR_W,		InputCommand.CURSOR_LEFT);
		downMap.put(InputButton.DIR_N,		InputCommand.CURSOR_UP);
		downMap.put(InputButton.DIR_S,		InputCommand.CURSOR_DOWN);
		
		downMap.put(InputButton.BUTTON_1,	InputCommand.INTENT_CONFIRM);
		downMap.put(InputButton.BUTTON_2,	InputCommand.INTENT_CANCEL);
		downMap.put(InputButton.MENU,		InputCommand.INTENT_CANCEL);
		downMap.put(InputButton.TAB,		InputCommand.INTENT_CANCEL);
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandMap#get
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton, boolean)
	 */
	@Override
	public InputCommand get(InputButton button, boolean wasRelease) {
		if (wasRelease) {
			return null;
		} else {
			if (downMap.containsKey(button)) {
				return downMap.get(button);
			} else {
				return null;
			}
		}
	}

}
