/**
 *  FlagStatValue.java
 *  Created on Feb 28, 2014 2:40:56 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.rpg.data;

/**
 * Struct for flag stats. Stores how many hits but has an equality operator
 * for ease of use.
 */
public class FlagStatValue {
	
	public final int count;
	
	/** @param count The initial count to start with */
	public FlagStatValue(int count) { this.count = count; }
	
	/** Creates a new value at 0 */
	public FlagStatValue() { this(0); }
	
	/** Checks if this flag is set */
	public boolean on() { return count > 0; }
	
}
