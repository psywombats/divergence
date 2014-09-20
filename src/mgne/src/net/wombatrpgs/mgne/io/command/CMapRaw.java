/**
 *  CMapRaw.java
 *  Created on Sep 19, 2014 8:31:47 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.command;

import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.InputEvent.EventType;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * For when this whole double mapping thing is overkill.
 */
public class CMapRaw extends EasyCommandMap {

	/**
	 * Creates a new raw cmap with global commands included.
	 */
	public CMapRaw() {
		super(true);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.command.EasyCommandMap#initBindings()
	 */
	@Override
	protected void initBindings() {
		bindings.put(new InputEvent(InputButton.BUTTON_A,		EventType.PRESS),	InputCommand.RAW_A);
		bindings.put(new InputEvent(InputButton.BUTTON_B,		EventType.PRESS),	InputCommand.RAW_B);
		
		bindings.put(new InputEvent(InputButton.BUTTON_START,	EventType.PRESS),	InputCommand.RAW_START);
		bindings.put(new InputEvent(InputButton.BUTTON_SELECT,	EventType.PRESS),	InputCommand.RAW_SELECT);
		
		bindings.put(new InputEvent(InputButton.UP,				EventType.PRESS),	InputCommand.RAW_UP);
		bindings.put(new InputEvent(InputButton.DOWN,			EventType.PRESS),	InputCommand.RAW_DOWN);
		bindings.put(new InputEvent(InputButton.LEFT,			EventType.PRESS),	InputCommand.RAW_LEFT);
		bindings.put(new InputEvent(InputButton.RIGHT,			EventType.PRESS),	InputCommand.RAW_RIGHT);
	}

}
