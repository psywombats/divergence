/**
 *  CommandMap.java
 *  Created on Nov 23, 2012 3:47:12 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io;

import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * Okay, this is a little weird, but this maps virtual button presses to command
 * intentions. Something like if you press the virtual A-button, this interprets
 * that as a confirm. However, in most games this only exists as a single blob
 * and never changes. This is here so that multiple maps can be plug-and-played
 * for testing purposes. A default implementation should be kicking around
 * somewhere.<br><br>
 * 
 * Also of note: these should be specific to a context, so the command map for
 * menus shouldn't be the same as gameplay, etc.<br><br>
 * 
 * As of 2013-10-20, no longer operates on a listener basis. Instead, does
 * translation like a normal map.
 */
public abstract class CommandMap {
	
	/**
	 * Translates from input button to input command.
	 * @param	event			The virtual button event that triggered
	 * @return					The command that the event indicates if the
	 * 							event is meaningful, null otherwise
	 */
	public abstract InputCommand parse(InputEvent event);
	
}
