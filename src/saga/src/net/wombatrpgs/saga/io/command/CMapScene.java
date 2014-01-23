/**
 *  CMapScene.java
 *  Created on Jan 22, 2014 8:24:35 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io.command;

import net.wombatrpgs.saga.io.InputEvent;
import net.wombatrpgs.saga.io.InputEvent.EventType;
import net.wombatrpgs.sagaschema.io.data.InputButton;
import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * Command map for cutscenes.
 */
public class CMapScene extends EasyCommandMap {

	/**
	 * Creates the command map and includes default commands.
	 */
	public CMapScene() {
		super(true);
	}

	/**
	 * @see net.wombatrpgs.saga.io.command.EasyCommandMap#initBindings()
	 */
	@Override
	protected void initBindings() {
		bindings.put(new InputEvent(InputButton.BUTTON_A, EventType.PRESS), InputCommand.UI_CONFIRM);
	}

}
