/**
 *  Move.java
 *  Created on Dec 29, 2012 12:55:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import net.wombatrpgs.rainfall.maps.Level;

/**
 * Something the hero can do, usually. I don't think there's really much more to
 * say on the matter. Performs some action on a map.
 */
public interface Actionable {
	
	/**
	 * Perform some action on the map.
	 * @param 	map			The map to perform the action on
	 */
	public void act(Level map);
}
