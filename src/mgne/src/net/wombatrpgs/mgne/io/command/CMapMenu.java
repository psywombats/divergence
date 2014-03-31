/**
 *  CMapScene.java
 *  Created on Jan 22, 2014 8:24:35 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.command;

import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.InputEvent.EventType;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Command map for menus and UI.
 */
public class CMapMenu extends EasyCommandMap {

	/**
	 * Creates the command map and includes default commands.
	 */
	public CMapMenu() {
		super(true);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.command.EasyCommandMap#initBindings()
	 */
	@Override
	protected void initBindings() {
		bindings.put(new InputEvent(InputButton.BUTTON_A,		EventType.PRESS),	InputCommand.UI_CONFIRM);
		bindings.put(new InputEvent(InputButton.BUTTON_B,		EventType.PRESS),	InputCommand.UI_CANCEL);
		bindings.put(new InputEvent(InputButton.BUTTON_START,	EventType.PRESS),	InputCommand.UI_CANCEL);
		
		bindings.put(new InputEvent(InputButton.UP,				EventType.PRESS),	InputCommand.MOVE_UP);
		bindings.put(new InputEvent(InputButton.DOWN,			EventType.PRESS),	InputCommand.MOVE_DOWN);
		bindings.put(new InputEvent(InputButton.LEFT,			EventType.PRESS),	InputCommand.MOVE_LEFT);
		bindings.put(new InputEvent(InputButton.RIGHT,			EventType.PRESS),	InputCommand.MOVE_RIGHT);
	}

}
