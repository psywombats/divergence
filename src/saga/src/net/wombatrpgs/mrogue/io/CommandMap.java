/**
 *  CommandMap.java
 *  Created on Nov 23, 2012 3:47:12 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import net.wombatrpgs.mrogueschema.io.data.InputButton;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

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
	 * Creates and initializes a new command map.
	 */
	public CommandMap() {
		
	}
	
	/**
	 * Translates from input button to input command.
	 * @param	button			The virtual button that was pressed
	 * @param	wasRelease		True if this was a release of a button
	 * @return					The command that button indicates
	 */
	public abstract InputCommand get(InputButton button, boolean wasRelease);
	
}
