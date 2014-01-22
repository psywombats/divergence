/**
 *  DefaultCommandMap.java
 *  Created on Nov 23, 2012 3:51:04 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io.command;

import net.wombatrpgs.saga.io.InputEvent;
import net.wombatrpgs.saga.io.InputEvent.EventType;
import net.wombatrpgs.sagaschema.io.data.InputButton;
import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * The default mapping of virtual keys to commands. This is a test map,
 * basically, and command maps probably need to be swapped in and out depending
 * on context.
 */
public class CMapGame extends EasyCommandMap {

	/**
	 * Constructs a new command map that includes global bindings.
	 */
	public CMapGame() {
		super(true);
	}

	/**
	 * @see net.wombatrpgs.saga.io.command.EasyCommandMap#initBindings()
	 */
	@Override
	protected void initBindings() {
		bindings.put(new InputEvent(InputButton.BUTTON_A,	EventType.PRESS),	InputCommand.WORLD_INTERACT);
		
		bindings.put(new InputEvent(InputButton.UP,			EventType.HOLD),	InputCommand.MOVE_UP);
		bindings.put(new InputEvent(InputButton.DOWN,		EventType.HOLD),	InputCommand.MOVE_DOWN);
		bindings.put(new InputEvent(InputButton.LEFT,		EventType.HOLD),	InputCommand.MOVE_LEFT);
		bindings.put(new InputEvent(InputButton.RIGHT,		EventType.HOLD),	InputCommand.MOVE_RIGHT);
	}
	
}
