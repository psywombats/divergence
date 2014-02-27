/**
 *  CMapTactics.java
 *  Created on Feb 14, 2014 4:36:14 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.io;

import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.InputEvent.EventType;
import net.wombatrpgs.mgne.io.command.EasyCommandMap;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Commands for selecting a (ortho) direction.
 */
public class CMapDirSelect extends EasyCommandMap {

	/**
	 * Constructs a new command map that includes global bindings.
	 */
	public CMapDirSelect() {
		super(true);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.command.EasyCommandMap#initBindings()
	 */
	@Override
	protected void initBindings() {
		
		bindings.put(new InputEvent(InputButton.BUTTON_B,		EventType.PRESS),	InputCommand.UI_CANCEL);
		
		bindings.put(new InputEvent(InputButton.UP,				EventType.PRESS),	InputCommand.MOVE_UP);
		bindings.put(new InputEvent(InputButton.DOWN,			EventType.PRESS),	InputCommand.MOVE_DOWN);
		bindings.put(new InputEvent(InputButton.LEFT,			EventType.PRESS),	InputCommand.MOVE_LEFT);
		bindings.put(new InputEvent(InputButton.RIGHT,			EventType.PRESS),	InputCommand.MOVE_RIGHT);
		
	}

}
