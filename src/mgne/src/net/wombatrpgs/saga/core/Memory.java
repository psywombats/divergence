/**
 *  Memory.java
 *  Created on Jan 22, 2014 8:36:57 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.HashMap;
import java.util.Map;

/**
 * I don't know, all of the switches and variables for the game? It's meant to
 * be saved along with the hero and level to keep track of things. Like how RM
 * stored its save games: party progress, plus all the F9 stuff. Well we'll
 * store the current stuff too!
 * 
 * This kind of neeeds to be fleshed out and made to persist.
 */
public class Memory {
	
	protected Map<String, Boolean> switches;
	
	/**
	 * Creates a new memory holder! This is great! It should also probably only
	 * be called from SGlobal.
	 */
	public Memory() {
		switches = new HashMap<String, Boolean>();
	}
	
	/**
	 * Sets a certain switch on or off.
	 * @param	name			The name of the switch to toggle
	 * @param	value			The value to toggle to (on=true, off=false)
	 */
	public void setSwitch(String name, boolean value) {
		switches.put(name, value);
	}
	
	/**
	 * Determines if a certain switch is on or off. If the switch hasn't been
	 * touched yet, it's assumed that the switch is off.
	 * @param	name			The name of the switch to check
	 * @return					The value of the switch (on=true, off=false)
	 */
	public boolean getSwitch(String name) {
		Boolean val = switches.get(name);
		return (val == null) ? false : val;
	}

}
