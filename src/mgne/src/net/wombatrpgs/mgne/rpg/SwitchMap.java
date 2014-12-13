/**
 *  SwitchMap.java
 *  Created on Dec 13, 2014 2:09:30 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple map from string to boolean. Meant to make serialization easier.
 */
public class SwitchMap {
	
	protected Map<String, Boolean> switches;
	
	/**
	 * Creates a new, blank switch map. All switches are set to false.
	 */
	public SwitchMap() {
		switches = new HashMap<String, Boolean>();
	}
	
	/**
	 * Sets a switch value in the map.
	 * @param	key				The name of the switch to set
	 * @param	value			The value to set it to
	 */
	public void put(String key, boolean value) {
		switches.put(key, value);
	}
	
	/**
	 * Gets the value of the named switch.
	 * @param	key				The name of the switch to get
	 * @return					The value of the switch
	 */
	public boolean get(String key) {
		return switches.get(key) == null ? false : switches.get(key);
	}

}
