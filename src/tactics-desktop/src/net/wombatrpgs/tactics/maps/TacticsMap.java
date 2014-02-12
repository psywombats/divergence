/**
 *  TacticsMap.java
 *  Created on Feb 12, 2014 3:09:32 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.maps;

import net.wombatrpgs.mgne.maps.Level;

/**
 * A map that gets created from a normal map for a tactics battle.
 */
public class TacticsMap {
	
	protected Level map;
	
	/**
	 * Creates a new tactics map based on an existing map. Does not populate it
	 * with units or anything like that. Presumably the hero is currentl on the
	 * level.
	 * @param	map				The map the battle will take place on
	 */
	public TacticsMap(Level map) {
		this.map = map;
	}

}
