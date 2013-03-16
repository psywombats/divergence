/**
 *  SceneCommandMap.java
 *  Created on Feb 4, 2013 4:27:33 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.rainfallschema.io.data.InputButton;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * Map used when cutscenes are in play.
 */
// TODO: shift this and all other maps to database
public class SceneCommandMap extends CommandMap {
	
	// shitty solution - every key is mapped to one command
	private Map<InputButton, InputCommand> downMap;
	
	/**
	 * Creates and initializes the cutscene command map. Should be called new
	 * for each command? eugh
	 */
	public SceneCommandMap() {
		downMap = new HashMap<InputButton, InputCommand>();
		
		downMap.put(InputButton.BUTTON_1, 	InputCommand.INTENT_CONFIRM);
		downMap.put(InputButton.BUTTON_2, 	InputCommand.INTENT_CANCEL);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.rainfallschema.io.data.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		if (downMap.containsKey(button)) {
			this.signal(downMap.get(button));
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.rainfallschema.io.data.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		// fuck it
	}

}
