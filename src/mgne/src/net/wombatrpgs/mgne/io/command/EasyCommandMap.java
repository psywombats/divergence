/**
 *  EasyCommandMap.java
 *  Created on Jan 22, 2014 1:36:15 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.command;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.InputEvent.EventType;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * A command map with some lists and things filled out.
 */
public abstract class EasyCommandMap extends CommandMap {
	
	protected Map<InputEvent, InputCommand> bindings;
	
	/**
	 * Initializes a new easy map. Tells the children to fill in the bindings.
	 * @param	includeGlobals	True to include game-wide UI commands in the
	 * 							bindings, things like "fullscreen"
	 */
	public EasyCommandMap(boolean includeGlobals) {
		bindings = new HashMap<InputEvent, InputCommand>();
		initBindings();
		if (includeGlobals) {
			initGlobals();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandMap#parse
	 * (net.wombatrpgs.mgne.io.InputEvent)
	 */
	@Override
	public final InputCommand parse(InputEvent event) {
		if (event.type == EventType.CHARACTER) {
			return parseCharacter(event.c);
		} else {
			return bindings.get(event);
		}
	}
	
	/**
	 * Parse a character being entered. Usually return null unless some other
	 * processing to a command is required.
	 * @param	character		The character that was entered
	 * @return					True if the char was processed, else false
	 */
	protected InputCommand parseCharacter(char character) {
		return null;
	}

	/**
	 * The subclasses should add things to the bindings list here. Called from
	 * the constructor automatically.
	 */
	protected abstract void initBindings();
	
	/**
	 * The same thing, but this is for things that are shared across screens and
	 * contexts, such as fiddling with the window.
	 */
	protected final void initGlobals() {
		bindings.put(new InputEvent(InputButton.FULLSCREEN, EventType.PRESS),
				InputCommand.GLOBAL_FULLSCREEN);
	}
}
