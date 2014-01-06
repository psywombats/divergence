/**
 *  SplashCommandMap.java
 *  Created on Oct 23, 2013 2:56:10 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io.command;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.saga.io.CommandMap;
import net.wombatrpgs.sagaschema.io.data.InputButton;
import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * Map for title and game over.
 */
public class CMapSplash extends CommandMap {
	
	protected Map<InputButton, InputCommand> downMap;

	/**
	 * Creates a new command map.
	 */
	public CMapSplash() {
		downMap = new HashMap<InputButton, InputCommand>();
		
		downMap.put(InputButton.BUTTON_1,	InputCommand.INTENT_CONFIRM);
		
		downMap.put(InputButton.BUTTON_2,	InputCommand.INTENT_QUIT);
		downMap.put(InputButton.MENU,		InputCommand.INTENT_QUIT);
	}

	/**
	 * @see net.wombatrpgs.saga.io.CommandMap#get
	 * (net.wombatrpgs.sagaschema.io.data.InputButton, boolean)
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
